package com.my.game.tools.FightDecorators;

import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.Entity;

/**
 * Created by lorib on 13/11/2017.
 */

public class DefaultFight extends Fight {

    public DefaultFight(boolean isPlayer, Entity entity, World world, BitHeroes game){
        super(isPlayer, entity, world,game);

    }

    protected void attack(){
        lockAttack=false;
    }

}
