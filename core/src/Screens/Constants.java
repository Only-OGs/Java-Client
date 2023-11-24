package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Constants {

    public static Viewport viewport = new FitViewport(Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());

    public static final Music music  = Gdx.audio.newMusic(Gdx.files.internal("Music/StartMenuMusic.mp3"));

    public static final Sound clickButton = Gdx.audio.newSound((Gdx.files.internal("Music/clickInterface.mp3")));

    public static final Sound messageReceived = Gdx.audio.newSound((Gdx.files.internal("Music/messageReceived.wav")));
    public static final Skin buttonSkin = new Skin(Gdx.files.internal("ButtonStyle/quantum-horizon/skin/quantum-horizon-ui.json"));

    public static final Skin buttonSkin2 = new Skin(Gdx.files.internal("ButtonStyle/lgdxs/skin/lgdxs-ui.json"));


    public static Stage stage = new Stage(viewport);


    public static Label title = new Label("", Constants.buttonSkin, "title");

    public Constants() {}
}
