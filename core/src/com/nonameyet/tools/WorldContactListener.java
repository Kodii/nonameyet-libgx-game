package com.nonameyet.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.nonameyet.maps.MapFactory;
import com.nonameyet.maps.MapManager;
import com.nonameyet.screens.GameScreen;

public class WorldContactListener implements ContactListener {

    private MapManager _mapMgr;

    public WorldContactListener(GameScreen screen) {
        this._mapMgr = screen.getMapMgr();
    }

    @Override
    public void beginContact(Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        Gdx.app.debug("CONTACT", "BEGIN: " + fixtureA.getBody().getUserData() + " " + fixtureA.isSensor());
        Gdx.app.debug("CONTACT", "BEGIN: " + fixtureB.getBody().getUserData() + " " + fixtureB.isSensor());

        if (fixtureA.getBody().getUserData().equals("PORTAL") || fixtureB.getBody().getUserData().equals("PORTAL")) {
            Gdx.app.debug("TRIGGER", "BEGIN: " + fixtureA.getBody().getUserData() + " " + fixtureA.isSensor());

            if (_mapMgr.getCurrentMapType() == MapFactory.MapType.TOWN)
                _mapMgr.setCurrentMapType(MapFactory.MapType.TOP_WORLD);
            else _mapMgr.setCurrentMapType(MapFactory.MapType.TOWN);

            _mapMgr.setMapChanged(true);
        }

    }

    @Override
    public void endContact(Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        Gdx.app.debug("CONTACT", "END: " + fixtureA.getBody().getUserData() + " " + fixtureA.isSensor());
        Gdx.app.debug("CONTACT", "END: " + fixtureB.getBody().getUserData() + " " + fixtureB.isSensor());
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

}
