package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.my.game.MyGame;

/**
 * Created by lorib on 13/05/2017.
 */

public class WorldContactListener implements ContactListener {
    private MyGame game;

    /**
     * Create the listener for every collision event.
     * @param game
     */
    public WorldContactListener(MyGame game){
        this.game=game;
    }

    /**
     * Handles all collisions between fixtures in the screen.
     * Controls fixture filterBits to check the Entity, Bullet or TileObject type
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixPlayer = null;
        Fixture fixObject = null;
        Fixture fixEnemy = null;
        Fixture fixPlayerBullet = null;
        Fixture fixPlayerMelee = null;
        Fixture fixEnemyBullet = null;
        Fixture fixEnemyMelee = null;

        /**
         * Control if one of the fixtures are the Player fixture
         */
        if (contact.getFixtureA() != null && contact.getFixtureA().getFilterData().categoryBits== MyGame.PLAYER_BIT) {
            fixPlayer = contact.getFixtureA();
        } else if (contact.getFixtureB() != null &&contact.getFixtureB().getFilterData().categoryBits== MyGame.PLAYER_BIT) {
            fixPlayer = contact.getFixtureB();
        }

        /**
         * Control if one of the fixtures are the EnemyMelee fixture
         */
        if (contact.getFixtureA() != null && contact.getFixtureA().getFilterData().categoryBits== MyGame.ENEMY_MELEE_BIT) {
            fixEnemyMelee = contact.getFixtureA();
        } else if (contact.getFixtureB() != null &&contact.getFixtureB().getFilterData().categoryBits== MyGame.ENEMY_MELEE_BIT) {
            fixEnemyMelee = contact.getFixtureB();
        }

        /**
         * Control if one of the fixtures are the PlayerMelee fixture
         */
        if (contact.getFixtureA() != null && contact.getFixtureA().getFilterData().categoryBits== MyGame.PLAYER_MELEE_BIT) {
            fixPlayerMelee = contact.getFixtureA();
        } else if (contact.getFixtureB() != null &&contact.getFixtureB().getFilterData().categoryBits== MyGame.PLAYER_MELEE_BIT) {
            fixPlayerMelee = contact.getFixtureB();
        }

        /**
         * Control if one of the fixtures are the EnemyBullet fixture or the PlayerButton fixture
         */
        if (contact.getFixtureA() != null && contact.getFixtureA().getUserData() instanceof Bullet){
            if(contact.getFixtureA().getFilterData().categoryBits== MyGame.PLAYER_BULLET_BIT) {
                fixPlayerBullet = contact.getFixtureA();
            }else if (contact.getFixtureA().getFilterData().categoryBits== MyGame.ENEMY_BULLET_BIT){
                fixEnemyBullet = contact.getFixtureA();
            }
        } else if (contact.getFixtureB() != null && contact.getFixtureB().getUserData() instanceof Bullet){
            if(contact.getFixtureB().getFilterData().categoryBits== MyGame.PLAYER_BULLET_BIT){
                fixPlayerBullet = contact.getFixtureB();
            } else {
                fixEnemyBullet = contact.getFixtureB();
            }
        }

        /**
         * Control if one of the fixtures are the Enemy fixture
         */
        if (contact.getFixtureA() != null && contact.getFixtureA().getFilterData().categoryBits== MyGame.ENEMY_BIT) {
            fixEnemy = contact.getFixtureA();
        } else if (contact.getFixtureB() != null &&contact.getFixtureB().getFilterData().categoryBits== MyGame.ENEMY_BIT) {
            fixEnemy = contact.getFixtureB();
        }

        /**
         * Control if one of the fixtures are the TileObject fixture
         */
        if (contact.getFixtureA() != null && contact.getFixtureA().getUserData() instanceof TileObject) {
            fixObject = contact.getFixtureA();
        } else if (contact.getFixtureB() != null &&contact.getFixtureB().getUserData() instanceof TileObject) {
            fixObject = contact.getFixtureB();
        }

        /**
         * Handle collision with Player and something else
         */
        if(fixPlayer!=null) {
            if (fixObject != null) {
                if(fixPlayer.getUserData() instanceof Entity)
                    ((TileObject) fixObject.getUserData()).onHit(((Entity)fixPlayer.getUserData()));
            }

            if(fixEnemyBullet!=null){
                game.getCurrentPlayScreen().player.hit(((Bullet) fixEnemyBullet.getUserData()).getDamage());
                ((Bullet) fixEnemyBullet.getUserData()).dispose();
            }else if(fixEnemyMelee!=null){
                game.getCurrentPlayScreen().player.hit((Integer) fixEnemyMelee.getUserData());
                fixEnemyMelee.setUserData(0);
            }
        }

        /**
         * Handle collision with Enemy and something else
         */
        if (fixEnemy!=null) {
            if(fixPlayer!=null) {
                if (fixPlayer.getUserData().toString().contains("feet")) {
                    if (!((Enemy) fixEnemy.getUserData()).isInvulnerable()&&!((Enemy) fixEnemy.getUserData()).isFlying()) {
                        game.getCurrentPlayScreen().player.body.applyLinearImpulse(
                                new Vector2(0, (-game.getCurrentPlayScreen().player.body.getLinearVelocity().y) + 3),
                                game.getCurrentPlayScreen().player.body.getWorldCenter(), true
                        );

                        ((Enemy) fixEnemy.getUserData()).hit(110);
                    }
                }
            }else if(fixObject!=null){
                ((TileObject) fixObject.getUserData()).onHit(((Enemy)fixEnemy.getUserData()));
            }else if(fixPlayerBullet!=null){
                ((Enemy) fixEnemy.getUserData()).hit(((Bullet) fixPlayerBullet.getUserData()).getDamage());
                ((Bullet) fixPlayerBullet.getUserData()).dispose();
            }else if(fixPlayerMelee!=null){
                ((Enemy) fixEnemy.getUserData()).hit((Integer) fixPlayerMelee.getUserData());
                fixPlayerMelee.setUserData(0);
            }
        }

        /**
         * Handle collision with enemy and player bullets and the ground
         */
        if(fixPlayerBullet!=null){
            ((Bullet) fixPlayerBullet.getUserData()).dispose();
        }
        if(fixEnemyBullet!=null){
            ((Bullet) fixEnemyBullet.getUserData()).dispose();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
