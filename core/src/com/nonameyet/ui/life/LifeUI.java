package com.nonameyet.ui.life;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.AbstractScreen;
import com.nonameyet.utils.Constants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LifeUI implements Disposable {
    private final String TAG = this.getClass().getSimpleName();

    private static final String LIFE_FULL = "FULL";
    private static final String LIFE_EMPTY = "EMPTY";

    private final Stage stage;

    private TextureRegion textureLifeEmpty;
    private TextureRegion textureLifeFull;

    private int hpVal = 3;
    private int hpCurrentMax = 4;

    // events
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public LifeUI(Stage stage) {
        this.stage = stage;
        addLife();
    }

    private void addLife() {
        Texture texture = Assets.manager.get(AssetName.LIFE.getAssetName());

        textureLifeEmpty = new TextureRegion(texture, 0, 0, 7, 7);
        textureLifeFull = new TextureRegion(texture, 7, 0, 7, 7);
    }

    private Image addLifeEmpty() {
        Image lifeEmpty;
        lifeEmpty = new Image(textureLifeEmpty);
        lifeEmpty.setSize(textureLifeEmpty.getRegionWidth() * (AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH),
                textureLifeEmpty.getRegionHeight() * (AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT));

        return lifeEmpty;
    }

    private Image addLifeFull() {
        Image lifeFull;
        lifeFull = new Image(textureLifeFull);
        lifeFull.setSize(textureLifeFull.getRegionWidth() * (AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH),
                textureLifeFull.getRegionHeight() * (AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT));
        return lifeFull;
    }


    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
            if (hpVal < hpCurrentMax) {
                hpVal += 1;
                changes.firePropertyChange(StatusEvent.class.getSimpleName(), null, StatusEvent.ADD_HP);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            if (hpVal > 0) {
                hpVal -= 1;
                changes.firePropertyChange(StatusEvent.class.getSimpleName(), null, StatusEvent.REMOVE_HP);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            hpVal = hpCurrentMax;
            changes.firePropertyChange(StatusEvent.class.getSimpleName(), null, StatusEvent.HEAL_HP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            hpCurrentMax += 1;
            hpVal = hpCurrentMax;
            changes.firePropertyChange(StatusEvent.class.getSimpleName(), null, StatusEvent.UPGRADE_HP);
        }

    }

    public void renderLifes() {
        Array<Actor> actors = stage.getActors();

        float ppi_width = AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH;
        float ppi_height = AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT;

        for (int i = 0; i < hpCurrentMax; i++) {
            Image lifeFull = addLifeFull();
            Image lifeEmpty = addLifeEmpty();

            // add full lifes
            if (i < hpVal) {
                lifePosition(ppi_width, ppi_height, i, lifeFull, lifeFull.getPrefHeight(), LIFE_FULL);
                compareLifeActors(actors, lifeFull);
            }

            // add empty lifes
            if (i >= hpVal && i <= hpCurrentMax) {
                lifePosition(ppi_width, ppi_height, i, lifeEmpty, lifeFull.getPrefHeight(), LIFE_EMPTY);
                compareLifeActors(actors, lifeEmpty);
            }
        }

    }

    private void lifePosition(float ppi_width, float ppi_height, int i, Image lifeFull, float prefHeight, String lifeFull2) {
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

        stage.addActor(life);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


    public void addPropertyChangeListener(
            PropertyChangeListener p) {
        changes.addPropertyChangeListener(p);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener p) {
        changes.removePropertyChangeListener(p);
    }


}
