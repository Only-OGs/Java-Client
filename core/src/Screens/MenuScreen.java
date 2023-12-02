package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;

public abstract class MenuScreen extends ScreenAdapter {

    private Viewport viewport;

    protected Stage stage;

    protected SpriteBatch batch;

    protected Texture backgroundTexture;

    protected TextButton buttonLeft, buttonMiddle, buttonRight;

    public MenuScreen() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture("Background/BackgroundOGRacerGame.png");
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        if (Constants.music != null) {
            Constants.music.setLooping(true);
            Constants.music.setVolume(0.01f);
            Constants.music.play();
        }
        Constants.title.setSize(Gdx.graphics.getWidth(), 100);
        Constants.title.setPosition(0, stage.getHeight() - 100);
        Constants.title.setAlignment(Align.center);
        stage.addActor(Constants.title);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
        Constants.buttonSkin.dispose();
        Constants.music.dispose();
        Constants.clickButton.dispose();
        Constants.messageReceived.dispose();
    }

    protected void addButton(String btn1, String btn2, String btn3) {

        if (!Objects.equals(btn1, "")) {
            buttonLeft = new TextButton(btn1, Constants.buttonSkin);
            buttonLeft.setSize(250, 60);
            buttonLeft.setPosition((stage.getWidth() - buttonLeft.getWidth()) / 2 - buttonLeft.getWidth() - 50, stage.getHeight() / 2 - (buttonLeft.getHeight()) + 25);
            stage.addActor(buttonLeft);
        }

        if (!Objects.equals(btn2, "")) {
            buttonMiddle = new TextButton(btn2, Constants.buttonSkin);
            buttonMiddle.setSize(250, 60);
            buttonMiddle.setPosition((stage.getWidth() - buttonMiddle.getWidth()) / 2, stage.getHeight() / 2 - (buttonMiddle.getHeight()));
            stage.addActor(buttonMiddle);
        }

        if (!Objects.equals(btn3, "")) {
            buttonRight = new TextButton(btn3, Constants.buttonSkin);
            buttonRight.setSize(250, 60);
            buttonRight.setPosition((stage.getWidth() - buttonRight.getWidth()) / 2 + buttonRight.getWidth() + 50, stage.getHeight() / 2 - (buttonRight.getHeight()) + 25);
            stage.addActor(buttonRight);
        }
    }

    protected void removeButton() {
        if (buttonLeft != null) buttonLeft.remove();
        if (buttonMiddle != null) buttonMiddle.remove();
        if (buttonRight != null) buttonRight.remove();
    }

    protected void buttonListener() {
    }
}
