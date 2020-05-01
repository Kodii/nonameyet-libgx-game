package com.nonameyet.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.b2d.LightBuilder;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.*;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.utils.Collision;

public class PlayerEntity extends Entity {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;

    private final B2dBodyComponent b2dbody;
    private final B2dLightComponent b2dlight;
    private final StateComponent state;

    public PlayerEntity(final ECSEngine ecsEngine, final Vector2 playerSpawnLocation) {
        this.screen = ecsEngine.getScreen();


        // Create the Entity and all the components that will go in the entity
        final Entity playerEntity = ecsEngine.createEntity();

        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dlight = new B2dLightComponent(screen);
        final PlayerComponent player = ecsEngine.createComponent(PlayerComponent.class);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        state = ecsEngine.createComponent(StateComponent.class);

        // create the data for the components and add them to the components
        position.position.set(playerSpawnLocation.x, playerSpawnLocation.y, 0);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.PLAYER_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("player_idle_right");


        createRunAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 28, 40);

        b2dbody.body = BodyBuilder.dynamicRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(playerSpawnLocation.x, playerSpawnLocation.y),
                new Vector2(14, 36),
                "PLAYER",
                Collision.PLAYER);
        b2dbody.body.setLinearDamping(20f);

        player.speed = 4;
        type.type = TypeComponent.PLAYER;
        state.set(StateComponent.STATE_STANDING_DOWN);

        createLight();


        playerEntity.add(position);
        playerEntity.add(animation);
        playerEntity.add(texture);
        playerEntity.add(b2dbody);
        playerEntity.add(player);
        playerEntity.add(type);
        playerEntity.add(state);
        playerEntity.add(b2dlight);

        ecsEngine.addEntity(playerEntity);
    }

    private void createRunAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
        float frameDuration = 0.17f;

        animation.animations.put(StateComponent.STATE_STANDING_DOWN, new Animation(0, textureAtlas.findRegion("player_idle_left")));
        animation.animations.put(StateComponent.STATE_STANDING_LEFT, new Animation(0, textureAtlas.findRegion("player_idle_left")));
        animation.animations.put(StateComponent.STATE_STANDING_UP, new Animation(0, textureAtlas.findRegion("player_idle_right")));
        animation.animations.put(StateComponent.STATE_STANDING_RIGHT, new Animation(0, textureAtlas.findRegion("player_idle_right")));

        animation.animations.put(StateComponent.STATE_RUNNING_DOWN, new Animation(frameDuration, textureAtlas.findRegions("player_run_left"), Animation.PlayMode.LOOP));
        animation.animations.put(StateComponent.STATE_RUNNING_LEFT, new Animation(frameDuration, textureAtlas.findRegions("player_run_left"), Animation.PlayMode.LOOP));
        animation.animations.put(StateComponent.STATE_RUNNING_RIGHT, new Animation(frameDuration, textureAtlas.findRegions("player_run_right"), Animation.PlayMode.LOOP));
        animation.animations.put(StateComponent.STATE_RUNNING_UP, new Animation(frameDuration, textureAtlas.findRegions("player_run_right"), Animation.PlayMode.LOOP));
    }

    private void createLight() {

        b2dlight.distance = 1f;

        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);
        b2dlight.light.attachToBody(b2dbody.body, 0, -0.6f);
    }
}
