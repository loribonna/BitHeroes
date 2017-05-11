package com.my.game.tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.MyGame;
import com.my.game.screens.PlayScreen;
import com.my.game.tools.EntityInterface;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * Created by lorib on 11/05/2017.
 */

public abstract class Entity extends Sprite implements EntityInterface{

    public World world;
    public Body body;

    public State currentState;
    public State previusState;

    protected Animation attackAnimation;
    protected Animation runAnimation;
    protected TextureRegion standAnimation;

    protected boolean runRight;
    protected float stateTimer;

    public Entity(World w, PlayScreen screen, Vector2 position) {
        super();
        currentState = State.STAND;
        previusState = State.STAND;
        this.world = w;
        define(position);
        getAnimations(screen.getAtlas());
    }

    @Override
    public abstract void getAnimations(TextureAtlas atlas);

    @Override
    public abstract void update(float delta);

    public TextureRegion getFrame(float dt) {
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
        if ((body.getLinearVelocity().x < 0 || !runRight) && !region.isFlipX()) {
            region.flip(true, false);
            runRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runRight) && region.isFlipX()) {
            region.flip(true, false);
            runRight = true;
        }

        if (currentState == previusState) {
            stateTimer = stateTimer + dt;
        } else {
            stateTimer = 0;
        }
        previusState = currentState;
        return region;
    }

    public void attack() {
        currentState = State.ATTACK;
        previusState = State.ATTACK;
        stateTimer = 0;
        setSize(27 / MyGame.PPM, 16 / MyGame.PPM);
        setRegion(getFrame(0));
    }

    public State getState() {
        if (previusState == State.ATTACK) {
            if (!attackAnimation.isAnimationFinished(stateTimer))
                return State.ATTACK;
            else {
                setSize(16 / MyGame.PPM, 16 / MyGame.PPM);
                if (body.getLinearVelocity().x != 0) {
                    return State.RUN;
                } else if (previusState != State.ATTACK) {
                    return State.STAND;
                }
            }
        } else if (body.getLinearVelocity().y > 0) {
            return State.JUMP;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALL;
        } else if (body.getLinearVelocity().x != 0) {
            return State.RUN;
        }
        return State.STAND;
    }

    public void define(Vector2 position) {
        BodyDef bDef= new BodyDef();
        bDef.position.set(position.x / MyGame.PPM,position.y / MyGame.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;

        body=world.createBody(bDef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MyGame.PPM);
        fdef.shape=shape;
        body.createFixture(fdef);
    }
}
