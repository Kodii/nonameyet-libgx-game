package com.nonameyet.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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

    public PlayerEntity(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();


        // Create the Entity and all the components that will go in the entity
        final Entity entity = ecsEngine.createEntity();

        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dlight = new B2dLightComponent(screen);
        final PlayerComponent player = ecsEngine.createComponent(PlayerComponent.class);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        state = ecsEngine.createComponent(StateComponent.class);

        // create the data for the components and add them to the components
        position.position.set(spawnLocation.x, spawnLocation.y, 0);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.PLAYER2_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("player_idle_right");


        createRunAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 22, 39);

        b2dbody.body = BodyBuilder.playerFootBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(14, 39),
                "PLAYER",
                Collision.PLAYER);
        b2dbody.body.setLinearDamping(20f);

        player.speed = 4;
        type.type = TypeComponent.PLAYER;
        state.set(StateComponent.STATE_STANDING_DOWN);

        createLight();


        entity.add(position);
        entity.add(animation);
        entity.add(texture);
        entity.add(b2dbody);
        entity.add(player);
        entity.add(type);
        entity.add(state);
        entity.add(b2dlight);

        ecsEngine.addEntity(entity);
    }

    private void createRunAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
        float frameDuration = 0.14f;

        TextureAtlas.AtlasRegion player_idle_up = textureAtlas.findRegion("player_idle_up");
        animation.animations.put(StateComponent.STATE_STANDING_UP, new Animation(0, player_idle_up));

        TextureAtlas.AtlasRegion player_idle_right = textureAtlas.findRegion("player_idle_right");
        animation.animations.put(StateComponent.STATE_STANDING_RIGHT, new Animation(0, player_idle_right));

        TextureAtlas.AtlasRegion player_idle_down = textureAtlas.findRegion("player_idle_down");
        animation.animations.put(StateComponent.STATE_STANDING_DOWN, new Animation(0, player_idle_down));

        TextureAtlas.AtlasRegion player_idle_left = textureAtlas.findRegion("player_idle_left");
        animation.animations.put(StateComponent.STATE_STANDING_LEFT, new Animation(0, player_idle_left));

        Array<TextureAtlas.AtlasRegion> player_run_up = textureAtlas.findRegions("player_run_up");
        animation.animations.put(StateComponent.STATE_RUNNING_UP, new Animation(frameDuration, player_run_up, Animation.PlayMode.LOOP));

        Array<TextureAtlas.AtlasRegion> player_run_right = textureAtlas.findRegions("player_run_right");
        animation.animations.put(StateComponent.STATE_RUNNING_RIGHT, new Animation(frameDuration, player_run_right, Animation.PlayMode.LOOP));

        Array<TextureAtlas.AtlasRegion> player_run_left = textureAtlas.findRegions("player_run_left");
        animation.animations.put(StateComponent.STATE_RUNNING_DOWN, new Animation(frameDuration, player_run_left, Animation.PlayMode.LOOP));
        animation.animations.put(StateComponent.STATE_RUNNING_LEFT, new Animation(frameDuration, player_run_left, Animation.PlayMode.LOOP));

    }

    private void createLight() {

        b2dlight.distance = 1.5f;

        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);
        b2dlight.light.attachToBody(b2dbody.body);
    }
}
