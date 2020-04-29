package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/*
 * Stores the type of entity this is
 */
public class TypeComponent implements Component, Pool.Poolable {

    public static final int OTHER = 0;
    public static final int PLAYER = 100;
    public static final int ENEMY = 120;
    public static final int SCENERY = 200;
    public static final int CHEST = 140;
    public static final int TORCH = 160;


    public int type = OTHER;

    @Override
    public void reset() {
        int type = OTHER;
    }
}
