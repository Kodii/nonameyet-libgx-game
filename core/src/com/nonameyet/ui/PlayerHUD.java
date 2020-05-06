package com.nonameyet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.events.ChestEvent;
import com.nonameyet.events.StatusEvent;
import com.nonameyet.input.GameKeyInputListener;
import com.nonameyet.input.GameKeys;
import com.nonameyet.input.InputManager;
import com.nonameyet.preferences.PlayerPref;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.chest.ChestInventoryUI;
import com.nonameyet.ui.clock.ClockUI;
import com.nonameyet.ui.life.LifeUI;
import com.nonameyet.ui.stats.StatsUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlayerHUD implements Screen, PropertyChangeListener, GameKeyInputListener {
    private final String TAG = this.getClass().getSimpleName();
    private final GameScreen screen;

    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    private LifeUI lifeUI;
    private ChestInventoryUI chestInventoryUI;
    private StatsUI statsUI;

    private ClockUI clockUI;

    public PlayerHUD(OrthographicCamera camera, GameScreen screen) {
        this.camera = camera;
        this.screen = screen;

        //setup the HUD viewport using a new camera seperate from gamecam
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.camera);

        stage = new Stage(viewport);

        lifeUI = new LifeUI(stage);

        cameraFrame();

        chestInventoryUI = new ChestInventoryUI();
        stage.addActor(chestInventoryUI);

        statsUI = new StatsUI();
        stage.addActor(statsUI);

        // listeners
        lifeUI.addPropertyChangeListener(this);

        clockUI = new ClockUI(screen, "0");
        clockUI.setPosition(stage.getWidth() - clockUI.getWidth() - 15, stage.getHeight() - clockUI.getHeight() - 15);
        clockUI.setRateOfTime(PlayerPref.getTimeSpeed());
        clockUI.setVisible(true);
        clockUI.setTotalTime(PlayerPref.getCurrentTime());

        stage.addActor(clockUI);

        InputManager.getInstance().addInputListener(this);
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
        camera.update();

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
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "GUI --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(ChestEvent.NAME)) {
            chestWindowEvent((ChestEvent) evt.getNewValue());
        } else if (evt.getPropertyName().equals(StatusEvent.NAME)) {
            statusEvent((StatusEvent) evt.getNewValue());
        }
    }

    public void chestWindowEvent(ChestEvent event) {

        switch (event) {
            case CHEST_OPENED:
                chestInventoryUI.setVisible(true);
                break;
            case CHEST_CLOSED:
                chestInventoryUI.setVisible(false);
                break;
        }
    }

    public void statusEvent(StatusEvent event) {
        switch (event) {
            case ADD_HP:
                break;

            case REMOVE_HP:
                break;

            case HEAL_HP:
                break;

            case UPGRADE_HP:
                break;
        }
    }

    public Stage getStage() {
        return stage;
    }

    public ClockUI getClockUI() {
        return clockUI;
    }

    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key) {
            case STATS:
                statsUI.setVisible(!statsUI.isVisible());
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(InputManager inputManager, GameKeys key) {

    }

    @Override
    public void dispose() {
        lifeUI.removePropertyChangeListener(this);
        screen.getMapMgr().getCollisionSystem().removePropertyChangeListener(this);
        stage.dispose();
        InputManager.getInstance().removeInputListener(this);
    }
}
