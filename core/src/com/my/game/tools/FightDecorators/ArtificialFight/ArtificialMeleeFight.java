package com.my.game.tools.FightDecorators.ArtificialFight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.AppConstants.Direction;
import com.my.game.tools.AppConstants.AttackType;
import com.my.game.tools.Entity;
import com.my.game.tools.FightDecorators.Fight;

/**
 * Created by lorib on 13/11/2017.
 */

public class ArtificialMeleeFight extends ArtificialFightDecorator {

    public ArtificialMeleeFight(int damage, Entity entity, World world, Fight fight, Animation animation, BitHeroes game){
        super(entity, world,game);
        this.fight=fight;
        if(!damages.containsKey(AppConstants.AttackType.MELEE)){
            damages.put(AppConstants.AttackType.MELEE,damage);
        }
        if(!animations.containsKey(AppConstants.AttackType.MELEE)){
            animations.put(AppConstants.AttackType.MELEE,animation);
        }
    }

    public Direction setTarget(float targetDistanceX,float targetDistanceY){
        Direction returnDirection=fight.setTarget(targetDistanceX,targetDistanceY);
        if(returnDirection==Direction.NONE){
            if(Math.abs(targetDistanceX)<Math.abs(attackRange)&&
                    targetDistanceY>-attackRange&&targetDistanceY<attackRange){

                returnDirection=Direction.STOP;
                throwAttack(AttackType.MELEE);
            }
        }

        return returnDirection;
    }

    @Override
    protected void meleeAttack(){
        entity.updateState(AppConstants.State.ATTACK,AppConstants.State.ATTACK);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
            }
        },animations.get(AttackType.MELEE).getAnimationDuration());


        final BodyDef bDef=new BodyDef();
        bDef.position.set(entity.getBody().getPosition());
        bDef.type = BodyDef.BodyType.DynamicBody;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                final Body attackBody = world.createBody(bDef);
                attackBody.setGravityScale(0);

                if (entity.isFlipX()) {
                    final Fixture f = attackBody.createFixture(createBackAttackFixture());
                    f.setUserData(damages.get(AttackType.MELEE));

                } else {
                    final Fixture f = attackBody.createFixture(createFrontAttackFixture());
                    f.setUserData(damages.get(AttackType.MELEE));
                }
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        attackBody.setUserData(true);
                    }
                }, animations.get(AttackType.MELEE).getAnimationDuration() / 2);
            }
        }, animations.get(AttackType.MELEE).getAnimationDuration() / 2);
    }

}
