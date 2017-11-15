package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.screens.PlayScreens.FirstLevel;
import com.my.game.screens.PlayScreens.SecondLevel;
import com.my.game.screens.PlayScreens.ThirdLevel;
import com.my.game.sprites.TileObjects.Brick;
import com.my.game.sprites.TileObjects.Coin;
import com.my.game.sprites.TileObjects.Exit;
import com.my.game.sprites.TileObjects.Terrain;
import com.my.game.sprites.TileObjects.Void;
import com.my.game.sprites.TileObjects.Wall;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Create the world's TileObjects
 */

public class B2WorldCreator {

    private World world;
    private TiledMap map;
    private BitHeroes game;

    private void instantiateRectangleObjects(int layer,Class<? extends TileObject> tileObjectClass){
        try{
            MapLayer l = map.getLayers().get(layer);
            Constructor<? extends TileObject> constructor = tileObjectClass.getConstructor(World.class, Rectangle.class, BitHeroes.class);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject)obj).getRectangle();
                constructor.newInstance(world, rect,game);
            }
        }catch (Exception e){
            Gdx.app.log("Exception in tile loading",e.getMessage());
        }
    }

    private void instantiateEllipseObjects(int layer,Class<? extends TileObject> tileObjectClass){
        try{
            MapLayer l = map.getLayers().get(layer);
            Constructor<? extends TileObject> constructor = tileObjectClass.getConstructor(World.class, Ellipse.class, BitHeroes.class);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject)obj).getRectangle();
                constructor.newInstance(world, rect,game);
            }
        }catch (Exception e){
            Gdx.app.log("Exception in tile loading",e.getMessage());
        }
    }

    /**
     * Create a TileObject to every object and layer in the current TiledMap of the current World.
     * Every level map has different map structure.
     * @param world
     * @param map
     */
    public B2WorldCreator(World world, TiledMap map, ArrayList<TileObject> animatedObjects,BitHeroes game) {
        this.game=game;
        this.map=map;
        this.world=world;

        if (game.getCurrentPlayScreen() instanceof FirstLevel) {
            instantiateRectangleObjects(2, Terrain.class);
            instantiateRectangleObjects(3, Void.class);
            instantiateEllipseObjects(6, Coin.class);
            instantiateRectangleObjects(7, Wall.class);
            instantiateRectangleObjects(4, Exit.class);
            instantiateRectangleObjects(5, Brick.class);
        }else if(game.getCurrentPlayScreen() instanceof SecondLevel){
            instantiateRectangleObjects(3, Terrain.class);
            instantiateRectangleObjects(2, Void.class);
            instantiateEllipseObjects(5, Coin.class);
            instantiateRectangleObjects(6, Wall.class);
            instantiateRectangleObjects(4, Exit.class);
        }else{
            assert (game.getCurrentPlayScreen() instanceof ThirdLevel) : "Level unknown";
            instantiateRectangleObjects(1, Terrain.class);
            instantiateRectangleObjects(2, Wall.class);
        }
    }
}
