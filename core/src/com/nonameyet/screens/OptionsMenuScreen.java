package com.nonameyet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nonameyet.NoNameYet;

public class OptionsMenuScreen extends AbstractMenuScreen {
    private static final String TAG = OptionsMenuScreen.class.getSimpleName();

    Table table;

    public OptionsMenuScreen(NoNameYet game) {
        super(game);

        title("Options");
        TextButton backButton = backButton();

        table = new Table();
        table.setFillParent(true);
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)

        resolution();


        stage.addActor(table);

        //Listeners
        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                OptionsMenuScreen.this.game.setScreen(ScreenType.MAIN_MENU);
            }
        });
    }

    // todo: add impl
    private void resolution() {

        Label windowModeLabel = new Label("Window Mode:", labelStyle);
        Label windowed = new Label("Windowed", labelStyle);

        Label resolutionLabel = new Label("Resolution:", labelStyle);
        Label resolution = new Label(Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight(), labelStyle);

        Label vSyncLabel = new Label("VSync:", labelStyle);
        Label vSync = new Label("On", labelStyle);

        Label musicVolumeLabel = new Label("Music Volume:", labelStyle);
        Label musicVolume = new Label("100%", labelStyle);

        Label soundVolumeLabel = new Label("Sound Volume", labelStyle);
        Label soundVolume = new Label("100%", labelStyle);

        table.add(windowModeLabel).padRight(10).spaceBottom(10);
        table.add(windowed).padLeft(10).spaceBottom(10).row();

        table.add(resolutionLabel).padRight(10).spaceBottom(10);
        table.add(resolution).padLeft(10).spaceBottom(10).row();

        table.add(vSyncLabel).padRight(10).spaceBottom(10);
        table.add(vSync).padLeft(10).spaceBottom(10).row();

        table.add(musicVolumeLabel).padRight(10).spaceBottom(10);
        table.add(musicVolume).padLeft(10).spaceBottom(10).row();

        table.add(soundVolumeLabel).padRight(10).spaceBottom(10);
        table.add(soundVolume).padLeft(10).spaceBottom(10).row();

        // buttons:
        final TextButton resetToDefaultSettingsButton = new TextButton("Reset to Default Settings", textButtonStyle);
        final TextButton applyButton = new TextButton("Apply", textButtonStyle);

        table.add(resetToDefaultSettingsButton).padRight(10).spaceTop(60);
        table.add(applyButton).padLeft(10).spaceTop(60);


    }

}
