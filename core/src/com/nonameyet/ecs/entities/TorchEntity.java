package com.nonameyet.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.b2d.LightBuilder;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.*;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.clock.DayTimeEvent;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TorchEntity extends Entity implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();

    final AnimationComponent animation;
    private final B2dBodyComponent b2dbody;
    private final B2dLightComponent b2dlight;
    private final GameScreen screen;
    private final StateComponent state;

    public TorchEntity(ECSEngine ecsEngine, Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();

        // Create the Entity and all the components that will go in the entity
        final Entity entity = ecsEngine.createEntity();

        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dlight = new B2dLightComponent(screen);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        state = ecsEngine.createComponent(StateComponent.class);


        // create the data for the components and add them to the components
        position.position.set(spawnLocation.x, spawnLocation.y, 0);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.TORCH_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("torch");

        createAnimation(textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 6, 15);

        b2dbody.body = BodyBuilder.staticPointBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(6, 15),
                "TORCH",
                Collision.LIGHT);

        type.type = TypeComponent.TORCH;
        state.set(StateComponent.STATE_TORCH_OFF);

        createLight();


        entity.add(position);
        entity.add(animation);
        entity.add(texture);
        entity.add(b2dbody);
        entity.add(type);
        entity.add(state);
        entity.add(b2dlight);

        ecsEngine.addEntity(entity);

        // listeners
        screen.getPlayerHUD().getClockUI().addPropertyChangeListener(this);
    }

    private void createAnimation(TextureAtlas textureAtlas) {
        animation.animations.put(StateComponent.STATE_TORCH_OFF, new Animation(0, textureAtlas.findRegion("torch")));

        Array<TextureAtlas.AtlasRegion> torchRegions = textureAtlas.findRegions("torch");
        torchRegions.removeIndex(0);
        animation.animations.put(StateComponent.STATE_TORCH_ON, new Animation(0.1f, torchRegions, Animation.PlayMode.LOOP_PINGPONG));

    }

    private void createLight() {
        float random = (float) (2.6f + Math.random() * (2.9f - 2.3f));

        b2dlight.distance = random;

        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);

        b2dlight.callEvery = 0.15f;
        b2dlight.flSpeed = 2;
        b2dlight.flDistance = b2dlight.light.getDistance() * 0.1f;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "Torch --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(DayTimeEvent.class.getSimpleName())) {
            lightsEvent((DayTimeEvent) evt.getNewValue());
        }
    }

    private void lightsEvent(DayTimeEvent event) {
        switch (event) {
            case DAWN:
            case AFTERNOON:
                state.set(StateComponent.STATE_TORCH_OFF);
                break;

            case DUSK:
            case NIGHT:
                state.set(StateComponent.STATE_TORCH_ON);
                break;
        }
    }


    @Override
    public void dispose() {
        screen.getPlayerHUD().getClockUI().removePropertyChangeListener(this);

    }

}
