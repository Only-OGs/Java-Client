package Screens.MenuArea;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Screens.Constants;
import Screens.LobbyScreen;
import Screens.SearchScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class LobbyMenu extends MultiplayerMenu {

    private Button createButton, quickButton, searchButton, logOut;

    private String lobbyMessage = "";

    private Label idLabel;

    private final String ID;

    private int count = 0;

    public LobbyMenu(String ID) {
        this.ID = ID;
        Constants.title.setText("Lobby Auswahl");
        status.remove();
        statusTitle.remove();
        removeButton();
        lobbySelectionButton();
        lobbyButtonListener();
        addLabelID();
        Constants.music.stop();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        stage.act();
        stage.draw();

        if (!Client.connect) OGRacerGame.getInstance().setScreen(new MultiplayerMenu());

        if ("login_success".equals(Client.loginStatus)) {
            lobbyMessage = Client.loginMessage;
            Client.loginStatus = "";
            Client.loginMessage = "";
        }

        if ("failed".equals(Client.quickStatus)) {
            count = 0;
            lobbyMessage = Client.quickMessage;
            Client.quickStatus = "";
            Client.quickMessage = "";
        }

        if ("success".equals(Client.quickStatus)) {

            LobbyScreen.idList = new ArrayList<>(Arrays.asList(Client.playerString.split(";")));
            OGRacerGame.getInstance().setScreen(new LobbyScreen(ID));
            Client.quickStatus = "";
            Client.quickMessage = "";
        }

        if (200 == count) {
            serverMessage.setText("");
            lobbyMessage = "";
            count++;
        } else if (count < 200) {
            serverMessage.setText(lobbyMessage);
            count++;
        }
    }

    public void addLabelID() {
        idLabel = new Label("ID: " + ID, Constants.buttonSkin);
        idLabel.setBounds(stage.getWidth() / 1.3f, stage.getHeight() - 65, 190, 40);
        stage.addActor(idLabel);
    }

    protected void lobbyButtonListener() {
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                lobbyMessage = "";
                try {
                    createLobby();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        quickButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                lobbyMessage = "";
                Client.getLobby();
            }
        });

        searchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                OGRacerGame.getInstance().setScreen(new SearchScreen(ID));
            }
        });

        logOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                serverMessage.setText("");
                Client.loginStatus = "";
                Client.loginMessage = "";
                try {
                    Client.sendLogOut();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                OGRacerGame.getInstance().setScreen(new LoginMenu());
            }
        });
    }

    private void lobbySelectionButton() {

        createButton = new TextButton("Lobby erstellen", Constants.buttonSkin);

        createButton.setSize(260, 60);
        createButton.setPosition(stage.getWidth() / 2 - (createButton.getWidth() / 2), stage.getHeight() / 2 - (createButton.getHeight() / 2) + 90);

        stage.addActor(createButton);

        quickButton = new TextButton("Schnelles Spiel", Constants.buttonSkin);
        quickButton.setSize(260, 60);
        quickButton.setPosition(stage.getWidth() / 2 - (quickButton.getWidth() / 2), stage.getHeight() / 2 - (quickButton.getHeight() / 2));

        stage.addActor(quickButton);

        searchButton = new TextButton("Lobby suchen", Constants.buttonSkin);
        searchButton.setSize(260, 60);
        searchButton.setPosition(stage.getWidth() / 2 - (searchButton.getWidth() / 2), stage.getHeight() / 2 - (searchButton.getHeight() / 2) - 90);
        stage.addActor(searchButton);

        logOut = new TextButton("Abmelden", Constants.buttonSkin);
        logOut.setSize(260, 60);
        logOut.setPosition(stage.getWidth() / 2 - (logOut.getWidth() / 2), stage.getHeight() / 2 - (logOut.getHeight() / 2) - 180);
        stage.addActor(logOut);

        Gdx.input.setInputProcessor(stage);
    }

    private void createLobby() throws JSONException {
        OGRacerGame.getInstance().setScreen(new LobbyScreen(ID));
        Client.sendCreateLobby();
    }
}
