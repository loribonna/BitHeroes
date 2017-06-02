package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by lorib on 13/05/2017.
 */

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixPlayer = null;
        Fixture fixObject = null;
        Fixture fixEnemy = null;
        Fixture fixBullet = null;

        if (contact.getFixtureA() != null && contact.getFixtureA().getUserData().toString().contains("good")) {
            fixPlayer = contact.getFixtureA();
        } else if (contact.getFixtureB() != null &&contact.getFixtureB().getUserData().toString().contains("good")) {
            fixPlayer = contact.getFixtureB();
        }

        if (contact.getFixtureA() != null && contact.getFixtureA().getUserData() instanceof Bullet) {
            fixBullet = contact.getFixtureA();
        } else if (contact.getFixtureB() != null &&contact.getFixtureB().getUserData()instanceof Bullet) {
            fixBullet = contact.getFixtureB();
        }

        if (contact.getFixtureA() != null && contact.getFixtureA().getUserData() instanceof Enemy) {
            fixEnemy = contact.getFixtureA();
        } else if (contact.getFixtureB() != null &&contact.getFixtureB().getUserData() instanceof Enemy) {
            fixEnemy = contact.getFixtureB();
        }

        if (contact.getFixtureA() != null && contact.getFixtureA().getUserData() instanceof TileObject) {
            fixObject = contact.getFixtureA();
        } else if (contact.getFixtureB() != null &&contact.getFixtureB().getUserData() instanceof TileObject) {
            fixObject = contact.getFixtureB();
        }

        if(fixPlayer!=null) {
            if (fixPlayer.getUserData().toString().contains("body")) {
                if (fixObject != null) {
                    ((TileObject) fixObject.getUserData()).onHit(fixPlayer.getUserData());
                }
            }

            if (PlayScreen.current.getPlayerState() == EntityInterface.State.JUMP) {
                if (fixPlayer.getUserData().toString().contains("head")) {
                    if (fixObject != null) {
                        ((TileObject) fixObject.getUserData()).onHit(fixPlayer.getUserData());
                    }
                }
            }
        }

        if (fixEnemy!=null) {
            if(fixPlayer!=null) {
                if (fixPlayer.getUserData().toString().contains("feet")) {
                    if (!((Enemy) fixEnemy.getUserData()).isInvulnerable()) {
                        PlayScreen.current.player.body.applyLinearImpulse(new Vector2(0, (-PlayScreen.current.player.body.getLinearVelocity().y) + 3), PlayScreen.current.player.body.getWorldCenter(), true);
                        ((Enemy) fixEnemy.getUserData()).hit();
                    }
                }
            }else if(fixObject!=null){
                ((TileObject) fixObject.getUserData()).onHit(fixEnemy.getUserData());
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
