package com.nonameyet.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nonameyet.NoNameYet;
import com.nonameyet.preferences.Preferences;
import com.nonameyet.screens.ScreenType;

public class OptionsMenuScreen extends AbstractMenuScreen {
    private static final String TAG = OptionsMenuScreen.class.getSimpleName();

    Table table;

    TextButton resolutionButton;
    TextButton vsyncButton;
    TextButton musicVolumeButton;
    TextButton musicButton;
    TextButton soundVolumeButton;
    TextButton soundButton;

    TextButton resetToDefaultSettingsButton;

    public OptionsMenuScreen(NoNameYet game) {
        super(game);

        title("Options");
        TextButton backButton = backButton();

        table = new Table();
        table.setFillParent(true);
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)

        createResolution();
        createVsync();
        createMusicVolume();
        createMusic();
        createSoundVolume();
        createSound();
        options();

        listeners();

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
    private void options() {

        Label windowModeLabel = new Label("Window Mode:", labelStyle);
        Label windowed = new Label("Windowed", labelStyle);

        Label resolutionLabel = new Label("Resolution:", labelStyle);

        Label vSyncLabel = new Label("VSync:", labelStyle);

        Label musicVolumeLabel = new Label("Music Volume:", labelStyle);

        Label musicLabel = new Label("Music:", labelStyle);

        Label soundVolumeLabel = new Label("Sound Volume", labelStyle);

        Label soundLabel = new Label("Sound Volume", labelStyle);

        table.add(windowModeLabel).padRight(10).spaceBottom(10);
        table.add(windowed).padLeft(10).spaceBottom(10).row();

        table.add(resolutionLabel).padRight(10).spaceBottom(10);
        table.add(resolutionButton).padLeft(10).spaceBottom(10).row();

        table.add(vSyncLabel).padRight(10).spaceBottom(10);
        table.add(vsyncButton).padLeft(10).spaceBottom(10).row();

        table.add(musicVolumeLabel).padRight(10).spaceBottom(10);
        table.add(musicVolumeButton).padLeft(10).spaceBottom(10).row();

        table.add(musicLabel).padRight(10).spaceBottom(10);
        table.add(musicButton).padLeft(10).spaceBottom(10).row();

        table.add(soundVolumeLabel).padRight(10).spaceBottom(10);
        table.add(soundVolumeButton).padLeft(10).spaceBottom(10).row();

        table.add(soundLabel).padRight(10).spaceBottom(10);
        table.add(soundButton).padLeft(10).spaceBottom(10).row();

        // buttons:
        resetToDefaultSettingsButton = new TextButton("Reset to Default Settings", textButtonStyle);

        table.add(resetToDefaultSettingsButton).colspan(2).spaceTop(60).row();

    }

