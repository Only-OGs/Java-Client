package Screens.MenuArea;

import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import Screens.Constants;
import Screens.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import javax.swing.*;

public class SettingMenu extends MenuScreen {

    public static String fullScreenString = "AUS";

    public static boolean fullScreenCheck = false;

    public static String musicString = "AN";

    public static boolean musicCheck = true;
    private Slider musicSlider;

    protected ShapeRenderer shapeRenderer = new ShapeRenderer();


    private TextButton fullScreen = new TextButton(fullScreenString, Constants.buttonSkin);
    private Label fullscreenLabel = new Label("Vollbildmodus", Constants.buttonSkin);

    private TextButton music = new TextButton(musicString, Constants.buttonSkin);
    private Label musicLabel = new Label("Musik", Constants.buttonSkin);


    public SettingMenu() {
        Constants.title.setText("Einstellungen");
        removeButton();
        addButton("Zurueck", "", "");
        setup();
        buttonListener();

    }

    private void setup() {

        fullscreenLabel.setColor(StyleGuide.white);
        fullscreenLabel.setFontScale(1.4f);
        fullscreenLabel.setBounds(stage.getWidth()/2 -50,stage.getHeight()/2 -20,100,50);
        fullScreen.setBounds(stage.getWidth()/2 -50 +280, stage.getHeight()/2 -20, 100, 40);


        musicLabel.setColor(StyleGuide.white);
        musicLabel.setFontScale(1.4f);
        musicLabel.setBounds(stage.getWidth()/2 -50,stage.getHeight()/2 -60,100,50);
        music.setBounds(stage.getWidth()/2 -50 +280, stage.getHeight()/2 -60,100, 40);

        stage.addActor(fullScreen);
        stage.addActor(fullscreenLabel);
        stage.addActor(music);
        stage.addActor(musicLabel);

    }


    @Override
    protected void buttonListener() {

        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                OGRacerGame.getInstance().setScreen(new MainMenu());
            }
        });

        fullScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (!fullScreenCheck) {
                    fullScreenCheck = true;
                    fullScreenString = "AN";
                    fullScreen.setText(fullScreenString);
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    OGRacerGame.getInstance().setScreen(new SettingMenu());

                } else {
                    fullScreenCheck = false;
                    fullScreenString = "AUS";
                    fullScreen.setText(fullScreenString);
                    Gdx.graphics.setWindowedMode(1329,886);
                    OGRacerGame.getInstance().setScreen(new SettingMenu());

                }
            }
        });

        music.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!musicCheck) {
                    musicCheck = true;
                    musicString = "AN";
                    music.setText(musicString);
                    Constants.music.setVolume(0.01f);
                } else {
                    musicCheck = false;
                    musicString = "AUS";
                    music.setText(musicString);
                    Constants.music.setVolume(0f);
                }

            }
        });


    }

    @Override
    public void render(float delta) {


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Constants.clickButton.play(0.2f);
            OGRacerGame.getInstance().setScreen(new MainMenu());
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1,1,1,1f);
        shapeRenderer.rect(0, 0,200,800);
        shapeRenderer.end();


        super.render(delta);
    }

    private void addSlider() {
        musicSlider = new Slider(0.0f, 0.7f, 0.01f, false, Constants.buttonSkin);
        musicSlider.setValue(Constants.music.getVolume());
        musicSlider.setPosition(stage.getWidth() / 1.3f, 20);
        musicSlider.setSize(200, 10);

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float volume = musicSlider.getValue();
                Constants.music.setVolume(volume);
            }
        });
        stage.addActor(musicSlider);
    }

}
