package com.nonameyet.b2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.nonameyet.ecs.components.TypeComponent;
import com.nonameyet.ecs.entities.ChestEntity;
import com.nonameyet.ecs.entities.PortalEntity;
import com.nonameyet.ecs.entities.humans.BlacksmithEntity;
import com.nonameyet.ecs.entities.humans.ElderEntity;
import com.nonameyet.ecs.entities.humans.PlayerEntity;
import com.nonameyet.events.BlacksmithEvent;
import com.nonameyet.events.ChestEvent;
import com.nonameyet.events.ElderEvent;
import com.nonameyet.maps.MapFactory;
import com.nonameyet.maps.MapManager;
import com.nonameyet.screens.GameScreen;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CollisionSystem implements ContactListener {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;
    private final MapManager mapMgr;

    // events
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public CollisionSystem(GameScreen screen) {
        this.screen = screen;
        this.mapMgr = screen.getMapMgr();
    }

    @Override
    public void beginContact(Contact contact) {
        final Fixture fa = contact.getFixtureA();
        final Fixture fb = contact.getFixtureB();

        Gdx.app.debug("CONTACT", "START " + fa.getBody().getUserData() + " has hit " + fb.getBody().getUserData());

        if (fa.getBody().getUserData() instanceof PlayerEntity)
            beginCollision(fa, fb);
        else if (fb.getBody().getUserData() instanceof PlayerEntity)
            beginCollision(fb, fa);
    }

    private void beginCollision(Fixture playerFixture, Fixture fixture) {
        Object userData = fixture.getBody().getUserData();

        if (userData instanceof PortalEntity) beginPortalContact();
        if (userData instanceof ChestEntity) beginChestContact();
        if (userData instanceof BlacksmithEntity) beginBlacksmithContact();
        if (userData instanceof ElderEntity) beginElderContact();

        if (userData instanceof Entity) {
            TypeComponent typeCmp = ((Entity) userData).getComponent(TypeComponent.class);

            if (typeCmp.type == TypeComponent.ITEM) {
                typeCmp.type = TypeComponent.SOCKET_ITEM;
                PlayerEntity playerEntity = (PlayerEntity) playerFixture.getBody().getUserData();

                if (playerEntity.armEntity.socketCmp.itemEntity != null) {
                    // drop last item
                    TypeComponent oldEntityType = playerEntity.armEntity.socketCmp.itemEntity.getComponent(TypeComponent.class);
                    oldEntityType.type = TypeComponent.ITEM;
                }

                playerEntity.armEntity.socketCmp.itemEntity = (Entity) userData;
            }
        }
    }

    private void beginPortalContact() {
        if (mapMgr.getCurrentMapType() == MapFactory.MapType.SPAWN)
            mapMgr.setCurrentMapType(MapFactory.MapType.FIRST);
        else mapMgr.setCurrentMapType(MapFactory.MapType.SPAWN);

        mapMgr.setMapChanged(true);
    }

    private void beginChestContact() {
        changes.firePropertyChange(ChestEvent.NAME, null, ChestEvent.SHOW_BUBBLE);
    }

    private void beginBlacksmithContact() {
        changes.firePropertyChange(BlacksmithEvent.NAME, null, BlacksmithEvent.SHOW_BUBBLE);
    }

    private void beginElderContact() {
        changes.firePropertyChange(ElderEvent.NAME, null, ElderEvent.SHOW_BUBBLE);
    }

    @Override
    public void endContact(Contact contact) {
        final Fixture fa = contact.getFixtureA();
        final Fixture fb = contact.getFixtureB();

        Gdx.app.debug("CONTACT", "END " + fa.getBody().getUserData() + " has hit " + fb.getBody().getUserData());

        if (fa.getBody().getUserData() instanceof PlayerEntity)
            endCollision(fb);
        else if (fb.getBody().getUserData() instanceof PlayerEntity)
            endCollision(fa);
    }

    private void endCollision(Fixture fixture) {
        Object userData = fixture.getBody().getUserData();

        if (userData instanceof ChestEntity) endChestContact();
        if (userData instanceof BlacksmithEntity) endBlacksmithContact();
        if (userData instanceof ElderEntity) endElderContact();
    }

    private void endChestContact() {
        changes.firePropertyChange(ChestEvent.NAME, null, ChestEvent.HIDE_BUBBLE);
    }

    private void endBlacksmithContact() {
        changes.firePropertyChange(BlacksmithEvent.NAME, null, BlacksmithEvent.HIDE_BUBBLE);
    }

    private void endElderContact() {
        changes.firePropertyChange(ElderEvent.NAME, null, ElderEvent.HIDE_BUBBLE);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        /* example:
        if Mario jump from below to platform than we don't want to contact,
        if Mario is above and landing to platform, we want contact */
//        contact.setEnabled(false);

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public void addPropertyChangeListener(
            PropertyChangeListener p) {
        changes.addPropertyChangeListener(p);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener p) {
        changes.removePropertyChangeListener(p);
    }
}
