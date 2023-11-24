package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Screens.MenuArea.LobbyMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.json.JSONException;

public class SearchScreen extends ScreenAdapter {

    private TextButton sendButton = new TextButton("Senden", Constants.buttonSkin);

    private TextButton backButton = new TextButton("Zurueck", Constants.buttonSkin);

    private TextField messageField = new TextArea("", Constants.buttonSkin);

    private Stage stage;

    private final String ID;


    public SearchScreen(String ID) {
        this.ID = ID;
        FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        messageField.setBounds((float) Gdx.graphics.getWidth() / 2 - (75), (float) Gdx.graphics.getHeight() / 2, 150, 50);
        sendButton.setBounds((float) Gdx.graphics.getWidth() / 2 - (75), (float) Gdx.graphics.getHeight() / 2 - 60, 150, 50);

        backButton.setBounds((float) Gdx.graphics.getWidth() / 1.3f, (float) Gdx.graphics.getHeight() / 2 - 60, 150, 50);
        buttonListener();
        stage.addActor(messageField);
        stage.addActor(backButton);
        stage.addActor(sendButton);
        Gdx.input.setInputProcessor(stage);
    }


    private void buttonListener() {
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                if (messageField.getText().length() > 1) {
                    try {

                        Client.joinLobby(messageField.getText());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    messageField.setText("");
                    OGRacerGame.getInstance().setScreen(new WaitScreen(ID));
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                OGRacerGame.getInstance().setScreen(new LobbyMenu(ID));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
