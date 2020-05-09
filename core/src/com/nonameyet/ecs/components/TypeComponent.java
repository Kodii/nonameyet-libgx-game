package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/*
 * Stores the type of entity this is
 */
public class TypeComponent implements Component, Pool.Poolable {

    public static final int OTHER = 0;

    public static final int PLAYER = 100;
    public static final int ARM_PLAYER = 101;

    public static final int ITEM = 150;
    public static final int SOCKET_ITEM = 151;


    public static final int SCENERY_ITEMS = 180;
    public static final int TORCH = 181;

    public static final int SCENERY = 200;
    public static final int PORTAL = 210;

    public int type = OTHER;

    @Override
    public void reset() {
        int type = OTHER;
    }
}
