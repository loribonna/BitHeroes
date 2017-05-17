package com.my.game.tools;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.sprites.Brick;
import com.my.game.sprites.Coin;
import com.my.game.sprites.Terrain;
import com.my.game.sprites.Vuoto;

/**
 * Created by lorib on 09/05/2017.
 */

public class B2WorldCreator {

    /**
     * Apply BodyDefinitions and Fixtures to every layer in the current TiledMap to the current World.
     * @param world: Current world
     * @param map: Current map
     */
    public B2WorldCreator(World world, TiledMap map){
        MapLayer l = map.getLayers().get(2);
        for(MapObject obj : l.getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)obj).getRectangle();
            new Terrain(world,map,rect);
        }

        l = map.getLayers().get(3);
        for(MapObject obj : l.getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)obj).getRectangle();
            new Vuoto(world,map,rect);
        }

        //TODO: Crea uscita
        l = map.getLayers().get(4);
        for(MapObject obj : l.getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)obj).getRectangle();
           // new Uscita(world,map,rect);
        }

        l = map.getLayers().get(5);
        for(MapObject obj : l.getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)obj).getRectangle();
            new Brick(world,map,rect);
        }

    }
}
