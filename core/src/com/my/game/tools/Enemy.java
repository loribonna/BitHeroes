package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.my.game.MyGame;

import java.io.Console;
import java.util.Iterator;

/**
 * Created by lorib on 29/05/2017.
 */

public abstract class Enemy extends Entity{
    protected enum direction{right,left,stop,up};
    protected float attackRange=0.18f;
    protected float maxMoveRange=10;
    protected float minHeight=0.1f;
    protected direction XDirection=direction.stop;
    protected direction YDirection=direction.stop;

    public Enemy(World w, TextureAtlas screenAtlas, Vector2 position) {
        super(w, screenAtlas, position);
    }

    /**
     * Set current target based on Player position in the current PlayScreen
     */
    protected void setTarget(){
        Vector2 targetPlayer = PlayScreen.current.getPlayerPosition();
        Vector2 entityPosition= getPosition();
        float dy=targetPlayer.y-entityPosition.y;
        float dx=targetPlayer.x-entityPosition.x;
        float m=dy/dx;
        if(dx<maxMoveRange){
            if(dx>attackRange){
                throwAttack(AttackType.THROW);
                XDirection=direction.right;
            }else if(dx<-attackRange) {
                throwAttack(AttackType.THROW);
                XDirection = direction.left;
            }else{
                XDirection=direction.stop;
                attack();
            }
        }else{
            XDirection=direction.stop;
            attack();
        }

        if(dy>minHeight&&XDirection!=direction.stop){
            YDirection=direction.up;
        }else{
            YDirection=direction.stop;
        }
    }

    @Override
    public void update(float delta) {
        if(!dead) {
            setTarget();
            if (XDirection == direction.right) {
                if (body.getLinearVelocity().x <= 1)
                    body.applyLinearImpulse(new Vector2(0.1f, 0), body.getWorldCenter(), true);
            } else if (XDirection == direction.left) {
                if (body.getLinearVelocity().x >= -1)
                    body.applyLinearImpulse(new Vector2(-0.1f, 0), body.getWorldCenter(), true);
            } else if (XDirection == direction.stop) {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
            }

            if (YDirection == direction.up) {
                if (this.getState() != State.JUMP &&
                        this.getState() != State.FALL &&
                        this.getState() != State.ATTACK) {
                    body.applyLinearImpulse(new Vector2(0, 3), body.getWorldCenter(), true);

                }
            }

            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(delta));
        }
    }

    @Override
    public void recoil(){
        if(isFlipX())
            body.applyLinearImpulse(new Vector2(2,1),body.getWorldCenter(),true);
        else body.applyLinearImpulse(new Vector2(-2,1),body.getWorldCenter(),true);
    }

    @Override
    public Filter getFilter() {
        Filter f = new Filter();
        f.categoryBits = MyGame.ENEMY_BIT;
        f.maskBits =(MyGame.DEFAULT_BIT | MyGame.BRICK_BIT | MyGame.COIN_BIT |
                MyGame.PLAYER_BIT | MyGame.VOID_BIT | MyGame.PLAYER_BULLET_BIT | MyGame.PLAYER_MELEE_BIT);
        f.groupIndex = MyGame.GROUP_ENEMIES;
        return f;
    }

    @Override
    public void destroy(){
        dead=true;
        body.setUserData(new Boolean(true));
        PlayScreen.current.objectsToRemove.add(this);
    }

    @Override
    public void createBorders() {
        FixtureDef fdef = new FixtureDef();
        Filter filter = getFilter();

        fdef.filter.groupIndex=filter.groupIndex;
        fdef.filter.categoryBits= filter.categoryBits;
        fdef.filter.maskBits=filter.maskBits;

        CircleShape bShape = new CircleShape();
        bShape.setRadius(6/MyGame.PPM);
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData(this);
    }
}
