package com.my.game.sprites.TileObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.screens.PlayScreens.FirstLevel;
import com.my.game.screens.PlayScreens.SecondLevel;
import com.my.game.screens.PlayScreens.ThirdLevel;
import com.my.game.tools.Entity;
import com.my.game.tools.TileObject;

/**
 * Created by lorib on 10/06/2017.
 */

public class Exit extends TileObject {
    /**
     * Create a Exit TileObject
     * @param world
     * @param rect
     * @param game
     */
    public Exit(World world, Rectangle rect, MyGame game) {
        super(world, rect,game);
        setCategoryBits(MyGame.EXIT_BIT);
    }

    @Override
    public void update(float delta) {}

    /**
     * When the player touches the exit jumps to the next level
     * @param entity: Enemy or Player
     */
    @Override
    public void onHit(Entity entity) {
        if(game.getCurrentPlayScreen() instanceof FirstLevel){
            game.changeLevel(new SecondLevel(game,game.getCurrentPlayer(),game.getScore()));
        }
        else if(game.getCurrentPlayScreen() instanceof SecondLevel){
            game.changeLevel(new ThirdLevel(game,game.getCurrentPlayer(),game.getScore()));
        }
    }
}
