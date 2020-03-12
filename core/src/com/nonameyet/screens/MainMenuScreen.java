package com.nonameyet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nonameyet.NoNameYet;
import com.nonameyet.environment.AssetName;
import com.nonameyet.environment.Assets;

public class MainMenuScreen extends AbstractScreen {
    private static final String TAG = MainMenuScreen.class.getSimpleName();

    private Stage _stage;

    private TextButton.TextButtonStyle _textButtonStyle;

    public MainMenuScreen(NoNameYet game) {
        super(game);

        //creation
        _stage = new Stage();

        renderBackground();
        setFont();

        Table table = new Table();
        table.setFillParent(true);

        final TextButton newGameButton = new TextButton("New Game", _textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", _textButtonStyle);

//        Layout
        table.add(newGameButton).spaceBottom(10).row();
        table.add(exitButton).spaceBottom(10).row();

        _stage.addActor(table);

        //Listeners
        newGameButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                MainMenuScreen.this.game.setScreen(ScreenType.GAME);
            }
        });

        exitButton.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }

        });
    }

    private void setFont() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetName.OLD_NEWSPAPER_FONT.getAssetName()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        BitmapFont bitmapFont = generator.generateFont(parameter);

        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        _textButtonStyle = new TextButton.TextButtonStyle();
        _textButtonStyle.font = bitmapFont;
        _textButtonStyle.fontColor = Color.WHITE;
        _textButtonStyle.downFontColor = Color.GRAY;
        _textButtonStyle.overFontColor = Color.GRAY;
    }

    private void renderBackground() {
        Texture texture = Assets.manager.get(AssetName.MAIN_MENU_BACKGROUND.getAssetName());
        TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());

        Image backgroundImage = new Image(textureRegion);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        _stage.addActor(backgroundImage);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(_stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _stage.act(delta);
        _stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        _stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        _stage.dispose();
    }
}
