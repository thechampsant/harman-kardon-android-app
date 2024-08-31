package app.core.image.capture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
/*import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;*/
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.example.com.test.image.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import app.core.action.image.IActionImage;
import app.core.action.image.IDirectory;
import app.core.action.image.IWebClient;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.camera.CameraActivity;
import app.core.entitymodels.ImageInfo;
import app.core.model.ImageCaptureResult;
import app.core.server.FileUploader;
import app.core.utils.Dialog;
import linq.ArrayList;
import androidx.fragment.app.FragmentActivity;


public abstract class ImageActivity extends InnosolsActivity implements
        IDirectory, IWebClient, IActionImage {

    public static final String DEBUG_SHOW_TOAST = "DEBUG_SHOW_TOAST";
    public static final String IMAGE_CAPTURED = "IMAGE_CAPTURED";
    public static final String IMAGE_UPLOADED = "IMAGE_UPLOADED";
    public static final String IMAGE_DELETED = "IMAGE_DELETED";
    public static final String IMAGE_CAPTURE_RESULT = "IMAGE_CAPTURE_RESULT";

    public static final String PARAMS_GUID = "GUID";
    public static final String PARAMS_USERNAME = "UserName";

    public static final String PARAMS_DOC_TYPE = "DocType";
    public static final String PARAMS_MODEL_ID = "PARAMS_MODEL_HEADERID";
    public static final String PARAMS_INSTANCE = "KEYS_CAPTURE_IMAGE";
    public static final String PARAMS_DOC_TITLE = "PARAMS_DOC_TITLE";
    public static final String ACTION_ENABLE_GPS = "ACTION_ENABLE_GPS";

    public static final int ACTION_TAKE_PHOTO_B = 1;
    public static final int ACTION_TAKE_PHOTO_S = 2;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    Integer rangle = 0;
    private ImageUtils utils;
    private ViewPager viewPager;
    private String DocType = "NA";
    private String activityId = "NA";
    private String DocTitle = "NA";
    private String GUID = "NA";
    private String ModelID = "NA";
    private String UserName = "NA";
    private ArrayList<ImageInfo> Images = new ArrayList<ImageInfo>();
    private FileUploader uploader = null;
    private ImageInfo CurrentImage = null;
    private boolean IsShowImageInfo = true;
    private boolean IsGPSEnabled = false;
    private int currentPage = 0;
    private ImageCaptureResult _ImageCaptureResult = null;
    private Intent intent = null;
    private boolean IsDebugToast = false;

    // Some lifecycle callbacks so that the image can survive orientation change
    private static boolean isIntentAvailable(Context context, String action) {

        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making Some Changes
        setContentView(R.layout.activity_image_pager);
        try {
            utils = new ImageUtils(this, getAlbumStorageDir());
            viewPager = (ViewPager) findViewById(R.id.pager);
            SetPolicy();
            setInitial();
            intent = new Intent();
            _ImageCaptureResult = new ImageCaptureResult();
        } catch (Exception ex) {
            ShowToastLong("Error: " + ex.getMessage(), 0);
            this.finish();
        }

    }

    /*** handle Change Configuration *****/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ImageActivity.PARAMS_DOC_TYPE, DocType);
        outState.putString(ImageActivity.PARAMS_DOC_TITLE, DocTitle);
        outState.putString(ImageActivity.PARAMS_GUID, GUID);
        outState.putString(ImageActivity.PARAMS_USERNAME, UserName);
        outState.putString(ImageActivity.PARAMS_MODEL_ID, ModelID);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.getString(ImageActivity.PARAMS_DOC_TYPE) != null)
            DocType = savedInstanceState
                    .getString(ImageActivity.PARAMS_DOC_TYPE);
        if (savedInstanceState.getString(ImageActivity.PARAMS_DOC_TITLE) != null)
            DocTitle = savedInstanceState
                    .getString(ImageActivity.PARAMS_DOC_TITLE);
        if (savedInstanceState.getString(ImageActivity.PARAMS_USERNAME) != null)
            UserName = savedInstanceState
                    .getString(ImageActivity.PARAMS_USERNAME);
        if (savedInstanceState.getString(ImageActivity.PARAMS_GUID) != null)
            GUID = savedInstanceState.getString(ImageActivity.PARAMS_GUID);
        if (savedInstanceState.getString(ImageActivity.PARAMS_MODEL_ID) != null)
            ModelID = savedInstanceState
                    .getString(ImageActivity.PARAMS_MODEL_ID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_takephoto) {
            startCameraActivity();
            return true;
        } else {
            super.onOptionsItemSelected(item);
            return false;
        }
    }

    //Changed

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_context_menu, menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.ctx_upload).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ctx_delete) {
            showImageDeleteDialog();
            return true;

        } else if (item.getItemId() == R.id.ctx_close) {

            this.onBackPressed();
            return true;

        } else if (item.getItemId() == R.id.ctx_upload) {
            tryUpdateImage();
            return true;
        } else {
            return super.onContextItemSelected(item);

        }
    }

    private boolean isImagesFound() {
        return this.Images != null && this.Images.Count() >= 1;
    }

    private boolean isCurrentImageFound() {
        return this.CurrentImage != null
                && new File(this.CurrentImage.LocalUrl).exists();
    }

    private void setCurrentImage(ImageInfo image) {
        this.CurrentImage = image;
    }

    private ArrayList<ImageInfo> getImageData() {
        if (GUID == null || GUID == "")
            return this.setImageData();
        else
            return this.setImageData().where("HeaderID", GUID);

    }

    private void setInitial() {
        Intent i = getIntent();
        GUID = i.getStringExtra(ImageActivity.PARAMS_GUID);
        DocType = i.getStringExtra(ImageActivity.PARAMS_DOC_TYPE);
        DocTitle = i.getStringExtra(ImageActivity.PARAMS_DOC_TITLE);
        UserName = i.getStringExtra(ImageActivity.PARAMS_USERNAME);
        ModelID = i.getStringExtra(ImageActivity.PARAMS_MODEL_ID);
        IsDebugToast = i.getBooleanExtra(ImageActivity.DEBUG_SHOW_TOAST, false);
        Images = getImageData();
        IsGPSEnabled = i
                .getBooleanExtra(ImageActivity.ACTION_ENABLE_GPS, false);

        uploader = new FileUploader()
                .setFileUploadUrl(this.setImageUploadUrl());
        if (IsGPSEnabled)
            InvokeGPSService();
        setImageList();
        if (!isImagesFound())
            startCameraActivity();

    }

    // private void startCameraActivity()
    // {
    // utils.dispatchTakePictureIntent(ImageActivity.ACTION_TAKE_PHOTO_S);
    // }

    private void startCameraActivity() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.PARAMS_IMAGE_PATH, utils.getImagePath());
        this.startActivityForResult(intent, ImageActivity.ACTION_TAKE_PHOTO_S);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setImageList() {

        if (!isImagesFound())
            return;
        GenricAdapter adaptor = new GenricAdapter(this,
                R.layout.activity_image_view).setData(Images);
        adaptor.setGenricAdapter(new IAdapter<ImageInfo>() {

            @Override
            public void setItemView(ImageInfo item, View view, int index) {
                view.setTag(item);
                setCurrentImage(item);
                setCurrentImageOnPager(view);
                setImageStatus(view);
                setImageInfo(view);
            }
        }).setPageAdapter();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // method stub

            }

            @Override
            public void onPageSelected(int arg0) {
                currentPage = arg0;
                updateCurrentPage();
            }
        });

        viewPager.setAdapter(adaptor.getPageAdapter());

    }

    private void tryShowRemarksDialog() {

        try {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Remarks");
            final EditText tv = new EditText(this);
            tv.setText(CurrentImage.Remarks);
            tv.setEnabled(!CurrentImage.isUpdated());
            dialog.setView(tv);
            if (!CurrentImage.isUpdated()) {
                dialog.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                CurrentImage.Remarks = tv.getText().toString() == null ? "NA"
                                        : tv.getText().toString();
                                CurrentImage.InsertOrUpdate();
                                updateCurrentPage();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Cancel", null);
            }

            dialog.setNegativeButton("Cancel", null);

            dialog.show();
        } catch (Exception ex) {
            this.ShowToastLong(ex.getMessage(), 0);
        }
    }

    private void updateCurrentPage() {
        ImageInfo info = Images.get(currentPage);
        View view = viewPager.findViewWithTag(info);
        if (info == null)
            return;
        if (view == null)
            return;
        setCurrentImage(info);
        setCurrentImageOnPager(view);
        setImageStatus(view);
        setImageInfo(view);
    }

    // Image Uploading methods.................

    private void setCurrentImageOnPager(final View view) {

        TouchImageView imgDisplay = (TouchImageView) view
                .findViewById(R.id.imgDisplay);
        Button btnUpload = (Button) view.findViewById(R.id.btnupload);
        Button btnClose = (Button) view.findViewById(R.id.btnClose);
        Button btnoptions = (Button) view.findViewById(R.id.btn_options);
        Button btnInfo = (Button) view.findViewById(R.id.btn_info);
        Button btnShare = (Button) view.findViewById(R.id.btn_share);
        Button btnTakephoto = (Button) view.findViewById(R.id.btn_takephotos);

        btnClose.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);

        btnoptions.setOnCreateContextMenuListener(this);
        btnoptions.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.showContextMenu();

            }
        });
        btnInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                IsShowImageInfo = !IsShowImageInfo;
                setImageInfo(view);
            }
        });
        btnTakephoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startCameraActivity();
            }
        });
        btnUpload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tryUpdateImage();
            }
        });
        if (isCurrentImageFound()) {
            imgDisplay.setImageURI(Uri.parse(CurrentImage.LocalUrl));
            imgDisplay.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    tryShowRemarksDialog();
                    return true;
                }
            });

        }

    }

    private void tryUpdateImage() {
        setCurrentImage(Images.get(viewPager.getCurrentItem()));
        uploadImage();
    }

    // Actions on Image methods....................

    public void uploadImage() {
        if (!isCurrentImageFound()) {
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
                uploader.addParamters("HeaderID", CurrentImage.ModelID);
                uploader.addParamters("DocTitle", CurrentImage.DocTitle);
                uploader.addParamters("UploadedBy", CurrentImage.UserName);
                uploader.addParamters("DocID", CurrentImage.DocID);

                uploader.addParamters("DocType", CurrentImage.DocType);
                uploader.addParamters("Remarks", CurrentImage.Remarks);
                uploader.addParamters("Latitude", CurrentImage.Latitude);
                uploader.addParamters("Longitude", CurrentImage.Longitude);
                response = uploader.Uploadfile(new File(CurrentImage.LocalUrl),
                        bp.getProgessDialog());
                return response;
            }

            @Override
            public void processResponse(Object response) throws Exception {
                try {
                    if (response != null) {
                        if (response.toString().startsWith("Success")) {
                            updateImageStatus("false");
                            // intent.putExtra(ImageActivity.IMAGE_UPLOADED,
                            // true);
                            intent.putExtra(ImageActivity.IMAGE_UPLOADED, true);
                            _ImageCaptureResult.setIsImageUploaded(true);
                            ShowToast("file uploaded successfully!");
                        } else {
                            ShowToastLong("Unable to upload file!", 0);
                            ShowToastLong("Error: " + response, 0);
                            new Dialog(ImageActivity.this).setTitle("Message")
                                    .show(response.toString());
                        }
                    } else
                        ShowToast("Unable to upload file");
                } catch (Exception ex) {
                    ShowToastLong(ex.getMessage(), 0);
                }

            }
        }).execute();
    }

    private void notifyAdapter(boolean defaultpos) {
        // viewPager.getAdapter().notifyDataSetChanged();
        setImageList();
        if (!defaultpos)
            viewPager.setCurrentItem(this.Images.Count() - 1);
    }

    public boolean DeleteImageLocally() {
        boolean deleted = false;
        boolean isRemoved = false;
        try {
            isRemoved = removeImage(CurrentImage);
            if (isRemoved)
                deleted = new File(CurrentImage.LocalUrl).delete();
            if (!deleted)
                ShowToastLong("Unable to delete!", 0);

            return isRemoved;

        } catch (Exception ex) {
            ShowToastLong(ex.getMessage(), 0);
            return isRemoved;
        }
    }

    // setting image status methods.........................
    private void setImageStatus(View view) {

        if (CurrentImage.IsOffline.equalsIgnoreCase("true")) {
            view.findViewById(R.id.img_status).setVisibility(View.VISIBLE);
            ((ImageView) (view.findViewById(R.id.img_status)))
                    .setImageResource(R.drawable.pending);
            view.findViewById(R.id.btnupload).setVisibility(View.VISIBLE);
        } else if (CurrentImage.IsOffline.equalsIgnoreCase("false")) {
            ((ImageView) (view.findViewById(R.id.img_status)))
                    .setImageResource(R.drawable.done);
            view.findViewById(R.id.img_status).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btnupload).setVisibility(View.GONE);
            this.invalidateOptionsMenu();
        } else {
            view.findViewById(R.id.img_status).setVisibility(View.GONE);
            view.findViewById(R.id.btnupload).setVisibility(View.GONE);
        }

    }

    private void setImageInfo(View view) {

        if (IsShowImageInfo && isCurrentImageFound())
            view.findViewById(R.id.lyt_imgdetail).setVisibility(View.VISIBLE);
        else
            view.findViewById(R.id.lyt_imgdetail).setVisibility(View.GONE);

        TextView DocType = (TextView) view.findViewById(R.id.txt_doctype);
        TextView CapturedOn = (TextView) view.findViewById(R.id.txt_capturedon);
        TextView Remarks = (TextView) view.findViewById(R.id.txt_remarks);

        DocType.setText("Image Type: " + CurrentImage.DocType);
        CapturedOn.setText("Capture on: " + CurrentImage.CapturedOn);
        if (CurrentImage.Remarks != null)
            Remarks.setText("Remarks: " + CurrentImage.Remarks);
        else
            Remarks.setVisibility(View.GONE);

    }

    // ---------------Add Image To pending model-----------//
    private void AddImageToPending(String ImageUrl) {
        try {
            File f = new File(ImageUrl);
            if (f.length() < 1)
                return;
            ImageInfo model = new ImageInfo();
            model.UserName = UserName == null ? "NA" : UserName;
            model.LocalUrl = ImageUrl;
            model.DocID = UserName + "_" + utils.GetTimeStamp();
            model.DocType = DocType == null ? "NA" : DocType;
            model.DocTitle = DocTitle == null ? "NA" : DocTitle;
            model.HeaderID = GUID == null ? "NA" : GUID;
            model.ModelID = ModelID == null ? "NA" : ModelID;
            model.CapturedOn = utils.GetCurrentDateTimeInString();
            if (IsGPSEnabled && this.locationHelper != null) {
                model.Latitude = this.locationHelper.getLatitude() + "";
                model.Longitude = this.locationHelper.getLongitude() + "";
            }
            addImage(model);
        } catch (Exception ex) {
            ShowToastLong("Unable to add image locally!", 0);
            ShowToastLong("Error: " + ex.getMessage(), 0);
        }
    }

    private void updateImageStatus(String status) {
        try {
            CurrentImage.IsOffline = status;
            updateImageInfo(CurrentImage);
            updateCurrentPage();
        } catch (Exception ex) {
            ShowToastLong("Unable to set status of file!", 0);
            ShowToastLong("Error: " + ex.getMessage(), 0);
        }
    }

    public void showImageDeleteDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Want to delete?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                tryDeleteImage();
                            }
                        }).setNegativeButton("No", null).show();

    }

    // abstract image methods overriding.....

    private void tryDeleteImage() {
        setCurrentImage(Images.get(viewPager.getCurrentItem()));
        if (DeleteImageLocally()) {
            notifyAdapter(true);
            ShowToast("Operation Ok!");
            if (!isImagesFound())
                startCameraActivity();
        }
    }

    @Override
    public boolean addImage(ImageInfo imageToAdd) {
        imageToAdd.InsertOrUpdate();
        Images.add(imageToAdd);
        return true;
    }

    @Override
    public boolean updateImageInfo(ImageInfo updatedImage) {
        updatedImage.InsertOrUpdate();
        return true;
    }

    @Override
    public boolean removeImage(ImageInfo imageToRemove) {
        imageToRemove.Delete();
        Images.remove(imageToRemove);
        return true;
    }

    // --------------------------End of pending image----------------------//

    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // data) {
    // switch (requestCode) {
    // case 1: {
    // try {
    // if (resultCode == RESULT_OK) {
    // utils.handleBigCameraPhoto(data);
    // AddImageToPending(utils.CurrentFileName);
    // notifyAdapter(false);
    // } else {
    // ShowToastLong("Camera is unable to save picture!", 0);
    // this.finish();
    // }
    // } catch (IOException e) {
    // ShowToastLong(e.getMessage(), 0);
    // e.printStackTrace();
    // }
    // }
    // break;
    //
    // case 2: {
    // try {
    // if (resultCode == RESULT_OK) {
    // utils.handleSmallCameraPhoto(data);
    // AddImageToPending(utils.CurrentFileName);
    // notifyAdapter(false);
    // } else {
    // ShowToastLong("Camera is unable to save picture!", 0);
    // this.finish();
    // }
    // } catch (Exception e) {
    // ShowToastLong(e.getMessage(), 0);
    // e.printStackTrace();
    // }
    // break;
    // }
    // default: {
    // ShowToastLong("Unable to match request code!" + requestCode, 0);
    // this.finish();
    // }
    // }
    // }

    private void rotateImage(String imageUrl) {
        rangle = rangle == 360 ? 0 : rangle + 90;
        Bitmap source = BitmapFactory.decodeFile(imageUrl);

        Matrix matrix = new Matrix();
        matrix.postRotate(rangle);
        Bitmap dest = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        dest.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        FileInputStream fileInputStream = null;
        File file = new File("yourfile");
        byteArray = new byte[(int) file.length()];

    }

    private void showDebugToastOnActivityResult(int requestCode,
                                                int resultCode, Intent data) {
        if (!IsDebugToast)
            return;
        ShowToastLong(
                "Image Path: "
                        + data.getStringExtra(CameraActivity.PARAMS_IMAGE_PATH),
                0);
        ShowToastLong("Request Code: " + resultCode, 0);
        ShowToastLong(
                "Is Image Saved: "
                        + data.getBooleanExtra(
                        CameraActivity.PARAMS_IS_IMAGE_CAPTURED, false),
                0);
    }

    private void leaveActivityIfNecessary() {
        if (this.isImagesFound())
            return;
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        showDebugToastOnActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                try {
                    if (resultCode == RESULT_OK
                            && data.getBooleanExtra(
                            CameraActivity.PARAMS_IS_IMAGE_CAPTURED, false)) {
                        utils.CurrentFileName = data
                                .getStringExtra(CameraActivity.PARAMS_IMAGE_PATH);
                        AddImageToPending(utils.CurrentFileName);
                        notifyAdapter(false);
                    } else {
                        ShowToastLong("Image  not saved!", 0);
                        leaveActivityIfNecessary();
                    }
                } catch (Exception e) {
                    ShowToastLong(e.getMessage(), 0);
                    e.printStackTrace();
                    this.finish();
                }
            }
            break;

            case 2: {
                try {
                    if (resultCode == RESULT_OK
                            && data.getBooleanExtra(
                            CameraActivity.PARAMS_IS_IMAGE_CAPTURED, false)) {
                        utils.CurrentFileName = data
                                .getStringExtra(CameraActivity.PARAMS_IMAGE_PATH);
                        AddImageToPending(utils.CurrentFileName);
                        notifyAdapter(false);
                    } else {
                        ShowToastLong("Image  not saved!", 0);
                        leaveActivityIfNecessary();
                    }
                } catch (Exception e) {
                    ShowToastLong(e.getMessage(), 0);
                    e.printStackTrace();
                    this.finish();
                }
                break;
            }
            default: {
                ShowToastLong("Unable to match request code!" + requestCode, 0);
                this.finish();
            }
        }
    }

    // ----------------------END-------------------------------//

    // ---------------------Utility methods------------------//
    public void setOpacityView(View view) {
        // method stub
        AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F);
        alpha.setDuration(0); // Make animation instant
        alpha.setFillAfter(true); // Tell it to persist after the animation ends
        // And then on your layout
        view.startAnimation(alpha);
    }

    public void goToBack() {
        setCustomIntent();
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        _ImageCaptureResult.setIsImageCaptured(utils.CurrentFileName != null);
        setCustomIntent();
        setResult(Activity.RESULT_OK, intent);

        try {
            locationHelper.stopLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onBackPressed();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setCustomIntent() {
        if (intent == null)
            intent = new Intent();
        intent.putExtra(ImageActivity.PARAMS_GUID, GUID);
        intent.putExtra(ImageActivity.IMAGE_CAPTURED,
                utils.CurrentFileName != null);

        putData(ImageActivity.IMAGE_CAPTURE_RESULT,
                this._ImageCaptureResult.setResult(Images, GUID));
    }

    // --------------------utility method end here--------------//

    public File getAlbumStorageDir() {
        if (setImageDirectoryName() == null)
            return new File(Environment.getExternalStorageDirectory(),
                    "Download");
        else
            return new File(Environment.getExternalStorageDirectory(),
                    setImageDirectoryName());
    }

    // ----------------------------Low RAM Message handling------------------//
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(
                this,
                "Low RAM!!! Please close running applications \n Or Restart your phone ",
                Toast.LENGTH_LONG).show();
    }

    @SuppressLint("NewApi")
    private void SetPolicy() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public void RegisterTableInfoForLocalDB() {
        // this.RegisterTableForDataEntity(new ImageInfo());

    }

}
