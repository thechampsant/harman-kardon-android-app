package com.fieldforce.harmonkardonff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import android.Manifest;
import android.content.pm.PackageManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ariston.training_module.modules.training_module.view_models.ProfileViewModel;
import com.ariston.training_module.networking.RetrofitConstants;
import com.ariston.training_module.utility.ConnectionDetector;
import com.ariston.training_module.utility.widgets.RobotoTextView;
import app.core.server.FileUploader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 101;
    private static final int REQUEST_GALLERY = 102;
    private static final int REQUEST_CAMERA_PERMISSION = 103;
    private static final String TAG = "ProfileActivity";

    private ProfileViewModel profileViewModel;
    ConnectionDetector _conn = null;
    RobotoTextView rtv_errorMessage, userName, userMobile, userID, education, counter, address, assigned, helpline;
    ProgressBar progressBar;
    RelativeLayout iv_backView;
    CardView cv_noDataContainer;
    ImageView profilePic, iv_camera;
    String UserID;
    private File cameraImageFile;
    private String lastUploadDocId;

    @Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        if (cameraImageFile != null)
            outState.putString("cameraImageFile", cameraImageFile.getAbsolutePath());
    }

    @Override
    protected void onRestoreInstanceState(android.os.Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String path = savedInstanceState.getString("cameraImageFile");
            if (path != null) cameraImageFile = new File(path);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ariston.training_module.R.layout.activity_profile);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        _conn = new ConnectionDetector(this);
        initViews();

        if (_conn.isConnectingToInternet()) {
            initObserver();
        } else {
            cv_noDataContainer.setVisibility(View.VISIBLE);
            rtv_errorMessage.setText("No Internet Connection!!");
            progressBar.setVisibility(View.GONE);
        }
        getData();
        iv_backView.setOnClickListener(view -> onBackPressed());
        iv_camera.setVisibility(View.VISIBLE);
        profilePic.setOnClickListener(v -> showImagePickerDialog());
        iv_camera.setOnClickListener(v -> showImagePickerDialog());
    }

    public void initObserver() {
        profileViewModel.getProfile(UserID);
    }

    public void initViews() {
        UserID = getIntent().getStringExtra("UserID");
        progressBar = findViewById(com.ariston.training_module.R.id.pbload);
        iv_backView = findViewById(com.ariston.training_module.R.id.iv_backView);
        cv_noDataContainer = findViewById(com.ariston.training_module.R.id.cv_noDataContainer);
        rtv_errorMessage = findViewById(com.ariston.training_module.R.id.rtv_errorMessage);
        userID = findViewById(com.ariston.training_module.R.id.userID);
        userMobile = findViewById(com.ariston.training_module.R.id.userMobile);
        userName = findViewById(com.ariston.training_module.R.id.userName);
        counter = findViewById(com.ariston.training_module.R.id.counter);
        education = findViewById(com.ariston.training_module.R.id.education);
        address = findViewById(com.ariston.training_module.R.id.address);
        assigned = findViewById(com.ariston.training_module.R.id.assigned);
        helpline = findViewById(com.ariston.training_module.R.id.helpline);
        profilePic = findViewById(com.ariston.training_module.R.id.profilePic);
        iv_camera = findViewById(com.ariston.training_module.R.id.iv_camera);
    }

    private void showImagePickerDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        new AlertDialog.Builder(this)
                .setTitle("Update Profile Photo")
                .setItems(options, (dialog, item) -> {
                    if (options[item].equals("Take Photo")) openCamera();
                    else if (options[item].equals("Choose from Gallery")) openGallery();
                    else dialog.dismiss();
                }).show();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        try {
            cameraImageFile = createImageFile();
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraImageFile != null) {
                Uri photoUri = androidx.core.content.FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider", cameraImageFile);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } catch (Exception e) {
            Log.e(TAG, "openCamera error: " + e.getMessage());
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "PROFILE_" + timeStamp;
            File storageDir = getFilesDir();
            return new File(storageDir, imageFileName + ".jpg");
        } catch (Exception e) {
            Log.e(TAG, "createImageFile error: " + e.getMessage());
            return null;
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            if (cameraImageFile != null && cameraImageFile.exists()) {
                Bitmap bitmap = decodeSampledBitmapFromFile(cameraImageFile.getAbsolutePath(), 1080, 1080);
                if (bitmap != null) {
                    loadImageIntoViews(bitmap);
                    uploadProfileImage(cameraImageFile);
                } else {
                    Toast.makeText(this, "Unable to read captured image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_GALLERY && data != null) {
            try {
                Uri selectedUri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(selectedUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bitmap != null) {
                    loadImageIntoViews(bitmap);
                    File galleryFile = saveBitmapToFile(bitmap);
                    if (galleryFile != null) uploadProfileImage(galleryFile);
                }
            } catch (Exception e) {
                Log.e(TAG, "Gallery error: " + e.getMessage());
            }
        }
    }

    private Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        if (path == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return Math.max(1, inSampleSize);
    }



    private void loadImageIntoViews(Bitmap bitmap) {
        if (bitmap == null) return;
        
        profilePic.setImageBitmap(bitmap);
        try {
            if (MainActivity.Current != null && MainActivity.Current.iv_profile_pic != null) {
                MainActivity.Current.iv_profile_pic.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating MainActivity profile pic: " + e.getMessage());
        }
    }

    private File saveBitmapToFile(Bitmap bitmap) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File file = new File(getFilesDir(), "profile_" + timeStamp + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            Log.e(TAG, "saveBitmapToFile: " + e.getMessage());
            return null;
        }
    }

    private void uploadProfileImage(File file) {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Uploading...");
        progress.setCancelable(false);
        progress.show();

        lastUploadDocId = null;
        String uploadUrl = buildProfileUploadUrl();
        Log.d(TAG, "Upload URL: " + uploadUrl);
        Log.d(TAG, "Upload file: " + file.getAbsolutePath());

        new Thread(() -> {
            String result = uploadWithLegacyUploader(file);
            runOnUiThread(() -> {
                progress.dismiss();
                if (isUploadSuccess(result)) {
                    Log.d(TAG, "Upload success response: " + result);
                    String uploadedUrl = extractUploadedImageUrl(result);
                    if (uploadedUrl != null && !uploadedUrl.isEmpty()) {
                        try {
                            MainActivity.MyInfo.FileUrl = uploadedUrl;
                            // Update profile pic immediately with uploaded image
                            Glide.with(ProfileActivity.this)
                                    .load(uploadedUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(profilePic);
                            if (MainActivity.Current != null && MainActivity.Current.iv_profile_pic != null) {
                                Glide.with(MainActivity.Current)
                                        .load(uploadedUrl)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(MainActivity.Current.iv_profile_pic);
                            }
                            Log.d(TAG, "Updated profile pic with new URL: " + uploadedUrl);
                        } catch (Exception e) {
                            Log.e(TAG, "Error loading uploaded image: " + e.getMessage());
                        }
                    }
                    Toast.makeText(this, "Profile photo updated!", Toast.LENGTH_SHORT).show();
                    // Refresh profile data to get updated image URL
                    refreshProfileData();
                } else {
                    Log.e(TAG, "Upload failed response: " + result);
                    Toast.makeText(this, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private String buildProfileUploadUrl() {
        if (lastUploadDocId == null || lastUploadDocId.trim().isEmpty()) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            lastUploadDocId = timeStamp + "_" + UserID + "_" + UUID.randomUUID().toString().substring(0, 6);
        }
        return RetrofitConstants.PROFILE_PICTURE_UPLOAD_URL + UserID
                + "&HeaderID=" + UserID
                + "&DocTitle=NA"
                + "&UploadedBy=" + UserID
                + "&DocID=" + lastUploadDocId
                + "&DocType=ProfilePicture"
                + "&Remarks=Na"
                + "&Latitude=Na"
                + "&Longitude=Na"
                + "&AnswerId=0"
                + "&QuestionId=0";
    }

    private boolean isUploadSuccess(String response) {
        if (response == null) return false;
        String normalized = response.trim();
        return normalized.contains("Success:")
                || normalized.contains("/Uploads/ProfilePictures/")
                || normalized.toLowerCase(Locale.US).contains("success");
    }

    private String extractUploadedImageUrl(String response) {
        if (response == null) return null;
        int start = response.indexOf("http");
        if (start < 0) return null;
        String url = response.substring(start).trim();
        int newline = url.indexOf('\n');
        if (newline >= 0) {
            url = url.substring(0, newline).trim();
        }
        return sanitizeImageUrl(url);
    }

    private String sanitizeImageUrl(String url) {
        if (url == null) return null;
        String clean = url.trim()
                .replace("&quot;", "")
                .replace("\"", "")
                .replace("\\", "");
        int jpgIdx = clean.toLowerCase(Locale.US).indexOf(".jpg");
        if (jpgIdx >= 0) return clean.substring(0, jpgIdx + 4);
        int jpegIdx = clean.toLowerCase(Locale.US).indexOf(".jpeg");
        if (jpegIdx >= 0) return clean.substring(0, jpegIdx + 5);
        int pngIdx = clean.toLowerCase(Locale.US).indexOf(".png");
        if (pngIdx >= 0) return clean.substring(0, pngIdx + 4);
        return clean;
    }

    private String uploadWithLegacyUploader(File sourceFile) {
        try {
            FileUploader uploader = new FileUploader()
                    .setFileUploadUrl(RetrofitConstants.PROFILE_PICTURE_UPLOAD_URL + UserID + "&");
            uploader.RemoveAllParameters();
            uploader.addParamters("HeaderID", UserID);
            uploader.addParamters("DocTitle", "NA");
            uploader.addParamters("UploadedBy", UserID);
            uploader.addParamters("DocID", lastUploadDocId != null ? lastUploadDocId : "");
            uploader.addParamters("DocType", "ProfilePicture");
            uploader.addParamters("Remarks", "Na");
            uploader.addParamters("Latitude", "Na");
            uploader.addParamters("Longitude", "Na");
            uploader.addParamters("AnswerId", "0");
            uploader.addParamters("QuestionId", "0");
            String response = uploader.Uploadfile(sourceFile);
            Log.d(TAG, "Upload raw response: " + response);
            return response;
        } catch (Exception e) {
            Log.e(TAG, "uploadWithLegacyUploader error: " + e.getMessage());
        }
        return null;
    }

    public void getData() {
        profileViewModel.getProfileResponse().observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            if (response.getmStatus()) {
                Log.e("Data", response.getData().get(0).getUserName() + "Null");
                userID.setText(response.getData().get(0).getUserId() != null ? response.getData().get(0).getUserId().toString() : "");
                userName.setText(response.getData().get(0).getUserName() != null ? response.getData().get(0).getUserName().toString() : "");
                userMobile.setText(response.getData().get(0).getHelpline() != null ? response.getData().get(0).getHelpline().toString() : "");
                helpline.setText(response.getData().get(0).getHelpline() != null ? response.getData().get(0).getHelpline().toString() : "");
                counter.setText(response.getData().get(0).getCounter() != null ? response.getData().get(0).getCounter().toString() : "");
                assigned.setText(response.getData().get(0).getAssignedOn() != null ? response.getData().get(0).getAssignedOn().toString() : "");
                education.setText(response.getData().get(0).getEducation() != null ? response.getData().get(0).getEducation().toString() : "");
                address.setText(response.getData().get(0).getAddress() != null ? response.getData().get(0).getAddress().toString() : "");

                String picUrl = sanitizeImageUrl(response.getData().get(0).getProfilePicUrl());
                if (picUrl != null && !picUrl.isEmpty()) {
                    Glide.with(this).load(picUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(profilePic);
                    MainActivity.MyInfo.FileUrl = picUrl;
                    if (MainActivity.Current != null && MainActivity.Current.iv_profile_pic != null) {
                        Glide.with(MainActivity.Current).load(picUrl).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(MainActivity.Current.iv_profile_pic);
                    }
                }
            } else {
                Toast.makeText(this, response.getmErrormsg(), Toast.LENGTH_SHORT).show();
                cv_noDataContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void refreshProfileData() {
        // Add a small delay to ensure server has processed the upload
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Wait 2 seconds
                runOnUiThread(() -> {
                    Log.d(TAG, "Refreshing profile data after image upload");
                    if (_conn.isConnectingToInternet()) {
                        profileViewModel.getProfile(UserID);
                    }
                });
            } catch (InterruptedException e) {
                Log.e(TAG, "Thread interrupted: " + e.getMessage());
            }
        }).start();
    }
}
