package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;

public class PlayerStateComponent implements Component {
    public static final int STATE_STANDING_DOWN = 0;
    public static final int STATE_STANDING_UP = 1;
    public static final int STATE_STANDING_LEFT = 2;
    public static final int STATE_STANDING_RIGHT = 3;
    public static final int STATE_RUNNING_DOWN = 4;
    public static final int STATE_RUNNING_UP = 5;
    public static final int STATE_RUNNING_LEFT = 6;
    public static final int STATE_RUNNING_RIGHT = 7;

    private int state = 5;
    public float time = 0.0f;
    public boolean isLooping = true;

    public void set(int newState) {
        state = newState;
        time = 0.0f;
    }

    public int get() {
        return state;
    }
}
