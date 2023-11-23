package de.ogracer.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import OGRacerGame.OGRacerGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("OG Racer");
		config.setWindowIcon("Icon/OGRacerIcon.png");
		config.setWindowedMode(1024,780);
		config.setResizable(true);
		new Lwjgl3Application(OGRacerGame.getInstance(), config);
	}
}