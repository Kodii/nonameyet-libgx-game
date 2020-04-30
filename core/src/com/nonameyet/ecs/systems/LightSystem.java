package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.nonameyet.ecs.ComponentMappers;
import com.nonameyet.ecs.components.B2dBodyComponent;
import com.nonameyet.ecs.components.B2dLightComponent;
import com.nonameyet.ecs.components.LightStateComponent;

public class LightSystem extends IteratingSystem {

    public LightSystem() {
        super(Family.all(B2dBodyComponent.class, B2dLightComponent.class, LightStateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final B2dLightComponent b2dlight = ComponentMappers.b2dlightCmpMapper.get(entity);
        final LightStateComponent lightState = ComponentMappers.lightStateCmpMapper.get(entity);


        if (lightState.get() == LightStateComponent.STATE__ON && b2dlight.flDistance > 0) {
            b2dlight.flTime += (b2dlight.flSpeed * deltaTime);

            if (b2dlight.flTime > MathUtils.PI2) {
                b2dlight.flTime = 0;
            }
            if (b2dlight.callEvery > 0) {
                b2dlight.renderTime += deltaTime;
                if (b2dlight.renderTime >= b2dlight.callEvery) {
                    b2dlight.light.setDistance(b2dlight.distance + MathUtils.sin(b2dlight.flTime) * b2dlight.flDistance);
                    b2dlight.renderTime = 0;
                }
            } else {
                b2dlight.light.setDistance(b2dlight.distance + MathUtils.sin(b2dlight.flTime) * b2dlight.flDistance);
            }

        }
    }
}
