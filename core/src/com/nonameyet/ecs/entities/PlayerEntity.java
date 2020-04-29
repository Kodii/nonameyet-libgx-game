package com.nonameyet.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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
        final BodyComponent b2dbody = ecsEngine.createComponent(BodyComponent.class);
        final TransformComponent position = ecsEngine.createComponent(TransformComponent.class);
        final TextureComponent texture = ecsEngine.createComponent(TextureComponent.class);
        final PlayerComponent player = ecsEngine.createComponent(PlayerComponent.class);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);
        final PlayerStateComponent state = ecsEngine.createComponent(PlayerStateComponent.class);
//      final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);

        // create the data for the components and add them to the components
        position.position.set(playerSpawnLocation.x / PPM, playerSpawnLocation.y / PPM, 0);
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


        playerEntity.add(b2dbody);
        playerEntity.add(position);
        playerEntity.add(texture);
        playerEntity.add(player);
        playerEntity.add(type);
        playerEntity.add(state);
//        player.add(animationComponent);

        ecsEngine.addEntity(playerEntity);
    }

    private TextureRegion createStand(Texture texture) {
        int frameWidth = texture.getWidth() / 4;
        int frameHeight = texture.getHeight() / 4;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        return new TextureRegion(texture, temp[0][0].getRegionX(), temp[0][0].getRegionY(), frameWidth, frameHeight);
//        standLeft = new TextureRegion(texture, temp[1][0].getRegionX(), temp[1][0].getRegionY(), frameWidth, frameHeight);
//        standRight = new TextureRegion(texture, temp[2][0].getRegionX(), temp[2][0].getRegionY(), frameWidth, frameHeight);
//        standUp = new TextureRegion(texture, temp[3][0].getRegionX(), temp[3][0].getRegionY(), frameWidth, frameHeight);
    }
}
