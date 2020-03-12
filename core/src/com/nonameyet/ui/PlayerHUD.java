package com.nonameyet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nonameyet.environment.AssetName;
import com.nonameyet.environment.Assets;
import com.nonameyet.screens.AbstractScreen;
import com.nonameyet.utils.Constants;

public class PlayerHUD implements Screen {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage _stage;
    private Viewport _viewport;
    private Camera _camera;

    private Table _table;

    private TextureRegion _textureLifeEmpty;
    private TextureRegion _textureLifeFull;

    private int _hpVal = 3;

    private int _hpCurrentMax = 4;

    public PlayerHUD(Camera camera) {
        _camera = camera;

        //setup the HUD viewport using a new camera seperate from gamecam
        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);

        cameraFrame();
        addLife();
        renderLife();
    }

    private void cameraFrame() {
        Texture texture = Assets.manager.get(AssetName.CAMERA_FRAME.getAssetName());

        TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());

        Image cameraFrame = new Image(textureRegion);
        cameraFrame.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        _stage.addActor(cameraFrame);
    }

    private void addLife() {
        Texture texture = Assets.manager.get(AssetName.LIFE.getAssetName());

        _textureLifeEmpty = new TextureRegion(texture, 0, 0, 7, 7);
        _textureLifeFull = new TextureRegion(texture, 7, 0, 7, 7);
    }

    private Image addLifeEmpty() {
        Image lifeEmpty = new Image();
        lifeEmpty = new Image(_textureLifeEmpty);
        lifeEmpty.setSize(_textureLifeEmpty.getRegionWidth() * (AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH),
                _textureLifeEmpty.getRegionHeight() * (AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT));

        return lifeEmpty;
    }

    private Image addLifeFull() {
        Image lifeFull = new Image();
        lifeFull = new Image(_textureLifeFull);
        lifeFull.setSize(_textureLifeFull.getRegionWidth() * (AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH),
                _textureLifeFull.getRegionHeight() * (AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT));

        return lifeFull;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        _stage.act(delta);
        _stage.draw();
    }

    private void renderLife() {

        float ppi_width = AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH;
        float ppi_height = AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT;
        Gdx.app.log(TAG, "ppi_width: " + ppi_width);
        Gdx.app.log(TAG, "ppi_height: " + ppi_height);

        for (int i = 0; i < _hpCurrentMax; i++) {
            Image lifeFull = addLifeFull();


            lifeFull.setPosition((7 * ppi_width) + i * (lifeFull.getPrefHeight() * ppi_width) + (i > 0 ? (i * (2 * ppi_width)) : 0),
                    AbstractScreen.VIEWPORT.physicalHeight - (lifeFull.getPrefHeight() * ppi_height) - (7 * ppi_width));

            _stage.addActor(lifeFull);
        }

    }

    @Override
    public void resize(int width, int height) {
        _stage.getViewport().update(width, height, true);
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
        _stage.dispose();
    }
}
