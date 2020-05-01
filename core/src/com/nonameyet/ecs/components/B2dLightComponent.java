package com.nonameyet.ecs.components;

import box2dLight.Light;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.clock.DayTimeEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class B2dLightComponent implements Component, Pool.Poolable, Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;

    public B2dLightComponent(GameScreen screen) {
        this.screen = screen;

        // listeners
        screen.getPlayerHUD().getClockUI().addPropertyChangeListener(this);
    }

    public Light light;

    public float distance = 0;
    public float flDistance = 0;
    public float flSpeed = 0;

    // render every i > callEvery
    public float callEvery = 0;

    // dont touch
    public float flTime = 0;
    public float renderTime = 0;

    @Override
    public void reset() {
        light.dispose();
        distance = 0;
        flDistance = 0;
        flSpeed = 0;
        flTime = 0;
        renderTime = 0;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "b2dlight --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(DayTimeEvent.class.getSimpleName())) {
            lightsEvent((DayTimeEvent) evt.getNewValue());
        }
    }

    private void lightsEvent(DayTimeEvent event) {
        switch (event) {
            case DAWN:
            case AFTERNOON:
                light.setActive(false);
                break;

            case DUSK:
            case NIGHT:
                light.setActive(true);
                break;
        }
    }

    @Override
    public void dispose() {
        screen.getPlayerHUD().getClockUI().removePropertyChangeListener(this);
    }
}
