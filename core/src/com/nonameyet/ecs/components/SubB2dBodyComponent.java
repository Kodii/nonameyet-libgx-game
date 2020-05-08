package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class SubB2dBodyComponent implements Component, Pool.Poolable {
    public Body body;

    @Override
    public void reset() {
        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }
    }
}
