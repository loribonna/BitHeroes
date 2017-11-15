package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;

/**
 * Abstract class with TileObject controls
 */

public abstract class TileObject extends Sprite{
    protected World world;
    protected Rectangle rect;
    protected Body body;
    protected Fixture fixture;
    protected Ellipse ell;
    protected BitHeroes game;
    protected Music music;

    /**
     * Create a TileObject with a rectangular shape
     * @param world World where to create the TileObject
     * @param rect Position and size of the TileObject in the world
     * @param game
     */
    public TileObject(World world, Rectangle rect,BitHeroes game){
        super();
        this.game=game;
        this.world = world;
        this.rect = rect;
        define();
        fixture.setUserData(this);
    }

    /**
     * Create a TileObject with a ellipse shape.
     * The define method must be overridden.
     * @param world World where to create the TileObject
     * @param ell Position and size of the TileObject in the world
     * @param game
     */
    public TileObject(World world, Ellipse ell,BitHeroes game){
        super();
        this.game=game;
        this.world = world;
        this.ell = ell;
        define();
        fixture.setUserData(this);
    }

    /**
     * Create body and set position on the current position rect.
     * Create fixture around the body.
     */
    public void define(){
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set((rect.getX()+rect.getWidth()/2) / BitHeroes.PPM,(rect.getY()+rect.getHeight()/2) / BitHeroes.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;

        body=world.createBody(bdef);

        shape.setAsBox((rect.getWidth()/2)/ BitHeroes.PPM,(rect.getHeight()/2)/ BitHeroes.PPM);
        fdef.shape=shape;
        fixture = body.createFixture(fdef);

    }

    protected void playSound(String name){
        if(!BitHeroes.disableAudio){
            try{
                music = game.getManager().get(name,Music.class);
                music.setLooping(false);
                music.setVolume(0.5f);
                music.play();
            }catch (Exception e){
                Gdx.app.log("Error","audio file not found");
            }
        }
    }

    /**
     * Update object frames and position of the TileObject
     */
    public abstract void update(float delta);

    /**
     * Set filter bits to trigger collisions with other fixtures.
     * @param filterBits
     */
    public void setCategoryBits(short filterBits){
        Filter filter = new Filter();
        filter.categoryBits = filterBits;
        filter.groupIndex = BitHeroes.GROUP_SCENERY;
        fixture.setFilterData(filter);
    }

    /**
     * Called from WorldContactListener when hit
     * @param entity Enemy or Player
     */
    public abstract void onHit(Entity entity);

    /**
     * Remove object and body
     */
    public void dispose(){
        game.removeObject(this);
        body.setUserData(true);
    }

}
