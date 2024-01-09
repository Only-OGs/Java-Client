package Rendering;

import Helpers.Util;
import Road.Car;
import Road.CustomSprite;
import Road.Helper.Segment;
import Screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Diese Klasse ist für das Zeichnen der Spielinhalte zuständig und benutzt die verschiedenen Rendererklassen dafür.
 * Weiterhin werden Informationen die Hauptsächlich fürs Rendern wichtig sind hier gespeichert.
 */
public class Render {
    private final int drawDistance = 200;
    private final float fogDensity = drawDistance/20f;
    private final int segmentLength = 200;
    private final int lanes = 3;
    private final int roadWidth = 2000;
    private final float resolution = Gdx.graphics.getHeight() / 480f;
    private final int FOV = 100;
    private final float cameraDepth = (float) (1f / Math.tan((FOV / 2f) * Math.PI / 180f));
    private final int cameraHeight = 1000;
    private final float playerZ = cameraHeight * cameraDepth;
    private final int playerMaxSpeed = segmentLength*60;
    private float skyOffset = 0;
    private float hillOffset = 0;
    private float treeOffset = 0;
    private final Segment[] segments;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    GameScreen gameScreen;

    public Render(Segment[] segments, SpriteBatch batch, ShapeRenderer shapeRenderer, GameScreen gameScreen) {
        this.segments = segments;
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.gameScreen = gameScreen;
    }

    /**
     * Hauptmethode holt sich noch fehlende Werte von der GameScreen Klasse und nutzt diese dann, zuerst der Hintergrund,
     * dann die Segmente, nachdem ein Segment gemalt wurde, werden, falls vorhanden Autos und Hindernisse gezeichnet.
     * soll das Segment dem playerSegment entsprechen wird dieser auch gezeichnet
     * */
    public void render(){

        double cameraPosition = gameScreen.getCameraPosition();
        double lastCameraPosition = gameScreen.getLastCameraPosition();

        Segment playerSegment = GameScreen.findSegment(cameraPosition + playerZ);
        float playerPercent = (float) Util.percentRemaining((float) cameraPosition + playerZ, segmentLength);
        int playerY = (int) Util.interpolate(playerSegment.getP1().getWorld().getY(), playerSegment.getP2().getWorld().getY(), playerPercent);


        BackgroundRenderer.renderBackground(batch,skyOffset,hillOffset,treeOffset,playerY,resolution);

        renderSegments(cameraPosition,playerPercent,playerY);

        Gdx.gl.glDisable(GL20.GL_BLEND);
        skyOffset = (float) Util.increase(skyOffset, (0.001f * playerSegment.getCurve() * (cameraPosition - lastCameraPosition) / segmentLength), 1);
        hillOffset = (float) Util.increase(skyOffset, (0.002f * playerSegment.getCurve() * (cameraPosition - lastCameraPosition) / segmentLength), 1);
        treeOffset = (float) Util.increase(skyOffset, (0.003f * playerSegment.getCurve() * (cameraPosition - lastCameraPosition) / segmentLength), 1);
    }


