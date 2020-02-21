package com.nonameyet.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.nonameyet.NoNameYet;

public class AbstractScreen implements Screen {
    protected final NoNameYet context;
    protected final FitViewport viewport;

    public AbstractScreen(final NoNameYet context) {
        this.context = context;
        viewport = context.getScreenViewport();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height);

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
