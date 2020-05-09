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

    private final B2dBodyComponent b2dbody;
    private final B2dLightComponent b2dlight;

    public AnvilEntity(ECSEngine ecsEngine, Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();

        // Create the Entity and all the components that will go in the entity
        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dlight = new B2dLightComponent(screen);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        final StateComponent state = ecsEngine.createComponent(StateComponent.class);

        // create the data for the components and add them to the components
        position.position.set(spawnLocation.x, spawnLocation.y, 1);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.ANVIL_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("anvil");

        createRunAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 30, 27);

        b2dbody.body = BodyBuilder.npcFootRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(30, 27),
                "ANVIL",
                Collision.OBJECT
        );
        type.type = TypeComponent.SCENERY_ITEMS;
        state.set(StateComponent.STATE_ANVIL);

        createLight();


        this.add(position);
        this.add(animation);
        this.add(texture);
        this.add(b2dbody);
        this.add(type);
        this.add(state);
        this.add(b2dlight);

        ecsEngine.addEntity(this);
    }

    private void createRunAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
        float frameDuration = 0.3f;

        animation.animations.put(StateComponent.STATE_ANVIL, new Animation(frameDuration, textureAtlas.findRegions("anvil"), Animation.PlayMode.LOOP));
    }

    private void createLight() {
        b2dlight.distance = 1f;

        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);
        b2dlight.light.attachToBody(b2dbody.body);
    }

}
