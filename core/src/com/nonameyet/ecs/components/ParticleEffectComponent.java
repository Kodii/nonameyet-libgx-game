package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class ParticleEffectComponent implements Component, Pool.Poolable {
    public ParticleEffectPool.PooledEffect effect;
    public ParticleEffectType effectType;
    public final Vector2 effectPosition = new Vector2();
    public float scalling;

    @Override
    public void reset() {

        if (effect != null) {
            effect.free();
            effect = null;
        }

        effectPosition.set(0, 0);
        effectType = null;
        scalling = 0;
    }

    public enum ParticleEffectType {
        TORCH("particles/torch.party");

        private final String effectFilePath;

        ParticleEffectType(String effectFilePath) {
            this.effectFilePath = effectFilePath;
        }

        public String getEffectFilePath() {
            return effectFilePath;
        }
    }
}
