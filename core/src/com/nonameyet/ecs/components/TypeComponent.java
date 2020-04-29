package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/*
 * Stores the type of entity this is
 */
public class TypeComponent implements Component, Pool.Poolable {

    public static final int PLAYER = 0;
    public static final int ENEMY = 1;
    public static final int SCENERY = 3;
    public static final int OTHER = 4;

    public int type = OTHER;

    @Override
    public void reset() {
        int type = OTHER;
    }
}
