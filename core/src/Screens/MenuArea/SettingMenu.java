package Screens.MenuArea;

import OGRacerGame.OGRacerGame;
import Screens.Constants;
import Screens.MenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingMenu extends MenuScreen {

    private Label clickVolume;

    private Label musicVolume;

    private Label allSounds;

    private Label windowsSize;

    private Slider musicSlider;

    private Slider soundSlider;

    public SettingMenu() {
        Constants.title.setText("Einstellungen");
        removeButton();
        addButton("Zurueck", "", "");
        buttonListener();
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
    }

    private void addSlider() {
        musicSlider = new Slider(0.0f, 0.7f, 0.01f, false, Constants.buttonSkin);
        musicSlider.setValue(Constants.music.getVolume());
        musicSlider.setPosition( stage.getWidth() / 1.3f, 20);
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
