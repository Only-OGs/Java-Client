package Screens.Area;

import OGRacerGame.OGRacerGame;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SettingMenu extends MenuArea {

    private Slider slider;
    public SettingMenu() {
        title.setText("Einstellungen");
        removeButton();
        addButton("", "Zuruck", "");
        buttonListener();
    }

    @Override
    protected void buttonListener() {

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                OGRacerGame.getInstance().setScreen(new MainMenu());
            }
        });
    }

    private void addSlider() {
        slider = new Slider(0.0f, 0.7f, 0.01f, false, buttonSkin);
        slider.setValue(music.getVolume());
        slider.setPosition(stage.getWidth() / 1.3f, 20);
        slider.setSize(200, 10);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float volume = slider.getValue();
                music.setVolume(volume);
            }
        });
        stage.addActor(slider);
    }
}
