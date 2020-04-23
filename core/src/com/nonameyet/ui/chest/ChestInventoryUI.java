package com.nonameyet.ui.chest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.AbstractScreen;
import com.nonameyet.utils.Constants;

public class ChestInventoryUI implements Disposable {
    private static final String TAG = ChestInventoryUI.class.getSimpleName();

    private final Stage stage;
    Image chestWindow;

    ChestWindowEvent chestWindowEvent;

    public ChestInventoryUI(Stage stage) {
        this.stage = stage;
        chestWindowEvent = ChestWindowEvent.CHEST_CLOSED;
        createChestWindow();
    }

    private void createChestWindow() {
        Texture texture = Assets.manager.get(AssetName.CHEST_WINDOW.getAssetName());

        TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());

        chestWindow = new Image(textureRegion);
        chestWindow.setSize(textureRegion.getRegionWidth() * (AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH),
                textureRegion.getRegionHeight() * (AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT));

        chestWindow.setPosition((AbstractScreen.VIEWPORT.physicalWidth / 2) - chestWindow.getWidth() / 2, (AbstractScreen.VIEWPORT.physicalHeight / 2) - chestWindow.getHeight() / 2);

    }

    public void update(ChestWindowEvent event) {

        switch (event) {
            case CHEST_OPENED:
                Gdx.app.debug(TAG, event.toString() + " otwórz sie");
                chestWindowEvent = event;
                break;
            case CHEST_CLOSED:
                Gdx.app.debug(TAG, event.toString() + " zamknij się");
                chestWindowEvent = event;
                break;
        }
    }

    public void renderChestWindow() {
        switch (chestWindowEvent) {

            case CHEST_OPENED:
//                Gdx.app.debug(TAG, "otwarcie skrzyni");
                stage.addActor(chestWindow);
                break;
            case CHEST_CLOSED:
//                Gdx.app.debug(TAG, "zamknięcie skrzyni");
                stage.clear();
                break;
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
