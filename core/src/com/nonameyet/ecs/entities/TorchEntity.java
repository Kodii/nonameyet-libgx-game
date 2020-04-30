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
    private final LightStateComponent lightState;

    public TorchEntity(ECSEngine ecsEngine, Vector2 torchLocation) {
        this.screen = ecsEngine.getScreen();

        // Create the Entity and all the components that will go in the entity
        final Entity torchEntity = ecsEngine.createEntity();

        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        state = ecsEngine.createComponent(StateComponent.class);
        b2dlight = ecsEngine.createComponent(B2dLightComponent.class);
        lightState = ecsEngine.createComponent(LightStateComponent.class);

        // create the data for the components and add them to the components
        position.position.set(torchLocation.x, torchLocation.y, 0);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.TORCH_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("torch");

        createAnimation(textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 6, 15);

        b2dbody.body = BodyBuilder.staticPointBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(torchLocation.x, torchLocation.y),
                new Vector2(6, 15),
                "TORCH",
                Collision.LIGHT);

        type.type = TypeComponent.TORCH;
        state.set(StateComponent.STATE_TORCH_OFF);

        createLight();


        torchEntity.add(position);
        torchEntity.add(animation);
        torchEntity.add(texture);
        torchEntity.add(b2dbody);
        torchEntity.add(type);
        torchEntity.add(state);
        torchEntity.add(b2dlight);
        torchEntity.add(lightState);

        ecsEngine.addEntity(torchEntity);

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
        float random = (float) (1.9f + Math.random() * (2.1f - 1.9f));

        b2dlight.distance = random;

        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);

        b2dlight.flSpeed = 3;
        b2dlight.flDistance = b2dlight.light.getDistance() * 0.1f;

        lightState.set(LightStateComponent.STATE_OFF);
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
                lightState.set(LightStateComponent.STATE_OFF);
                break;

            case DUSK:
            case NIGHT:
                state.set(StateComponent.STATE_TORCH_ON);
                lightState.set(LightStateComponent.STATE__ON);
                break;
        }
    }


    @Override
    public void dispose() {
        screen.getPlayerHUD().getClockUI().removePropertyChangeListener(this);

    }

}
