package com.nonameyet;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.nonameyet.preferences.Preferences;
import com.nonameyet.screens.ScreenType;

import java.util.EnumMap;

public class NoNameYet extends Game {
    private final String TAG = this.getClass().getSimpleName();

    // fixed timestep
    private float accumulator;

    private EnumMap<ScreenType, Screen> screenCache;

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(Preferences.getResolutionWidth(), Preferences.getResolutionHeight());
        Gdx.graphics.setVSync(Preferences.isVsync());

        Pixmap pixmap = new Pixmap(Gdx.files.internal("sprites/cursor2.png"));
        int xHotspot = pixmap.getWidth() / 2;
        int yHotspot = pixmap.getHeight() / 2;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();

        screenCache = new EnumMap<>(ScreenType.class);

        setScreen(ScreenType.LOADING);

    }

    public void setScreen(final ScreenType screenType) {
        final Screen screen = screenCache.get(screenType);
        if (screen == null) {
            // screen not created it -> create it
            try {
                Gdx.app.debug(TAG, "Creating new screen: " + screenType);
                final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(), NoNameYet.class).newInstance(this);
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

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
