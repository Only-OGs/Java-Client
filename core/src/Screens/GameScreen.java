package Screens;

import Connection.Client;
import Helpers.Constants;
import Helpers.Util;
import OGRacerGame.OGRacerGame;
import Rendering.CarRenderer;
import Rendering.RenderSegment;
import Rendering.SpritesRenderer;
import Road.*;
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
import java.util.Arrays;

public class GameScreen extends ScreenAdapter implements IInputHandler {

    private float skyOffset = 0;
    private float hillOffset = 0;
    private float treeOffset = 0;
    private final boolean multiplayer;
    private String userID = "";
    public static ArrayList<RoadPart> roadBuilders = new ArrayList<>();
    private final Viewport viewport;

    protected Stage stage;
    private final SpriteBatch batch;

    private final ShapeRenderer renderer;
    private final Texture txtSky = new Texture("background/sky.png");
    private final Texture txtHills = new Texture("background/hills.png");
    private final Texture txtTrees = new Texture("background/trees.png");

    private static Segment[] segments;

    Label timeLabel = new Label("", Constants.buttonSkin);
    Label lastLapTimeLabel = new Label("", Constants.buttonSkin);
    Label fastestTimeLabel = new Label("", Constants.buttonSkin);
    Label speedLabel = new Label("", Constants.buttonSkin);

    Label lapLabel = new Label("", Constants.buttonSkin);

    Label serverStatus = new Label("", Constants.buttonSkin, "title");


    Label timerStartLabel = new Label("", Constants.buttonSkin, "title");

    Image exitBackground = new Image(new Texture("sprites/exitBackground.png"));
    TextButton exitResume = new TextButton("WEITER", Constants.buttonSkin);
    TextButton exitLeave = new TextButton("VERLASSEN", Constants.buttonSkin);


    //TEST Variables

    private int roadWidth = 2000;

    private static int segmentLenght = 200;

    private int lanes = 3;

    private int segmentsCount = 600;

    private int trackLength;

    private int FOV = 100;

    private int cameraHeight = 1000;

    private float cameraDepth = (float) (1 / Math.tan((FOV / 2) * Math.PI / 180));

    private int drawDistance = 200;

    private float playerX = 0;

    private float playerY = 0;

    private float playerZ = cameraHeight * cameraDepth;

    private float playerSpeed = 0;
    private final float playerMaxSpeed = segmentLenght * 60;
    private final float accel = playerMaxSpeed / 5;
    private final float offRoadDecel = -playerMaxSpeed / 2;
    private final float offRoadLimit = playerMaxSpeed / 4;
    private float centrifugal = 0.3f;
    private float speedPercent = playerSpeed / playerMaxSpeed;
    private float dx = 0;
    private double cameraPosition = 0;
    private double lastCameraPosition = 0;
    private float resolution = Gdx.graphics.getHeight() / 480;
    private float sunOffset = 0;
    private double fogDensity = drawDistance / 20;
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
        segments = RoadBuilder.resetRoad(segmentsCount, segmentLenght);
        trackLength = segments.length * segmentLenght;
        setNewCars(RoadBuilder.createCarArr(segmentsCount));
        setupTimerLabel();
        leaderboard = new Leaderboard(stage);
        setupHUD(stage);
        renderSegments(renderer);
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
        trackLength = segments.length * segmentLenght;
        leaderboard = new Leaderboard(stage);
        setupTimerLabel();
        setupHUD(stage);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        renderBackground(batch, txtSky, skyOffset, (int) (playerY * 0.001f * resolution));
        renderBackground(batch, txtHills, hillOffset, (int) (playerY * 0.002f * resolution));
        renderBackground(batch, txtTrees, treeOffset, (int) (playerY * 0.003f * resolution));
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderSegments(renderer);
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

        if (Client.startGame) {
            Client.startGame = false;
            startPosition();
            placeSprites();
        }
        updateHUD();
        updatePosition(delta);

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

        if (playerSpeed <= 0) OGRacerGame.movement = false;
        else OGRacerGame.movement = true;

        stage.draw();

