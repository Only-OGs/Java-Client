package Screens;

import OGRacerGame.OGRacerGame;
import MathHelpers.Util;
import Rendering.RenderSegment;
import Road.RoadBuilder;
import Road.Segment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter implements IInputHandler{

    private Viewport viewport;

    protected Stage stage;

    private SpriteBatch batch;

    private Texture background;

    private ShapeRenderer renderer;

    private int backgroundWitdh = 500;

    private int backgroundHeight = 500;


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

    private float playerSpeed = 10;
    private final float playerMaxSpeed = 250;
    private final float accel = playerMaxSpeed/5;
    private final float offRoadDecel = -playerMaxSpeed/2;
    private final float offRoadLimit = playerMaxSpeed/4;
    private float centrifugal = 3f;
    private float speedPercent = playerSpeed/playerMaxSpeed;
    private float dx = 0;

    private double cameraPosition = 0;


    public GameScreen() {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        background = new Texture("background/hills.png");
        renderer = new ShapeRenderer();
        segments = RoadBuilder.resetRoad(segmentsCount,segmentLenght);
        trackLenght = segments.length*segmentLenght;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        renderSegments(renderer);
        double result = cameraPosition + playerSpeed;
        while (result >= trackLenght)
            result -= trackLenght;
        while (result < 0)
            result += trackLenght;
        cameraPosition=result;

        updatePosition(delta);
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

        Segment segment;
        int segmentLoopedValue;

        for (int i=0;i<drawDistance;i++){
            segment = segments[(baseSegment.getIndex()+i)%segments.length];
            segment.setLooped(segment.getIndex()<baseSegment.getIndex());

            if(segment.isLooped()){
                segmentLoopedValue=trackLenght;
            }else{
                segmentLoopedValue=0;
            }

            Util.project(segment.getP1(), (int) (((int)(playerX*roadWidth))-x),playerY+cameraHeight,(int)cameraPosition-segmentLoopedValue,cameraDepth,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),roadWidth);
            Util.project(segment.getP2(), (int) (((int)(playerX*roadWidth))-x-dx),playerY+cameraHeight,(int)cameraPosition-segmentLoopedValue,cameraDepth,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),roadWidth);

            x += dx;   //nach jeden segment steigt oder singt die Versätzung der Segmente abhängig von der Stärke der Kurve
            dx += segment.getCurve();

            if((segment.getP1().getCamera().getZ() <= cameraDepth) || (segment.getP2().getScreen().getY()>=maxy)){
                continue;
            }
            RenderSegment.render(r,Gdx.graphics.getWidth(),lanes,
                    segment.getP1().getScreen().getX(),
                    segment.getP1().getScreen().getY(),
                    segment.getP1().getScreen().getW(),
                    segment.getP2().getScreen().getX(),
                    segment.getP2().getScreen().getY(),
                    segment.getP2().getScreen().getW(),
                    0,
                    segment.getColor()
            );
            maxy=segment.getP2().getScreen().getY();
        }
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

    /** [GameScreen] Fragt ab ob eine Taste gedruckt wurde/ist */
    @Override
    public void checkInput(OGRacerGame game, float dt) {
        // Pausieren/Fortfahren des Spiels
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.isRunning = !game.isRunning;
            //Menü anzeigen
        }
        // Wenn das Spiel pausiert ist, sollen keine Eingaben zum steuern des Autos abgefragt werden
        if(!game.isRunning) return;


		/*	Durch die Struktur ist es unmöglich
			gleichzeitig zu bremsen und zu beschleunigen bzw.
			gleichzeitig nach Links und nach Rechts zu fahren
		*/
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            //Beschleunigen
            playerSpeed = playerSpeed + (accel * dt);
            //Centrifugal
            playerX = playerX - (dx * speedPercent * findSegment(cameraPosition+playerZ).getCurve() * centrifugal);
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
            Segment playerSegment = findSegment(cameraPosition+playerZ);
            playerX = playerX - (dx * speedPercent * playerSegment.getCurve() * centrifugal);
            playerX = Util.limit(playerX, -2, 2);
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            //Nach Rechts fahren
            playerX = playerX + dx;
            playerX = Util.limit(playerX, -2, 2);
        }
    }
}
