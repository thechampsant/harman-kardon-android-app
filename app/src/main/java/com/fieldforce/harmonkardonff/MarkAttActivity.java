package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fieldforce.utility.Storage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.geoservice.GpsResultCallBack;
import app.core.image.capture.ImageActivity;
import app.core.model.ImageCaptureResult;
import app.core.model.Response;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.entitiymodels.attnd_model;
import mob.field.harmonkardonff.services.NotifyService;
import mob.field.harmonkardonff.services.WebService;
import mob.field.harmonkardonff.tabs.DatTab;

import static com.fieldforce.harmonkardonff.MainActivity.IS_SOURCE_DIALOG;

public class MarkAttActivity extends InnosolsActivity implements OnMapReadyCallback{

    WebService server = new WebService();
    // GPSTracker gpsTracker;
    SupportMapFragment mapFragment;
    private boolean IsImageUploaed = false;
    private MDAT CurrentMDAT = new MDAT();
    private Date LStartDate;
    private Date LEndDate;
    // Google Map
    private double userLat = 0.0, userLong = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        CheckMarkingAvilable();
        SetCurrentDate();


        InvokeGPSService();
        initializeMap();
        LStartDate = getCurrentDate();
        AttachSpinnerEventHandler();
        AttachCapturePhoto();
        AttachSubmitDDAT();
        AttachRefreshGpsButton();
        this.hideKeyPad();
        // statusCheck();

        if (!IsPendingToday())
            checkingForAttendaceOnServer();

        /*if (IsPendingToday()) {
            if (isNetworkAvailable()) {
                *//* Checks if user has marked attendance today else shows an Alert Box *//*
                checkIfUserHasMarkedAttendanceToday();
            }
        }*/

        if (IsPendingToday()) {
            if (!locationHelper.isGpsEnabled()) {
                locationHelper.showSettingsAlert();
            }
        }

    }

    private void initializeMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void AttachRefreshGpsButton() {

        findViewById(R.id.mark_attendance_refresh_location).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (findViewById(R.id.mark_attendance_progress_bar) != null)
                    findViewById(R.id.mark_attendance_progress_bar).setVisibility(View.VISIBLE);
              //  gpsTracker.refreshGps();

            }
        });
    }

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
        TelephonyManager mngr = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        CurrentMDAT.IMEI = Storage.getPhoneIMEI(this
                .getApplicationContext());// mngr.getDeviceId();
        /*
         * CurrentMDAT.Operator = mngr.getNetworkOperatorName();
		 * CurrentMDAT.DeviceModel = Build.MODEL; CurrentMDAT.Manufacture =
		 * Build.MANUFACTURER; CurrentMDAT.OSVersion = Build.VERSION.SDK_INT;
		 */
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationHelper!=null)
        locationHelper.stopLocationUpdates();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            ///this.setVisibility(R.id.cv_att_options, View.GONE);
            this.setVisibility(R.id.btnSubmitDAT, View.GONE);
            this.setVisibility(R.id.mark_attendance_remarks_layout, View.GONE);
            this.setVisibility(R.id.mark_attendance_current_location_layout, View.GONE);
           // this.setVisibility(R.id.mark_attendance_map_fragment, View.GONE);
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
                        UpdateAttendance();

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

        }
        return _DATOptions;

    }

    protected void HideDateOptions() {
        LinearLayout llDate = (LinearLayout) this.findViewById(R.id.datell);
        llDate.setVisibility(View.GONE);
    }

    protected void ShowDateOptions() {
        LinearLayout llDate = (LinearLayout) this.findViewById(R.id.datell);
        llDate.setVisibility(View.VISIBLE);
    }

    private void SetCurrentDate() {
        SetTextViewAsString(R.id.date, GetCurrentDateInString());
    }

    public void CapturePhoto() {
        Intent I = new Intent(this, ImageCaptureActivity.class);
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

                    return server.TryMarkAttendance(CurrentMDAT);
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
            new Dialog(this).show(response.errormsg);
            OnUpdateComplete(true);
        }

    }

    private void getLatituteLogitute() {

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

        if ((CurrentMDAT.DocIDs != null && !CurrentMDAT.DocIDs
                .equalsIgnoreCase(""))
                || !CurrentMDAT.option.equalsIgnoreCase("P")) {
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

        if (CurrentMDAT.Latitude == null
                || CurrentMDAT.Latitude.equalsIgnoreCase("")
                || CurrentMDAT.Latitude.equalsIgnoreCase("0.0")
                || CurrentMDAT.Longitude == null
                || CurrentMDAT.Longitude.equalsIgnoreCase("")
                || CurrentMDAT.Longitude.equalsIgnoreCase("0.0")) {

            new Dialog(this).setTitle("Information").show("Your Gps Coordinates not available Yet \n " +
                    "Please turn on Gps if not turned on" +
                    "and wait until your location is updated on map then Submit again");

            if (!locationHelper.isGpsEnabled())
                buildAlertMessageNoGps();

            return false;
        }


        return true;
    }

    public void setDocsIDs(String docsIDs, boolean IsImageUploaded) {
        this.CurrentMDAT.DocIDs = docsIDs;

        this.IsImageUploaed = IsImageUploaded;

        GetTextView(R.id.mark_attendance_current_docid).append(docsIDs);

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


    @Override
    public void RegisterTableInfoForLocalDB() {
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.4233438, -122.0728817))
                .title("LinkedIn")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.4629101,-122.2449094))
                .title("Facebook")
                .snippet("Facebook HQ: Menlo Park"));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.3092293, -122.1136845))
                .title("Apple"));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4233438, -122.0728817), 10));
    }
}
