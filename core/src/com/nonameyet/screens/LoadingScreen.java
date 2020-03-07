package com.nonameyet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.nonameyet.NoNameYet;
import com.nonameyet.environment.Assets;

public class LoadingScreen extends AbstractScreen {
    private static final String TAG = LoadingScreen.class.getSimpleName();

    private final Assets _assets;

    public LoadingScreen(final NoNameYet game) {
        super(game);

        _assets = game.getAssets();
        _assets.load();

    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (_assets.manager.update()) {
            game.setScreen(ScreenType.MAIN_MENU);
        }

    }
}
