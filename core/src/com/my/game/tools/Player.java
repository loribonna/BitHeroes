package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.FightDecorators.DefaultFight;

/**
 * Created by lorib on 14/11/2017.
 */

public abstract class Player extends Entity {
    protected Player(World world, TextureAtlas screenAtlas, Vector2 position, BitHeroes game){
        super(world, screenAtlas, position, game);
        isPlayer=true;
        this.attackSystem=new DefaultFight(isPlayer,this,world,game);
    }

    public void attack(AppConstants.AttackType type){
        this.attackSystem.performAttack(type);
    }

    /**
     * Update current state and animations.
     * @param delta
     */
    @Override
    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    /**
     * Perform shake movement after hit.
     */
    @Override
    public void recoil() {
        if(currentState!= AppConstants.State.JUMP) {
            body.applyLinearImpulse(new Vector2(0, 1), body.getWorldCenter(), true);
        }
    }

    /**
     * If the player gets destroyed the game is over
     */
    @Override
    public void destroy() {
        try{
            music = game.getManager().get("sounds/morte.wav",Music.class);
            music.setLooping(false);
            music.setVolume(1);
            music.play();
        }catch (Exception e){
            Gdx.app.log("Error","audio file not found");
        }
        game.getCurrentPlayScreen().gameOver();
    }

    /**
     * @return player filter bits to set collisions.
     */
    @Override
    public Filter getFilter() {
        Filter f = new Filter();
        f.categoryBits = BitHeroes.PLAYER_BIT;
        f.maskBits =(BitHeroes.DEFAULT_BIT | BitHeroes.BRICK_BIT | BitHeroes.COIN_BIT | BitHeroes.ENEMY_BIT |
                BitHeroes.VOID_BIT | BitHeroes.WALL_BIT | BitHeroes.EXIT_BIT | BitHeroes.ENEMY_BULLET_BIT | BitHeroes.ENEMY_MELEE_BIT);
        f.groupIndex = BitHeroes.GROUP_PLAYER;
        return f;
    }

    @Override
    public void createBorders() {
        FixtureDef fdef = new FixtureDef();
        Filter filter = getFilter();

        fdef.filter.groupIndex=filter.groupIndex;
        fdef.filter.categoryBits= filter.categoryBits;
        fdef.filter.maskBits=filter.maskBits;

        CircleShape bShape = new CircleShape();
        bShape.setRadius(6/ BitHeroes.PPM);
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-4,-6).scl(1/ BitHeroes.PPM),new Vector2(4,-6).scl(1/ BitHeroes.PPM));
        fdef.shape = feet;
        fdef.isSensor=true;
        body.createFixture(fdef).setUserData("good_feet");
    }
}
