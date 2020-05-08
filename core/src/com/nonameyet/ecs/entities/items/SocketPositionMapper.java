package com.nonameyet.ecs.entities.items;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.nonameyet.ecs.components.AnimationComponent;
import com.nonameyet.ecs.components.StateComponent;
import com.nonameyet.ecs.components.TextureComponent;
import com.nonameyet.ecs.components.TransformComponent;
import com.nonameyet.ecs.entities.humans.ArmEntity;

import static com.nonameyet.ecs.ComponentMappers.*;
import static com.nonameyet.utils.Constants.PPM;

public class SocketPositionMapper {


    public static void transformSocketItem(TransformComponent transformCmp, ArmEntity armEntity) {

        TextureComponent aTextureCmp = textureCmpMapper.get(armEntity);
        AnimationComponent aAnimationCmp = animationCmpMapper.get(armEntity);
        TransformComponent aTransformCmp = transformCmpMapper.get(armEntity);
        StateComponent aStateComponent = stateCmpMapper.get(armEntity);


        switch (aStateComponent.get()) {
            case StateComponent.STATE_STANDING_UP:
            case StateComponent.STATE_STANDING_DOWN:
                transformCmp.position.x = aTransformCmp.position.x - (5.8f / PPM);
                transformCmp.position.y = aTransformCmp.position.y - (5.1f / PPM);
                transformCmp.rotation = 0;
                break;
            case StateComponent.STATE_STANDING_LEFT:
                transformCmp.position.x = aTransformCmp.position.x + (5f / PPM);
                transformCmp.position.y = aTransformCmp.position.y - (5.2f / PPM);
                transformCmp.rotation = 0;
                break;
            case StateComponent.STATE_STANDING_RIGHT:
                transformCmp.position.x = aTransformCmp.position.x - (5f / PPM);
                transformCmp.position.y = aTransformCmp.position.y - (4.2f / PPM);
                transformCmp.rotation = 0;
                break;

            case StateComponent.STATE_RUNNING_UP:
            case StateComponent.STATE_RUNNING_RIGHT:
                if (((TextureAtlas.AtlasRegion) aTextureCmp.region).name.equals("player_run_right") ||
                        ((TextureAtlas.AtlasRegion) aTextureCmp.region).name.equals("player_run_up")) {

                    int index = ((TextureAtlas.AtlasRegion) aTextureCmp.region).index;
                    switch (index) {
                        case 0:
                            transformCmp.position.x = aTransformCmp.position.x - (5f / PPM);
                            transformCmp.position.y = aTransformCmp.position.y - (3.5f / PPM);
                            transformCmp.rotation = 0;
                            break;
                        case 1:
                        case 5:
                            transformCmp.position.x = aTransformCmp.position.x - (4.3f / PPM);
                            transformCmp.position.y = aTransformCmp.position.y - (5.5f / PPM);
                            transformCmp.rotation = 20;
                            break;
                        case 2:
                        case 4:
                            transformCmp.position.x = aTransformCmp.position.x - (3.3f / PPM);
                            transformCmp.position.y = aTransformCmp.position.y - (5.5f / PPM);
                            transformCmp.rotation = 30;
                            break;
                        case 3:
                            transformCmp.position.x = aTransformCmp.position.x + (4f / PPM);
                            transformCmp.position.y = aTransformCmp.position.y + (2.3f / PPM);
                            transformCmp.rotation = 45;
                            break;
                    }
                }
                break;
            case StateComponent.STATE_RUNNING_DOWN:
            case StateComponent.STATE_RUNNING_LEFT:
                if (((TextureAtlas.AtlasRegion) aTextureCmp.region).name.equals("player_run_left") ||
                        ((TextureAtlas.AtlasRegion) aTextureCmp.region).name.equals("player_run_down")) {

                    int index2 = ((TextureAtlas.AtlasRegion) aTextureCmp.region).index;
                    switch (index2) {
                        case 0:
                            transformCmp.position.x = aTransformCmp.position.x + (5f / PPM);
                            transformCmp.position.y = aTransformCmp.position.y - (3.5f / PPM);
                            transformCmp.rotation = 0;
                            break;
                        case 1:
                        case 5:
                            transformCmp.position.x = aTransformCmp.position.x + (4.3f / PPM);
                            transformCmp.position.y = aTransformCmp.position.y - (5.5f / PPM);
                            transformCmp.rotation = -20;
                            break;
                        case 2:
                        case 4:
                            transformCmp.position.x = aTransformCmp.position.x + (3.3f / PPM);
                            transformCmp.position.y = aTransformCmp.position.y - (5.5f / PPM);
                            transformCmp.rotation = -30;
                            break;
                        case 3:
                            transformCmp.position.x = aTransformCmp.position.x - (4f / PPM);
                            transformCmp.position.y = aTransformCmp.position.y + (2.3f / PPM);
                            transformCmp.rotation = -45;
                            break;
                    }
                }
                break;
            default:
                break;
        }

    }
}
