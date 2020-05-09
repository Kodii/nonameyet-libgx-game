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

public class AnvilEntity extends Entity {
    private final GameScreen screen;
    private final ECSEngine ecsEngine;

    private B2dBodyComponent b2dBodyCmp;
    private B2dLightComponent b2dLightCmp;

    private TextureAtlas textureAtlas;

    public AnvilEntity(ECSEngine ecsEngine, Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();
        this.ecsEngine = ecsEngine;

        positionComponent(spawnLocation);
        textureComponent();
        animationComponent();
        b2dBodyComponent(spawnLocation);
        b2dLightComponent();
        stateComponent();
        typeComponent();

        ecsEngine.addEntity(this);
    }

    private void positionComponent(Vector2 spawnLocation) {
        final TransformComponent positionCmp = ecsEngine.createComponent(TransformComponent.class);
        positionCmp.position.set(spawnLocation.x, spawnLocation.y, 1);
        this.add(positionCmp);
    }

    private void textureComponent() {
        final TextureComponent textureCmp = ecsEngine.createComponent(TextureComponent.class);
        textureAtlas = Assets.manager.get(AssetName.ANVIL_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("anvil");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, 30, 27);
        this.add(textureCmp);
    }

    private void animationComponent() {
        final AnimationComponent animationCmp = ecsEngine.createComponent(AnimationComponent.class);
        createAnimation(animationCmp, textureAtlas);
        this.add(animationCmp);
    }

    private void createAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
        float frameDuration = 0.3f;

        animation.animations.put(StateComponent.STATE_ANVIL, new Animation(frameDuration, textureAtlas.findRegions("anvil"), Animation.PlayMode.LOOP));
    }

    private void b2dBodyComponent(Vector2 spawnLocation) {
        b2dBodyCmp = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dBodyCmp.body = BodyBuilder.npcFootRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(30, 27),
                this,
                Collision.OBJECT);
        this.add(b2dBodyCmp);
    }

    private void createLight() {
        b2dLightCmp.distance = 1f;

        b2dLightCmp.light = LightBuilder.pointLight(screen.getRayHandler(), b2dBodyCmp.body, Color.valueOf("#e28822"), b2dLightCmp.distance);
        b2dLightCmp.light.setSoft(true);
        b2dLightCmp.light.attachToBody(b2dBodyCmp.body);
    }

    private void b2dLightComponent() {
        b2dLightCmp = new B2dLightComponent(screen);
        createLight();
        this.add(b2dLightCmp);
    }

    private void stateComponent() {
        final StateComponent state = ecsEngine.createComponent(StateComponent.class);
        state.set(StateComponent.STATE_ANVIL);
        this.add(state);
    }

    private void typeComponent() {
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY_ITEMS;
        this.add(type);
    }

}
