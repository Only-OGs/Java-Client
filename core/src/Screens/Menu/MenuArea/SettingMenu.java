package Screens.Menu.MenuArea;

import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import Helpers.Constants;
import Screens.Menu.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingMenu extends MenuScreen {

    public static String fullScreenString = "AUS";

    public static boolean fullScreenCheck = false;

    public static String musicString = "AN";

    public static boolean musicCheck = true;

    protected ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final TextButton fullScreen = new TextButton(fullScreenString, Constants.buttonSkin);

    private final Label fullscreenLabel = new Label("Vollbildmodus", Constants.buttonSkin);

    private TextButton music = new TextButton(musicString, Constants.buttonSkin);

    private final Label musicLabel = new Label("Musik", Constants.buttonSkin);

    public SettingMenu() {
        Constants.title.setText("Einstellungen");
        removeButton();
        addButton("Zurueck", "", "");
        setupLabel();
        buttonListener();
    }

    // Setzt Farbe, Größe und Position von Labels.
    private void setupLabel() {

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
}
