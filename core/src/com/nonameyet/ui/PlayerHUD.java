package com.nonameyet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
import com.nonameyet.ui.chest.ChestInventoryUI;
import com.nonameyet.ui.chest.ChestWindowEvent;
import com.nonameyet.ui.life.LifeUI;
import com.nonameyet.ui.life.StatusListener;

public class PlayerHUD implements Screen, StatusListener, InputProcessor {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage stage;
    private Viewport viewport;
    private Camera camera;

    private LifeUI lifeUI;
    private ChestInventoryUI chestInventoryUI;

    public PlayerHUD(Camera camera) {
        this.camera = camera;


        //setup the HUD viewport using a new camera seperate from gamecam
        viewport = new ScreenViewport(this.camera);
        stage = new Stage(viewport);

        lifeUI = new LifeUI(stage);

        cameraFrame();

        chestInventoryUI = new ChestInventoryUI();
        stage.addActor(chestInventoryUI);

        // listeners
        lifeUI.attachListener(this);
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
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        lifeUI.handleInput(delta);

        lifeUI.renderLifes();

        stage.act(delta);
        stage.draw();
    }

    public void update(ChestWindowEvent event) {

        switch (event) {
            case CHEST_OPENED:
                Gdx.app.debug(TAG, event.toString() + " otwórz sie");
                chestInventoryUI.setVisible(true);
                break;
            case CHEST_CLOSED:
                Gdx.app.debug(TAG, event.toString() + " zamknij się");
                chestInventoryUI.setVisible(false);
                break;
        }
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
        Gdx.input.setInputProcessor(null);
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

    public ChestInventoryUI getChestInventoryUI() {
        return chestInventoryUI;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
