package com.nonameyet.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.AnimationComponent;
import com.nonameyet.ecs.components.StateComponent;
import com.nonameyet.ecs.components.TextureComponent;
import com.nonameyet.ecs.components.TransformComponent;
import com.nonameyet.screens.GameScreen;

import static com.nonameyet.utils.Constants.PPM;

public class BubbleEntity extends Entity {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;
    public final StateComponent state;

    private final Entity parentEntity;

    public BubbleEntity(ECSEngine ecsEngine, Entity parentEntity) {
        this.screen = ecsEngine.getScreen();
        this.parentEntity = parentEntity;

        // Create the Entity and all the components that will go in the entity
        final Entity entity = ecsEngine.createEntity();

        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        state = ecsEngine.createComponent(StateComponent.class);

        // create the data for the components and add them to the components


        TextureRegion region = parentEntity.getComponent(TextureComponent.class).region;
        Vector3 parentPosition = parentEntity.getComponent(TransformComponent.class).position;
        position.position.set(parentPosition.x + (region.getRegionWidth() / PPM / 2),
                parentPosition.y + (region.getRegionHeight() / PPM) + (region.getRegionHeight() / 4 / PPM),
                parentPosition.z);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.NPC_BUBBLE.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("npc_bubble");

        createAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 26, 17);
        state.set(StateComponent.NPC_BUBBLE_NORMAL);


        entity.add(position);
        entity.add(animation);
        entity.add(texture);
        entity.add(state);

        ecsEngine.addEntity(entity);
    }

    private void createAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
        animation.animations.put(StateComponent.NPC_BUBBLE_NORMAL, new Animation(0, textureAtlas.findRegion("npc_bubble")));
        animation.animations.put(StateComponent.NPC_BUBBLE_SHOW, new Animation(0.12f, textureAtlas.findRegions("npc_bubble")));
        animation.animations.put(StateComponent.NPC_BUBBLE_HIDE, new Animation(0.12f, textureAtlas.findRegions("npc_bubble"), Animation.PlayMode.REVERSED));

    }
}
