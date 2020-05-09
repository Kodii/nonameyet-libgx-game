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

    private B2dBodyComponent b2dBodyCmp;

    public final ArmEntity armEntity;

    private TextureAtlas textureAtlas;

    public PlayerEntity(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        this.ecsEngine = ecsEngine;
        this.screen = ecsEngine.getScreen();

        playerComponent();
        transformComponent(spawnLocation);
        textureComponent();
        animationComponent();
        b2dBodyComponent(spawnLocation);
        b2dLightComponent();
        stateComponent();
        typeComponent();

        armEntity = new ArmEntity(ecsEngine, spawnLocation);

        ecsEngine.addEntity(this);
    }

    private void playerComponent() {
        final PlayerComponent playerCmp = ecsEngine.createComponent(PlayerComponent.class);
        playerCmp.speed = 4;
        this.add(playerCmp);
    }

    private void b2dBodyComponent(Vector2 spawnLocation) {
        b2dBodyCmp = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dBodyCmp.body = BodyBuilder.playerFootBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(20, 39),
                this,
                Collision.PLAYER);
        b2dBodyCmp.body.setLinearDamping(20f);
        this.add(b2dBodyCmp);
    }

    private void transformComponent(Vector2 spawnLocation) {
        final TransformComponent transformCmp = ecsEngine.createComponent(TransformComponent.class);
        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 2);
        this.add(transformCmp);
    }

    private void textureComponent() {
        final TextureComponent textureCmp = ecsEngine.createComponent(TextureComponent.class);
        textureAtlas = Assets.manager.get(AssetName.PLAYER_WITHOUT_ARM_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("player_idle_down");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, 20, 39);
        this.add(textureCmp);
    }

    private void animationComponent() {
        final AnimationComponent animationCmp = ecsEngine.createComponent(AnimationComponent.class);
        createRunAnimation(animationCmp.animations, textureAtlas);
        this.add(animationCmp);
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

    private void b2dLightComponent() {
        final B2dLightComponent b2dLightCmp = new B2dLightComponent(screen);

        b2dLightCmp.distance = 1.5f;
        b2dLightCmp.light = LightBuilder.pointLight(screen.getRayHandler(), b2dBodyCmp.body, Color.valueOf("#e28822"), b2dLightCmp.distance);
        b2dLightCmp.light.setSoft(true);
        b2dLightCmp.light.attachToBody(b2dBodyCmp.body);

        this.add(b2dLightCmp);
    }

    private void stateComponent() {
        final StateComponent stateCmp = ecsEngine.createComponent(StateComponent.class);
        stateCmp.set(StateComponent.STATE_STANDING_DOWN);
        this.add(stateCmp);
    }

    private void typeComponent() {
        final TypeComponent typeCmp = ecsEngine.createComponent(TypeComponent.class);
        typeCmp.type = TypeComponent.PLAYER;
        this.add(typeCmp);
    }
}
