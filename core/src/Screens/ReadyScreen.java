package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Screens.MenuArea.MultiplayerMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class ReadyScreen extends ScreenAdapter {

    private FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    private Stage stage = new Stage(viewport);

    private Timer.Task timerTask;

    public ReadyScreen() {
        Gdx.input.setInputProcessor(stage);
        Constants.title.setText("Das Spiel startet gleich ...");
        Constants.title.setSize(Gdx.graphics.getWidth(), 100);
        Constants.title.setPosition(0, stage.getHeight() - 100);
        Constants.title.setAlignment(Align.center);
        stage.addActor(Constants.title);
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

    private void waitTimer() {

        timerTask = new Timer.Task() {
            @Override
            public void run() {
                OGRacerGame.getInstance().setScreen(new GameScreen());
            }
        };

        // Starte den Timer mit einer Verzögerung von 10 Sekunden
        Timer.schedule(timerTask, 5);  // muss 10
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}
