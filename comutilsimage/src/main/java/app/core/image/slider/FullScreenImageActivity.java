package app.core.image.slider;

import java.io.File;
import linq.ArrayList;
import com.example.com.test.image.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
/*import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;*/
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import app.core.action.image.IActionImage;
import app.core.action.image.IWebClient;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.entitymodels.ImageInfo;
import app.core.image.capture.TouchImageView;
import app.core.server.FileUploader;
import app.core.utils.Dialog;
import app.core.utils.IntentFactory;

public class FullScreenImageActivity extends InnosolsActivity implements
		IWebClient, IActionImage {

	private ViewPager viewPager;
	ArrayList<ImageInfo> Images = new ArrayList<ImageInfo>();
	private FileUploader uploader = null;
	private ImageInfo CurrentImage = null;
	private boolean IsShowImageInfo = true;
	private int currentPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.activity_image_pager);
			viewPager = (ViewPager) findViewById(R.id.pager);
			Intent i = getIntent();
			int position = i.getIntExtra("position", 0);
			Images = IntentFactory.getDataArrayList("IMAGES_APP_VIEW_ACTION");
			uploader = new FileUploader().setFileUploadUrl(this
					.setImageUploadUrl());
			setImageList();
			viewPager.setCurrentItem(position);

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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.image_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.ctx_delete) {
			showImageDeleteDialog();
			return true;
		} else if (item.getItemId() == R.id.ctx_close) {
			this.finish();
			return true;

		} else if (item.getItemId() == R.id.ctx_upload) {
			tryUpdateImage();
			return true;
		} else {
			return super.onContextItemSelected(item);

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
				// method stub
				currentPage = arg0;
				updateCurrentPage();
			}
		});

		viewPager.setAdapter(adaptor.getPageAdapter());
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
		btnTakephoto.setVisibility(View.GONE);

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
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tryUpdateImage();
			}
		});
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// method stub
				finish();
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

	// Image Uploading methods.................

	private void tryUpdateImage() {
		setCurrentImage(Images.get(viewPager.getCurrentItem()));
		uploadImage();
	}

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
							ShowToast("file uploaded successfully!");
						} else {
							ShowToastLong("Unable to upload file!", 0);
							ShowToastLong("Error: " + response, 0);
							new Dialog(FullScreenImageActivity.this).setTitle(
									"Message").show(response.toString());
						}
					} else
						ShowToast("Unable to upload file");
				} catch (Exception ex) {
					ShowToastLong(ex.getMessage(), 0);
				}

			}
		}).execute();
	}

	// Actions on Image methods....................

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

	private void tryDeleteImage() {
		setCurrentImage(Images.get(viewPager.getCurrentItem()));
		if (DeleteImageLocally()) {
			notifyAdapter(true);
			ShowToast("Operation Ok!");
			if (!isImagesFound())
				this.goToBack();
		}
	}

	// --------------------------End of pending image----------------------//

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
		Intent data = new Intent();
		setResult(Activity.RESULT_OK, data);
		super.onBackPressed();
	}

	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		setResult(Activity.RESULT_OK, data);
		super.onBackPressed();
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

	private boolean isCurrentImageFound() {
		if (this.CurrentImage == null)
			return false;
		else if (new File(this.CurrentImage.LocalUrl).exists())
			return true;
		else
			return false;
	}

	private void setCurrentImage(ImageInfo image) {
		this.CurrentImage = image;
	}

	private boolean isImagesFound() {
		if (this.Images == null)
			return false;
		else if (this.Images.Count() < 1)
			return false;
		else
			return true;
	}

	@Override
	public ArrayList<ImageInfo> setImageData() {
		// method stub
		return Images;
	}

	@Override
	public boolean addImage(ImageInfo imageToAdd) {
		Images.add(imageToAdd);
		imageToAdd.InsertOrUpdate();
		return true;
	}

	@Override
	public boolean updateImageInfo(ImageInfo updatedImage) {
		updatedImage.InsertOrUpdate();
		return true;
	}

	@Override
	public boolean removeImage(ImageInfo imageToRemove) {
		Images.remove(imageToRemove);
		imageToRemove.Delete();
		return true;
	}

	@Override
	public String setImageUploadUrl() {
		// "http://dummay/notavialable.co.in"
		return getIntent().getStringExtra("WEB_UPLOAD_URL");
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// method stub

	}
}
