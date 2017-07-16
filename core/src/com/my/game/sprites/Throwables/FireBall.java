package com.my.game.sprites.Throwables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.MyGame;
import com.my.game.tools.Bullet;
import com.my.game.tools.PlayScreen;

/**
 * Created by lorib on 13/07/2017.
 */

public class FireBall extends Bullet {

    /**
     * Create a new FireBall Bullet
     * @param position
     * @param world
     * @param rightDirection
     * @param isPlayer
     * @param game
     */
    public FireBall(Vector2 position, World world, boolean rightDirection, boolean isPlayer, MyGame game) {
        super(position,world,rightDirection,isPlayer,game);
        body.applyLinearImpulse(new Vector2(forceAttack,0),body.getWorldCenter(),true);
        // forceDrag=1; Never fall
        damage=30;
        atl=new TextureAtlas("aceP/ace.pack");
        getAnimations(atl);
    }

    /**
     * Import FireBall-specific animations from the FireBender atlas.
     * @param atlas
     */
    public void getAnimations(TextureAtlas atlas){
        flyBullet=new TextureRegion(atlas.findRegion("ace_animation"), 1, 8, 50, 14);
        if(oppositeDirection) flyBullet.flip(true,false);
        setBounds(0, 0, 16 / MyGame.PPM, 6 / MyGame.PPM);
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
            setCategoryBits(MyGame.NOTHING_BIT);
            dispose();
        }
        setRegion(flyBullet);
    }
}
