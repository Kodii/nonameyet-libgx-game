package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.nonameyet.ecs.components.AnimationComponent;
import com.nonameyet.ecs.components.NpcComponent;
import com.nonameyet.ecs.components.StateComponent;
import com.nonameyet.ecs.components.TextureComponent;

import static com.nonameyet.ecs.ComponentMappers.*;


public class AnimationSystem extends IteratingSystem {

    @SuppressWarnings("unchecked")
    public AnimationSystem() {
        super(Family.all(TextureComponent.class, AnimationComponent.class, StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        AnimationComponent animation = animationCmpMapper.get(entity);
        StateComponent state = stateCmpMapper.get(entity);
        NpcComponent npc = npcCmpMapper.get(entity);

        if (animation.animations.containsKey(state.get())) {
            TextureComponent texture = textureCmpMapper.get(entity);
            texture.region = (TextureRegion) animation.animations.get(state.get()).getKeyFrame(state.time, state.isLooping);
        }

        state.time += deltaTime;
    }
}
