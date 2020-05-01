package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class NpcComponent implements Component, Pool.Poolable {

    public float animationBrake = 0;

    @Override
    public void reset() {
        animationBrake = 0;
    }


}
