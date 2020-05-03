package com.nonameyet.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.events.ChestEvent;
import com.nonameyet.events.DayTimeEvent;
import com.nonameyet.preferences.Preferences;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AudioManager implements PropertyChangeListener {
    private static final String TAG = AudioManager.class.getSimpleName();

    private static AudioManager instance = null;

    Music music = null;
    Sound sound = null;

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "AudioManager --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(ChestEvent.NAME)) {
            chestWindowEvent((ChestEvent) evt.getNewValue());
        } else if (evt.getPropertyName().equals(DayTimeEvent.NAME)) {
            lightsEvent((DayTimeEvent) evt.getNewValue());
        }
    }

    public void chestWindowEvent(ChestEvent event) {

        switch (event) {
            case CHEST_OPENED:
                sound = playSound((Sound) Assets.manager.get(AssetName.CHEST_OPEN_EFFECT.getAssetName()));
                break;
            case CHEST_CLOSED:
                sound = playSound((Sound) Assets.manager.get(AssetName.CHEST_CLOSE_EFFECT.getAssetName()));
                break;
        }
    }

    private void lightsEvent(DayTimeEvent event) {

        switch (event) {
            case DUSK:
            case NIGHT:
                if (music != null)
                    music.stop();
                music = playMusic(true, (Music) Assets.manager.get(AssetName.TOWN_NIGHT_MUSIC.getAssetName()));
                break;
            case DAWN:
            case AFTERNOON:
                if (music != null)
                    music.stop();
                music = playMusic(true, (Music) Assets.manager.get(AssetName.TOWN_DAY_MUSIC.getAssetName()));
                break;
        }
    }

    public Music playMusic(boolean isLooping, Music music) {
        Gdx.app.debug(TAG, "isMusicEnabled: " + Preferences.isMusicEnabled());
        if (!Preferences.isMusicEnabled())
            return null;

        if (music != null) {
            music.setLooping(isLooping);
            music.play();
            music.setVolume(Preferences.getMusicVolume());
        } else {
            Gdx.app.debug(TAG, "Music not loaded");
            return null;
        }
        return music;
    }

    public Sound playSound(Sound sound) {
        Gdx.app.debug(TAG, "isSoundEnabled: " + Preferences.isSoundEnabled());
        if (!Preferences.isSoundEnabled())
            return null;

        if (sound != null) {
            long soundId = sound.play();
            sound.setVolume(soundId, Preferences.getSoundVolume());
        } else {
            Gdx.app.debug(TAG, "Sound not loaded");
            return null;
        }
        return sound;
    }
}
