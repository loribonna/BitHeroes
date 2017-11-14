package com.my.game.tools.FightDecorators.ArtificialFight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.Entity;
import com.my.game.tools.FightDecorators.DefaultFight;

/**
 * Created by lorib on 13/11/2017.
 */

public abstract class ArtificialFightDecorator extends DefaultFight {
    public abstract AppConstants.Direction setTarget(float targetDistanceX, float targetDistanceY);

    public ArtificialFightDecorator(Entity entity, World world, BitHeroes game, Animation animation){
        super(false, entity, world,game);
        if(!animations.containsKey(AppConstants.AttackType.MELEE)){
            animations.put(AppConstants.AttackType.MELEE,animation);
        }
    }
}
