package Screens;

import OGRacerGame.OGRacerGame;
import MathHelpers.Util;
import Rendering.CarRenderer;
import Rendering.RenderSegment;
import Rendering.SpritesRenderer;
import Rendering.SunShade;
import Road.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class GameScreen extends ScreenAdapter implements IInputHandler{
    private boolean multiplayer;

    public static ArrayList<RoadPart> roadBuilders = new ArrayList<>();

    private Viewport viewport;

    protected Stage stage;

    private SpriteBatch batch;

    private ShapeRenderer renderer;

    private static Segment[] segments;

    private int roadWidth = 2000;

    private static int segmentLenght = 200;

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
    private final float playerMaxSpeed = segmentLenght;
    private final float accel = playerMaxSpeed/5;
    private final float offRoadDecel = -playerMaxSpeed/2;
    private final float offRoadLimit = playerMaxSpeed/4;
    private float centrifugal = 0.3f;
    private float speedPercent = playerSpeed/playerMaxSpeed;
    private float dx = 0;
    private double cameraPosition = 0;
    private float resolution = Gdx.graphics.getHeight()/480;
    private float sunOffset = 0;
    private double fogDensity = drawDistance/20;
    private boolean newCarsToPlace = false;
    private Car[] newCars;
    private Car[] oldCars;


    public GameScreen() {
        multiplayer=false;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        segments = RoadBuilder.resetRoad(segmentsCount,segmentLenght);
        trackLenght = segments.length*segmentLenght;
        setNewCars(RoadBuilder.createCarArr(segmentsCount));
    }
    public GameScreen(boolean multiplayer) {
        this.multiplayer=multiplayer;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        segments=RoadBuilder.resetRoad(roadBuilders.toArray(RoadPart[]::new));
        trackLenght = segments.length*segmentLenght;
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT  );
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderSegments(renderer);
        if(newCarsToPlace){
            if(oldCars!=null){
                for (Car c:oldCars) {
                    c.remove();
                }
            }
            if(newCars!=null){
                for (Car c:newCars) {
                    c.place();
                }
                oldCars = newCars;
            }
            newCarsToPlace = false;
        }
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
        renderer.dispose();
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
        sunOffset += dx*speedPercent;

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
        }
        SunShade.sun(r,batch,sunOffset,maxy);
        Segment s;
        for(int n = (drawDistance-1) ; n > 0 ; n--) {
            s = segments[((baseSegment.getIndex() + n) % segments.length)];
            //cars
            if(s.getCars()!=null){
                for(int q=0;q<s.getCars().size();q++){
                    Car car = s.getCars().get(q);
                    double spriteScale = Util.interpolate(s.getP1().getScreen().getScale(),s.getP2().getScreen().getScale(),0.5f);
                    float spriteX = (float) (Util.interpolate(s.getP1().getScreen().getX(),s.getP2().getScreen().getX(),0.5f)+(spriteScale*0.5f*roadWidth*Gdx.graphics.getWidth()/2));
                    float spriteY = (float) (Util.interpolate(s.getP1().getScreen().getY(),s.getP2().getScreen().getY(),0.5f));
                    SpritesRenderer.render(batch,resolution,roadWidth,car.getCs().getT(),spriteScale,spriteX,spriteY,-0.5f,-1f ,s.getClip());
                }
            }
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

    private Segment findSegment(double p) {
        return segments[(int) (Math.floor(p / segmentLenght) % segments.length)];
    }

    private void updatePosition(float delta) {
        speedPercent=playerSpeed/playerMaxSpeed;
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
    public void setNewCars(Car[] cars){
        newCars = cars;
        newCarsToPlace = true;
    }

    public static Segment[] getSegments() {return segments;}

    public static int getSegmentLenght() {return segmentLenght;}
}
