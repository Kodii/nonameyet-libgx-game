package com.nonameyet.utils;

import com.sun.istack.internal.Nullable;

import java.util.HashMap;

public enum GameState {
    SAVING, LOADING, RUNNING, PAUSED, GAME_OVER;

    private static final HashMap<String, GameState> mappings = new HashMap<>(5);

    static {
        for (GameState gameState : GameState.values()) {
            mappings.put(gameState.name(), gameState);
        }
    }

    @Nullable
    public static GameState resolve(@Nullable String state) {
        return state != null ? mappings.get(state) : null;
    }
}

