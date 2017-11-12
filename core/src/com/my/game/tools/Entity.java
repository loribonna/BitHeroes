package com.my.game.tools;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.tools.Interfaces.IEntity;
import com.my.game.tools.Interfaces.IFight;

/**
 * Common controls for any Entity
 */

public abstract class Entity extends Sprite implements IEntity,IFight {
    protected World world;
    protected Body body;
    protected boolean isPlayer=true;
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
    protected boolean lockAttack;
    protected boolean dead=false;
    protected int meleeDamage=20;
    protected BitHeroes game;
    protected Music music;

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
                if(!lockAttack) {
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

    /**
     * Perform entity melee attack. Default is nothing.
     */
    public void meleeAttack(){
        lockAttack=false;
    }

    /**
     * Perform entity distance attack. Default is nothing.
     */
    public void distanceAttack(){
        lockAttack=false;
    }

    /**
     * Perform entity's special attack. Default is nothing.
     */
    public void specialAttack(){
        lockAttack=false;
    }

    /**
     * Perform attack based on the attackType parameter.
     * Control if lockAttack is released before perform another attack.
     * @param attackType
     */
    public void throwAttack(AttackType attackType) {
        if(!lockAttack) {
            lockAttack=true;
            if (attackType == AttackType.DISTANCE) {
                distanceAttack();
            } else {
                meleeAttack();
            }
        }else{
            return;
        }
    }

    /**
     * @return fixture to trigger collision for Melee attack if the attack is front
     */
    public FixtureDef createFrontAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponFront = new PolygonShape();
        weaponFront.set(new Vector2[]{new Vector2(16,-2).scl(1/ BitHeroes.PPM),new Vector2(16,-4).scl(1/ BitHeroes.PPM)
                ,new Vector2(8,-2).scl(1/ BitHeroes.PPM),new Vector2(8,-4).scl(1/ BitHeroes.PPM)});
        fdef.shape = weaponFront;
        if(isPlayer){
            fdef.filter.categoryBits= BitHeroes.PLAYER_MELEE_BIT;
            fdef.filter.groupIndex= BitHeroes.GROUP_BULLET;
            fdef.filter.maskBits= BitHeroes.ENEMY_BIT;
        }else{
            fdef.filter.categoryBits= BitHeroes.ENEMY_MELEE_BIT;
            fdef.filter.groupIndex= BitHeroes.GROUP_BULLET;
            fdef.filter.maskBits= BitHeroes.PLAYER_BIT;
        }
        fdef.isSensor=true;
        return fdef;
    }

    /**
     * @return fixture to trigger collision for Melee attack if the body is flipped.
     */
    public FixtureDef createBackAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponBack = new PolygonShape();
        weaponBack.set(new Vector2[]{new Vector2(-16,-2).scl(1/ BitHeroes.PPM),new Vector2(-16,-4).scl(1/ BitHeroes.PPM)
                ,new Vector2(-8,-2).scl(1/ BitHeroes.PPM),new Vector2(-8,-4).scl(1/ BitHeroes.PPM)});
        fdef.shape = weaponBack;
        if(isPlayer){
            fdef.filter.categoryBits= BitHeroes.PLAYER_MELEE_BIT;
            fdef.filter.groupIndex= BitHeroes.GROUP_BULLET;
            fdef.filter.maskBits= BitHeroes.ENEMY_BIT;
        }else{
            fdef.filter.categoryBits= BitHeroes.ENEMY_MELEE_BIT;
            fdef.filter.groupIndex= BitHeroes.GROUP_BULLET;
            fdef.filter.maskBits= BitHeroes.PLAYER_BIT;
        }
        fdef.isSensor=true;
        return fdef;
    }


}
