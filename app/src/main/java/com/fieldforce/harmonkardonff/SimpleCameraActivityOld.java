package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.entitymodels.ImageInfo;
import app.core.model.ImageCaptureResult;
import app.core.server.FileUploader;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.services.WebService;


public class SimpleCameraActivityOld extends InnosolsActivity implements View.OnClickListener {

    @Override
    public void RegisterTableInfoForLocalDB() {

    }

    public static final String PARAMS_GUID = "GUID";
    public static final String PARAMS_USERNAME = "UserName";
    public static final String PARAMS_DOC_TYPE = "DocType";
    public static final String PARAMS_MODEL_ID = "PARAMS_MODEL_HEADERID";
    public static final String PARAMS_INSTANCE = "KEYS_CAPTURE_IMAGE";
    public static final String PARAMS_DOC_TITLE = "PARAMS_DOC_TITLE";
    public static final String ACTION_ENABLE_GPS = "ACTION_ENABLE_GPS";
    private ImageView imageView;
    private Button buttonClick;
    private Button buttonReAttach;
    private Button buttonUpload;
    private TextView textViewError;
    private TextView textViewCaptureImageTitle;
    private boolean isImageReadyForUpload = false;
    private String docType = "";
    private String DocType = "NA";
    private String DocTitle = "NA";
    private String GUID = "NA";
    private String ModelID = "NA";
    private String UserName = "NA";
    private String DocID = "NA";
    private boolean IsDebugToast = false;
    private boolean ISimageUpload = false;
    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_camera);
        init();
        updateTitle();
        manageVisibility();
        attachListener();
    }

    private void init() {
        docType = getIntent().getStringExtra("docType");
        GUID = getIntent().getStringExtra(SimpleCameraActivityOld.PARAMS_GUID);
        DocType = getIntent().getStringExtra(SimpleCameraActivityOld.PARAMS_DOC_TYPE);
        DocTitle = getIntent().getStringExtra(SimpleCameraActivityOld.PARAMS_DOC_TITLE);
        UserName = getIntent().getStringExtra(SimpleCameraActivityOld.PARAMS_USERNAME);
        ModelID = getIntent().getStringExtra(SimpleCameraActivityOld.PARAMS_MODEL_ID);
        DocID = GetTimeStamp() + "_" + WebService.UserName;
        //  IsDebugToast = getIntent().getBooleanExtra(ImageActivity.DEBUG_SHOW_TOAST, false);
        imageView = findViewById(R.id.iv_storeImageFromCamera);
        buttonReAttach = findViewById(R.id.btn_reAttach);
        buttonClick = findViewById(R.id.btn_click);
        buttonUpload = findViewById(R.id.btn_upload);
        textViewError = findViewById(R.id.tv_errorMessage);
        textViewCaptureImageTitle = findViewById(R.id.tv_captureImageTitle);
    }

    private void updateTitle() {
        textViewCaptureImageTitle.setText("Attach Image");
    }

    private void manageVisibility() {
        showView(imageView);
        showView(buttonClick);
        hideView(buttonReAttach);
        hideView(buttonUpload);
        hideView(textViewError);
    }

    private void attachListener() {
        buttonReAttach.setOnClickListener(this);
        buttonClick.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    Glide.with(this).load(photoURI).into(imageView);
                    hideView(buttonClick);
                    hideView(textViewError);
                    showView(buttonUpload);
                    showView(buttonReAttach);
                    isImageReadyForUpload = true;
                    compressImage(this, photoURI);

                } catch (Exception ex) {
                    textViewError.setText("ERROR: " + ex.getMessage());
                    showView(textViewError);
                }
            }
        }
    }

    FileUploader uploader = null;

    private void prepareUploadTask() {
        if(DocType.equalsIgnoreCase("Hygiene"))
            uploader = new FileUploader().setFileUploadUrl("http://harman.infield.co.in/FileUploader/ISD/Harman_Kardon/FloorHygieneHandler.ashx?");
            else
            uploader = new FileUploader().setFileUploadUrl("http://harman.infield.co.in/UploadFileHandler.ashx?");
    }


    private ArrayList<ImageInfo> Images = new ArrayList<ImageInfo>();
    private ImageCaptureResult _ImageCaptureResult = new ImageCaptureResult();
    ;
    private Intent intent = new Intent();

    @Override
    public void onBackPressed() {
        _ImageCaptureResult.setIsImageCaptured(photoURI != null);
        setCustomIntent();
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private void setCustomIntent() {
        if (intent == null)
            intent = new Intent();
        String uri="";
        if(photoURI!=null){
            intent.putExtra(SimpleCameraActivityOld.PARAMS_GUID, GUID);
            intent.putExtra("IMAGE_CAPTURED", photoURI.toString());
            intent.putExtra("DocID", DocID);
        }

        else
        {
            uri="";
            intent.putExtra(SimpleCameraActivityOld.PARAMS_GUID, GUID);
            intent.putExtra("IMAGE_CAPTURED", uri);
            intent.putExtra("DocID", "");
        }

        //putData(ImageActivity.IMAGE_CAPTURE_RESULT, this._ImageCaptureResult.setResult(GetTimeStamp()+"_"+WebService.UserName, GUID));
    }
    //---------------------------------------------

    private static final String TAG = "SimpleCameraActivity";

    public void uploadImage() {
        prepareUploadTask();
        if (!isImageReadyForUpload) {
            ShowToastLong("Image is crashed!", 0);
            return;
        }
        if (!isNetworkFoundDialog())
            return;
        final BackgroundProcess bp = new BackgroundProcess(this)
                .setProgressDialog(true).showProgressType(true);
        bp.setbackgroundProcess(new IProcess() {

            @Override
            public Object underProcess() throws Exception {
                String response = null;
                uploader.RemoveAllParameters();
                uploader.addParamters("HeaderID", GUID);
                uploader.addParamters("DocTitle", "NA");
                uploader.addParamters("UploadedBy", WebService.UserName);
                uploader.addParamters("DocID", DocID);
                uploader.addParamters("DocType", DocType);
                uploader.addParamters("Remarks", "Na");
                uploader.addParamters("Latitude", "Na");
                uploader.addParamters("Longitude", "Na");
                if (photoURI.getPath() != null) {
                    Log.d(TAG, "underProcess: " + photoURI.getPath());
                    //response = uploader.Uploadfile(new File(photoURI.getPath()), bp.getProgessDialog());
                    response = uploader.Uploadfile(photoFile, bp.getProgessDialog());
                    Log.d(TAG, "underProcess: " + response);
                } else {
                    response = "Can't create file from URI";
                }
                return response;
            }

            @Override
            public void processResponse(Object response) throws Exception {
                try {
                    if (response != null) {
                        if (response.toString().startsWith("Success")) {
                            /*updateImageStatus("false");
                            // intent.putExtra(ImageActivity.IMAGE_UPLOADED,
                            // true);
                            intent.putExtra(ImageActivity.IMAGE_UPLOADED, true);
                            _ImageCaptureResult.setIsImageUploaded(true);*/
                            _ImageCaptureResult.setIsImageUploaded(true);
                            ISimageUpload = true;
                            ShowToast("file uploaded successfully!");
                        } else {
                            ShowToastLong("Unable to upload file!", 0);
                            ShowToastLong("Error: " + response, 0);
                            new Dialog(SimpleCameraActivityOld.this).setTitle("Message").show(response.toString());
                        }
                    } else
                        ShowToast("Unable to upload file");
                } catch (Exception ex) {
                    ShowToastLong(ex.getMessage(), 0);
                }

            }
        }).execute();
    }


    //--------------------------------------------


    public String GetTimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        return timeStamp;
    }

    public String setImageDirectoryName() {
        return "MotorolaAppImages";
    }


    private File createImageFile() throws IOException {
        // Create a unique file name for image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        //Getting a reference to Target storage directory (ComioMerchImages)
        //File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants_of_Project.IMAGE_fOLDER);
        File storageDir = getExternalFilesDir("MotorolaAppImages");
        ///Creating directory if not made already
        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                Toast.makeText(this, "Directory can't be made", Toast.LENGTH_SHORT).show();
            }
            storageDir.mkdir();
        }
        return new File(storageDir, imageFileName + ".jpg");
    }

    static final int CAMERA_REQUEST = 101;
    static final int STORAGE_REQUEST = 102;
    static final int REQUEST_TAKE_PHOTO = 202;
    private Uri photoURI;
    File photoFile = null;

    private void dispatchTakePictureIntent() {
        if (CameraUtils.isExternalStorageWritable()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    String authority = getApplicationContext().getPackageName() + ".fileprovider";
                    Log.i("authW", authority);
                    photoURI = FileProvider.getUriForFile(this, authority, photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    Log.i("authW", "uri -> " + photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_click) {
            if (DocType.equalsIgnoreCase("SaleEnter")) {
                dispatchTakePictureIntent();
            } else {
                Intent cameraActivity = new Intent(SimpleCameraActivityOld.this, CameraActivity.class);
                startActivityForResult(cameraActivity, REQUEST_TAKE_PHOTO);
            }

        } else if (id == R.id.btn_reAttach) {
            if (DocType.equalsIgnoreCase("SaleEnter")) {
                dispatchTakePictureIntent();
            } else {
                Intent cameraActivity = new Intent(SimpleCameraActivityOld.this, CameraActivity.class);
                startActivityForResult(cameraActivity, REQUEST_TAKE_PHOTO);
            }

        } else if (id == R.id.btn_upload) {
            if (!photoURI.equals("")) {
                buttonUpload.setEnabled(false);
            }
            uploadImage();
        }
    }


    private Bitmap compressImage(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int size = 0;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            if (bm != null) {
                size = bm.getWidth();
            }
            i++;
        } while (size < minWidthQuality && i < sampleSizes.length);
        OutputStream os;
        try {
            os = new FileOutputStream(photoFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, os);
            os.flush();
            os.close();
        } catch (Exception ignored) {
        }
        return bm;
    }

    private Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;


        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
        }

        Bitmap actuallyUsableBitmap = null;
        if (fileDescriptor != null) {
            actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor.getFileDescriptor(), null, options);
        }

//        Log.d(TAG, options.inSampleSize + " sample method bitmap ... " +
//                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }

}