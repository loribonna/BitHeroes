package com.my.game.tools.FightDecorators.PlayerFight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.Entity;
import com.my.game.tools.FightDecorators.Fight;

/**
 * Created by lorib on 14/11/2017.
 */

public class PlayerFightMelee extends PlayerFightDecorator {
    public PlayerFightMelee(int damage, Entity entity, World world, Fight fight, Animation animation, BitHeroes game) {
        super(entity, world, game);
        this.fight = fight;
        if (!damages.containsKey(AppConstants.AttackType.MELEE)) {
            damages.put(AppConstants.AttackType.MELEE, damage);
        }
        if (!animations.containsKey(AppConstants.AttackType.MELEE)) {
            animations.put(AppConstants.AttackType.MELEE, animation);
        }
    }

    public PlayerFightMelee(int damage, Entity entity, World world, Fight fight, Animation animation, BitHeroes game, AppConstants.Float2 defaultSize) {
        super(entity, world, game,defaultSize);
        this.fight = fight;
        if (!damages.containsKey(AppConstants.AttackType.MELEE)) {
            damages.put(AppConstants.AttackType.MELEE, damage);
        }
        if (!animations.containsKey(AppConstants.AttackType.MELEE)) {
            animations.put(AppConstants.AttackType.MELEE, animation);
        }
    }

    public void performAttack(AppConstants.AttackType type){
        if(type== AppConstants.AttackType.MELEE){
            throwAttack(AppConstants.AttackType.MELEE);
        }
        else fight.performAttack(type);
    }

    @Override
    public void meleeAttack(){
        if(attackSize!=null&&defaultSize!=null){
            entity.updateSize(attackSize.x,attackSize.y);
        }
        entity.updateState(AppConstants.State.ATTACK,AppConstants.State.ATTACK);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
                if(defaultSize!=null){
                    entity.updateSize(defaultSize.x,defaultSize.y);
                }
            }
        },animations.get(AppConstants.AttackType.MELEE).getAnimationDuration());


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
                    f.setUserData(damages.get(AppConstants.AttackType.MELEE));

                } else {
                    final Fixture f = attackBody.createFixture(createFrontAttackFixture());
                    f.setUserData(damages.get(AppConstants.AttackType.MELEE));
                }
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        attackBody.setUserData(true);
                    }
                }, animations.get(AppConstants.AttackType.MELEE).getAnimationDuration() / 2);
            }
        }, animations.get(AppConstants.AttackType.MELEE).getAnimationDuration() / 2);
    }
}
