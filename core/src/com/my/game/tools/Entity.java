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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.my.game.MyGame;
import com.my.game.sprites.Enemies.Dragon;

/**
 * Created by lorib on 11/05/2017.
 */

public abstract class Entity extends Sprite implements com.my.game.tools.Interfaces.EntityInterface {
    public World world;
    public Body body;
    public boolean isPlayer=true;

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
    public Entity(World w, TextureAtlas screenAtlas, Vector2 position, MyGame game) {
        super();
        this.game=game;
        currentState = State.STAND;
        previusState = State.STAND;
        this.world = w;
        define(position);
        getAnimations(screenAtlas);
    }

    /**
     * Return body position.
     * @return
     */
    public Vector2 getPosition(){
        return this.body.getPosition();
    }

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
     * Get current entity filter to sei collisions.
     * @return
     */
    public abstract Filter getFilter();

    /**
     * Update position, target and animation.
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
     * Perform action after Hit event.
     */
    public abstract void recoil();

    /**
     * Destroy current body fixtures.
     */
    public abstract void destroy();

    /**
     * Get current frame of the animation based on the current state.
     * @param dt
     * @return
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
     * @return: Current state based on the action being performed and movement of the body.
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
     * Create body of the entity in the given position in the world.
     * Call method to set fixtures.
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

    public abstract void firstAttack();
    public abstract void secondAttack();
    public abstract void specialAttack();

    /**
     * Perform attack based on the attackType parameter.
     * Control if lockAttack object is released before perform another attack.
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
    protected abstract FixtureDef createFrontAttackFixture();

    /**
     * Create fixture to trigger collision for Melee attack if the body is flipped.
     * @return
     */
    protected abstract FixtureDef createBackAttackFixture();


}
