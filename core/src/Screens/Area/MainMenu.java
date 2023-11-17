package Screens.Area;

import OGRacerGame.OGRacerGame;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu extends MenuArea {

    public MainMenu() {
        title.setText("Hauptmenu");
        removeButton();
        addButton("Einzelspieler","Einstellungen","Mehrspieler");
        buttonListener();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    protected void buttonListener() {
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);

            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                OGRacerGame.getInstance().setScreen(new SettingMenu());

            }
        });

        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                OGRacerGame.getInstance().setScreen(new MultiplayerMenu());
            }
        });
    }
}
