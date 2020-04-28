package com.nonameyet.input;

public interface InputListener {

    void keyPressed(final InputManager inputManager, final GameKeys key);

    void keyReleased(final InputManager inputManager, final GameKeys key);
}
