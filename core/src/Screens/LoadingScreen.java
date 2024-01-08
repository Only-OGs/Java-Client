package Screens;

import Connection.Client;
import Helpers.Constants;
import OGRacerGame.OGRacerGame;
import Screens.Menu.MenuArea.MultiplayerMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LoadingScreen extends ScreenAdapter {

    private final FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private final Stage stage = new Stage(viewport);

    public LoadingScreen(String userID) {
        Gdx.input.setInputProcessor(stage);
        Constants.title.setText("Das Spiel startet gleich ...");
        Constants.title.setSize(Gdx.graphics.getWidth(), 100);
        Constants.title.setPosition(0, stage.getHeight() - 100);
        Constants.title.setAlignment(Align.center);
        stage.addActor(Constants.title);
        OGRacerGame.getInstance().setGameScreen(new GameScreen(userID));
        waitTimer();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        if (!Client.connect) OGRacerGame.getInstance().setScreen(new MultiplayerMenu());

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        super.resize(width, height);
    }

    /**
     * Startet einen Thread wo für 1 Sekunde gewartet wird, um sicherzustellen,
     * dass alle notwendigen Daten vom Server empfangen wurden.
     * Danach wird zum GameScreen gewechselt.
     */
    private void waitTimer() {

        Timer.Task timerTask = new Timer.Task() {
            @Override
            public void run() {
                OGRacerGame.getInstance().setScreen(OGRacerGame.getInstance().getGameScreen());
                Client.clientReady();
            }
        };
        Timer.schedule(timerTask, 1);
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}
