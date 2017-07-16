package com.my.game.sprites.TileObjects;

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
    private Animation round;
    private float stateTimer;
    private TextureAtlas atl;

    /**
     * Create a Coin TileObject
     * @param world
     * @param ell
     * @param game
     */
    public Coin(World world, Ellipse ell,MyGame game) {
        super(world, ell,game);
        setCategoryBits(MyGame.COIN_BIT);
        atl=new TextureAtlas("coinP/coin.pack");
        getAnimations(atl);
    }

    /**
     * Import coin-specific animations from the Coin atlas.
     * @param atlas
     */
    public void getAnimations(TextureAtlas atlas){
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("skullcoin_b1"), 1, 3, 19, 20));
        frames.add(new TextureRegion(atlas.findRegion("skullcoin_b1"), 35, 25, 19, 20));
        frames.add(new TextureRegion(atlas.findRegion("skullcoin_b1"), 1, 25, 19, 20));
        frames.add(new TextureRegion(atlas.findRegion("skullcoin_b1"), 35, 25, 19, 20));
        setBounds(0, 0, 16 / MyGame.PPM, 16 / MyGame.PPM);
        round = new Animation(0.3f, frames);
        stateTimer = 0;
    }

    /**
     * @param dt
     * @return current displaying frame for the given delta time
     */
    public TextureRegion getFrame(float dt){
        TextureRegion region;
        region = (TextureRegion)round.getKeyFrame(stateTimer, true);

        stateTimer = stateTimer + dt;
        return region;
    }

    /**
     * Update position and animation of the Coin
     * @param delta
     */
    public void update(float delta){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    /**
     * Create body and shape with the fixture to collide with the player
     */
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
     * Increase the coin count in the hud when the player touches the coin.
     * Destroy current coin object
     * @param entity
     */
    @Override
    public void onHit(Entity entity) {
        if(entity.isPlayer()) {
            setCategoryBits(MyGame.NOTHING_BIT);
            game.removeObject(this);
            body.setUserData(true);
            game.getCurrentPlayScreen().addCoin();
            dispose();
        }
    }


}
