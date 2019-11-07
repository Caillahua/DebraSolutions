package com.codev.capturalo.presentation.search.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codev.capturalo.PackageManagerUtils;
import com.codev.capturalo.R;
import com.codev.capturalo.data.local.SessionManager;
import com.codev.capturalo.presentation.products.ProductsActivity;
import com.codev.capturalo.utils.ImagePicker;
import com.codev.capturalo.utils.TouchImageView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.WebEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.rv_progress_bar)
    RelativeLayout rvProgressBar;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.container_cancelar)
    ConstraintLayout containerCancelar;
    @BindView(R.id.background)
    ConstraintLayout background; //CameraContract.View {

    private TouchImageView mPreview;
    private FloatingActionButton mSendImage, mClose, mTips;
    private String mPath;
    private Bitmap image;
    private Intent CropIntent;
    private Uri uri;
    private SessionManager mSessionManager;
    // private CameraContract.Presenter mPresenter;
    Bitmap bitmap;
    byte[] b;

    private static int REQUEST_GET_SINGLE_FILE = 1000;
    private static final String CLOUD_VISION_API_KEY = "AIzaSyDr_uWO8Cz-RgglkUY4NbueOgu2NWzi9Ck";
    private static final int MAX_DIMENSION = 1200;
    private static final int MAX_LABEL_RESULTS = 10;

    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    float scalediff;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_preview_photo);
        ButterKnife.bind(this);

        //mPresenter = new CameraService(this, getApplicationContext());
        mSessionManager = new SessionManager(getApplicationContext());
        mPreview = (TouchImageView) findViewById(R.id.im_preview);
        mSendImage = (FloatingActionButton) findViewById(R.id.btn_send_photo);

        mClose = (FloatingActionButton) findViewById(R.id.imbtn_close);

        mTips = findViewById(R.id.btn_tips);

        // mPath = (String) getIntent().getExtras().get("filePath");

        //File image = new File(mPath);


        Bitmap bitmap = ImagePicker.getImageFromResult(getApplicationContext());

        System.out.println("------------------>" + mPath);

        mPreview.setImageBitmap(bitmap);

        //Glide.with(getApplicationContext()).load(image).dontTransform().into(mPreview);
        mClose.setOnClickListener(this);
        mSendImage.setOnClickListener(this);
        mTips.setOnClickListener(this);

    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imbtn_close:
                finish();
                Intent intent = new Intent(PreviewActivity.this, NewShowCameraActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_tips:
                //DialogTips dialogTips = new DialogTips(this, 3);
                //dialogTips.show();
                break;
            case R.id.btn_send_photo:
                //  String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                File image = ImagePicker.getTempFile(getApplicationContext());
                //mSessionManager.sendCaptureImage(image.getAbsolutePath());
                Uri selectedImageUri = Uri.fromFile(image);

                //Uri selectedImageUri = data.getData();
                // Get the path from the Uri
                //final String path = image.getAbsolutePath();
                //if (path != null) {
                //   File f = new File(path);
                //   selectedImageUri = Uri.fromFile(f);
                //}
                // Set the image in ImageView
                //viewImage.setImageURI(selectedImageUri);

                uploadImage(selectedImageUri);
                mPreview.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                containerCancelar.setVisibility(View.GONE);
                background.setVisibility(View.GONE);
                rvProgressBar.setVisibility(View.VISIBLE);
                //finish();
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                callCloudVision(bitmap);
                //viewImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d("Error image upload", "Image picking failed because " + e.getMessage());
                Toast.makeText(this, "Error al obtener imagen", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("Error image upload", "Image picker gave us a null image.");
            Toast.makeText(this, "Error de imagen null", Toast.LENGTH_LONG).show();
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("WEB_DETECTION");
                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d("CLOUD VISION", "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private static class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<PreviewActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;
        private Activity activity;
        SessionManager mSessionManager;

        LableDetectionTask(PreviewActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
            this.activity = activity;
            mSessionManager = new SessionManager(activity.getApplicationContext());
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("CLOUD VISION", "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                mSessionManager.sendValueImage(convertResponseToString(response));
                Intent intent = new Intent(activity, ProductsActivity.class);
                activity.startActivity(intent);
                activity.finish();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d("CLOUD VISION", "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d("CLOUD VISION", "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            PreviewActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                //TextView imageDetail = activity.findViewById(R.id.image_details);
                // imageDetail.setText(result);
                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        //mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d("CLOUD VISION", "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder();
        List<WebEntity> labels = response.getResponses().get(0).getWebDetection().getWebEntities();
        if (labels != null) {
            for (int i = 0; i <labels.size() ; i++) {
                if(i == labels.size()-1){
                    message.append(labels.get(i).getDescription());
                }else{
                    message.append(labels.get(i).getDescription()+ ",");
                }

            }
        } else {
            message.append("nothing");
        }
        return message.toString();
        //return message.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);

    }
}
