package com.nonameyet.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.GameScreen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.nonameyet.utils.Constants.PPM;

public class Torch extends Sprite implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();
    private GameScreen screen;

    public enum State {
        TORCH_ON,
        TORCH_OFF
    }

    private State currentState = State.TORCH_OFF;
    private State previousState = State.TORCH_OFF;

    private World world;
    public Body b2body;
    Vector2 position;

    private TextureRegion torchOff;
    private Animation<TextureRegion> torchOn;
    private float stateTime = 0;

    private static TextureAtlas textureAtlas = Assets.manager.get(AssetName.TORCH_ATLAS.getAssetName());

    public Torch(GameScreen screen, Vector2 position) {
        super(textureAtlas.findRegion("torch"));
        this.screen = screen;
        this.world = screen.getWorld();
        this.position = position;

        torchOff = new TextureRegion(getTexture(), 0, 0, 6, 15);
        setBounds(0, 0, 6 / PPM, 15 / PPM);
        setRegion(torchOff);
        createAnimation();
        defineTorch();

        // listeners
        screen.getMapMgr().addPropertyChangeListener(this);

        Gdx.app.debug(TAG, "Torch was created");
    }

    private void createAnimation() {
        Array<TextureAtlas.AtlasRegion> torch = textureAtlas.findRegions("torch");
        torch.removeIndex(0);
        torchOn = new Animation<TextureRegion>(0.2f, torch, Animation.PlayMode.LOOP);
    }

    public void update(float dt) {

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "Torch --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(LightsEvent.class.getSimpleName())) {
            lightsEvent((LightsEvent) evt.getNewValue());
        }
    }

    private void lightsEvent(LightsEvent event) {
        switch (event) {
            case ON:
                currentState = State.TORCH_ON;
                break;
            case OFF:
                currentState = State.TORCH_OFF;
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

    private void defineTorch() {
        float x = torchOff.getRegionWidth() / PPM;
        float y = torchOff.getRegionHeight() / PPM;

        BodyDef bdef = new BodyDef();
        bdef.position.set(position.x, position.y + (y / 2));

        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        b2body.setUserData("TORCH");

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(x / 2, y / 2);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        shape.dispose();
    }


    @Override
    public void dispose() {
        screen.getMapMgr().removePropertyChangeListener(this);
    }

}
