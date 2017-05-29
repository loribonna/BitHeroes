package com.my.game.tools;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;

/**
 * Created by lorib on 13/05/2017.
 */

public abstract class TileObject implements TileObjectInterface {

    protected World world;
    protected TiledMap map;
    protected Rectangle rect;
    protected Body body;
    protected Fixture fixture;
    protected Ellipse ell;

    public TileObject(World world, TiledMap map,Rectangle rect){
        this.world = world;
        this.map= map;
        this.rect = rect;
        define();
        fixture.setUserData(this);
    }

    public TileObject(World world, TiledMap map,Ellipse ell){
        this.world = world;
        this.map= map;
        this.ell = ell;
        define();
        fixture.setUserData(this);
    }

    public void define(){
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set((rect.getX()+rect.getWidth()/2) / MyGame.PPM,(rect.getY()+rect.getHeight()/2) / MyGame.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;

        body=world.createBody(bdef);

        shape.setAsBox((rect.getWidth()/2)/MyGame.PPM,(rect.getHeight()/2)/MyGame.PPM);
        fdef.shape=shape;
        fixture = body.createFixture(fdef);

    }

    public void setCategoryBits(short filterBits){
        Filter filter = new Filter();
        filter.categoryBits = filterBits;
        filter.groupIndex = MyGame.GROUP_SCENERY;
        fixture.setFilterData(filter);
    }

    public abstract void onHit();


    /**
     * Get tile cell coordinate by scaling up and dividing by the tile size.
     * @return Tile Cell Coordinate.
     */
    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer l = (TiledMapTileLayer) map.getLayers().get(1);
        return l.getCell((int)(body.getPosition().x * MyGame.PPM / 16),
                (int)(body.getPosition().y * MyGame.PPM / 16));

    }

    public void dispose(){
       // map.dispose();
        fixture=null;
      //  body.destroyFixture(fixture);
    }

}
