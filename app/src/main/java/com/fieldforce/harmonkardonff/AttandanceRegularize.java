package com.fieldforce.harmonkardonff;

import static com.fieldforce.harmonhelper.App.GetCurrentDate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.fieldforce.harmonhelper.GPSTracker;
import com.fieldforce.harmonkardonff.corona_survey_module.ui.CoronaSurveyTabsActivity;
import com.fieldforce.utility.RuntimePermissionContainer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.image.slider.AppConstant;
import app.core.model.Response;
import app.core.utils.Dialog;
import app.core.utils.Message;
import linq.ArrayList;
import mob.field.harmonkardonff.GpsDetector;
import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.entitiymodels.StringModel;
import mob.field.harmonkardonff.fragments.ViewAttendance;
import mob.field.harmonkardonff.services.WebService;

public class AttandanceRegularize extends InnosolsActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    LocationManager locationManager;
    private GPSTracker gpsService;
    TextView date;
    private MarkerOptions marker = new MarkerOptions();
    private TextView textViewReferesh;
    private EditText remarkEditText;
    private String CheckInTime="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        if (requestCode == AppConstant.PERM_REQ_CODE) {
            checkForAllPermissions();
        }
    }

    private static View view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regularrize_attandance);
        try {
            // checkForAllPermissions();
            String date= ViewAttendance.datee;
            CheckInTime= getIntent().getStringExtra("CheckInTime");
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            if(!CheckInTime.equalsIgnoreCase("-"))
            {
                SetTextViewAsString(R.id.checkintime,(CheckInTime));
            }
            else {
                SetTextViewAsString(R.id.checkintime, currentTime);
            }
            getView(R.id.checkintime).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(AttandanceRegularize.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            SetTextViewAsString(R.id.checkintime,( getDD(selectedHour) + ":" + getDD(selectedMinute)));
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });
            getView(R.id.checkouttime).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    int second = mcurrentTime.get(Calendar.SECOND);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(AttandanceRegularize.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            SetTextViewAsString(R.id.checkouttime,( getDD(selectedHour) + ":" + getDD(selectedMinute)));
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });



            SetTextViewAsString(R.id.checkouttime, currentTime);

            if(date.equalsIgnoreCase(""))
                SetTextViewAsString(R.id.date, GetCurrentDateInString());
            else {
                SetTextViewAsString(R.id.date, date);
                //GetButton(R.id.btn_img).setVisibility(View.GONE);
            }

            try {
                checkForAllPermissions();
            }
            catch (Exception exception) {
                new Dialog(this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
            }
           /* textViewReferesh = findViewById(R.id.tv_getLocation);
            remarkEditText = findViewById(R.id.edt_remarks_checkOut);*/


        } catch (Exception exception) {
            new Dialog(this).setTitle("Exception").setMessage(exception.getMessage() + " in " + TAG + ", " + exception.getStackTrace()[0]).show();
        }
    }
    InnosolsActivity Base;
    private MDAT CurrentMDAT = new MDAT();
    private Date LStartDate;
    private Date LEndDate;
    private boolean IsImageUploaed = false;
     WebService server = new WebService();


    private String getDD(int num) {
        return num > 9 ? "" + num : "0" + num;
    }

    @SuppressLint("MissingPermission")
    private void weHavePermissionsToProceed() {
        /**
         * User came After Clicking Update Pending Attendance on MainActivity,
         * so we will route him to Pending Attendance Tab
         */
        if (getDataAsBoolean(MainActivity.IS_SOURCE_DIALOG)) {
            putDataAsBoolean(MainActivity.IS_SOURCE_DIALOG, false);
            setTab(1);
        }
        CheckMarkingAvilable();
        initilizeMap();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GpsDetector(AttandanceRegularize.this));

        gpsService = new GPSTracker(AttandanceRegularize.this);

        //SetCurrentDate();
        LStartDate = GetCurrentDate();
        SetRestrictionOnDatePicker();
        AttachSpinnerEventHandler();

        AttachCapturePhoto();
        AttachSubmitDDAT();
        this.hideKeyPad();

        //getMessageForCoronaSurveyFromWeb();
    }













    private void CheckMarkingAvilable() {
        if(ViewAttendance.datee.equalsIgnoreCase("")){
            if (IsPendingToday()) {
                this.setVisibility(R.id.txt_msg, View.GONE);
                this.setVisibility(R.id.scrollView1, View.VISIBLE);
            } else {
                this.setVisibility(R.id.txt_msg, View.VISIBLE);
                this.setVisibility(R.id.scrollView1, View.GONE);
                //this.ShowToast("Your attendance is already marked");
            }
        }
        else {
            this.setVisibility(R.id.txt_msg, View.GONE);
            this.setVisibility(R.id.scrollView1, View.VISIBLE);
        }

    }

    private boolean IsPendingToday() {
        try {
            if (MainActivity.MyAttendances.Count() == 0)
                return true;
            else {
                boolean ismarked = MainActivity.MyAttendances.Any("ForDate", GetCurrentDateInString());
                return !ismarked;
            }

        } catch (Exception exception) {
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
            return false;
        }
    }

    private void AttachCapturePhoto() {

        this.SetOnClickListenerOnButton(R.id.btn_take_pic, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CapturePhoto();
            }
        });
    }

    private void AttachSubmitDDAT() {
        this.SetOnClickListenerOnButton(R.id.btnSubmitDAT,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (GpsDetector.isGPSon) {
                            if (gpsService.getLatitude()==0.0) {
                                Toast.makeText(AttandanceRegularize.this, "Check your Device Google Map Once or enable your Network", Toast.LENGTH_SHORT).show();
                            }
                            addMarkerOnMap(gpsService.getLatitude(), gpsService.getLongitude());
                            UpdateAttendance();
                        } else if (gpsService.isGPSEnable()) {
                            if (gpsService.getLatitude()==0.0) {
                                Toast.makeText(AttandanceRegularize.this, "Check your Device Google Map Once ", Toast.LENGTH_SHORT).show();
                            } else {
                                addMarkerOnMap(gpsService.getLatitude(), gpsService.getLongitude());
                                UpdateAttendance();
                            }
                        } else {
                            Toast.makeText(AttandanceRegularize.this, "Please Enable the GPS of your device", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String optionValue = "";
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

    private void AttachSpinnerEventHandler() {

        Spinner spinner = (Spinner) this.findViewById(R.id.spdat);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                optionValue = (String) arg0.getSelectedItem();
                hideCapturePhotoButton(optionValue);
                CurrentMDAT.option = getdatoptn(optionValue);
                if (optionValue.equalsIgnoreCase("Leave (L)")) {
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

    private void SetRestrictionOnDatePicker() {
        DatePicker dp = (DatePicker) this.findViewById(R.id.dpdatfor);
        dp.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public void CapturePhoto() {
        Intent I = new Intent(this, SimpleCameraActivity.class);
        I.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "Attendance");
        I.putExtra(SimpleCameraActivity.PARAMS_USERNAME, User.GetUserName());
        I.putExtra(SimpleCameraActivity.PARAMS_GUID, CurrentMDAT.guid);

        // For getting the result in the parent activity
        // this.context.startActivityForResult(I, 1);

        // for getting the result in the fragment
        startActivityForResult(I, 1);

    }

    private void UpdateAttendance() {
        try {
            if (CurrentMDAT.option == null)
                CurrentMDAT.option = getdatoptn(getSpinnerAsString(R.id.spdat));

            CurrentMDAT.ForDate = GetTextView(R.id.date).getText().toString();
            CurrentMDAT.Latitude = String.valueOf(gpsService.getLatitude());
            CurrentMDAT.Longitude = String.valueOf(gpsService.getLongitude());


            if (optionValue.equalsIgnoreCase("")) {
                ShowToast("Please select option first...");
            } else {
                if (CurrentMDAT.option.equalsIgnoreCase("Leave (L)")) {
                    GetLeaveDates();
                    if (Validate()) {
                        SaveForEachLeaveDay();
                        SubmitAttendanceOnServer();
                    } else
                        return;
                } else {
                    if (Validate()) {//btnSubmitDAT;
                        SubmitAttendanceOnServer();
                    } else
                        return;
                }
            }
        } catch (Exception exception) {
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
        }
    }

    private void SubmitAttendanceOnServer() {
        if (isNetworkAvailable()) {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


            BackgroundProcess bp = new BackgroundProcess(this).setProgressMessage("sending to server..");
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
            //new Dialog(this.context).setTitle("Message").show(Message.NO_INTERNET_FOUND);
            AlertDialog.Builder db = new AlertDialog.Builder(this);
            db.setTitle("settings");
            db.setMessage(Message.NO_INTERNET_FOUND);
            db.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SaveAttendanceInLocalDb("true");
                    OnUpdateComplete();
                }
            });
            db.show();
			/*SaveAttendanceInLocalDb("true");
			this.OnUpdateComplete();*/
        }
    }

    public static final String LOCATION_ERROR_MSG = "Dear ISP, Your current GPS location isn’t near your store location -which";
    public static final String COVID_QUESTIONNAIRE_ERROR_MSG = "Please submit Questionnaire before marking attendance";

    @SuppressWarnings("rawtypes")
    private void ProcessAttendanceResponse(final Response response) {
        try {
            if (response.isSuccess()) {
                this.ShowToast("Attendance updated successfully!");
                // SaveAttendanceInLocalDb("false");
                OnUpdateComplete();
            } else {
                if (response.errormsg != null) {
                    String errorMessageValueFromServer = response.errormsg;

                    if (errorMessageValueFromServer.contains(COVID_QUESTIONNAIRE_ERROR_MSG)) {
                        executeCovidDialogTask(errorMessageValueFromServer);
                    } else {
                        if (errorMessageValueFromServer.contains(LOCATION_ERROR_MSG)) {
                            executeLocationNotNearYesNoDialog(errorMessageValueFromServer);
                        } else {
                            executeNormalErrorMessageDialogWithOkButton(errorMessageValueFromServer);
                        }
                    }
                } else {
                    ShowToast("No error message found");
                }
            }
        } catch (Exception exception) {
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMark, " + exception.getStackTrace()[0]).show();
        }
    }

    private void executeCovidDialogTask(String errormsg) {
        final AlertDialog.Builder covidDialog = new AlertDialog.Builder(this);
        covidDialog.setMessage(errormsg);
        covidDialog.setTitle("Covid-19");
        covidDialog.setPositiveButton("Attempt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AttandanceRegularize.this, CoronaSurveyTabsActivity.class);
                startActivity(intent);
                if (AttandanceRegularize.this != null)
                    AttandanceRegularize.this.finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        covidDialog.show();
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
                OnUpdateComplete();
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

    private void OnUpdateComplete() {
        Intent intent1=new Intent(AttandanceRegularize.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    private void SaveForEachLeaveDay() {
        try {
            Calendar sDate = this.getDatePart(LStartDate);
            Calendar eDate = this.getDatePart(LEndDate);

            while (sDate.compareTo(eDate) <= 0) {

                CurrentMDAT.ForDate = this.ConvertDateToString(sDate.getTime());
                // SaveAttendanceInLocalDb("");
                sDate.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception exception) {
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
        }
    }

    private void GetLeaveDates() {
        try {
            CurrentMDAT.LStartDate = this.GetCurrentDateInString();
            CurrentMDAT.LEndDate = this.GetDateInString(R.id.dpedate);
            LEndDate = this.GetDateFDP(R.id.dpedate);
        } catch (Exception exception) {
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
        }
    }

    private void SaveAttendanceInLocalDb(String status) {

        try {
            CurrentMDAT.IsOfflineOnly = status;
            CurrentMDAT.InsertOrUpdate();
        } catch (Exception exception) {
            Log.e("Error:", exception.toString());
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
        }
    }

    private boolean Validate() {
        try {
            CurrentMDAT.CheckInTime=GetTextViewAsString(R.id.checkintime);
            CurrentMDAT.CheckOutTime=GetTextViewAsString(R.id.checkouttime);
            CurrentMDAT.Regularize="true";
            if(!CurrentMDAT.CheckOutTime.equalsIgnoreCase(null)&&!CurrentMDAT.CheckInTime.equalsIgnoreCase(""))
            {
                String time1 =CurrentMDAT.CheckInTime ;
                String time2 = CurrentMDAT.CheckOutTime;

                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Date date1 = format.parse(time1);
                Date date2 = format.parse(time2);
                long difference = date2.getTime() - date1.getTime();
                long diffHours = difference / (60 * 60 * 1000) % 24;
                if(diffHours<=0)
                {
                    ShowToast("Please select correct Time");
                    return false;
                }

            }
            else {
                ShowToast("Please Select Check-in or Check-out Time");
                return false;
            }

            if (optionValue.equalsIgnoreCase("")) {
                ShowToast("Please select option first");
                return false;
            }
            /*else if(CurrentMDAT.DocIDs.equalsIgnoreCase(""))
            {
                ShowToast("Please select image first!");
                return false;
            }*/
            else {
                if (CurrentMDAT.option.equalsIgnoreCase("Leave (L)")) {
                    if (LEndDate.before(LStartDate)) {
                        ShowToast("Please Enter Valid Leave Dates");
                        return false;
                    }
                }

                if (CurrentMDAT.option.equalsIgnoreCase("Select")) {
                    ShowToast("Please Select Option");
                    return false;
                }
            }


            if(ViewAttendance.datee.equalsIgnoreCase(""))
                if (!IsImageUploaed) {
                    ShowToast("Please upload the image first !");
                    return false;
                }
            return true;
        } catch (Exception exception) {
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMark, " + exception.getStackTrace()[0]).show();
            //ShowToast(exception.getMessage());
            return false;
        }

    }

    private void setDocsIDs(String docsIDs, boolean IsImageUploaded) {
        try {
            this.CurrentMDAT.DocIDs = docsIDs;
            this.IsImageUploaed = IsImageUploaded;
        } catch (Exception exception) {
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
        }


    }

    private void showImageToastAfterImageCapture(String result) {

        if (!result.equals(""))
            ShowToastLong(" image is attached", 0);
        else
            ShowToastLong("No image found to attach", 0);

    }


    private void initilizeMap() {
        try {

            MapFragment mapFragment = (MapFragment) AttandanceRegularize.this.getFragmentManager().findFragmentById(R.id.mapFragment);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);


            }
            // check if map is created successfully or not
        } catch (Exception exception) {
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
        }

    }

    private void addMarkerOnMap(double latitude, double longitude) {
        try {
            if (googleMap == null) {
                Toast.makeText(AttandanceRegularize.this, "Sorry! Unable To Show Location On Map", Toast.LENGTH_SHORT).show();
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
            new Dialog(AttandanceRegularize.this).setTitle("Exception").setMessage(exception.getMessage() + " in AttendanceMarkActivity, " + exception.getStackTrace()[0]).show();
        }
    }

    private void checkForAllPermissions() {


        requestPermissions(RuntimePermissionContainer.getPermissionsForCheckIn(), AppConstant.PERM_REQ_CODE);

    }

    private static final String TAG = "AttendanceMarko";

    private boolean isAllPermissionGranted = true;

    private HashMap<String, Boolean> temp = new HashMap<>();

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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
                        Uri uri = Uri.fromParts("package", AttandanceRegularize.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, AppConstant.PERM_REQ_CODE);
                    } else if (android.Manifest.permission.WRITE_CONTACTS.equals(permission)) {
                        checkForAllPermissions();
                        //showRationale(permission, R.string.permission_denied_contacts);
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                    } else if (android.Manifest.permission.READ_CONTACTS.equals(permission)) {
                        checkForAllPermissions();
                    } else if (android.Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
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


    //----------------------------- Corona Survey Task -----------------------------------------


    private AlertDialog coronaPopupbuilder;
    private Button buttonOkInCoronaSurveyDialog;
    private TextView textViewCoronaSurveyPopupMessage;

    private void setupForPopupOfCoronaSurvey(String message) {
        View view = getLayoutInflater().inflate(R.layout.custom_popup_dialog, null, false);
        coronaPopupbuilder = new AlertDialog.Builder(AttandanceRegularize.this).create();
        buttonOkInCoronaSurveyDialog = view.findViewById(R.id.btn_okInCoronaSurveyPopupOnMainScreen);
        textViewCoronaSurveyPopupMessage = view.findViewById(R.id.tv_msgForCoronaSurvey);
        textViewCoronaSurveyPopupMessage.setText(message);
        coronaPopupbuilder.setCanceledOnTouchOutside(false);
        buttonOkInCoronaSurveyDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttandanceRegularize.this, CoronaSurveyTabsActivity.class);
                startActivity(intent);
                coronaPopupbuilder.dismiss();
            }
        });
        coronaPopupbuilder.setView(view);
        coronaPopupbuilder.show();
    }

    private void showCoronaSurveyDialog() {
        coronaPopupbuilder.show();
    }

    private void hideCoronaSurveyDialog() {
        if (coronaPopupbuilder != null && coronaPopupbuilder.isShowing())
            coronaPopupbuilder.dismiss();
    }


    private void getMessageForCoronaSurveyFromWeb() {
        if (isNetworkAvailable()) {
            BackgroundProcess backgroundProcess = new BackgroundProcess(this);
            backgroundProcess.setProgressMessage("Getting Data...");
            backgroundProcess.setbackgroundProcess(new IProcess() {
                @Override
                public Object underProcess() throws Exception {
                    return server.getPopupForCoronaSurveyOnMainScreen();
                }

                @Override
                public void processResponse(Object response) throws Exception {
                    Response messageResponseFromServer = (Response) response;
                    if (messageResponseFromServer.isSuccess() && messageResponseFromServer.data != null) {
                        if ((StringModel) messageResponseFromServer.data.get(0) != null) {
                            StringModel arrDocModel = (StringModel) messageResponseFromServer.data.get(0);
                            setupForPopupOfCoronaSurvey(arrDocModel.message);
                            Log.d(TAG, "processResponse: " + arrDocModel);
                        }
                    } else {
                        hideCoronaSurveyDialog();
                    }
                }
            });
            backgroundProcess.execute();
        } else {
            ShowToast("No internet...");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
		/*if (coronaPopupbuilder != null && !coronaPopupbuilder.isShowing())
			getMessageForCoronaSurveyFromWeb();*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        this.googleMap=googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        LatLng sydney = new LatLng(gpsService.getLatitude(), gpsService.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15F));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null);
    }

    @Override
    public void RegisterTableInfoForLocalDB() {

    }
}

