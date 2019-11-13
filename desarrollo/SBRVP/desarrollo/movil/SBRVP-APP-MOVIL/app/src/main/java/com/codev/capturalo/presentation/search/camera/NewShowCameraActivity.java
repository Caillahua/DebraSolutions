package com.codev.capturalo.presentation.search.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codev.capturalo.R;
import com.codev.capturalo.utils.DrawingView;
import com.codev.capturalo.utils.ImagePicker;
import com.codev.capturalo.utils.PathUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class NewShowCameraActivity extends Activity implements OnClickListener {
    private static final String TAG = "ShowCameraActivity";
    NewShowCamera preview;
    FloatingActionButton flashButton, closeButton, captureButton, tipsButton;
    Camera mCamera;
    LinearLayout frameLayout;
    Activity act;
    Context ctx;
    private Bitmap mBitmap;

    private Camera.PictureCallback mPicture;

    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters params;
    ImageView miniaturaButton;

    private DrawingView drawingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.app_take_photo);

        initComponents();

        isFlashOn = false;
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        setHasFlash(hasFlash);

        // Toast.makeText(ctx,"Photo", Toast.LENGTH_LONG).show();

        getImageMini();

        mCamera =  Camera.open();
        mCamera.setDisplayOrientation(90);
        frameLayout = (LinearLayout) findViewById(R.id.surfaceView);
        preview = new NewShowCamera(ctx, mCamera);
        frameLayout.addView(preview);

        preview.setListener(preview);
        //cameraPreview.changeExposureComp(-currentAlphaAngle);
        drawingView = (DrawingView) findViewById(R.id.drawing_surface);
        preview.setDrawingView(drawingView);

     /*   frameLayout.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean arg0, Camera arg1) {
                        //Toast.makeText(act, "Autofocus", Toast.LENGTH_SHORT).show();
                        //mCamera.takePicture(null, null, mPictureCallback);
                    }
                });
                return true;
            }
        });*/
    }

    public void initComponents() {

        // inicializamos el surfaceview para la cámara a tamaño de pantalla completo


        // inicializamos botones y le asignamos evento de click

        captureButton = findViewById(R.id.btn_take_picture);
        closeButton = findViewById(R.id.btn_close);
        flashButton = findViewById(R.id.btn_flash);
        miniaturaButton = findViewById(R.id.btn_miniatura);
        tipsButton = findViewById(R.id.btn_tips);

        frameLayout = findViewById(R.id.surfaceView);

        flashButton.setOnClickListener(this);
        captureButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        miniaturaButton.setOnClickListener(this);
        tipsButton.setOnClickListener(this);
    }

    private void setHasFlash(Boolean hasFlash) {

        if (!hasFlash) {
            // dispositivo no soporta flash
            // muestra mensaje de alerta
            AlertDialog alert = new AlertDialog.Builder(NewShowCameraActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Lo sentimos, su dispositivo no soporta flash!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    //finish();
                    flashButton.setEnabled(false);
                }
            });
            alert.show();
            return;
        }
    }

    /*  @Override public void onResume() { super.onResume();
      try { camera = Camera.open();
          holder.addCallback(this);
          surface.setVisibility(View.VISIBLE); }
          catch (Exception e) { e.printStackTrace(); }
      }
      @Override public void onPause() {
          try { surface.setVisibility(View.GONE);
              holder.removeCallback(this);
              camera.release(); } catch (Exception e)
          { e.printStackTrace();
          } super.onPause(); }*/
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if(mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mPicture = getPictureCallback();
            preview.refreshCamera(mCamera);
            Log.d("nu", "null");
        }else {
            Log.d("nu","no null");
        }

    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                int rotationDegrees = 0;

                ExifInterface exifInterface = null;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        exifInterface = new ExifInterface(new ByteArrayInputStream(data));
                        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotationDegrees = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotationDegrees = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotationDegrees = 270;
                                break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //Bitmap bitmapRedimensionada = redimensionarImagenMaximo(rotateImage(mBitmap,rotationDegrees), 800, 1200);
                int heigth = mBitmap.getHeight();
                Bitmap bitmapRedimensionada;
                if (heigth > 4500) {
                    bitmapRedimensionada = scaleDown(rotateImage(mBitmap, rotationDegrees), String.valueOf(heigth / 3.5), true);
                    ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, NewShowCameraActivity.this, 100);
                } else {
                    if (heigth > 2500) {
                        bitmapRedimensionada = scaleDown(rotateImage(mBitmap, rotationDegrees), String.valueOf(heigth / 2.5), true);
                        ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, NewShowCameraActivity.this, 100);
                    } else {
                        if (heigth > 1500) {
                            bitmapRedimensionada = scaleDown(rotateImage(mBitmap, rotationDegrees), String.valueOf(heigth / 1.5), true);
                            ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, NewShowCameraActivity.this, 100);
                        }else{
                            ImagePicker.saveToChangesToExternalCacheDir(rotateImage(mBitmap, rotationDegrees), NewShowCameraActivity.this, 100);
                        }
                    }
                }
         /*
            if (heigth>5000){

            }else{


            }*/


                //ImagePicker.saveToChangesToExternalCacheDir(mBitmap, ShowCameraActivity.this, 100);


                initPreview();

                resetCam();
            }
        };
        return picture;
    }

    private int getCamera() {
        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

            for (int cameraId = 0; cameraId < numberOfCameras; cameraId++) {
                Camera.getCameraInfo(cameraId, cameraInfo);

                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    return cameraId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCamera != null) {
           // resetCam();
        }
    }

    @Override
    protected void onPause() {
        turnOffFlash();
       /* if (mCamera != null) {
            mCamera.release();
            mCamera = null;

        }*/
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        turnOffFlash();
/*
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;

        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        turnOffFlash();

       /* if (mCamera != null) {
            mCamera.release();
            mCamera = null;

        }*/
    }

    private void resetCam() {
        mCamera.startPreview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_picture:
                captureImage();
                break;
            case R.id.btn_flash:
                params = mCamera.getParameters();
                if (isFlashOn) {
                    isFlashOn = false;
                    flashButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
                } else {
                    isFlashOn = true;
                    flashButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on));
                }
                break;
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_miniatura:
                openGaleria();
                break;
            case R.id.btn_tips:
                //DialogTips dialogTips = new DialogTips(this, 2);
                //dialogTips.show();
                break;
        }
    }


    private void captureImage() {
        if (mCamera != null) {
            if (isFlashOn) {
                turnOnFlash();
            } else {
                mCamera.takePicture(null, null, getPictureCallback());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    Uri path = data.getData();
                    //String newPath = path.getPath();
                    String filePath = null;
                    try {
                        filePath = PathUtil.getPath(getApplicationContext(), path);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    int rotationDegrees = 0;

                    ExifInterface exifInterface = null;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            exifInterface = new ExifInterface(filePath);

                            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotationDegrees = 90;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotationDegrees = 180;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotationDegrees = 270;
                                    break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    // Bitmap bitmapRedimensionada = redimensionarImagenMaximo(rotateImage(mBitmap,rotationDegrees), 800, 1200);
                    //Bitmap bitmapRedimensionada = Bitmap.createScaledBitmap(rotateImage(mBitmap,rotationDegrees), 800, 1200, true);
                    //ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, ShowCameraActivity.this, 100);
                    int heigth = mBitmap.getHeight();
                    Bitmap bitmapRedimensionada;
                    if (heigth > 4500) {
                        bitmapRedimensionada = scaleDown(rotateImage(mBitmap, rotationDegrees), String.valueOf(heigth / 3.5), true);
                        ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, NewShowCameraActivity.this, 100);
                    } else {
                        if (heigth > 2500) {
                            bitmapRedimensionada = scaleDown(rotateImage(mBitmap, rotationDegrees), String.valueOf(heigth / 2.5), true);
                            ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, NewShowCameraActivity.this, 100);
                        } else {
                            if (heigth > 1500) {
                                bitmapRedimensionada = scaleDown(rotateImage(mBitmap, rotationDegrees), String.valueOf(heigth / 1.5), true);
                                ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, NewShowCameraActivity.this, 100);
                            }else{
                                ImagePicker.saveToChangesToExternalCacheDir(rotateImage(mBitmap, rotationDegrees), NewShowCameraActivity.this, 100);
                            }
                        }

                    }

                    //ImagePicker.saveToChangesToExternalCacheDir(mBitmap, ShowCameraActivity.this, 100);

                    initPreview();
                   /* Uri path = data.getData();
                    //String newPath = path.getPath();
                    String filePath = null;
                    try {
                        filePath = PathUtil.getPath(getApplicationContext(), path);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(ShowCameraActivity.this, PreviewActivity.class);
                    intent.putExtra("filePath", filePath);
                    startActivity(intent);*/
                    //finish();
                    break;
            }
        }
    }


    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        //img.recycle();
        return rotatedImg;
    }

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        } else {
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");
            if (!folder_gui.exists()) {
                folder_gui.mkdirs();
            }

            File outputFile = new File(folder_gui, "temp.jpg");
            return outputFile;
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, String maxImageSize,
                                   boolean filter) {
        float newMax = Float.valueOf(maxImageSize);
        float ratio = Math.min(
                (float) newMax / realImage.getWidth(),
                (float) newMax / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth) {
        //Redimensionamos
        float width = mBitmap.getWidth();
        float height = mBitmap.getHeight();
        float scaleWidth = (newWidth) / width;
        float scaleHeight = (newHeigth) / height;
        // create a matrix for the manipulation
        // Matrix matrix = new Matrix();
        // resize the bit map
        //matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap imagenFinal = Bitmap.createScaledBitmap(mBitmap, 800, mBitmap.getHeight() * (int) scaleWidth, false);
        //return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
        return imagenFinal;
    }


    private void turnOnFlash() {
        if (mCamera == null || params == null) {
            return;
        }
        // play sound
        params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);

        mCamera.setParameters(params);
        mCamera.startPreview();
        mCamera.takePicture(null, null, getPictureCallback());

        // isFlashOn = true;
        //flashButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on));
        // changing button/switch image

    }

    private void turnOffFlash() {
        if (mCamera == null || params == null) {
            return;
        }
        // play sound

        params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);

         /*   if(!isFlashOn){
                mCamera.takePicture(null, null, mPictureCallback);
            }
*/
        // mCamera.stopPreview();
        // isFlashOn = false;

        // changing button/switch image
    }

    public static final int PICK_IMAGE = 1;

    private void openGaleria() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }

    private void initPreview() {

        Intent intent = new Intent(NewShowCameraActivity.this, PreviewActivity.class);
        //intent.putExtra("filePath", mPath);
        turnOffFlash();
        startActivity(intent);
        finish();
    }

    String[] projection = new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
    };

    private void getImageMini() {

        final Cursor cursor = getApplicationContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

// Put it in the image view
        if (cursor.moveToFirst()) {
            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {   // TODO: is there a better way to do this?

                File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File image = new File(imageFile.getPath());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                //  bitmap = Bitmap.createScaledBitmap(bitmap,miniaturaButton.getWidth(),miniaturaButton.getHeight(),true);
                miniaturaButton.setImageBitmap(bitmap);
            }
        }
    }
}