package com.my.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.my.game.screens.FirstScreen;
import com.my.game.tools.PlayScreen;

public class MyGame extends Game {
	//
	private SpriteBatch batch;
	private String currentPlayer;
	private PlayScreen currentPlayScreen;

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
	public static final short EXIT_BIT = 512;
	public static final short PLAYER_MELEE_BIT = 1024;
	public static final short ENEMY_MELEE_BIT = 2048;

	/**
	 * Initialize game scenes and SpriteBatch
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new FirstScreen(this));
	}

	public PlayScreen getCurrentPlayScreen(){
		return this.currentPlayScreen;
	}

	public void setCurrentPlayScreen(PlayScreen level){
		this.currentPlayScreen=level;
	}

	public void changeLevel(PlayScreen level){
		setScreen(level);
	}

	public void setCurrentPlayer(String player){
		this.currentPlayer=player;
	}

	public String getCurrentPlayer(){
		return this.currentPlayer;
	}

	public SpriteBatch getBatch(){
		return this.batch;
	}

	public void removeObject(Object obj){
		this.currentPlayScreen.objectsToRemove.add(obj);
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
