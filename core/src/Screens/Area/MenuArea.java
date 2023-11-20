package Screens.Area;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;

public abstract class MenuArea extends ScreenAdapter {

    private Viewport viewport;
    protected Stage stage;
    protected SpriteBatch batch;
    protected Texture backgroundTexture;
    protected Sound clickSound;
    static Music music;
    protected Label title;

    protected TextButton buttonLeft, buttonMiddle, buttonRight;
    protected Skin buttonSkin;

    public MenuArea() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture("background/BackgroundOGRacerGame.png");
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        buttonSkin = new Skin(Gdx.files.internal("ButtonStyle/quantum-horizon/skin/quantum-horizon-ui.json"));
        clickSound = Gdx.audio.newSound((Gdx.files.internal("Music/clickInterface.mp3")));
        if(music == null){
            music = Gdx.audio.newMusic(Gdx.files.internal("Music/StartMenuMusic.mp3"));
            music.setLooping(true);
            music.setVolume(0.01f);
            music.play();
        }
        addLabel();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();

        stage.act();
        stage.draw();
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.render(delta);
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
        buttonSkin.dispose();
        music.dispose();
        clickSound.dispose();

    }

    protected void addButton(String btn1, String btn2, String btn3) {

        if(!Objects.equals(btn1, "")){
            buttonLeft = new TextButton(btn1, buttonSkin);
            buttonLeft.setSize(250, 60);
            buttonLeft.setPosition((stage.getWidth() - buttonLeft.getWidth()) / 2 - buttonLeft.getWidth() - 50, stage.getHeight() / 2 - (buttonLeft.getHeight()) + 25);
            stage.addActor(buttonLeft);
        }

        if(!Objects.equals(btn2, "")){
            buttonMiddle = new TextButton(btn2, buttonSkin);
            buttonMiddle.setSize(250, 60);
            buttonMiddle.setPosition((stage.getWidth() - buttonMiddle.getWidth()) / 2, stage.getHeight() / 2 - (buttonMiddle.getHeight()));
            stage.addActor(buttonMiddle);
        }

        if(!Objects.equals(btn3, "")){
            buttonRight = new TextButton(btn3, buttonSkin);
            buttonRight.setSize(250, 60);
            buttonRight.setPosition((stage.getWidth() - buttonRight.getWidth()) / 2 + buttonRight.getWidth() + 50, stage.getHeight() / 2 - (buttonRight.getHeight()) + 25);
            stage.addActor(buttonRight);

        }

        Gdx.input.setInputProcessor(stage);
    }

    protected void removeButton(){
        if(buttonLeft!= null)  buttonLeft.remove();
        if(buttonMiddle != null)  buttonMiddle.remove();
        if(buttonRight != null)  buttonRight.remove();
    }

    protected void buttonListener(){

    }

    private void addLabel() {
        title = new Label("", buttonSkin, "title");
        title.setSize(Gdx.graphics.getWidth(), 100);
        title.setPosition(0, stage.getHeight() - 100);
        title.setAlignment(Align.center);
        stage.addActor(title);
    }
}
