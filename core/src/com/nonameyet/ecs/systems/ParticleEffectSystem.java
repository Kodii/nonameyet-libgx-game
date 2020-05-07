package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.nonameyet.assets.Assets;
import com.nonameyet.ecs.ComponentMappers;
import com.nonameyet.ecs.components.ParticleEffectComponent;

import java.util.EnumMap;

public class ParticleEffectSystem extends IteratingSystem {
    private final EnumMap<ParticleEffectComponent.ParticleEffectType, ParticleEffectPool> effectPools;

    public ParticleEffectSystem() {
        super(Family.all(ParticleEffectComponent.class).get());

        effectPools = new EnumMap<>(ParticleEffectComponent.ParticleEffectType.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParticleEffectComponent particleEffectCmp = ComponentMappers.particleEffectCmpMapper.get(entity);

        if (particleEffectCmp.effect != null) {
            // update it
            particleEffectCmp.effect.update(deltaTime);
            if (particleEffectCmp.effect.isComplete()) {
                particleEffectCmp.effect.reset();
                particleEffectCmp.effect.setPosition(particleEffectCmp.effectPosition.x, particleEffectCmp.effectPosition.y);
                particleEffectCmp.effect.scaleEffect(particleEffectCmp.scalling);
//                entity.remove(ParticleEffectComponent.class);
            }
        } else if (particleEffectCmp.effectType != null) {
            // create the effect
            ParticleEffectPool effectPool = effectPools.get(particleEffectCmp.effectType);
            if (effectPool == null) {
                ParticleEffect particleEffect = Assets.manager.get(particleEffectCmp.effectType.getEffectFilePath(), ParticleEffect.class);
                particleEffect.setEmittersCleanUpBlendFunction(false);
                effectPool = new ParticleEffectPool(particleEffect, 1, 128);
                effectPools.put(particleEffectCmp.effectType, effectPool);
            }
            particleEffectCmp.effect = effectPool.obtain();
            particleEffectCmp.effect.setPosition(particleEffectCmp.effectPosition.x, particleEffectCmp.effectPosition.y);
            particleEffectCmp.effect.scaleEffect(particleEffectCmp.scalling);
        }
    }
}
