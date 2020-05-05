package com.nonameyet.ui.chest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.AbstractScreen;
import com.nonameyet.utils.Constants;

public class ChestInventoryUI extends Window {
    private final String TAG = this.getClass().getSimpleName();
    private static final WindowStyle windowStyle;
    private static final TextureRegion region;
    private static final ImageButton.ImageButtonStyle closeButtonStyle;
    private static final BitmapFont bitmapFont;

    static {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetName.PIXEL_FONT.getAssetName()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        bitmapFont = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.CHEST_WINDOW.getAssetName());
        region = textureAtlas.findRegion("bg");
        windowStyle = new WindowStyle(bitmapFont, Color.WHITE, new TextureRegionDrawable(region));

        closeButtonStyle = new ImageButton.ImageButtonStyle();
        closeButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("close_off"));
        closeButtonStyle.imageChecked = new TextureRegionDrawable(textureAtlas.findRegion("close_on"));
    }

    public ChestInventoryUI() {
        super("Inventory", windowStyle);

        final ImageButton closeButton = new ImageButton(closeButtonStyle);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });

        this.add(closeButton).size(11 * (AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH),
                13 * (AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT));

        setClip(false);
        setTransform(true);


        this.setSize(region.getRegionWidth() * (AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH),
                region.getRegionHeight() * (AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT));
        this.setModal(true);
        this.setVisible(true);
        this.setMovable(true);
        this.setPosition((AbstractScreen.VIEWPORT.physicalWidth / 2) - this.getWidth() / 2, (AbstractScreen.VIEWPORT.physicalHeight / 2) - this.getHeight() / 2);

//        this.padTop(40);
//        this.padLeft(10);


        this.debug();
    }
}
