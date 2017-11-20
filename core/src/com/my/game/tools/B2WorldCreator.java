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
    private ArrayList<TileObject> animatedObjects;

    private void instantiateRectangleObjects(int layer,Class<? extends TileObject> tileObjectClass,boolean isAnimated){
        try{
            MapLayer l = map.getLayers().get(layer);
            Constructor<? extends TileObject> constructor = tileObjectClass.getConstructor(World.class, Rectangle.class, BitHeroes.class);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject)obj).getRectangle();
                TileObject instance = constructor.newInstance(world, rect,game);
                if(isAnimated){
                    this.animatedObjects.add(instance);
                }
            }
        }catch (Exception e){
            Gdx.app.log("Exception in tile loading",e.getMessage());
        }
    }

    private void instantiateEllipseObjects(int layer,Class<? extends TileObject> tileObjectClass,boolean isAnimated){
        try{
            MapLayer l = map.getLayers().get(layer);
            Constructor<? extends TileObject> constructor = tileObjectClass.getConstructor(World.class, Ellipse.class, BitHeroes.class);
            for (MapObject obj : l.getObjects().getByType(EllipseMapObject.class)) {
                Ellipse ell = ((EllipseMapObject)obj).getEllipse();
                TileObject instance = constructor.newInstance(world, ell,game);
                if(isAnimated){
                    this.animatedObjects.add(instance);
                }
            }
        }catch (Exception e){
            Gdx.app.log("Exception in tile loading",e.getMessage());
        }
    }

    private void istantiateFirstLevel(){
        instantiateRectangleObjects(2, Terrain.class,false);
        instantiateRectangleObjects(3, Void.class,false);
        instantiateEllipseObjects(6, Coin.class,true);
        instantiateRectangleObjects(7, Wall.class,false);
        instantiateRectangleObjects(4, Exit.class,false);
        instantiateRectangleObjects(5, Brick.class,false);
    }

    private void istantiateSecondLevel(){
        instantiateRectangleObjects(3, Terrain.class,false);
        instantiateRectangleObjects(2, Void.class,false);
        instantiateEllipseObjects(5, Coin.class,true);
        instantiateRectangleObjects(6, Wall.class,false);
        instantiateRectangleObjects(4, Exit.class,false);
    }

    private void istantiateThirdLevel(){
        instantiateRectangleObjects(1, Terrain.class,false);
        instantiateRectangleObjects(2, Wall.class,false);
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
        this.animatedObjects=animatedObjects;

        if (game.getCurrentPlayScreen() instanceof FirstLevel) {
            istantiateFirstLevel();
        }else if(game.getCurrentPlayScreen() instanceof SecondLevel){
            istantiateSecondLevel();
        }else if(game.getCurrentPlayScreen() instanceof ThirdLevel){
            istantiateThirdLevel();
        }else{
            throw new ExceptionInInitializerError();
        }
    }
}
