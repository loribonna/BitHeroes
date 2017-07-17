package com.my.game.sprites.TileObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.Enemy;
import com.my.game.tools.Entity;
import com.my.game.tools.TileObject;

/**
 * Create a solid Terrain TileObject
 */

public class Terrain extends TileObject {
    /**
     * Create a Terrain TileObject
     * @param world
     * @param rect
     * @param game
     */
    public Terrain(World world, Rectangle rect, BitHeroes game) {
        super(world, rect,game);
        setCategoryBits(BitHeroes.DEFAULT_BIT);
    }

    @Override
    public void update(float delta) {}

    /**
     * If a Enemy hits the ground then can jump again.
     * @param entity Enemy or Player
     */
    @Override
    public void onHit(Entity entity) {
        if(!entity.isPlayer()) {
            ((Enemy) entity).setFlying(false);
        }
    }

}
