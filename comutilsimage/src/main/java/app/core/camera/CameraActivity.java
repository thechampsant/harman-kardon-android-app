package app.core.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.com.test.image.R;

public class CameraActivity extends Activity implements SensorEventListener {

	public static final byte FRONT_CAMERA= 1;
	public static final byte BACK_CAMERA=2;

	private Camera mCamera;
	private CameraPreview mPreview;
	private SensorManager sensorManager = null;
	private int orientation;
	private ExifInterface exif;
	private int deviceHeight;
	private int deviceWidth;
	private Button ibRetake;
	private Button ibUse;
	private Button ibCapture;
	private FrameLayout flBtnContainer;
	private ImageView rotatingImage;
	private Button btnRotateCam;
	private int degrees = -1;
	private String CurrentFileName = null;
	private boolean cameraConfigured = false;
	private ImageView rotatingSave;
	private ImageView rotatingDiscard;
	private ImageView switchCamera;
	private File sdRoot;
	private String dir;
	private Camera.Parameters campParams;
	public final static int CAMERA_ID = -10;
	private int frontCameraID = CAMERA_ID;
	private int rearCameraID = CAMERA_ID;
	private static int currentCamreaID = CAMERA_ID;
	public final static String PARAMS_IMAGE_PATH = "PARAMS_IMAGE_PATH";
	public final static String PARAMS_IS_IMAGE_CAPTURED = "PARAMS_IS_IMAGE_CAPTURED";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		this.setCameraIds();
		// Setting all the path for the image
		sdRoot = Environment.getExternalStorageDirectory();
		dir = "/DCIM/Camera/";

		CurrentFileName = this.getIntent().getStringExtra(
				CameraActivity.PARAMS_IMAGE_PATH);
		// Getting all the needed elements from the layout
		rotatingImage = (ImageView) findViewById(R.id.imageView1);
		rotatingSave = (ImageView) findViewById(R.id.img_save);
		rotatingDiscard = (ImageView) findViewById(R.id.img_discard);
		switchCamera = (ImageView) findViewById(R.id.img_switchCamera);
		ibRetake = (Button) findViewById(R.id.ibRetake);
		ibUse = (Button) findViewById(R.id.ibUse);
		ibCapture = (Button) findViewById(R.id.ibCapture);
		btnRotateCam = (Button) findViewById(R.id.ibCapture_rotatecam);

		flBtnContainer = (FrameLayout) findViewById(R.id.flBtnContainer);

		// Getting the sensor service.
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Selecting the resolution of the Android device so we can create a
		// proportional preview
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		deviceHeight = display.getHeight();
		deviceWidth = display.getWidth();

