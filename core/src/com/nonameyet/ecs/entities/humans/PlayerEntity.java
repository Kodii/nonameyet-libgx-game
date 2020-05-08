package com.nonameyet.ecs.entities.humans;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
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
    private final ECSEngine ecsEngine;

    private final B2dBodyComponent b2dbody;
    private B2dLightComponent b2dlight;
    private final StateComponent state;

    public PlayerEntity(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        this.ecsEngine = ecsEngine;
        this.screen = ecsEngine.getScreen();

        // Create the Entity and all the components that will go in the entity
        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        position.position.set(spawnLocation.x, spawnLocation.y, 0);
        this.add(position);


        final PlayerComponent player = ecsEngine.createComponent(PlayerComponent.class);
        player.speed = 4;
        this.add(player);


        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        type.type = TypeComponent.PLAYER;
        this.add(type);

        state = ecsEngine.createComponent(StateComponent.class);
        state.set(StateComponent.STATE_STANDING_DOWN);
        this.add(state);


        createPlayerWithoutArmComponent();

        createArmComponent();

        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dbody.body = BodyBuilder.playerFootBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(20, 39),
                "PLAYER",
                Collision.PLAYER);
        b2dbody.body.setLinearDamping(20f);
        this.add(b2dbody);

        createLight();

        ecsEngine.addEntity(this);
    }

    private void createPlayerWithoutArmComponent() {

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.PLAYER_WITHOUT_ARM_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("player_idle_down");

        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        texture.region = new TextureRegion(textureRegion, 0, 0, 20, 39);
        this.add(texture);

        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        createRunAnimation(animation.animations, textureAtlas);
        this.add(animation);
    }

    private void createArmComponent() {
        TextureAtlas textureAtlasArm = Assets.manager.get(AssetName.ARM_ATLAS.getAssetName());
        TextureRegion textureRegionArm = textureAtlasArm.findRegion("player_idle_down");

        final SubAnimationComponent animationArm = ecsEngine.createComponent(SubAnimationComponent.class);
        createRunAnimation(animationArm.animations, textureAtlasArm);
        this.add(animationArm);

        final SubTextureComponent textureArm = ecsEngine.createComponent(SubTextureComponent.class);
        textureArm.region = new TextureRegion(textureRegionArm, 0, 0, 20, 39);
        this.add(textureArm);

        final SubB2dBodyComponent subB2dBody = ecsEngine.createComponent(SubB2dBodyComponent.class);

        this.add(subB2dBody);
    }

    private void createRunAnimation(IntMap<Animation> animations, TextureAtlas textureAtlas) {
        float frameDuration = 0.14f;

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

    private void createLight() {
        b2dlight = new B2dLightComponent(screen);

        b2dlight.distance = 1.5f;
        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);
        b2dlight.light.attachToBody(b2dbody.body);

        this.add(b2dlight);
    }
}
