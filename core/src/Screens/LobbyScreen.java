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
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.json.JSONException;

import java.util.ArrayList;

public class LobbyScreen extends ScreenAdapter {

    private FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    private Stage stage = new Stage(viewport);

    private int searchCounter = 0;

    public static ArrayList<String> idList = new ArrayList<>(8);

    private static String[] playerReady;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final String ID;

    private Label idLabel, lobbyCode;

    private final Label timeLabel = new Label("Start in:", Constants.buttonSkin);

    private final Label[] player = new Label[8];


    private final TextButton leaveButton = new TextButton("Verlassen", Constants.buttonSkin);

    private final TextButton optionButton = new TextButton("Einstellungen", Constants.buttonSkin);

    private final TextButton logoutButton = new TextButton("Abmelden", Constants.buttonSkin);

    private final TextButton readyButton = new TextButton("Nicht Bereit", Constants.buttonSkin);

    private final Button[] lobbyButton =  {
            new TextButton("Verlassen", Constants.buttonSkin),
            new TextButton("Einstellungen", Constants.buttonSkin),
            new TextButton("Abmelden", Constants.buttonSkin),
            new TextButton("Nicht Bereit", Constants.buttonSkin)
    };

    private final MessageChat chat;

    private boolean ready = false;


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

        readyButton.setBounds(Gdx.graphics.getWidth() / 1.9f-6,(float) Gdx.graphics.getHeight() / 16-5,410,60);
        readyButton.setColor(StyleGuide.purpleDark);
        stage.addActor(readyButton);
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

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if(Client.timerStatus){
            timeLabel.setText("Start in:");
            Client.timerStatus = false;
            Client.timer = -1;
        }

        if(Client.timer != -1){

            if(Client.timer == 10){
                timeLabel.setText("Start in: " + Client.timer);
            }else{
                timeLabel.setText("Start in:  " + Client.timer);
            }
        }

        if(Client.waitGame){
            OGRacerGame.getInstance().setScreen(new LoadingScreen(ID));
        }

    }

    private void buttonListener() {

        leaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                idList = new ArrayList<>(8);
                Client.leaveLobby();
                Client.playerString = "";
                Client.joinStatus = "";
                Client.timer = -1;
                OGRacerGame.getInstance().setScreen(new LobbyMenu(ID));
            }
        });

        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                idList = new ArrayList<>(8);
                Client.playerString = "";
                Client.joinStatus = "";
                try {
                    Client.leaveLobby();
                    Client.sendLogOut();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
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

        readyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                if(!ready){
                    Client.ready();
                    readyButton.setColor(new Color((int) (0.98611116*255),0,1,1));
                    readyButton.setText("Bereit");
                    ready = true;
                }else{
                    Client.notReady();
                    readyButton.setColor(StyleGuide.purpleDark);
                    readyButton.setText("Nicht Bereit");
                    ready = false;
                }
            }
        });
    }

    private void setupButton() {

        leaveButton.setBounds(70, stage.getHeight() / 1.5f, 180, 50);
        leaveButton.setTransform(true);
        leaveButton.setRotation(90);
        stage.addActor(leaveButton);

        optionButton.setBounds(70, stage.getHeight() / 2 - 115, 230, 50);
        optionButton.setTransform(true);
        optionButton.setRotation(90);
        stage.addActor(optionButton);

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
    
    private void playerReady(){

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