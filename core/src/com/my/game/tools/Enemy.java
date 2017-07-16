package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;

/**
 * Created by lorib on 29/05/2017.
 */

public abstract class Enemy extends Entity{
    protected enum direction{right,left,stop,up};
    protected float attackRange=0.18f;
    protected float minPlayerDistance=0.1f;
    protected float maxMoveRange=1.5f;
    protected boolean disableJump=false;
    protected float minHeight=0.1f;
    protected direction XDirection=direction.stop;
    protected direction YDirection=direction.stop;
    protected boolean isFlying=false;

    /**
     * Initialize enemy variables and disable body. It will be activated when the player gets close.
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Enemy(World world, TextureAtlas screenAtlas, Vector2 position,MyGame game) {
        super(world, screenAtlas, position, game);
        isPlayer=false;
        body.setActive(false);

    }

    /**
     * Perform a distance attack if the player is too far
     */
    protected abstract void distanceAttack();

    /**
     * Set fliying flag
     * @param isFlying
     */
    public void setFlying(boolean isFlying){
        this.isFlying=isFlying;
    }

    /**
     * @return true if entity is performing a jump
     */
    public boolean isFlying(){
        return isFlying;
    }

    /**
     * Perform a melee attack when the player is close enough
     */
    protected abstract void meleeAttack();

    /**
     * Set current target based on Player position in the current PlayScreen.
     * If the Player gets close the enemy gets activated.
     */
    protected void setTarget(){
        Vector2 targetPlayer = game.getCurrentPlayScreen().getPlayerPosition();
        Vector2 entityPosition= getPosition();
        float dy=targetPlayer.y-entityPosition.y;
        float dx=targetPlayer.x-entityPosition.x;
        float m=dy/dx;
        if(Math.abs(dx)<maxMoveRange){
            if(!body.isActive()) {
                body.setActive(true);
            }
            if(Math.abs(dx)>Math.abs(minPlayerDistance)){
                if(dx>attackRange){
                    throwAttack(AttackType.SECOND);
                    XDirection=direction.right;
                }else if(dx<-attackRange) {
                    throwAttack(AttackType.SECOND);
                    XDirection = direction.left;
                }else if(dy>-attackRange&&dy<attackRange){
                    XDirection=direction.stop;
                    throwAttack(AttackType.FIRST);
                }
            }else{
                if(dy>-attackRange&&dy<attackRange) {
                    throwAttack(AttackType.SECOND);
                }

                if(dx>0)
                {
                    if(isFlipX()){
                        XDirection=direction.right;
                    }else{
                        XDirection=direction.stop;
                    }
                }else{
                    if(!isFlipX()){
                        XDirection=direction.left;
                    }else{
                        XDirection=direction.stop;
                    }
                }

            }


        }else{
            XDirection=direction.stop;
        }

        if(dy>minHeight&&XDirection!=direction.stop){
            YDirection=direction.up;
        }else{
            YDirection=direction.stop;
        }
    }

    /**
     * Perform Artificial-Intelligence controls to set movements and attacks.
     * Update current state and animations.
     * @param delta
     */
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

            if (!disableJump && YDirection == direction.up) {
                if (this.getState() != State.JUMP &&
                        this.getState() != State.FALL &&
                        this.getState() != State.ATTACK) {
                    if(!isFlying) {
                        isFlying=true;
                        body.applyLinearImpulse(new Vector2(0, 3), body.getWorldCenter(), true);
                    }

                }
            }

            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(delta));
        }
    }

    /**
     * Perform shake movement after hit.
     */
    @Override
    public void recoil(){
        if(isFlipX())
            body.applyLinearImpulse(new Vector2(0.2f,1),body.getWorldCenter(),true);
        else body.applyLinearImpulse(new Vector2(-0.2f,1),body.getWorldCenter(),true);
    }

    /**
     * Get enemy filter bits to set collisions.
     * @return
     */
    @Override
    public Filter getFilter() {
        Filter f = new Filter();
        f.categoryBits = MyGame.ENEMY_BIT;
        f.maskBits =(MyGame.DEFAULT_BIT | MyGame.BRICK_BIT | MyGame.COIN_BIT |
                MyGame.PLAYER_BIT | MyGame.VOID_BIT | MyGame.PLAYER_BULLET_BIT | MyGame.PLAYER_MELEE_BIT);
        f.groupIndex = MyGame.GROUP_ENEMIES;
        return f;
    }

    /**
     * Destroy current body and fixtures.
     */
    @Override
    public void destroy(){
        dead=true;
        body.setUserData(new Boolean(true));
        game.removeObject(this);
    }

    /**
     * Set fixtures in the current body.
     */
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
