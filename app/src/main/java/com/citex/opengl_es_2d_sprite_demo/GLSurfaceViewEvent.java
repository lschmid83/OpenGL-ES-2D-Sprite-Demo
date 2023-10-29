package com.citex.opengl_es_2d_sprite_demo;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.MotionEvent;
import android.view.KeyEvent;

/**
 * This is a subclass of GLSurfaceView which receives keyboard and touch events.
 *
 *  @version 1.0
 *  @modified 29/10/2023
 *  @author Lawrence Schmid
 *
 *  Original code from:  http://code.google.com/p/apps-for-android/source/browse/trunk/SpriteMethodTest
 */

public class GLSurfaceViewEvent extends GLSurfaceView {

	/** The GL surface view rederer */
    public static GLSurfaceViewRenderer mRenderer;
    
    /**
     * Construct the GLSurfaceViewEvent handler
     * @param context The MainActiviy context
     */
    public GLSurfaceViewEvent(Context context, AssetManager assetManager) {
        super(context);
        mRenderer = new GLSurfaceViewRenderer(assetManager);
        setRenderer(mRenderer);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
    }
   
    /**
     * Called when a touch screen motion event occurs
     * @param event The motion event that occurred 
     * return If you handled the event, return true 
     */
    public boolean onTouchEvent(final MotionEvent event) {
        mRenderer.onTouchEvent(event); 	
    	return true;
    }

    /**
     * Called when a key down event has occurred
     * @param keyCode The value in event.getKeyCode()
     * @param event Description of the key event
     * @return If you handled the event, return true 
     */
    public boolean onKeyDown(int keyCode, final KeyEvent event) {  
    	mRenderer.onKeyDown(event);
		if(keyCode == KeyEvent.KEYCODE_BACK) 
			return true;
		else    	
			return super.onKeyDown(keyCode, event);
    }
    
    /**
     * Called when a key up event has occurred
     * @param keyCode The value in event.getKeyCode()
     * @param event Description of the key event
     * @return If you handled the event, return true 
     */
    public boolean onKeyUp(int keyCode, final KeyEvent event) {
        mRenderer.onKeyUp(event);
        return super.onKeyUp(keyCode, event);
    }
}
