package com.codev.capturalo.presentation.search.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceHolder holder;
    List<Camera.Size> mSupportedPreviewSizes;
    Camera.Size mPreviewSize;



    public ShowCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if(camera!=null){

            try {
                camera.stopPreview();
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e){
                camera.release();
                e.printStackTrace();
            }

            Camera.Parameters parameters = camera.getParameters();

            mSupportedPreviewSizes = parameters.getSupportedPictureSizes();

            int maxWidth = 0;
            int maxHeight = 0;

            for (Camera.Size size : mSupportedPreviewSizes) {
                if (size.width > maxWidth || size.height > maxHeight) {
                    maxWidth = size.width;
                    maxHeight = size.height;
                }
            }

            parameters.setPictureSize(maxWidth, maxHeight);

            if(this.getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE){
                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
            }else{
                parameters.set("orientation", "landscape");
                camera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }


            setCamFocusMode(parameters);
            //
            camera.setParameters(parameters);


        }


    }
    private void setCamFocusMode(Camera.Parameters parameters){

        if(null == camera) {
            return;
        }

        /* Set Auto focus */
        List<String>    focusModes = parameters.getSupportedFocusModes();
        if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else
        if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }/*else
        if(focusModes.contains(Camera.Parameters.FOCUS_MODE_FIXED)){
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
        }*/
        //camera.setParameters(parameters);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.set("jpeg-quality", 100);
        parameters.set("orientation", "portrait");
        parameters.setRotation(90);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

            Camera.Size optimalSize = getOptimalPreviewSizeMoto(sizes, width, height);
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);
            optimalSize = getOptimalPreviewSizeMoto(parameters.getSupportedPictureSizes(), width, height);
            parameters.setPictureSize(optimalSize.width, optimalSize.height);
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N){
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

            Camera.Size optimalSize = getOptimalPreviewSize(sizes, width, height);
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);
            optimalSize = getOptimalPreviewSize(parameters.getSupportedPictureSizes(), width, height);
            parameters.setPictureSize(optimalSize.width, optimalSize.height);
        }
         //optimalSize = getOptimalPreviewSize(parameters.getSupportedPictureSizes(), width, height);
        //parameters.setPictureSize(optimalSize.width, optimalSize.height);
        camera.setParameters(parameters);
        try {
            camera.startPreview();
        } catch (Exception e) {
            Log.e("ShowCamera", "Could not start preview", e);
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            //camera.re();
        }
    }


    private Camera.Size getOptimalPreviewSizeMoto(List<Camera.Size> sizes, int w, int h) {
        Camera.Size  bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size  s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
