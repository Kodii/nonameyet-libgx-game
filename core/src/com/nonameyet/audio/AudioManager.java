package com.nonameyet.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.ui.chest.ChestWindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AudioManager implements PropertyChangeListener {
    private static final String TAG = AudioManager.class.getSimpleName();

    private static AudioManager instance = null;

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
        }
    }

    public void chestWindowEvent(ChestWindowEvent event) {

        switch (event) {
            case CHEST_OPENED:
                playSound((Sound) Assets.manager.get(AssetName.CHEST_OPEN_EFFECT.getAssetName()));
                break;
            case CHEST_CLOSED:
                playSound((Sound) Assets.manager.get(AssetName.CHEST_CLOSE_EFFECT.getAssetName()));
                break;
        }
    }

    private Sound playSound(Sound sound) {
        if (sound != null) {
            long soundId = sound.play();
            sound.setVolume(soundId, 0.05f);
        } else {
            Gdx.app.debug(TAG, "Sound not loaded");
            return null;
        }
        return sound;
    }
}
