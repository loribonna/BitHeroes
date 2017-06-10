package com.my.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.badlogic.gdx.utils.Array;
import com.my.game.MyGame;
import com.my.game.tools.Enemy;
import com.my.game.tools.Entity;
import com.my.game.tools.PlayScreen;
import com.my.game.tools.TileObject;

import java.awt.geom.RectangularShape;

/**
 * Created by lorib on 13/05/2017.
 */

public class Coin extends TileObject{
    Animation round;
    float stateTimer;
    TextureAtlas atl;
    public Coin(World world, TiledMap map, Ellipse ell) {
        super(world, map, ell);
        setCategoryBits(MyGame.COIN_BIT);
        // getAnimations(new TextureAtlas("coin.pack"));

    }

    public void getAnimations(TextureAtlas atlas){
        //TODO: missing animatinos for coin
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("coin").getTexture(), 0, 0, 14, 16));
        setBounds(0, 0, 16 / MyGame.PPM, 16 / MyGame.PPM);
        round = new Animation(0.5f, frames);
        stateTimer = 0;
    }

    public TextureRegion getFrame(){
        TextureRegion region;
        region = (TextureRegion)round.getKeyFrame(stateTimer, true);
        if(round.isAnimationFinished(stateTimer)) stateTimer=0;
        return region;
    }

    public void update(){
        //setRegion(getFrame());
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

    /**
     *
     * @param entity: If String is the contact point with player, else is Enemy.
     */
    @Override
    public void onHit(Object entity) {
        if(entity instanceof String) {
            Gdx.app.log("Head", "Coin");
            setCategoryBits(MyGame.NOTHING_BIT);
            PlayScreen.current.removeWithLock(this);
            dispose();
        }
    }


}
