package com.citex.opengl_es_2d_sprite_demo;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * This is the OpenGL ES version of a sprite.
 *
 *  @version 1.0
 *  @modified 29/10/2023
 *  @author Lawrence Schmid
 *
 *  Original code from: <a href="http://code.google.com/p/apps-for-android/source/browse/trunk/SpriteMethodTest">...</a>
 */

public class GLSprite extends Renderable implements Cloneable {

    /** The OpenGL ES texture handle to draw. */
    private int mTextureName;

    /** The grid object defining those vertices */
    private Grid mGrid;

    /** The array in which the generated texture names are stored. */
    private int[] mTextureNameWorkspace;

    /** The texture crop */
    private int[] mCropWorkspace;

    /** The texture name */
    private int mTextureID;

    /** The texture mapping coordinates */
    float uMin, vMin, uMax, vMax;

    /** Is the texture flipped horizontally */
    boolean flipTexture;

    /** Provides access to an application's raw asset files */
    private AssetManager mAssetManager;

    /**
     * Constructs the GLSprite
     * @param gl The GL context
     * @param texturePath The texture path
     * @param assetManager Provides access to an application's raw asset files
     * @throws IOException
     */
    public GLSprite(GL10 gl, String texturePath, AssetManager assetManager) throws IOException {
        this(gl, texturePath, null, assetManager);
    }

    /**
     * Constructs the GLSprite
     * @param gl The GL context
     * @param texturePath The texture path
     * @param clip The clipping region
     * @param assetManager
     * @throws IOException
     */
    public GLSprite(GL10 gl, String texturePath, float clip[], AssetManager assetManager) throws IOException {
        super();

        //Pre-allocate and store these objects so we can use them at runtime
        mTextureNameWorkspace = new int[1];
        mCropWorkspace = new int[4];
        mAssetManager = assetManager;
        mTextureID = loadBitmap(gl, texturePath);

        if (mTextureID != -1) {
            setVertices(gl, clip);
            // Load our texture and set its texture name
            setTextureName(getTextureID());
            Grid currentGrid = getGrid();
            currentGrid.generateHardwareBuffers(gl);
        }
    }

    /**
     * Sets a vertex grid with the clipping region and calculates the texture mapping
     * coordinates
     * @param gl The GL context
     * @param clip The clipping region
     */
    public void setVertices(GL10 gl, float[] clip) {
        if (clip == null) //clipping region not set
        {
            clip = new float[4];
            clip[0] = 0;
            clip[1] = 0;
            clip[2] = width;
            clip[3] = height;
        }

        spriteX = clip[0];
        spriteY = clip[1];
        spriteWidth = clip[2];
        spriteHeight = clip[3];

        //find the normalized texture coordinates (Beginning iPhone Games Development p.275)
        uMin = spriteX / width;
        vMin = spriteY / height;
        uMax = (spriteX + spriteWidth) / width;
        vMax = (spriteY + spriteHeight) / height;

        // Setup the vertices for the quad (Beginning iPhone Games Development p.268 UV)
        Grid verts = new Grid(2, 2, false);
        verts.set(0, 0, 0.0f, 0.0f, 0.0f, uMin, vMin, null); //0
        verts.set(1, 0, spriteHeight, 0.0f, 0.0f, uMin, vMax, null); //1
        verts.set(0, 1, 0.0f, spriteWidth, 0.0f, uMax, vMin, null); //2
        verts.set(1, 1, spriteHeight, spriteWidth, 0.0f, uMax, vMax, null); //3
        setGrid(verts);

        if (mTextureID != -1) {
            // Load our texture and set its texture name
            setTextureName(getTextureID());
            Grid currentGrid = getGrid();
            currentGrid.generateHardwareBuffers(gl);
        }
    }

    /**
     * Flips the texture mapping coordinates horizontally
     * @param horizontal True: Flip sprite horizontally False: No flip
     */
    public void flipSprite(GL10 gl, boolean horizontal) {
        if (horizontal && flipTexture == false) //flip horizontally
        {
            Grid verts = new Grid(2, 2, false);
            verts.set(0, 0, 0.0f, 0.0f, 0.0f, uMax, vMin, null); //0
            verts.set(1, 0, spriteHeight, 0.0f, 0.0f, uMax, vMax, null); //1
            verts.set(0, 1, 0.0f, spriteWidth, 0.0f, uMin, vMin, null); //2
            verts.set(1, 1, spriteHeight, spriteWidth, 0.0f, uMin, vMax, null); //3
            setGrid(verts);
            flipTexture = true;
        } else if (!horizontal && flipTexture == true) //restore to original
        {
            Grid verts = new Grid(2, 2, false);
            verts.set(0, 0, 0.0f, 0.0f, 0.0f, uMin, vMin, null); //0
            verts.set(1, 0, spriteHeight, 0.0f, 0.0f, uMin, vMax, null); //1
            verts.set(0, 1, 0.0f, spriteWidth, 0.0f, uMax, vMin, null); //2
            verts.set(1, 1, spriteHeight, spriteWidth, 0.0f, uMax, vMax, null); //3
            setGrid(verts);
            flipTexture = false;
        }

        if (mTextureID != -1) {
            Grid currentGrid = getGrid();
            currentGrid.generateHardwareBuffers(gl);
        }
    }

