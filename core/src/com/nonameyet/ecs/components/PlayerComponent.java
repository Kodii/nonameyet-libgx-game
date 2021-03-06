package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * PlayerComponent should have info about player like: speed, health, lvl, exp etc
 */
public class PlayerComponent implements Component, Pool.Poolable {
    public float speed;

    @Override
    public void reset() {
        this.speed = 0;
    }
}
