package com.nonameyet.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.nonameyet.NoNameYet;

public class AbstractScreen implements Screen {
    protected final NoNameYet context;

    protected final OrthographicCamera cam;
    protected final FitViewport viewPort;

    protected final World world;
    protected final Box2DDebugRenderer box2DDebugRenderer;

    public AbstractScreen(final NoNameYet context) {
        this.context = context;

        // create cam used to follow player
        this.cam = new OrthographicCamera();
        this.cam.zoom = 100f;

        // create a FitViewport to maintain virtual aspect ratio
        this.viewPort = new FitViewport(16, 9, cam);

        this.world = context.getWorld();
        this.box2DDebugRenderer = context.getBox2DDebugRenderer();

    }

    @Override
    public void show() {

    }

    public void handleInput(float delta) {

    }

    public void update(float delta) {
        handleInput(delta);

        cam.update();

    }

    @Override
    public void render(float delta) {
        update(delta);

    }

    @Override
    public void resize(final int width, final int height) {
        viewPort.update(width, height);

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
