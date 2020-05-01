package com.nonameyet.ecs;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.ecs.entities.*;
import com.nonameyet.ecs.systems.AnimationSystem;
import com.nonameyet.ecs.systems.LightSystem;
import com.nonameyet.ecs.systems.PlayerCameraSystem;
import com.nonameyet.ecs.systems.PlayerControlSystem;
import com.nonameyet.screens.GameScreen;

public class ECSEngine extends PooledEngine {
    private final GameScreen screen;

    public ECSEngine(final GameScreen screen) {
        super();
        this.screen = screen;

        this.addSystem(new AnimationSystem());
        this.addSystem(new LightSystem());
        this.addSystem(new PlayerControlSystem());
        this.addSystem(new PlayerCameraSystem(screen));
    }

    public GameScreen getScreen() {
        return screen;
    }

    public void createPlayer(Vector2 position) {
        new PlayerEntity(this, position);
    }

    public void createChest(Vector2 position) {
        new ChestEntity(this, position);
    }

    public void createTorch(Vector2 position) {
        new TorchEntity(this, position);
    }

    public void createElder(Vector2 position) {
        new ElderEntity(this, position);
    }

    public void createBlacksmith(Vector2 position) {
        new BlacksmithEntity(this, position);
    }

    public void createAnvil(Vector2 position) {
        new AnvilEntity(this, position);
    }

    public void createOwen(Vector2 position) {
        new OwenEntity(this, position);
    }
}
