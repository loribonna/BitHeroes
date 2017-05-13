package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.my.game.screens.PlayScreen;

/**
 * Created by lorib on 13/05/2017.
 */

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
       // Gdx.app.log("BeginContact","");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Gdx.app.log("Player State", PlayScreen.current.getPlayerState().toString());

        if(PlayScreen.current.getPlayerState() == EntityInterface.State.JUMP) {
            if (fixA.getUserData() == "good_head" || fixB.getUserData() == "good_head") {
                Fixture entity;
                Fixture obj;
                if (fixA.getUserData() == "good_head") {
                    entity = fixA;
                    obj = fixB;
                } else {
                    entity = fixB;
                    obj = fixA;
                }

                if (obj.getUserData() instanceof TileObject) {
                    ((TileObject) obj.getUserData()).onHit();
                }

            }
        }

        if (fixA.getUserData() == "good_feet" || fixB.getUserData() == "good_feet") {
            Fixture entity;
            Fixture obj;
            if (fixA.getUserData() == "good_feet") {
                entity = fixA;
                obj = fixB;
            } else {
                entity = fixB;
                obj = fixA;
            }

            if (obj.getUserData() == "bad_body") {
                Gdx.app.log("bad_body", "");

                PlayScreen.current.playerJump();
            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        //Gdx.app.log("EndContact","");

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
