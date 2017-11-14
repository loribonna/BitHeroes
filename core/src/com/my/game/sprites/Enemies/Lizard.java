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
import com.my.game.tools.FightDecorators.ArtificialFight.ArtificialMeleeFight;

/**
 * Create a Lizard entity from Enemy class
 */

public class Lizard extends Enemy {
    /**
     * Create a Lizard from Enemy class
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Lizard(World world, TextureAtlas screenAtlas,Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        this.attackSystem=new ArtificialMeleeFight(meleeDamage,this,world,this.attackSystem,attackAnimation,game);
        life=1;
    }

    public void throwBullet(){}

    /**
     * Import enity-specific animations from the Lizard atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("lucertola_walking"), 59, 6, 52, 34);
        standAnimation.flip(true, false);

        setBounds(0, 0, 24 / BitHeroes.PPM, 30 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = AppConstants.State.STAND;
        previousState = AppConstants.State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("lucertola_walking"), 3, -1, 52, 34));
        frames.add(new TextureRegion(atlas.findRegion("lucertola_walking"), 59, -1, 52, 34));
        frames.add(new TextureRegion(atlas.findRegion("lucertola_walking"), 122, -1, 52, 34));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("lucertola_attack"), 5, 4, 36, 47));
        frames.add(new TextureRegion(atlas.findRegion("lucertola_attack"), 47, 4, 36, 47));
        frames.add(new TextureRegion(atlas.findRegion("lucertola_attack"), 87, 4, 36, 47));

        attackAnimation = new Animation(0.1f, frames);
        frames.clear();

    }

}