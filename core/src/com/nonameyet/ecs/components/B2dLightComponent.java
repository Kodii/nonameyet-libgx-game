package com.nonameyet.ecs.components;

import box2dLight.Light;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class B2dLightComponent implements Component, Pool.Poolable {

    public Light light;

    public float distance = 0;
    public float flDistance = 0;
    public float flSpeed = 0;

    // render every i > callEvery
    public float callEvery = 0;

    // dont touch
    public float flTime = 0;
    public float renderTime = 0;

    @Override
    public void reset() {
        light.dispose();
        distance = 0;
        flDistance = 0;
        flSpeed = 0;
        flTime = 0;
        renderTime = 0;
    }
}
