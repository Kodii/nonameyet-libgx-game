package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class SocketComponent implements Component, Pool.Poolable {
    public Entity itemEntity;

    @Override
    public void reset() {
        itemEntity = null;
    }
}