		// Add a listener to the Capture button
		ibCapture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mCamera.takePicture(null, null, mPicture);
			}
		});

		// Add a listener to the Retake button
		ibRetake.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Deleting the image from the SD card/
				deleteImageFromSdCard();
				// releaseSurface();
				// Restart the camera preview.
				mCamera.startPreview();

				// Reorganize the buttons on the screen
				flBtnContainer.setVisibility(View.VISIBLE);
				findViewById(R.id.flBtnContainer_rotatecam).setVisibility(
						View.VISIBLE);
				findViewById(R.id.fm_discard).setVisibility(View.GONE);
				findViewById(R.id.fm_save).setVisibility(View.GONE);
			}
		});

		// Add a listener to the Use button
		ibUse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(CameraActivity.PARAMS_IS_IMAGE_CAPTURED, true);
				intent.putExtra(CameraActivity.PARAMS_IMAGE_PATH,
						CurrentFileName);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});

		// switching camera from rear to front and vice-versa
		btnRotateCam.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				rotateCamera();
			}
		});
	}

	private void createCamera() {
		try {
			setCurrentCameraId();
			// Create an instance of Camera
			getCameraInstance();
			// Setting the right parameters in the camera
			if (this.mCamera == null) {
				cameraActivityCrashed(null);
				return;
			}
			campParams = mCamera.getParameters();
			setCameraConfigurations();
			addCameraPreview();
		} catch (Exception ex) {
			cameraActivityCrashed(ex);
		}
	}

	private void cameraActivityCrashed(Exception ex) {
		Toast.makeText(this, "Unable to open camera ! Please try again!",
				Toast.LENGTH_LONG).show();
		if(ex!=null)
		Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
		Intent intent = new Intent();
		setResult(Activity.RESULT_CANCELED, intent);
		this.finish();
	}

	private void addCameraPreview() {

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);

		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				deviceWidth, deviceHeight);
		preview.setLayoutParams(layoutParams);

		// Adding the camera preview after the FrameLayout and before the button
		// as a separated element.
		preview.addView(mPreview);
	}

	private void setCameraConfigurations()
	{
		if (!cameraConfigured) {
			Camera.Size size = getBestPreviewSize(1600, 1200, campParams);
			Camera.Size pictureSize = getSmallestPictureSize(campParams);

			if (size != null && pictureSize != null) {
				campParams.setPreviewSize(size.width, size.height);
				campParams
						.setPictureSize(pictureSize.width, pictureSize.height);
				campParams.setPictureFormat(PixelFormat.JPEG);
				mCamera.setParameters(campParams);
				cameraConfigured = true;
			}

		}

	}

	private Camera.Size getSmallestPictureSize(Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPictureSizes()) {
			if (result == null) {
				result = size;
			} else {
				int resultArea = result.width * result.height;
				int newArea = size.width * size.height;

				if (newArea < resultArea) {
					result = size;
				}
			}
		}

		return (result);
	}

	private Camera.Size getBestPreviewSize(int width, int height,
										   Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Test if there is a camera on the device and if the SD card is
		// mounted.
		this.setCameraIds();
		if (!hasCamera()) {
			Intent i = new Intent(this, NoCamera.class);
			startActivity(i);
			finish();
		} else if (!checkSDCard()) {
			Intent i = new Intent(this, NoSDCard.class);
			startActivity(i);
			finish();
		}

		// Creating the camera
		createCamera();

		// Register this class as a listener for the accelerometer sensor
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// release the camera immediately on pause event
		releaseCamera();
		releaseSurface();
	}

	private void holdSurface() {
		this.releaseCamera();
	}

	private void releaseSurface() {
		// removing the inserted view - so when we come back to the app we
		// won't have the views on top of each other.
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		if (preview == null || preview.getChildCount() < 1)
			return;
		preview.removeViewAt(0);
	}

	private void switchCamera() {
		try {
			cameraConfigured=false;
			releaseSurface();
			releaseCamera();
			createCamera();
		} catch (Exception ex) {
			Toast.makeText(this, "Unable to switch camera !", Toast.LENGTH_LONG)
					.show();
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	/** Check if this device has a camera */
	private boolean hasCamera() {
		return this.hasFrontCamera() || this.hasRearCamera();
	}

	private boolean hasFrontCamera() {
		return this.frontCameraID != CameraActivity.CAMERA_ID;
	}

	private boolean hasRearCamera() {
		return this.rearCameraID != CameraActivity.CAMERA_ID;
	}

	private void setCameraIds() {
		int numberOfCameras = Camera.getNumberOfCameras();
		CameraInfo ci = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, ci);
			if (ci.facing == CameraInfo.CAMERA_FACING_FRONT) {
				this.frontCameraID = i;
			}
			if (ci.facing == CameraInfo.CAMERA_FACING_BACK) {
				this.rearCameraID = i;
			}
		}
		currentCamreaID = CameraActivity.CAMERA_ID;
	}

	private void setCurrentCameraId() {
		if (currentCamreaID != CameraActivity.CAMERA_ID) {
			currentCamreaID = currentCamreaID == this.frontCameraID ? this.rearCameraID
					: this.frontCameraID;
			return;
		}
		currentCamreaID = this.rearCameraID;

	}

	private void rotateCamera() {
		if (!hasFrontCamera()) {
			Toast.makeText(this, "Front camera is not avilable!",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			this.switchCamera();
		}
	}

	private boolean checkSDCard() {
		boolean state = false;

		String sd = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(sd)) {
			state = true;
		}

		return state;
	}

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	private Camera getCameraInstance() {

		try {

			// attempt to get a Camera instance
			mCamera = Camera.open(currentCamreaID);
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}

		// returns null if camera is unavailable
		return mCamera;

	}

	private String getDefaultFilePath() {
		// Creating the directory where to save the image. Sadly in older
		// version of Android we can not get the Media catalog name
		File mkDir = new File(sdRoot, dir);
		mkDir.mkdirs();
		String fileName = "IMG_"
				+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
				.toString() + ".jpg";
		File pictureFile = new File(sdRoot, dir + fileName);
		return pictureFile.getAbsolutePath();
	}

	private PictureCallback mPicture = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {

			// Replacing the button after a photho was taken.
			// holdSurface();
			flBtnContainer.setVisibility(View.GONE);
			findViewById(R.id.flBtnContainer_rotatecam)
					.setVisibility(View.GONE);
			findViewById(R.id.fm_discard).setVisibility(View.VISIBLE);
			findViewById(R.id.fm_save).setVisibility(View.VISIBLE);

			// Main file where to save the data that we recive from the camera
			CurrentFileName = CurrentFileName == null ? getDefaultFilePath()
					: CurrentFileName;
			File pictureFile = new File(CurrentFileName);

			boolean successfullWrite = false;
			FileOutputStream purge=null;
			try {
				purge = new FileOutputStream(pictureFile);
				byte cameraUsed = checkCameraUsed();
				byte [] pictureData = ImageHelper.rotateImageIfRequired(data,degrees,cameraUsed);
				purge.write(pictureData);
				purge.close();
				successfullWrite=true;
			} catch (FileNotFoundException e) {
				Log.d("DG_DEBUG", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("DG_DEBUG", "Error accessing file: " + e.getMessage());
			}
			finally {
				if(!successfullWrite)
				{
					try {
						if(purge==null)
						purge = new FileOutputStream(pictureFile);
						purge.write(data);
						purge.close();
					}
					catch (Exception e)
					{

					}
				}
			}

			// Adding Exif data for the orientation. For some strange reason the
			// ExifInterface class takes a string instead of a file.
			try {
				exif = new ExifInterface(CurrentFileName);
				exif.setAttribute(ExifInterface.TAG_ORIENTATION, ""
						+ orientation);
//				switch (orientation) {
//		        case ExifInterface.ORIENTATION_ROTATE_90:
//		        	exif.setAttribute(ExifInterface.TAG_ORIENTATION, ""
//							+ 90);
//		            break;
//		        case ExifInterface.ORIENTATION_ROTATE_180:
//		        	exif.setAttribute(ExifInterface.TAG_ORIENTATION, ""
//							+ 180);
//		            break;
//		        case ExifInterface.ORIENTATION_ROTATE_270:
//		        	exif.setAttribute(ExifInterface.TAG_ORIENTATION, ""
//							+ 270);
//		            break;
//		        }
				exif.saveAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	private byte checkCameraUsed() {
		//If current camera id is -10 (default value)
		if(currentCamreaID==-10)
			return CameraActivity.BACK_CAMERA;
		if(currentCamreaID==frontCameraID)
			return CameraActivity.FRONT_CAMERA;
		if(currentCamreaID==rearCameraID)
			return CameraActivity.BACK_CAMERA;

		return CameraActivity.BACK_CAMERA;
	}

	private void deleteImageFromSdCard() {
		try {
			File discardedPhoto = new File(CurrentFileName);
			discardedPhoto.delete();
		} catch (Exception ex) {

		}
	}

	@Override
	public void onBackPressed() {

		deleteImageFromSdCard();
		Intent intent = new Intent();
		intent.putExtra(CameraActivity.PARAMS_IS_IMAGE_CAPTURED, false);
		setResult(Activity.RESULT_OK, intent);
		super.onBackPressed();
	}

	/**
	 * Putting in place a listener so we can get the sensor data only when
	 * something changes.
	 */
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				RotateAnimation animation = null;
				if (event.values[0] < 4 && event.values[0] > -4) {
					if (event.values[1] > 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_90) {
						// UP
						orientation = ExifInterface.ORIENTATION_ROTATE_90;
						animation = getRotateAnimation(270);
						degrees = 270;
					} else if (event.values[1] < 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_270) {
						// UP SIDE DOWN
						orientation = ExifInterface.ORIENTATION_ROTATE_270;
						animation = getRotateAnimation(90);
						degrees = 90;
					}
				} else if (event.values[1] < 4 && event.values[1] > -4) {
					if (event.values[0] > 0
							&& orientation != ExifInterface.ORIENTATION_NORMAL) {
						// LEFT
						orientation = ExifInterface.ORIENTATION_NORMAL;
						animation = getRotateAnimation(0);
						degrees = 0;
					} else if (event.values[0] < 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_180) {
						// RIGHT
						orientation = ExifInterface.ORIENTATION_ROTATE_180;
						animation = getRotateAnimation(180);
						degrees = 180;
					}
				}
				if (animation != null) {
					rotatingImage.startAnimation(animation);
					rotatingSave.startAnimation(animation);
					rotatingDiscard.startAnimation(animation);
					switchCamera.startAnimation(animation);

				}
			}

		}
	}

	/**
	 * Calculating the degrees needed to rotate the image imposed on the button
	 * so it is always facing the user in the right direction
	 *
	 * @param toDegrees
	 * @return
	 */
	private RotateAnimation getRotateAnimation(float toDegrees) {
		float compensation = 0;

		if (Math.abs(degrees - toDegrees) > 180) {
			compensation = 360;
		}

		// When the device is being held on the left side (default position for
		// a camera) we need to add, not subtract from the toDegrees.
		if (toDegrees == 0) {
			compensation = -compensation;
		}

		// Creating the animation and the RELATIVE_TO_SELF means that he image
		// will rotate on it center instead of a corner.
		RotateAnimation animation = new RotateAnimation(degrees, toDegrees
				- compensation, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		// Adding the time needed to rotate the image
		animation.setDuration(250);

		// Set the animation to stop after reaching the desired position. With
		// out this it would return to the original state.
		animation.setFillAfter(true);

		return animation;
	}

	/**
	 * STUFF THAT WE DON'T NEED BUT MUST BE HEAR FOR THE COMPILER TO BE HAPPY.
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}