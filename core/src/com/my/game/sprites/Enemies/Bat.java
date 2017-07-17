package com.my.game.sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.tools.*;

/**
 * Create a Bat entity from Enemy class
 */

public class Bat extends Enemy {

    /**
     * Create a Bat entity from Enemy class
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Bat(World world, TextureAtlas screenAtlas,Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        attackRange=0.18f;
        life=1;
    }

    /**
     * Perform a distance attack
     */
    @Override
    protected void distanceAttack() {
        secondAttack();
    }

    /**
     * Perform a melee attack
     */
    @Override
    protected void meleeAttack() {
        firstAttack();
    }

    /**
     * Import enity-specific animations from the Bat atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("bat_flying"), 17,-1, 15, 34);
        setBounds(0, 0, 24 / BitHeroes.PPM, 30 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("bat_flying"),  1, -1, 15, 34));
        frames.add(new TextureRegion(atlas.findRegion("bat_flying"),  17, -1, 15, 34));
        frames.add(new TextureRegion(atlas.findRegion("bat_flying"),  36, -1 ,15, 34));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("bat_attack"), -1, 2, 15, 14));
        frames.add(new TextureRegion(atlas.findRegion("bat_attack"), 15, 2, 15, 14));
        attackAnimation = new Animation (0.1f, frames);
        frames.clear();
    }

    /**
     * Replace the first attack with a melee attack
     */
    @Override
    public void firstAttack() {
        currentState = State.ATTACK;
        previusState = State.ATTACK;
        stateTimer = 0;
        setRegion(getFrame(0));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
            }
        },attackAnimation.getAnimationDuration());


        final BodyDef bDef=new BodyDef();
        bDef.position.set(body.getPosition());
        bDef.type = BodyDef.BodyType.DynamicBody;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                final Body attackBody = world.createBody(bDef);
                attackBody.setGravityScale(0);

                if (isFlipX()) {
                    final Fixture f = attackBody.createFixture(createBackAttackFixture());
                    f.setUserData(meleeDamage);

                } else {
                    final Fixture f = attackBody.createFixture(createFrontAttackFixture());
                    f.setUserData(meleeDamage);
                }
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        attackBody.setUserData(true);
                    }
                }, attackAnimation.getAnimationDuration() / 2);
            }
        }, attackAnimation.getAnimationDuration() / 2);
    }

}