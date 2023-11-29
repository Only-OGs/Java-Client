package Screens;

import OGRacerGame.OGRacerGame;
import MathHelpers.Util;
import Rendering.CarRenderer;
import Rendering.RenderSegment;
import Rendering.SpritesRenderer;
import Rendering.SunShade;
import Road.CustomSprite;
import Road.RoadBuilder;
import Road.Segment;
import Root.StyleGuide;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Date;

public class GameScreen extends ScreenAdapter implements IInputHandler{

    private Viewport viewport;

    protected Stage stage;

    private SpriteBatch batch;

    private Texture background;

    private ShapeRenderer renderer;

    private int backgroundWitdh = 500;

    private int backgroundHeight = 500;

    Label timeLabel = new Label("", Constants.buttonSkin);
    Label lastLapTimeLabel = new Label("", Constants.buttonSkin);
    Label fastestTimeLabel = new Label("", Constants.buttonSkin);
    Label speedLabel = new Label("", Constants.buttonSkin);

    //TEST Variables
    private Segment[] segments;

    private int roadWidth = 2000;

    private int segmentLenght = 200;

    private int lanes = 3;

    private int segmentsCount=600;

    private int trackLenght;

    private int FOV = 100;

    private int cameraHeight = 1000;

    private float cameraDepth  = (float) (1/Math.tan((FOV/2)*Math.PI/180));

    private int drawDistance = 200;

    private float playerX = 0;

    private float playerZ = cameraHeight*cameraDepth;

    private float playerSpeed = 0;
    private final float playerMaxSpeed = 250;
    private final float accel = playerMaxSpeed/5;
    private final float offRoadDecel = -playerMaxSpeed/2;
    private final float offRoadLimit = playerMaxSpeed/4;
    private float centrifugal = 0.3f;
    private float speedPercent = playerSpeed/playerMaxSpeed;
    private float dx = 0;
    private double cameraPosition = 0;
    private double lastCameraPosition = 0;
    private float resolution = Gdx.graphics.getHeight()/480;
    private float sunOffset = 0;
    private double fogDensity = drawDistance/20;
    private float timer = 0;
    private float lastLapTime = 0;
    private float fastestLapTime = 0;

    public GameScreen() {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        segments = RoadBuilder.resetRoad(segmentsCount,segmentLenght);
        trackLenght = segments.length*segmentLenght;
        setupHUD(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT  );
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderSegments(renderer);
        double result = cameraPosition + playerSpeed;
        while (result >= trackLenght)
            result -= trackLenght;
        while (result < 0)
            result += trackLenght;
        lastCameraPosition = cameraPosition;
        cameraPosition=result;

        updatePosition(delta);
        updateHUD();
        stage.draw();

        timer += delta;
    }

    @Override
    public void dispose () {
        batch.dispose();
        background.dispose();
        stage.dispose();
    }

    public void renderSegments(ShapeRenderer r){
        Segment baseSegment = findSegment(cameraPosition);
        Segment playerSegment = findSegment(cameraPosition+playerZ);
        int maxy = Gdx.graphics.getHeight();
        float basePercent = (float) Util.percentRemaining((float) cameraPosition,segmentLenght);
        float playerPercent = (float) Util.percentRemaining((float) cameraPosition+playerZ,segmentLenght);
        int playerY = (int) Util.interpolate(playerSegment.getP1().getWorld().getY(),playerSegment.getP2().getWorld().getY(),playerPercent);
        float x = 0;                                                                                                    //Akkumulierter seitlicher versatz der Segmente
        float dx = 0f- (baseSegment.getCurve()*basePercent);                                                            // hilft die Versätzung zu speichern und berechnen
        sunOffset += dx;
        int renderedCounter=0;

        Segment segment;
        int segmentLoopedValue;

        for (int i=0;i<drawDistance;i++){
            segment = segments[(baseSegment.getIndex()+i)%segments.length];
            segment.setLooped(segment.getIndex()<baseSegment.getIndex());
            segment.setFog(1-Util.exponentialFog(Float.parseFloat(String.valueOf(i))/drawDistance,fogDensity));
            segment.setClip(maxy);

            if(segment.isLooped()){
                segmentLoopedValue=trackLenght;
            }else{
                segmentLoopedValue=0;
            }

            Util.project(segment.getP1(), (int) (((int)(playerX*roadWidth))-x),playerY+cameraHeight,(int)cameraPosition-segmentLoopedValue,cameraDepth,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),roadWidth);
            Util.project(segment.getP2(), (int) (((int)(playerX*roadWidth))-x-dx),playerY+cameraHeight,(int)cameraPosition-segmentLoopedValue,cameraDepth,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),roadWidth);

            x += dx;   //nach jeden segment steigt oder singt die Versätzung der Segmente abhängig von der Stärke der Kurve
            dx += segment.getCurve();

