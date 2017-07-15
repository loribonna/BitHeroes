package com.my.game.sprites.Players;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.my.game.MyGame;
import com.my.game.sprites.Throwables.Arrow;
import com.my.game.tools.Entity;

/**
 * Created by lorib on 09/05/2017.
 */

public class Archer extends Entity {

    public Archer(World w, TextureAtlas screenAtlas,Vector2 position,MyGame game) {
        super(w, screenAtlas,position,game);
        life=150;
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
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 23, 10, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 52, 10, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 90, 10, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 121, 10, 27, 32));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 7, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 45, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 84, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 123, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 164, 6, 34, 32));

        throwAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    @Override
    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    @Override
    public void recoil() {
        if(currentState!=State.JUMP) {
            body.applyLinearImpulse(new Vector2(0, 1), body.getWorldCenter(), true);
        }
    }

    @Override
    public void destroy() {
        game.getCurrentPlayScreen().gameOver();
    }

    protected void throwBullet() {
        game.getCurrentPlayScreen().addBullet(new Arrow(getPosition(), world, isFlipX(),true,game));
    }

    @Override
    protected FixtureDef createFrontAttackFixture() {
        return null;
    }

    @Override
    protected FixtureDef createBackAttackFixture() {
        return null;
    }

    @Override
    public Filter getFilter() {
        Filter f = new Filter();
        f.categoryBits = MyGame.PLAYER_BIT;
        f.maskBits =(MyGame.DEFAULT_BIT | MyGame.BRICK_BIT | MyGame.COIN_BIT | MyGame.ENEMY_BIT |
                MyGame.VOID_BIT | MyGame.WALL_BIT | MyGame.EXIT_BIT | MyGame.ENEMY_BULLET_BIT | MyGame.ENEMY_MELEE_BIT);
        f.groupIndex = MyGame.GROUP_PLAYER;
        return f;
    }

    @Override
    public void createBorders() {
        FixtureDef fdef = new FixtureDef();
        Filter filter = getFilter();

        fdef.filter.groupIndex=filter.groupIndex;
        fdef.filter.categoryBits= filter.categoryBits;
        fdef.filter.maskBits=filter.maskBits;

        CircleShape bShape = new CircleShape();
        bShape.setRadius(6/MyGame.PPM);
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-4,-6).scl(1/MyGame.PPM),new Vector2(4,-6).scl(1/MyGame.PPM));
        fdef.shape = feet;
        fdef.isSensor=true;
        body.createFixture(fdef).setUserData("good_feet");

    }

    @Override
    public void firstAttack() {
        currentState = State.THROW;
        previusState = State.THROW;
        stateTimer = 0;
        setRegion(getFrame(0));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
            }
        },throwAnimation.getAnimationDuration());

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                throwBullet();
            }
        },throwAnimation.getAnimationDuration()/2);

    }

    @Override
    public void secondAttack() {
        lockAttack=false;
    }

    @Override
    public void specialAttack() {
        lockAttack=false;
    }

}