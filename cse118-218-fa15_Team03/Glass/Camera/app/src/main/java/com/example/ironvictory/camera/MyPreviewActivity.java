package com.example.ironvictory.camera;

import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.touchpad.Gesture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Shows a simple camera preview and takes a picture on tap.
 */
public class MyPreviewActivity extends Activity implements GestureDetector.BaseListener {

    private static final String TAG = "MyPreviewActivity";

    private CameraPreview mPreview;
    private Camera mCamera;

    private GestureDetector mDetector;
    private TextView mZoomLevelView;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SmartCamera");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");


        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private final PictureCallback mPictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d(TAG, "Picture taken!");
            // TODO: process the picture.
        }
    };

    Camera.PictureCallback mjpeg = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // copied from http://developer.android.com/guide/topics/media/camera.html#custom-camera
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.v(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                Intent intent = new Intent(AppService.appService(), ImageViewActivity.class);
                intent.putExtra("picturefilepath", pictureFile);
                startActivity(intent);

                finish(); // works! (after card inserted to timeline)

            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCamera = getCameraInstance();

        setContentView(R.layout.activity_main);
        mPreview = (CameraPreview) findViewById(R.id.preview);
        mZoomLevelView = (TextView)findViewById(R.id.zoomLevel);

        mDetector = new GestureDetector(this).setBaseListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPreview.setCamera(mCamera);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPreview.setCamera(null);
        mCamera.release();
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mDetector.onMotionEvent(event);
    }

    @Override
    public boolean onGesture(Gesture gesture) {
        if (gesture == Gesture.TAP) {
            mCamera.takePicture(null, null, mjpeg);
            return true;
        }
        return false;
    }

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
            // Work around for Camera preview issues.
            Camera.Parameters params = camera.getParameters();
            params.setPreviewFpsRange(30000, 30000);
            camera.setParameters(params);
        } catch (Exception e) {
            // cannot get camera or does not exist
            Log.e(TAG, e.getMessage());
        }
        return camera;
    }
}

