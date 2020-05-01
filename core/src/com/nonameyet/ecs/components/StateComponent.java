package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {
    // for player
    public static final int STATE_STANDING_DOWN = 0;
    public static final int STATE_STANDING_UP = 1;
    public static final int STATE_STANDING_LEFT = 2;
    public static final int STATE_STANDING_RIGHT = 3;
    public static final int STATE_RUNNING_DOWN = 4;
    public static final int STATE_RUNNING_UP = 5;
    public static final int STATE_RUNNING_LEFT = 6;
    public static final int STATE_RUNNING_RIGHT = 7;

    // for chest
    public static final int STATE_CHEST_NORMAL = 100;
    public static final int STATE_CHEST_OPEN = 101;
    public static final int STATE_CHEST_CLOSE = 102;

    public static final int STATE_TORCH_ON = 140;
    public static final int STATE_TORCH_OFF = 141;

    public static final int STATE_ELDER = 200;

    public static final int STATE_BLACKSMITH = 210;
    public static final int STATE_OWEN = 211;
    public static final int STATE_ANVIL = 212;


    private int state = 0;
    public float time = 0.0f;
    public boolean isLooping = false;

    public void set(int newState) {
        state = newState;
        time = 0.0f;
    }

    public int get() {
        return state;
    }
}
