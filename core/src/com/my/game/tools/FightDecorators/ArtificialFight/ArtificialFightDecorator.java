package com.my.game.tools.FightDecorators.ArtificialFight;

import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.Entity;
import com.my.game.tools.FightDecorators.Fight;

/**
 * Created by lorib on 13/11/2017.
 */

public abstract class ArtificialFightDecorator extends Fight {
    public abstract AppConstants.Direction setTarget(float targetDistanceX, float targetDistanceY);
    protected Fight fight;

    public ArtificialFightDecorator(Entity entity, World world, BitHeroes game,Fight fight){
        super(false, entity, world,game);
        this.fight=fight;
    }

    public ArtificialFightDecorator(Entity entity, World world, BitHeroes game, AppConstants.Float2 defaultSize,Fight fight){
        super(false, entity, world,game,defaultSize);
        this.fight=fight;
    }
}
