package Screens.MenuArea;

import OGRacerGame.OGRacerGame;
import Screens.Constants;
import Screens.GameScreen;
import Screens.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenu extends MenuScreen {

    public MainMenu() {
        Constants.title.setText("Hauptmenu");
        removeButton();
        addButton("Einzelspieler","Einstellungen","Mehrspieler");
        buttonListener();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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