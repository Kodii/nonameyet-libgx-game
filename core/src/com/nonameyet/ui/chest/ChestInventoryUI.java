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

    private static float hudRatio = AbstractScreen.VIEWPORT.physicalHeight / Constants.CAMERA_PIXELS_HEIGHT;

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
        closeButtonStyle.imageUp = new TextureRegionDrawable(textureAtlas.findRegion("closeoff"));
        closeButtonStyle.imageOver = new TextureRegionDrawable(textureAtlas.findRegion("closeon"));
    }

    public ChestInventoryUI() {
        super("", windowStyle);

        final ImageButton closeButton = new ImageButton(closeButtonStyle);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });

        closeButton.getImage().setScale(hudRatio);


        getTitleTable().add(closeButton).padRight(22 * hudRatio).padTop(20 * hudRatio);

        setClip(false);
        setTransform(true);


        this.setSize(region.getRegionWidth() * hudRatio,
                region.getRegionHeight() * hudRatio);
        this.setVisible(false);
        this.setMovable(true);
        this.setPosition((AbstractScreen.VIEWPORT.physicalWidth / 2) - this.getWidth() / 2, (AbstractScreen.VIEWPORT.physicalHeight / 2) - this.getHeight() / 2);

        this.padTop(40);
        this.padLeft(10);


//        this.debug();
    }
}
