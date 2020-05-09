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
    private final GameScreen screen;
    private final ECSEngine ecsEngine;

    public StateComponent stateCmp;

    private TextureAtlas textureAtlas;

    public BubbleEntity(ECSEngine ecsEngine, Entity parentEntity) {
        this.screen = ecsEngine.getScreen();
        this.ecsEngine = ecsEngine;

        transformComponent(parentEntity);
        textureComponent();
        animationComponent();
        stateComponent();

        ecsEngine.addEntity(this);
    }

    private void transformComponent(Entity parentEntity) {
        TextureRegion parentRegion = parentEntity.getComponent(TextureComponent.class).region;
        Vector3 parentPosition = parentEntity.getComponent(TransformComponent.class).position;

        final TransformComponent transformCmp = ecsEngine.createComponent(TransformComponent.class);
        transformCmp.position.set(parentPosition.x + (parentRegion.getRegionWidth() / PPM / 2),
                parentPosition.y + (parentRegion.getRegionHeight() / PPM) + (5 / PPM),
                parentPosition.z);
        this.add(transformCmp);
    }

    private void textureComponent() {
        final TextureComponent textureCmp = ecsEngine.createComponent(TextureComponent.class);
        textureAtlas = Assets.manager.get(AssetName.NPC_BUBBLE.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("npc_bubble");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, 18, 20);
        this.add(textureCmp);
    }

    private void animationComponent() {
        final AnimationComponent animationCmp = ecsEngine.createComponent(AnimationComponent.class);
        createAnimation(animationCmp, textureAtlas);
        this.add(animationCmp);
    }

    private void stateComponent() {
        stateCmp = ecsEngine.createComponent(StateComponent.class);
        stateCmp.set(StateComponent.NPC_BUBBLE_NORMAL);
        this.add(stateCmp);
    }

    private void createAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
        animation.animations.put(StateComponent.NPC_BUBBLE_NORMAL, new Animation(0, textureAtlas.findRegion("npc_bubble")));
        animation.animations.put(StateComponent.NPC_BUBBLE_SHOW, new Animation(0.08f, textureAtlas.findRegions("npc_bubble")));
        animation.animations.put(StateComponent.NPC_BUBBLE_HIDE, new Animation(0.08f, textureAtlas.findRegions("npc_bubble"), Animation.PlayMode.REVERSED));

    }
}
