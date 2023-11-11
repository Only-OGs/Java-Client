package Screens;

import Connection.Client;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.json.JSONException;

public class GameScreen extends ScreenAdapter {

    private static Camera camera;
    private static Viewport viewport;

    private SpriteBatch batch;
    private Texture background;
    private int backgroundWitdh = 500;

    private int backgroundHeight = 500;


    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport();
        batch = new SpriteBatch();
        background = new Texture("background/hills.png");

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(background, 0,0,backgroundWitdh,backgroundHeight);
        batch.end();

        if(Client.socket.connected()){
            try {
                Client.emitCoordinate(1,1,1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose () {
        batch.dispose();
        background.dispose();
    }
}
