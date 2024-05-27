package com.javlovers.bcfs;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.javlovers.bcfs.Others.GlobalEntities;
import com.javlovers.bcfs.Screens.BackEnd.DB.LocalHostConnection;
import com.javlovers.bcfs.Screens.BackEnd.Globals.DBHelpers;
import com.javlovers.bcfs.Screens.BackEnd.Main.User;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		// DB SetUp
		DBHelpers.setGlobalConnection(new LocalHostConnection());
		//Change This
		User.setCurrentUser("Mana",1);
		GlobalEntities.setCurrentUser(User.getCurrUser());
		//
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1280,720);
		config.useVsync(true);
		config.setResizable(false);
		config.setTitle("Big Cock Fighting Simulator");
		new Lwjgl3Application(new BCFS(), config);
	}
}
