package com.nonameyet.lights;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

import static com.nonameyet.utils.Constants.PPM;

public class LightBuilder {

    public static PointLight pointLight(RayHandler rayHandler, Body body, Color color, float distance) {
        PointLight light = new PointLight(rayHandler, 120, color, distance, 0, 0);
        light.setSoftnessLength(0f);
        light.attachToBody(body);
        light.setXray(false);
        return light;
    }

    public static PointLight pointLight(RayHandler rayHandler, float x, float y, Color color, float distance) {
        PointLight light = new PointLight(rayHandler, 120, color, distance, x / PPM, y / PPM);
        light.setSoftnessLength(0f);
        light.setXray(false);
        return light;
    }

    public static ConeLight coneLight(RayHandler rayHandler, Body body, Color color, float distance, float dir, float cone) {
        ConeLight light = new ConeLight(rayHandler, 120, color, distance, 0, 0, dir, cone);
        light.setSoftnessLength(0f);
        light.attachToBody(body);
        light.setXray(false);
        return light;
    }

    public static ConeLight coneLight(RayHandler rayHandler, float x, float y, Color color, float distance, float dir, float cone) {
        ConeLight light = new ConeLight(rayHandler, 120, color, distance, x / PPM, y / PPM, dir, cone);
        light.setSoftnessLength(0f);
        light.setXray(false);
        return light;
    }
}
