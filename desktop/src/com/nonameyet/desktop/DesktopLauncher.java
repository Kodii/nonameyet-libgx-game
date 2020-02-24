package com.nonameyet.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nonameyet.NoNameYet;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "NoNameYet";
        config.useGL30 = false;
        config.width = 1280;
        config.height = 720;

        new LwjglApplication(new NoNameYet(), config);

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }
}
