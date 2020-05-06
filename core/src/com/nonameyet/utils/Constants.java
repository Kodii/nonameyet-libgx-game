package com.nonameyet.utils;

/**
 * Collision category bits and collision mask bits.
 */
public class Constants {

    // fixed timestep
    public static final float FIXED_TIME_STEP = 1 / 60f;

    // collision bits
    public static final short BIT_PLAYER = 2;
    public static final short BIT_GROUND = 4;

    // 1 unit is 32 pixels
    public static final float PPM = 30;

    // number of pixels in aseprite to fill camera hud
    public static final float CAMERA_PIXELS_WIDTH = 512;
    public static final float CAMERA_PIXELS_HEIGHT = 288;
}
