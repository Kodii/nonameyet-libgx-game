package com.nonameyet.utils;

/**
 * Collision category bits and collision mask bits.
 */
public class Constants {

    public static final int V_WIDTH = 1280;
    public static final int V_HEIGHT = 720;

    // fixed timestep
    public static final float FIXED_TIME_STEP = 1 / 60f;

    // collision bits
    public static final short BIT_PLAYER = 1 << 0;
    public static final short BIT_GROUND = 1 << 2;

    // 1 unit is 32 pixels
//    public static final float UNIT_SCALE = 1 / 32f;
    public static final float PPM = 32;

}
