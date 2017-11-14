package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants.State;
import com.my.game.tools.FightDecorators.Fight;

/**
 * Common controls for any Entity
 */

public abstract class Entity extends Sprite {
    protected World world;
    protected Body body;
    protected boolean isPlayer;
    protected int life=100;
    protected State currentState;
    protected State previousState;
    protected Animation throwAnimation;
    protected Animation attackAnimation;
    protected Animation runAnimation;
    protected TextureRegion standAnimation;
    protected boolean runRight;
    protected float stateTimer;
    protected boolean invulnerable=false;
    protected boolean dead=false;
    protected BitHeroes game;
    protected Music music;
    protected Fight attackSystem;
    protected float moveSpeed=1;

    /**
     * Initialize Entity variables and create borders and animations.
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Entity(World world, TextureAtlas screenAtlas, Vector2 position, BitHeroes game) {
        super();
        this.game=game;
        currentState = State.STAND;
        previousState = State.STAND;
        this.world = world;
        define(position);
        getAnimations(screenAtlas);
    }

    public boolean isPlayer(){
        return isPlayer;
    }

    public Body getBody(){
        return body;
    }

    public Vector2 getPosition(){
        return this.body.getPosition();
    }

    public int getLife(){
        return life;
    }

    public abstract void getAnimations(TextureAtlas atlas);

    public abstract Filter getFilter();

    /**
     * Update position, target (if enemy) and animation.
     * @param delta
     */
    public abstract void update(float delta);

    public boolean isInvulnerable(){return invulnerable;}

    /**
     * Process hit event.
     * @param damage
     */
    public void hit(int damage){
        life-=damage;
        if(life<=0){
            destroy();
        }else if(damage>0){
            recoil();
        }
        invulnerable =true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                invulnerable =false;
            }
        },1);
    }

    public void setMoveSpeed(float value){
        this.moveSpeed=value;
    }

    /**
     * Perform some action after Hit event.
     */
    public abstract void recoil();

    /**
     * Destroy current body and fixtures.
     */
    public abstract void destroy();

    /**
     * @param dt
     * @return current frame of animation based on the current state.
     */
    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case RUN:
                region = (TextureRegion) runAnimation.getKeyFrame(stateTimer, true);
                break;
            case ATTACK:
                region = (TextureRegion) attackAnimation.getKeyFrame(stateTimer);
                break;
            case THROW:
                region = (TextureRegion) throwAnimation.getKeyFrame(stateTimer);
                break;
            default:
                region = standAnimation;
                break;
        }
        if ((body.getLinearVelocity().x < 0 || !runRight) && !region.isFlipX()) {
            region.flip(true, false);
            runRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runRight) && region.isFlipX()) {
            region.flip(true, false);
            runRight = true;
        }

        if (currentState == previousState) {
            stateTimer = stateTimer + dt;
        } else {
            stateTimer = 0;
        }
        previousState = currentState;
        return region;
    }

    public void updateState(State currentState,State previousState){
        this.currentState=currentState;
        this.previousState=previousState;
        stateTimer = 0;
        setRegion(getFrame(0));
    }

    /**
     * @return new state based on the action being performed and movement of the body.
     */
    public State getState() {
        if (previousState == State.ATTACK) {
            if (!attackAnimation.isAnimationFinished(stateTimer))
                return State.ATTACK;
            else {
                if (body.getLinearVelocity().x != 0) {
                    return State.RUN;
                } else if (previousState != State.ATTACK) {
                    return State.STAND;
                }
            }
        }else if(previousState == State.THROW){
            if(throwAnimation!=null) {
                if (!throwAnimation.isAnimationFinished(stateTimer))
                    return State.THROW;
                else{
                    if (body.getLinearVelocity().x != 0) {
                        return State.RUN;
                    } else if (previousState != State.ATTACK) {
                        return State.STAND;
                    }
                }
            }else{
                if(!attackSystem.isFighting()) {
                    if (body.getLinearVelocity().x != 0) {
                        return State.RUN;
                    } else if (previousState != State.ATTACK) {
                        return State.STAND;
                    }
                }
            }

        }

        if (body.getLinearVelocity().y > 0) {
            return State.JUMP;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALL;
        }
        if (body.getLinearVelocity().x != 0) {
            return State.RUN;
        }

        return State.STAND;
    }

    protected void jump(float power){
        if(power<0) return;
        body.applyLinearImpulse(new Vector2(0, power), body.getWorldCenter(), true);
    }

    protected void moveRight(float power){
        if(power<0) return;
        body.applyLinearImpulse(new Vector2(0.1f*power,0),body.getWorldCenter(),true);
    }

    protected void moveLeft(float power){
        if(power<0) return;
        body.applyLinearImpulse(new Vector2(-0.1f*power,0),body.getWorldCenter(),true);
    }

    public void updateSize(float width,float height){
        setSize(width / BitHeroes.PPM, height / BitHeroes.PPM);
    }

    /**
     * Create the body of the entity in the given position in the world.
     * @param position
     */
    public void define(Vector2 position) {
        BodyDef bdef= new BodyDef();

        bdef.position.set(position.x / BitHeroes.PPM,position.y / BitHeroes.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body=world.createBody(bdef);

        createBorders();
    }

    /**
     * Set fixtures in the current body.
     */
    public abstract void createBorders();

}
