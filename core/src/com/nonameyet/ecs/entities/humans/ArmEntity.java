package com.nonameyet.ecs.entities.humans;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.*;
import com.nonameyet.ecs.entities.items.ItemEntity;
import com.nonameyet.input.GameKeyInputListener;
import com.nonameyet.input.GameKeys;
import com.nonameyet.input.InputManager;

public class ArmEntity extends Entity implements GameKeyInputListener {
    private final String TAG = this.getClass().getSimpleName();

    private final ECSEngine ecsEngine;

    public StateComponent stateCmp;
    public TransformComponent transformCmp;
    public SocketComponent socketCmp;

    private TextureAtlas textureAtlas;

    public ArmEntity(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        this.ecsEngine = ecsEngine;

        transformComponent(spawnLocation);
        textureComponent();
        animationComponent(textureAtlas);
        socketComponent();
        stateComponent();
        typeComponent();

        this.ecsEngine.addEntity(this);

        InputManager.getInstance().addInputListener(this);
    }

    private void transformComponent(Vector2 spawnLocation) {
        transformCmp = ecsEngine.createComponent(TransformComponent.class);
        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 0);
        this.add(transformCmp);
    }

    private void textureComponent() {
        final TextureComponent textureCmp = ecsEngine.createComponent(TextureComponent.class);
        textureAtlas = Assets.manager.get(AssetName.ARM_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("player_idle_down");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, 20, 39);
        this.add(textureCmp);
    }

    private void animationComponent(TextureAtlas textureAtlas) {
        final AnimationComponent animationCmp = ecsEngine.createComponent(AnimationComponent.class);
        createRunAnimation(animationCmp.animations, textureAtlas);
        this.add(animationCmp);
    }

    private void createRunAnimation(IntMap<Animation> animations, TextureAtlas textureAtlas) {
        float frameDuration = 0.14F;

        TextureAtlas.AtlasRegion player_idle_up = textureAtlas.findRegion("player_idle_up");
        animations.put(StateComponent.STATE_STANDING_UP, new Animation(0, player_idle_up));

        TextureAtlas.AtlasRegion player_idle_right = textureAtlas.findRegion("player_idle_right");
        animations.put(StateComponent.STATE_STANDING_RIGHT, new Animation(0, player_idle_right));

        TextureAtlas.AtlasRegion player_idle_down = textureAtlas.findRegion("player_idle_down");
        animations.put(StateComponent.STATE_STANDING_DOWN, new Animation(0, player_idle_down));

        TextureAtlas.AtlasRegion player_idle_left = textureAtlas.findRegion("player_idle_left");
        animations.put(StateComponent.STATE_STANDING_LEFT, new Animation(0, player_idle_left));

        Array<TextureAtlas.AtlasRegion> player_run_right = textureAtlas.findRegions("player_run_right");
        animations.put(StateComponent.STATE_RUNNING_UP, new Animation(frameDuration, player_run_right, Animation.PlayMode.LOOP));
        animations.put(StateComponent.STATE_RUNNING_RIGHT, new Animation(frameDuration, player_run_right, Animation.PlayMode.LOOP));

        Array<TextureAtlas.AtlasRegion> player_run_left = textureAtlas.findRegions("player_run_left");
        animations.put(StateComponent.STATE_RUNNING_DOWN, new Animation(frameDuration, player_run_left, Animation.PlayMode.LOOP));
        animations.put(StateComponent.STATE_RUNNING_LEFT, new Animation(frameDuration, player_run_left, Animation.PlayMode.LOOP));

    }

    private void socketComponent() {
        socketCmp = ecsEngine.createComponent(SocketComponent.class);
//        socketCmp.itemEntity = ItemsCreator.createSword(ecsEngine, spawnLocation);
        this.add(socketCmp);
    }

    private void stateComponent() {
        stateCmp = ecsEngine.createComponent(StateComponent.class);
        stateCmp.set(StateComponent.STATE_STANDING_DOWN);
        this.add(stateCmp);
    }

    private void typeComponent() {
        final TypeComponent typeCmp = ecsEngine.createComponent(TypeComponent.class);
        typeCmp.type = TypeComponent.ARM_PLAYER;
        this.add(typeCmp);
    }


    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key) {
            case DROP_SOCKET:
                dropSocket();
                break;
            default:
                break;
        }
    }

    private void dropSocket() {
        if (socketCmp.itemEntity != null) {
            ((ItemEntity) socketCmp.itemEntity).drop();
            socketCmp.itemEntity = null;
            Gdx.app.log(TAG, "Socket item successfully dropped");
        } else Gdx.app.log(TAG, "You don't have socket item");
    }

    @Override
    public void keyReleased(InputManager inputManager, GameKeys key) {

    }
}
