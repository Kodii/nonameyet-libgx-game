package com.nonameyet.tools;

import com.badlogic.gdx.physics.box2d.*;

import static com.nonameyet.utils.Constants.BIT_GROUND;

public class B2WorldCreator {

    public B2WorldCreator(World world) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();


        // create a room
        bodyDef.position.set(0, 0);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        final Body room = world.createBody(bodyDef);
        room.setUserData("GROUND");

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        fixtureDef.filter.maskBits = -1;
        final ChainShape cShape = new ChainShape();
        cShape.createLoop(new float[]{1, 1, 1, 8, 14, 8, 14, 1});
        fixtureDef.shape = cShape;
        room.createFixture(fixtureDef);
        cShape.dispose();

    }
}
