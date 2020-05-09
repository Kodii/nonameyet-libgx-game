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
import com.nonameyet.events.ElderEvent;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ElderEntity extends Entity implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;

    private final B2dBodyComponent b2dbody;
    private final B2dLightComponent b2dlight;

    private final BubbleEntity bubbleEntity;

    public ElderEntity(ECSEngine ecsEngine, Vector2 spawnLocation) {
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

        TextureAtlas textureAtlas = Assets.manager.get(AssetName.ELDER_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("elder");


        createRunAnimation(animation, textureAtlas);

        texture.region = new TextureRegion(textureRegion, 0, 0, 26, 40);

        b2dbody.body = BodyBuilder.npcFootRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(20, 36),
                "ELDER_BODY",
                Collision.NPC);

        triggerb2dbody.trigger = BodyBuilder.triggerCircleBody(
                ecsEngine.getScreen().getWorld(),
                texture.region.getRegionHeight(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                50,
                "ELDER",
                Collision.NPC);

        type.type = TypeComponent.SCENERY_ITEMS;
        state.set(StateComponent.STATE_ELDER);

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "Elder --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(ElderEvent.NAME)) {
            showDialogMark((ElderEvent) evt.getNewValue());
        }
    }

    private void showDialogMark(ElderEvent event) {
        switch (event) {
            case SHOW_BUBBLE:
                bubbleEntity.state.set(StateComponent.NPC_BUBBLE_SHOW);
                break;
            case HIDE_BUBBLE:
                bubbleEntity.state.set(StateComponent.NPC_BUBBLE_HIDE);
                break;
            default:
                break;
        }
    }

    @Override
    public void dispose() {
        // listeners
        screen.getMapMgr().getCollisionSystem().removePropertyChangeListener(this);
    }

}