        if (multiplayer) {
            if (camaeraPositionOld != cameraPosition) {
                try {
                    Client.ingamePos(playerX, cameraPosition);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            camaeraPositionOld = cameraPosition;

            if (Client.updatePos) {
                Client.updatePos = false;
                updateCars();
            }
        } else for (Car c : newCars) CarsKi.kiMoveBot(c);
    }

    private void startTimer(float delta) {
        timerToStart -= delta;
        if (timerToStart < 1) {
            timerStartLabel.setColor(0.3f, 1f, 0.5f, 1f);
            timerStartLabel.setText("GO");
        } else timerStartLabel.setText((int) timerToStart);
    }

    private void renderBackground(SpriteBatch sB, Texture txt, float offset, int lift) {
        int sourceX = (int) Math.floor(txt.getWidth() * offset);
        int sourceW = Math.min(txt.getWidth(), txt.getWidth() - sourceX);

        int destW = (int) Math.floor(Gdx.graphics.getWidth() * (sourceW / (txt.getWidth() / 2f)));

        sB.draw(txt, 0, lift, destW, Gdx.graphics.getHeight(), sourceX, 0, sourceW, txt.getHeight(), false, false);
        if (sourceW < txt.getWidth()) {
            sB.draw(txt, destW - 1, lift, Gdx.graphics.getWidth() - destW, Gdx.graphics.getHeight(), 0, 0, txt.getWidth() / 2 - sourceW, txt.getHeight(), false, false);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        stage.dispose();
    }

    public void renderSegments(ShapeRenderer r) {
        Segment baseSegment = findSegment(cameraPosition);
        Segment playerSegment = findSegment(cameraPosition + playerZ);
        int maxy = Gdx.graphics.getHeight();
        float basePercent = (float) Util.percentRemaining((float) cameraPosition, segmentLenght);
        float playerPercent = (float) Util.percentRemaining((float) cameraPosition + playerZ, segmentLenght);
        int playerY = (int) Util.interpolate(playerSegment.getP1().getWorld().getY(), playerSegment.getP2().getWorld().getY(), playerPercent);
        float x = 0; //Akkumulierter seitlicher versatz der Segmente
        float dx = 0f - (baseSegment.getCurve() * basePercent); // hilft die Versätzung zu speichern und berechnen
        sunOffset += dx * speedPercent;

        Segment segment;
        int segmentLoopedValue;

        for (int i = 0; i < drawDistance; i++) {
            segment = segments[(baseSegment.getIndex() + i) % segments.length];
            segment.setLooped(segment.getIndex() < baseSegment.getIndex());
            segment.setFog(1 - Util.exponentialFog(Float.parseFloat(String.valueOf(i)) / drawDistance, fogDensity));
            segment.setClip(maxy);

            if (segment.isLooped()) {
                segmentLoopedValue = trackLength;
            } else {
                segmentLoopedValue = 0;
            }

            Util.project(segment.getP1(), (int) (((int) (playerX * roadWidth)) - x), playerY + cameraHeight, (int) cameraPosition - segmentLoopedValue, cameraDepth, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), roadWidth);
            Util.project(segment.getP2(), (int) (((int) (playerX * roadWidth)) - x - dx), playerY + cameraHeight, (int) cameraPosition - segmentLoopedValue, cameraDepth, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), roadWidth);

            x += dx;   //nach jeden segment steigt oder singt die Versätzung der Segmente abhängig von der Stärke der Kurve
            dx += segment.getCurve();

            if ((segment.getP1().getCamera().getZ() <= cameraDepth) ||
                    (segment.getP2().getScreen().getY() >= maxy) ||
                    (segment.getP2().getScreen().getY() >= segment.getP1().getScreen().getY())) {
                continue;
            }
            RenderSegment.render(r, Gdx.graphics.getWidth(), lanes,
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
        //SunShade.sun(r, batch, sunOffset, maxy);
        Segment s;
        for (int n = (drawDistance - 1); n > 0; n--) {
            s = segments[((baseSegment.getIndex() + n) % segments.length)];
            //cars
            if (s.getCars() != null) {
                for (int q = 0; q < s.getCars().size(); q++) {
                    Car car = s.getCars().get(q);
                    double spriteScale = Util.interpolate(s.getP1().getScreen().getScale(), s.getP2().getScreen().getScale(), 0.5f);
                    float spriteX = (float) (Util.interpolate(s.getP1().getScreen().getX(), s.getP2().getScreen().getX(), 0.5f) + (spriteScale * car.getCs().getOffset() * roadWidth * Gdx.graphics.getWidth() / 2));
                    float spriteY = (float) (Util.interpolate(s.getP1().getScreen().getY(), s.getP2().getScreen().getY(), 0.5f));
                    SpritesRenderer.render(batch, resolution, roadWidth, car.getCs().getT(), spriteScale, spriteX, spriteY, -0.5f, -1f, s.getClip());
                }
            }
            if (s.getSprites() != null) {
                for (int q = 0; q < s.getSprites().length; q++) {
                    CustomSprite cs = s.getSprites()[q];
                    double spriteScale = s.getP1().getScreen().getScale();
                    float spriteX = (float) (s.getP1().getScreen().getX() + (spriteScale * cs.getOffset() * roadWidth * Gdx.graphics.getWidth() / 2));
                    float spriteY = s.getP1().getScreen().getY();
                    SpritesRenderer.render(batch, resolution, roadWidth, cs.getT(), spriteScale, spriteX, spriteY, cs.getOffset(), -1f, s.getClip());
                }
            }
            if (s == playerSegment) {
                CarRenderer.renderPlayerCar(batch, playerSegment, resolution, roadWidth, playerSpeed / playerMaxSpeed, cameraDepth / playerZ, Gdx.graphics.getWidth() / 2,
                        (int) ((Gdx.graphics.getHeight() / 2) - (cameraDepth / playerZ * Util.interpolate((int) playerSegment.getP1().getCamera().getY(), (int) playerSegment.getP2().getCamera().getY(), playerPercent)) * Gdx.graphics.getHeight() / 2));

            }
        }
        skyOffset = (float) Util.increase(skyOffset, (0.001f * playerSegment.getCurve() * (cameraPosition - lastCameraPosition) / segmentLenght), 1);
        hillOffset = (float) Util.increase(skyOffset, (0.002f * playerSegment.getCurve() * (cameraPosition - lastCameraPosition) / segmentLenght), 1);
        treeOffset = (float) Util.increase(skyOffset, (0.003f * playerSegment.getCurve() * (cameraPosition - lastCameraPosition) / segmentLenght), 1);
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
            speedLabel.setText("Geschwindigkeit:\n" + Util.formatSpeed(playerSpeed / 100, playerMaxSpeed));
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
                    speedLabel.setText("Geschwindigkeit:\n" + Util.formatSpeed(playerSpeed / 100, playerMaxSpeed));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Segment findSegment(double p) {
        return segments[(int) (Math.floor(p / segmentLenght) % segments.length)];
    }

    private void updatePosition(float delta) {
        Segment playerSegment = findSegment(cameraPosition + playerZ);
        speedPercent = playerSpeed / playerMaxSpeed;
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
        double playerW = CarRenderer.tS.getWidth() * scale;

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
            double playerW = (CarRenderer.tS.getWidth() * scale);

            if ((sprite.getOffset() >= 0 && Util.overlap(_playerX, playerW, spriteX + 0.125f, spriteW - 0.125f, 0.5f)) ||
                    (sprite.getOffset() < 0 && Util.overlap(playerX, playerW, spriteX - spriteW - 0.25, spriteW, 0.5f))) {
                playerSpeed = playerMaxSpeed / 5;
                cameraPosition = Util.increase(playerSegment.getP1().getWorld().getZ(), (int) -playerZ, trackLength);
                break;
            }
        }
    }

    private void placeSprites() {
        ArrayList<CustomSprite> sprites = new ArrayList<>();
        try {
            // Iteriere durch jedes JSON-Objekt im Array
            for (int i = 0; i < Client.jsonArrayAssets.length(); i++) {
                JSONObject jsonObj = Client.jsonArrayAssets.getJSONObject(i);

                // Greife auf die Werte der Schlüssel zu
                float offset = Float.parseFloat(jsonObj.getString("side"));
                double pos = Double.parseDouble(jsonObj.getString("pos"));
                String texture = jsonObj.getString("model");
                CustomSprite cs = new CustomSprite(texture, offset, pos);
                sprites.add(cs);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RoadBuilder.addSprites(segments, sprites.toArray(sprites.toArray(CustomSprite[]::new)));
    }

    private void startPosition() {
        try {
            // Iteriere durch jedes JSON-Objekt im Array
            for (int i = 0; i < Client.jsonArrayUpdatePos.length(); i++) {
                JSONObject jsonObj = Client.jsonArrayUpdatePos.getJSONObject(i);

                // Greife auf die Werte der Schlüssel zu
                float offset = Float.parseFloat(jsonObj.getString("offset"));
                double pos = Double.parseDouble(jsonObj.getString("pos"));
                String id = jsonObj.getString("id");
                if (id.equals(userID)) {
                    playerX = offset;
                    cameraPosition = pos;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateCars() {
        ArrayList<Car> cars = new ArrayList<>();

        try {
            // Iteriere durch jedes JSON-Objekt im Array
            for (int i = 0; i < Client.jsonArrayUpdatePos.length(); i++) {
                JSONObject jsonObj = Client.jsonArrayUpdatePos.getJSONObject(i);

                // Greife auf die Werte der Schlüssel zu
                float offset = Float.parseFloat(jsonObj.getString("offset"));
                double pos = Double.parseDouble(jsonObj.getString("pos"));
                String id = jsonObj.getString("id");
                if (newCars == null) {
                    CustomSprite sprite = switch (jsonObj.getString("asset")) {
                        case "1" -> new CustomSprite("car01.png", offset, pos);
                        case "2" -> new CustomSprite("car02.png", offset, pos);
                        case "3" -> new CustomSprite("car03.png", offset, pos);
                        case "4" -> new CustomSprite("car04.png", offset, pos);
                        case "5" -> new CustomSprite("truck.png", offset, pos);
                        case "6" -> new CustomSprite("semi.png", offset, pos);
                        default -> new CustomSprite(offset, pos);
                    };
                    cars.add(new Car(id, sprite));
                } else {
                    cars = new ArrayList<>(Arrays.asList(newCars));
                    if (!id.equals(userID)) {
                        cars.forEach(c -> {
                            if (c.getID().equals(id)) {
                                c.setZ(pos);
                                c.setOffset(offset);
                                c.setPercent((float) Util.percentRemaining((float) (pos % 200), 200f));
                            }
                        });
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setNewCars(cars.toArray(Car[]::new));
    }

    @Override
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

    public static int getSegmentLenght() {
        return segmentLenght;
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

    public void setCameraPosition(double cameraPosition) {
        this.cameraPosition = cameraPosition;
    }

    public float getPlayerX() {
        return playerX;
    }

    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }
}
