package com.my.game.tools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;

/**
 * Created by lorib on 29/05/2017.
 */

public abstract class Enemy extends Entity{
    public Enemy(World w, TextureAtlas screenAtlas, Vector2 position) {
        super(w, screenAtlas, position);
    }

    @Override
    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));



    }
}
