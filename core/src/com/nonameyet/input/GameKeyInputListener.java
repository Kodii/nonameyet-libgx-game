package com.nonameyet.input;

public interface GameKeyInputListener {

    void keyPressed(final InputManager inputManager, final GameKeys key);

    void keyReleased(final InputManager inputManager, final GameKeys key);
}
