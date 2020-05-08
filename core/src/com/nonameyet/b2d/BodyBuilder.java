package com.nonameyet.b2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.nonameyet.utils.Constants.PPM;
import static com.nonameyet.utils.Constants.PPM_MOVABLE_ITEMS;

public class BodyBuilder {

    public static Body playerFootBody(World world, Vector2 position, Vector2 size, String userData, short categoryBits) {

        return body(world,
                new Vector2(position.x, position.y),
                new Vector2(size.x, (size.y / 6)),
                BodyDef.BodyType.DynamicBody,
                userData,
                categoryBits);
    }

    public static Body npcFootRectangleBody(World world, Vector2 position, Vector2 size, String userData, short categoryBits) {

        return body(world,
                new Vector2(position.x, position.y),
                new Vector2(size.x, (size.y / 6)),
                BodyDef.BodyType.StaticBody,
                userData,
                categoryBits);
    }

    public static Body triggerBody(World world, int regionHeight, Vector2 position, float radius, String userData, short categoryBits) {

        return trigger(world,
                regionHeight,
                new Vector2(position.x, position.y),
                radius,
                BodyDef.BodyType.StaticBody,
                userData,
                categoryBits);
    }


    public static Body triggerItemBody(World world, int regionHeight, Vector2 position, float radius, String userData, short categoryBits) {

        return triggerItem(world,
                regionHeight,
                new Vector2(position.x, position.y),
                radius,
                BodyDef.BodyType.StaticBody,
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

    public static Body staticRectangleBody(World world, Vector2 position, Vector2 size, String userData, short categoryBits) {

        return body(
                world,
                position,
                size,
                BodyDef.BodyType.StaticBody,
                userData,
                categoryBits);

    }

    private static Body body(World world, Vector2 position, Vector2 size, BodyDef.BodyType bodyType, String userData, short categoryBits) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x, position.y);

        bdef.type = bodyType;
        Body body = world.createBody(bdef);
        body.setUserData(userData);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / PPM / 2, size.y / PPM / 2);

        fdef.filter.categoryBits = categoryBits;

        fdef.shape = shape;
        body.createFixture(fdef);

        shape.dispose();

        return body;
    }

    private static Body trigger(World world, int regionHeight, Vector2 position, float radius, BodyDef.BodyType bodyType, String userData, short categoryBits) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x, position.y + (regionHeight / PPM / 2));

        bdef.type = bodyType;
        Body body = world.createBody(bdef);
        body.setUserData(userData);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        fdef.filter.categoryBits = categoryBits;

        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef);

        shape.dispose();

        return body;
    }

    private static Body triggerItem(World world, int regionHeight, Vector2 position, float radius, BodyDef.BodyType bodyType, String userData, short categoryBits) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x, position.y + (regionHeight / PPM / 2));

        bdef.type = bodyType;
        Body body = world.createBody(bdef);
        body.setUserData(userData);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM_MOVABLE_ITEMS);

        fdef.filter.categoryBits = categoryBits;

        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef);

        shape.dispose();

        return body;
    }
}
