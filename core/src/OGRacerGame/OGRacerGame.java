package OGRacerGame;


import Connection.Client;
import Screens.GameScreen;
import Screens.StartScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class OGRacerGame extends Game {

	public boolean isRunning = false;
	private GameScreen gameScreen;
	@Override
	public void create () {
		Client.connect();
		gameScreen = new GameScreen();
		setScreen(gameScreen);
		isRunning = true;
	}

	@Override
	public void dispose() {
		getScreen().dispose();
	}

	@Override
	public void render() {
		super.render();
		handleInput();

	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width,height);
	}

	public void handleInput() {
		if(getScreen() instanceof GameScreen gs) {
			gs.checkInput(this);
		} else if(getScreen() instanceof StartScreen sc) {
            sc.checkInput(this);
		}
	}

}
