package com.citex.opengl_es_2d_sprite_demo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import com.citex.opengl_es_2d_sprites.R;

/**
 *  This class is the main activity context and creates GLSurfaceView which
 *  renders the sprites.
 *
 *  @version 1.0
 *  @modified 29/10/2023
 *  @author Lawrence Schmid
 */

public class MainActivity extends AppCompatActivity {

    /** The surface for displaying OpenGL rendering */
    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the GLSurface view for rendering
        mGLSurfaceView = new GLSurfaceViewEvent(this, getAssets());
        setContentView(mGLSurfaceView);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        mGLSurfaceView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        mGLSurfaceView.onResume();
    }
}