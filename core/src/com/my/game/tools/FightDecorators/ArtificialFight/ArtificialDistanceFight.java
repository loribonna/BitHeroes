package com.my.game.tools.FightDecorators.ArtificialFight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.sprites.Throwables.BlobBall;
import com.my.game.tools.AppConstants;
import com.my.game.tools.AppConstants.AttackType;
import com.my.game.tools.AppConstants.Direction;
import com.my.game.tools.Entity;
import com.my.game.tools.FightDecorators.Fight;

/**
 * Created by lorib on 14/11/2017.
 */

public class ArtificialDistanceFight extends ArtificialFightDecorator  {
    private Fight fight;
    public ArtificialDistanceFight(Entity entity, World world,Fight fight, Animation animation,BitHeroes game){
        super(entity, world,game,animation);
        this.fight=fight;
    }

    public Direction setTarget(float targetDistanceX, float targetDistanceY){
        Direction returnDirection=fight.setTarget(targetDistanceX,targetDistanceY);

        if(returnDirection== Direction.NONE) {
            if (Math.abs(targetDistanceX) > Math.abs(minDistance)) {
                if (targetDistanceX > attackRange) {
                    throwAttack(AttackType.DISTANCE);
                    returnDirection = Direction.RIGHT;
                } else if (targetDistanceX < -attackRange) {
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
                entity.throwBullet();
            }
        },animations.get(AttackType.DISTANCE).getAnimationDuration()/2);
    }
}
