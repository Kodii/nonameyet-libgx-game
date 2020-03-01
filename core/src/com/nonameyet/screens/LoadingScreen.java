package com.nonameyet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.nonameyet.NoNameYet;
import com.nonameyet.environment.Assets;

public class LoadingScreen implements Screen {
    private static final String TAG = LoadingScreen.class.getSimpleName();
    private final NoNameYet game;

    private final Assets assets;

    public LoadingScreen(NoNameYet game) {
        this.game = game;

        assets = game.getAssets();
        assets.load();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (assets.manager.update()) {
            game.setScreen(ScreenType.GAME);
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}