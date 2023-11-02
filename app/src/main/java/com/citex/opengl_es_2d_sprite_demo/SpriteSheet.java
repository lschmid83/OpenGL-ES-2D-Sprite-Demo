package com.citex.opengl_es_2d_sprite_demo;

import javax.microedition.khronos.opengles.GL10;
import android.content.res.AssetManager;
import java.io.IOException;

/**
 *  This class loads an image containing a sprite sheet and buffers the frames of animation.
 *
 *  @version 1.0
 *  @modified 29/10/2023
 *  @author Lawrence Schmid
 */

public class SpriteSheet {

    /** The sprite sheet containing the animation */
    private GLSprite mSpriteSheet;

    /** The buffered frames of animation */
    private GLSprite mFrames[];

    /** The frame dimensions */
    private int mFrameWidth, mFrameHeight;

    /** The sprite sheet dimensions */
    private int mSheetWidth, mSheetHeight;

    /** The number of rows and columns in the sprite sheet */
    private int mRows, mColumns;

    /** The number of frames in the sprite sheet */
    private int mFrameCount;

    /** Provides access to an application's raw asset files */
    private AssetManager mAssetManager;

    /**
     * Constructs the sprite sheet
     * @param gl The GL context
     * @param path The path of the image file
     * @param frameWidth The frame width
     * @param frameHeight The frame height
     * @param frameCount The frame count
     * @param assetManager Provides access to an application's raw asset files
     * @throws IOException
     */
    public SpriteSheet(Object gl, String path, int frameWidth, int frameHeight, int frameCount, AssetManager assetManager) throws IOException  {

        mFrameWidth = frameWidth;
        mFrameHeight = frameHeight;
        mFrameCount = frameCount;
        mAssetManager = assetManager;
        mSpriteSheet = new GLSprite((GL10) gl, path, mAssetManager);

        if (mSpriteSheet != null) {
            mSheetWidth = mSpriteSheet.getWidth();
            mSheetHeight = mSpriteSheet.getHeight();
            mRows = mSheetHeight / (mFrameHeight);
            mColumns = mSheetWidth / (mFrameWidth);
        } else {
            mSheetWidth = 0;
            mSheetHeight = 0;
            mRows = 0;
            mColumns = 0;
            mFrameCount = 1;
        }

        //buffer frames from sheet
        mFrames = new GLSprite[mFrameCount];
        for (int i = 0; i < mFrameCount; i++) {
            mFrames[i] = getFrameFromSheet(gl, i); //store each frame in the array
        }
    }

    /**
     * Returns a frame of animation from the sprite sheet
     * @param frameNumber The frame number
     * @return The GLSprite representing the frame of animation
     */
    public GLSprite getFrameFromSheet(Object gl, int frameNumber) {
        try {
            if (mRows > 0 && mColumns > 0) {
                int col = frameNumber / mRows; //column
                int row = frameNumber - (col * mRows); //row
                return mSpriteSheet.getSubImage(gl, col * mFrameWidth,
                        row * mFrameHeight, mFrameWidth, mFrameHeight);
            } else //one column in sheet
            {
                return mSpriteSheet.getSubImage(gl, 0, frameNumber * 16, mFrameWidth, mFrameHeight);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Draws a buffered frame of animation from the sprite sheet at x and y coordinates
     * @param gl The GL context
     * @param frameNumber The frame number
     * @param x The x coordinate
     * @param y The x coordinate
     */
    public void drawFrame(GL10 gl, int frameNumber, float x, float y) {
        mFrames[frameNumber].draw(gl, x, y);
    }


    /**
     * Draws a buffered frame of animation from the sprite sheet repeating to a set width and height
     * Used to draw repeating tiles in one draw routine
     * @param gl The GL context
     * @param x The x coordinate
     * @param y The x coordinate
     * @param repeatWidth The width to repeat the sprite.
     * @param repeatHeight The height to repeat the sprite.
     */
    public void drawFrame(GL10 gl, int frameNumber, float x, float y, float repeatWidth, float repeatHeight) {
        mFrames[frameNumber].draw(gl, x, y, repeatWidth, repeatHeight);
    }


    /**
     * Draws a buffered frame of animation from the sprite sheet at x,y coordinates in a 'l' - left
     * or 'r' - right direction
     * @param gl The GL context
     * @param direction The direction the sprite is facing
     * @param x The x coordinate
     * @param y The x coordinate
     */
    public void drawFrame(GL10 gl, int frameNumber, char direction, float x, float y) {
        mFrames[frameNumber].draw(gl, direction, x, y);
    }

    /**
     * Draws the a buffered frame of animation from the spritesheet at x,y coordinates in a
     * 'l' - left or 'r' - right direction rotated to an angle with a center position
     * @param gl The GL context
     * @param direction The direction the sprite is facing
     * @param angle The angle of rotation
     * @param x The x coordinate
     * @param y The x coordinate
     * @param centerX The center x of rotation
     * @param centerY The center y of rotation
     */
    public void drawFrame(GL10 gl, int frameNumber, char direction, float angle, int x, int y, int centerX, int centerY) {
        if(frameNumber <= mFrameCount)
            mFrames[frameNumber].draw(gl, direction, angle, x, y, centerX, centerY);
    }

    /**
     * Destroys the textures and releases hardware buffers
     * @param gl The GL context
     */
    public void destroy(GL10 gl) {
    }

    /**
     * Gets the frame width.
     * @return Frame width.
     */
    public int getFrameWidth() {
        return mFrameWidth;

    }

    /**
     * Gets the frame height.
     * @return Frame height.
     */
    public int getFrameHeight() {
        return mFrameHeight;
    }
}