package com.my.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.my.game.screens.FirstScreen;
import com.my.game.tools.AppConstants;
import com.my.game.tools.PlayScreen;

public class BitHeroes extends Game {

	private static BitHeroes instance;

	private BitHeroes(){}

	public static BitHeroes getInstance(){
		if(instance==null){
			instance=new BitHeroes();
		}
		return instance;
	}

	/*
	* It's convenient to have only one SpriteBatch and reference to it
	*/
	private SpriteBatch batch;
	private AppConstants.PlayerName currentPlayer;
	private PlayScreen currentPlayScreen;
	private AssetManager manager;

	public static boolean disableAudio=true;
	public static boolean enableDebugRenderer=false;

	public static final String name = "Bit Heroes";
	public static final float V_WIDTH = 360;
	public static final float V_HEIGHT = 202;
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
	 * Initialize game scenes, SpriteBatch and manager for audio clips
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		if(!BitHeroes.disableAudio){
			try{
				for(FileHandle sound : Gdx.files.internal("sounds").list()){
					manager.load(sound.path(), Music.class);
				}
			}catch (Exception e){
				Gdx.app.log("Error","Path 'sounds' not found, unable to load Music");
			}
		}
		manager.finishLoading();
		setScreen(new FirstScreen(this));
	}

	/**
	 * @return current asset manager
	 */
	public AssetManager getManager(){
		return manager;
	}

	/**
	 * @return currentPlayScreen
	 */
	public PlayScreen getCurrentPlayScreen(){
		return this.currentPlayScreen;
	}

	/**
	 * Sets currentPlayScreen
	 * @param level
	 */
	public void setCurrentPlayScreen(PlayScreen level){
		this.currentPlayScreen=level;
	}

	/**
	 * Sets the current playing level.
	 * @param level
	 */
	public void changeLevel(PlayScreen level){
		setScreen(level);
	}

	/**
	 * @return current score from the currentPlayScreen hud.
	 */
	public int getScore(){
		return getCurrentPlayScreen().getCurrentScore();
	}

	/**
	 * Sets currentPlayer string name
	 * @param player
	 */
	public void setCurrentPlayer(AppConstants.PlayerName player){
		this.currentPlayer=player;
	}

	/**
	 * @return currentPlayer string name
	 */
	public AppConstants.PlayerName getCurrentPlayer(){
		return this.currentPlayer;
	}

	/**
	 * @return global SpriteBatch
	 */
	public SpriteBatch getBatch(){
		return this.batch;
	}

	/**
	 * Mark the object obj for sweep in currentPlayScreen and dispose it
	 * @param obj
	 */
	public void removeObject(Object obj){
		this.currentPlayScreen.objectsToRemove.add(obj);
	}

	/**
	 * Calls the ApplicationListener render method to render the application
	 */
	@Override
	public void render () {
		super.render();
	}

	/**
	 * Called when the application is destroyed
	 */
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
	}
}
