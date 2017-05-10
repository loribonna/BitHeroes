package com.my.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.MyGame;
import com.my.game.screens.PlayScreen;
import com.sun.org.apache.xerces.internal.impl.dv.xs.AnyURIDV;

/**
 * Created by lorib on 09/05/2017.
 */

public class Warrior extends Sprite {
    public World world;
    public Body body;

    public enum State {
        FALL,
        ATTACK,
        STAND,
        RUN,
        JUMP
    }

    public State currentState;
    public State previusState;

    private Animation warriorRun;
    private Animation warriorAttack;

    private boolean runRight;
    private float stateTimer;

    private TextureRegion warriorStand;

    public Warrior(World w, PlayScreen screen) {
        super(screen.getAtlas().findRegion("warrior_idle"));
        //super();
        this.world = w;
        defineWarrior();
        warriorStand = new TextureRegion(screen.getAtlas().findRegion("warrior_idle").getTexture(), 0, 0, 14, 16);
        setBounds(0, 0, 16 / MyGame.PPM, 16 / MyGame.PPM);
        setRegion(warriorStand);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        int i = 0;
        for (; i < 3; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("warrior_walk"), i * 16, 0, 14, 16));
            i++;
        }
        warriorRun = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("warrior_attack"), 0, 0, 27, 16));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("warrior_attack"), 27, 0, 27, 16));
        warriorAttack = new Animation(0.5f, frames);
        frames.clear();

    }

    public void update(float dt) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case RUN:
                region = (TextureRegion) warriorRun.getKeyFrame(stateTimer, true);
                //setSize(16 / MyGame.PPM, 16 / MyGame.PPM);
                break;
            case ATTACK:
                region = (TextureRegion) warriorAttack.getKeyFrame(stateTimer);
                //setSize(27 / MyGame.PPM, 16 / MyGame.PPM);
                break;
            default:
                region = warriorStand;
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
        //TODO: attacco

        currentState = State.ATTACK;
        previusState = State.ATTACK;
        stateTimer = 0;
        setSize(27 / MyGame.PPM, 16 / MyGame.PPM);
        setRegion(getFrame(0));

    }

    public State getState() {
        if (previusState == State.ATTACK) {
            if (!warriorAttack.isAnimationFinished(stateTimer))
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


    public void defineWarrior(){
        BodyDef bDef= new BodyDef();
        bDef.position.set(32 / MyGame.PPM,32 / MyGame.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;

        body=world.createBody(bDef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MyGame.PPM);

        fdef.shape=shape;
        body.createFixture(fdef);

    }

}
