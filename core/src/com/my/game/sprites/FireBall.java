package com.my.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.MyGame;
import com.my.game.tools.Bullet;

/**
 * Created by lorib on 13/07/2017.
 */

public class FireBall extends Bullet {
    public FireBall(Vector2 position, World world, boolean rightDirection, boolean isPlayer) {
        super(position,world,rightDirection,isPlayer);
        body.applyLinearImpulse(new Vector2(forceAttack,0),body.getWorldCenter(),true);
        // forceDrag=1; Never fall
        damage=20;
        atl=new TextureAtlas("aceP/ace.pack");
        getAnimations(atl);
    }

    public void getAnimations(TextureAtlas atlas){
        flyBullet=new TextureRegion(atlas.findRegion("ace_first_attack").getTexture(), 290, 30, 50, 14);
        setBounds(0, 0, 16 / MyGame.PPM, 6 / MyGame.PPM);
    }

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