    /**
     * Flips the texture mapping coordinates horizontally
     */
    public void cropSprite(GL10 gl, float w, float h) {

        if (w > 0 && h > 0&& flipTexture == false) //flip horizontally
        {
            uMin = spriteX / width;
            vMin = spriteY / height;
            uMax = (spriteX + w) / width;
            vMax = (spriteY + h) / height;

            Grid verts = new Grid(2, 2, false);
            verts.set(0, 0, 0.0f, 0.0f, 0.0f, uMin, vMin, null); //0
            verts.set(1, 0, h, 0.0f, 0.0f, uMin, vMax, null); //1
            verts.set(0, 1, 0.0f, w, 0.0f, uMax, vMin, null); //2
            verts.set(1, 1, h, w, 0.0f, uMax, vMax, null); //3

            setGrid(verts);
            flipTexture = true;
        } else if (w == 0 && h == 0 && flipTexture == true) //restore to original
        {
            uMin = spriteX / width;
            vMin = spriteY / height;
            uMax = (spriteX + spriteWidth) / width;
            vMax = (spriteY + spriteHeight) / height;

            Grid verts = new Grid(2, 2, false);
            verts.set(0, 0, 0.0f, 0.0f, 0.0f, uMin, vMin, null); //0
            verts.set(1, 0, spriteHeight, 0.0f, 0.0f, uMin, vMax, null); //1
            verts.set(0, 1, 0.0f, spriteWidth, 0.0f, uMax, vMin, null); //2
            verts.set(1, 1, spriteHeight, spriteWidth, 0.0f, uMax, vMax, null); //3
            setGrid(verts);
            flipTexture = false;
        }

        if (mTextureID != -1) {
            Grid currentGrid = getGrid();
            currentGrid.generateHardwareBuffers(gl);
        }
    }

    /**
     * Creates a copy of the GLSprite object
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // This should never happen
            throw new InternalError(e.toString());
        }
    }

    /**
     * Returns the width of the sprite
     * @return The width
     */
    public int getWidth() {
        return (int) width;
    }

    /**
     * Returns the height of the sprite
     * @return The height
     */
    public int getHeight() {
        return (int) height;
    }

    /**
     * Returns the texture ID
     * @return The texture ID
     */
    public int getTextureID() {
        return mTextureID;
    }

    /**
     * Sets the texture name
     * @param name The texture ID
     */
    public void setTextureName(int name) {
        mTextureName = name;
    }

    /**
     * Returns the texture name
     * @return The texture ID
     */
    public int getTextureName() {
        return mTextureName;
    }

    /**
     * Sets the vertex grid
     * @param grid The vertex grid
     */
    public void setGrid(Grid grid) {
        mGrid = grid;
    }

    /**
     * Returns the vertex grid
     * @return The vertex grid
     */
    public Grid getGrid() {
        return mGrid;
    }

