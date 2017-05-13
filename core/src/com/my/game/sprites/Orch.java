package com.my.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.MyGame;
import com.my.game.screens.PlayScreen;
import com.my.game.tools.*;

/**
 * Created by lorib on 11/05/2017.
 */

public class Orch extends Entity {
    public Orch(World w, PlayScreen screen,Vector2 position) {
        super(w, screen,position);
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
    public State getState(){
        return State.STAND;
    }

    public Filter getFilter() {
        Filter f = new Filter();
        f.categoryBits = MyGame.ENEMY_BIT;
        f.maskBits =(MyGame.DEFAULT_BIT | MyGame.BRICK_BIT | MyGame.COIN_BIT | MyGame.PLAYER_BIT);
        f.groupIndex = MyGame.GROUP_ENEMIES;
        return f;
    }

    @Override
    public void createBorders(Vector2 position) {
        FixtureDef fdef = new FixtureDef();
        Filter filter = getFilter();

        fdef.filter.groupIndex=filter.groupIndex;
        fdef.filter.categoryBits= filter.categoryBits;
        fdef.filter.maskBits=filter.maskBits;

        /*PolygonShape bShape = new PolygonShape();
        bShape.set(new Vector2[]{
                new Vector2(-6,6).scl(1/MyGame.PPM),
                new Vector2(6,6).scl(1/MyGame.PPM),
                new Vector2(6,-6).scl(1/MyGame.PPM),
                new Vector2(-6,-6).scl(1/MyGame.PPM)
        });*/
        CircleShape bShape = new CircleShape();
        bShape.setRadius(6/MyGame.PPM);
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData("bad_body");

        EdgeShape front = new EdgeShape();
        front.set(new Vector2(6,6).scl(1/MyGame.PPM),new Vector2(6,-6).scl(1/MyGame.PPM));
        fdef.shape=front;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("bad_front");

        EdgeShape back = new EdgeShape();
        back.set(new Vector2(-6,6).scl(1/MyGame.PPM),new Vector2(-6,-6).scl(1/MyGame.PPM));
        fdef.shape=back;
        body.createFixture(fdef).setUserData("bad_back");
    }

    @Override
    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case RUN:
                region = (TextureRegion) runAnimation.getKeyFrame(stateTimer, true);
                break;
            case ATTACK:
                region = (TextureRegion) attackAnimation.getKeyFrame(stateTimer);
                break;
            default:
                region = standAnimation;
                break;
        }
        /*if ((body.getLinearVelocity().x < 0 || !runRight) && !region.isFlipX()) {
            region.flip(true, false);
            runRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runRight) && region.isFlipX()) {
            region.flip(true, false);
            runRight = true;
        }*/

        if (currentState == previusState) {
            stateTimer = stateTimer + dt;
        } else {
            stateTimer = 0;
        }
        previusState = currentState;
        return region;
    }

    @Override
    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }
}
