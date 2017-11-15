package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.my.game.BitHeroes;
import com.my.game.sprites.TileObjects.Terrain;

/**
 * Create the contact listener for the current world
 */

public class WorldContactListener implements ContactListener {
    private BitHeroes game;

    /**
     * Create the listener for every collision event.
     * @param game
     */
    public WorldContactListener(BitHeroes game){
        this.game=game;
    }

    private boolean isCategoryFixture(Fixture fixture,short categoryBit){
        return (fixture != null && fixture.getFilterData().categoryBits== categoryBit);
    }

    private boolean isInstanceFixture(Fixture fixture,Class c){
        return (fixture != null && fixture.getUserData().getClass().getSuperclass().equals(c));
    }

    private Fixture getFixtureByCategory(Contact contact,short categoryBit){
        if (isCategoryFixture(contact.getFixtureA(),categoryBit)) {
            return contact.getFixtureA();
        } else if (isCategoryFixture(contact.getFixtureB(),categoryBit)) {
            return contact.getFixtureB();
        }
        else return null;
    }

    private Fixture getFixtureByInstance(Contact contact,Class c){
        if (isInstanceFixture(contact.getFixtureA(),c)) {
            return contact.getFixtureA();
        } else if (isInstanceFixture(contact.getFixtureB(),c)) {
            return contact.getFixtureB();
        }
        else return null;
    }

    /**
     * Handles all collisions between fixtures in the screen.
     * Controls fixture filterBits to check the Entity, Bullet or TileObject type
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixPlayer;
        Fixture fixEnemy;
        Fixture fixObject;
        Fixture fixEnemyMelee;
        Fixture fixPlayerMelee;
        Fixture fixPlayerBullet = null;
        Fixture fixEnemyBullet = null;

        fixPlayer=getFixtureByCategory(contact,BitHeroes.PLAYER_BIT);
        fixEnemy=getFixtureByCategory(contact,BitHeroes.ENEMY_BIT);
        fixObject=getFixtureByInstance(contact,TileObject.class);

        fixEnemyMelee=getFixtureByCategory(contact,BitHeroes.ENEMY_MELEE_BIT);
        fixPlayerMelee=getFixtureByCategory(contact,BitHeroes.PLAYER_MELEE_BIT);

        Fixture bulletFixture = getFixtureByInstance(contact,Bullet.class);
        if(isCategoryFixture(bulletFixture,BitHeroes.PLAYER_BULLET_BIT)){
            fixPlayerBullet = bulletFixture;
        } else if(isCategoryFixture(bulletFixture,BitHeroes.ENEMY_BULLET_BIT)){
            fixEnemyBullet = bulletFixture;
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
