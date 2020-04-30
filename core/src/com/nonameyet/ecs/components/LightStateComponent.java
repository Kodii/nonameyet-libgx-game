package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;

public class LightStateComponent implements Component {

    public static final int STATE_OFF = 0;
    public static final int STATE__ON = 1;

    private int state = 0;

    public void set(int newState) {
        state = newState;
    }

    public int get() {
        return state;
    }

}
