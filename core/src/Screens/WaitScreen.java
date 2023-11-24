package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Road.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;

public class WaitScreen extends ScreenAdapter {

    private final String ID;

    private int count = 0;


    public WaitScreen(String ID) {
        this.ID = ID;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        if("joined".equals(Client.joinLobby)){
            OGRacerGame.getInstance().setScreen(new LobbyScreen(ID));
            Client.joinLobby = "";
        }

        if (200 == count) {
            OGRacerGame.getInstance().setScreen(new SearchScreen(ID));
        }
        count++;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
