package com.nonameyet.ecs;


import com.badlogic.ashley.core.ComponentMapper;
import com.nonameyet.ecs.components.*;

public class ComponentMappers {

    public static ComponentMapper<AnimationComponent> animationCmpMapper = ComponentMapper.getFor(AnimationComponent.class);
    public static ComponentMapper<BodyComponent> bodyCmpMapper = ComponentMapper.getFor(BodyComponent.class);
    public static ComponentMapper<CollisionComponent> collisionCmpMapper = ComponentMapper.getFor(CollisionComponent.class);
    public static ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static ComponentMapper<StateComponent> stateCmpMapper = ComponentMapper.getFor(StateComponent.class);
    public static ComponentMapper<TextureComponent> textureCmpMapper = ComponentMapper.getFor(TextureComponent.class);
    public static ComponentMapper<TransformComponent> transformCmpMapper = ComponentMapper.getFor(TransformComponent.class);
    public static ComponentMapper<TypeComponent> typeCmpMapper = ComponentMapper.getFor(TypeComponent.class);

}
