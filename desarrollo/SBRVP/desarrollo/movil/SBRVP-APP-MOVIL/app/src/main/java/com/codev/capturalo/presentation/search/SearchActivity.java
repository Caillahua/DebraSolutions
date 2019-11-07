package com.codev.capturalo.presentation.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.codev.capturalo.R;
import com.codev.capturalo.core.BaseActivity;
import com.codev.capturalo.presentation.main.tabs.PrincipalSeekerFragment;
import com.codev.capturalo.presentation.search.camera.NewShowCameraActivity;
import com.codev.capturalo.presentation.search.camera.PreviewActivity;
import com.codev.capturalo.utils.ActivityUtils;
import com.codev.capturalo.utils.ImagePicker;
import com.codev.capturalo.utils.PathUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.IOException;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by junior on 01/08/17.
 */

public class SearchActivity extends BaseActivity implements OnTabSelectListener {


    private static final int MENU_HOME = 1;
    private static final int MENU_SEARCH = 2;
    private static final int MENU_GALERY = 3;
    private Bitmap mBitmap;

    @BindView(R.id.contentContainer_1)
    FrameLayout contentContainer1;
    @BindView(R.id.contentContainer_2)
    FrameLayout contentContainer2;
    @BindView(R.id.contentContainer_3)
    FrameLayout contentContainer3;
    @BindView(R.id.contentContainer_4)
    FrameLayout contentContainer4;
    @BindView(R.id.contentContainer_5)
    FrameLayout contentContainer5;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    private boolean isFirst = true;

    @Override
    protected void onResume() {
        super.onResume();
        bottomBar.selectTabAtPosition(1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        ButterKnife.bind(this);
        bottomBar.setOnTabSelectListener(this);



        PrincipalSeekerFragment principalSeekerFragment = (PrincipalSeekerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentContainer_2);

        if (principalSeekerFragment == null) {
            principalSeekerFragment = PrincipalSeekerFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    principalSeekerFragment, R.id.contentContainer_2);
        }

        bottomBar.selectTabAtPosition(1);
        selectedFragment(MENU_SEARCH);
    }


    private void selectedFragment(int option) {

        contentContainer2.setVisibility(option == MENU_SEARCH ? View.VISIBLE : View.GONE);

    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_camera:
                if(isFirst){
                    isFirst = false;
                }else{
                    if (reviewPermission()) {
                        Intent intent = new Intent(this, NewShowCameraActivity.class);
                        startActivity(intent);
                    } else {
                        //Nuestra app no tiene permiso, entonces debo solicitar el mismo
                        requestPermission();
                    }
                }
                break;
            case R.id.tab_search:
                selectedFragment(MENU_SEARCH);
                break;
            case R.id.tab_galery:
                openGaleria();
                break;
            default:
                break;
        }
    }


    public static final int PICK_IMAGE = 1;

    private void openGaleria() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }


    private boolean reviewPermission() {
        //Array de permisos
        String[] permisos = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String perms : permisos) {
            int res = checkSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;

    }

    private static final int CODIGO_SOLICITUD_PERMISO = 123;

    private void requestPermission() {

        String[] permisos = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {         //Verificamos si la version de android del dispositivo es mayor
            ActivityCompat.requestPermissions(this, permisos, CODIGO_SOLICITUD_PERMISO);  //o igual a MarshMallow
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean autorizado = true;   //Si el permiso fue autorizado

        switch (requestCode) {
            case CODIGO_SOLICITUD_PERMISO:
                for (int res : grantResults) {
                    //si el usuario concedió todos los permisos
                    autorizado = autorizado && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;

            default:
                //Si el usuario autorizó los permisos
                autorizado = false;
                break;
        }

        if (autorizado) {
            //Si el usuario autorizó todos los permisos podemos ejecutar nuestra tarea
            Intent intent = new Intent(this, NewShowCameraActivity.class);
            startActivity(intent);

        } else {
            //Se debe alertar al usuario que los permisos no han sido concedidos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Tiene que aceptar los permisos para continuar", Toast.LENGTH_SHORT).show();
                }
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
                        ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, SearchActivity.this, 100);
                    } else {
                        if (heigth > 2500) {
                            bitmapRedimensionada = scaleDown(rotateImage(mBitmap, rotationDegrees), String.valueOf(heigth / 2.5), true);
                            ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, SearchActivity.this, 100);
                        } else {
                            if (heigth > 1500) {
                                bitmapRedimensionada = scaleDown(rotateImage(mBitmap, rotationDegrees), String.valueOf(heigth / 1.5), true);
                                ImagePicker.saveToChangesToExternalCacheDir(bitmapRedimensionada, SearchActivity.this, 100);
                            }else{
                                ImagePicker.saveToChangesToExternalCacheDir(rotateImage(mBitmap, rotationDegrees), SearchActivity.this, 100);
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

    private void initPreview() {

        Intent intent = new Intent(SearchActivity.this, PreviewActivity.class);
        //intent.putExtra("filePath", mPath);
        startActivity(intent);
        finish();
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


    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        //img.recycle();
        return rotatedImg;
    }



}
