package Screens;

import Connection.Client;
import Messenger.MessageChat;
import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import Screens.MenuArea.LobbyMenu;
import Screens.MenuArea.LoginMenu;
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
import org.json.JSONException;

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

    private TextButton logoutButton = new TextButton("Abmelden", Constants.buttonSkin);

    private int lastIDListSize = 0;

    private Timer.Task timerTask;

    private MessageChat chat;

    private float time = 11;

    private boolean startTime = false;


    public LobbyScreen(String ID) {
        this.ID = ID;
        chat = new MessageChat(ID, stage);
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


    private void setupLabel() {

        for (int i = 0; i < player.length; i++)
            player[i] = new Label("Suchen ", Constants.buttonSkin);

        timeLabel = new Label("Start in: -", Constants.buttonSkin);
        timeLabel.setBounds(stage.getWidth() / 1.3f+9, stage.getHeight() - 75, 190, 40);
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

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        if (!Client.connect) OGRacerGame.getInstance().setScreen(new MultiplayerMenu());

        updateField();
        addLobbyID();
        chat.update(idList);
        chat.updateScrollBar();
        if (startTime) startTimer(delta);

        if (idList.size() != lastIDListSize) {
            if (timerTask != null) timerTask.cancel();
            time = 11;
            startTime = false;
            timeLabel.setColor(Color.WHITE);
            timeLabel.setText("Start in:  ");
        }

        if (idList.size() != lastIDListSize && idList.size() > 1) waitTimer(); // muss 1 sein

        lastIDListSize = idList.size();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void waitTimer() {

        timerTask = new Timer.Task() {
            @Override
            public void run() {
                startTime = true;
            }
        };

        // Starte den Timer mit einer Verzögerung von 10 Sekunden
        Timer.schedule(timerTask, 10);  // muss 10
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
                OGRacerGame.getInstance().setScreen(new LobbyMenu(ID));
            }
        });

        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                idList = new ArrayList<>(8);
                Client.playerString = "";
                Client.joinLobbyStatus = "";
                try {
                    Client.leaveLobby();
                    Client.sendLogOut();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (timerTask != null) {
                    timerTask.cancel();
                }
                OGRacerGame.getInstance().setScreen(new LoginMenu());
            }
        });

        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);

            }
        });
    }

    private void startTimer(float delta) {


        if (time > 0f) {
            time = Math.max(time - delta, 0f);
        }


        if(time > 10){
            timeLabel.setText("Start in: " + (int) time);
        }else if(time > 1){
            timeLabel.setText("Start in:  " + (int) time);
        }else{
            timeLabel.setColor(Color.GREEN);
            timeLabel.setText("   --> GO -->");
        }

    }

    private void setupButton() {

        optionButton.setBounds(70, stage.getHeight() / 2 - 115, 230, 50);
        optionButton.setTransform(true);
        optionButton.setRotation(90);
        stage.addActor(optionButton);

        lobbyLeaveButton.setBounds(70, stage.getHeight() / 1.5f, 180, 50);
        lobbyLeaveButton.setTransform(true);
        lobbyLeaveButton.setRotation(90);
        stage.addActor(lobbyLeaveButton);

        logoutButton.setBounds(70, stage.getHeight() / 3f - 170, 170, 50);
        logoutButton.setTransform(true);
        logoutButton.setRotation(90);
        stage.addActor(logoutButton);
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
