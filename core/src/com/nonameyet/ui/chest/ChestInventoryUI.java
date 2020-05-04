package com.nonameyet.ui.chest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.AbstractScreen;
import com.nonameyet.utils.Constants;

public class ChestInventoryUI extends Window {
    private final String TAG = this.getClass().getSimpleName();
    private static final WindowStyle windowStyle;
    private static final Texture texture;
    private static final BitmapFont bitmapFont;

    static {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetName.PIXEL_FONT.getAssetName()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        bitmapFont = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        texture = Assets.manager.get(AssetName.CHEST_WINDOW.getAssetName());
        windowStyle = new WindowStyle(bitmapFont, Color.WHITE, new TextureRegionDrawable(texture));
    }

    public ChestInventoryUI() {
        super("", windowStyle);

        this.setSize(texture.getWidth() * (AbstractScreen.VIEWPORT.physicalWidth / Constants.CAMERA_PIXELS_WIDTH),
                texture.getHeight() * (AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT));
        this.setVisible(false);
        this.setMovable(true);
        this.setPosition((AbstractScreen.VIEWPORT.physicalWidth / 2) - this.getWidth() / 2, (AbstractScreen.VIEWPORT.physicalHeight / 2) - this.getHeight() / 2);

        this.padTop(30);

//        this.debug();
    }
}
