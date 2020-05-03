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
import com.nonameyet.audio.AudioManager;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.b2d.LightBuilder;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.*;
import com.nonameyet.events.ChestEvent;
import com.nonameyet.input.GameKeyInputListener;
import com.nonameyet.input.GameKeys;
import com.nonameyet.input.InputManager;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChestEntity extends Entity implements Disposable, PropertyChangeListener, GameKeyInputListener {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;
    private final StateComponent state;
    private final B2dBodyComponent b2dbody;
    private final B2dLightComponent b2dlight;

    private final BubbleEntity bubbleEntity;

    // events
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public ChestEntity(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();

        // Create the Entity and all the components that will go in the entity
        final Entity entity = ecsEngine.createEntity();

        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        TriggerB2dBodyComponent triggerb2dbody = ecsEngine.createComponent(TriggerB2dBodyComponent.class);
        b2dlight = new B2dLightComponent(screen);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        final StateComponent state = ecsEngine.createComponent(StateComponent.class);
        this.state = state;

        // create the data for the components and add them to the components
        position.position.set(spawnLocation.x, spawnLocation.y, 1);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.CHEST_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("chest");

        createAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 19, 16);

        b2dbody.body = BodyBuilder.npcFootRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(19, 30),
                "CHEST_BODY",
                Collision.OBJECT);

        triggerb2dbody.trigger = BodyBuilder.triggerBody(
                ecsEngine.getScreen().getWorld(),
                texture.region.getRegionHeight(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                45,
                "CHEST",
                Collision.OBJECT);

        type.type = TypeComponent.NPC;
        state.set(StateComponent.STATE_CHEST_NORMAL);

        createLight();

        entity.add(position);
        entity.add(animation);
        entity.add(texture);
        entity.add(b2dbody);
        entity.add(triggerb2dbody);
        entity.add(type);
        entity.add(state);

        ecsEngine.addEntity(entity);

        bubbleEntity = new BubbleEntity(ecsEngine, entity);

        // listeners
        addPropertyChangeListener(AudioManager.getInstance());
        addPropertyChangeListener(screen.getPlayerHUD());

        screen.getMapMgr().getCollisionSystem().addPropertyChangeListener(this);
    }

    private void createAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {

        animation.animations.put(StateComponent.STATE_CHEST_NORMAL, new Animation(0, textureAtlas.findRegion("chest")));
        animation.animations.put(StateComponent.STATE_CHEST_OPEN, new Animation(0.15f, textureAtlas.findRegions("chest")));
        animation.animations.put(StateComponent.STATE_CHEST_CLOSE, new Animation(0.15f, textureAtlas.findRegions("chest"), Animation.PlayMode.REVERSED));
    }


    private void createLight() {

        b2dlight.distance = 1f;

        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "Chest --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(ChestEvent.NAME)) {
            chestWindowEvent((ChestEvent) evt.getNewValue());
        }
    }

    public void chestWindowEvent(ChestEvent event) {

        switch (event) {
            case SHOW_BUBBLE:
                bubbleEntity.state.set(StateComponent.NPC_BUBBLE_SHOW);
                InputManager.getInstance().addInputListener(this);
                break;
            case HIDE_BUBBLE:
                if (state.get() == StateComponent.STATE_CHEST_OPEN) {
                    state.set(StateComponent.STATE_CHEST_CLOSE);
                    changes.firePropertyChange(ChestEvent.NAME, null, ChestEvent.CHEST_CLOSED);
                }
                if (bubbleEntity.state.get() == StateComponent.NPC_BUBBLE_SHOW)
                    bubbleEntity.state.set(StateComponent.NPC_BUBBLE_HIDE);

                InputManager.getInstance().removeInputListener(this);
                break;
        }
    }

    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key) {
            case SELECT:
                state.set(StateComponent.STATE_CHEST_OPEN);
                bubbleEntity.state.set(StateComponent.NPC_BUBBLE_HIDE);
                changes.firePropertyChange(ChestEvent.NAME, null, ChestEvent.CHEST_OPENED);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(InputManager inputManager, GameKeys key) {

    }

    @Override
    public void dispose() {
        screen.getMapMgr().getCollisionSystem().removePropertyChangeListener(this);

        removePropertyChangeListener(AudioManager.getInstance());
        removePropertyChangeListener(screen.getPlayerHUD());
    }

    public void addPropertyChangeListener(
            PropertyChangeListener p) {
        changes.addPropertyChangeListener(p);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener p) {
        changes.removePropertyChangeListener(p);
    }

}
