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
 * Create a Mummy entity from Enemy class
 */

public class Mummy extends Enemy {
    /**
     * Create a Mummy from Enemy class
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Mummy(World world, TextureAtlas screenAtlas, Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        this.attackSystem=new ArtificialMeleeFight(20,this,world,this.attackSystem,attackAnimation,game);
        life=1;
    }

    /**
     * Import enity-specific animations from the Mummy atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("mummia_walking"), 34,9, 31, 36);
        setBounds(0, 0, 24 / BitHeroes.PPM, 30 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = AppConstants.State.STAND;
        previousState = AppConstants.State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("mummia_walking"),  2, 9, 31, 36));
        frames.add(new TextureRegion(atlas.findRegion("mummia_walking"),  34,9, 31, 36));
        frames.add(new TextureRegion(atlas.findRegion("mummia_walking"),  66, 9, 31, 36));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("mummia_attack"), 4, 3, 35, 34));
        frames.add(new TextureRegion(atlas.findRegion("mummia_attack"), 33, 3, 35, 34));
        frames.add(new TextureRegion(atlas.findRegion("mummia_attack"), 63,3, 35, 34));
        frames.add(new TextureRegion(atlas.findRegion("mummia_attack"), 102,3, 35 , 34));
        attackAnimation = new Animation (0.1f, frames);
        frames.clear();
    }
}

