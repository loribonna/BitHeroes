package com.my.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;

/**
 * Created by lorib on 09/05/2017.
 */

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body b;

        for(MapObject obj : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2) / MyGame.PPM,(rect.getY()+rect.getHeight()/2) / MyGame.PPM);

            b=world.createBody(bdef);
            shape.setAsBox((rect.getWidth()/2) / MyGame.PPM,(rect.getHeight()/2) / MyGame.PPM);
            fdef.shape = shape;
            b.createFixture(fdef);

        }

    }
}
