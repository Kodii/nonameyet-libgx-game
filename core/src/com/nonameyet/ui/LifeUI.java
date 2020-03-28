package com.nonameyet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.environment.AssetName;
import com.nonameyet.environment.Assets;
import com.nonameyet.screens.AbstractScreen;
import com.nonameyet.utils.Constants;

class LifeUI implements Disposable, StatusSubject {
    private static final String TAG = LifeUI.class.getSimpleName();

    private static final String LIFE_FULL = "FULL";
    private static final String LIFE_EMPTY = "EMPTY";

    private final Stage _stage;

    private TextureRegion _textureLifeEmpty;
    private TextureRegion _textureLifeFull;

    private int _hpVal = 3;
    private int _hpCurrentMax = 4;

    // events
    private Array<StatusListener> listeners = new Array<>();

    LifeUI(Stage stage) {
        this._stage = stage;

        addLife();
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


    void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
            if (_hpVal < _hpCurrentMax) {
                _hpVal += 1;
                Gdx.app.log(TAG, " ADD_HP = _hpVal: " + _hpVal);
                notify(StatusListener.StatusEvent.ADD_HP);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            if (_hpVal > 0) {
                _hpVal -= 1;
                Gdx.app.log(TAG, " REMOVE_HP = _hpVal: " + _hpVal);
                notify(StatusListener.StatusEvent.REMOVE_HP);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            _hpVal = _hpCurrentMax;
            Gdx.app.log(TAG, " HEAL_HP = _hpVal: " + _hpVal);
            notify(StatusListener.StatusEvent.HEAL_HP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            _hpCurrentMax += 1;
            _hpVal = _hpCurrentMax;
            Gdx.app.log(TAG, " UPGRADE_HP = _hpVal: " + _hpVal);
            Gdx.app.log(TAG, " UPGRADE_HP = _hpCurrentMax: " + _hpCurrentMax);
            notify(StatusListener.StatusEvent.UPGRADE_HP);
        }

    }

    void renderLifes() {
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

    private void positionLife(float ppi_width, float ppi_height, int i, Image lifeFull, float prefHeight, String lifeFull2) {
        lifeFull.setPosition((7 * ppi_width) + i * (prefHeight * ppi_width) + (i > 0 ? (i * (2 * ppi_width)) : 0),
                AbstractScreen.VIEWPORT.physicalHeight - (prefHeight * ppi_height) - (7 * ppi_width));
        lifeFull.setName(lifeFull2 + i);
    }

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
    public void dispose() {
        _stage.dispose();
    }


    @Override
    public void attachListener(StatusListener listener) {
        listeners.add(listener);
    }

    @Override
    public void detachListener(StatusListener listener) {
        listeners.removeValue(listener, true);
    }

    @Override
    public void notify(StatusListener.StatusEvent event) {

        for (StatusListener listener : listeners) listener.update(event);
    }
}