    /**
     * Draws the sprite
     * @param gl The GL context
     */
    public void draw(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureName);
        // Draw using verts or VBO verts.
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0);
        mGrid.draw(gl, true, false);
        gl.glPopMatrix();
    }


    /**
     * Draws the sprite
     * @param gl The GL context
     * @param angle Angle of rotation.
     */
    public void draw(GL10 gl, float angle, int centerX, int centerY) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureName);

        // Draw using verts or VBO verts.
        gl.glPushMatrix();

        // Rotate.
        gl.glTranslatef(x + centerY, y + centerX, 0);
        gl.glRotatef(angle,0,0,1.0f);
        gl.glTranslatef(-centerY, -centerX, 0);

        mGrid.draw(gl, true, false);
        gl.glPopMatrix();
    }


    /**
     * Draws the sprite at the x,y coordinates
     * @param gl The GL context
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public void draw(GL10 gl, float x, float y) {
        this.x = y;
        this.y = x;
        draw(gl);
    }

    public void draw(GL10 gl, float x, float y, float w, float h) {

        if(w < 0 || h < 0)
            return;

        // Loop through width.
        for(int a = 0; a < w / spriteWidth; a++) {

            // Loop through height.
            for(int b = 0; b < h / spriteHeight; b++) {

                int subW = (int)spriteWidth;
                int subH = (int)spriteHeight;

                cropSprite(gl, 0, 0);

                // Crop width.
                if(a == (int)(w / spriteWidth)) {

                    if(w % (a * spriteWidth) != 0) {
                        subW = (int)(w - a * spriteWidth) + 1;
                    }

                    if(subW < 1) {
                        subW = 1;
                    }

                    cropSprite(gl, subW, subH);
                }

                // Crop height.
                if(b == (int)(h / spriteHeight)) {

                    if(h % (b * spriteHeight) != 0) {
                        subH = (int)(h - b * spriteHeight);
                    }

                    if(subH < 1) {
                        subH = 1;
                    }

                    cropSprite(gl, subW, subH);
                }

                draw(gl, (int)x + a * spriteWidth, (int)y + b * spriteHeight);

            }

        }

    }

    public void draw(GL10 gl, char direction, float x, float y) {

        this.x = y;
        this.y = x;

        if(direction == 'r') {
            flipSprite(gl, true);
        }
        else {
            flipSprite(gl, false);
        }
        draw(gl);
    }

    public void draw(GL10 gl, char direction, float angle, float x, float y, float centerX, float centerY) {

        this.x = y;
        this.y = x;

        if(direction == 'r') {
            flipSprite(gl, true);
        }
        else {
            flipSprite(gl, false);
        }
        draw(gl, angle, (int)centerX, (int)centerY);
    }

    /**
     * Loads a bitmap into OpenGL and sets up the common parameters for 2D texture maps
     * @param gl The GL context
     * @param texturePath The texture path
     * @throws IOException
     */

    /**
     * Loads a bitmap into OpenGL and sets up the common parameters for 2D texture maps
     * @param gl The GL context
     * @param texturePath The texture path
     */
    public int loadBitmap(GL10 gl, String texturePath) throws IOException {
        int textureName = -1;

        if (mAssetManager != null && gl != null) {
            gl.glGenTextures(1, mTextureNameWorkspace, 0);

            textureName = mTextureNameWorkspace[0];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

            InputStream is = null;
            Bitmap bitmap = null;
            try {
                is = mAssetManager.open(texturePath);
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.e("error", e.toString());
            } finally { // handle exception
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }
            if (bitmap != null) {
                width = bitmap.getWidth();
                height = bitmap.getHeight();

                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

                mCropWorkspace[0] = 0;
                mCropWorkspace[1] = bitmap.getHeight();
                mCropWorkspace[2] = bitmap.getWidth();
                mCropWorkspace[3] = -bitmap.getHeight();

                bitmap.recycle();

                ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
                        GL11Ext.GL_TEXTURE_CROP_RECT_OES, mCropWorkspace, 0);
            } else {
                textureName = -1;
            }

            int error = gl.glGetError();
            if (error != GL10.GL_NO_ERROR) {
                Log.e("SpaceWar", "Texture Load GLError: " + error);
            }

        }
        return textureName;
    }

    /**
     * Returns a subimage defined by a specified rectangular region.
     * The returned BufferedImage shares the same data array as the original image.
     * @param x - the X coordinate of the upper-left corner of the specified rectangular region
     * @param y - the Y coordinate of the upper-left corner of the specified rectangular region
     * @param w - the width of the specified rectangular region
     * @param h - the height of the specified rectangular region
     * @return A BufferedImage that is the subimage of this BufferedImage.
     */
    public GLSprite getSubImage(Object gl, int x, int y, int w, int h)
    {
        GLSprite subImage = (GLSprite) this.clone();
        subImage.setVertices((GL10)gl, createClip(x, y, w, h));
        return subImage;
    }

    /**
     * Destroys the textures and release hardware buffers
     * @param gl The GL context
     */
    public void destroy(GL10 gl) {
        if (mTextureID != -1) {
            int[] textureToDelete = new int[1];
            textureToDelete[0] = getTextureName();
            gl.glDeleteTextures(1, textureToDelete, 0);
            setTextureName(0);
            //if (Settings.UseHardwareBuffers) {
            getGrid().releaseHardwareBuffers(gl);
            //}
        }
    }

    /**
     * Constructs a four element array specifying the clipping region
     * @param x The x coordinate
     * @param y The y coordinate
     * @param w The width
     * @param h The height
     * @return The clipping rectangle
     */
    public float[] createClip(float x, float y, float w, float h) {
        float clip[] = {x, y, w, h};
        return clip;
    }
}
