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
import com.my.game.sprites.Throwables.FireBall;
import com.my.game.tools.Entity;

/**
 * Created by lorib on 13/07/2017.
 */

public class FireBender extends Entity {

    public FireBender(World w, TextureAtlas screenAtlas, Vector2 position,MyGame game) {
        super(w, screenAtlas,position,game);
    }

    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("ace_walking"), 0, 0, 27, 43);
        setBounds(0, 0, 24 / MyGame.PPM, 20 / MyGame.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("ace_walking"), 0, 0, 27, 43));
        frames.add(new TextureRegion(atlas.findRegion("ace_walking"), 30, 0, 27, 43));

        runAnimation = new Animation(0.15f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("ace_first_attack"), 6, 5, 42, 43));
        frames.add(new TextureRegion(atlas.findRegion("ace_first_attack"), 50, 5, 42, 43));
        frames.add(new TextureRegion(atlas.findRegion("ace_first_attack"), 99, 5, 42, 43));

        throwAnimation = new Animation(0.15f, frames);
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
        game.getCurrentPlayScreen().addBullet(new FireBall(getPosition(), world, isFlipX(),true,game));
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