package com.nonameyet.utils;


import java.util.HashMap;

public enum GameState {
    SAVING, LOADING, RUNNING, PAUSED, GAME_OVER;

    private static final HashMap<String, GameState> mappings = new HashMap<>(5);

    static {
        for (GameState gameState : GameState.values()) {
            mappings.put(gameState.name(), gameState);
        }
    }


    public static GameState resolve(String state) {
        return state != null ? mappings.get(state) : null;
    }
}

