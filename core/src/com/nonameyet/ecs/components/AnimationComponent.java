package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component, Pool.Poolable {
    public IntMap<Animation> animations = new IntMap<>();

//    public ArrayMap<String, Animation> animations = new ArrayMap<String, Animation>();




    @Override
    public void reset() {
        animations = new IntMap<>();
    }


}
