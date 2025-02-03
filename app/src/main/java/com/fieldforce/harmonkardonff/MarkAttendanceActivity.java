package com.fieldforce.harmonkardonff;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fieldforce.utility.RuntimePermissionContainer;
import com.fieldforce.utility.Storage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.geoservice.GpsResultCallBack;
import app.core.image.slider.AppConstant;
import app.core.model.Response;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.entitiymodels.attnd_model;
import mob.field.harmonkardonff.services.NotifyService;
import mob.field.harmonkardonff.services.WebService;
import mob.field.harmonkardonff.tabs.DatTab;

import static com.fieldforce.harmonkardonff.MainActivity.IS_SOURCE_DIALOG;
import static com.fieldforce.harmonkardonff.MainActivity.gpsTracker;

public class MarkAttendanceActivity extends InnosolsActivity implements OnMapReadyCallback {

    WebService server = new WebService();
    MarkerOptions marker = new MarkerOptions();
    private boolean IsImageUploaed = false;
    private MDAT CurrentMDAT = new MDAT();
    private Date LStartDate;
    private Date LEndDate;
    // Google Map
    private GoogleMap googleMap;
    private double userLat = 0.0;
    private double userLong = 0.0;
    private MapView mapView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        SetTextViewAsString(R.id.current_user_name, MainActivity.MyInfo.ISPName + " (" + WebService.UserName + ")");
        try {
            checkForAllPermissions();
        }
        catch (Exception exception) {
            new Dialog(this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
        }
        /*CheckMarkingAvilable();
        SetCurrentDate();

        LStartDate = getCurrentDate();
        AttachSpinnerEventHandler();
        AttachCapturePhoto();
        AttachSubmitDDAT();
        AttachRefreshGpsButton();
        this.hideKeyPad();


        InvokeGPSService();

        locationHelper.setCallBack(gpsLocationcallBack);


        if (!IsPendingToday()) {
            checkingForAttendaceOnServer();
        }

        if (IsPendingToday()) {
            if (!locationHelper.isGpsEnabled()) {
                locationHelper.showSettingsAlert();
            }
        }

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);*/


    }

