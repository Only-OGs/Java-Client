package Screens.MenuArea;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Screens.Constants;
import Screens.LobbyScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import org.json.JSONException;

public class LobbyMenu extends MultiplayerMenu {
    

    private Button createButton, quickButton, searchButton, logOut;


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
        updateStatusMessage = true;
        addIdLabel();
        Constants.music.stop();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Constants.stage.act();
        Constants.stage.draw();
        Constants.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        if (200 == count) {
            updateStatusMessage = false;
            if (serverMessage != null) serverMessage.remove();
            Client.statusMessage = "";
        }
        count++;
        if (updateStatusMessage) addServerMessage();


        if(!Client.connect){
            OGRacerGame.getInstance().setScreen(new MultiplayerMenu());
        }

    }

    public void addIdLabel() {
        idLabel = new Label("ID: " + ID, Constants.buttonSkin);
        idLabel.setSize(190, 40);
        idLabel.setPosition( Constants.stage.getWidth() / 1.3f,  Constants.stage.getHeight() - 65);
        Constants.stage.addActor(idLabel);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    protected void lobbyButtonListener() {
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
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


            }
        });

        searchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
            }
        });

        logOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                if (serverMessage != null) serverMessage.remove();
                try {
                    Client.sendLogOut();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                OGRacerGame.getInstance().setScreen(new MultiplayerMenu());


            }
        });
    }

    private void lobbySelectionButton() {

        createButton = new TextButton("Lobby erstellen", Constants.buttonSkin);
        createButton.setSize(260, 60);
        createButton.setPosition( Constants.stage.getWidth() / 2 - (createButton.getWidth() / 2),  Constants.stage.getHeight() / 2 - (createButton.getHeight()/2)+90);
        Constants.stage.addActor(createButton);

        quickButton = new TextButton("Schnelles Spiel", Constants.buttonSkin);
        quickButton.setSize(260, 60);
        quickButton.setPosition( Constants.stage.getWidth() / 2 - (quickButton.getWidth() / 2),  Constants.stage.getHeight() / 2 - (quickButton.getHeight()/2));
        Constants.stage.addActor(quickButton);

        searchButton = new TextButton("Lobby suchen", Constants.buttonSkin);
        searchButton.setSize(260, 60);
        searchButton.setPosition( Constants.stage.getWidth() / 2 - (searchButton.getWidth() / 2),  Constants.stage.getHeight() / 2 - (searchButton.getHeight()/2)-90);
        Constants.stage.addActor(searchButton);

        logOut = new TextButton("Abmelden", Constants.buttonSkin);
        logOut.setSize(260, 60);
        logOut.setPosition( Constants.stage.getWidth() / 2 - (logOut.getWidth() / 2),  Constants.stage.getHeight() / 2 - (logOut.getHeight()/2)-180);
        Constants.stage.addActor(logOut);

        Gdx.input.setInputProcessor( Constants.stage);
    }

    private void createLobby() throws JSONException {

        Client.sendCreateLobby();
        LobbyScreen.idList.add(ID);
        OGRacerGame.getInstance().setScreen(new LobbyScreen(ID));
    }

    private void searchLobbyButton(){
        Constants.title.setText("Lobby Auswahl");
    }

    private void quickGameButton(){
        Constants.title.setText("Lobby Auswahl");
    }

}
