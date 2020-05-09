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
    private final ECSEngine ecsEngine;

    private TextureComponent textureCmp;
    private B2dBodyComponent b2dBodyCmp;
    private B2dLightComponent b2dLightCmp;

    private final BubbleEntity bubbleEntity;

    private TextureAtlas textureAtlas;

    // events
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public BlacksmithEntity(ECSEngine ecsEngine, Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();
        this.ecsEngine = ecsEngine;

        npcComponent();
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
        screen.getMapMgr().getCollisionSystem().addPropertyChangeListener(this);
    }

    private void npcComponent() {
        final NpcComponent npcCmp = ecsEngine.createComponent(NpcComponent.class);
        this.add(npcCmp);
    }

    private void transformComponent(Vector2 spawnLocation) {
        final TransformComponent transformCmp = ecsEngine.createComponent(TransformComponent.class);
        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 1);
        this.add(transformCmp);
    }

    private void textureComponent() {
        textureCmp = ecsEngine.createComponent(TextureComponent.class);
        textureAtlas = Assets.manager.get(AssetName.BLACKSMITH_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("blacksmith");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, 26, 40);
        this.add(textureCmp);
    }

    private void animationComponent() {
        final AnimationComponent animationCmp = ecsEngine.createComponent(AnimationComponent.class);
        createAnimation(animationCmp, textureAtlas);
        this.add(animationCmp);
    }

    private void createAnimation(AnimationComponent animation, TextureAtlas textureAtlas) {
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

    private void b2dLightComponent() {
        b2dLightCmp = new B2dLightComponent(screen);
        createLight();
        this.add(b2dLightCmp);
    }

    private void createLight() {
        b2dLightCmp.distance = 1f;

        b2dLightCmp.light = LightBuilder.pointLight(screen.getRayHandler(), b2dBodyCmp.body, Color.valueOf("#e28822"), b2dLightCmp.distance);
        b2dLightCmp.light.setSoft(true);
        b2dLightCmp.light.attachToBody(b2dBodyCmp.body);
    }

    private void b2dBodyComponent(Vector2 spawnLocation) {
        b2dBodyCmp = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dBodyCmp.body = BodyBuilder.npcFootRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(26, 40),
                "BLACKSMITH_BODY",
                Collision.NPC);
        this.add(b2dBodyCmp);
    }

    private void triggerB2dBodyComponent(Vector2 spawnLocation) {
        TriggerB2dBodyComponent triggerB2dBodyCmp = ecsEngine.createComponent(TriggerB2dBodyComponent.class);
        triggerB2dBodyCmp.trigger = BodyBuilder.triggerCircleBody(
                ecsEngine.getScreen().getWorld(),
                textureCmp.region.getRegionHeight(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                50,
                this,
                Collision.NPC);
        this.add(triggerB2dBodyCmp);
    }

    private void stateComponent() {
        final StateComponent stateCmp = ecsEngine.createComponent(StateComponent.class);
        stateCmp.set(StateComponent.STATE_BLACKSMITH);
        this.add(stateCmp);
    }

    private void typeComponent() {
        final TypeComponent typeCmp = ecsEngine.createComponent(TypeComponent.class);
        typeCmp.type = TypeComponent.SCENERY_ITEMS;
        this.add(typeCmp);
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
                bubbleEntity.stateCmp.set(StateComponent.NPC_BUBBLE_SHOW);
                InputManager.getInstance().addInputListener(this);
                break;
            case HIDE_BUBBLE:
                bubbleEntity.stateCmp.set(StateComponent.NPC_BUBBLE_HIDE);
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

