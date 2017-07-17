package com.my.game.sprites.Throwables;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.BitHeroes;
import com.my.game.tools.Bullet;

/**
 * Create a BlobBall bullet
 */

public class BlobBall extends Bullet {

    /**
     * Create a new BlobBall Bullet
     * @param position
     * @param world
     * @param rightDirection
     * @param isPlayer
     * @param game
     */
    public BlobBall(Vector2 position, World world, boolean rightDirection, boolean isPlayer, BitHeroes game) {
        super(position,world,rightDirection,isPlayer,game);
        body.applyLinearImpulse(new Vector2(forceAttack,0),body.getWorldCenter(),true);
        damage=20;
        atl=new TextureAtlas("blobP/blob.pack");
        getAnimations(atl);
    }

    /**
     * Import BlobBall-specific animations from the Blob atlas.
     * @param atlas
     */
    public void getAnimations(TextureAtlas atlas){
        flyBullet = new TextureRegion(atlas.findRegion("blob"), 7, 8, 11, 11);
        if(oppositeDirection) flyBullet.flip(true,false);
        setBounds(0, 0, 10 / BitHeroes.PPM, 10 / BitHeroes.PPM);
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
