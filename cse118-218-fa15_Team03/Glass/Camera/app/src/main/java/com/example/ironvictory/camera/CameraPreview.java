package com.example.ironvictory.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.AdapterView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Shows a simple camera preview.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private boolean mIsPreviewing;

    public CameraPreview(Context context) {
        this(context, null, 0);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraPreview(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        getHolder().addCallback(this);
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        updatePreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        updatePreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder = null;
        updatePreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Nothing to do here.
    }

    private void updatePreview() {
        boolean shouldPreview = mHolder != null && mCamera != null;

        if (shouldPreview != mIsPreviewing) {
            if (shouldPreview) {
                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                    mIsPreviewing = true;
                } catch (IOException e) {
                    // left blank for now
                }
            } else {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                }
                mIsPreviewing = false;
            }
        }
    }

}

