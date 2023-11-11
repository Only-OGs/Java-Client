package OGRacerGame;


import Connection.Client;
import Screens.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class OGRacerGame extends Game {

	private GameScreen gameScreen;
	@Override
	public void create () {
		Client.connect();
		gameScreen = new GameScreen();
		setScreen(gameScreen);

	}

	@Override
	public void dispose() {
		getScreen().dispose();
	}

	@Override
	public void render() {
		super.render();
		//ÄNDERUNG ZUM TESTEN
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width,height);
	}
}
