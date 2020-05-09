package com.nonameyet.ecs.entities.humans;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.*;
import com.nonameyet.ecs.entities.items.ItemsCreator;

public class ArmEntity extends Entity {
    private final String TAG = this.getClass().getSimpleName();

    private final ECSEngine ecsEngine;

    public final StateComponent stateCmp;
    public TransformComponent transformCmp;
    public final SocketComponent socketCmp;

    public ArmEntity(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        this.ecsEngine = ecsEngine;

        createArmComponent(spawnLocation);

        final TypeComponent typeCmp = ecsEngine.createComponent(TypeComponent.class);
        typeCmp.type = TypeComponent.ARM_PLAYER;
        this.add(typeCmp);

        stateCmp = ecsEngine.createComponent(StateComponent.class);
        stateCmp.set(StateComponent.STATE_STANDING_DOWN);
        this.add(stateCmp);

        socketCmp = ecsEngine.createComponent(SocketComponent.class);
        socketCmp.itemEntity = ItemsCreator.createSword(ecsEngine, spawnLocation);
        this.add(socketCmp);

        this.ecsEngine.addEntity(this);

    }

    private void createArmComponent(final Vector2 spawnLocation) {
        TextureAtlas textureAtlas = Assets.manager.get(AssetName.ARM_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("player_idle_down");

        transformCmp = ecsEngine.createComponent(TransformComponent.class);
        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 0);
        this.add(transformCmp);

        final AnimationComponent animationCmp = ecsEngine.createComponent(AnimationComponent.class);
        createRunAnimation(animationCmp.animations, textureAtlas);
        this.add(animationCmp);

        final TextureComponent textureCmp = ecsEngine.createComponent(TextureComponent.class);
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, 20, 39);
        this.add(textureCmp);
    }

    private void createRunAnimation(IntMap<Animation> animations, TextureAtlas textureAtlas) {
        float frameDuration = 0.14F;

        TextureAtlas.AtlasRegion player_idle_up = textureAtlas.findRegion("player_idle_up");
        animations.put(StateComponent.STATE_STANDING_UP, new Animation(0, player_idle_up));

        TextureAtlas.AtlasRegion player_idle_right = textureAtlas.findRegion("player_idle_right");
        animations.put(StateComponent.STATE_STANDING_RIGHT, new Animation(0, player_idle_right));

        TextureAtlas.AtlasRegion player_idle_down = textureAtlas.findRegion("player_idle_down");
        animations.put(StateComponent.STATE_STANDING_DOWN, new Animation(0, player_idle_down));

        TextureAtlas.AtlasRegion player_idle_left = textureAtlas.findRegion("player_idle_left");
        animations.put(StateComponent.STATE_STANDING_LEFT, new Animation(0, player_idle_left));

        Array<TextureAtlas.AtlasRegion> player_run_right = textureAtlas.findRegions("player_run_right");
        animations.put(StateComponent.STATE_RUNNING_UP, new Animation(frameDuration, player_run_right, Animation.PlayMode.LOOP));
        animations.put(StateComponent.STATE_RUNNING_RIGHT, new Animation(frameDuration, player_run_right, Animation.PlayMode.LOOP));

        Array<TextureAtlas.AtlasRegion> player_run_left = textureAtlas.findRegions("player_run_left");
        animations.put(StateComponent.STATE_RUNNING_DOWN, new Animation(frameDuration, player_run_left, Animation.PlayMode.LOOP));
        animations.put(StateComponent.STATE_RUNNING_LEFT, new Animation(frameDuration, player_run_left, Animation.PlayMode.LOOP));

    }
}
