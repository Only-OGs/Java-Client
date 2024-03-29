package Screens.Menu;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Helpers.Constants;
import Screens.Menu.MenuArea.LobbyMenu;
import Screens.Menu.MenuArea.MultiplayerMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.json.JSONException;

public class SearchScreen extends ScreenAdapter {

    private final TextButton sendButton = new TextButton("Senden", Constants.buttonSkin);

    private final TextButton backButton = new TextButton("Zurueck", Constants.buttonSkin);

    private final TextField messageField = new TextArea("", Constants.buttonSkin);

    private final FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    private final Stage stage = new Stage(viewport);

    private final Label serverMessage = new Label("", Constants.buttonSkin);

    private final Label lobbyCodeTitle = new Label("Lobby ID Eingabe", Constants.buttonSkin);

    private final String ID;

    private Label idLabel;

    private int count = 0;

    public SearchScreen(String ID) {
        this.ID = ID;
        Gdx.input.setInputProcessor(stage);
        setup();
        buttonListener();
    }

    // Setzt die Größen der Buttons und des Eingabefelds.
    private void setup(){
        messageField.setBounds((float) Gdx.graphics.getWidth() / 2 - (75), (float) Gdx.graphics.getHeight() / 2, 150, 22);
        sendButton.setBounds((float) Gdx.graphics.getWidth() / 2 - (75), (float) Gdx.graphics.getHeight() / 2 - 60, 150, 50);
        backButton.setBounds((float) Gdx.graphics.getWidth() / 1.3f, (float) Gdx.graphics.getHeight() / 2 - 60, 150, 50);

        stage.addActor(messageField);
        stage.addActor(backButton);
        stage.addActor(sendButton);

        setupLabel();
    }

    // Setzt alle Labels für den Screen.
    private void setupLabel(){

        idLabel = new Label("ID: " + ID, Constants.buttonSkin);
        idLabel.setBounds(stage.getWidth() / 1.3f,  stage.getHeight() - 65,190, 40);
        stage.addActor(idLabel);

        serverMessage.setBounds(stage.getWidth() / 30f, 10,190, 40);
        lobbyCodeTitle.setBounds(messageField.getX(),messageField.getY() + messageField.getHeight() +5,150,20);
        lobbyCodeTitle.setFontScale(0.8f);

        Constants.title.setText("Lobby suche");
        Constants.title.setBounds(0, stage.getHeight() - 100,Gdx.graphics.getWidth(), 100);
        Constants.title.setAlignment(Align.center);

        stage.addActor(Constants.title);
        stage.addActor(lobbyCodeTitle);
        stage.addActor(idLabel);
        stage.addActor(serverMessage);
    }

    // Hier wird auf Button Ereignisse reagiert.
    private void buttonListener() {
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                if (messageField.getText().length() > 1) {

                    try {
                        Client.playerString = "";
                        Client.searchLobbyCodeMessage = "";
                        count = 0;
                        Client.joinLobby(messageField.getText());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    messageField.setText("");
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                Client.searchLobbyCodeMessage = "";
                Client.searchLobbyCodeStatus = "";
                OGRacerGame.getInstance().setScreen(new LobbyMenu(ID));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        if(!Client.connect) OGRacerGame.getInstance().setScreen(new MultiplayerMenu());

        if("success".equals(Client.searchLobbyCodeStatus)){
            Client.searchLobbyCodeStatus = "";
            Client.searchLobbyCodeMessage = "";
            OGRacerGame.getInstance().setScreen(new LobbyScreen(ID));
        }

        if (200 == count) {
            serverMessage.setText("");
            count++;
        }else if( count < 200){
            serverMessage.setText(Client.searchLobbyCodeMessage);
            count++;
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}