package OGRacerGame;


import Screens.GameScreen;
import Screens.LobbyScreen;
import Screens.MenuArea.MainMenu;
import com.badlogic.gdx.Game;

public class OGRacerGame extends Game {
	private static OGRacerGame instance;

	private GameScreen gameScreen;


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
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}
}
