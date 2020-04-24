package com.nonameyet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.chest.ChestInventoryUI;
import com.nonameyet.ui.chest.ChestWindowEvent;
import com.nonameyet.ui.life.LifeUI;
import com.nonameyet.ui.life.StatusListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlayerHUD implements Screen, StatusListener, PropertyChangeListener {
    private static final String TAG = PlayerHUD.class.getSimpleName();
    private GameScreen screen;

    private Stage stage;
    private Viewport viewport;
    private Camera camera;

    private LifeUI lifeUI;
    private ChestInventoryUI chestInventoryUI;

    public PlayerHUD(Camera camera, GameScreen screen) {
        this.camera = camera;
        this.screen = screen;

        //setup the HUD viewport using a new camera seperate from gamecam
        viewport = new ScreenViewport(this.camera);
        stage = new Stage(viewport);

        lifeUI = new LifeUI(stage);

        cameraFrame();

        chestInventoryUI = new ChestInventoryUI();
        stage.addActor(chestInventoryUI);

        // listeners
        lifeUI.attachListener(this);

        screen.getMapMgr().getWorldContactListener().addPropertyChangeListener(this);

    }

    private void cameraFrame() {
        Texture texture = Assets.manager.get(AssetName.CAMERA_FRAME.getAssetName());

        TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());

        Image cameraFrame = new Image(textureRegion);
        cameraFrame.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(cameraFrame);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        lifeUI.handleInput(delta);

        lifeUI.renderLifes();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        lifeUI.detachListener(this);
        stage.dispose();
    }

    @Override
    public void update(StatusEvent event) {
        switch (event) {
            case ADD_HP:
                Gdx.app.log(TAG, "event: ADD_HP");
                break;

            case REMOVE_HP:
                Gdx.app.log(TAG, "event: REMOVE_HP");
                break;

            case HEAL_HP:
                Gdx.app.log(TAG, "event: HEAL_HP");
                break;

            case UPGRADE_HP:
                Gdx.app.log(TAG, "event: UPGRADE_HP");
                break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "GUI --> propertyChange(): " + evt.getPropertyName());

        if (evt.getPropertyName().equals(ChestWindowEvent.class.getName())) {
            chestOpenClose((ChestWindowEvent) evt.getNewValue());
        }
    }

    public void chestOpenClose(ChestWindowEvent event) {

        switch (event) {
            case CHEST_OPENED:
                chestInventoryUI.setVisible(true);
                break;
            case CHEST_CLOSED:
                chestInventoryUI.setVisible(false);
                break;
        }
    }

    public Stage getStage() {
        return stage;
    }
}
