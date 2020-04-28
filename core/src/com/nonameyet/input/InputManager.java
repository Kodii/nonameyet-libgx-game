package com.nonameyet.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {

    private static InputManager instance = null;

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    private final GameKeys[] keyMapping;
    private final boolean[] keyState;
    private Array<InputListener> listeners;

    public InputManager() {
        this.keyMapping = new GameKeys[256];
        for (final GameKeys gameKey : GameKeys.values()) {
            for (final int code : gameKey.keyCode) {
                keyMapping[code] = gameKey;
            }
        }
        keyState = new boolean[GameKeys.values().length];
        listeners = new Array<>();
    }

    public void addInputListener(final InputListener listener) {
        listeners.add(listener);
    }

    public void removeInputListener(final InputListener listener) {
        listeners.removeValue(listener, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        GameKeys gameKey = keyMapping[keycode];
        if (gameKey == null) {
            // no mapping --> nothing to do
            return false;
        }

        notifyKeyDown(gameKey);

        return false;
    }

    public void notifyKeyDown(final GameKeys gameKey) {
        keyState[gameKey.ordinal()] = true;
        for (final InputListener listener : listeners) {
            listener.keyPressed(this, gameKey);
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        GameKeys gameKey = keyMapping[keycode];
        if (gameKey == null) {
            // no mapping --> nothing to do
            return false;
        }

        notifyKeyUp(gameKey);

        return false;
    }

    public void notifyKeyUp(final GameKeys gameKey) {
        keyState[gameKey.ordinal()] = false;
        for (final InputListener listener : listeners) {
            listener.keyReleased(this, gameKey);
        }
    }

    public boolean isKeyPressed(final GameKeys gameKey) {
        return keyState[gameKey.ordinal()];
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
