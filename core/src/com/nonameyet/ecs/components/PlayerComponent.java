package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component, Pool.Poolable {
    private final String TAG = this.getClass().getSimpleName();

    public float speed;

    @Override
    public void reset() {
        this.speed = 0;
    }
}
