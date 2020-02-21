package com.nonameyet.screen;

public enum ScreenType {
    GAME(GameScreen.class),
    LOADING(LoadingScreen.class);

    private final Class<? extends AbstractScreen> screenClass;

    ScreenType(Class<? extends AbstractScreen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends AbstractScreen> getScreenClass() {
        return screenClass;
    }
}
