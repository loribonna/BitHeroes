package com.my.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.my.game.screens.FirstScreen;

public class MyGame extends Game {
	//
	public SpriteBatch batch;
	public static MyGame current;
	public static Screen currentPlayScreen;
	public static String name = "Bit Heroes";
	public static final float V_WIDTH = 400;
	public static final float V_HEIGHT = 208;
	public static final float PPM = 100;

	public static final short GROUP_PLAYER = 1;
	public static final short GROUP_ENEMIES = 2;
	public static final short GROUP_SCENERY = 4;
	public static final short GROUP_BULLET = 8;

	public static final short NOTHING_BIT = 0;
	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short ENEMY_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short BRICK_BIT = 16;
	public static final short VOID_BIT = 32;
	public static final short WALL_BIT=64;
	public static final short PLAYER_BULLET_BIT=128;
	public static final short ENEMY_BULLET_BIT=256;

	/**
	 * Initialize game scenes and SpriteBatch
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		//setScreen(new PlayScreen(this));
		setScreen(new FirstScreen(this));
		current=this;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
