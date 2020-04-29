package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;

import java.util.Comparator;

import static com.nonameyet.ecs.ComponentMappers.transformCmpMapper;

public class ZComparator implements Comparator<Entity> {

    public ZComparator() {
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
        return (int) Math.signum(transformCmpMapper.get(entityB).position.z -
                transformCmpMapper.get(entityA).position.z);
    }
}
