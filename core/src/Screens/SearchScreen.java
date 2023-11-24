package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.json.JSONException;

public class SearchScreen extends ScreenAdapter {

    private TextButton sendButton = new TextButton("Senden", Constants.buttonSkin);
    private TextField messageField = new TextArea("", Constants.buttonSkin);


    private Stage stage;

    private final String ID;



    public SearchScreen(String ID) {
        this.ID = ID;
        FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        messageField.setBounds((float) Gdx.graphics.getWidth() /2 -(75), (float) Gdx.graphics.getHeight() /2,150,50);
        sendButton.setBounds((float) Gdx.graphics.getWidth() /2-(75), (float) Gdx.graphics.getHeight() /2-60,150,50);

        sendListener();

        stage.addActor(messageField);
        stage.addActor(sendButton);
        Gdx.input.setInputProcessor(stage);
    }


    private void sendListener(){
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (messageField.getText().length() > 1) {
                    try {

                        Client.joinLobby(messageField.getText());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    messageField.setText("");
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


        if("joined".equals(Client.joinLobby)){
            OGRacerGame.getInstance().setScreen(new LobbyScreen(ID));
            Client.status = "";
            System.out.println("joined");
        }

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
