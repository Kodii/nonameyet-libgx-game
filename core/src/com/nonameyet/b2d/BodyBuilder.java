package com.nonameyet.b2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.nonameyet.utils.Constants.PPM;

public class BodyBuilder {


    public static Body dynamicFootRectangleBody(World world, Vector2 position, Vector2 size, String userData, short categoryBits) {

        return body(world,
                new Vector2(position.x, position.y),
                new Vector2(size.x, (size.y / 8)),
                BodyDef.BodyType.DynamicBody,
                userData,
                categoryBits);
    }

    public static Body staticFootRectangleBody(World world, Vector2 position, Vector2 size, String userData, short categoryBits) {

        return body(world,
                new Vector2(position.x, position.y),
                new Vector2(size.x, (size.y / 8)),
                BodyDef.BodyType.KinematicBody,
                userData,
                categoryBits);
    }


    public static Body staticPointBody(World world, Vector2 position, Vector2 size, String userData, short categoryBits) {

        return body(
                world,
                new Vector2(position.x, position.y + (size.y / PPM / 2)),
                size,
                BodyDef.BodyType.StaticBody,
                userData,
                categoryBits);
    }

    public static void staticRectangleBody(World world, Vector2 position, Vector2 size, String userData, short categoryBits) {

        body(
                world,
                position,
                size,
                BodyDef.BodyType.StaticBody,
                userData,
                categoryBits);

    }

    private static Body body(World world, Vector2 position, Vector2 size, BodyDef.BodyType bodyType, String userData, short categoryBits) {
        float x = size.x / PPM;
        float y = size.y / PPM;

        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x, position.y);

        bdef.type = bodyType;
        Body body = world.createBody(bdef);
        body.setUserData(userData);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(x / 2, y / 2);

        fdef.filter.categoryBits = categoryBits;

        fdef.shape = shape;
        body.createFixture(fdef);

        shape.dispose();

        return body;
    }

}
