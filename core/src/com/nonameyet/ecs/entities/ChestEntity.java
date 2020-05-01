package com.nonameyet.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.b2d.LightBuilder;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.*;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.chest.ChestWindowEvent;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ChestEntity extends Entity implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;
    private final StateComponent state;
    private final B2dBodyComponent b2dbody;
    private final B2dLightComponent b2dlight;

    public ChestEntity(final ECSEngine ecsEngine, final Vector2 chestSpawnLocation) {
        this.screen = ecsEngine.getScreen();

        // Create the Entity and all the components that will go in the entity
        final Entity chestEntity = ecsEngine.createEntity();

        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dlight = new B2dLightComponent(screen);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        final StateComponent state = ecsEngine.createComponent(StateComponent.class);
        this.state = state;

        // create the data for the components and add them to the components
        position.position.set(chestSpawnLocation.x, chestSpawnLocation.y, 0);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.CHEST_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("chest_grass");

        createAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 19, 16);

        b2dbody.body = BodyBuilder.staticPointBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(chestSpawnLocation.x, chestSpawnLocation.y),
                new Vector2(19, 16),
                "CHEST",
                Collision.OBJECT);
        type.type = TypeComponent.CHEST;
        state.set(StateComponent.STATE_CHEST_NORMAL);

        createLight();

        chestEntity.add(position);
        chestEntity.add(animation);
        chestEntity.add(texture);
        chestEntity.add(b2dbody);
        chestEntity.add(type);
        chestEntity.add(state);

        ecsEngine.addEntity(chestEntity);

        // listeners
        screen.getMapMgr().getB2dContactListener().addPropertyChangeListener(this);
    }

    private void createAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {

        animation.animations.put(StateComponent.STATE_CHEST_NORMAL, new Animation(0, textureAtlas.findRegion("chest_grass")));
        animation.animations.put(StateComponent.STATE_CHEST_OPEN, new Animation(0.15f, textureAtlas.findRegions("chest_grass")));
        animation.animations.put(StateComponent.STATE_CHEST_CLOSE, new Animation(0.15f, textureAtlas.findRegions("chest_grass"), Animation.PlayMode.REVERSED));
    }


    private void createLight() {

        b2dlight.distance = 1f;

        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "Chest --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(ChestWindowEvent.class.getSimpleName())) {
            chestWindowEvent((ChestWindowEvent) evt.getNewValue());
        }
    }

    public void chestWindowEvent(ChestWindowEvent event) {

        switch (event) {
            case CHEST_OPENED:
                state.set(StateComponent.STATE_CHEST_OPEN);
                break;
            case CHEST_CLOSED:
                state.set(StateComponent.STATE_CHEST_CLOSE);
                break;
        }
    }

    @Override
    public void dispose() {
        screen.getMapMgr().getB2dContactListener().removePropertyChangeListener(this);
    }
}
