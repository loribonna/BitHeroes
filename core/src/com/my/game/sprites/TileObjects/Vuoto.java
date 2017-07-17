package com.my.game.sprites.TileObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.Entity;
import com.my.game.tools.TileObject;

/**
 * Created by lorib on 17/05/2017.
 */

public class Vuoto extends TileObject {
    /**
     * Create a Vuoto TileObject
     * @param world
     * @param rect
     * @param game
     */
    public Vuoto(World world, Rectangle rect,BitHeroes game) {
        super(world, rect,game);
        setCategoryBits(BitHeroes.VOID_BIT);
    }

    @Override
    public void update(float delta) {}

    /**
     * If the player touches the void gets game over.
     * If a enemy touches the void gets destroyed.
     * @param entity Enemy or Player
     */
    @Override
    public void onHit(Entity entity) {
        if(!entity.isPlayer()) {
            entity.destroy();
        }else{
            game.getCurrentPlayScreen().gameOver();
        }
    }
}
