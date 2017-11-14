package com.my.game.sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.BitHeroes;
import com.my.game.tools.Enemy;
import com.my.game.tools.FightDecorators.ArtificialFight.ArtificialMeleeFight;
import com.my.game.tools.AppConstants.State;

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
        this.attackSystem=new ArtificialMeleeFight(20,this,world,this.attackSystem,attackAnimation,game);
        life=1;
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
        previousState = State.STAND;
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
}