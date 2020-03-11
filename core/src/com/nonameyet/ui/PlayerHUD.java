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
import com.nonameyet.NoNameYet;
import com.nonameyet.environment.AssetName;

public class PlayerHUD implements Screen {
    private static final String TAG = PlayerHUD.class.getSimpleName();
    private final NoNameYet _game;

    private Stage _stage;
    private Viewport _viewport;
    private Camera _camera;


    public PlayerHUD(Camera camera, NoNameYet game) {
        this._game = game;
        _camera = camera;

        //setup the HUD viewport using a new camera seperate from gamecam
        //define stage using that viewport and games spritebatch
        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);

        cameraFrame();

    }

    private void cameraFrame() {
        Texture texture = _game.getAssets().manager.get(AssetName.CAMERA_FRAME.getAssetName());

        TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());

        Image cameraFrame = new Image(textureRegion);
        cameraFrame.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        _stage.addActor(cameraFrame);
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        _stage.act(delta);
        _stage.draw();
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
