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

public class ElderEntity extends Entity {
    private final GameScreen screen;

    private final B2dBodyComponent b2dbody;
    private final B2dLightComponent b2dlight;

    public ElderEntity(ECSEngine ecsEngine, Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();

        // Create the Entity and all the components that will go in the entity
        final Entity entity = ecsEngine.createEntity();

        final NpcComponent npc = ecsEngine.createComponent(NpcComponent.class);
        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dlight = new B2dLightComponent(screen);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        final StateComponent state = ecsEngine.createComponent(StateComponent.class);

        // create the data for the components and add them to the components
        position.position.set(spawnLocation.x, spawnLocation.y, 1);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.ELDER_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("elder");


        createRunAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 26, 40);

        b2dbody.body = BodyBuilder.staticFootRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(20, 36),
                "ELDER",
                Collision.NPC
        );
        type.type = TypeComponent.NPC;
        state.set(StateComponent.STATE_ELDER);

        createLight();


        entity.add(position);
        entity.add(animation);
        entity.add(texture);
        entity.add(b2dbody);
        entity.add(npc);
        entity.add(type);
        entity.add(state);
        entity.add(b2dlight);

        ecsEngine.addEntity(entity);
    }

    private void createRunAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
        float frameDuration = 0.1f;

        TextureAtlas.AtlasRegion first = textureAtlas.findRegion("elder", 0);
        TextureAtlas.AtlasRegion second = textureAtlas.findRegion("elder", 1);
        TextureAtlas.AtlasRegion third = textureAtlas.findRegion("elder", 2);
        TextureAtlas.AtlasRegion last = textureAtlas.findRegion("elder", 3);

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 12; i++) {
            frames.addAll(first);
        }
        frames.addAll(first, second, third, last);
        for (int i = 0; i < 12; i++) {
            frames.addAll(last);
        }
        frames.addAll(last, third, second, first);

        animation.animations.put(StateComponent.STATE_ELDER, new Animation(frameDuration, frames, Animation.PlayMode.LOOP));

    }

    private void createLight() {
        b2dlight.distance = 2f;

        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);
        b2dlight.light.attachToBody(b2dbody.body);
    }
}
