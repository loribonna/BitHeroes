package com.my.game.tools;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.screens.PlayScreens.FirstLevel;
import com.my.game.screens.PlayScreens.SecondLevel;

import java.util.ArrayList;

/**
 * Created by lorib on 09/05/2017.
 */

public class B2WorldCreator {

    /**
     * Apply BodyDefinitions and Fixtures to every layer in the current TiledMap to the current World.
     * @param world: Current world
     * @param map: Current map
     */
    public B2WorldCreator(World world, TiledMap map, ArrayList<TileObject> animatedObjects,MyGame game) {
        if (game.getCurrentPlayScreen() instanceof FirstLevel) {
            MapLayer l = map.getLayers().get(2);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Terrain(world, map, rect,game);
            }

            l = map.getLayers().get(3);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Vuoto(world, map, rect,game);
            }

            l = map.getLayers().get(6);
            for (MapObject obj : l.getObjects().getByType(EllipseMapObject.class)) {
                Ellipse ell = ((EllipseMapObject) obj).getEllipse();
                animatedObjects.add(new com.my.game.sprites.TileObjects.Coin(world, map, ell,game));
            }

            l = map.getLayers().get(7);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Wall(world, map, rect,game);
            }

            l = map.getLayers().get(4);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Exit(world, map, rect,game);
            }

            l = map.getLayers().get(5);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Brick(world, map, rect,game);
            }
        }else if(game.getCurrentPlayScreen() instanceof SecondLevel){
            MapLayer l = map.getLayers().get(3);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Terrain(world, map, rect,game);
            }

            l = map.getLayers().get(2);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Vuoto(world, map, rect,game);
            }

            l = map.getLayers().get(5);
            for (MapObject obj : l.getObjects().getByType(EllipseMapObject.class)) {
                Ellipse ell = ((EllipseMapObject) obj).getEllipse();
                animatedObjects.add(new com.my.game.sprites.TileObjects.Coin(world, map, ell,game));
            }

            l = map.getLayers().get(6);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Wall(world, map, rect,game);
            }

            l = map.getLayers().get(4);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Exit(world, map, rect,game);
            }
        }else{
            MapLayer l = map.getLayers().get(1);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Terrain(world, map, rect,game);
            }

            l = map.getLayers().get(2);
            for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                new com.my.game.sprites.TileObjects.Wall(world, map, rect,game);
            }


        }
    }
}
