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

public class PlayerHUD implements Screen, StatusListener {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage stage;
    private Viewport viewport;
    private Camera camera;

    private LifeUI lifeUI;

    public PlayerHUD(Camera camera) {
        this.camera = camera;


        //setup the HUD viewport using a new camera seperate from gamecam
        viewport = new ScreenViewport(this.camera);
        stage = new Stage(viewport);

        lifeUI = new LifeUI(stage);

        cameraFrame();

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
}
