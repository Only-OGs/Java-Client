package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import Screens.MenuArea.LobbyMenu;
import Screens.MenuArea.MultiplayerMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class LobbyScreen extends ScreenAdapter {

    private FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private Stage stage = new Stage(viewport);
    private int searchCounter = 0;
    public static ArrayList<String> idList = new ArrayList<>(8);
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final String ID;
    private Label idLabel, lobbyCode, timeLabel;

    private final Label[] player = new Label[8];

    private TextButton lobbyLeaveButton = new TextButton("Verlassen", Constants.buttonSkin);

    private TextButton optionButton = new TextButton("Einstellungen", Constants.buttonSkin);

    private int lastIDListSize = 0;

    private Timer.Task timerTask;

    private MessageChat chat;

    private String time = "-";

    public LobbyScreen(String ID) {
        this.ID = ID;
        chat = new MessageChat(ID,stage);
        Gdx.input.setInputProcessor(stage);
        Constants.title.setText("Die Lobby");
        Constants.title.setSize(Gdx.graphics.getWidth(), 100);
        Constants.title.setPosition(0, stage.getHeight() - 100);
        Constants.title.setAlignment(Align.center);
        stage.addActor(Constants.title);

        setupLabel();
        setupButton();
        buttonListener();

        chat.build();
    }


    private void setupLabel(){

        for (int i = 0; i < player.length; i++)
            player[i] = new Label("Suchen ", Constants.buttonSkin);

        timeLabel = new Label("Start in: ", Constants.buttonSkin);
        timeLabel.setBounds(stage.getWidth() / 1.3f, stage.getHeight() - 75, 140, 50);
        stage.addActor(timeLabel);

        idLabel = new Label("ID: " + ID, Constants.buttonSkin);
        idLabel.setBounds(100, stage.getHeight() - 75, 190, 40);
        stage.addActor(idLabel);

        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        font.setColor(Color.WHITE);

        lobbyCode = new Label("Lobby ID: ", style);
        lobbyCode.setBounds(stage.getWidth() / 1.2f, 25, 190, 10);
        stage.addActor(lobbyCode);
    }

    private void addLobbyID() {
        lobbyCode.setText("Lobby ID: " + Client.lobbyID);
    }

    private void updateTime() {
        timeLabel.setText("Start in: " + time);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        if (!Client.connect) OGRacerGame.getInstance().setScreen(new MultiplayerMenu());

        updateField();
        addLobbyID();
        chat.update(idList);
        chat.updateScrollBar();
        updateTime();

        if (idList.size() != lastIDListSize) {
            if (timerTask != null) timerTask.cancel();

            time = "-";
        }

        if (idList.size() != lastIDListSize && idList.size() > 1) waitTimer();

        lastIDListSize = idList.size();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void waitTimer() {

        timerTask = new Timer.Task() {
            @Override
            public void run() {
                time = "10";
            }
        };

        // Starte den Timer mit einer Verzögerung von 3 Sekunden
        Timer.schedule(timerTask, 5);

    }

    private void buttonListener() {

        lobbyLeaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                idList = new ArrayList<>(8);
                Client.leaveLobby();
                Client.playerString = "";
                Client.joinLobbyStatus = "";
                if (timerTask != null) {
                    timerTask.cancel();
                }
                time = "-";
                OGRacerGame.getInstance().setScreen(new LobbyMenu(ID));
            }
        });

        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);

            }
        });
    }

    private void setupButton() {
        lobbyLeaveButton.setBounds(70, stage.getHeight() - 190, 170, 50);
        lobbyLeaveButton.setTransform(true);
        lobbyLeaveButton.setRotation(90);
        stage.addActor(lobbyLeaveButton);

        optionButton.setBounds(70, stage.getHeight() - 420, 225, 50);
        optionButton.setTransform(true);
        optionButton.setRotation(90);
        stage.addActor(optionButton);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
        super.dispose();
    }

    void updateField() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(255, 255, 255, 0.08f));
        for (int i = 0; i <= 560; i += 80)
            shapeRenderer.rect(100, ((float) Gdx.graphics.getHeight() / 16) + i, 400, 40);
        shapeRenderer.end();

        createPlayer();

        for (int i = 0; i < idList.size(); i++) {
            player[i].setText(idList.get(i));
            player[i].setColor(StyleGuide.colors[i]);
        }

        searchCounter++;
        if (searchCounter == 60) {
            for (int i = idList.size(); i < player.length; i++) {
                player[i].setColor(StyleGuide.white);
                player[i].setText("Suchen .");
            }
        } else if (searchCounter == 120) {
            for (int i = idList.size(); i < player.length; i++) {
                player[i].setColor(StyleGuide.white);
                player[i].setText("Suchen ..");
            }
        } else if (searchCounter == 180) {
            for (int i = idList.size(); i < player.length; i++) {
                player[i].setColor(StyleGuide.white);
                player[i].setText("Suchen ...");
            }
            searchCounter = 0;
        }
    }

    void createPlayer() {
        int yOffset = 560;
        for (int i = 0; i < player.length; i++) {
            player[i].setPosition(120, ((float) Gdx.graphics.getHeight() / 16) + yOffset);
            player[i].setSize(400, 40);
            stage.addActor(player[i]);
            yOffset -= 80;
        }
    }
}
