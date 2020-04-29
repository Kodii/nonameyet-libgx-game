package com.nonameyet.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.*;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.clock.DayTimeEvent;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TorchEntity extends Entity implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;
    private final StateComponent state;

    public TorchEntity(ECSEngine ecsEngine, Vector2 torchLocation) {
        this.screen = ecsEngine.getScreen();

        // Create the Entity and all the components that will go in the entity
        final Entity torchEntity = ecsEngine.createEntity();

        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        final BodyComponent b2dbody = ecsEngine.createComponent(BodyComponent.class);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        final StateComponent state = ecsEngine.createComponent(StateComponent.class);
        this.state = state;

        // create the data for the components and add them to the components
        position.position.set(torchLocation.x, torchLocation.y, 0);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.TORCH_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("torch");

        createAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 6, 15);

        b2dbody.body = BodyBuilder.staticPointBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(torchLocation.x, torchLocation.y),
                new Vector2(6, 15),
                "TORCH",
                Collision.LIGHT);

        type.type = TypeComponent.TORCH;
        state.set(StateComponent.STATE_TORCH_OFF);


        torchEntity.add(position);
        torchEntity.add(animation);
        torchEntity.add(texture);
        torchEntity.add(b2dbody);
        torchEntity.add(type);
        torchEntity.add(state);

        ecsEngine.addEntity(torchEntity);

        // listeners
        screen.getPlayerHUD().getClockUI().addPropertyChangeListener(this);
    }

    private void createAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
        animation.animations.put(StateComponent.STATE_TORCH_OFF, new Animation(0, textureAtlas.findRegion("torch")));

        Array<TextureAtlas.AtlasRegion> torchRegions = textureAtlas.findRegions("torch");
        torchRegions.removeIndex(0);
        animation.animations.put(StateComponent.STATE_TORCH_ON, new Animation(0.1f, torchRegions, Animation.PlayMode.LOOP_PINGPONG));

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
//                torchLight.setActive(false);
                break;

            case DUSK:
            case NIGHT:
                state.set(StateComponent.STATE_TORCH_ON);
//                torchLight.setActive(true);
                break;
        }
    }


    @Override
    public void dispose() {
        screen.getPlayerHUD().getClockUI().removePropertyChangeListener(this);

    }

}
