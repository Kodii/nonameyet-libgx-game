package com.nonameyet.ui;

public interface StatusListener {

    public static enum StatusEvent {

        ADD_HP,
        REMOVE_HP,
        UPGRADE_HP,
        HEAL_HP

    }

    void update(StatusEvent event);
}
