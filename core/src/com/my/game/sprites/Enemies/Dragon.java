package com.my.game.sprites.Enemies;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.BitHeroes;
import com.my.game.screens.FinalScreen;
import com.my.game.sprites.Throwables.DragonBall;
import com.my.game.tools.AppConstants;
import com.my.game.tools.AppConstants.State;
import com.my.game.tools.Enemy;
import com.my.game.tools.FightDecorators.ArtificialFight.ArtificialDistanceFight;
import com.my.game.tools.FightDecorators.ArtificialFight.ArtificialMeleeFight;

/**
 * Create a Dragon entity from Enemy class
 */

public class Dragon extends Enemy {
    /**
     * Create a Dragon from Enemy class
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Dragon(World world, TextureAtlas screenAtlas, Vector2 position, BitHeroes game) {
        super(world, screenAtlas,position,game);
        life=200;
        maxMoveRange=100;
        disableJump=true;

        this.attackSystem=new ArtificialMeleeFight(35,this,world,this.attackSystem,this.attackAnimation,game);
        this.attackSystem=new ArtificialDistanceFight(this,world,this.attackSystem,this.throwAnimation,game,DragonBall.class);
        this.attackSystem.setAttackRange(AppConstants.AttackType.DISTANCE,maxMoveRange);
        this.attackSystem.setRunAway(false);
        this.attackSystem.setAttackSound(AppConstants.AttackType.MELEE,"sounds/ruggito.wav");
        this.attackSystem.setAttackSound(AppConstants.AttackType.DISTANCE,"sounds/ruggito.wav");
        this.attackSystem.setAttackFixtureMargins(25,-10,4,-17);

    }

    /**
     * Replace default to create bigger shape
     */
    @Override
    public void createBorders() {
        FixtureDef fdef = new FixtureDef();
        Filter filter = getFilter();

        fdef.filter.groupIndex=filter.groupIndex;
        fdef.filter.categoryBits= filter.categoryBits;
        fdef.filter.maskBits=filter.maskBits;

        PolygonShape bShape = new PolygonShape();
        bShape.set(new Vector2[]{
                new Vector2(-6,15).scl(1/ BitHeroes.PPM),
                new Vector2(6,15).scl(1/ BitHeroes.PPM),
                new Vector2(6,-15).scl(1/ BitHeroes.PPM),
                new Vector2(-6,-15).scl(1/ BitHeroes.PPM)
        });
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Replace default to jump to FinalScreen
     */
    @Override
    public void destroy(){
        dead=true;
        game.getCurrentPlayScreen().dispose();
        Screen screen = new FinalScreen(game);
        game.setScreen(screen);
        body.setUserData(new Boolean(true));
        game.removeObject(this);
    }

    /**
     * Import enity-specific animations from the Dragon atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("dragon_walking"),8,9, 136, 131);
        setBounds(0, 0, 100 / BitHeroes.PPM, 100 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previousState = State.STAND;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("dragon_walking"),8,2, 145, 131));
        frames.add(new TextureRegion(atlas.findRegion("dragon_walking"),  167, 2, 145, 131));
        frames.add(new TextureRegion(atlas.findRegion("dragon_walking"),  328, 2,145, 131));

        runAnimation = new Animation(0.2f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("dragon_second_attack"), 0, 3, 230, 140));
        frames.add(new TextureRegion(atlas.findRegion("dragon_second_attack"), 240, 3, 230, 140));
        frames.add(new TextureRegion(atlas.findRegion("dragon_second_attack"), 484, 3, 250, 140));
        frames.add(new TextureRegion(atlas.findRegion("dragon_second_attack"), 750, 3, 230, 140));
        attackAnimation = new Animation (0.2f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("dragon_first_attack"), 660, 20, 200, 110));
        frames.add(new TextureRegion(atlas.findRegion("dragon_first_attack"), 18, 20, 170, 110));
        frames.add(new TextureRegion(atlas.findRegion("dragon_first_attack"), 660, 20, 200, 110));
        throwAnimation = new Animation(0.2f,frames);
    }

}
