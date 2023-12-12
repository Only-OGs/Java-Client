package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import MathHelpers.Util;
import Rendering.CarRenderer;
import Rendering.RenderSegment;
import Rendering.SpritesRenderer;
import Rendering.SunShade;

import Road.*;
import Screens.MenuArea.LobbyMenu;
import Screens.MenuArea.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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
    private int backgroundWitdh = 500;

    private int backgroundHeight = 500;

    Label timeLabel = new Label("", Constants.buttonSkin);
    Label lastLapTimeLabel = new Label("", Constants.buttonSkin);
    Label fastestTimeLabel = new Label("", Constants.buttonSkin);
    Label speedLabel = new Label("", Constants.buttonSkin);

    Label exitBackground = new Label("", Constants.buttonSkin);
    TextButton exitResume = new TextButton("WEITER", Constants.buttonSkin);
    TextButton exitLeave = new TextButton("VERLASSEN", Constants.buttonSkin);

    //TEST Variables

    private int roadWidth = 2000;

    private static int segmentLenght = 200;

    private int lanes = 3;

    private int segmentsCount=600;

    private int trackLength;

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
    private double lastCameraPosition = 0;
    private float resolution = Gdx.graphics.getHeight()/480;
    private float sunOffset = 0;
    private double fogDensity = drawDistance/20;
    private boolean newCarsToPlace = false;
    private Car[] newCars;
    private Car[] oldCars;

    private float timer = 0;
    private float lastLapTime = 0;
    private float fastestLapTime = 0;

    public GameScreen() {
        multiplayer=false;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        segments = RoadBuilder.resetRoad(segmentsCount,segmentLenght);
        trackLength = segments.length*segmentLenght;
        setNewCars(RoadBuilder.createCarArr(segmentsCount));

        setupHUD(stage);
    }
    public GameScreen(boolean multiplayer) {
        this.multiplayer=multiplayer;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        segments=RoadBuilder.resetRoad(roadBuilders.toArray(RoadPart[]::new));
        trackLength = segments.length*segmentLenght;
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
        while (result >= trackLength)
            result -= trackLength;
        while (result < 0)
            result += trackLength;
        lastCameraPosition = cameraPosition;
        cameraPosition=result;
        updateHUD();
        updatePosition(delta);

        stage.draw();

        timer += delta;
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
                segmentLoopedValue= trackLength;
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
                    float spriteX = (float) (Util.interpolate(s.getP1().getScreen().getX(),s.getP2().getScreen().getX(),0.5f)+(spriteScale*car.getCs().getOffset()*roadWidth*Gdx.graphics.getWidth()/2));
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

        //Exit Screen
        exitBackground.setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/1.5f);
        exitBackground.setPosition(Gdx.graphics.getWidth()/2f - exitBackground.getWidth()/2,
                                   Gdx.graphics.getHeight()/2f - exitBackground.getHeight()/2);
        exitBackground.getStyle().background = new Image(new Texture("sprites/exitBackground.png")).getDrawable();
        exitBackground.setVisible(false);
        stage.addActor(exitBackground);

        exitResume.setSize(exitBackground.getWidth()/2f, exitBackground.getHeight()/10);
        exitResume.setPosition(Gdx.graphics.getWidth()/2f - exitResume.getWidth()/2,
                Gdx.graphics.getHeight()/2f + exitResume.getHeight());
        exitResume.setVisible(false);
        exitResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                OGRacerGame.getInstance().isRunning = true;
                exitBackground.setVisible(false);
                exitResume.setVisible(false);
                exitLeave.setVisible(false);
            }
        });
        stage.addActor(exitResume);

        exitLeave.setSize(exitBackground.getWidth()/2f, exitBackground.getHeight()/10);
        exitLeave.setPosition(Gdx.graphics.getWidth()/2f - exitLeave.getWidth()/2,
                Gdx.graphics.getHeight()/2f - exitLeave.getHeight() * 2);
        exitLeave.setVisible(false);
        exitLeave.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                OGRacerGame.getInstance().isRunning = true;
                exitBackground.setVisible(false);
                exitResume.setVisible(false);
                exitLeave.setVisible(false);
                OGRacerGame.getInstance().setScreen(new MainMenu());
                //Disconnect bei Multiplayer
            }
        });
        stage.addActor(exitLeave);
    }

    private void updateHUD() {
        if(cameraPosition < lastCameraPosition) {
            fastestLapTime = lastLapTime > 0 ? Math.min(fastestLapTime, timer) : timer;
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
        Segment playerSegment = findSegment(cameraPosition+playerZ);
        speedPercent=playerSpeed/playerMaxSpeed;
        dx = delta * 2 * (playerSpeed/playerMaxSpeed);

        // Langsamer auf Offroad
        if (((playerX < -1) || (playerX > 1))) {
            if((playerSpeed > offRoadLimit)) {
                playerSpeed = playerSpeed + (offRoadDecel * delta);
                playerSpeed = (int) Util.limit(playerSpeed, 0, playerMaxSpeed);
            }
            checkSpriteCollision(playerSegment);

        }

        double scale = playerSegment.getP1().getScreen().getScale();
        double playerW = CarRenderer.tS.getWidth() * scale;

        if(playerSegment.getCars() != null) {
            for(int i = 0 ; i < playerSegment.getCars().size() ; i++) {
                Car car = playerSegment.getCars().get(i);
                double carW = car.getCs().getT().getWidth() * scale;
                //if (speed > car.speed) {
                if (Util.overlap(playerX, playerW, car.getCs().getOffset() -carW*2 , carW * 3.5, 0.8)) {
                    playerSpeed = playerMaxSpeed/5;//car.speed * (car.speed/speed);
                    cameraPosition = Util.increase((int) car.getCs().getZ(), (int) -playerZ, trackLength);
                    break;
                }
                //}
            }
        }


        //Beschleunigen | Bremsen | Nach Links | Nach Rechts
        checkInput(OGRacerGame.getInstance(), delta);
    }

    private void checkSpriteCollision(Segment playerSegment) {
        if(playerSegment.getSprites() == null) {
            return;
        }
        for(int i = 0 ; i < playerSegment.getSprites().length ; i++) {

            double scale = playerSegment.getP1().getScreen().getScale();

            CustomSprite sprite = playerSegment.getSprites()[i];
            double spriteW = sprite.getT().getWidth() * scale;
            double playerW = CarRenderer.tS.getWidth() * scale;
            if (Util.overlap(playerX, playerW, sprite.getOffset(), spriteW*4, 0.5f) ||
                    Util.overlap(playerX, playerW, sprite.getOffset()-spriteW*4, spriteW*4, 0.5f)) {
                playerSpeed = playerMaxSpeed/5;
                cameraPosition = Util.increase(playerSegment.getP1().getWorld().getZ(), (int) -playerZ, trackLength);
                break;
            }
        }
    }

    @Override
    public void checkInput(OGRacerGame game, float dt) {
        // Pausieren/Fortfahren des Spiels
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.isRunning = false;
            //Menü anzeigen
            exitBackground.setVisible(true);
            exitResume.setVisible(true);
            exitLeave.setVisible(true);
        }
        // Wenn das Spiel pausiert ist, sollen keine Eingaben zum steuern des Autos abgefragt werden
        if(!game.isRunning) {
            playerSpeed = playerSpeed + (-accel * dt);
            playerSpeed = (int)Util.limit(playerSpeed, 0, playerMaxSpeed);
            playerX = playerX - (dx * playerSpeed/playerMaxSpeed * findSegment(cameraPosition+playerZ).getCurve() * centrifugal);
            return;
        }


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
