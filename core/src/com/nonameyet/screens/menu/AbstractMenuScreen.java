package com.nonameyet.screens.menu;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.nonameyet.NoNameYet;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.AbstractScreen;

public class AbstractMenuScreen extends AbstractScreen {
    private static final String TAG = AbstractMenuScreen.class.getSimpleName();

    Stage stage;

    TextButton.TextButtonStyle textButtonStyle;

    Label.LabelStyle labelStyle;

    AbstractMenuScreen(NoNameYet game) {
        super(game);

        //creation
        stage = new Stage();

        renderBackground();
        setFont();

    }

    private void setFont() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetName.PIXEL_FONT.getAssetName()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        BitmapFont bitmapFont = generator.generateFont(parameter);

        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitmapFont;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.GRAY;
        textButtonStyle.overFontColor = Color.GRAY;

        labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;
        labelStyle.fontColor = Color.WHITE;
    }

    private void renderBackground() {
        Texture texture = Assets.manager.get(AssetName.MAIN_MENU_BACKGROUND.getAssetName());
        TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());

        Image backgroundImage = new Image(textureRegion);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(backgroundImage);
    }

    TextButton backButton() {
        final TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.setBounds(Gdx.graphics.getWidth() / 16, Gdx.graphics.getHeight() / 10, backButton.getWidth(), backButton.getHeight());

        stage.addActor(backButton);

        return backButton;
    }

    void title(String title) {

        Label titleLabel = new Label(title, labelStyle);
        titleLabel.setBounds((Gdx.graphics.getWidth() / 2f) - (titleLabel.getWidth()), Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 5), titleLabel.getWidth(), titleLabel.getHeight());
        titleLabel.setFontScale(2f, 2f);

        stage.addActor(titleLabel);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
