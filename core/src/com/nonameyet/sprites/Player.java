package com.nonameyet.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.nonameyet.environment.AssetName;
import com.nonameyet.environment.Assets;

import static com.nonameyet.utils.Constants.*;

public class Player extends Sprite {
    public World world;
    public Body b2body;

    private TextureRegion playerStand;

    public Player(World world, Assets assets) {
        super((Texture) assets.manager.get(AssetName.PLAYER_PNG.getAssetName()));
        this.world = world;
        definePlayer();
        playerStand = new TextureRegion(getTexture(), 0, 0, 64, 64);
        setBounds(0, 0, 32 / PPM, 32 / PPM);
        setRegion(playerStand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void definePlayer() {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        // create player
        bodyDef.position.set(32 / PPM, 32 / PPM);
//        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);
        b2body.setUserData("PLAYER");

        fixtureDef.density = 1;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.3f, 0.3f);
        fixtureDef.shape = pShape;
        b2body.createFixture(fixtureDef);
        pShape.dispose();
    }
}
