package com.my.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.EllipseShapeBuilder;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.tools.TileObject;

import java.awt.geom.RectangularShape;

/**
 * Created by lorib on 13/05/2017.
 */

public class Coin extends TileObject{

    Animation gira;
    float stateTimer;
    TextureAtlas atl;
    public Coin(World world, TiledMap map, Ellipse ell) {
        super(world, map, ell);
        setCategoryBits(MyGame.COIN_BIT);
        //atl = new TextureAtlas("coin.pack");

    }



    @Override
    public void define(){
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        bdef.position.set((ell.x+ell.width/2) / MyGame.PPM,(ell.y+ell.height/2) / MyGame.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;

        body=world.createBody(bdef);
        fdef.isSensor=true;
        shape.setRadius((ell.width/2)/MyGame.PPM);
        fdef.shape=shape;
        fixture = body.createFixture(fdef);

    }

    @Override
    public void onHit() {
        Gdx.app.log("Head","Coin");
        setCategoryBits(MyGame.NOTHING_BIT);
        dispose();
    }


}
