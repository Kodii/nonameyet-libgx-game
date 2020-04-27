package com.nonameyet.b2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.audio.AudioManager;
import com.nonameyet.maps.MapFactory;
import com.nonameyet.maps.MapManager;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.chest.ChestWindowEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class B2dContactListener implements Disposable, ContactListener {
    private final String TAG = this.getClass().getSimpleName();

    private final MapManager mapMgr;

    // events
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public B2dContactListener(GameScreen screen) {
        this.mapMgr = screen.getMapMgr();

        addPropertyChangeListener(AudioManager.getInstance());
    }

    @Override
    public void beginContact(Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        Gdx.app.debug("CONTACT", "BEGIN: " + fixtureA.getBody().getUserData() + " & " + fixtureB.getBody().getUserData());

        beginPortalContact(fixtureA, fixtureB);
        beginChestContact(fixtureA, fixtureB);

    }

    private void beginPortalContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getBody().getUserData().equals("PORTAL") || fixtureB.getBody().getUserData().equals("PORTAL")) {

            if (mapMgr.getCurrentMapType() == MapFactory.MapType.TOWN)
                mapMgr.setCurrentMapType(MapFactory.MapType.TOP_WORLD);
            else mapMgr.setCurrentMapType(MapFactory.MapType.TOWN);

            mapMgr.setMapChanged(true);
        }
    }

    private void beginChestContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getBody().getUserData().equals("CHEST") || fixtureB.getBody().getUserData().equals("CHEST")) {
            changes.firePropertyChange(ChestWindowEvent.class.getSimpleName(), null, ChestWindowEvent.CHEST_OPENED);
        }
    }

    @Override
    public void endContact(Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        Gdx.app.debug("CONTACT", "END: " + fixtureA.getBody().getUserData() + " & " + fixtureB.getBody().getUserData());

        endChestContact(fixtureA, fixtureB);
    }

    private void endChestContact(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getBody().getUserData().equals("CHEST") || fixtureB.getBody().getUserData().equals("CHEST")) {
            changes.firePropertyChange(ChestWindowEvent.class.getSimpleName(), null, ChestWindowEvent.CHEST_CLOSED);
        }
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


    @Override
    public void dispose() {
        removePropertyChangeListener(AudioManager.getInstance());
    }
}