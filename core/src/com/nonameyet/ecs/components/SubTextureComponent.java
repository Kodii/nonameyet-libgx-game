package com.nonameyet.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class SubTextureComponent implements Component, Pool.Poolable {
    public TextureRegion region = null;

    @Override
    public void reset() {
        region = null;
    }
}