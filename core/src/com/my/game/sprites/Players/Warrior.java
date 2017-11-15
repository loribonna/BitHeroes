package com.my.game.sprites.Players;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.FightDecorators.PlayerFight.PlayerFightMelee;
import com.my.game.tools.Player;


/**
 * Create a playable Warrior entity
 */

public class Warrior extends Player {

    /**
     * Create a Archer Warrior with player controls
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Warrior(World world, TextureAtlas screenAtlas,Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        this.attackSystem=new PlayerFightMelee(20,this,world,this.attackSystem,attackAnimation,game);
        this.attackSystem.updateSize(new AppConstants.Float2(16,16),new AppConstants.Float2(27,16));
        this.attackSystem.setAttackSound(AppConstants.AttackType.MELEE,"sounds/spadata.mp3");
        life=480;
    }

    /**
     * Import enity-specific animations from the Warrior atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("warrior_idle"), 0, 0, 14, 16);
        setBounds(0, 0, 16 / BitHeroes.PPM, 16 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = AppConstants.State.STAND;
        previousState = AppConstants.State.STAND;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        int i = 0;
        for (; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("warrior_walk"), i * 16, 0, 14, 16));
            i++;
        }
        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("warrior_attack"), 0, 0, 27, 16));
        frames.add(new TextureRegion(atlas.findRegion("warrior_attack"), 27, 0, 27, 16));
        attackAnimation = new Animation(0.5f, frames);
        frames.clear();
    }

}
