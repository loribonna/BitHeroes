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
 * Create a Orch entity from Enemy class
 */

public class Orch extends Enemy {
    /**
     * Create a Orch from Enemy class
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Orch(World world, TextureAtlas screenAtlas,Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        this.attackSystem=new ArtificialMeleeFight(20,this,world,this.attackSystem,attackAnimation,game);
        life=1;
    }

    /**
     * Import enity-specific animations from the Orch atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("orc_walking"), 371-264, 6, 50, 82);
        setBounds(0, 0, 24 / BitHeroes.PPM, 30 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = AppConstants.State.ATTACK.STAND;
        previousState = AppConstants.State.STAND;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("orc_walking"),  261-264, 6, 50, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_walking"),  316-264, 6, 50, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_walking"),  371-264, 6, 50, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_walking"),  427-264, 6, 50, 82));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("orc_attack"), 2, 8, 80, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_attack"), 95, 8, 80, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_attack"), 177, 8, 80 , 82));
        attackAnimation = new Animation (0.2f, frames);
        frames.clear();
    }

}
