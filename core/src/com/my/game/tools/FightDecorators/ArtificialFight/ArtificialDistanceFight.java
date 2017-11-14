package com.my.game.tools.FightDecorators.ArtificialFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.AppConstants.AttackType;
import com.my.game.tools.AppConstants.Direction;
import com.my.game.tools.Bullet;
import com.my.game.tools.Entity;
import com.my.game.tools.FightDecorators.Fight;

import java.lang.reflect.Constructor;

/**
 * Created by lorib on 14/11/2017.
 */

public class ArtificialDistanceFight extends ArtificialFightDecorator  {
    private Class<? extends Bullet> bullet;

    public ArtificialDistanceFight(Entity entity, World world,Fight fight, Animation animation,BitHeroes game,Class<? extends Bullet> bullet){
        super(entity, world,game);
        this.fight=fight;
        if(!animations.containsKey(AttackType.DISTANCE)){
            animations.put(AppConstants.AttackType.DISTANCE,animation);
        }
        this.bullet=bullet;
    }

    public Direction setTarget(float targetDistanceX, float targetDistanceY){
        Direction returnDirection=fight.setTarget(targetDistanceX,targetDistanceY);

        if(returnDirection== Direction.NONE) {
            if (Math.abs(targetDistanceX) < Math.abs(attackRange)) {
                if (targetDistanceX < attackRange) {
                    throwAttack(AttackType.DISTANCE);
                    returnDirection = Direction.RIGHT;
                } else if (targetDistanceX > -attackRange) {
                    throwAttack(AttackType.DISTANCE);
                    returnDirection = Direction.LEFT;
                }
            }
        }

        return returnDirection;
    }

    @Override
    protected void distanceAttack(){
        entity.updateState(AppConstants.State.THROW,AppConstants.State.THROW);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
            }
        },animations.get(AttackType.DISTANCE).getAnimationDuration());

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                throwBullet();
            }
        },animations.get(AttackType.DISTANCE).getAnimationDuration()/2);
    }

    public void throwBullet() {
        try{
            Constructor<? extends Bullet> co = bullet.getConstructor(Vector2.class, World.class, boolean.class, boolean.class, BitHeroes.class);
            game.getCurrentPlayScreen().addBullet(co.newInstance(entity.getPosition(), world, entity.isFlipX(),false,game));
        }catch (Exception e){
            Gdx.app.log("Instantiating error",e.getMessage());
        }
    }
}