            if((segment.getP1().getCamera().getZ() <= cameraDepth) ||
                    (segment.getP2().getScreen().getY()>=maxy)||
                    (segment.getP2().getScreen().getY()>=segment.getP1().getScreen().getY())){
                continue;
            }
            RenderSegment.render(r,Gdx.graphics.getWidth(),lanes,
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
            maxy=segment.getP1().getScreen().getY();
            renderedCounter++;
        }
        SunShade.sun(r,batch,sunOffset,maxy);
        Segment s;
        for(int n = (drawDistance-1) ; n > 0 ; n--) {
            s = segments[((baseSegment.getIndex() + n) % segments.length)];
            if(s.getSprites()!=null){
                for(int q=0;q<s.getSprites().length;q++){
                    CustomSprite cs = s.getSprites()[q];
                    double spriteScale = s.getP1().getScreen().getScale();
                    float spriteX = (float) (s.getP1().getScreen().getX()+(spriteScale*cs.getOffset()*roadWidth*Gdx.graphics.getWidth()/2));
                    float spriteY = s.getP1().getScreen().getY();
                    SpritesRenderer.render(batch,resolution,roadWidth,cs.getT(),spriteScale,spriteX,spriteY,(cs.getOffset() < 0 ? -1f : 0f) ,-1f ,s.getClip());
                }
            }
            if(s==playerSegment){
                CarRenderer.renderPlayerCar(batch,playerSegment,resolution,roadWidth,playerSpeed/playerMaxSpeed,cameraDepth/playerZ,Gdx.graphics.getWidth()/2,
                        (int) ((Gdx.graphics.getHeight()/2)-(cameraDepth/playerZ*Util.interpolate((int) playerSegment.getP1().getCamera().getY(), (int) playerSegment.getP2().getCamera().getY(),playerPercent))*Gdx.graphics.getHeight()/2));

            }
        }
    }

    private void setupHUD(Stage stage) {
        Gdx.input.setInputProcessor(stage);
        int spacing = Gdx.graphics.getWidth() / 5;

        timeLabel.setBounds(25 + 0*spacing, Gdx.graphics.getHeight() - 50, 0, 0);
        stage.addActor(timeLabel);

        lastLapTimeLabel.setBounds(1*spacing - 25, Gdx.graphics.getHeight() - 50, 0, 0);
        stage.addActor(lastLapTimeLabel);

        fastestTimeLabel.setBounds(75 + 2*spacing, Gdx.graphics.getHeight() - 50, 0, 0);
        stage.addActor(fastestTimeLabel);

        speedLabel.setBounds(175 + 3*spacing, Gdx.graphics.getHeight() - 50, 0, 0);
        stage.addActor(speedLabel);
    }

    private void updateHUD() {
        if(cameraPosition < lastCameraPosition) {
            fastestLapTime = lastLapTime > 0 ? Math.min(lastLapTime, timer) : timer;
            lastLapTime = timer;
            timer = 0;
        }

        timeLabel.setText("Zeit:\n" + Util.formatTimer(timer));
        lastLapTimeLabel.setText("Letzte Runde:\n" + (lastLapTime > 0 ? Util.formatTimer(lastLapTime) : ""));
        fastestTimeLabel.setText("Schnelllste Runde:\n" + (fastestLapTime > 0 ? Util.formatTimer(fastestLapTime) : ""));
        speedLabel.setText("Geschwindigkeit:\n" + Util.formatSpeed(playerSpeed,playerMaxSpeed));
    }

    private Segment findSegment(double p) {
        return segments[(int) (Math.floor(p / segmentLenght) % segments.length)];
    }

    private void updatePosition(float delta) {
        dx = delta * 2 * (playerSpeed/playerMaxSpeed);
        //Beschleunigen | Bremsen | Nach Links | Nach Rechts
        checkInput(OGRacerGame.getInstance(), delta);
        // Langsamer auf Offroad
        if (((playerX < -1) || (playerX > 1)) && (playerSpeed > offRoadLimit)) {
            playerSpeed = playerSpeed + (offRoadDecel * delta);
            playerSpeed = (int)Util.limit(playerSpeed, 0, playerMaxSpeed);
        }
    }

    @Override
    public void checkInput(OGRacerGame game, float dt) {
        // Pausieren/Fortfahren des Spiels
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.isRunning = !game.isRunning;
            //Menü anzeigen
        }
        // Wenn das Spiel pausiert ist, sollen keine Eingaben zum steuern des Autos abgefragt werden
        // if(!game.isRunning) return;


		/*	Durch die Struktur ist es unmöglich
			gleichzeitig zu bremsen und zu beschleunigen bzw.
			gleichzeitig nach Links und nach Rechts zu fahren
		*/
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            //Beschleunigen
            playerSpeed = playerSpeed + (accel * dt);
            //Centrifugal
            playerX = playerX - (dx * playerSpeed/playerMaxSpeed * findSegment(cameraPosition+playerZ).getCurve() * centrifugal);
            playerX = Util.limit(playerX, -2, 2);

            playerSpeed = Util.limit(playerSpeed, 0, playerMaxSpeed);
        } else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            //Bremsen
            playerSpeed = playerSpeed + (-playerMaxSpeed * dt);
            playerSpeed = (int)Util.limit(playerSpeed, 0, playerMaxSpeed);
        } else {
            //Entschleunigen
            playerSpeed = playerSpeed + (-accel * dt);
            playerSpeed = (int)Util.limit(playerSpeed, 0, playerMaxSpeed);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            //Nach links fahren
            playerX = playerX - dx;
            playerX = Util.limit(playerX, -2, 2);
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            //Nach Rechts fahren
            playerX = playerX + dx;
            playerX = Util.limit(playerX, -2, 2);
        }
    }
}
