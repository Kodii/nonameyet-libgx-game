package com.nonameyet.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nonameyet.NoNameYet;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.useGL30 = false;
        config.width = 1920;
        config.height = 1080;

        new LwjglApplication(new NoNameYet(), config);

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }
}
