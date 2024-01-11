package Screens;

import Connection.Client;
import Helpers.Util;
import OGRacerGame.OGRacerGame;
import Rendering.AssetData;
import Rendering.Render;
import Road.*;
import Road.Helper.Segment;
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
import org.json.JSONException;

import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    private final boolean multiplayer;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final ShapeRenderer renderer;
    private final Render fullRenderer;
    private final Multiplayerfunktions multiplayerFunctions;
    private final Hud HUD;
    private final float playerMaxSpeed = segmentLength * 60;
    private final float accel = playerMaxSpeed / 5;
    private final float offRoadDecel = -playerMaxSpeed / 2;
    private final float offRoadLimit = playerMaxSpeed / 4;
    private final float centrifugal = 0.3f;

    private String userID = "";
    private int FOV = 100;
    private float cameraDepth = (float) (1 / Math.tan((FOV / 2) * Math.PI / 180));
    private float playerX = 0;
    private float playerZ = cameraHeight * cameraDepth;
    private float playerSpeed = 0;
    private float dx;
    private boolean newCarsToPlace = false;
    private Car[] newCars;
    private float timer = 0;
    private Leaderboard leaderboard;
    private float timerToStart = 6;
    private double cameraPosition;
    private double camaeraPositionOld = 0;
    private float spriteScale= (float) (0.3f*(1f/AssetData.getplayer(0).getWidth()));

    private static double lastCameraPosition = 0;
    public static ArrayList<RoadPart> roadBuilders = new ArrayList<>();
    private static Segment[] segments;
    private static int segmentLength = 200;
    private static int trackLength;
    private static int cameraHeight = 1000;

    protected Stage stage;

    public static boolean runSingleplayer;
    public static boolean runMultiplayer;

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
        leaderboard = new Leaderboard(stage);
        fullRenderer = new Render(segments, batch, renderer, this);
        multiplayerFunctions = new Multiplayerfunktions(this);
        CarController.setup(this);
        HUD = new Hud(stage, false);
    }

    public GameScreen(String userID) {
        OGRacerGame.getInstance().isRunning = false;
        this.userID = userID;
        multiplayer = true;
        runSingleplayer = false;
        runMultiplayer = false;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        segments = RoadBuilder.resetRoad(roadBuilders.toArray(RoadPart[]::new));
        trackLength = segments.length * segmentLength;
        leaderboard = new Leaderboard(stage);
        fullRenderer = new Render(segments, batch, renderer, this);
        multiplayerFunctions = new Multiplayerfunktions(this);
        CarController.setup(this);
        HUD = new Hud(stage, true);
    }


    /* Überprüft, ob wir im Mehrspieler oder Einzelspieler.
     * Wird ein Timer gesetzt und startet, das man sich bewegen kann.
     */
    private void startRace(float delta) {

        // Sorgt dafür das man erst fahren kann, wenn der Timer abgelaufen ist im Multiplayer
        if (Client.start && multiplayer && !runMultiplayer) {
            runMultiplayer = true;
            HUD.timerStartLabel.setText("");
            if (HUD.isMenuClosed()) OGRacerGame.getInstance().isRunning = true;

        } else if (!runMultiplayer && multiplayer) HUD.timerStartLabel.setText(Client.timerToStart);
        if (runMultiplayer && multiplayer) timer += delta;

        // Sorgt dafür das man erst fahren kann, wenn der Timer abgelaufen ist im Einzelspieler
        if (timerToStart <= 0 && !multiplayer && !runSingleplayer) {
            runSingleplayer = true;
            HUD.timerStartLabel.setText("");
            if (HUD.isMenuClosed()) OGRacerGame.getInstance().isRunning = true;

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

        if (!Client.connect && multiplayer) HUD.serverStatus.setText("Verbindung zum Server\n\nverloren");

        setupPositionData();
        if(HUD.updateHUD(
                cameraPosition < lastCameraPosition,
                timer,
                renderer,
                userID,
                Util.formatSpeed(playerSpeed / 100, playerMaxSpeed))
        ) timer = 0;
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

    /**Startet den Timer zu Beginn des Rennens*/
    private void startTimer(float delta) {
        timerToStart -= delta;
        if (timerToStart < 1) {
            HUD.timerStartLabel.setColor(0.3f, 1f, 0.5f, 1f);
            HUD.timerStartLabel.setText("GO");
        } else HUD.timerStartLabel.setText((int) timerToStart);
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        stage.dispose();
    }

    public static Segment findSegment(double p) {
        return segments[(int) (Math.floor(p / segmentLength) % segments.length)];
    }

    private void updatePosition(float delta) {
        Segment playerSegment = findSegment(cameraPosition + playerZ);
        dx = delta * 2 * (playerSpeed / playerMaxSpeed);

        if (((playerX < -1) || (playerX > 1))) {
            // Langsamer auf Offroad
            if ((playerSpeed > offRoadLimit)) {
                playerSpeed = playerSpeed + (offRoadDecel * delta);
                playerSpeed = (int) Util.limit(playerSpeed, 0, playerMaxSpeed);
            }
            //Kollision mit Sprites am Straßenrand überprüfen
            checkSpriteCollision(playerSegment);
        }

        double scale = playerSegment.getP1().getScreen().getScale();
        double playerW = AssetData.getplayer(0).getWidth() * scale;

        //Kollision mit anderen Autos überprüfen
        if (playerSegment.getCars() != null) {
            for (int i = 0; i < playerSegment.getCars().size(); i++) {
                Car car = playerSegment.getCars().get(i);
                double carW = car.getCs().getT().getWidth() * scale;
                if (Util.overlap(playerX, playerW, car.getCs().getOffset() - carW * 2, carW * 3.5, 0.8)&&car.getSpeed()<playerSpeed) {
                    playerSpeed = playerMaxSpeed / 5;
                    cameraPosition = Util.increase((int) car.getCs().getZ(), (int) -playerZ, trackLength);
                    break;
                }
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
            HUD.setMenuOpen(true);
            game.isRunning = false;
            //Menü anzeigen
            HUD.exitBackground.setVisible(true);
            HUD.exitResume.setVisible(true);
            HUD.exitLeave.setVisible(true);
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

        //Zentrifugal Kraft anwenden
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
    public Render getFullRenderer(){
        return fullRenderer;
    }

    public float getPlayerMaxSpeed() {
        return playerMaxSpeed;
    }

    public float getSpriteScale() {
        return spriteScale;
    }
}
