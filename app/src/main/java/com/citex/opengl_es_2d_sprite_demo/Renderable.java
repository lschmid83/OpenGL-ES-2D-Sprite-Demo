package com.citex.opengl_es_2d_sprite_demo;

/**
 * Base class defining the core set of information necessary to render (and move
 * an object on the screen.  This is an abstract type and must be derived to
 * add methods to actually draw (see CanvasSprite and GLSprite).
 *
 *  @version 1.0
 *  @modified 29/10/2023
 *  @author Lawrence Schmid
 *
 *  Original code from: http://code.google.com/p/apps-for-android/source/browse/trunk/SpriteMethodTest
 */

public abstract class Renderable {

	/** The x coordinate */
    public float x;

    /** The y coordinate */
    public float y;

    /** The x coordinate of the clipped region of the image */
    public float spriteX;

    /** The y coordinate of the clipped region of the image */
    public float spriteY;

    /** The width of the image */
    public float width;

    /** The height of the image */
    public float height;

    /** The width of the clipped region of the image */
    public float spriteWidth;

    /** The height of the clipped region of the image */
    public float spriteHeight;
}
