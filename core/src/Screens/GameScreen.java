package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.json.JSONException;

public class GameScreen extends ScreenAdapter implements IInputHandler{

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

    /** [GameScreen] Fragt ab ob eine Taste gedruckt wurde/ist */
    @Override
    public void checkInput(OGRacerGame game) {
        // Pausieren/Fortfahren des Spiels
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.isRunning = !game.isRunning;
            System.out.println("DDD");
            //Menü anzeigen
        }
        // Wenn das Spiel pausiert ist, sollen keine Eingaben zum steuern des Autos abgefragt werden
        if(!game.isRunning) return;


		/*	Durch die Struktur ist es unmöglich
			gleichzeitig zu bremsen und zu beschleunigen bzw.
			gleichzeitig nach Links und nach Rechts zu fahren
		*/
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            //Beschleunigen
        } else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            //Bremsen
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            //Nach links fahren
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            //Nach Rechts fahren
        }
    }
}
