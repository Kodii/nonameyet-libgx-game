package com.nonameyet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.nonameyet.NoNameYet;

public class LoadingScreen extends AbstractScreen {

    public LoadingScreen(NoNameYet context) {
        super(context);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0.3f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            context.setScreen(ScreenType.GAME);
        }

    }

}
