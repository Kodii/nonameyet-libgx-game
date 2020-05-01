package com.nonameyet.utils;

import com.badlogic.gdx.physics.box2d.Filter;

public class Collision {

    // categories
    public final static short OBSTACLE = 1; // 1 - everything without a mask
    public final static short PLAYER = 1 << 1;
    public final static short OBJECT = 1 << 3;
    public final static short LIGHT = 1 << 4;
    public final static short NPC = 1 << 5;
//    public final static short SENSOR = 1 << 5;
//    public final static short BODY_PART = 1 << 6;

    public final static short LIGHT_GROUP = 1;

    // masks
//    public final static short MASK_LIGHTS = PLAYER | OBJECT;
    public final static short MASK_LIGHTS = 0;
    public final static short MASK_PLAYER = OBSTACLE;


    // light setup
    public static Filter lightsFilter() {
        Filter filter = new Filter();
        filter.categoryBits = Collision.LIGHT;
        filter.groupIndex = Collision.LIGHT_GROUP;
        filter.maskBits = Collision.MASK_LIGHTS;
        return filter;
    }
}
