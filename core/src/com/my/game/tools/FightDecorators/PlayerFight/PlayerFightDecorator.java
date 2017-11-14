package com.my.game.tools.FightDecorators.PlayerFight;

import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.Entity;
import com.my.game.tools.FightDecorators.Fight;

/**
 * Created by lorib on 14/11/2017.
 */

public abstract class PlayerFightDecorator extends Fight {
    public abstract void performAttack(AppConstants.AttackType type);

    public PlayerFightDecorator(Entity entity, World world, BitHeroes game, AppConstants.Float2 defaultSize){
        super(false, entity, world,game,defaultSize);
    }

    public PlayerFightDecorator(Entity entity, World world, BitHeroes game){
        super(false, entity, world,game);
    }

}
