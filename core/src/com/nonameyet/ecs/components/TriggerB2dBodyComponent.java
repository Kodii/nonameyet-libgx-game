package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class TriggerB2dBodyComponent implements Component, Pool.Poolable {

    public Body trigger;

    @Override
    public void reset() {
        if (trigger != null) {
            trigger.getWorld().destroyBody(trigger);
            trigger = null;
        }
    }
}