    private void renderSegments(double cameraPosition,float playerPercent,int playerY){
        float playerX = gameScreen.getPlayerX();
        float playerSpeed = gameScreen.getPlayerSpeed();

        Segment baseSegment = GameScreen.findSegment(cameraPosition);
        Segment playerSegment = GameScreen.findSegment(cameraPosition + playerZ);
        float basePercent = (float) Util.percentRemaining((float) cameraPosition, segmentLength);

        int maxy = Gdx.graphics.getHeight();
        float x = 0;                                            //Akkumulierter seitlicher versatz der Segmente
        float dx = 0f - (baseSegment.getCurve() * basePercent); // hilft die Versätzung zu speichern und berechnen

        Segment segment;
        int segmentLoopedValue;


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //Render der Segmente//
        for (int i = 0; i < drawDistance; i++) {
            segment = segments[(baseSegment.getIndex() + i) % segments.length];
            segment.setLooped(segment.getIndex() < baseSegment.getIndex());
            segment.setFog(1 - Util.exponentialFog(Float.parseFloat(String.valueOf(i)) / drawDistance, fogDensity));
            segment.setClip(maxy);

            if (segment.isLooped()) {
                segmentLoopedValue = gameScreen.getTrackLength();
            } else {
                segmentLoopedValue = 0;
            }

            Util.project(segment.getP1(),
                    (int) ((playerX * roadWidth) - x),
                    playerY + cameraHeight,
                    (int) cameraPosition - segmentLoopedValue,
                    cameraDepth,
                    Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight(),
                    roadWidth);

            Util.project(segment.getP2(),
                    (int) ((playerX * roadWidth) - x - dx),
                    playerY + cameraHeight,
                    (int) cameraPosition - segmentLoopedValue,
                    cameraDepth,
                    Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight(),
                    roadWidth);

            x += dx;   //nach jeden segment steigt oder singt die Versätzung der Segmente abhängig von der Stärke der Kurve
            dx += segment.getCurve();

            if ((segment.getP1().getCamera().getZ() <= cameraDepth) ||
                    (segment.getP2().getScreen().getY() >= maxy) ||
                    (segment.getP2().getScreen().getY() >= segment.getP1().getScreen().getY())) {
                continue;
            }
            RenderSegment.render(shapeRenderer, Gdx.graphics.getWidth(), lanes,
                    segment.getP1().getScreen().getX(),
                    segment.getP1().getScreen().getY(),
                    segment.getP1().getScreen().getW(),
                    segment.getP2().getScreen().getX(),
                    segment.getP2().getScreen().getY(),
                    segment.getP2().getScreen().getW(),
                    segment.getFog(),
                    segment.getColor(),
                    segment
            );
            maxy = segment.getP1().getScreen().getY();
        }
        Segment s;
        for (int n = (drawDistance - 1); n > 0; n--) {
            s = segments[((baseSegment.getIndex() + n) % segments.length)];
            // Rendern der Autos
            if (s.getCars() != null) {
                for (int q = 0; q < s.getCars().size(); q++) {
                    Car car = s.getCars().get(q);
                    double spriteScale = Util.interpolate(s.getP1().getScreen().getScale(), s.getP2().getScreen().getScale(), car.getPercent());
                    float spriteX = (float) (Util.interpolate(s.getP1().getScreen().getX(), s.getP2().getScreen().getX(), car.getPercent()) + (spriteScale * car.getCs().getOffset() * roadWidth * Gdx.graphics.getWidth() / 2));
                    float spriteY = (float) (Util.interpolate(s.getP1().getScreen().getY(), s.getP2().getScreen().getY(), car.getPercent()));
                    SpritesRenderer.render(batch, resolution, roadWidth, car.getCs().getT(), spriteScale, spriteX, spriteY, -0.5f, -1f, s.getClip());
                }
            }
            //Rendern der Sprites
            if (s.getSprites() != null) {
                for (int q = 0; q < s.getSprites().length; q++) {
                    CustomSprite cs = s.getSprites()[q];
                    double spriteScale = s.getP1().getScreen().getScale();
                    float spriteX = (float) (s.getP1().getScreen().getX() + (spriteScale * cs.getOffset() * roadWidth * Gdx.graphics.getWidth() / 2));
                    float spriteY = s.getP1().getScreen().getY();
                    SpritesRenderer.render(batch, resolution, roadWidth, cs.getT(), spriteScale, spriteX, spriteY, cs.getOffset(), -1f, s.getClip());
                }
            }
            //Rendern des Spielers
            if (s == playerSegment) {
                CarRenderer.renderPlayerCar(batch, playerSegment, resolution, roadWidth, playerSpeed / playerMaxSpeed, cameraDepth / playerZ, Gdx.graphics.getWidth() / 2,
                        (int) ((Gdx.graphics.getHeight() / 2) - (cameraDepth / playerZ * Util.interpolate((int) playerSegment.getP1().getCamera().getY(), (int) playerSegment.getP2().getCamera().getY(), playerPercent)) * Gdx.graphics.getHeight() / 2));

            }
        }
    }
}
