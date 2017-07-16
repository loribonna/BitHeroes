package com.my.game.sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.my.game.MyGame;
import com.my.game.sprites.Throwables.BlobBall;
import com.my.game.tools.*;

/**
 * Created by lorib on 11/05/2017.
 */

public class Blob extends Enemy {
    /**
     * Create a Blob from Enemy class
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Blob(World world, TextureAtlas screenAtlas, Vector2 position, MyGame game) {
        super(world, screenAtlas,position,game);
        attackRange=0.18f;
        life=1;
        minPlayerDistance=maxMoveRange-2;
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
     * Import enity-specific animations from the Blob atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("blob_walking"), 40, 1, 32, 34);
        setBounds(0, 0, 24 / MyGame.PPM, 30 / MyGame.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("blob_walking"),  2, 1, 32, 34));
        frames.add(new TextureRegion(atlas.findRegion("blob_walking"),  40, 1, 32, 34));
        frames.add(new TextureRegion(atlas.findRegion("blob_walking"),  81, 1 ,32, 34));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("blob_attack"), 10, 8, 54, 32));
        frames.add(new TextureRegion(atlas.findRegion("blob_attack"), 63, 8, 54, 32));
        frames.add(new TextureRegion(atlas.findRegion("blob_attack"), 126, 8, 54, 32));
        throwAnimation = new Animation (0.2f, frames);
        frames.clear();
    }

    /**
     * Replace the second attack with a distance attack
     */
    @Override
    public void secondAttack() {
        currentState = State.THROW;
        previusState = State.THROW;
        stateTimer = 0;
        setRegion(getFrame(0));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
            }
        },throwAnimation.getAnimationDuration());

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                throwBullet();
            }
        },throwAnimation.getAnimationDuration()/2);
    }

    /**
     * Add a bullet in the currentPlayScreen
     */
    private void throwBullet() {
        game.getCurrentPlayScreen().addBullet(new BlobBall(getPosition(), world, isFlipX(),false,game));
    }

}