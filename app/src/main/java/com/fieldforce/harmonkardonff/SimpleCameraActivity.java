package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MotionEvent;

import androidx.annotation.Nullable;


import com.suveyform.ImageResponse;
import com.suveyform.ui.fragments.questions_fragment.CoronaNewQuestionnaireFragment;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
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
import mob.field.gcm.AsynResponse;
import mob.field.harmonkardonff.services.WebService;

import static com.suveyform.ui.fragments.questions_fragment.CoronaNewQuestionnaireFragment.imageResponses;


public class SimpleCameraActivity extends InnosolsActivity implements View.OnClickListener {

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
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

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
    Bitmap bitmap=null;
    private String UserName = "NA";
    private String DocID = "NA";
    private boolean IsDebugToast = false;
    private boolean ISimageUpload = false;
    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;
    private String Qid="NA";

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
        uploaderFile = new FileUploadsClass(this);
        docType = getIntent().getStringExtra("docType");
        GUID = getIntent().getStringExtra(SimpleCameraActivity.PARAMS_GUID);
        DocType = getIntent().getStringExtra(SimpleCameraActivity.PARAMS_DOC_TYPE);
        DocTitle = getIntent().getStringExtra(SimpleCameraActivity.PARAMS_DOC_TITLE);
        UserName = getIntent().getStringExtra(SimpleCameraActivity.PARAMS_USERNAME);
        ModelID = getIntent().getStringExtra(SimpleCameraActivity.PARAMS_MODEL_ID);
        DocID = GetTimeStamp() + "_" + WebService.UserName;
        Qid=getIntent().getStringExtra("QuestionId");
        //  IsDebugToast = getIntent().getBooleanExtra(ImageActivity.DEBUG_SHOW_TOAST, false);
        imageView = findViewById(R.id.iv_storeImageFromCamera);
        buttonReAttach = findViewById(R.id.btn_reAttach);
        buttonClick = findViewById(R.id.btn_click);
        buttonUpload = findViewById(R.id.btn_upload);
        textViewError = findViewById(R.id.tv_errorMessage);
        textViewCaptureImageTitle = findViewById(R.id.tv_captureImageTitle);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
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
                File imageFile = null;
                try {
                    imageFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bp = null;
                try {
                    bp = BitmapFactory.decodeStream(new FileInputStream(
                            imageFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bp = getScaledBitmap(bp);
                bitmap=bp;
                imageView.setImageBitmap(bp);
               // Glide.with(this).load(photoURI).into(imageView);

                try {
                    photoURI=Uri.fromFile((new File(String.valueOf(createImageFile()))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    //String authority = getApplicationContext().getPackageName() + ".fileprovider";
                    //Log.i("authW", authority);
                    //photoURI = FileProvider.getUriForFile(this, authority, createImageFile());
                   // Glide.with(this).load(photoURI).into(imageView);
                    hideView(buttonClick);
                    hideView(textViewError);
                    showView(buttonUpload);
                    showView(buttonReAttach);
                    isImageReadyForUpload = true;
                    //compressImage(this, photoURI);

                } catch (Exception ex) {
                    textViewError.setText("ERROR: " + ex.getMessage());
                    showView(textViewError);
                }
            }
            if (requestCode == 100) {
                Uri mediaUri = data.getData();
                try {
                    InputStream inputStream = getBaseContext().getContentResolver().openInputStream(mediaUri);
                    Bitmap bm = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byte[] byteArray = stream.toByteArray();

                    imageView.setImageBitmap(bm);
                    Gallery=true;
                    galleryImage=saveBitMap(this,bm);

                    photoURI=Uri.fromFile((saveBitMap(this, bm)));

                    try {
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

                    //Log.i("SANJAY ", "onActivityResult: " + saveBitMap(this, bm));
                    // uri = Uri.fromFile(saveBitMap(this, bm));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    FileUploader uploader = null;
    private File saveBitMap(Context context, Bitmap Final_bitmap) {
        File pictureFileDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/"/*Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ""*/);
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("SANJAY ", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            Final_bitmap.compress(Bitmap.CompressFormat.PNG, 18, oStream);
            oStream.flush();
            oStream.close();
            Log.i("SANJAY ", "saveBitMap :: Save Image Successfully..");

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("SANJAY", "There was an issue saving the image.");
            Log.i("SANJAY", "Error :: " + e.getLocalizedMessage());
        }
        return pictureFile;
    }
    private void prepareUploadTask() {
        if(DocType.equalsIgnoreCase("Hygiene"))
            uploader = new FileUploader().setFileUploadUrl("http://harman.infield.co.in/FileUploader/ISD/Harman_Kardon/FloorHygieneHandler.ashx?");
        else if(DocType.equalsIgnoreCase("Vac"))
            uploader = new app.core.server.FileUploader().setFileUploadUrl("http://harman.infield.co.in/FileUploader/ISD/Harman_Kardon/ImageUploadHandler.ashx?");
        else
            uploader = new FileUploader().setFileUploadUrl("http://harman.infield.co.in/UploadFileHandler.ashx?");
    }

    private Bitmap getScaledBitmap(Bitmap bp) {
        int newScaleWidth = 0;
        int newScaleHeight = 0;
        int width = bp.getWidth();
        int height = bp.getHeight();
        if (width > height) {
            newScaleWidth = 650;
            float percentage = ((width - newScaleWidth) * 100) / width;
            newScaleHeight = (int) (height - ((height * percentage) / 100));
        } else {
            newScaleHeight = 650;
            float percentage = ((height - newScaleHeight) * 100) / height;
            newScaleWidth = (int) (width - ((width * percentage) / 100));
        }
        Bitmap newBitmap = Bitmap.createScaledBitmap(bp, newScaleWidth,
                newScaleHeight, false);

        /*
         * ByteArrayOutputStream out = new ByteArrayOutputStream();
         * newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); newBitmap =
         * BitmapFactory.decodeStream(new
         * ByteArrayInputStream(out.toByteArray()));
         */
        return newBitmap;
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
            if(DocType.equalsIgnoreCase("Vac")) {
                ImageResponse imageRespons1=new ImageResponse(Integer.parseInt(Qid),photoURI.toString());
                imageResponses.add(imageRespons1);
                intent.putExtra(SimpleCameraActivity.PARAMS_GUID, GUID);
                intent.putExtra("IMAGE_CAPTURED", photoURI.toString());
                intent.putExtra("DocID", DocID);
                intent.putExtra("QID", Qid);
            } else {
                intent.putExtra(SimpleCameraActivity.PARAMS_GUID, GUID);
                intent.putExtra("IMAGE_CAPTURED", photoURI.toString());
                intent.putExtra("DocID", DocID);
                intent.putExtra("QID", Qid);
            }
        } else {
            if(DocType.equalsIgnoreCase("Vac")) {
                ImageResponse imageRespons1=new ImageResponse(Integer.parseInt(Qid),"");
                imageResponses.add(imageRespons1);
                intent.putExtra(SimpleCameraActivity.PARAMS_GUID, GUID);
                intent.putExtra("IMAGE_CAPTURED", "");
                intent.putExtra("DocID", DocID);
                intent.putExtra("QID", Qid);
            }
            else {
                uri="";
                intent.putExtra(SimpleCameraActivity.PARAMS_GUID, GUID);
                intent.putExtra("IMAGE_CAPTURED", uri);
                intent.putExtra("DocID", "");
            }
        }

        //putData(ImageActivity.IMAGE_CAPTURE_RESULT, this._ImageCaptureResult.setResult(GetTimeStamp()+"_"+app.core.base.WebService.UserName, GUID));
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
                /*uploader.RemoveAllParameters();
                uploader.addParamters("date", getdate());
                uploader.addParamters("username", WebService.UserName);
                uploader.addParamters("doctype", DocType);*/
                uploader.RemoveAllParameters();
                uploader.addParamters("HeaderID", GUID);
                uploader.addParamters("DocTitle", "NA");
                uploader.addParamters("UploadedBy", WebService.UserName);
                uploader.addParamters("DocID", DocID);
                uploader.addParamters("DocType", DocType);
                uploader.addParamters("Remarks", "Na");
                uploader.addParamters("Latitude", "Na");
                uploader.addParamters("Longitude", "Na");
                uploader.addParamters("AnswerId", "0");
                if(Qid==null) {
                    uploader.addParamters("QuestionId", "0");
                } else {
                    uploader.addParamters("QuestionId", Qid);
                    uploader.addParamters("UploadedOn", CoronaNewQuestionnaireFragment.SelectDate);
                }
                if(Gallery)
                    response = uploader.Uploadfile(galleryImage, bp.getProgessDialog());
                else
                {
                    if (createImageFile().getPath() != null) {
                        Log.d(TAG, "underProcess11: " + createImageFile().getPath());
                        //  response = uploader.Uploadfile(new File(createImageFile().getPath()), bp.getProgessDialog());
                        response = uploader.Uploadfile(createImageFile(), bp.getProgessDialog());
                        Log.d(TAG, "underProcess: 12" + response);
                    } else {
                        response = "Can't create file from URI";
                    }

                }


                return response;
            }

            @Override
            public void processResponse(Object response) throws Exception {
                try {
                    if (response != null) {
                        if (response.toString().contains("Success")) {
                            /*updateImageStatus("false");
                            // intent.putExtra(ImageActivity.IMAGE_UPLOADED,
                            // true);
                            intent.putExtra(ImageActivity.IMAGE_UPLOADED, true);
                            _ImageCaptureResult.setIsImageUploaded(true);*/
                            _ImageCaptureResult.setIsImageUploaded(true);
                            ISimageUpload = true;
                            ShowToast("file uploaded successfully!");
                            onBackPressed();
                        } else {
                           // ShowToastLong("Unable to upload file!", 0);
                          //  ShowToastLong("Error: " + response, 0);
                            new Dialog(SimpleCameraActivity.this).setTitle("Message").show(response.toString());
                        }
                    } else
                        ShowToast("Unable to upload file");
                } catch (Exception ex) {
                  //  ShowToastLong(ex.getMessage(), 0);
                }

            }
        }).execute();
    }
    FileUploadsClass uploaderFile;
    private ProgressDialog progress;
    private JSONObject saveAndUploadImage() {
        progress=new ProgressDialog(this);
        progress.setMessage("Uploading........");
       progress.setCancelable(false);
        progress.show();

        if (bitmap != null) {
            // imageCaptured = true;
            String imageCompletePath = uploaderFile.saveImageToPhone(bitmap);
            String imageUrl = "";
//            http://harman.infield.co.in/UploadFileHandler.ashx?
            imageUrl = WebService.ApiUrl + "docs/upload/?"
                    + "username=" + WebService.getUsernameForUrl() + "&date="
                    + getdate() + "&doctype=" + DocType;
            imageUrl = "http://harman.infield.co.in/UploadFileHandler.ashx?"
                    + "username=" + WebService.getUsernameForUrl() + "&date="
                    + getdate() + "&doctype=" + DocType;
            uploaderFile.setAsyResponse(new AsynResponse() {

                @Override
                public void response(JSONObject jobj) {
                    progress.hide();

                    _ImageCaptureResult.setIsImageUploaded(true);
                    ISimageUpload = true;
                    ShowToast("file uploaded successfully!");
                    onBackPressed();
                }
            });
            uploaderFile.uploadFileWithAsy(imageUrl, imageCompletePath);

            return null;
        }
        return null;
    }

    //--------------------------------------------
    private String getdate() {
        DateFormat dateFormatnew = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String Date = dateFormatnew.format(date);
        return Date;
    }

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
        String timeStamp = "123456";
        String imageFileName = "IMG_" + timeStamp;
        File storageDir =getFilesDir();

        File image = new File(storageDir, imageFileName + ".png");
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    static final int CAMERA_REQUEST = 101;
    static final int STORAGE_REQUEST = 102;
    static final int REQUEST_TAKE_PHOTO = 202;
    private Uri photoURI;
    File photoFile = null;
    boolean Gallery=false;
    File galleryImage;
    private void dispatchTakePictureIntent() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SimpleCameraActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    if (CameraUtils.isExternalStorageWritable()) {
                        Intent cameraActivity = new Intent(SimpleCameraActivity.this, CameraActivity.class);
                        startActivityForResult(cameraActivity, REQUEST_TAKE_PHOTO);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");

                    startActivityForResult(intent, 100);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    /*        }
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click: {
                //startCameraTask();
                if(DocType.equalsIgnoreCase("SaleEnter"))
                 dispatchTakePictureIntent();
                else
                {
                    Intent cameraActivity = new Intent(SimpleCameraActivity.this, CameraActivity.class);
                    startActivityForResult(cameraActivity, REQUEST_TAKE_PHOTO);
                }
                break;
            }
            case R.id.btn_reAttach: {
                if(docType.equalsIgnoreCase("SaleEnter"))
                    dispatchTakePictureIntent();
                else
                {
                    Intent cameraActivity = new Intent(SimpleCameraActivity.this, CameraActivity.class);
                    startActivityForResult(cameraActivity, REQUEST_TAKE_PHOTO);
                }
                break;
            }
            case R.id.btn_upload: {
                if(!photoURI.equals(""))
                buttonUpload.setEnabled(false);
                uploadImage();
                break;
            }
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

            try (FileOutputStream out = new FileOutputStream(createImageFile())) {
                bm.compress(Bitmap.CompressFormat.PNG, 85, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (IOException e) {
                e.printStackTrace();
            }

        bitmap=bm;
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
