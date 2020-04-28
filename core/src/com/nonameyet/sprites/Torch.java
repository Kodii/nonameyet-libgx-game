package com.nonameyet.sprites;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.b2d.LightBuilder;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.clock.DayTimeEvent;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.nonameyet.utils.Constants.PPM;

public class Torch extends Sprite implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();
    private final GameScreen screen;

    public enum State {
        TORCH_ON,
        TORCH_OFF
    }

    private State currentState = State.TORCH_OFF;
    private State previousState = State.TORCH_OFF;

    Body b2body;

    private final TextureRegion torchOff;
    private Animation<TextureRegion> torchOn;
    private float stateTime = 0;

    private PointLight torchLight;
    boolean forward = true;
    float timer = 0;

    private static TextureAtlas textureAtlas = Assets.manager.get(AssetName.TORCH_ATLAS.getAssetName());

    public Torch(GameScreen screen, Vector2 position) {
        super(textureAtlas.findRegion("torch"));
        this.screen = screen;

        torchOff = new TextureRegion(getTexture(), 0, 0, 6, 15);
        setBounds(0, 0, 6 / PPM, 15 / PPM);
        setRegion(torchOff);
        createAnimation();

        b2body = BodyBuilder.staticPointBody(
                screen.getWorld(),
                new Vector2(position.x, position.y),
                new Vector2(torchOff.getRegionWidth(), torchOff.getRegionHeight()),
                "TORCH",
                Collision.LIGHT);

        createLight(screen);

        screen.getPlayerHUD().getClockUI().addPropertyChangeListener(this);

        Gdx.app.debug(TAG, "Torch was created");
    }

    private void createLight(GameScreen screen) {
        float random = (float) (1.82f + Math.random() * (2.18f - 1.82f));

        torchLight = LightBuilder.pointLight(screen.getRayHandler(), b2body, Color.valueOf("#e28822"), random);
        torchLight.setContactFilter(Collision.lightsFilter());
        torchLight.setSoft(true);
    }

    private void createAnimation() {
        Array<TextureAtlas.AtlasRegion> torch = textureAtlas.findRegions("torch");
        torch.removeIndex(0);
        torchOn = new Animation<TextureRegion>(0.1f, torch, Animation.PlayMode.LOOP);
    }

    public void update(float dt) {

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        torchBlinking(dt);
    }

    private void torchBlinking(float dt) {
        if (torchLight.getDistance() >= 2.1f) {
            forward = false;
        } else if (torchLight.getDistance() <= 1.9f)
            forward = true;

        // change torch distance every 1/5 sec
        timer += dt;
        if (timer >= .2f) {
            if (forward)
                torchLight.setDistance(torchLight.getDistance() + 0.1f);
            else torchLight.setDistance(torchLight.getDistance() - 0.1f);
            timer = 0f;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "Torch --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(DayTimeEvent.class.getSimpleName())) {
            lightsEvent((DayTimeEvent) evt.getNewValue());
        }
    }

    private void lightsEvent(DayTimeEvent event) {
        switch (event) {
            case DAWN:
            case AFTERNOON:
                currentState = State.TORCH_OFF;
                torchLight.setActive(false);
                break;

            case DUSK:
            case NIGHT:
                currentState = State.TORCH_ON;
                torchLight.setActive(true);
                break;
        }
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;

        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;

        switch (currentState) {
            case TORCH_ON:
                region = torchOn.getKeyFrame(stateTime);
                break;
            case TORCH_OFF:
            default:
                region = torchOff;
                break;
        }

        return region;
    }

    @Override
    public void dispose() {
        screen.getPlayerHUD().getClockUI().removePropertyChangeListener(this);
    }
}
