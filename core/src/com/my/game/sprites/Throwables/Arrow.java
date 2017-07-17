package com.my.game.sprites.Throwables;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.Bullet;

/**
 * Create a Arrow bullet
 */

public class Arrow extends Bullet {

    /**
     * Create a new Arrow Bullet
     * @param position
     * @param world
     * @param rightDirection
     * @param isPlayer
     * @param game
     */
    public Arrow(Vector2 position, World world, boolean rightDirection, boolean isPlayer, BitHeroes game) {
        super(position,world,rightDirection,isPlayer,game);
        body.applyLinearImpulse(new Vector2(forceAttack,0),body.getWorldCenter(),true);
        damage=20;
        atl=new TextureAtlas("archerP/archer.pack");
        getAnimations(atl);
    }

    /**
     * Import Arrow-specific animations from the Archer atlas.
     * @param atlas
     */
    public void getAnimations(TextureAtlas atlas){
        flyBullet = new TextureRegion(atlas.findRegion("archer_normal_attack"), 206, 17, 23, 8);
        if(oppositeDirection) flyBullet.flip(true,false);
        setBounds(0, 0, 14 / BitHeroes.PPM, 5 / BitHeroes.PPM);
    }

    /**
     * Update position and animation. Destroy when speed is too low.
     * @param delta
     */
    @Override
    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        if((body.getLinearVelocity().x>minSpeed&&!oppositeDirection)||
                (body.getLinearVelocity().x<-minSpeed&&oppositeDirection)) {
            body.applyForce(new Vector2(0, -world.getGravity().y * forceDrag), body.getWorldCenter(), true);
        }
        if(body.getLinearVelocity().y+body.getLinearVelocity().x<minSpeed&&!oppositeDirection||
                body.getLinearVelocity().y+body.getLinearVelocity().x>-minSpeed&&oppositeDirection){
            setCategoryBits(BitHeroes.NOTHING_BIT);
            dispose();
        }
        setRegion(flyBullet);
    }
}
