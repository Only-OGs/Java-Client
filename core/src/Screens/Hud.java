package Screens;

import Connection.Client;
import Helpers.Constants;
import Helpers.Util;
import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import Screens.Menu.MenuArea.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import org.json.JSONException;
import org.json.JSONObject;

public class Hud {
    public Label timeLabel;
    public Label lastLapTimeLabel;
    public Label fastestTimeLabel;
    public Label speedLabel;
    public Label lapLabel;

    public Image exitBackground;
    public TextButton exitResume;
    public TextButton exitLeave;

    public Label serverStatus;
    public Label timerStartLabel;
    private int lap;

    private float lastLapTime;
    private float fastestLapTime;
    private boolean menuOpen;
    private boolean multiplayer;
    private Stage stage;

    public Hud(Stage pStage, boolean pMultiplayer) {
        stage = pStage;

        timeLabel = new Label("", Constants.buttonSkin);
        lastLapTimeLabel = new Label("", Constants.buttonSkin);
        fastestTimeLabel = new Label("", Constants.buttonSkin);
        speedLabel = new Label("", Constants.buttonSkin);
        lapLabel = new Label("", Constants.buttonSkin);

        exitBackground = new Image(new Texture("sprites/exitBackground.png"));
        exitResume = new TextButton("WEITER", Constants.buttonSkin);
        exitLeave = new TextButton("VERLASSEN", Constants.buttonSkin);

        serverStatus = new Label("", Constants.buttonSkin, "title");
        timerStartLabel = new Label("", Constants.buttonSkin, "title");

        lastLapTime = 0;
        fastestLapTime = 0;
        lap = 1;

        menuOpen = false;
        multiplayer = pMultiplayer;
        setupHUD();
    }

    private void setupHUD() {
        setupGameHud();
        setupStatusHud();
        setupPauseMenu();
        setupTimerLabel();
    }

    private void setupGameHud() {
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
    }

    private void setupStatusHud() {
        serverStatus.setColor(Color.RED);
        serverStatus.setBounds(0, stage.getHeight() / 2.5f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 30);
        serverStatus.setAlignment(Align.center);
        serverStatus.setFontScale(0.9f);
        stage.addActor(serverStatus);
    }

    private void setupPauseMenu() {
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
                if (!GameScreen.runMultiplayer && GameScreen.runSingleplayer) OGRacerGame.getInstance().isRunning = true;
                if (GameScreen.runMultiplayer && Client.start) OGRacerGame.getInstance().isRunning = true;

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

    private void setupTimerLabel() {
        timerStartLabel.setBounds(0, stage.getHeight() / 2.5f, Gdx.graphics.getWidth(), 100);
        timerStartLabel.setAlignment(Align.center);
        timerStartLabel.setFontScale(2.0f);
        stage.addActor(timerStartLabel);
    }

    public boolean updateHUD(boolean increaseLapCount, float timer, ShapeRenderer renderer, String userID, String speed) {
        if (increaseLapCount) {
            lap++;
            fastestLapTime = lastLapTime > 0 ? Math.min(fastestLapTime, timer) : timer;
            lastLapTime = timer;
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
            speedLabel.setText("Geschwindigkeit:\n" + speed + " km/h");
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
                    speedLabel.setText("Geschwindigkeit:\n" + speed + " km/h");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return increaseLapCount;
    }

    public boolean isMenuClosed() {
        return !menuOpen;
    }

    public void setMenuOpen(boolean pMenuOpen) {
        menuOpen = pMenuOpen;
    }
}
