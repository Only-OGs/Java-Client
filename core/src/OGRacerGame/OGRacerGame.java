package OGRacerGame;


import Screens.LobbyScreen;
import Screens.MenuArea.MainMenu;
import com.badlogic.gdx.Game;

public class OGRacerGame extends Game {
	private static OGRacerGame instance;


	public boolean isRunning = false;


	private OGRacerGame(){

	}

	public static OGRacerGame getInstance() {
		if (instance == null) {
			instance = new OGRacerGame();
		}
		return instance;
	}
	@Override
	public void create () {
		setup();
	}


	@Override
	public void render() {
		super.render();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void setup(){
		setScreen(new MainMenu());
		isRunning = true;
	}
}
