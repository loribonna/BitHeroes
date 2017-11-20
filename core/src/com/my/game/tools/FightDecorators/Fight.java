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
import com.my.game.tools.AppConstants;
import com.my.game.tools.AppConstants.AttackType;
import com.my.game.tools.AppConstants.Direction;
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
    protected HashMap<AttackType,Integer> damages;
    protected HashMap<AttackType,String> attackSounds;
    protected HashMap<AttackType,Float> attackRanges;
    protected Music music;
    protected BitHeroes game;
    protected Rectangle attackFixtureMargins;
    protected AppConstants.Float2 defaultSize;
    protected AppConstants.Float2 attackSize;
    protected boolean runAway=true;

    public static final float DEFAULT_RANGE=0.18f;

    private void initialize(){
        this.animations=new HashMap<AttackType, Animation>();
        this.damages=new HashMap<AttackType, Integer>();
        this.attackSounds=new HashMap<AttackType, String>();
        this.attackRanges=new HashMap<AttackType, Float>();
        this.attackFixtureMargins=new Rectangle(16,-4,2,-8);
    }

    public Fight(boolean isPlayer, Entity entity, World world, BitHeroes game, AppConstants.Float2 defaultSize){
        this.isPlayer=isPlayer;
        this.game=game;
        this.entity=entity;
        this.world=world;
        this.defaultSize=defaultSize;
        this.attackSize=defaultSize;
        initialize();
    }

    public Fight(boolean isPlayer, Entity entity, World world, BitHeroes game){
        this.isPlayer=isPlayer;
        this.game=game;
        this.entity=entity;
        this.world=world;
        initialize();
    }

    public void setRunAway(boolean value){
        this.runAway=value;
    }

    public void updateSize(AppConstants.Float2 attackSize){
        this.attackSize=attackSize;
    }

    public void updateSize(AppConstants.Float2 defaultSize, AppConstants.Float2 attackSize){
        this.defaultSize=defaultSize;
        this.attackSize=attackSize;
    }

    public void setAttackFixtureMargins(int x,int y,int width,int height){
        this.attackFixtureMargins=new Rectangle(x,y,width,height);
    }

    public void setAttackRange(AttackType type,float value){
        attackRanges.put(type,value);
    }

    public boolean isFighting(){
        return this.lockAttack;
    }

    public Direction setTarget(float targetDistanceX, float targetDistanceY){
        return Direction.NONE;
    }

    public void performAttack(AttackType attackType){}

    public void setAttackSound(AttackType type,String sound){
        this.attackSounds.put(type,sound);
    }

    protected void playAttackSound(AttackType type){
        if(!BitHeroes.disableAudio&&this.attackSounds.containsKey(type)){
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
            attack();
            playAttackSound(attackType);
        }else{
            return;
        }
    }

    protected abstract void attack();

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

    private FixtureDef createAttackFixture(boolean front){
        FixtureDef fdef = new FixtureDef();
        int rotation=front?1:-1;

        PolygonShape weaponFront = new PolygonShape();
        weaponFront.set(new Vector2[]{
                new Vector2(attackFixtureMargins.x,attackFixtureMargins.y+attackFixtureMargins.width).scl(1/ BitHeroes.PPM).scl(rotation),
                new Vector2(attackFixtureMargins.x*rotation,attackFixtureMargins.y).scl(1/ BitHeroes.PPM)
                ,new Vector2(attackFixtureMargins.x+attackFixtureMargins.height,(attackFixtureMargins.y+attackFixtureMargins.width)*rotation).scl(1/ BitHeroes.PPM),
                new Vector2(attackFixtureMargins.x+attackFixtureMargins.height,attackFixtureMargins.y).scl(1/ BitHeroes.PPM)});
        fdef.shape = weaponFront;
        getAttackFilterBits(fdef.filter);
        fdef.isSensor=true;
        return fdef;
    }

    public FixtureDef createFrontAttackFixture() {
        return createAttackFixture(true);
    }

    public FixtureDef createBackAttackFixture() {
        return createAttackFixture(false);
    }
}
