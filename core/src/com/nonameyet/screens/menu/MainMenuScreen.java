package com.nonameyet.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nonameyet.NoNameYet;
import com.nonameyet.screens.ScreenType;

public class MainMenuScreen extends AbstractMenuScreen {
    private static final String TAG = MainMenuScreen.class.getSimpleName();

    public MainMenuScreen(NoNameYet game) {
        super(game);

        final TextButton newGameButton = new TextButton("New Game", textButtonStyle);
        final TextButton optionsButton = new TextButton("Options", textButtonStyle);
        final TextButton releaseNotesButton = new TextButton("Release Notes", textButtonStyle);
        final TextButton creditsButton = new TextButton("Credits", textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)

//        Layout
        table.add(newGameButton).spaceBottom(10).row();
        table.add(optionsButton).spaceBottom(10).row();
        table.add(releaseNotesButton).spaceBottom(10).row();
        table.add(creditsButton).spaceBottom(10).row();
        table.add(exitButton).spaceBottom(10).row();

        stage.addActor(table);

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

        optionsButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                MainMenuScreen.this.game.setScreen(ScreenType.OPTIONS_MENU);
            }
        });

        releaseNotesButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                MainMenuScreen.this.game.setScreen(ScreenType.RELEASE_NOTES);
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                MainMenuScreen.this.game.setScreen(ScreenType.CREDITS);
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
}
