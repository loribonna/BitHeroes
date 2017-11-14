package com.my.game.sprites.Players;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.BitHeroes;
import com.my.game.sprites.Throwables.FireBall;
import com.my.game.tools.AppConstants;
import com.my.game.tools.FightDecorators.PlayerFight.PlayerFightDistance;
import com.my.game.tools.Player;

/**
 * Create a playable FireBender entity
 */

public class FireBender extends Player {

    /**
     * Create a FireBender Entity with player controls
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public FireBender(World world, TextureAtlas screenAtlas, Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        this.attackSystem=new PlayerFightDistance(this,world,this.attackSystem,throwAnimation,game,FireBall.class);
        this.attackSystem.setAttackSound(AppConstants.AttackType.DISTANCE,"sounds/attaccofuoco.wav");
        life=150;
    }

    /**
     * Import enity-specific animations from the FireBender atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("ace_walking"), 0, 0, 27, 43);
        setBounds(0, 0, 24 / BitHeroes.PPM, 20 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = AppConstants.State.STAND;
        previousState = AppConstants.State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("ace_walking"), 0, 0, 27, 43));
        frames.add(new TextureRegion(atlas.findRegion("ace_walking"), 30, 0, 27, 43));

        runAnimation = new Animation(0.15f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("ace_first_attack"), 6, 5, 42, 43));
        frames.add(new TextureRegion(atlas.findRegion("ace_first_attack"), 50, 5, 42, 43));
        frames.add(new TextureRegion(atlas.findRegion("ace_first_attack"), 99, 5, 42, 43));

        throwAnimation = new Animation(0.15f, frames);
        frames.clear();
    }
}
