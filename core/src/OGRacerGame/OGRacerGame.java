package OGRacerGame;


import Screens.StartScreen;
import Screens.GameScreen;
import Screens.StartScreenFirst;
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
		handleInput();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void handleInput() {
		if(getScreen() instanceof GameScreen gs) {
			gs.checkInput(this);
		} else if(getScreen() instanceof StartScreenFirst sc) {
            sc.checkInput(this);
		}
	}

	public void setup(){
		new StartScreen();
		isRunning = true;
	}

}
