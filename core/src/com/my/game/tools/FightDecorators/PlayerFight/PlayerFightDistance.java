package com.my.game.tools.FightDecorators.PlayerFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.Bullet;
import com.my.game.tools.Entity;
import com.my.game.tools.FightDecorators.Fight;

import java.lang.reflect.Constructor;

/**
 * Created by lorib on 14/11/2017.
 */

public class PlayerFightDistance extends PlayerFightDecorator {
    private Class<? extends Bullet> bulletType;

    public PlayerFightDistance(Entity entity, World world,Fight fight, Animation animation,BitHeroes game,Class<? extends Bullet> bulletType){
        super(entity, world,game,fight);
        if(!animations.containsKey(AppConstants.AttackType.DISTANCE)){
            animations.put(AppConstants.AttackType.DISTANCE,animation);
        }
        this.bulletType=bulletType;
    }

    public void performAttack(AppConstants.AttackType attackType){
        if(attackType== AppConstants.AttackType.DISTANCE){
            throwAttack(AppConstants.AttackType.DISTANCE);
        }
        else fight.performAttack(attackType);
    }

    protected void attack(){
        entity.updateState(AppConstants.State.THROW,AppConstants.State.THROW);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
            }
        },animations.get(AppConstants.AttackType.DISTANCE).getAnimationDuration());

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                throwBullet();
            }
        },animations.get(AppConstants.AttackType.DISTANCE).getAnimationDuration()/2);
    }

    private void throwBullet() {
        try{
            Constructor<? extends Bullet> co = bulletType.getConstructor(Vector2.class, World.class, boolean.class, boolean.class, BitHeroes.class);
            game.getCurrentPlayScreen().addBullet(co.newInstance(entity.getPosition(), world, entity.isFlipX(),true,game));
        }catch (Exception e){
            Gdx.app.log("Instantiating error",e.getMessage());
        }
    }

}
