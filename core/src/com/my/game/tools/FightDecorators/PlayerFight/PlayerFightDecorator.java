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
    public abstract void performAttack(AppConstants.AttackType attackType);
    protected Fight fight;

    public PlayerFightDecorator(Entity entity, World world, BitHeroes game, AppConstants.Float2 defaultSize,Fight fight){
        super(true, entity, world,game,defaultSize);
        this.fight=fight;
    }

    public PlayerFightDecorator(Entity entity, World world, BitHeroes game,Fight fight){
        super(true, entity, world,game);
        this.fight=fight;
    }

}
