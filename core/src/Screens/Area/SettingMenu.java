package Screens.Area;

import OGRacerGame.OGRacerGame;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingMenu extends MenuArea {

    private Label clickVolume;

    private Label musicVolume;

    private Label allSounds;

    private Label windowsSize;

    private Slider musicSlider;
    private Slider soundSlider;

    public SettingMenu() {
        title.setText("Einstellungen");
        removeButton();
        addButton("Zurueck", "", "");
        buttonListener();
    }

    @Override
    protected void buttonListener() {

        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                OGRacerGame.getInstance().setScreen(new MainMenu());
            }
        });
    }

    private void addSlider() {
        musicSlider = new Slider(0.0f, 0.7f, 0.01f, false, buttonSkin);
        musicSlider.setValue(music.getVolume());
        musicSlider.setPosition(stage.getWidth() / 1.3f, 20);
        musicSlider.setSize(200, 10);

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float volume = musicSlider.getValue();
                music.setVolume(volume);
            }
        });
        stage.addActor(musicSlider);
    }
}
