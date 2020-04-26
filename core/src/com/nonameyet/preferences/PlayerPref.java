package com.nonameyet.preferences;

import com.badlogic.gdx.Gdx;

public class PlayerPref {
    private static final String PREFS_NAME = "playerPref";

    private static final String PREF_CURRENT_TIME = "player.currentTime";

    static com.badlogic.gdx.Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public static float getCurrentTime() {
        return getPrefs().getFloat(PREF_CURRENT_TIME, 60 * 60 * 12);
    }

    public static void setCurrentTime(float currentTime) {
        getPrefs().putFloat(PREF_CURRENT_TIME, currentTime);
        getPrefs().flush();
    }

}
