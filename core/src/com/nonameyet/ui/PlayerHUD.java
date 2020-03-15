package com.nonameyet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nonameyet.environment.AssetName;
import com.nonameyet.environment.Assets;
import com.nonameyet.screens.AbstractScreen;
import com.nonameyet.utils.Constants;

public class PlayerHUD implements Screen {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private static final String LIFE_FULL = "FULL";
    private static final String LIFE_EMPTY = "EMPTY";

    private Stage _stage;
    private Viewport _viewport;
    private Camera _camera;

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
        Image lifeEmpty;
        lifeEmpty = new Image(_textureLifeEmpty);
        lifeEmpty.setSize(_textureLifeEmpty.getRegionWidth() * (AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH),
                _textureLifeEmpty.getRegionHeight() * (AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT));

        return lifeEmpty;
    }

    private Image addLifeFull() {
        Image lifeFull;
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
        handleInput(delta);

        renderLife();

        _stage.act(delta);
        _stage.draw();
    }

    private void handleInput(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            if (_hpVal > 0) _hpVal -= 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
            if (_hpVal < _hpCurrentMax) _hpVal += 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            _hpCurrentMax += 1;
            _hpVal = _hpCurrentMax;
        }

    }

    // todo: extract to another class
    private void renderLife() {
        Array<Actor> actors = _stage.getActors();

        float ppi_width = AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH;
        float ppi_height = AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT;

        for (int i = 0; i < _hpCurrentMax; i++) {
            Image lifeFull = addLifeFull();
            Image lifeEmpty = addLifeEmpty();

            // add full lifes
            if (i < _hpVal) {
                positionLife(ppi_width, ppi_height, i, lifeFull, lifeFull.getPrefHeight(), LIFE_FULL);
                compareLifeActors(actors, lifeFull);
            }

            // add empty lifes
            if (i >= _hpVal && i <= _hpCurrentMax) {
                positionLife(ppi_width, ppi_height, i, lifeEmpty, lifeFull.getPrefHeight(), LIFE_EMPTY);
                compareLifeActors(actors, lifeEmpty);
            }
        }

    }

    // todo: extract to another class
    private void positionLife(float ppi_width, float ppi_height, int i, Image lifeFull, float prefHeight, String lifeFull2) {
        lifeFull.setPosition((7 * ppi_width) + i * (prefHeight * ppi_width) + (i > 0 ? (i * (2 * ppi_width)) : 0),
                AbstractScreen.VIEWPORT.physicalHeight - (prefHeight * ppi_height) - (7 * ppi_width));
        lifeFull.setName(lifeFull2 + i);
    }

    // todo: extract to another class
    private void compareLifeActors(Array<Actor> actors, Image life) {
        int i;
        for (i = 0; i < actors.size; i++) {
            String tempActorName = actors.get(i).getName();
            if (tempActorName != null && tempActorName.equals(life.getName())) {
                actors.removeIndex(i);
                break;
            }
        }

        _stage.addActor(life);
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
