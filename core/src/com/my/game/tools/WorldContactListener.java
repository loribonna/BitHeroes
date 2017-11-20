package com.my.game.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.my.game.BitHeroes;

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

    private void checkPlayerCollisions(Fixture player,Fixture object,Fixture bullet,Fixture enemyMelee){
        if(player!=null) {
            if (object != null) {
                if(player.getUserData() instanceof Entity)
                    ((TileObject) object.getUserData()).onHit(((Entity)player.getUserData()));
            }

            if(bullet!=null){
                game.getCurrentPlayScreen().player.hit(((Bullet) bullet.getUserData()).getDamage());
                ((Bullet) bullet.getUserData()).dispose();
            }else if(enemyMelee!=null){
                game.getCurrentPlayScreen().player.hit((Integer) enemyMelee.getUserData());
                enemyMelee.setUserData(0);
            }
        }
    }

    private void checkEnemyCollisions(Fixture enemy,Fixture object,Fixture bullet,Fixture player, Fixture playerMelee){
        if (enemy!=null) {
            if(player!=null) {
                if (player.getUserData().toString().contains("feet")) {
                    if (!((Enemy) enemy.getUserData()).isInvulnerable()&&!((Enemy) enemy.getUserData()).isFlying()) {
                        game.getCurrentPlayScreen().player.body.applyLinearImpulse(
                                new Vector2(0, (-game.getCurrentPlayScreen().player.body.getLinearVelocity().y) + 3),
                                game.getCurrentPlayScreen().player.body.getWorldCenter(), true
                        );

                        ((Enemy) enemy.getUserData()).hit(110);
                    }
                }
            }else if(object!=null){
                ((TileObject) object.getUserData()).onHit(((Enemy)enemy.getUserData()));
            }else if(bullet!=null){
                ((Enemy) enemy.getUserData()).hit(((Bullet) bullet.getUserData()).getDamage());
                ((Bullet) bullet.getUserData()).dispose();
            }else if(playerMelee!=null){
                ((Enemy) enemy.getUserData()).hit((Integer) playerMelee.getUserData());
                playerMelee.setUserData(0);
            }
        }
    }

    private void checkBulletCollision(Fixture bullet){
        if(bullet!=null){
            ((Bullet) bullet.getUserData()).dispose();
        }
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

        checkPlayerCollisions(fixPlayer,fixObject,fixEnemyBullet,fixEnemyMelee);
        checkEnemyCollisions(fixEnemy,fixObject,fixPlayerBullet,fixPlayer,fixPlayerMelee);
        checkBulletCollision(fixEnemyBullet);
        checkBulletCollision(fixPlayerBullet);

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
