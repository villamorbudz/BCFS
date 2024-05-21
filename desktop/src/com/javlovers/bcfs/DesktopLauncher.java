package com.javlovers.bcfs;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.javlovers.bcfs.BCFS;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1280,720);
		config.useVsync(true);
		config.setResizable(false);
		config.setTitle("Big Cock Fighting Simulator");
		new Lwjgl3Application(new BCFS(), config);
	}
}
