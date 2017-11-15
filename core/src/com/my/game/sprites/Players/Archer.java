package com.my.game.sprites.Players;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.BitHeroes;
import com.my.game.sprites.Throwables.Arrow;
import com.my.game.tools.AppConstants;
import com.my.game.tools.FightDecorators.PlayerFight.PlayerFightDistance;
import com.my.game.tools.Player;

/**
 * Create a playable Archer entity
 */

public class Archer extends Player {

    /**
     * Create a Archer Entity with player controls
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Archer(World world, TextureAtlas screenAtlas,Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        this.attackSystem=new PlayerFightDistance(this,world,this.attackSystem,throwAnimation,game,Arrow.class);
        this.attackSystem.setAttackSound(AppConstants.AttackType.DISTANCE,"sounds/freccia.wav");
        life=150;
    }

    /**
     * Import enity-specific animations from the Archer atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("archer_normal_attack"), 7, 6, 34, 32);
        setBounds(0, 0, 24 / BitHeroes.PPM, 20 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = AppConstants.State.STAND;
        previousState = AppConstants.State.STAND;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 23, 10, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 52, 10, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 90, 10, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 121, 10, 27, 32));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 7, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 45, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 84, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 123, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 164, 6, 34, 32));

        throwAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

}