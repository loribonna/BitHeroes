package com.my.game.sprites;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.MyGame;
import com.my.game.tools.Entity;
import com.my.game.tools.EntityInterface;
import com.my.game.tools.PlayScreen;


/**
 * Created by lorib on 09/05/2017.
 */

public class Warrior extends Entity {

    public Warrior(World w, TextureAtlas screenAtlas,Vector2 position) {
        super(w, screenAtlas,position);
    }

    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("warrior_idle").getTexture(), 0, 0, 14, 16);
        setBounds(0, 0, 16 / MyGame.PPM, 16 / MyGame.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        int i = 0;
        for (; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("warrior_walk"), i * 16, 0, 14, 16));
            i++;
        }
        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("warrior_attack"), 0, 0, 27, 16));
        frames.add(new TextureRegion(atlas.findRegion("warrior_attack"), 27, 0, 27, 16));
        attackAnimation = new Animation(0.5f, frames);
        frames.clear();
    }

    @Override
    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    @Override
    public void recoil() {
        body.applyLinearImpulse(new Vector2(0,1),body.getWorldCenter(),true);
    }

    @Override
    public void destroy() {
        PlayScreen.current.gameOver();
    }

    public Filter getFilter() {
        Filter f = new Filter();
        f.categoryBits = MyGame.PLAYER_BIT;
        f.maskBits =(MyGame.DEFAULT_BIT | MyGame.BRICK_BIT | MyGame.COIN_BIT | MyGame.ENEMY_BIT |
                MyGame.VOID_BIT | MyGame.WALL_BIT | MyGame.EXIT_BIT | MyGame.ENEMY_BULLET_BIT);
        f.groupIndex = MyGame.GROUP_PLAYER;
        return f;
    }

    @Override
    public void createBorders(Vector2 position) {
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
        front.set(new Vector2(6,4).scl(1/MyGame.PPM),new Vector2(6,-4).scl(1/MyGame.PPM));
        fdef.shape=front;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("good_front");

        EdgeShape back = new EdgeShape();
        back.set(new Vector2(-6,4).scl(1/MyGame.PPM),new Vector2(-6,-4).scl(1/MyGame.PPM));
        fdef.shape=back;
        body.createFixture(fdef).setUserData("good_back");

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-4,-6).scl(1/MyGame.PPM),new Vector2(4,-6).scl(1/MyGame.PPM));
        fdef.shape = feet;
        body.createFixture(fdef).setUserData("good_feet");

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2,7).scl(1/MyGame.PPM),new Vector2(2,7).scl(1/MyGame.PPM));
        fdef.shape = head;
        body.createFixture(fdef).setUserData("good_head");
    }

    @Override
    protected void throwBullet() {
        PlayScreen.current.addBullet(new Arrow(getPosition(), world, isFlipX(),true));
    }

    @Override
    protected Fixture createFrontAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponFront = new PolygonShape();
        weaponFront.set(new Vector2[]{new Vector2(12,-2).scl(1/MyGame.PPM),new Vector2(12,-4).scl(1/MyGame.PPM)
                ,new Vector2(8,-2).scl(1/MyGame.PPM),new Vector2(8,-4).scl(1/MyGame.PPM)});
        fdef.shape = weaponFront;
        fdef.filter.categoryBits=MyGame.PLAYER_MELEE_BIT;
        fdef.filter.groupIndex=MyGame.GROUP_BULLET;
        fdef.filter.maskBits=MyGame.ENEMY_BIT;
        fdef.isSensor=true;
        Fixture frontAttack=body.createFixture(fdef);
        frontAttack.setUserData(20);
        return frontAttack;
    }

    @Override
    protected Fixture createBackAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponBack = new PolygonShape();
        weaponBack.set(new Vector2[]{new Vector2(-12,-2).scl(1/MyGame.PPM),new Vector2(-12,-4).scl(1/MyGame.PPM)
                ,new Vector2(-8,-2).scl(1/MyGame.PPM),new Vector2(-8,-4).scl(1/MyGame.PPM)});
        fdef.shape = weaponBack;
        fdef.filter.categoryBits=MyGame.PLAYER_MELEE_BIT;
        fdef.filter.groupIndex=MyGame.GROUP_BULLET;
        fdef.filter.maskBits=MyGame.ENEMY_BIT;
        fdef.isSensor=true;
        Fixture backAttack=body.createFixture(fdef);
        backAttack.setUserData(20);
        return backAttack;
    }


}
