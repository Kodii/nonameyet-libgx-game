package com.nonameyet.screens.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.nonameyet.NoNameYet;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.AbstractScreen;
import com.nonameyet.screens.ScreenType;

public class LoadingScreen extends AbstractScreen {
    private static final String TAG = LoadingScreen.class.getSimpleName();

    Stage stage;
    Label.LabelStyle labelStyle;

    public LoadingScreen(final NoNameYet game) {
        super(game);

        //creation
        stage = new Stage();
        renderBackground();
        setFont();
        loading();

        Assets.load();
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Assets.manager.update()) {
            game.setScreen(ScreenType.MAIN_MENU);
        } else {
            stage.act(delta);
            stage.draw();
        }

    }

    private void setFont() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetName.PIXEL_FONT.getAssetName()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        BitmapFont bitmapFont = generator.generateFont(parameter);

        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;
        labelStyle.fontColor = Color.WHITE;
    }


    void loading() {

        Label titleLabel = new Label("Loading...", labelStyle);
        titleLabel.setBounds((Gdx.graphics.getWidth() / 2f) - (titleLabel.getWidth()), (Gdx.graphics.getHeight() / 2f) - (titleLabel.getHeight()), titleLabel.getWidth(), titleLabel.getHeight());
        titleLabel.setFontScale(2f, 2f);

        stage.addActor(titleLabel);
    }

    private void renderBackground() {
        Texture texture = new Texture(Gdx.files.internal(AssetName.MAIN_MENU_BACKGROUND.getAssetName()));
        TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());

        Image backgroundImage = new Image(textureRegion);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(backgroundImage);
    }
}
