package com.nonameyet.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.nonameyet.ecs.components.TextureComponent;
import com.nonameyet.ecs.components.TransformComponent;
import com.nonameyet.ecs.entities.humans.ArmEntity;

import static com.nonameyet.ecs.ComponentMappers.textureCmpMapper;
import static com.nonameyet.ecs.ComponentMappers.transformCmpMapper;
import static com.nonameyet.utils.Constants.PPM;
import static com.nonameyet.utils.Constants.PPM_MOVABLE_ITEMS;

public class SocketPositionMapper {

    public enum TextureFlip {
        LEFT, RIGHT
    }

    private static TextureFlip textureFlip = TextureFlip.RIGHT;

    public static void resetFlip() {
        textureFlip = TextureFlip.RIGHT;
    }


    public static void transformSocketItem(Entity itemEntity, ArmEntity armEntity) {
        TextureComponent armTextureCmp = textureCmpMapper.get(armEntity);
        TransformComponent armTransformCmp = transformCmpMapper.get(armEntity);

        TransformComponent transformCmp = transformCmpMapper.get(itemEntity);
        TextureComponent textureCmp = textureCmpMapper.get(itemEntity);

        transformCmp.position.x = armTransformCmp.position.x - (textureCmp.region.getRegionWidth() / 2 / PPM_MOVABLE_ITEMS);
        transformCmp.position.y = armTransformCmp.position.y - (textureCmp.region.getRegionHeight() / 2 / PPM_MOVABLE_ITEMS);

        switch (((TextureAtlas.AtlasRegion) armTextureCmp.region).name) {
            case "player_idle_up":
                transformCmp.position.x -= (6f / PPM);
                transformCmp.position.y -= (1.5f / PPM);
                transformCmp.rotation = 0;
                calculateFlip(textureCmp, transformCmp, TextureFlip.LEFT);
                break;
            case "player_idle_down":
                transformCmp.position.x -= (6.8f / PPM);
                transformCmp.position.y -= (1.5f / PPM);
                transformCmp.rotation = 0;
                calculateFlip(textureCmp, transformCmp, TextureFlip.RIGHT);
                break;
            case "player_idle_left":
                transformCmp.position.x += (6f / PPM);
                transformCmp.position.y -= (1.5f / PPM);
                transformCmp.rotation = 0;
                calculateFlip(textureCmp, transformCmp, TextureFlip.LEFT);
                break;
            case "player_idle_right":
                transformCmp.position.x -= (6.2f / PPM);
                transformCmp.position.y -= (1.5f / PPM);
                transformCmp.rotation = 0;
                calculateFlip(textureCmp, transformCmp, TextureFlip.RIGHT);
                break;

            case "player_run_up":
            case "player_run_right":
                int index = ((TextureAtlas.AtlasRegion) armTextureCmp.region).index;
                switch (index) {
                    case 0:
                        transformCmp.position.x -= (6.2f / PPM);
                        transformCmp.position.y -= (1f / PPM);
                        transformCmp.rotation = 0;
                        break;
                    case 1:
                    case 5:
                        transformCmp.position.x -= (5.5f / PPM);
                        transformCmp.position.y -= (2f / PPM);
                        transformCmp.rotation = 15;
                        break;
                    case 2:
                    case 4:
                        transformCmp.position.x -= (5.5f / PPM);
                        transformCmp.position.y -= (1.5f / PPM);
                        transformCmp.rotation = 30;
                        break;
                    case 3:
                        transformCmp.position.x += (1f / PPM);
                        transformCmp.position.y += (6.5f / PPM);
                        transformCmp.rotation = 45;
                        break;
                }
                calculateFlip(textureCmp, transformCmp, TextureFlip.RIGHT);

                break;
            case "player_run_down":
            case "player_run_left":

                int index2 = ((TextureAtlas.AtlasRegion) armTextureCmp.region).index;
                switch (index2) {
                    case 0:
                        transformCmp.position.x += (6f / PPM);
                        transformCmp.position.y -= (1f / PPM);
                        transformCmp.rotation = 0;
                        break;
                    case 1:
                    case 5:
                        transformCmp.position.x += (5f / PPM);
                        transformCmp.position.y -= (2f / PPM);
                        transformCmp.rotation = -15;
                        break;
                    case 2:
                    case 4:
                        transformCmp.position.x += (4.5f / PPM);
                        transformCmp.position.y -= (1f / PPM);
                        transformCmp.rotation = -30;
                        break;
                    case 3:
                        transformCmp.position.x -= (1f / PPM);
                        transformCmp.position.y += (6f / PPM);
                        transformCmp.rotation = -45;
                        break;
                }
                calculateFlip(textureCmp, transformCmp, TextureFlip.LEFT);
                break;
            default:
                break;
        }

    }

    private static void calculateFlip(TextureComponent textureCmp, TransformComponent transformCmp, TextureFlip currentState) {

        switch (currentState) {
            case LEFT:
                if (!textureFlip.equals(currentState)) textureCmp.region.flip(true, false);
                textureFlip = TextureFlip.LEFT;
                break;
            case RIGHT:
                if (!textureFlip.equals(currentState)) textureCmp.region.flip(true, false);
                textureFlip = TextureFlip.RIGHT;
                transformCmp.position.x += (textureCmp.region.getRegionWidth() / PPM_MOVABLE_ITEMS);
                break;
        }
    }
}