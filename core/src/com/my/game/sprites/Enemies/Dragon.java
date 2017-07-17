package com.my.game.sprites.Enemies;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.screens.FinalScreen;
import com.my.game.sprites.Throwables.DragonBall;
import com.my.game.tools.Enemy;
import com.my.game.tools.Interfaces.EntityInterface;

/**
 * Create a Dragon entity from Enemy class
 */

public class Dragon extends Enemy {
    /**
     * Create a Dragon from Enemy class
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Dragon(World world, TextureAtlas screenAtlas, Vector2 position, BitHeroes game) {
        super(world, screenAtlas,position,game);
        attackRange=0.2f;
        life=200;
        maxMoveRange=100;
        disableJump=true;
        meleeDamage=35;
    }

    /**
     * Perform a distance attack
     */
    @Override
    protected void distanceAttack() {
        secondAttack();
    }

    /**
     * Perform a melee attack
     */
    @Override
    protected void meleeAttack() {
        firstAttack();
    }

    /**
     * Replace default to create bigger shape
     */
    @Override
    public void createBorders() {
        FixtureDef fdef = new FixtureDef();
        Filter filter = getFilter();

        fdef.filter.groupIndex=filter.groupIndex;
        fdef.filter.categoryBits= filter.categoryBits;
        fdef.filter.maskBits=filter.maskBits;

        PolygonShape bShape = new PolygonShape();
        bShape.set(new Vector2[]{
                new Vector2(-6,15).scl(1/ BitHeroes.PPM),
                new Vector2(6,15).scl(1/ BitHeroes.PPM),
                new Vector2(6,-15).scl(1/ BitHeroes.PPM),
                new Vector2(-6,-15).scl(1/ BitHeroes.PPM)
        });
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Replace default to jump to FinalScreen
     */
    @Override
    public void destroy(){
        dead=true;
        game.getCurrentPlayScreen().dispose();
        Screen screen = new FinalScreen(game);
        game.setScreen(screen);
        body.setUserData(new Boolean(true));
        game.removeObject(this);
    }

    /**
     * Import enity-specific animations from the Dragon atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("dragon_walking"),8,9, 136, 131);
        setBounds(0, 0, 100 / BitHeroes.PPM, 100 / BitHeroes.PPM);
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

    /**
     * Replace the first attack with a melee attack
     */
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
                music = game.getManager().get("sounds/ruggito.wav",Music.class);
                music.setLooping(false);
                music.setVolume(1);
                music.play();
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

    /**
     * Add a bullet in the currentPlayScreen
     */
    protected void throwBullet() {
        Vector2 position = new Vector2(getPosition().x,getPosition().y+20/ BitHeroes.PPM);
        game.getCurrentPlayScreen().addBullet(new DragonBall(position, world, isFlipX(),false,game));
    }

    /**
     * Replace the second attack with a distance attack
     */
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
                music = game.getManager().get("sounds/ruggito.wav",Music.class);
                music.setLooping(false);
                music.setVolume(1);
                music.play();
                throwBullet();
            }
        },throwAnimation.getAnimationDuration()/2);
    }

    /**
     * Replace default to increase fixture shape
     * @return fixture to trigger collision for Melee attack if the attack is front
     */
    @Override
    public FixtureDef createFrontAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponFront = new PolygonShape();
        weaponFront.set(new Vector2[]{new Vector2(25,-6).scl(1/ BitHeroes.PPM),new Vector2(25,-10).scl(1/ BitHeroes.PPM)
                ,new Vector2(8,-6).scl(1/ BitHeroes.PPM),new Vector2(8,-10).scl(1/ BitHeroes.PPM)});
        fdef.shape = weaponFront;
        fdef.filter.categoryBits= BitHeroes.ENEMY_MELEE_BIT;
        fdef.filter.groupIndex= BitHeroes.GROUP_BULLET;
        fdef.filter.maskBits= BitHeroes.PLAYER_BIT;
        fdef.isSensor=true;
        return fdef;
    }

    /**
     * Replace default to increase fixture shape
     * @return fixture to trigger collision for Melee attack if the body is flipped.
     */
    @Override
    public FixtureDef createBackAttackFixture() {
        FixtureDef fdef = new FixtureDef();

        PolygonShape weaponBack = new PolygonShape();
        weaponBack.set(new Vector2[]{new Vector2(-25,-6).scl(1/ BitHeroes.PPM),new Vector2(-25,-10).scl(1/ BitHeroes.PPM)
                ,new Vector2(-8,-6).scl(1/ BitHeroes.PPM),new Vector2(-8,-10).scl(1/ BitHeroes.PPM)});
        fdef.shape = weaponBack;
        fdef.filter.categoryBits= BitHeroes.ENEMY_MELEE_BIT;
        fdef.filter.groupIndex= BitHeroes.GROUP_BULLET;
        fdef.filter.maskBits= BitHeroes.PLAYER_BIT;
        fdef.isSensor=true;
        return fdef;
    }
}
