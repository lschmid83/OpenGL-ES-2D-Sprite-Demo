package com.citex.opengl_es_2d_sprite_demo;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * An OpenGL ES renderer based on the GLSurfaceView rendering framework. This
 * class is responsible for drawing a list of renderables to the screen every
 * frame. It also manages loading of textures and (when VBOs are used) the
 * allocation of vertex buffer objects.
 *
 *  @version 1.0
 *  @modified 29/10/2023
 *  @author Lawrence Schmid
 *
 *  Original code from:  http://code.google.com/p/apps-for-android/source/browse/trunk/SpriteMethodTest
 */

public class GLSurfaceViewRenderer implements GLSurfaceView.Renderer {

    /** Specifies the format our textures should be converted to upon load. */
    private BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();

    /** The device screen size */
    public static int mWidth, mHeight;

	/** Delta time frame rate variables */
	private long time1, time2;

    /** Provides access to an application's raw asset files */
    private AssetManager mAssetManager;

    /** The drawing panel */
    private GamePanel mGamePanel;

    /**
     * Constructs the GLSurfaceViewRenderer
     */
    public GLSurfaceViewRenderer(AssetManager assetManager) {
        // Set our bitmaps to 16-bit, 565 format.
        sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mAssetManager = assetManager;
    }

    /**
     * Called whenever the surface is created.  This happens at startup, and
     * may be called again at runtime if the device context is lost (the screen
     * goes to sleep, etc).  This function must fill the contents of vram with
     * texture data and (when using VBOs) hardware vertex arrays.
     * @param gl The GL context
     */
    public void surfaceCreated(GL10 gl) {
        /*
         * Some one-time OpenGL initialization can be made here probably based
         * on features of this particular context
         */
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        gl.glShadeModel(GL10.GL_FLAT);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glDepthMask(false);
        gl.glDisable(GL10.GL_DITHER);
        gl.glDisable(GL10.GL_LIGHTING);

        mGamePanel = new GamePanel(gl, mAssetManager);
    }

    /**
     * Called when the size of the window changes.
     * @param gl The GL context
     * @param width The width of the window
     * @param height The height of the window
     */
    public void sizeChanged(GL10 gl, int width, int height) {

        mWidth = width;
        mHeight = height;

        // Set the viewport
        gl.glViewport(0, 0, width, height);

        // Set our projection matrix. This doesn't have to be done each time we
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(mGamePanel.getResolutionHeight(), 0.0f, 480f, 0.0f, 0.0f, 1.0f);

        // Scale the screen to the resolution.
        gl.glScalef( mGamePanel.getResolutionHeight() / mGamePanel.getResolutionWidth(), mGamePanel.getResolutionWidth() / mGamePanel.getResolutionHeight(), 1);
        gl.glTranslatef(mGamePanel.getResolutionWidth(), 0.0f, 0.0f);

        // Rotate the screen so top left is 0,0
        gl.glRotatef(-270.0f, 0.0f, 0.0f, 1.0f);

        // Enable shader.
        gl.glShadeModel(GL10.GL_FLAT);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
        gl.glEnable(GL10.GL_TEXTURE_2D);
    }

    /**
     * Draws the sprites
     * @param gl The GL context
     */
    public void drawFrame(GL10 gl) {

    	time2 = System.currentTimeMillis(); // Get current time
		float delta = (int) (time2 - time1); // Calculate how long it's been since last updated

        if(delta < 0)
            delta = 0.01f;

        Grid.beginDrawing(gl, true, false);
       	
        // Draw the game panel
        mGamePanel.paintComponent(gl, delta);

        Grid.endDrawing(gl);

        time1 = time2; // Update our time variables.             
    }

    /**
     * Gets the returns a standard configuration
     */
    public int[] getConfigSpec() {
        // We don't need a depth buffer, and don't care about our
        // color depth.
        int[] configSpec = {EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE};
        return configSpec;
    }

    /**
     * Called when the rendering thread shuts down.  This is a good place to
     * release OpenGL ES resources.
     * @param gl
     */
    public void shutdown(GL10 gl) {
    	mGamePanel.destroy(gl);
    	
    }
    
    /**
     * Pass the key down event to the GamePanel
     * @param event Description of the key event
     */
    public void onKeyDown(KeyEvent event) {
        mGamePanel.onKeyDown(event);
    }

    /**
     * Called when a touch screen motion event occurs
     * @param event The motion event that occurred 
     */
    public void onTouchEvent(final MotionEvent event) {
    	mGamePanel.onTouchEvent(event); 	
    }
    
    /**
     * Pass the key up event to the GamePanel
     * @param event Description of the key event
     */
    public void onKeyUp(KeyEvent event) {
        mGamePanel.onKeyUp(event);
    }
}