    private void checkForAllPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(RuntimePermissionContainer.getPermissionsForCheckIn(), AppConstant.PERM_REQ_CODE);
        }
    }

    private HashMap<String, Boolean> temp = new HashMap<>();

    private static final String TAG = "AttendanceMarko";

    private boolean isAllPermissionGranted = true;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstant.PERM_REQ_CODE) {
            isAllPermissionGranted = true;
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                Log.d(TAG, "onRequestPermissionsResult: " + i + " : " + permission);
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    temp.put(permission, false);
                    isAllPermissionGranted = false;
                    boolean showRationale = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, AppConstant.PERM_REQ_CODE);
                    } else if (Manifest.permission.WRITE_CONTACTS.equals(permission)) {
                        checkForAllPermissions();
                        //showRationale(permission, R.string.permission_denied_contacts);
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                    } else if (Manifest.permission.READ_CONTACTS.equals(permission)) {
                        checkForAllPermissions();
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
                        checkForAllPermissions();
                    } else if (Manifest.permission.CAMERA.equals(permission)) {
                        checkForAllPermissions();
                    }
                } else {
                    temp.put(permission, true);
                }
            }
            for (String str : RuntimePermissionContainer.getPermissionsForCheckIn()) {
                if (temp.get(str) != null && !temp.get(str)) {
                    isAllPermissionGranted = false;
                }
            }
            if (isAllPermissionGranted) {
                ShowToast("Permissions Granted");
                weHavePermissionsToProceed();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void weHavePermissionsToProceed() {
        // mapView = findViewById(R.id.mark_attendance_map_fragment);
        // mapView = findViewById(R.id.mark_attendance_map_fragment);
        CheckMarkingAvilable();
        SetCurrentDate();

        LStartDate = getCurrentDate();
        AttachSpinnerEventHandler();
        AttachCapturePhoto();
        AttachSubmitDDAT();
        AttachRefreshGpsButton();
        this.hideKeyPad();


        InvokeGPSService();

        locationHelper.setCallBack(gpsLocationcallBack);
        initilizeMap();



        if (!IsPendingToday()) {
            checkingForAttendaceOnServer();
        }

        if (IsPendingToday()) {
            if (!locationHelper.isGpsEnabled()) {
                locationHelper.showSettingsAlert();
            }
        }

      /*  mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);*/

        //getMessageForCoronaSurveyFromWeb();
    }

    private void AttachRefreshGpsButton() {

        findViewById(R.id.mark_attendance_refresh_location).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (findViewById(R.id.mark_attendance_progress_bar) != null)
                    findViewById(R.id.mark_attendance_progress_bar).setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            locationHelper.togglePeriodicLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String GUID = data.getExtras().getString(SimpleCameraActivity.PARAMS_GUID);
            String IMAGE_CAPTURED = data.getExtras().getString("IMAGE_CAPTURED") + "";
            CurrentMDAT.DocIDs = data.getExtras().getString("DocID");
            // ImageCaptureResult result = getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);
            showImageToastAfterImageCapture(IMAGE_CAPTURED);
            // if (result.getTotalImagesCount() > 0) {
            setDocsIDs(CurrentMDAT.DocIDs, true);
            // }
        }
    }

    private void getMobileDeviceId() {
        try {
            TelephonyManager mngr = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            CurrentMDAT.IMEI = Storage.getPhoneIMEI(this
                    .getApplicationContext());// mngr.getDeviceId();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        /*
         * CurrentMDAT.Operator = mngr.getNetworkOperatorName();
         * CurrentMDAT.DeviceModel = Build.MODEL; CurrentMDAT.Manufacture =
         * Build.MANUFACTURER; CurrentMDAT.OSVersion = Build.VERSION.SDK_INT;
         */
    }

    private void initilizeMap() {

        try {

            MapFragment mapFragment = (MapFragment) this.getFragmentManager().findFragmentById(R.id.mark_attendance_map_fragment);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);


            }
            // check if map is created successfully or not
        } catch (Exception exception) {
            new Dialog(this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
        }
    }

    private void addMarkerOnMap(double latitude, double longitude) {
        if (googleMap == null) {
            Toast.makeText(this,
                            "Sorry! Unable To Show Location On Map", Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        googleMap.clear();

        // create marker
        marker.position(new LatLng(latitude, longitude)).title("Your Current Location");

        // adding marker
        googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(17).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onPause() {
        super.onPause();
       // mapView.onPause();
        try {
            locationHelper.stopLocationUpdates();
        } catch (Exception e) {
        }

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

            BackgroundProcess bp = new BackgroundProcess(this, false);

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
                OnUpdateComplete(true);
            }
        }
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
            this.setVisibility(R.id.btn_take_pic, View.GONE);
            this.setVisibility(R.id.mark_attendance_date_layout, View.GONE);
            this.setVisibility(R.id.mark_attendance_option_layout, View.GONE);
            this.setVisibility(R.id.btnSubmitDAT, View.GONE);
            this.setVisibility(R.id.mark_attendance_remarks_layout, View.GONE);
            this.setVisibility(R.id.mark_attendance_current_location_layout, View.GONE);
            this.setVisibility(R.id.mark_attendance_map_fragment, View.GONE);
            this.setVisibility(R.id.cv_mark_attendance_map_fragment, View.GONE);
            this.setVisibility(R.id.mark_attendance_current_docid, View.GONE);

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
        final LocationManager manager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String _DATOptions = (String) arg0.getSelectedItem();
                //ShowToast(_DATOptions);
                hideCapturePhotoButton(_DATOptions);
                CurrentMDAT.option = getdatoptn(_DATOptions);
                if (CurrentMDAT.option.equalsIgnoreCase("L")) {
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
            case "Absent":
                return "A";
            case "Leave":
                return "L";
            case "Weekly Off":
                return "O";
            case "Gate-meeting":
                return "GM";
            case "Half Day":
                return "Half Day";
            case "Comp Off":
                return "CO";
            case "National Holiday":
                return "NH";
        }
        return _DATOptions;

    }

    protected void HideDateOptions() {
        LinearLayout llDate = (LinearLayout) this.findViewById(R.id.datell);
        llDate.setVisibility(View.GONE);
    }

    private void hideCapturePhotoButton(String optionSelected) {
        Button button = findViewById(R.id.btn_take_pic);
        CurrentMDAT.option = getdatoptn(optionSelected);
        if (CurrentMDAT.option.equalsIgnoreCase("A")) {
            button.setVisibility(View.GONE);
        } else {
            if (CurrentMDAT.option.equalsIgnoreCase("L")) {
                button.setVisibility(View.GONE);
            } else {
                if (CurrentMDAT.option.equalsIgnoreCase("O")) {
                    button.setVisibility(View.GONE);
                }
                else if (CurrentMDAT.option.equalsIgnoreCase("CO")) {
                    button.setVisibility(View.GONE);
                }
                else if (CurrentMDAT.option.equalsIgnoreCase("NH")) {
                    button.setVisibility(View.GONE);
                } else {
                    button.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    protected void ShowDateOptions() {
        LinearLayout llDate = (LinearLayout) this.findViewById(R.id.datell);
        llDate.setVisibility(View.VISIBLE);
    }

    private void SetCurrentDate() {
        SetTextViewAsString(R.id.date, GetCurrentDateInString());
    }

    public void CapturePhoto() {
        Intent I = new Intent(this, SimpleCameraActivity.class);
        I.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "Attendance");
        I.putExtra(SimpleCameraActivity.PARAMS_USERNAME, User.GetUserName());
        I.putExtra(SimpleCameraActivity.PARAMS_GUID, CurrentMDAT.guid);
        I.putExtra(SimpleCameraActivity.PARAMS_MODEL_ID, CurrentMDAT.ID);
        I.putExtra(SimpleCameraActivity.ACTION_ENABLE_GPS, true);
        // For getting the result in the parent activity
        // this.context.startActivityForResult(I, 1);

        // for getting the result in the fragment
        startActivityForResult(I, 1);

    }

    private void SubmitAttendanceOnServer() {
        if (isNetworkAvailable()) {
            BackgroundProcess bp = new BackgroundProcess(this)
                    .setProgressMessage("sending to server..");
            bp.setbackgroundProcess(new IProcess() {

                @SuppressWarnings("rawtypes")
                @Override
                public void processResponse(Object arg0) throws Exception {
                    ProcessAttendanceResponse((Response) arg0);
                }

                @Override
                public Object underProcess() throws Exception {
                    return server.TryMarkAttendance1(CurrentMDAT);
                }
            });

            bp.execute(null, null, null);
        } else {
            ShowToastLong("Attendance Saved In Pending Attendance", 0);
            SaveAttendanceInLocalDb("true", new MDAT());
            putDataAsBoolean(IS_SOURCE_DIALOG, true);
            this.OnUpdateComplete(true);
        }
    }

    public static final String LOCATION_ERROR_MSG = "Dear ISP, Your current GPS location isn’t near your store location -which";
    //Dear ISP, Your Current GPS Location not near the store Location.( 12499  meters)(Store's lat :28.5330062,long :77.2735978)(Current lat:28.6062777,long :77.3740705

    @SuppressWarnings("rawtypes")
    private void ProcessAttendanceResponse(Response response) {
        if (response.isSuccess() && response.isDataFound()) {
            this.ShowToast("Attendance updated successfully!");
            if (isNetworkAvailable()) {

                BackgroundProcess backgroundProcess = new BackgroundProcess(MarkAttendanceActivity.this);
                backgroundProcess.setbackgroundProcess(new IProcess() {
                    @Override
                    public Object underProcess() throws Exception {
                        return server.GetDisplayPicNotification();

                    }

                    @Override
                    public void processResponse(Object response1) throws Exception {
                        Response res = (Response) response1;
                        if (res.status.equalsIgnoreCase("false"))
                            new AlertDialog.Builder(MarkAttendanceActivity.this)
                                    .setTitle("DisplayPic Notification")
                                    .setMessage(res.errormsg)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            SaveAttendanceInLocalDb("false", (MDAT) response.data.First());
                                            NotifyService.unNotityForPendingAttendance();
                                            OnUpdateComplete(true);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                    }
                });
                backgroundProcess.execute(null, null, null);

            }

        } else {
            Log.d(TAG, "ProcessAttendanceResponse: " + response.errormsg);
            if (response.errormsg != null) {
                String errorMessageValueFromServer = response.errormsg;
                if (errorMessageValueFromServer.contains(LOCATION_ERROR_MSG)) {
                    executeLocationNotNearYesNoDialog(errorMessageValueFromServer);
                } else {
                    executeNormalErrorMessageDialogWithOkButton(errorMessageValueFromServer);
                }
            } else {
                ShowToast("No Error message found");
            }
            // new Dialog(this).show(response.errormsg);
        }
    }

    private void executeLocationNotNearYesNoDialog(String errorMessage) {
        AlertDialog.Builder db = new AlertDialog.Builder(this);
        db.setMessage(errorMessage);
        db.setTitle("Alert");
        db.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CurrentMDAT.isForcefullyUpdated = "true";
                UpdateAttendance();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        db.show();
    }

    private void executeNormalErrorMessageDialogWithYesNoButton(String errorMessage) {
        AlertDialog.Builder db = new AlertDialog.Builder(this);
        db.setMessage(errorMessage);
        db.setTitle("Alert");
        db.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OnUpdateComplete(true);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        db.show();
    }

    private void executeNormalErrorMessageDialogWithOkButton(String errorMsg) {
        AlertDialog.Builder db = new AlertDialog.Builder(this);
        db.setMessage(errorMsg);
        db.setTitle("Alert");
        db.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        db.show();
    }

    private void getLatituteLogitute() {

        // Storage.ShowToast(""+gpsTracker.latitude+" : "+gpsTracker.longitude,
        // this);
        CurrentMDAT.Latitude = String.valueOf(userLat);
        CurrentMDAT.Longitude = String.valueOf(userLong);
        // if (CurrentMDAT.Latitude.equalsIgnoreCase("0.0")
        // || CurrentMDAT.Longitude.equalsIgnoreCase("0.0")) {
        // ShowToast("Problem to get location");
        // return false;
        // }
        // return true;
    }

    private void UpdateAttendance() {

        if (CurrentMDAT.option == null)
            CurrentMDAT.option = getdatoptn(getSpinnerAsString(R.id.spdat));


        if (((CurrentMDAT.DocIDs != null && !CurrentMDAT.DocIDs.equalsIgnoreCase("")) || !CurrentMDAT.option.equalsIgnoreCase("P"))) {
            if ((CurrentMDAT.option.equalsIgnoreCase("training") || CurrentMDAT.option.equalsIgnoreCase("GM")) && ((CurrentMDAT.DocIDs == null || CurrentMDAT.DocIDs.equalsIgnoreCase("")))) {
                ShowToast("Image is mandatory");
            } else {
                CurrentMDAT.ForDate = this.GetCurrentDateInString();
                CurrentMDAT.Remarks = this.GetEditTextAsString(R.id.mark_attendance_remarks);
                getMobileDeviceId();
                getLatituteLogitute();
                if (CurrentMDAT.option.equalsIgnoreCase("L")) {
                    GetLeaveDates();
                    if (Validate()) {
                        SaveForEachLeaveDay();
                        SubmitAttendanceOnServer();
                    } else
                        return;
                } else {
                    if (Validate()) {
                        SubmitAttendanceOnServer();
                    } else
                        return;
                }
            }
            /*CurrentMDAT.ForDate = this.GetCurrentDateInString();
            CurrentMDAT.Remarks = this.GetEditTextAsString(R.id.mark_attendance_remarks);
            getMobileDeviceId();
            getLatituteLogitute();
            if (CurrentMDAT.option.equalsIgnoreCase("L"))
            {
                GetLeaveDates();
                if (Validate()) {
                    SaveForEachLeaveDay();
                    SubmitAttendanceOnServer();
                } else
                    return;
            }
            else
                {
                if (Validate())
                {
                    SubmitAttendanceOnServer();
                } else
                    return;
            }*/
        } else {
            ShowToastLong("Image is mandatory, Please attach image!", 0);
        }
    }

    private void OnUpdateComplete(boolean status) {
        if (status == true) {
            startActivity(DatTab.class);
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

        return true;

        //Location check disabled

/*        if (locationHelp.isGpsEnabled()) {
            return true;
        } else {
            if (CurrentMDAT.Latitude == null
                    || CurrentMDAT.Latitude.equalsIgnoreCase("")
                    || CurrentMDAT.Latitude.equalsIgnoreCase("0.0")
                    || CurrentMDAT.Longitude == null
                    || CurrentMDAT.Longitude.equalsIgnoreCase("")
                    || CurrentMDAT.Longitude.equalsIgnoreCase("0.0")) {
                ShowToastLong("Gps Co-ordinates are mandatory , Turn On Gps", 0);
                buildAlertMessageNoGps();
                return false;
            } else
                return true;
        }*/

    }

    public void setDocsIDs(String docsIDs, boolean IsImageUploaded) {
        this.CurrentMDAT.DocIDs = docsIDs;

        this.IsImageUploaed = IsImageUploaded;

        GetTextView(R.id.mark_attendance_current_docid).append(docsIDs);

    }

    private void showImageToastAfterImageCapture(String result) {

        if (!result.equals(""))
            ShowToastLong(" image is attached", 0);
        else
            ShowToastLong("No image found to attach", 0);

    }

    @Override
    public void RegisterTableInfoForLocalDB() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        this.googleMap=googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        if(gpsTracker.getLatitude()!=0.0 &&gpsTracker.getLongitude()!=0.0) {
            LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15F));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null);
        }

    }

    @Override
    public void onResume() {
       // mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
       // mapView.onLowMemory();
    }
}
