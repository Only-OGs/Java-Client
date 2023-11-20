package Screens;

import OGRacerGame.OGRacerGame;
import Screens.Area.MainMenu;

public class StartScreen {

    public StartScreen() {
        OGRacerGame.getInstance().setScreen(new MainMenu());
    }
}
