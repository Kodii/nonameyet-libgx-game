package com.nonameyet.ecs.entities.humans;

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
import com.nonameyet.ecs.entities.BubbleEntity;
import com.nonameyet.events.BlacksmithEvent;
import com.nonameyet.input.GameKeyInputListener;
import com.nonameyet.input.GameKeys;
import com.nonameyet.input.InputManager;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BlacksmithEntity extends Entity implements Disposable, PropertyChangeListener, GameKeyInputListener {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;

    private final B2dBodyComponent b2dbody;
    private final B2dLightComponent b2dlight;

    private final BubbleEntity bubbleEntity;

    // events
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public BlacksmithEntity(ECSEngine ecsEngine, Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();

        // Create the Entity and all the components that will go in the entity
        final NpcComponent npc = ecsEngine.createComponent(NpcComponent.class);
        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        TriggerB2dBodyComponent triggerb2dbody = ecsEngine.createComponent(TriggerB2dBodyComponent.class);
        b2dlight = new B2dLightComponent(screen);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        final StateComponent state = ecsEngine.createComponent(StateComponent.class);

        // create the data for the components and add them to the components
        position.position.set(spawnLocation.x, spawnLocation.y, 1);

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.BLACKSMITH_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("blacksmith");

        createRunAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 26, 40);

        b2dbody.body = BodyBuilder.npcFootRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(26, 40),
                "BLACKSMITH_BODY",
                Collision.NPC);

        triggerb2dbody.trigger = BodyBuilder.triggerBody(
                ecsEngine.getScreen().getWorld(),
                texture.region.getRegionHeight(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                50,
                "BLACKSMITH",
                Collision.NPC);

        type.type = TypeComponent.NPC;
        state.set(StateComponent.STATE_BLACKSMITH);

        createLight();


        this.add(position);
        this.add(animation);
        this.add(texture);
        this.add(b2dbody);
        this.add(triggerb2dbody);
        this.add(npc);
        this.add(type);
        this.add(state);
        this.add(b2dlight);

        ecsEngine.addEntity(this);

        bubbleEntity = new BubbleEntity(ecsEngine, this);

        // listeners
        screen.getMapMgr().getCollisionSystem().addPropertyChangeListener(this);
    }

    private void createRunAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
        float frameDuration = 0.1f;

        TextureAtlas.AtlasRegion first = textureAtlas.findRegion("blacksmith", 0);
        TextureAtlas.AtlasRegion second = textureAtlas.findRegion("blacksmith", 1);
        TextureAtlas.AtlasRegion last = textureAtlas.findRegion("blacksmith", 2);

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 12; i++) {
            frames.addAll(first);
        }

        frames.addAll(first, second, last);

        for (int i = 0; i < 12; i++) {
            frames.addAll(last);
        }

        frames.addAll(last, second, first);

        animation.animations.put(StateComponent.STATE_BLACKSMITH, new Animation(frameDuration, frames, Animation.PlayMode.LOOP));

    }

    private void createLight() {
        b2dlight.distance = 1f;

        b2dlight.light = LightBuilder.pointLight(screen.getRayHandler(), b2dbody.body, Color.valueOf("#e28822"), b2dlight.distance);
        b2dlight.light.setSoft(true);
        b2dlight.light.attachToBody(b2dbody.body);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "Blacksmith --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(BlacksmithEvent.NAME)) {
            showDialogMark((BlacksmithEvent) evt.getNewValue());
        }
    }

    private void showDialogMark(BlacksmithEvent event) {
        switch (event) {
            case SHOW_BUBBLE:
                bubbleEntity.state.set(StateComponent.NPC_BUBBLE_SHOW);
                InputManager.getInstance().addInputListener(this);
                break;
            case HIDE_BUBBLE:
                bubbleEntity.state.set(StateComponent.NPC_BUBBLE_HIDE);
                changes.firePropertyChange(BlacksmithEvent.NAME, null, BlacksmithEvent.BLACKSMITH_CLOSED);
                InputManager.getInstance().removeInputListener(this);
                break;
            default:
                break;
        }
    }


    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key) {
            case SELECT:
                changes.firePropertyChange(BlacksmithEvent.NAME, null, BlacksmithEvent.BLACKSMITH_OPENED);
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
        // listeners
        screen.getMapMgr().getCollisionSystem().removePropertyChangeListener(this);
    }
}

