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
    private final ECSEngine ecsEngine;

    private TextureComponent textureCmp;
    private StateComponent stateCmp;
    private B2dBodyComponent b2dBodyCmp;
    private B2dLightComponent b2dLightCmp;

    private final BubbleEntity bubbleEntity;

    private TextureAtlas textureAtlas;

    // events
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public ChestEntity(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();
        this.ecsEngine = ecsEngine;

        transformComponent(spawnLocation);
        textureComponent();
        animationComponent();
        b2dBodyComponent(spawnLocation);
        triggerB2dBodyComponent(spawnLocation);
        b2dLightComponent();
        stateComponent();
        typeComponent();

        ecsEngine.addEntity(this);

        bubbleEntity = new BubbleEntity(ecsEngine, this);

        // listeners
        addPropertyChangeListener(AudioManager.getInstance());
        addPropertyChangeListener(screen.getPlayerHUD());

        screen.getMapMgr().getCollisionSystem().addPropertyChangeListener(this);
    }

    private void transformComponent(Vector2 spawnLocation) {
        final TransformComponent transformCmp = ecsEngine.createComponent(TransformComponent.class);
        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 1);
        this.add(transformCmp);
    }

    private void textureComponent() {
        textureCmp = ecsEngine.createComponent(TextureComponent.class);
        textureAtlas = Assets.manager.get(AssetName.CHEST_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("chest");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, 18, 20);
        this.add(textureCmp);
    }

    private void animationComponent() {
        final AnimationComponent animationCmp = ecsEngine.createComponent(AnimationComponent.class);
        createAnimation(animationCmp, textureAtlas);
        this.add(animationCmp);
    }

    private void b2dBodyComponent(Vector2 spawnLocation) {
        b2dBodyCmp = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dBodyCmp.body = BodyBuilder.npcFootRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(19, 30),
                this,
                Collision.OBJECT);
        this.add(b2dBodyCmp);
    }

    private void triggerB2dBodyComponent(Vector2 spawnLocation) {
        TriggerB2dBodyComponent triggerB2dBodyCmp = ecsEngine.createComponent(TriggerB2dBodyComponent.class);
        triggerB2dBodyCmp.trigger = BodyBuilder.triggerCircleBody(
                ecsEngine.getScreen().getWorld(),
                textureCmp.region.getRegionHeight(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                45,
                this,
                Collision.OBJECT);
        this.add(triggerB2dBodyCmp);
    }

    private void b2dLightComponent() {
        b2dLightCmp = new B2dLightComponent(screen);
        createLight();
        this.add(b2dLightCmp);
    }

    private void stateComponent() {
        stateCmp = ecsEngine.createComponent(StateComponent.class);
        stateCmp.set(StateComponent.STATE_CHEST_NORMAL);
        this.add(stateCmp);
    }

    private void typeComponent() {
        final TypeComponent typeCmp = ecsEngine.createComponent(TypeComponent.class);
        typeCmp.type = TypeComponent.SCENERY_ITEMS;
        this.add(typeCmp);
    }

    private void createAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {

        animation.animations.put(StateComponent.STATE_CHEST_NORMAL, new Animation(0, textureAtlas.findRegion("chest")));
        animation.animations.put(StateComponent.STATE_CHEST_OPEN, new Animation(0.15f, textureAtlas.findRegions("chest")));
        animation.animations.put(StateComponent.STATE_CHEST_CLOSE, new Animation(0.15f, textureAtlas.findRegions("chest"), Animation.PlayMode.REVERSED));
    }


    private void createLight() {

        b2dLightCmp.distance = 1f;

        b2dLightCmp.light = LightBuilder.pointLight(screen.getRayHandler(), b2dBodyCmp.body, Color.valueOf("#e28822"), b2dLightCmp.distance);
        b2dLightCmp.light.setSoft(true);
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
                bubbleEntity.stateCmp.set(StateComponent.NPC_BUBBLE_SHOW);
                InputManager.getInstance().addInputListener(this);
                break;
            case HIDE_BUBBLE:
                if (stateCmp.get() == StateComponent.STATE_CHEST_OPEN) {
                    stateCmp.set(StateComponent.STATE_CHEST_CLOSE);
                    changes.firePropertyChange(ChestEvent.NAME, null, ChestEvent.CHEST_CLOSED);
                }
                if (bubbleEntity.stateCmp.get() == StateComponent.NPC_BUBBLE_SHOW)
                    bubbleEntity.stateCmp.set(StateComponent.NPC_BUBBLE_HIDE);

                InputManager.getInstance().removeInputListener(this);
                break;
        }
    }

    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key) {
            case SELECT:
                stateCmp.set(StateComponent.STATE_CHEST_OPEN);
                bubbleEntity.stateCmp.set(StateComponent.NPC_BUBBLE_HIDE);
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
