package com.my.game.sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.BitHeroes;
import com.my.game.sprites.Throwables.BlobBall;
import com.my.game.tools.*;
import com.my.game.tools.Enemy;
import com.my.game.tools.FightDecorators.ArtificialFight.ArtificialDistanceFight;

/**
 * Create a Blob entity from Enemy class
 */

public class Blob extends Enemy {
    /**
     * Create a Blob from Enemy class
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Blob(World world, TextureAtlas screenAtlas, Vector2 position, BitHeroes game) {
        super(world, screenAtlas,position,game);
        this.attackSystem=new ArtificialDistanceFight(this,world,this.attackSystem,this.attackAnimation,game,BlobBall.class);
        life=1;
        this.attackSystem.setAttackRange(maxMoveRange-2);
    }

    /**
     * Import enity-specific animations from the Blob atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("blob_walking"), 40, 1, 32, 34);
        setBounds(0, 0, 24 / BitHeroes.PPM, 30 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = AppConstants.State.STAND;
        previousState = AppConstants.State.STAND;
        stateTimer = 0;

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

}