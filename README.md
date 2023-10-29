# OpenGL-ES-2D-Sprite-Demo

This is an OpenGL ES 2D hardware accelerated sprite demo built in Android Studio. It demonstrates animated sprites with rotations and touch screen controls.

You will find you can load an image using the GLSprite class or entire sprite sheets containing frames of animations of different sizes and even change the display resolution of the screen. The sprite movement is controlled by a delta time calculated from the time between frame updates so that the demo runs at the same speed on different hardware configurations. 

Compiling the Android Mobile Version
====================================

Download the required software here:

[Android Studio](https://drive.google.com/file/d/1ZwvjCGVGCP0qfyri5DHjviEQRVc_IOu1/view?usp=drive_link)

1. Install Android Studio
2. Open the OpenGL-ES-2D-Sprite-Demo project
3. Select Tools -> SDK Manager -> SDK Platforms tab and ensure Android API 34 is installed
4. Select Tools -> SDK Manager -> SDK Tools tab and ensure Android SDK Build-Tools 34 is installed
5. If there are still build errors try selecting File -> Invalidate Caches...

You should now be able to press the run button and launch the application on a connected device.

If you want to test the application using a virtual device:

1. Select Tools -> Device Manager
2. Click Create Device
3. Choose a device. I recommend Nexus S
4. Download a system image 
5. Give the device a name and click Finish

You should now be able to press the run button and launch the application on a AVD.









