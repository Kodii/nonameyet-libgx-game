package com.nonameyet.screens;

import com.badlogic.gdx.Screen;
import com.nonameyet.screens.loading.LoadingScreen;
import com.nonameyet.screens.menu.CreditsScreen;
import com.nonameyet.screens.menu.MainMenuScreen;
import com.nonameyet.screens.menu.OptionsMenuScreen;
import com.nonameyet.screens.menu.ReleaseNotesScreen;

public enum ScreenType {
    LOADING(LoadingScreen.class),
    MAIN_MENU(MainMenuScreen.class),
    OPTIONS_MENU(OptionsMenuScreen.class),
    CREDITS(CreditsScreen.class),
    RELEASE_NOTES(ReleaseNotesScreen.class),
    GAME(GameScreen.class);

    private final Class<? extends Screen> screenClass;

    ScreenType(Class<? extends Screen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends Screen> getScreenClass() {
        return screenClass;
    }
}
