package com.my.game.sprites.TileObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.Entity;
import com.my.game.tools.TileObject;

/**
 * Create a solid Wall TileObject
 */

public class Wall extends TileObject {
    /**
     * Create a Wall TileObject
     * @param world
     * @param rect
     * @param game
     */
    public Wall(World world, Rectangle rect,BitHeroes game) {
        super(world, rect,game);
        setCategoryBits(BitHeroes.WALL_BIT);

    }

    @Override
    public void update(float delta) {}

    @Override
    public void onHit(Entity entity) {}
}
