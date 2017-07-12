package com.my.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.MyGame;
import com.my.game.tools.Entity;
import com.my.game.tools.EntityInterface;
import com.sun.org.apache.xerces.internal.impl.dv.xs.AnyURIDV;

/**
 * Created by lorib on 09/05/2017.
 */

public class Archer extends Entity {

    public Archer(World w, TextureAtlas screenAtlas,Vector2 position) {
        super(w, screenAtlas,position);
    }

    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("archer_normal_attack"), 7, 6, 34, 32);
        setBounds(0, 0, 24 / MyGame.PPM, 20 / MyGame.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 23, 6, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 52, 6, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 90, 6, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 121, 6, 27, 32));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 7, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 45, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 84, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 123, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 164, 6, 34, 32));

        throwAnimation = new Animation(0.5f, frames);
        frames.clear();
    }

    @Override
    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    @Override
    public void recoil() {

    }

    @Override
    public void destroy() {

    }

    @Override
    protected void throwBullet() {

    }

    @Override
    protected FixtureDef createFrontAttackFixture() {
        return null;
    }

    @Override
    protected FixtureDef createBackAttackFixture() {
        return null;
    }

    public Filter getFilter() {
        Filter f = new Filter();
        f.categoryBits = MyGame.PLAYER_BIT;
        f.maskBits =(MyGame.DEFAULT_BIT | MyGame.BRICK_BIT | MyGame.COIN_BIT | MyGame.ENEMY_BIT | MyGame.VOID_BIT | MyGame.WALL_BIT);
        f.groupIndex = MyGame.GROUP_PLAYER;
        return f;
    }

    @Override
    public void createBorders() {
        BodyDef bdef= new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Filter filter = getFilter();

        fdef.filter.groupIndex=filter.groupIndex;
        fdef.filter.categoryBits= filter.categoryBits;
        fdef.filter.maskBits=filter.maskBits;

        CircleShape bShape = new CircleShape();
        bShape.setRadius(6/MyGame.PPM);
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData("good_body");

        EdgeShape front = new EdgeShape();
        front.set(new Vector2(6,6).scl(1/MyGame.PPM),new Vector2(6,-6).scl(1/MyGame.PPM));
        fdef.shape=front;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("good_front");

        EdgeShape back = new EdgeShape();
        back.set(new Vector2(-6,6).scl(1/MyGame.PPM),new Vector2(-6,-6).scl(1/MyGame.PPM));
        fdef.shape=back;
        body.createFixture(fdef).setUserData("good_back");

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-3,-6).scl(1/MyGame.PPM),new Vector2(3,-6).scl(1/MyGame.PPM));
        fdef.shape = feet;
        body.createFixture(fdef).setUserData("good_feet");

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2,7).scl(1/MyGame.PPM),new Vector2(2,7).scl(1/MyGame.PPM));
        fdef.shape = head;
        body.createFixture(fdef).setUserData("good_head");


    }

}