    void listeners() {
        resetToDefaultSettingsButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Preferences.resetToDefault();
                initiateVsync();
                initiateMusicVolume();
                initiateMusic();
                initiateSoundVolume();
                initiateSound();
            }
        });

        resolutionButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                updateResolution();
            }
        });

        vsyncButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                updateVsync();
            }
        });

        musicVolumeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                updateMusicVolume();
            }
        });

        musicButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                updateMusic();
            }
        });

        soundVolumeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                updateSoundVolume();
            }
        });

        soundButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                updateSound();
            }
        });
    }

    // vsync
    private void createVsync() {
        vsyncButton = new TextButton("On", textButtonStyle);
        initiateVsync();
    }

    private void initiateVsync() {
        vsyncButton.setText(Preferences.isVsync() ? "On" : "Off");
    }

    private void updateVsync() {
        if (Preferences.isVsync())
            Preferences.setVsync(false);
        else Preferences.setVsync(true);

        vsyncButton.setText(Preferences.isVsync() ? "On" : "Off");

        Gdx.graphics.setVSync(Preferences.isVsync());
    }

    // resolution
    // vsync
    private void createResolution() {
        resolutionButton = new TextButton("1920x1080", textButtonStyle);
        initiateResolution();
    }

    private void initiateResolution() {
        updateResolutionText(resolutionButton, Preferences.getResolutionWidth(), Preferences.getResolutionHeight());
    }

    private void updateResolution() {

        switch (String.valueOf(Preferences.getResolutionWidth()).concat("x").concat(String.valueOf(Preferences.getResolutionHeight()))) {
            case "1280x720":
                Preferences.setResolutionWidth(1366);
                Preferences.setResolutionHeight(768);
                updateResolutionText(resolutionButton, Preferences.getResolutionWidth(), Preferences.getResolutionHeight());
                break;
            case "1366x768":
                Preferences.setResolutionWidth(1600);
                Preferences.setResolutionHeight(900);
                updateResolutionText(resolutionButton, Preferences.getResolutionWidth(), Preferences.getResolutionHeight());
                break;

            case "1600x900":
                Preferences.setResolutionWidth(1920);
                Preferences.setResolutionHeight(1080);
                updateResolutionText(resolutionButton, Preferences.getResolutionWidth(), Preferences.getResolutionHeight());
                break;
            case "1920x1080":
                Preferences.setResolutionWidth(1280);
                Preferences.setResolutionHeight(720);
                updateResolutionText(resolutionButton, Preferences.getResolutionWidth(), Preferences.getResolutionHeight());
                break;
        }

        Gdx.app.exit();
    }

    // music
    private void createMusic() {
        musicButton = new TextButton("On", textButtonStyle);
        initiateMusic();
    }

    private void initiateMusic() {
        vsyncButton.setText(Preferences.isMusicEnabled() ? "On" : "Off");
    }

    private void updateMusic() {
        if (Preferences.isMusicEnabled())
            Preferences.setMusicEnabled(false);
        else Preferences.setMusicEnabled(true);

        musicButton.setText(Preferences.isMusicEnabled() ? "On" : "Off");

        // todo: add impl
    }

    // music volume
    private void createMusicVolume() {
        musicVolumeButton = new TextButton("50%", textButtonStyle);

        initiateMusicVolume();
    }

    private void initiateMusicVolume() {
        updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
    }


    private void updateMusicVolume() {

        switch (String.valueOf(Preferences.getMusicVolume())) {
            case "0.1":
                Preferences.setMusicVolume(0.2f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            case "0.2":
                Preferences.setMusicVolume(0.3f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            case "0.3":
                Preferences.setMusicVolume(0.4f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            case "0.4":
                Preferences.setMusicVolume(0.5f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            case "0.5":
                Preferences.setMusicVolume(0.6f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            case "0.6":
                Preferences.setMusicVolume(0.7f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            case "0.7":
                Preferences.setMusicVolume(0.8f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            case "0.8":
                Preferences.setMusicVolume(0.9f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            case "0.9":
                Preferences.setMusicVolume(1.0f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            case "1.0":
                Preferences.setMusicVolume(0.1f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
            default:
                Preferences.setMusicVolume(0.5f);
                updateVolumeText(musicVolumeButton, Preferences.getMusicVolume());
                break;
        }

        // todo: add impl
    }

    // sound
    private void createSound() {
        soundButton = new TextButton("On", textButtonStyle);
        initiateSound();
    }

    private void initiateSound() {
        vsyncButton.setText(Preferences.isSoundEnabled() ? "On" : "Off");

        // todo: add impl
    }

    private void updateSound() {
        if (Preferences.isSoundEnabled())
            Preferences.setSoundEnabled(false);
        else Preferences.setSoundEnabled(true);

        soundButton.setText(Preferences.isSoundEnabled() ? "On" : "Off");

        // todo: add impl
    }

    // sound volume
    private void createSoundVolume() {
        soundVolumeButton = new TextButton("50%", textButtonStyle);

        initiateSoundVolume();
    }

    private void initiateSoundVolume() {
        updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());

        // todo: add impl
    }

    private void updateSoundVolume() {

        switch (String.valueOf(Preferences.getSoundVolume())) {
            case "0.1":
                Preferences.setSoundVolume(0.2f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            case "0.2":
                Preferences.setSoundVolume(0.3f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            case "0.3":
                Preferences.setSoundVolume(0.4f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            case "0.4":
                Preferences.setSoundVolume(0.5f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            case "0.5":
                Preferences.setSoundVolume(0.6f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            case "0.6":
                Preferences.setSoundVolume(0.7f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            case "0.7":
                Preferences.setSoundVolume(0.8f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            case "0.8":
                Preferences.setSoundVolume(0.9f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            case "0.9":
                Preferences.setSoundVolume(1.0f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            case "1.0":
                Preferences.setSoundVolume(0.1f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
            default:
                Preferences.setSoundVolume(0.5f);
                updateVolumeText(soundVolumeButton, Preferences.getSoundVolume());
                break;
        }

        // todo: add impl
    }

    private static void updateVolumeText(TextButton textButton, float value) {
        switch (String.valueOf(value)) {
            case "0.1":
                textButton.setText("10%");
                break;
            case "0.2":
                textButton.setText("20%");
                break;
            case "0.3":
                textButton.setText("30%");
                break;
            case "0.4":
                textButton.setText("40%");
                break;
            case "0.5":
                textButton.setText("50%");
                break;
            case "0.6":
                textButton.setText("60%");
                break;
            case "0.7":
                textButton.setText("70%");
                break;
            case "0.8":
                textButton.setText("80%");
                break;
            case "0.9":
                textButton.setText("90%");
                break;
            case "1.0":
                textButton.setText("100%");
                break;
        }

    }

    private static void updateResolutionText(TextButton textButton, int resolutionWidth, int resolutionHeight) {
        switch (String.valueOf(resolutionWidth).concat("x").concat(String.valueOf(resolutionHeight))) {
            case "1280x720":
                textButton.setText("1280x720");
                break;
            case "1366x768":
                textButton.setText("1366x768");
                break;
            case "1600x900":
                textButton.setText("1600x900");
                break;
            case "1920x1080":
                textButton.setText("1920x1080");
                break;
        }
    }

}
