package com.fieldforce.harmonkardonff;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;

import com.fieldforce.harmonhelper.GPSTracker;
import com.fieldforce.harmonhelper.ImageHelper;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.BrandingImageModel;
import mob.field.harmonkardonff.services.WebService;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.model.Response;
import app.core.server.FileUploader;
import app.core.utils.Dialog;


public class BrandingActivity extends InnosolsActivity {

	FileUploader fileUploader;

	String[] brandingHeader = { "Table Top", "Leaflet Dispenser", "Signages",
			"Flanges", "Front Wall", "Back Wall", "Both Wall" };

	public static final int REQUEST_IMAGE_CAPTURE = 1;
	private static int LOAD_IMAGE_RESULTS = 2;

	ArrayList<BrandingImageModel> imageDetails = new ArrayList<BrandingImageModel>();

	BrandingImageModel brandingImage;

	WebService webService = new WebService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_branding);
		setListenersOnRadioButtons();

		findViewById(R.id.branding_submit_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						validateAndSubmitData();
					}
				});

		fileUploader = new FileUploader().setFileUploadUrl(WebService
				.getBrandingImageUploadUrl());

		// Adding branding Data
		if (imageDetails.size() == 0) {
			for (int i = 0; i < 7; i++) {
				brandingImage = new BrandingImageModel();
				brandingImage.brandingName = brandingHeader[i];
				imageDetails.add(brandingImage);
			}
		}
	}

	private void setListenersOnRadioButtons() {
		((RadioGroup) findViewById(R.id.branding_table_top_radio_group))
				.setOnCheckedChangeListener(yesNoRadioButtonListener);

		((RadioGroup) findViewById(R.id.branding_leaflet_radio_group))
				.setOnCheckedChangeListener(yesNoRadioButtonListener);

		((RadioGroup) findViewById(R.id.branding_signages_radio_group))
				.setOnCheckedChangeListener(yesNoRadioButtonListener);

		((RadioGroup) findViewById(R.id.branding_flange_radio_group))
				.setOnCheckedChangeListener(yesNoRadioButtonListener);

		((RadioGroup) findViewById(R.id.branding_front_wall_radio_group))
				.setOnCheckedChangeListener(yesNoRadioButtonListener);

		((RadioGroup) findViewById(R.id.branding_back_wall_radio_group))
				.setOnCheckedChangeListener(yesNoRadioButtonListener);

		((RadioGroup) findViewById(R.id.branding_both_radio_group))
				.setOnCheckedChangeListener(yesNoRadioButtonListener);

	}

	RadioGroup.OnCheckedChangeListener yesNoRadioButtonListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			if (group.getTag() != null) {
				switch (group.getTag().toString()) {
				case "table_top":
					if (((RadioButton) group
							.findViewById(R.id.branding_table_top_radio_yes))
							.isChecked())
						findViewById(R.id.branding_table_proof_image_layout)
								.setVisibility(View.VISIBLE);
					else
						findViewById(R.id.branding_table_proof_image_layout)
								.setVisibility(View.GONE);
					break;

				case "leaflet":
					if (((RadioButton) group
							.findViewById(R.id.branding_leaflet_radio_yes))
							.isChecked())
						findViewById(R.id.branding_leaflet_proof_image_layout)
								.setVisibility(View.VISIBLE);
					else
						findViewById(R.id.branding_leaflet_proof_image_layout)
								.setVisibility(View.GONE);

					break;
				case "signages":
					if (((RadioButton) group
							.findViewById(R.id.branding_signages_radio_yes))
							.isChecked())
						findViewById(R.id.branding_signages_proof_image_layout)
								.setVisibility(View.VISIBLE);
					else
						findViewById(R.id.branding_signages_proof_image_layout)
								.setVisibility(View.GONE);

					break;
				case "flange":
					if (((RadioButton) group
							.findViewById(R.id.branding_flange_radio_yes))
							.isChecked())
						findViewById(R.id.branding_flange_proof_image_layout)
								.setVisibility(View.VISIBLE);
					else
						findViewById(R.id.branding_flange_proof_image_layout)
								.setVisibility(View.GONE);

					break;
				case "front_wall":
					if (((RadioButton) group
							.findViewById(R.id.branding_front_wall_radio_yes))
							.isChecked())
						findViewById(
								R.id.branding_front_wall_proof_image_layout)
								.setVisibility(View.VISIBLE);
					else
						findViewById(
								R.id.branding_front_wall_proof_image_layout)
								.setVisibility(View.GONE);

					break;
				case "back_wall":
					if (((RadioButton) group
							.findViewById(R.id.branding_back_wall_radio_yes))
							.isChecked())
						findViewById(R.id.branding_back_wall_proof_image_layout)
								.setVisibility(View.VISIBLE);
					else
						findViewById(R.id.branding_back_wall_proof_image_layout)
								.setVisibility(View.GONE);

					break;
				case "both_wall":
					if (((RadioButton) group
							.findViewById(R.id.branding_both_radio_yes))
							.isChecked())
						findViewById(R.id.branding_both_wall_proof_image_layout)
								.setVisibility(View.VISIBLE);
					else
						findViewById(R.id.branding_both_wall_proof_image_layout)
								.setVisibility(View.GONE);

					break;
				}

			}

		}
	};

	public void openGallery(View v) {
		brandingImage = null;
		Object tag = ((LinearLayout) ((RelativeLayout) v.getParent())
				.getParent()).getTag();

		currentView = ((RelativeLayout) v.getParent());
		if (tag != null) {
			switch (tag.toString()) {
			case "table_top":

				brandingImage = imageDetails.get(0);
				break;

			case "leaflet":
				brandingImage = imageDetails.get(1);
				break;
			case "signages":
				brandingImage = imageDetails.get(2);
				break;
			case "flange":
				brandingImage = imageDetails.get(3);
				break;
			case "front_wall":
				brandingImage = imageDetails.get(4);
				break;
			case "back_wall":
				brandingImage = imageDetails.get(5);
				break;
			case "both_wall":
				brandingImage = imageDetails.get(6);
				break;
			}

			if (brandingImage != null) {
				openGalleryPressed();
			}
		}

	}

	public void openCamera(View v) {
		brandingImage = null;
		Object tag = ((LinearLayout) ((RelativeLayout) v.getParent()).getParent()).getTag();

		currentView = ((RelativeLayout) v.getParent());
		
		if (tag != null) {
			switch (tag.toString()) {
			case "table_top":

				brandingImage = imageDetails.get(0);
				break;

			case "leaflet":
				brandingImage = imageDetails.get(1);
				break;
			case "signages":
				brandingImage = imageDetails.get(2);
				break;
			case "flange":
				brandingImage = imageDetails.get(3);
				break;
			case "front_wall":
				brandingImage = imageDetails.get(4);
				break;
			case "back_wall":
				brandingImage = imageDetails.get(5);
				break;
			case "both_wall":
				brandingImage = imageDetails.get(6);
				break;
			}

			if (brandingImage != null) {
				cameraButtonPressed();
			}
		}
	}

	File photoFile;
	Bitmap imageClicked;
	View currentView;

	/* Opens the Camera */
	public void cameraButtonPressed() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if (cameraIntent.resolveActivity(getPackageManager()) != null) {

			// Create the File where the photo should go
			try {
				photoFile = createImageFile();
			} catch (Exception ex) {
				// Error occurred while creating the File
				// showErrorMessage(ex.getMessage());
			}

			// Continue only if the File was successfully created
			if (photoFile != null) {
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}

	/* Opens the Gallery */
	public void openGalleryPressed() {

		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, LOAD_IMAGE_RESULTS);

	}

	private File createImageFile() throws JSONException, IOException {

		// Create a unique file name for image
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "IMG_" + timeStamp;

		// Getting a reference to Target storage directory (AuditCanonImages)
		File storageDir = Environment
				.getExternalStoragePublicDirectory("ComioISPImages");

		// /Creating directory if not made already
		storageDir.mkdirs();

		File image = new File(storageDir, imageFileName + ".jpg");

		return image;
	}

	private String getRealPathFromURI(Uri contentURI) {
	    Cursor cursor = this.getContentResolver().query(contentURI, null, null, null, null);
	    if (cursor == null) { 
	         // path
	        return contentURI.getPath();
	    } else {
	        cursor.moveToFirst();
	        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
	        String path = cursor.getString(idx);
	        cursor.close();
	        return path;
	    }
	    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode == RESULT_OK) {
				if (requestCode == LOAD_IMAGE_RESULTS) {
					Uri selectedImageUri = data.getData();

					if (selectedImageUri != null) {
						brandingImage.imagePath = getRealPathFromURI(selectedImageUri);
						brandingImage.imagePresent = "true";
					} else {
						ShowToastLong(
								"Unable to Capture Image,Reason Empty uri received",
								0);
						return;
					}
				} 
				else if (requestCode == REQUEST_IMAGE_CAPTURE) {
					brandingImage.imagePath = photoFile.getAbsolutePath();
					brandingImage.imagePresent = "true";
				}

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 1 >= REQUIRED_SIZE
						&& options.outHeight / scale / 1 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;

				imageClicked = BitmapFactory.decodeFile(
						brandingImage.imagePath, options);
				
				imageClicked = ImageHelper.scaleImageAtOptimum(imageClicked);
				
				ImageHelper.saveTo(brandingImage.imagePath, imageClicked);
				
				imageClicked = ImageHelper.scaleImageForThumbNail(imageClicked);

				if (imageClicked != null) {
					brandingImage.imagePresent = "true";
					((ImageView) currentView
							.findViewById(R.id.sublayout_proof_image_imageview))
							.setImageBitmap(imageClicked);
					
					hideCameraAndImageControls();
				}
			}
		} catch (Exception e) {
			new Dialog(this).setMessage(e.getMessage()).show();
		}
	}



	

	private void hideCameraAndImageControls() {
	
		currentView.findViewById(R.id.image_sublayout_camera_text).setVisibility(View.GONE);
		currentView.findViewById(R.id.image_sublayout_gallery_text).setVisibility(View.GONE);
		currentView.findViewById(R.id.dummy_layout).setVisibility(View.GONE);
		currentView.findViewById(R.id.image_sublayout_open_camera_button).setVisibility(View.GONE);
		currentView.findViewById(R.id.image_sublayout_open_gallery_button).setVisibility(View.GONE);
		
	}

	public void showErrorDialog(String errorMessage) {
		new Dialog(this).setTitle("Error").show(errorMessage);
	}

	public void validateAndSubmitData() {

		if (((RadioButton) findViewById(R.id.branding_table_top_radio_yes))
				.isChecked()) {
			if (imageDetails.get(0).imagePath.equalsIgnoreCase("")) {
				showErrorDialog("Click Table Top Image.");
				return;
			}
		}

		imageDetails.get(0).YesSelected = ((RadioButton) findViewById(R.id.branding_table_top_radio_yes))
				.isChecked() + "";

		if (((RadioButton) findViewById(R.id.branding_leaflet_radio_yes))
				.isChecked()) {
			if (imageDetails.get(1).imagePath.equalsIgnoreCase("")) {
				showErrorDialog("Click Leaflet Image.");
				return;
			}
		}

		imageDetails.get(1).YesSelected = ((RadioButton) findViewById(R.id.branding_leaflet_radio_yes))
				.isChecked() + "";

		if (((RadioButton) findViewById(R.id.branding_signages_radio_yes))
				.isChecked()) {
			if (imageDetails.get(2).imagePath.equalsIgnoreCase("")) {
				showErrorDialog("Click Signages Image.");
				return;
			}
		}

		imageDetails.get(2).YesSelected = ((RadioButton) findViewById(R.id.branding_signages_radio_yes))
				.isChecked() + "";

		if (((RadioButton) findViewById(R.id.branding_flange_radio_yes))
				.isChecked()) {
			if (imageDetails.get(3).imagePath.equalsIgnoreCase("")) {
				showErrorDialog("Click Flange Image.");
				return;
			}
		}

		imageDetails.get(3).YesSelected = ((RadioButton) findViewById(R.id.branding_flange_radio_yes))
				.isChecked() + "";

		if (((RadioButton) findViewById(R.id.branding_front_wall_radio_yes))
				.isChecked()) {
			if (imageDetails.get(4).imagePath.equalsIgnoreCase("")) {
				showErrorDialog("Click Front Wall Image.");
				return;
			}
		}

		imageDetails.get(4).YesSelected = ((RadioButton) findViewById(R.id.branding_front_wall_radio_yes))
				.isChecked() + "";

		if (((RadioButton) findViewById(R.id.branding_back_wall_radio_yes))
				.isChecked()) {
			if (imageDetails.get(5).imagePath.equalsIgnoreCase("")) {
				showErrorDialog("Click Back Wall Image.");
				return;
			}
		}

		imageDetails.get(5).YesSelected = ((RadioButton) findViewById(R.id.branding_back_wall_radio_yes))
				.isChecked() + "";

		if (((RadioButton) findViewById(R.id.branding_both_radio_yes))
				.isChecked()) {
			if (imageDetails.get(6).imagePath.equalsIgnoreCase("")) {
				showErrorDialog("Click Both Wall Image.");
				return;
			}
		}

		imageDetails.get(6).YesSelected = ((RadioButton) findViewById(R.id.branding_both_radio_yes))
				.isChecked() + "";

		uploadImages();

	}

	private void uploadImages() {

		if (isAllImageUploaded()) {
			submitImageDetails();
		} else {

			outer: for (BrandingImageModel imageDetail : imageDetails)

				if (!imageDetail.imagePresent.equalsIgnoreCase("true")
						|| (imageDetail.imagePresent.equalsIgnoreCase("true") && imageDetail.imageUploaded
								.equalsIgnoreCase("true")))
					continue;
				else {
					brandingImage = imageDetail;
					uploadImage(imageDetail.imagePath);
					break outer;

				}

		}

	}
	

	/** Image Upload Background process */
	BackgroundProcess uploadImageBP;
	String docID;

	public void uploadImage(final String imagePath) {

		if (MainActivity.gpsTracker == null)
			MainActivity.gpsTracker = new GPSTracker(BrandingActivity.this);

		if (!isNetworkFoundDialog())
			return;
		uploadImageBP = new BackgroundProcess(this)
		.setProgressDialog(true).showProgressType(true);

		uploadImageBP.setbackgroundProcess(new IProcess() {

			@Override
			public Object underProcess() throws Exception {
				String response = null;

				docID = generateRandomDocID();

				fileUploader.RemoveAllParameters();
				fileUploader.addParamters("HeaderID", "NA");
				fileUploader.addParamters("DocTitle", "Branding");
				fileUploader.addParamters("UploadedBy",
						MainActivity.MyInfo.EmployeeCode);
				fileUploader.addParamters("DocID", docID);
				fileUploader.addParamters("DocType", "Branding");
				fileUploader.addParamters("Remarks", "NA");
				fileUploader.addParamters("Latitude",
						MainActivity.gpsTracker.getLatitude() + "");
				fileUploader.addParamters("Longitude",
						MainActivity.gpsTracker.getLongitude() + "");
				response = fileUploader.Uploadfile(new File(imagePath),
						uploadImageBP.getProgessDialog());
				return response;
			}

			@Override
			public void processResponse(Object response) throws Exception {
				if (response != null) {
					if (response.toString().startsWith("Success")) {
						ShowToast("Image  uploaded Successfully");
						brandingImage.docId = docID;
						brandingImage.imageUploaded = "true";
						uploadImages();
					} else
						showErrorDialog("Error: " + response);
				} else
					showErrorDialog("Error Uploading File : " + response);

			}
		}).execute();
	}

	public static String generateRandomDocID() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}

	public boolean isAllImageUploaded() {
		boolean uploaded = false;

		for (BrandingImageModel image : imageDetails) {
			if (image.imagePresent.equalsIgnoreCase("true")
					&& !image.imageUploaded.equalsIgnoreCase("true"))
				return false;
		}

		return true;
	}

	public void submitImageDetails() {
		BackgroundProcess submitImageDetailsBp = new BackgroundProcess(this);
		submitImageDetailsBp.setProgressMessage("Submitting Data ...");
		submitImageDetailsBp.setbackgroundProcess(new IProcess() {

			@Override
			public Object underProcess() throws Exception {

				return webService.submitBrandingData(
						MainActivity.MyInfo.StoreID, imageDetails);
			}

			@Override
			public void processResponse(Object response) throws Exception {

				Response submitBrandingImageResponse = (Response) response;

				if (submitBrandingImageResponse.status.equalsIgnoreCase("true")) {
					ShowToastLong("Branding Details Submitted", 0);
					onBackPressed();
				} else
					showErrorDialog(submitBrandingImageResponse.errormsg);

			}
		});

		submitImageDetailsBp.execute();

	}

	@Override
	public void RegisterTableInfoForLocalDB() {
	}

}
