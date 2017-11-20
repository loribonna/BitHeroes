package com.my.game.sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.Enemy;
import com.my.game.tools.FightDecorators.ArtificialFight.ArtificialMeleeFight;

/**
 * Create a Skeleton entity from Enemy class
 */

public class Skeleton extends Enemy {
    /**
     * Create a Skeleton from Enemy class
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Skeleton(World world, TextureAtlas screenAtlas,Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        this.attackSystem=new ArtificialMeleeFight(20,this,world,this.attackSystem,attackAnimation,game);
        life=1;
    }

    /**
     * Import enity-specific animations from the Skeleton atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("Skeleton_walking"), 38, 1, 26, 34);
        standAnimation.flip(true,false);
        setBounds(0, 0, 24 / BitHeroes.PPM, 30 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = AppConstants.State.STAND;
        previousState = AppConstants.State.STAND;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("Skeleton_walking"),  2, -1, 26, 34));
        frames.add(new TextureRegion(atlas.findRegion("Skeleton_walking"),  38, 1, 26, 34));
        frames.add(new TextureRegion(atlas.findRegion("Skeleton_walking"),  67, -1, 26, 34));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("Skeleton_attack"), 5, -1, 33, 31));
        frames.add(new TextureRegion(atlas.findRegion("Skeleton_attack"), 30, -1, 33, 31));

        attackAnimation = new Animation (0.1f, frames);
        frames.clear();
    }
}