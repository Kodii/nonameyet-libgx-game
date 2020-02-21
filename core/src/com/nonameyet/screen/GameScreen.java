package com.nonameyet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.nonameyet.NoNameYet;

public class GameScreen extends AbstractScreen {

    public GameScreen(NoNameYet context) {
        super(context);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            context.setScreen(ScreenType.LOADING);
        }
    }
}
