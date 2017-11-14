package com.my.game.tools.FightDecorators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.badlogic.gdx.audio.Music;
import com.my.game.tools.AppConstants.Direction;
import com.my.game.tools.AppConstants.AttackType;
import com.my.game.tools.Entity;

import java.awt.Rectangle;
import java.util.HashMap;

/**
 * Created by lorib on 13/11/2017.
 */

public abstract class Fight {
    private boolean isPlayer;
    protected boolean lockAttack;
    protected Entity entity;
    protected World world;
    protected HashMap<AttackType,Animation> animations;
    protected HashMap<AttackType,Float> damages;
    protected HashMap<AttackType,String> attackSounds;
    protected float minDistance=0.1f;
    protected float attackRange=0.18f;
    protected Music music;
    protected BitHeroes game;
    protected Rectangle attackFixtureMargins;

    public Fight(boolean isPlayer,Entity entity,World world,BitHeroes game){
        this.isPlayer=isPlayer;
        this.attackFixtureMargins=new Rectangle(16,-4,2,-8);
        this.game=game;
        this.entity=entity;
        this.world=world;
        this.animations=new HashMap<AttackType, Animation>();
        this.damages=new HashMap<AttackType, Float>();
        this.attackSounds=new HashMap<AttackType, String>();
    }

    public void setAttackFixtureMargins(int x,int y,int width,int height){
        this.attackFixtureMargins=new Rectangle(x,y,width,height);
    }

    public void setMinDistance(float value){
        this.minDistance=value;
    }

    public void setAttackRange(float value){
        this.attackRange=value;
    }

    public boolean isFighting(){
        return this.lockAttack;
    }

    public Direction setTarget(float targetDistanceX, float targetDistanceY){
        return Direction.NONE;
    }

    public void setAttackSound(AttackType type,String sound){
        if(!this.attackSounds.containsKey(type)){
            this.attackSounds.put(type,sound);
        }
    }

    protected void playAttackSound(AttackType type){
        if(this.attackSounds.containsKey(type)){
            try {
                music = game.getManager().get(this.attackSounds.get(type), Music.class);
                music.setLooping(false);
                music.setVolume(1);
                music.play();
            }catch (Exception e){
                Gdx.app.log("Error","audio file not found");
            }
        }
    }

    /**
     * Perform attack based on the attackType parameter.
     * Control if lockAttack is released before perform another attack.
     * @param attackType
     */
    protected void throwAttack(AttackType attackType) {
        if(!lockAttack) {
            lockAttack=true;
            if (attackType == AttackType.DISTANCE) {
                distanceAttack();
            } else if(attackType==AttackType.MELEE){
                meleeAttack();
            } else if(attackType==AttackType.SPECIAL){
                specialAttack();
            } else return;
            playAttackSound(attackType);
        }else{
            return;
        }
    }

    /**
     * Perform entity melee attack. Default is nothing.
     */
    protected void meleeAttack(){
        lockAttack=false;
    }

    /**
     * Perform entity distance attack. Default is nothing.
     */
    protected void distanceAttack(){
        lockAttack=false;
    }

    /**
     * Perform entity's special attack. Default is nothing.
     */
    protected void specialAttack(){
        lockAttack=false;
    }

    private void getAttackFilterBits(Filter filter){
        if(isPlayer){
            filter.categoryBits= BitHeroes.PLAYER_MELEE_BIT;
            filter.groupIndex= BitHeroes.GROUP_BULLET;
            filter.maskBits= BitHeroes.ENEMY_BIT;
        }else{
            filter.categoryBits= BitHeroes.ENEMY_MELEE_BIT;
            filter.groupIndex= BitHeroes.GROUP_BULLET;
            filter.maskBits= BitHeroes.PLAYER_BIT;
        }
    }

    public FixtureDef createFrontAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponFront = new PolygonShape();
        weaponFront.set(new Vector2[]{
                new Vector2(attackFixtureMargins.x,attackFixtureMargins.y+attackFixtureMargins.width).scl(1/ BitHeroes.PPM),
                new Vector2(attackFixtureMargins.x,attackFixtureMargins.y).scl(1/ BitHeroes.PPM)
                ,new Vector2(attackFixtureMargins.x+attackFixtureMargins.height,attackFixtureMargins.y+attackFixtureMargins.width).scl(1/ BitHeroes.PPM),
                new Vector2(attackFixtureMargins.x+attackFixtureMargins.height,attackFixtureMargins.y).scl(1/ BitHeroes.PPM)});
        fdef.shape = weaponFront;
        getAttackFilterBits(fdef.filter);
        fdef.isSensor=true;
        return fdef;
    }

    public FixtureDef createBackAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponBack = new PolygonShape();
        weaponBack.set(new Vector2[]{new Vector2(-16,-2).scl(1/ BitHeroes.PPM),new Vector2(-16,-4).scl(1/ BitHeroes.PPM)
                ,new Vector2(-8,-2).scl(1/ BitHeroes.PPM),new Vector2(-8,-4).scl(1/ BitHeroes.PPM)});
        fdef.shape = weaponBack;
        getAttackFilterBits(fdef.filter);
        fdef.isSensor=true;
        return fdef;
    }
}
