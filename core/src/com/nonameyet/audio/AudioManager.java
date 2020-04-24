package com.nonameyet.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.sprites.LightsEvent;
import com.nonameyet.ui.chest.ChestWindowEvent;

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

        if (evt.getPropertyName().equals(ChestWindowEvent.class.getSimpleName())) {
            chestWindowEvent((ChestWindowEvent) evt.getNewValue());
        } else if (evt.getPropertyName().equals(LightsEvent.class.getSimpleName())) {
            lightsEvent((LightsEvent) evt.getNewValue());
        }
    }

    public void chestWindowEvent(ChestWindowEvent event) {

        switch (event) {
            case CHEST_OPENED:
                sound = playSound((Sound) Assets.manager.get(AssetName.CHEST_OPEN_EFFECT.getAssetName()));
                break;
            case CHEST_CLOSED:
                sound = playSound((Sound) Assets.manager.get(AssetName.CHEST_CLOSE_EFFECT.getAssetName()));
                break;
        }
    }

    private void lightsEvent(LightsEvent event) {

        switch (event) {
            case ON:
                if (music != null)
                    music.stop();
                music = playMusic(true, (Music) Assets.manager.get(AssetName.TOWN_NIGHT_MUSIC.getAssetName()));
                break;
            case OFF:
                if (music != null)
                    music.stop();
                music = playMusic(true, (Music) Assets.manager.get(AssetName.TOWN_DAY_MUSIC.getAssetName()));
                break;
        }
    }

    private Music playMusic(boolean isLooping, Music music) {
        if (music != null) {
            music.setLooping(isLooping);
            music.play();
            music.setVolume(0.05f);
        } else {
            Gdx.app.debug(TAG, "Music not loaded");
            return null;
        }
        return music;
    }

    private Sound playSound(Sound sound) {
        if (sound != null) {
            long soundId = sound.play();
            sound.setVolume(soundId, 0.1f);
        } else {
            Gdx.app.debug(TAG, "Sound not loaded");
            return null;
        }
        return sound;
    }
}
