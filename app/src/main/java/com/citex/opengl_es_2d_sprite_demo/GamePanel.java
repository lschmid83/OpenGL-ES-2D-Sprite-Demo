package com.citex.opengl_es_2d_sprite_demo;

import javax.microedition.khronos.opengles.GL10;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * 	This class draws all of the graphics for sprite demo test.
 *
 *  @version 1.0
 *  @modified 29/10/2023
 *  @author Lawrence Schmid
 */

public class GamePanel {

	/** The background image */
	private GLSprite mBackground;

	/** The moon rotating image */
	private GLSprite mMoon;

	/** The moon rotation angle */
	private float mMoonAngle;

	/** The player x coordinate */
	private int mPlayerX;

	/** The player frame number from the sprite sheet */
	private int mPlayerFrameNumber;

	/** The player sprite sheet */
    private SpriteSheet mPlayerSprite;

	/** Stores the enemy positions */
	private final ArrayList<EnemyPosition> mEnemyPositions;

	/** The player frame number from the sprite sheet */
	private int mEnemyFrameNumber;

	/** The player sprite sheet */
	private SpriteSheet mEnemySprite;

	/** The terrain block frame number from the sprite sheet */
	private int mBlockFrameNumber;

	/** The terrain block sprite sheet */
	private SpriteSheet mBlockSprite;

	/** Limits frame updates */
	private int mFrameLimiter;

	/** Screen resolution */
	private final float mResolutionWidth, mResolutionHeight;

	/** Provides access to an application's raw asset files */
	private final AssetManager mAssetManager;
	
	/**
	 * Constructs the GamePanel
	 * @param gl The GL context
	 */
	public GamePanel(GL10 gl, AssetManager assetManager) {

		mResolutionWidth = 480f;
		mResolutionHeight = 272f;

		mAssetManager = assetManager;

		String spriteFilename = "background.png";
		try {
			mBackground = new GLSprite(gl, spriteFilename, assetManager);
		}
		catch(Exception e) {
			Log.e("Sprite method test", "error loading spritesheet" + spriteFilename);
		}

		spriteFilename = "moon.png";
		try {
			mMoon = new GLSprite(gl, spriteFilename, assetManager);
		}
		catch(Exception e) {
			Log.e("Sprite method test", "error loading spritesheet" + spriteFilename);
		}

		spriteFilename = "player.png";
		try {
			mPlayerSprite = new SpriteSheet(gl, spriteFilename, 32, 32, 14, mAssetManager);
		}
		catch(Exception e) {
			Log.e("Sprite method test", "error loading spritesheet" + spriteFilename);
		}

		mEnemyPositions = new ArrayList<>();
		spriteFilename = "enemy.png";
		try {
			mEnemySprite = new SpriteSheet(gl, spriteFilename, 48, 48, 8, mAssetManager);
		}
		catch(Exception e) {
			Log.e("Sprite method test", "error loading spritesheet" + spriteFilename);
		}

		spriteFilename = "block.png";
		try {
			mBlockSprite = new SpriteSheet(gl, spriteFilename, 16, 16, 4, mAssetManager);
		}
		catch(Exception e) {
			Log.e("Sprite method test", "error loading spritesheet" + spriteFilename);
		}

		mPlayerX = -32;
		mPlayerFrameNumber = 11;
		mFrameLimiter = 0;
	}

	/**
	 * Draws the player, background, tileset and entities, handles collision detection
	 * and control the state of the game
	 * @param gl The GL context
	 * @param dt The delta time between frame updates
	 */
	public void paintComponent(GL10 gl, float dt) {

		// Draw background
		mBackground.draw(gl, 0, 0);

		// Draw rotating moon
		mMoonAngle -= 0.5f;
		if(mMoonAngle == 0)
			mMoonAngle = 360f;
		mMoon.draw(gl, 'r', mMoonAngle, mResolutionWidth - 50, 25, mMoon.getWidth() / 2, mMoon.getHeight() / 2);

		// Make player run across screen
		mPlayerX += 0.15 * dt;
		if(mPlayerX > mResolutionWidth) {
			mPlayerX = -32;
		}
		mPlayerSprite.drawFrame(gl, mPlayerFrameNumber, 'r', mPlayerX, mResolutionHeight - mBlockSprite.getFrameHeight() - mPlayerSprite.getFrameHeight());

		for(int i = 0; i < mEnemyPositions.size(); i++)
			mEnemySprite.drawFrame(gl, mEnemyFrameNumber, 'r',  mEnemyPositions.get(i).x, mEnemyPositions.get(i).y);

		// Draw terrain
		for(int i = 0; i< mResolutionWidth / 16; i++)
			mBlockSprite.drawFrame(gl, mBlockFrameNumber, i*16, mResolutionHeight - 16);

		mFrameLimiter++;
		if(mFrameLimiter > 5) {

			mPlayerFrameNumber++;
			if (mPlayerFrameNumber > 13)
				mPlayerFrameNumber = 11;

			mEnemyFrameNumber++;
			if (mEnemyFrameNumber > 7)
				mEnemyFrameNumber = 0;

			mBlockFrameNumber++;
			if(mBlockFrameNumber > 3)
				mBlockFrameNumber = 0;

			mFrameLimiter = 0;
		}
	}

	/**
	 * Invoked when a key has been pressed
	 * @param e Event which indicates that a keystroke occurred
	 */
	public void onKeyDown(KeyEvent e) {
	}
	
	/**
	 * Invoked when a key has been released
	 * @param e Event which indicates that a keystroke occurred
	 */
	public void onKeyUp(KeyEvent e) {
	}
	      
	/**
     * Called when a touch screen motion event occurs
     * @param event The motion event that occurred 
     */
    public void onTouchEvent(final MotionEvent event) {
		// Add a new enemy to the scene
		EnemyPosition enemyPosition = new EnemyPosition();
		enemyPosition.x = (event.getX() / GLSurfaceViewRenderer.mWidth) * mResolutionWidth - mEnemySprite.getFrameWidth() / 2;
		enemyPosition.y = (event.getY() / GLSurfaceViewRenderer.mHeight) * mResolutionHeight - mEnemySprite.getFrameHeight() / 2;
		mEnemyPositions.add(enemyPosition);
    }

	/**
	 * Gets the screen resolution width
	 * @return Resolution width
	 */
	public float getResolutionWidth() {
		return mResolutionWidth;
	}

	/**
	 * Gets the screen resolution height
	 * @return Resolution height
	 */
	public float getResolutionHeight() {
		return  mResolutionHeight;
	}

    /**
     * Destroys the resources and releases the hardware buffers
     * @param gl The GL context
     */
	public void destroy(GL10 gl) {
		mPlayerSprite.destroy(gl);
        Runtime r = Runtime.getRuntime();
        r.gc();
	}
}