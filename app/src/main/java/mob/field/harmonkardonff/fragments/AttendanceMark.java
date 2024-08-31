package mob.field.harmonkardonff.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import linq.ArrayList;
import mob.field.harmonkardonff.GpsDetector;
import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.entitiymodels.attnd_model;
import mob.field.harmonkardonff.services.NotifyService;
import mob.field.harmonkardonff.services.WebService;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.image.capture.ImageActivity;
import app.core.model.ImageCaptureResult;
import app.core.model.Response;
import app.core.utils.Dialog;
import app.core.utils.Message;

import com.fieldforce.harmonkardonff.AttandanceRegularize;
import com.fieldforce.harmonkardonff.ImageCaptureActivity;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonhelper.GPSTracker;

import app.core.geoservice.GpsResultCallBack;

import com.fieldforce.utility.Storage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AttendanceMark extends IFragment implements OnMapReadyCallback {

	private boolean IsImageUploaed = false;
	private MDAT CurrentMDAT = new MDAT();
	private Date LStartDate;
	private Date LEndDate;
	LocationManager locationManager;
	MapView mapView;
	private GoogleMap mMap;
	WebService server = new WebService();
	GPSTracker gpsTracker;
	private double userLat = 0.0;
	private double userLong = 0.0;
	// Google Map
	private GoogleMap googleMap;
	Fragment map;
	GpsResultCallBack gpsLocationcallBack = new GpsResultCallBack() {

		@Override
		public void onLocationChange(double longitude, double latitude) {

			if (googleMap != null)
				addMarkerOnMap(latitude, longitude);

			if (findViewById(R.id.mark_attendance_progress_bar) != null)
				findViewById(R.id.mark_attendance_progress_bar).setVisibility(View.INVISIBLE);

			userLat = latitude;
			userLong = longitude;

		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			ImageCaptureResult result = getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);
			showImageToastAfterImageCapture(result);
			if (result.getTotalImagesCount() > 0) {
				setDocsIDs(result.getDocsIDs(),
						result.getOnlineImagesCount() > 0);
			}
		}
	}

	private void getMobileDeviceId() {
		TelephonyManager mngr = (TelephonyManager) getActivity()
				.getSystemService(Context.TELEPHONY_SERVICE);
		CurrentMDAT.IMEI = Storage.getPhoneIMEI(getActivity()
				.getApplicationContext());// mngr.getDeviceId();
		/*
		 * CurrentMDAT.Operator = mngr.getNetworkOperatorName();
		 * CurrentMDAT.DeviceModel = Build.MODEL; CurrentMDAT.Manufacture =
		 * Build.MANUFACTURER; CurrentMDAT.OSVersion = Build.VERSION.SDK_INT;
		 */
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = null;
		try {
			v = this.InflateView(R.layout.activity_attendance, inflater, container);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return v;
	}

	@Override
	public void Activate(View arg0) {
		CheckMarkingAvilable();
		SetCurrentDate();
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GPSTracker(getActivity().getApplicationContext()));
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GpsDetector(getActivity()));


		gpsTracker = new GPSTracker(getActivity().getApplicationContext());
		gpsTracker.refreshGps();
		gps.setCallBack(gpsLocationcallBack);

		initilizeMap();
		addMarkerOnMap(gpsTracker.getLatitude(), gpsTracker.getLongitude());
		LStartDate = GetCurrentDate();
		AttachSpinnerEventHandler();
		AttachCapturePhoto();
		AttachSubmitDDAT();
		this.hideKeyPad();
		// statusCheck();
		checkingForAttendaceOnServer();

		/** Checks if user has marked attendance today else shows an Alert Box */
