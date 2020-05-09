package com.nonameyet.b2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.nonameyet.utils.Collision;

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

    public static Body triggerCircleBody(World world, int regionHeight, Vector2 position, float radius, String userData, short categoryBits) {

        return triggerCircle(world,
                regionHeight,
                new Vector2(position.x, position.y),
                radius,
                BodyDef.BodyType.StaticBody,
                userData,
                categoryBits);
    }

    private static Body triggerCircle(World world, int regionHeight, Vector2 position, float radius, BodyDef.BodyType bodyType, String userData, short categoryBits) {
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


    public static Body createWeaponBody(World world, Vector2 position, Vector2 size, String userData, short categoryBits) {

        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x, position.y);

        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        body.setUserData(userData);

        FixtureDef fdef = new FixtureDef();

        // create polygon shape
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(size.x / PPM_MOVABLE_ITEMS / 1.4f, size.y / PPM / 2);

        fdef.filter.categoryBits = categoryBits;
        fdef.filter.maskBits = Collision.NPC;

        fdef.shape = polygonShape;
        body.createFixture(fdef);
        polygonShape.dispose();

        // create circle trigger shape
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(size.y * 4 / PPM_MOVABLE_ITEMS);

        fdef.filter.categoryBits = categoryBits;
        fdef.filter.maskBits = Collision.PLAYER;

        fdef.shape = circleShape;
        fdef.isSensor = true;
        body.createFixture(fdef);
        circleShape.dispose();


        return body;
    }

    public static Body createItemBody(World world, Vector2 position, Vector2 size, String userData, short categoryBits) {

        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x, position.y);

        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        body.setUserData(userData);

        FixtureDef fdef = new FixtureDef();

        // create circle trigger shape
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(size.y * 4 / PPM_MOVABLE_ITEMS);

        fdef.filter.categoryBits = categoryBits;
        fdef.filter.maskBits = Collision.PLAYER;

        fdef.shape = circleShape;
        fdef.isSensor = true;
        body.createFixture(fdef);
        circleShape.dispose();

        return body;
    }


}
