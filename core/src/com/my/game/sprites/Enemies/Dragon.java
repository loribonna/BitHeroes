package com.my.game.sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.my.game.MyGame;
import com.my.game.screens.MenuScreen;
import com.my.game.sprites.Throwables.Arrow;
import com.my.game.sprites.Throwables.DragonBall;
import com.my.game.tools.Enemy;
import com.my.game.tools.Interfaces.EntityInterface;

/**
 * Created by lorib on 15/07/2017.
 */

public class Dragon extends Enemy {

    public Dragon(World w, TextureAtlas screenAtlas, Vector2 position, MyGame game) {
        super(w, screenAtlas,position,game);
        attackRange=0.2f;
        life=200;
        maxMoveRange=100;
        disableJump=true;
        meleeDamage=35;
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
    public void createBorders() {
        FixtureDef fdef = new FixtureDef();
        Filter filter = getFilter();

        fdef.filter.groupIndex=filter.groupIndex;
        fdef.filter.categoryBits= filter.categoryBits;
        fdef.filter.maskBits=filter.maskBits;

        PolygonShape bShape = new PolygonShape();
        bShape.set(new Vector2[]{
                new Vector2(-6,15).scl(1/MyGame.PPM),
                new Vector2(6,15).scl(1/MyGame.PPM),
                new Vector2(6,-15).scl(1/MyGame.PPM),
                new Vector2(-6,-15).scl(1/MyGame.PPM)
        });
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void hit(int damage){
        life-=damage;
        if(life<=0){
            //TODO: goto win
            game.getCurrentPlayScreen().dispose();
            Screen screen;
            screen = new MenuScreen(game);
            game.setScreen(screen);
            destroy();
        }else if(damage>0){
            recoil();
        }
        invulnarable=true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                invulnarable=false;
            }
        },1);
    }

    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("dragon_walking"),8,9, 136, 131);
        setBounds(0, 0, 100 / MyGame.PPM, 100 / MyGame.PPM);
        setRegion(standAnimation);
        currentState = EntityInterface.State.STAND;
        previusState = EntityInterface.State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("dragon_walking"),8,2, 145, 131));
        frames.add(new TextureRegion(atlas.findRegion("dragon_walking"),  167, 2, 145, 131));
        frames.add(new TextureRegion(atlas.findRegion("dragon_walking"),  328, 2,145, 131));

        runAnimation = new Animation(0.2f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("dragon_second_attack"), 0, 3, 230, 140));
        frames.add(new TextureRegion(atlas.findRegion("dragon_second_attack"), 240, 3, 230, 140));
        frames.add(new TextureRegion(atlas.findRegion("dragon_second_attack"), 484, 3, 250, 140));
        frames.add(new TextureRegion(atlas.findRegion("dragon_second_attack"), 750, 3, 230, 140));
        attackAnimation = new Animation (0.2f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("dragon_first_attack"), 660, 20, 200, 110));
        frames.add(new TextureRegion(atlas.findRegion("dragon_first_attack"), 18, 20, 170, 110));
        frames.add(new TextureRegion(atlas.findRegion("dragon_first_attack"), 660, 20, 200, 110));
        throwAnimation = new Animation(0.2f,frames);
    }

    @Override
    public void firstAttack() {
        currentState = EntityInterface.State.ATTACK;
        previusState = EntityInterface.State.ATTACK;
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

    protected void throwBullet() {
        Vector2 position = new Vector2(getPosition().x,getPosition().y+20/MyGame.PPM);
        game.getCurrentPlayScreen().addBullet(new DragonBall(position, world, isFlipX(),false,game));
    }

    @Override
    public void secondAttack() {
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
    public void specialAttack() {
        lockAttack=false;
    }

    @Override
    protected FixtureDef createFrontAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponFront = new PolygonShape();
        weaponFront.set(new Vector2[]{new Vector2(25,-6).scl(1/MyGame.PPM),new Vector2(25,-10).scl(1/MyGame.PPM)
                ,new Vector2(8,-6).scl(1/MyGame.PPM),new Vector2(8,-10).scl(1/MyGame.PPM)});
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
        weaponBack.set(new Vector2[]{new Vector2(-25,-6).scl(1/MyGame.PPM),new Vector2(-25,-10).scl(1/MyGame.PPM)
                ,new Vector2(-8,-6).scl(1/MyGame.PPM),new Vector2(-8,-10).scl(1/MyGame.PPM)});
        fdef.shape = weaponBack;
        fdef.filter.categoryBits=MyGame.ENEMY_MELEE_BIT;
        fdef.filter.groupIndex=MyGame.GROUP_BULLET;
        fdef.filter.maskBits=MyGame.PLAYER_BIT;
        fdef.isSensor=true;
        return fdef;
    }
}
