package Screens;

import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import Screens.MenuArea.MultiplayerMenu;
import Screens.MenuArea.SettingMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestScreen extends ScreenAdapter {

    private Stage stage;

    private TextButton down = new TextButton("\\/", Constants.buttonSkin);

    private TextButton top = new TextButton("/\\", Constants.buttonSkin);

    private TextButton sendButton = new TextButton("Senden", Constants.buttonSkin);

    private TextArea chatArea = new TextArea("Willkommen\n\n", Constants.buttonSkin);

    private TextArea sendMessage = new TextArea("", Constants.buttonSkin);

    private ScrollPane scrollPane = new ScrollPane(chatArea, Constants.buttonSkin);

    public TestScreen() {


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        scrollPane.setBounds(Gdx.graphics.getWidth() / 1.8f, 300, 310, 350);

        chatArea.setBounds(Gdx.graphics.getWidth() / 1.8f, 300, 310, 350);
        chatArea.setDisabled(true);

        sendButton.setBounds(Gdx.graphics.getWidth() / 1.8f + chatArea.getWidth(), chatArea.getY() - 75, 130, 50);
        sendMessage.setBounds(Gdx.graphics.getWidth() / 1.8f, chatArea.getY() - 100, chatArea.getWidth(), 100);

        stage.addActor(sendMessage);
        stage.addActor(sendButton);
        stage.addActor(chatArea);

        top.setBounds(50,50,50,50);
        down.setBounds(200,200,50,50);

        stage.addActor(top);
        stage.addActor(down);
        buttonListener();

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    protected void buttonListener() {
        top.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                chatArea.setCursorPosition(0);
            }
        });

        down.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                chatArea.setCursorPosition(chatArea.getText().length());

            }
        });

        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!sendMessage.getText().isEmpty()){
                    chatArea.appendText(sendMessage.getText() + "\n\n");
                    sendMessage.setText("");
                }
            }
        });
    }
}