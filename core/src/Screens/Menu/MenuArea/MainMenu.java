package Screens.Menu.MenuArea;

import OGRacerGame.OGRacerGame;
import Helpers.Constants;
import Screens.GameScreen;
import Screens.Menu.MenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu extends MenuScreen {

    public MainMenu() {
        Constants.title.setText("Hauptmenu");
        removeButton();
        addButton("Einzelspieler","Einstellungen","Mehrspieler");
        buttonListener();
    }

    @Override
    protected void buttonListener() {
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.music.stop();
                removeButton();
                OGRacerGame.getInstance().setScreen(new GameScreen());
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                removeButton();
                OGRacerGame.getInstance().setScreen(new SettingMenu());
            }
        });

        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                removeButton();
                OGRacerGame.getInstance().setScreen(new MultiplayerMenu());
            }
        });
    }
}
