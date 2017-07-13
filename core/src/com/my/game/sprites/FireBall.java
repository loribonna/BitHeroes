package com.my.game.sprites;

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
        //atl=new TextureAtlas("");
        //getAnimations(atl);
    }

    public void getAnimations(TextureAtlas atlas){
        //TODO: missing animatinos for coin
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("skullcoin_b1").getTexture(), 1, 3, 19, 20));
        frames.add(new TextureRegion(atlas.findRegion("skullcoin_b1").getTexture(), 35, 25, 19, 20));
        frames.add(new TextureRegion(atlas.findRegion("skullcoin_b1").getTexture(), 1, 25, 19, 20));
        frames.add(new TextureRegion(atlas.findRegion("skullcoin_b1").getTexture(), 35, 25, 19, 20));
        setBounds(0, 0, 16 / MyGame.PPM, 16 / MyGame.PPM);
        flyBullet = new Animation(0.3f, frames);
        stateTimer = 0;
    }

    public TextureRegion getFrame(float dt){
        TextureRegion region;
        region = (TextureRegion)flyBullet.getKeyFrame(stateTimer, true);

        stateTimer = stateTimer + dt;
        return region;
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
        //setRegion(getFrame(delta));
    }
}
