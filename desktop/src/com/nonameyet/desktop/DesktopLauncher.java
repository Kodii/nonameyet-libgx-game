package com.nonameyet.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nonameyet.NoNameYet;
import com.nonameyet.preferences.PlayerPref;
import com.nonameyet.preferences.Preferences;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.useGL30 = false;
        config.vSyncEnabled = true;
        config.width = 1366;
        config.height = 768;

        new LwjglApplication(new NoNameYet(), config);

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }
}
