package com.nonameyet.input;

import com.badlogic.gdx.Input;

public enum GameKeys {
    UP(Input.Keys.W),
    DOWN(Input.Keys.S),
    LEFT(Input.Keys.A),
    RIGHT(Input.Keys.D),
    SELECT(Input.Keys.F);

    final int[] keyCode;

    GameKeys(int... keyCode) {
        this.keyCode = keyCode;
    }

    public int[] getKeyCode() {
        return keyCode;
    }
}
