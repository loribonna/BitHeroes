package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants.Direction;
import com.my.game.tools.AppConstants.State;
import com.my.game.tools.FightDecorators.DefaultFight;

/**
 * Abstract class with Enemy controls and artificial intelligence
 */

public abstract class Enemy extends Entity {
    protected boolean disableJump=false;
    protected Direction XDirection= AppConstants.Direction.STOP;
    protected AppConstants.Direction YDirection=AppConstants.Direction.STOP;
    protected boolean isFlying=false;
    protected float maxMoveRange=1.7f;
    protected final float minHeight=0.1f;
    /**
     * Initialize enemy variables and disable body. It will be activated when the player gets close.
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Enemy(World world, TextureAtlas screenAtlas, Vector2 position,BitHeroes game) {
        super(world, screenAtlas, position, game);
        isPlayer=false;
        body.setActive(false);
        this.attackSystem=new DefaultFight(false,this,world,game);
        setMoveSpeed(0.5f);
    }

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
     * Set current target based on Player position in the current PlayScreen.
     * If the Player gets close the enemy gets activated.
     */
    protected void setTarget(){
        Vector2 targetPlayer = game.getCurrentPlayScreen().getPlayerPosition();
        Vector2 entityPosition= getPosition();
        float dy=targetPlayer.y-entityPosition.y;
        float dx=targetPlayer.x-entityPosition.x;

        if(Math.abs(dx)<maxMoveRange){

            if(!body.isActive()) {
                body.setActive(true);
            }
            XDirection=attackSystem.setTarget(dx,dy);

            if(XDirection==Direction.NONE){
                if(dx>0)
                {
                    XDirection=Direction.RIGHT;
                }else{
                    XDirection=Direction.LEFT;
                }
            }

        }else{
            XDirection=Direction.STOP;
        }

        if(dy>minHeight&&XDirection!=Direction.STOP){
            YDirection=Direction.UP;
        }else{
            YDirection=Direction.STOP;
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
            if (XDirection == Direction.RIGHT) {
                if (body.getLinearVelocity().x <= 1)
                    moveRight(moveSpeed);
            } else if (XDirection == Direction.LEFT) {
                if (body.getLinearVelocity().x >= -1)
                    moveLeft(moveSpeed);
            } else if (XDirection == Direction.STOP) {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
            }

            if (!disableJump && YDirection == AppConstants.Direction.UP) {
                if (this.getState() != State.JUMP &&
                        this.getState() != State.FALL &&
                        this.getState() != State.ATTACK) {
                    if(!isFlying) {
                        isFlying=true;
                        jump(3);
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
     * @return enemy filter bits to set collisions.
     */
    @Override
    public Filter getFilter() {
        Filter f = new Filter();
        f.categoryBits = BitHeroes.ENEMY_BIT;
        f.maskBits =(BitHeroes.DEFAULT_BIT | BitHeroes.BRICK_BIT | BitHeroes.COIN_BIT |
                BitHeroes.PLAYER_BIT | BitHeroes.VOID_BIT | BitHeroes.PLAYER_BULLET_BIT | BitHeroes.PLAYER_MELEE_BIT);
        f.groupIndex = BitHeroes.GROUP_ENEMIES;
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
        bShape.setRadius(6/ BitHeroes.PPM);
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData(this);
    }
}
