package com.my.game.sprites.Throwables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.my.game.MyGame;
import com.my.game.tools.Bullet;
import com.my.game.tools.PlayScreen;

/**
 * Created by lorib on 02/06/2017.
 */

public class Arrow extends Bullet {
    public Arrow(Vector2 position, World world, boolean rightDirection, boolean isPlayer, MyGame game) {
        super(position,world,rightDirection,isPlayer,game);
        body.applyLinearImpulse(new Vector2(forceAttack,0),body.getWorldCenter(),true);
       // forceDrag=1; Never fall
        damage=20;
        atl=new TextureAtlas("archerP/archer.pack");
        getAnimations(atl);
    }

    public void getAnimations(TextureAtlas atlas){
        flyBullet = new TextureRegion(atlas.findRegion("archer_normal_attack").getTexture(), 206, 30, 23, 8);

        setBounds(0, 0, 14 / MyGame.PPM, 5 / MyGame.PPM);
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
