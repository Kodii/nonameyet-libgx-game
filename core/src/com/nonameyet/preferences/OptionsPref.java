package com.nonameyet.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class OptionsPref {
    private static final String PREFS_NAME = "nonameyet.options";

    private static final String PREF_MUSIC_VOLUME = "music.volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";

    private static final String PREF_SOUND_VOLUME = "sound.volume";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";

    private static final String PREF_RESOLUTION_WIDTH = "resolution.width";
    private static final String PREF_RESOLUTION_HEIGHT = "resolution.height";

    private static final String PREF_VSYNC = "vsync";

    static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    // music
    public static float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    public static void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    public static boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public static void setMusicEnabled(boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    // sound
    public static float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOLUME, 0.5f);
    }

    public static void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOLUME, volume);
        getPrefs().flush();
    }

    public static boolean isSoundEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public static void setSoundEnabled(boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    // resolution
    public static int getResolutionWidth() {
        return getPrefs().getInteger(PREF_RESOLUTION_WIDTH, 1920);
    }

    public static void setResolutionWidth(int value) {
        getPrefs().putInteger(PREF_RESOLUTION_WIDTH, value);
        getPrefs().flush();
    }

    public static int getResolutionHeight() {
        return getPrefs().getInteger(PREF_RESOLUTION_HEIGHT, 1080);
    }

    public static void setResolutionHeight(int value) {
        getPrefs().putInteger(PREF_RESOLUTION_HEIGHT, value);
        getPrefs().flush();
    }

    // vsync
    public static boolean isVsync() {
        return getPrefs().getBoolean(PREF_VSYNC, true);
    }

    public static void setVsync(boolean vsync) {
        getPrefs().putBoolean(PREF_VSYNC, vsync);
        getPrefs().flush();
    }

    public static void resetToDefault() {
        setMusicVolume(0.5f);
        setMusicEnabled(true);
        setSoundVolume(0.5f);
        setSoundEnabled(true);
        setResolutionWidth(1920);
        setResolutionHeight(1080);
        setVsync(true);
    }
}