//		checkIfUserHasMarkedAttendanceToday();

	}

	private void initilizeMap() {
		try {

			MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mark_attendance_map_fragment);
			if (mapFragment != null) {
				mapFragment.getMapAsync(this);


			}
			// check if map is created successfully or not
		} catch (Exception exception) {
			new Dialog(getActivity()).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
		}

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		// Add a marker in Sydney and move the camera
		this.googleMap=googleMap;
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(false);
		googleMap.getUiSettings().setScrollGesturesEnabled(true);
		googleMap.getUiSettings().setTiltGesturesEnabled(false);
		/*LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
		googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15F));
		// Zoom in, animating the camera.
		googleMap.animateCamera(CameraUpdateFactory.zoomIn());
		// Zoom out to zoom level 10, animating with a duration of 2 seconds.
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null);*/
	}
    


	
	 MarkerOptions marker = new MarkerOptions();
	private void addMarkerOnMap(double latitude, double longitude) {
		try {
			if (googleMap == null) {

				return;
			}

			googleMap.clear();

			// create marker
			marker.position(new LatLng(latitude, longitude)).title("Your Current Location");

			// adding marker
			googleMap.addMarker(marker);

			CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(17).build();

			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		} catch (Exception exception) {
			new Dialog(getActivity()).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
		}
	}
	    
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (gpsTracker != null)
			gpsTracker.refreshGps();
	}

	private void checkIfUserHasMarkedAttendanceToday() {
		BackgroundProcess checkAttendanceprocess = new BackgroundProcess(this);
		checkAttendanceprocess
				.setProgressMessage("Checking Previous Attendance...");
		checkAttendanceprocess.setbackgroundProcess(new IProcess() {

			@Override
			public Object underProcess() throws Exception {
				return server.checkPreviousAttendance(GetCurrentDateInString());
			}

			@Override
			public void processResponse(Object response) throws Exception {
				Response checkResponse = (Response) response;
				if (checkResponse.status.equalsIgnoreCase("false")) {
					showMessage(checkResponse.errormsg.toString());
				}
			}
		});
		checkAttendanceprocess.execute();
	}

	private void showMessage(String message) {
		message = message.replace(".", ":");
		String[] messages = message.split(":");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("1. " + messages[0] + "\n" + "2. " + messages[1])
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		builder.setTitle("Before Marking Attendance Please Ensure");
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void checkingForAttendaceOnServer() {
		if (IsPendingToday()) {

			final String date = GetCurrentDateInString();
			// String date = GetCurrentDate() + "";
			// / String date = "2016-10-11T18:30:00.000Z";
			/*
			 * if (date.contains("IST")) date = getISTFormatToYYMMDD(date); else
			 * if (date.contains("T")) date =
			 * getDateFromTSeperatorToYYYYMMDD(date);
			 */
			if (date == null)
				return;

			/*
			 * String apiUrl = server.ApiUrl; apiUrl +=
			 * "attendance/get_attendance/" + date + "/?"; apiUrl += "username="
			 * + server.getUsername(); final String url = apiUrl;
			 */
			loadData();
			loadData1();

			BackgroundProcess bp = new BackgroundProcess(this.context, false);

			bp.setbackgroundProcess(new IProcess() {

				@SuppressWarnings("rawtypes")
				@Override
				public void processResponse(Object arg0) throws Exception {
					Response resp = (Response) arg0;
					if (resp == null) {
						return;
					}
					if (resp.isSuccess()) {
						saveAttendanceFromServerIFExist(resp.data);
					}
					/*
					 * Response res = (Response) arg0; MDAT mdat = (MDAT)
					 * res.data.get(0);
					 */
				}

				@Override
				public Object underProcess() throws Exception {

					return server.getattendance(date, date);
				}
			});

			bp.execute(null, null, null);

		}
	}

	/*
	 * protected void saveAttendanceFromServerIFExist(JSONObject resJsonObject)
	 * { if (resJsonObject != null) { JSONObject jobj; try { jobj =
	 * resJsonObject.getJSONObject("data"); if (jobj != null) { if
	 * (!jobj.isNull("attendance")) { CurrentMDAT.option =
	 * jobj.getString("attendance"); SaveAttendanceInLocalDb("false", new
	 * MDAT()); OnUpdateComplete(); } } } catch (JSONException e) {
	 * e.printStackTrace(); } } }
	 */
	protected void saveAttendanceFromServerIFExist(ArrayList<attnd_model> data) {
		if (data != null && data.size() > 0) {
			if (ToBool(data.get(0).IsMarked)) {
				CurrentMDAT.option = data.get(0).Attendance;
				SaveAttendanceInLocalDb("false", new MDAT());
				OnUpdateComplete();
			}
		}
	}

	private void OnUpdateComplete() {
		this.setTab(1);
	}

	private void loadData() {
		getLatituteLogitute();

	}

	/*
	 * private boolean getLatituteLogitute() { GPSTracker gpsTracker = new
	 * GPSTracker(getActivity()); CurrentMDAT.Latitude =
	 * String.valueOf(gpsTracker.getLatitude()); CurrentMDAT.Longitude =
	 * String.valueOf(gpsTracker.getLongitude()); if
	 * (CurrentMDAT.Latitude.equalsIgnoreCase("0.0") ||
	 * CurrentMDAT.Longitude.equalsIgnoreCase("0.0")) {
	 * ShowToastLong("GPS co-ordinates not found!!", Toast.LENGTH_LONG); return
	 * false; } ShowToastLong("GPS co-ordinates capture successfully!!",
	 * Toast.LENGTH_LONG); return true; }
	 */
	private void loadData1() {
		// CurrentMDAT.for_date = this.GetCurrentDateInStrings();
		// CurrentMDAT.Remarks = remarks_edit.getText().toString();
		CurrentMDAT.ForDate = this.GetCurrentDateInString();

	}

	private void CheckMarkingAvilable() {
		if (IsPendingToday()) {
			this.setVisibility(R.id.txt_msg, View.GONE);
		} else {
			this.setVisibility(R.id.txt_msg, View.VISIBLE);
			this.ShowToast("Your attendance is already marked");
			this.setVisibility(R.id.btn_take_pic, View.VISIBLE);
		}
	}

	private boolean IsPendingToday() {
		if (MainActivity.MyAttendances.Count() == 0)
			return true;
		else {
			CurrentMDAT = MainActivity.MyAttendances.where("ForDate",
					GetCurrentDateInString()).First();
			if (CurrentMDAT == null) {
				CurrentMDAT = new MDAT();
				return true;
			} else
				return false;
		}

	}

	private void AttachCapturePhoto() {

		this.findViewById(R.id.btn_take_pic).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						CapturePhoto();
					}
				});

	}

	public void statusCheck() {
		final LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();

		}

	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();

	}

	private void AttachSubmitDDAT() {
		this.SetOnClickListenerOnButton(R.id.btnSubmitDAT,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// final LocationManager manager = (LocationManager)
						// context
						// .getSystemService(Context.LOCATION_SERVICE);
						// if (manager
						// .isProviderEnabled(LocationManager.GPS_PROVIDER)
						// || !CurrentMDAT.option.equalsIgnoreCase("P")) {
						UpdateAttendance();
						// }
						// else {
						// buildAlertMessageNoGps();
						// }
					}
				});
	}

	private void AttachSpinnerEventHandler() {
		Spinner spinner = (Spinner) this.findViewById(R.id.spdat);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String _DATOptions = (String) arg0.getSelectedItem();

				CurrentMDAT.option = getdatoptn(_DATOptions);
				if (CurrentMDAT.option.equalsIgnoreCase("L")) {
					ShowDateOptions();
				} else {
					HideDateOptions();
				}

				if (CurrentMDAT.option.equalsIgnoreCase("CO")) {
					ShowDateOptions();
				} else {
					HideDateOptions();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

	}

	protected String getdatoptn(String _DATOptions) {
		switch (_DATOptions) {
		case "Present":
			return "P";
		case "Training":
			return "T";
		case "Leave":
			return "L";
		case "Weekly Off":
			return "O";
		case "Comp Off":
				return "CO";
		case "National Holiday":
				return "NH";

		}
		return _DATOptions;

	}

	protected void HideDateOptions() {
		this.setVisibility(R.id.btn_take_pic, View.GONE);
		LinearLayout llDate = (LinearLayout) this.findViewById(R.id.datell);
		llDate.setVisibility(View.GONE);
	}

	protected void ShowDateOptions() {
		this.setVisibility(R.id.btn_take_pic, View.VISIBLE);
		LinearLayout llDate = (LinearLayout) this.findViewById(R.id.datell);
		llDate.setVisibility(View.VISIBLE);
	}

	private void SetCurrentDate() {
		SetTextViewAsString(R.id.date, GetCurrentDateInString());
	}

	public void CapturePhoto() {
		Intent I = new Intent(this.context, ImageCaptureActivity.class);
		I.putExtra(ImageCaptureActivity.PARAMS_DOC_TYPE, "Attendance");
		I.putExtra(ImageCaptureActivity.PARAMS_USERNAME, User.GetUserName());
		I.putExtra(ImageCaptureActivity.PARAMS_GUID, CurrentMDAT.guid);
		I.putExtra(ImageActivity.PARAMS_MODEL_ID, CurrentMDAT.ID);
		I.putExtra(ImageActivity.ACTION_ENABLE_GPS, true);
		// For getting the result in the parent activity
		// this.context.startActivityForResult(I, 1);

		// for getting the result in the fragment
		startActivityForResult(I, 1);

	}

	private void SubmitAttendanceOnServer() {
		if (isNetworkAvailable()) {
			BackgroundProcess bp = new BackgroundProcess(this.context)
					.setProgressMessage("sending to server..");
			bp.setbackgroundProcess(new IProcess() {

				@SuppressWarnings("rawtypes")
				@Override
				public void processResponse(Object arg0) throws Exception {
					ProcessAttendanceResponse((Response) arg0);
				}

				@Override
				public Object underProcess() throws Exception {

					return server.TryMarkAttendance(CurrentMDAT);
				}
			});

			bp.execute(null, null, null);
		} else {
			new Dialog(this.context).setTitle("Message").show(
					Message.NO_INTERNET_FOUND);
			SaveAttendanceInLocalDb("true", new MDAT());
			this.OnUpdateComplete(false);
		}
	}

	@SuppressWarnings("rawtypes")
	private void ProcessAttendanceResponse(Response response) {
		if (response.isSuccess() && response.isDataFound()) {
			this.ShowToast("Attendance updated successfully!");
			SaveAttendanceInLocalDb("false", (MDAT) response.data.First());
			NotifyService.unNotityForPendingAttendance();
			OnUpdateComplete(true);
		} else {
			if (response.errormsg.contains("Marked by")) {
				SaveAttendanceInLocalDb("false", new MDAT());
			} else {
				SaveAttendanceInLocalDb("true", new MDAT());
			}
			new Dialog(this.context).show(response.errormsg);
			OnUpdateComplete(false);

		}

	}

	private void getLatituteLogitute() {

		// Storage.ShowToast(""+gpsTracker.latitude+" : "+gpsTracker.longitude,
		// this);
		CurrentMDAT.Latitude = String.valueOf(gpsTracker.getLatitude());
		CurrentMDAT.Longitude = String.valueOf(gpsTracker.getLongitude());
		// if (CurrentMDAT.Latitude.equalsIgnoreCase("0.0")
		// || CurrentMDAT.Longitude.equalsIgnoreCase("0.0")) {
		// ShowToast("Problem to get location");
		// return false;
		// }
		// return true;
	}

	private void UpdateAttendance() {

		if ((CurrentMDAT.DocIDs != null && !CurrentMDAT.DocIDs
				.equalsIgnoreCase(""))
				|| !CurrentMDAT.option.equalsIgnoreCase("P")) {
			CurrentMDAT.ForDate = this.GetCurrentDateInString();
			getMobileDeviceId();
			getLatituteLogitute();
			if (CurrentMDAT.option.equalsIgnoreCase("L")) {
				GetLeaveDates();
				String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                CurrentMDAT.CheckInTime=currentTime;
				CurrentMDAT.CheckOutTime="";
				if (Validate()) {
					SaveForEachLeaveDay();
					SubmitAttendanceOnServer();
				} else
					return;
			} else {
				String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
				CurrentMDAT.CheckInTime=currentTime;
				CurrentMDAT.CheckOutTime="";
				if (Validate()) {
					SubmitAttendanceOnServer();
				} else
					return;
			}
		} else {
			Storage.ShowToast("Image is mandatory, Please attach image!",
					AttendanceMark.this.getActivity());
		}
	}

	private void OnUpdateComplete(boolean status) {
		if (status == true) {
			this.setTab(1);
		}
	}

	private void SaveForEachLeaveDay() {
		Calendar sDate = this.getDatePart(LStartDate);
		Calendar eDate = this.getDatePart(LEndDate);

		while (sDate.compareTo(eDate) <= 0) {

			CurrentMDAT.ForDate = this.ConvertDateToString(sDate.getTime());
			// SaveAttendanceInLocalDb();
			sDate.add(Calendar.DAY_OF_MONTH, 1);
		}

	}

	private void GetLeaveDates() {
		CurrentMDAT.LStartDate = this.GetCurrentDateInString();
		CurrentMDAT.LEndDate = this.GetDateInString(R.id.dpedate);
		LEndDate = this.GetDateFDP(R.id.dpedate);
	}

	private void SaveAttendanceInLocalDb(String status, MDAT updatedDAT) {
		try {
			CurrentMDAT.IsOfflineOnly = status;
			CurrentMDAT.ID = updatedDAT.ID;
			CurrentMDAT.InsertOrUpdate();
		} catch (Exception ex) {
			Log.e("Error:", ex.toString());
		}
	}

	private boolean Validate() {

		if (CurrentMDAT.option.equalsIgnoreCase("L")) {
			if (LEndDate.before(LStartDate)) {
				ShowToast("Please Enter Valid Leave Dates");
				return false;
			}
		}

		if (CurrentMDAT.option.equalsIgnoreCase("Select")) {
			ShowToast("Please Select Option");
			return false;
		}
		if (CurrentMDAT.Latitude == null
				|| CurrentMDAT.Latitude.equalsIgnoreCase("")
				|| CurrentMDAT.Latitude.equalsIgnoreCase("0.0")
				|| CurrentMDAT.Longitude == null
				|| CurrentMDAT.Longitude.equalsIgnoreCase("")
				|| CurrentMDAT.Longitude.equalsIgnoreCase("0.0")) {
			ShowToast("Gps Co-ordinates are mandatory");
			buildAlertMessageNoGps();
			return false;
		}

		// if (!IsImageUploaed) {
		// ShowToast("Please upload the image first !");
		// return false;
		// }

		return true;
	}

	public void setDocsIDs(String docsIDs, boolean IsImageUploaded) {
		this.CurrentMDAT.DocIDs = docsIDs;
		this.IsImageUploaed = IsImageUploaded;

	}

	private void showImageToastAfterImageCapture(ImageCaptureResult result) {
		if (result.getTotalImagesCount() == 1)
			ShowToastLong(result.getTotalImagesCount() + " image is attached",
					0);
		else if (result.getTotalImagesCount() > 1)
			ShowToastLong(result.getTotalImagesCount() + " image are attached",
					0);
		else
			ShowToastLong("No image found to attach", 0);

		if (result.getOfflineImagesCount() == 1)
			ShowToastLong(result.getOfflineImagesCount()
					+ " image is still pending to upload", 0);
		else if (result.getOfflineImagesCount() > 1)
			ShowToastLong(result.getOfflineImagesCount()
					+ " image are still pending to upload", 0);
	}

}
