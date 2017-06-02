package com.my.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.tools.Bullet;

/**
 * Created by lorib on 02/06/2017.
 */

public class Arrow extends Bullet {
    public Arrow(Vector2 position, World world,Boolean rightDirection) {
        super(position,world,rightDirection);
        body.applyLinearImpulse(new Vector2(forceAttack,0),body.getWorldCenter(),true);
       // forceDrag=1; Never fall
    }

    @Override
    public Filter getFilter() {
        Filter f=new Filter();
        f.groupIndex= MyGame.GROUP_BULLET;
        f.categoryBits= MyGame.PLAYER_BULLET_BIT;
        f.maskBits=MyGame.ENEMY_BIT|MyGame.WALL_BIT|MyGame.BRICK_BIT|MyGame.DEFAULT_BIT;
        return f;
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

    }
}
