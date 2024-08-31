package app.core.image.capture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import app.core.base.InnosolsActivity;
import app.core.utils.Dialog;

public class ImageUtils {

	public ImageActivity context;
	public File StorageDirectory = null;
	public String AlbumName = null;

	public ImageUtils(ImageActivity _activity, File StorageDirectory) {
		this.context = _activity;
		this.StorageDirectory = StorageDirectory;
	}

	public ImageUtils(String AlbumName) {
		this.AlbumName = AlbumName;
		this.StorageDirectory = getAlbumStorageDir();
	}

	// variable------------------------------------------------

	public String BITMAP_STORAGE_KEY = "viewbitmap";
	public String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	public static final String JPEG_FILE_PREFIX = "IMG_";
	public static final String JPEG_FILE_SUFFIX = ".jpg";
	public String mCurrentPhotoPath;
	public ImageView mImageView;
	public String CurrentFileName=null;
	Integer rangle = 0;

	// methods----------------------------------------
	public String getAlbumName() throws Exception {
		if (AlbumName == null)
			throw new Exception(
					"Directory name is not avilable in ImageUtils !");
		else
			return this.AlbumName;
	}

	private File getAlbumStorageDir() {
		try {
			return new File(Environment.getExternalStorageDirectory(),
					getAlbumName());
		} catch (Exception ex) {
			new Exception(ex.getMessage());
		}
		return null;
	}

	public File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = this.StorageDirectory;

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v("Directory creation error:",
					"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	public File createImageFile() throws IOException {
		// Create an image file name
		String imageFileName = JPEG_FILE_PREFIX + GetTimeStamp() + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				albumF);
		return imageF;
	}

	private String RenameFile(String CUrl) {
		File f = new File(CUrl);
		try {
			File newfile = createImageFile();
			CurrentFileName = newfile.getAbsolutePath();
			f.renameTo(newfile);
		} catch (IOException e) {
			// catch block
			e.printStackTrace();
		}
		return CurrentFileName;
	}

	public String GetTimeStamp() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		return timeStamp;
	}

	public File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		return f;
	}

	public void dispatchTakePictureIntent(int actionCode) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if (context.IsFolderSave()) {
			try {
                File f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				mCurrentPhotoPath = null;
			}
		}
		context.startActivityForResult(takePictureIntent, actionCode);
	}

	public String getImagePath() {
		try {
			File f = null;
			f = setUpPhotoFile();
			mCurrentPhotoPath = f.getAbsolutePath();
			return mCurrentPhotoPath;
		} catch (Exception ex) {
			ex.printStackTrace();
			ex = null;
			mCurrentPhotoPath = null;
			return null;
		}
	}

	public int dpToPx(int dp) {
		float density = context.getApplicationContext().getResources()
				.getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	public void setPic() throws IOException {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);
		// mImageView.setVisibility(View.VISIBLE);
		// mVideoUri = null;

		// mVideoView.setVisibility(View.INVISIBLE);

		File file = setUpPhotoFile();
		FileOutputStream fOut = new FileOutputStream(file);

		bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
		fOut.flush();
		fOut.close();
	}

	public void galleryAddPic() {
		Intent mediaScanIntent = new Intent(
				"android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}




	private File getImageFile(Bitmap mImageBitmap) throws IOException {
		File f = this.setUpPhotoFile();
		FileOutputStream fOut = new FileOutputStream(f);
		mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		fOut.flush();
		fOut.close();
		return f;
	}

	public String getRealPathFromURI(Uri uri) {

		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.managedQuery(uri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

//	public void handleSmallCameraPhoto(Intent intent) {
//		Bundle extras = null;
//		try {
//			if (context.IsFolderSave()) {
//				CurrentFileName = mCurrentPhotoPath;
//			} else {
//				extras = intent.getExtras();
//				CurrentFileName = getImageFile((Bitmap) extras.get("data"))
//						.getAbsolutePath();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			if (mCurrentPhotoPath != null && context.IsFolderSave()) {
//				context.changeImageSavePreference(false);
//			} else {
//				context.changeImageSavePreference(true);
//			}
//		}
//	}
	public void handleSmallCameraPhoto(Intent intent) {
		CurrentFileName = mCurrentPhotoPath;		
	}
	

	public void handleBigCameraPhoto(Intent intent) throws IOException {

		Bundle extras = null;
		try {
			if (context.IsFolderSave()) {
				CurrentFileName = mCurrentPhotoPath;
			} else {
				extras = intent.getExtras();
				CurrentFileName = getImageFile((Bitmap) extras.get("data"))
						.getAbsolutePath();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mCurrentPhotoPath != null && context.IsFolderSave()) {
				context.changeImageSavePreference(false);
			} else {
				context.changeImageSavePreference(true);
			}
		}

	}

	public Uri getUri(String url) {
		try {
			return Uri.parse(url);
		} catch (Exception ex) {
			context.ShowToastLong("Unable to resolve path!", 0);
			return Uri.parse("/NA");
		}

	}

	@SuppressLint("SimpleDateFormat")
	public String GetCurrentDateTimeInString() {
		// method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

	public static long ToLong(String value) {
		try {
			return Long.parseLong(value);
		} catch (Exception ex) {
			return -1;
		}
	}

}
