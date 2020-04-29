package com.nonameyet.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.*;
import com.nonameyet.utils.Collision;

import static com.nonameyet.utils.Constants.PPM;

public class PlayerEntity extends Entity {

    public PlayerEntity(final ECSEngine ecsEngine, final Vector2 playerSpawnLocation) {
        // Create the Entity and all the components that will go in the entity
        final Entity playerEntity = ecsEngine.createEntity();

        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final AnimationComponent animation = ecsEngine.createComponent(AnimationComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        final BodyComponent b2dbody = ecsEngine.createComponent(BodyComponent.class);
        final PlayerComponent player = ecsEngine.createComponent(PlayerComponent.class);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        final PlayerStateComponent state = ecsEngine.createComponent(PlayerStateComponent.class);

        // create the data for the components and add them to the components
        position.position.set(playerSpawnLocation.x / PPM, playerSpawnLocation.y / PPM, 0);

        Texture textureAsset = Assets.manager.get(AssetName.PLAYER_PNG.getAssetName());

        createRunAnimation(animation, textureAsset);

        texture.region = createStand((Texture) Assets.manager.get(AssetName.PLAYER_PNG.getAssetName()));

        b2dbody.body = BodyBuilder.dynamicRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(playerSpawnLocation.x / PPM, playerSpawnLocation.y / PPM),
                new Vector2(16, 16),
                "PLAYER",
                Collision.PLAYER
        );
        player.speed = 3;
        type.type = TypeComponent.PLAYER;


        playerEntity.add(position);
        playerEntity.add(animation);
        playerEntity.add(texture);
        playerEntity.add(b2dbody);
        playerEntity.add(player);
        playerEntity.add(type);
        playerEntity.add(state);

        ecsEngine.addEntity(playerEntity);
    }

    private TextureRegion createStand(Texture texture) {
        int frameWidth = texture.getWidth() / 4;
        int frameHeight = texture.getHeight() / 4;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        return new TextureRegion(texture, temp[0][0].getRegionX(), temp[0][0].getRegionY(), frameWidth, frameHeight);
    }

    private void createRunAnimation(AnimationComponent animation, Texture textureAsset) {
        int frameWidth = textureAsset.getWidth() / 4;
        int frameHeight = textureAsset.getHeight() / 4;
        float frameDuration = 0.25f;

        TextureRegion[][] temp = TextureRegion.split(textureAsset, frameWidth, frameHeight);

        animation.animations.put(PlayerStateComponent.STATE_STANDING_DOWN, new Animation(0, temp[0][0]));
        animation.animations.put(PlayerStateComponent.STATE_STANDING_LEFT, new Animation(0, temp[1][0]));
        animation.animations.put(PlayerStateComponent.STATE_STANDING_RIGHT, new Animation(0, temp[2][0]));
        animation.animations.put(PlayerStateComponent.STATE_STANDING_UP, new Animation(0, temp[3][0]));

        Array<TextureRegion> frames = new Array<>();
        for (int c = 1; c < 4; c++)
            frames.add(temp[0][c]);
        animation.animations.put(PlayerStateComponent.STATE_RUNNING_DOWN, new Animation(frameDuration, frames, Animation.PlayMode.LOOP_PINGPONG));
        frames.clear();

        for (int c = 1; c < 4; c++)
            frames.add(temp[1][c]);
        animation.animations.put(PlayerStateComponent.STATE_RUNNING_LEFT, new Animation(frameDuration, frames, Animation.PlayMode.LOOP_PINGPONG));
        frames.clear();

        for (int c = 1; c < 4; c++)
            frames.add(temp[2][c]);
        animation.animations.put(PlayerStateComponent.STATE_RUNNING_RIGHT, new Animation(frameDuration, frames, Animation.PlayMode.LOOP_PINGPONG));
        frames.clear();

        for (int c = 1; c < 4; c++)
            frames.add(temp[3][c]);
        animation.animations.put(PlayerStateComponent.STATE_RUNNING_UP, new Animation(frameDuration, frames, Animation.PlayMode.LOOP_PINGPONG));
        frames.clear();
    }
}
