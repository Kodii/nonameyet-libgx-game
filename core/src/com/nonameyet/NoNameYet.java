package com.nonameyet;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.nonameyet.screen.AbstractScreen;
import com.nonameyet.screen.ScreenType;

import java.util.EnumMap;

public class NoNameYet extends Game {
    private static final String TAG = NoNameYet.class.getSimpleName();

    private EnumMap<ScreenType, AbstractScreen> screenCache;
    private FitViewport screenViewport;

    @Override

    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        screenViewport = new FitViewport(16, 9);
        screenCache = new EnumMap<>(ScreenType.class);
        setScreen(ScreenType.LOADING);
    }

    public FitViewport getScreenViewport() {
        return screenViewport;
    }

    public void setScreen(final ScreenType screenType) {
        final Screen screen = screenCache.get(screenType);
        if (screen == null) {
            // screen not created it -> create it
            try {
                Gdx.app.debug(TAG, "Creating new screen: " + screenType);
                final AbstractScreen newScreen = (AbstractScreen) ClassReflection.getConstructor(screenType.getScreenClass(), NoNameYet.class).newInstance(this);
                screenCache.put(screenType, newScreen);
                setScreen(newScreen);
            } catch (ReflectionException e) {
                throw new GdxRuntimeException("Screen " + screenType + " could not be created", e);
            }

        } else {
            Gdx.app.debug(TAG, "Switching to screen: " + screenType);
            setScreen(screen);
        }
    }

}