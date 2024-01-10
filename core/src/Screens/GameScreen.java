package Screens;

import Connection.Client;
import Helpers.Constants;
import Helpers.Util;
import OGRacerGame.OGRacerGame;
import Rendering.AssetData;
import Rendering.Render;
import Road.*;
import Road.Helper.Segment;
import Root.StyleGuide;
import Screens.Menu.MenuArea.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    private final boolean multiplayer;
    private String userID = "";
    public static ArrayList<RoadPart> roadBuilders = new ArrayList<>();
    private final Viewport viewport;

    protected Stage stage;
    private final SpriteBatch batch;

    private final ShapeRenderer renderer;
    private final Render fullRenderer;
    private final Multiplayerfunktions multiplayerFunctions;
    private static Segment[] segments;

    private Label timeLabel = new Label("", Constants.buttonSkin);
    private Label lastLapTimeLabel = new Label("", Constants.buttonSkin);
    private Label fastestTimeLabel = new Label("", Constants.buttonSkin);
    private Label speedLabel = new Label("", Constants.buttonSkin);

    private Label lapLabel = new Label("", Constants.buttonSkin);

    private Label serverStatus = new Label("", Constants.buttonSkin, "title");


    private Label timerStartLabel = new Label("", Constants.buttonSkin, "title");

    private Image exitBackground = new Image(new Texture("sprites/exitBackground.png"));
    private TextButton exitResume = new TextButton("WEITER", Constants.buttonSkin);
    private TextButton exitLeave = new TextButton("VERLASSEN", Constants.buttonSkin);


    //TEST Variables


    private static int segmentLength = 200;

    private static int trackLength;

    private int FOV = 100;

    private static int cameraHeight = 1000;

    private float cameraDepth = (float) (1 / Math.tan((FOV / 2) * Math.PI / 180));


    private float playerX = 0;
    private float playerZ = cameraHeight * cameraDepth;

    private float playerSpeed = 0;
    private final float playerMaxSpeed = segmentLength * 60;
    private final float accel = playerMaxSpeed / 5;
    private final float offRoadDecel = -playerMaxSpeed / 2;
    private final float offRoadLimit = playerMaxSpeed / 4;
    private static final float centrifugal = 0.3f;
    private float dx;
    private double cameraPosition;
    private static double lastCameraPosition = 0;
    private boolean newCarsToPlace = false;
    private Car[] newCars;
    private float timer = 0;
    private float lastLapTime = 0;
    private float fastestLapTime = 0;

    private double camaeraPositionOld = 0;

    private Leaderboard leaderboard;

    private float timerToStart = 6;

    private boolean runSingleplayer;
    private boolean runMultiplayer;

    private int lap = 1;

    private boolean menuOpen = false;


    public GameScreen() {
        OGRacerGame.getInstance().isRunning = false;
        runSingleplayer = false;
        runMultiplayer = false;
        multiplayer = false;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        segments = RoadBuilder.resetRoad(600, segmentLength);
        RoadBuilder.fillSprites();
        trackLength = segments.length * segmentLength;
        setNewCars(RoadBuilder.createCarArr(600));
        setupTimerLabel();
        leaderboard = new Leaderboard(stage);
        setupHUD(stage);
        fullRenderer = new Render(segments, batch, renderer, this);
        multiplayerFunctions = new Multiplayerfunktions(this);
    }

    public GameScreen(String userID) {
        OGRacerGame.getInstance().isRunning = false;
        this.userID = userID;
        this.multiplayer = true;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        segments = RoadBuilder.resetRoad(roadBuilders.toArray(RoadPart[]::new));
        trackLength = segments.length * segmentLength;
        leaderboard = new Leaderboard(stage);
        setupTimerLabel();
        setupHUD(stage);
        fullRenderer = new Render(segments, batch, renderer, this);
        multiplayerFunctions = new Multiplayerfunktions(this);
    }


    /* Überprüft, ob wir im Mehrspieler oder Einzelspieler.
     * Wird ein Timer gesetzt und startet, das man sich bewegen kann.
     */
    private void startRace(float delta) {

        // Sorgt dafür das man erst fahren kann, wenn der Timer abgelaufen ist im Multiplayer
        if (Client.start && multiplayer && !runMultiplayer) {
            runMultiplayer = true;
            timerStartLabel.setText("");
            if (!menuOpen) OGRacerGame.getInstance().isRunning = true;

        } else if (!runMultiplayer && multiplayer) timerStartLabel.setText(Client.timerToStart);
        if (runMultiplayer && multiplayer) timer += delta;

        // Sorgt dafür das man erst fahren kann, wenn der Timer abgelaufen ist im Einzelspieler
        if (timerToStart <= 0 && !multiplayer && !runSingleplayer) {
            runSingleplayer = true;
            timerStartLabel.setText("");
            if (!menuOpen) OGRacerGame.getInstance().isRunning = true;

        } else if (!runSingleplayer && !multiplayer) startTimer(delta);
        if (runSingleplayer && !multiplayer) timer += delta;
    }

    // Setzt unsere Startpostion sowie die Sprites
    private void setupPositionData() {
        if (Client.startGame) {
            Client.startGame = false;
            multiplayerFunctions.startPosition();
            multiplayerFunctions.placeSprites();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fullRenderer.render();

        Gdx.gl.glEnable(GL20.GL_BLEND);

        if (Client.showLeaderboard) leaderboard.show();

        if (newCarsToPlace) {
            if (newCars != null) for (Car c : newCars) c.place();
            newCarsToPlace = false;
        }

        double result = cameraPosition + playerSpeed * Gdx.graphics.getDeltaTime();
        while (result >= trackLength) result -= trackLength;
        while (result < 0) result += trackLength;
        lastCameraPosition = cameraPosition;
        cameraPosition = result;

        if (!Client.connect && multiplayer) serverStatus.setText("Verbindung zum Server\n\nverloren");

        setupPositionData();
        updateHUD();
        updatePosition(delta);
        startRace(delta);

        OGRacerGame.movement = !(playerSpeed <= 0);

        stage.draw();

        if (multiplayer) {
            if (camaeraPositionOld != cameraPosition) {
                try {
                    Client.ingamePos(playerX, cameraPosition + playerZ);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            camaeraPositionOld = cameraPosition;

            if (Client.updatePos) {
                Client.updatePos = false;
                multiplayerFunctions.updateCars(newCars);
            }
        } else CarController.kiMoveBot(newCars);
    }

    private void startTimer(float delta) {
        timerToStart -= delta;
        if (timerToStart < 1) {
            timerStartLabel.setColor(0.3f, 1f, 0.5f, 1f);
            timerStartLabel.setText("GO");
        } else timerStartLabel.setText((int) timerToStart);
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        stage.dispose();
    }

    private void setupTimerLabel() {

        timerStartLabel.setBounds(0, stage.getHeight() / 2.5f, Gdx.graphics.getWidth(), 100);
        timerStartLabel.setAlignment(Align.center);
        timerStartLabel.setFontScale(2.0f);
        stage.addActor(timerStartLabel);
    }

    private void setupHUD(Stage stage) {
        int spacing = Gdx.graphics.getWidth() / 5;

        timeLabel.setBounds(25 + 0 * spacing, Gdx.graphics.getHeight() - 25, 0, 0);
        timeLabel.setColor(StyleGuide.purpleLight);
        stage.addActor(timeLabel);

        lastLapTimeLabel.setBounds(1 * spacing - 25, Gdx.graphics.getHeight() - 25, 0, 0);
        lastLapTimeLabel.setColor(StyleGuide.purpleLight);
        stage.addActor(lastLapTimeLabel);

        fastestTimeLabel.setBounds(55 + 2 * spacing, Gdx.graphics.getHeight() - 25, 0, 0);
        fastestTimeLabel.setColor(StyleGuide.purpleLight);
        stage.addActor(fastestTimeLabel);

        speedLabel.setBounds(165 + 3 * spacing, Gdx.graphics.getHeight() - 25, 0, 0);
        speedLabel.setColor(StyleGuide.purpleLight);
        stage.addActor(speedLabel);

        lapLabel.setBounds(170 + 4 * spacing, Gdx.graphics.getHeight() - 25, 0, 0);
        lapLabel.setColor(StyleGuide.purpleLight);
        stage.addActor(lapLabel);

        serverStatus.setColor(Color.RED);
        serverStatus.setBounds(0, stage.getHeight() / 2.5f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 30);
        serverStatus.setAlignment(Align.center);
        serverStatus.setFontScale(0.9f);
        stage.addActor(serverStatus);


        //Exit Screen
        exitBackground.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 1.5f);
        exitBackground.setPosition(Gdx.graphics.getWidth() / 2f - exitBackground.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f - exitBackground.getHeight() / 2);


        exitBackground.setVisible(false);
        stage.addActor(exitBackground);

        exitResume.setSize(exitBackground.getWidth() / 2f, exitBackground.getHeight() / 10);
        exitResume.setPosition(Gdx.graphics.getWidth() / 2f - exitResume.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f + exitResume.getHeight());
        exitResume.setVisible(false);
        exitResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuOpen = false;
                if (!runMultiplayer && runSingleplayer) OGRacerGame.getInstance().isRunning = true;
                if (runMultiplayer && Client.start) OGRacerGame.getInstance().isRunning = true;

                exitBackground.setVisible(false);
                exitResume.setVisible(false);
                exitLeave.setVisible(false);
            }
        });
        stage.addActor(exitResume);

        exitLeave.setSize(exitBackground.getWidth() / 2f, exitBackground.getHeight() / 10);
        exitLeave.setPosition(Gdx.graphics.getWidth() / 2f - exitLeave.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f - exitLeave.getHeight() * 2);
        exitLeave.setVisible(false);
        exitLeave.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                menuOpen = false;

                if (multiplayer) {
                    Client.showLeaderboard = false;
                    Client.timerToStart = "";
                    OGRacerGame.getInstance().isRunning = false;
                    Client.start = false;
                    Client.leaveGame();
                }
                exitBackground.setVisible(false);
                exitResume.setVisible(false);
                exitLeave.setVisible(false);
                if (Client.socket != null) Client.socket.disconnect();
                OGRacerGame.getInstance().setGameScreen(null);
                OGRacerGame.getInstance().setScreen(new MainMenu());
            }
        });
        stage.addActor(exitLeave);
    }

    private void updateHUD() {
        if (cameraPosition < lastCameraPosition) {
            lap++;
            fastestLapTime = lastLapTime > 0 ? Math.min(fastestLapTime, timer) : timer;
            lastLapTime = timer;
            timer = 0;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        // Gradient
        for (float i = 0; i < 1f; i += 0.02f) {
            renderer.setColor(0, 0, 0, 1f - i);
            renderer.rect(0, stage.getHeight() - i * 100, stage.getWidth(), 2);
        }

        renderer.end();
        Gdx.input.setInputProcessor(stage);

        if (!multiplayer) {

            timeLabel.setText("Zeit:\n" + Util.formatTimer(timer));
            lastLapTimeLabel.setText("Letzte Runde:\n" + (lastLapTime > 0 ? Util.formatTimer(lastLapTime) : ""));
            fastestTimeLabel.setText("Schnellste Runde:\n" + (fastestLapTime > 0 ? Util.formatTimer(fastestLapTime) : ""));
            speedLabel.setText("Geschwindigkeit:\n" + Util.formatSpeed(playerSpeed / 100, playerMaxSpeed) + " km/h");
            lapLabel.setText("Runde:\n" + lap);

        } else if (Client.jsonArrayUpdatePos != null) {
            try {
                for (int i = 0; i < Client.jsonArrayUpdatePos.length(); i++) {
                    JSONObject jsonObj = Client.jsonArrayUpdatePos.getJSONObject(i);

                    if (userID.equals(jsonObj.getString("id"))) {
                        timeLabel.setText("Zeit:\n" + jsonObj.getString("current_time").replaceAll(";", ":"));
                        lastLapTimeLabel.setText("Letzte Runde:\n" + jsonObj.getString("lap_time").replaceAll(";", ":"));
                        fastestTimeLabel.setText("Schnellste Runde:\n" + jsonObj.getString("best_time").replaceAll(";", ":"));
                        if (Boolean.parseBoolean(jsonObj.getString("race_finished")))
                            OGRacerGame.getInstance().isRunning = false;
                        lapLabel.setText("Runde:\n" + jsonObj.getString("lap") + "/3");
                    }
                    speedLabel.setText("Geschwindigkeit:\n" + Util.formatSpeed(playerSpeed / 100, playerMaxSpeed) + " km/h");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static Segment findSegment(double p) {
        return segments[(int) (Math.floor(p / segmentLength) % segments.length)];
    }

    private void updatePosition(float delta) {
        Segment playerSegment = findSegment(cameraPosition + playerZ);
        dx = delta * 2 * (playerSpeed / playerMaxSpeed);

        // Langsamer auf Offroad
        if (((playerX < -1) || (playerX > 1))) {
            if ((playerSpeed > offRoadLimit)) {
                playerSpeed = playerSpeed + (offRoadDecel * delta);
                playerSpeed = (int) Util.limit(playerSpeed, 0, playerMaxSpeed);
            }
            checkSpriteCollision(playerSegment);
        }

        double scale = playerSegment.getP1().getScreen().getScale();
        double playerW = AssetData.getplayer(0).getWidth() * scale;

        if (playerSegment.getCars() != null) {
            for (int i = 0; i < playerSegment.getCars().size(); i++) {
                Car car = playerSegment.getCars().get(i);
                double carW = car.getCs().getT().getWidth() * scale;
                //if (speed > car.speed) {
                if (Util.overlap(playerX, playerW, car.getCs().getOffset() - carW * 2, carW * 3.5, 0.8)) {
                    playerSpeed = playerMaxSpeed / 5;//car.speed * (car.speed/speed);
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
        if (playerSegment.getSprites() == null) {
            return;
        }
        for (int i = 0; i < playerSegment.getSprites().length; i++) {

            double scale = playerSegment.getP1().getScreen().getScale();
            CustomSprite sprite = playerSegment.getSprites()[i];
            double spriteX = sprite.getOffset() < 0 ? sprite.getOffset() : sprite.getOffset() + 0.25;
            double spriteW = (sprite.getT().getWidth() * scale) * 2;
            double _playerX = playerX - 0.5;
            double playerW = (AssetData.getplayer(0).getWidth() * scale);

            if ((sprite.getOffset() >= 0 && Util.overlap(_playerX, playerW, spriteX + 0.125f, spriteW - 0.125f, 0.5f)) ||
                    (sprite.getOffset() < 0 && Util.overlap(playerX, playerW, spriteX - spriteW - 0.25, spriteW, 0.5f))) {
                playerSpeed = playerMaxSpeed / 5;
                cameraPosition = Util.increase(playerSegment.getP1().getWorld().getZ(), (int) -playerZ, trackLength);
                break;
            }
        }
    }


    public void checkInput(OGRacerGame game, float dt) {
        // Pausieren/Fortfahren des Spiels
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            menuOpen = true;
            game.isRunning = false;
            //Menü anzeigen
            exitBackground.setVisible(true);
            exitResume.setVisible(true);
            exitLeave.setVisible(true);
        }
        // Wenn das Spiel pausiert ist, sollen keine Eingaben zum Steuern des Autos abgefragt werden
        if (!game.isRunning) {
            playerSpeed = playerSpeed + (-accel * dt);
            playerSpeed = (int) Util.limit(playerSpeed, 0, playerMaxSpeed);
            playerX = playerX - (dx * playerSpeed / playerMaxSpeed * findSegment(cameraPosition + playerZ).getCurve() * centrifugal);
            return;
        }


		/*	Durch die Struktur ist es unmöglich
			gleichzeitig zu bremsen und zu beschleunigen bzw.
			gleichzeitig nach Links und nach rechts zu fahren
		*/
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            //Beschleunigen
            playerSpeed = playerSpeed + (accel * dt);

            playerSpeed = Util.limit(playerSpeed, 0, playerMaxSpeed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            //Bremsen
            playerSpeed = playerSpeed + (-playerMaxSpeed * dt);
            playerSpeed = (int) Util.limit(playerSpeed, 0, playerMaxSpeed);
        } else {
            //Entschleunigen
            playerSpeed = playerSpeed + (-accel * dt);
            playerSpeed = (int) Util.limit(playerSpeed, 0, playerMaxSpeed);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //Nach links fahren
            playerX = playerX - dx;
            playerX = Util.limit(playerX, -2, 2);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //Nach Rechts fahren
            playerX = playerX + dx;
            playerX = Util.limit(playerX, -2, 2);
        }

        //Centrifugal Kraft
        playerX = playerX - (dx * playerSpeed / playerMaxSpeed * findSegment(cameraPosition + playerZ).getCurve() * centrifugal);
        playerX = Util.limit(playerX, -2, 2);
    }

    public void setNewCars(Car[] cars) {
        newCars = cars;
        newCarsToPlace = true;
    }

    public static Segment[] getSegments() {
        return segments;
    }

    public static int getSegmentLength() {
        return segmentLength;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(double cP) {
        cameraPosition = cP;
    }

    public float getPlayerX() {
        return playerX;
    }

    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }

    public int getTrackLength() {
        return trackLength;
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public double getLastCameraPosition() {
        return lastCameraPosition;
    }
}
