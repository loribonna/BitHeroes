package com.my.game.sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.badlogic.gdx.utils.Timer;
import com.my.game.MyGame;
import com.my.game.tools.*;

/**
 * Created by lorib on 11/05/2017.
 */

public class Orch extends Enemy {
    public Orch(World w, TextureAtlas screenAtlas,Vector2 position,MyGame game) {
        super(w, screenAtlas,position,game);
        attackRange=0.18f;
        life=1;
    }

    @Override
    protected void distanceAttack() {
        secondAttack();
    }

    @Override
    protected void meleeAttack() {
        firstAttack();
    }

    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("orc_walking"), 371-264, 6, 50, 82);
        setBounds(0, 0, 24 / MyGame.PPM, 30 / MyGame.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("orc_walking"),  261-264, 6, 50, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_walking"),  316-264, 6, 50, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_walking"),  371-264, 6, 50, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_walking"),  427-264, 6, 50, 82));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("orc_attack"), 2, 8, 80, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_attack"), 95, 8, 80, 82));
        frames.add(new TextureRegion(atlas.findRegion("orc_attack"), 177, 8, 80 , 82));
        attackAnimation = new Animation (0.2f, frames);
        frames.clear();
    }

    @Override
    public void firstAttack() {
        currentState = State.ATTACK;
        previusState = State.ATTACK;
        stateTimer = 0;
        setRegion(getFrame(0));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
            }
        },attackAnimation.getAnimationDuration());


        final BodyDef bDef=new BodyDef();
        bDef.position.set(body.getPosition());
        bDef.type = BodyDef.BodyType.DynamicBody;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                final Body attackBody = world.createBody(bDef);
                attackBody.setGravityScale(0);

                if (isFlipX()) {
                    final Fixture f = attackBody.createFixture(createBackAttackFixture());
                    f.setUserData(meleeDamage);

                } else {
                    final Fixture f = attackBody.createFixture(createFrontAttackFixture());
                    f.setUserData(meleeDamage);
                }
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        attackBody.setUserData(true);
                    }
                }, attackAnimation.getAnimationDuration() / 2);
            }
        }, attackAnimation.getAnimationDuration() / 2);
    }

    @Override
    public void secondAttack() {
        lockAttack=false;
    }

    @Override
    public void specialAttack() {
        lockAttack=false;
    }

    @Override
    protected FixtureDef createFrontAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponFront = new PolygonShape();
        weaponFront.set(new Vector2[]{new Vector2(12,-2).scl(1/MyGame.PPM),new Vector2(12,-4).scl(1/MyGame.PPM)
                ,new Vector2(8,-2).scl(1/MyGame.PPM),new Vector2(8,-4).scl(1/MyGame.PPM)});
        fdef.shape = weaponFront;
        fdef.filter.categoryBits=MyGame.ENEMY_MELEE_BIT;
        fdef.filter.groupIndex=MyGame.GROUP_BULLET;
        fdef.filter.maskBits=MyGame.PLAYER_BIT;
        fdef.isSensor=true;
        return fdef;
    }

    @Override
    protected FixtureDef createBackAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponBack = new PolygonShape();
        weaponBack.set(new Vector2[]{new Vector2(-12,-2).scl(1/MyGame.PPM),new Vector2(-12,-4).scl(1/MyGame.PPM)
                ,new Vector2(-8,-2).scl(1/MyGame.PPM),new Vector2(-8,-4).scl(1/MyGame.PPM)});
        fdef.shape = weaponBack;
        fdef.filter.categoryBits=MyGame.ENEMY_MELEE_BIT;
        fdef.filter.groupIndex=MyGame.GROUP_BULLET;
        fdef.filter.maskBits=MyGame.PLAYER_BIT;
        fdef.isSensor=true;
        return fdef;
    }

}
