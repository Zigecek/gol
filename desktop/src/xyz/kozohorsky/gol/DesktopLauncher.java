package xyz.kozohorsky.gol;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import static xyz.kozohorsky.gol.Gol.SLEEP_TIME;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS((int) (1/SLEEP_TIME*2));
		config.setTitle("Game Of Life");
		config.setWindowedMode(1200, 400);
		new Lwjgl3Application(new Gol(), config);
	}
}
