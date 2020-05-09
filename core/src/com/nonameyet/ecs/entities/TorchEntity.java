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
import com.nonameyet.events.DayTimeEvent;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TorchEntity extends Entity implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;
    private final ECSEngine ecsEngine;

    private AnimationComponent animationCmp;
    private B2dBodyComponent b2dBodyCmp;
    private B2dLightComponent b2dLightCmp;
    private StateComponent stateCmp;

    private TextureAtlas textureAtlas;

    public TorchEntity(ECSEngine ecsEngine, Vector2 spawnLocation) {
        this.screen = ecsEngine.getScreen();
        this.ecsEngine = ecsEngine;

        transformComponent(spawnLocation);
        textureComponent();
        animationComponent();
        b2dBodyComponent(spawnLocation);
        b2dLightComponent();
        stateComponent();
        typeComponent();

        ecsEngine.addEntity(this);

        // listeners
        screen.getPlayerHUD().getClockUI().addPropertyChangeListener(this);
    }

    private void transformComponent(Vector2 spawnLocation) {
        final TransformComponent transformCmp = ecsEngine.createComponent(TransformComponent.class);
        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 1);
        this.add(transformCmp);
    }

    private void textureComponent() {
        final TextureComponent textureCmp = ecsEngine.createComponent(TextureComponent.class);
        textureAtlas = Assets.manager.get(AssetName.TORCH_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("torch");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, 6, 15);
        this.add(textureCmp);
    }

    private void animationComponent() {
        animationCmp = ecsEngine.createComponent(AnimationComponent.class);
        createAnimation(textureAtlas);
        this.add(animationCmp);
    }

    private void createAnimation(TextureAtlas textureAtlas) {
        animationCmp.animations.put(StateComponent.STATE_TORCH_OFF, new Animation(0, textureAtlas.findRegion("torch")));
        animationCmp.animations.put(StateComponent.STATE_TORCH_ON, new Animation(0, textureAtlas.findRegion("torch")));

//        Array<TextureAtlas.AtlasRegion> torchRegions = textureAtlas.findRegions("torch");
//        torchRegions.removeIndex(0);
//        animation.animations.put(StateComponent.STATE_TORCH_ON, new Animation(0.1f, torchRegions, Animation.PlayMode.LOOP_PINGPONG));

    }

    private void b2dBodyComponent(Vector2 spawnLocation) {
        b2dBodyCmp = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dBodyCmp.body = BodyBuilder.staticPointBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(6, 15),
                this,
                Collision.LIGHT);
        this.add(b2dBodyCmp);
    }

    private void b2dLightComponent() {
        b2dLightCmp = new B2dLightComponent(screen);
        createLight();
        this.add(b2dLightCmp);
    }

    private void stateComponent() {
        stateCmp = ecsEngine.createComponent(StateComponent.class);
        stateCmp.set(StateComponent.STATE_TORCH_OFF);
        this.add(stateCmp);
    }

    private void typeComponent() {
        final TypeComponent typeCmp = ecsEngine.createComponent(TypeComponent.class);
        typeCmp.type = TypeComponent.TORCH;
        this.add(typeCmp);
    }

    private void createLight() {
        float random = (float) (2.6f + Math.random() * (2.9f - 2.3f));

        b2dLightCmp.distance = random;

        b2dLightCmp.light = LightBuilder.pointLight(screen.getRayHandler(), b2dBodyCmp.body, Color.valueOf("#e28822"), b2dLightCmp.distance);
        b2dLightCmp.light.setSoft(true);

        b2dLightCmp.callEvery = 0.15f;
        b2dLightCmp.flSpeed = 2;
        b2dLightCmp.flDistance = b2dLightCmp.light.getDistance() * 0.1f;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "Torch --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(DayTimeEvent.NAME)) {
            lightsEvent((DayTimeEvent) evt.getNewValue());
        }
    }

    private void lightsEvent(DayTimeEvent event) {
        switch (event) {
            case DAWN:
            case AFTERNOON:
                disableParticleEffect();
                stateCmp.set(StateComponent.STATE_TORCH_OFF);
                break;

            case DUSK:
            case NIGHT:
                enableParticleEffect();
                stateCmp.set(StateComponent.STATE_TORCH_ON);
                break;
        }
    }


    private void disableParticleEffect() {
        this.remove(ParticleEffectComponent.class);
    }

    private void enableParticleEffect() {
        final ParticleEffectComponent particleEffect = ecsEngine.createComponent(ParticleEffectComponent.class);
        particleEffect.effectType = ParticleEffectComponent.ParticleEffectType.TORCH;
        particleEffect.scalling = 0.04f;
        particleEffect.effectPosition.set(b2dBodyCmp.body.getPosition());
        this.add(particleEffect);
    }


    @Override
    public void dispose() {
        screen.getPlayerHUD().getClockUI().removePropertyChangeListener(this);

    }

}
