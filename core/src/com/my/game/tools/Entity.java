package com.my.game.tools;

import com.badlogic.gdx.Gdx;
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
import com.my.game.MyGame;
import com.my.game.sprites.Enemies.Dragon;

/**
 * Created by lorib on 11/05/2017.
 */

public abstract class Entity extends Sprite implements com.my.game.tools.Interfaces.EntityInterface {
    protected World world;
    protected Body body;
    protected boolean isPlayer=true;
    protected int life=100;//default
    protected State currentState;
    protected State previusState;
    protected Animation throwAnimation;
    protected Animation attackAnimation;
    protected Animation runAnimation;
    protected TextureRegion standAnimation;
    protected boolean runRight;
    protected float stateTimer;
    protected boolean invulnarable=false;
    protected boolean lockAttack;
    protected boolean dead=false;
    protected int meleeDamage=20;
    protected MyGame game;

    /**
     * Initialize Entity variables and create borders and animations.
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Entity(World world, TextureAtlas screenAtlas, Vector2 position, MyGame game) {
        super();
        this.game=game;
        currentState = State.STAND;
        previusState = State.STAND;
        this.world = world;
        define(position);
        getAnimations(screenAtlas);
    }

    /**
     * @return true if the entity is the player
     */
    public boolean isPlayer(){
        return isPlayer;
    }

    /**
     * @return current entity body
     */
    public Body getBody(){
        return body;
    }

    /**
     * @return body position
     */
    public Vector2 getPosition(){
        return this.body.getPosition();
    }

    /**
     * @return current life
     */
    public int getLife(){
        return life;
    }

    /**
     * Import enity-specific animations from the atlas.
     * @param atlas
     */
    @Override
    public abstract void getAnimations(TextureAtlas atlas);

    /**
     * Get current entity filter to set collisions.
     * @return
     */
    public abstract Filter getFilter();

    /**
     * Update position, target (if enemy) and animation.
     * @param delta
     */
    @Override
    public abstract void update(float delta);

    /**
     * @return: True if entity is invulnerable
     */
    public boolean isInvulnerable(){return invulnarable;}

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
        invulnarable=true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                invulnarable=false;
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

        if (currentState == previusState) {
            stateTimer = stateTimer + dt;
        } else {
            stateTimer = 0;
        }
        previusState = currentState;
        return region;
    }

    /**
     * @return: new state based on the action being performed and movement of the body.
     */
    public State getState() {
        if (previusState == State.ATTACK) {
            if (!attackAnimation.isAnimationFinished(stateTimer))
                return State.ATTACK;
            else {
                if (body.getLinearVelocity().x != 0) {
                    return State.RUN;
                } else if (previusState != State.ATTACK) {
                    return State.STAND;
                }
            }
        }else if(previusState == State.THROW){
            if(throwAnimation!=null) {
                if (!throwAnimation.isAnimationFinished(stateTimer))
                    return State.THROW;
                else{
                    if (body.getLinearVelocity().x != 0) {
                        return State.RUN;
                    } else if (previusState != State.ATTACK) {
                        return State.STAND;
                    }
                }
            }else{
                if(!lockAttack) {
                    if (body.getLinearVelocity().x != 0) {
                        return State.RUN;
                    } else if (previusState != State.ATTACK) {
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

        bdef.position.set(position.x / MyGame.PPM,position.y / MyGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body=world.createBody(bdef);

        createBorders();
    }

    /**
     * Set fixtures in the current body.
     */
    public abstract void createBorders();

    /**
     * Perform entity primary attack. Default is nothing.
     */
    public void firstAttack(){
        lockAttack=false;
    }

    /**
     * Perform entity secondary attack. Default is nothing.
     */
    public void secondAttack(){
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
            if (attackType == AttackType.SECOND) {
                secondAttack();
            } else {
                firstAttack();
            }
        }else{
            return;
        }
    }

    /**
     * Create fixture to trigger collision for Melee attack if the attack is front
     * @return
     */
    public FixtureDef createFrontAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponFront = new PolygonShape();
        weaponFront.set(new Vector2[]{new Vector2(16,-2).scl(1/MyGame.PPM),new Vector2(16,-4).scl(1/MyGame.PPM)
                ,new Vector2(8,-2).scl(1/MyGame.PPM),new Vector2(8,-4).scl(1/MyGame.PPM)});
        fdef.shape = weaponFront;
        if(isPlayer){
            fdef.filter.categoryBits=MyGame.PLAYER_MELEE_BIT;
            fdef.filter.groupIndex=MyGame.GROUP_BULLET;
            fdef.filter.maskBits=MyGame.ENEMY_BIT;
        }else{
            fdef.filter.categoryBits=MyGame.ENEMY_MELEE_BIT;
            fdef.filter.groupIndex=MyGame.GROUP_BULLET;
            fdef.filter.maskBits=MyGame.PLAYER_BIT;
        }
        fdef.isSensor=true;
        return fdef;
    }

    /**
     * Create fixture to trigger collision for Melee attack if the body is flipped.
     * @return
     */
    public FixtureDef createBackAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponBack = new PolygonShape();
        weaponBack.set(new Vector2[]{new Vector2(-16,-2).scl(1/MyGame.PPM),new Vector2(-16,-4).scl(1/MyGame.PPM)
                ,new Vector2(-8,-2).scl(1/MyGame.PPM),new Vector2(-8,-4).scl(1/MyGame.PPM)});
        fdef.shape = weaponBack;
        if(isPlayer){
            fdef.filter.categoryBits=MyGame.PLAYER_MELEE_BIT;
            fdef.filter.groupIndex=MyGame.GROUP_BULLET;
            fdef.filter.maskBits=MyGame.ENEMY_BIT;
        }else{
            fdef.filter.categoryBits=MyGame.ENEMY_MELEE_BIT;
            fdef.filter.groupIndex=MyGame.GROUP_BULLET;
            fdef.filter.maskBits=MyGame.PLAYER_BIT;
        }
        fdef.isSensor=true;
        return fdef;
    }


}
