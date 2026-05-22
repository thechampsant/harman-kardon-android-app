package com.fieldforce.harmonkardonff;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fieldforce.utility.Helper;
import com.fieldforce.utility.widgets.RobotoTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.geoservice.GpsResultCallBack;
import app.core.model.Response;
import app.core.utils.Dialog;
import app.core.utils.Message;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.CheckoutModel;
import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.services.WebService;

import static com.fieldforce.harmonkardonff.PermissionsUtil.isMarshMellowAndAbove;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class CheckoutActivity extends InnosolsActivity implements OnMapReadyCallback {
    CheckoutModel check_out = new CheckoutModel();
    Boolean Isimageuploaded = false;
    WebService web = new WebService();
    ProgressBar refreshGpsProgressBar;
    MarkerOptions marker = new MarkerOptions();
    String stringLatitude;
    String stringLongitude;
    private EditText edt_remarks;
    private RobotoTextView text_submit;
    private RobotoTextView attach_image;
    private String remrks;
    private double userLat = 0.0;
    private double userLong = 0.0;
    // Google Map
    private GoogleMap googleMap;
    private MapView mapView;
    private static final String TAG = "CheckoutActivity";
    GpsResultCallBack gpsLocationcallBack = new GpsResultCallBack() {

        @Override
        public void onLocationChange(double longitude, double latitude) {

            if (refreshGpsProgressBar.getVisibility() == View.VISIBLE)
                refreshGpsProgressBar.setVisibility(View.INVISIBLE);

            addMarkerOnMap(latitude, longitude);

            userLat = latitude;
            userLong = longitude;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mapView = findViewById(R.id.checkout_map_fragment);
        if (isMarshMellowAndAbove()) {
            requestForAllPermissions();
        } else
            proceedAfterPermission();


        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

    }

    final int PERMISSION_CALLBACK_CONSTANT = 1;
    String[] permissionsRequired = new String[]
            {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA

            };

    private void requestForAllPermissions() {
        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
        } else {
            proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {
        initialize();
        SetTextViewAsString(R.id.checkout_current_user_textview, "Current User : " + WebService.UserName);
        refreshGpsProgressBar = (ProgressBar) findViewById(R.id.check_out_progress_bar);
    }

    private void initialize() {
        // TODO Auto-generated method stub

        text_submit = (RobotoTextView) findViewById(R.id.text_submt);
        attach_image = (RobotoTextView) findViewById(R.id.bt_attach_image);
        Helper.setDrawableTextView(attach_image, R.drawable.ic_photo_camera_black_24dp, 0);
        InvokeGPSService();

        locationHelper.setCallBack(gpsLocationcallBack);

        if (!locationHelper.isGpsEnabled()) {
            locationHelper.showSettingsAlert();
        }

        findViewById(R.id.checkout_refresh_location).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                refreshGpsProgressBar.setVisibility(View.VISIBLE);

            }
        });
        imageattachlistener();
        submitcheckoutlistener();


    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        try {
            locationHelper.stopLocationUpdates();
        } catch (Exception e) {
        }

    }
    private void initilizeMap() {
        try {
            if (googleMap == null) {
                ((MapFragment) getFragmentManager().findFragmentById(R.id.checkout_map_fragment)).getMapAsync(this);

                // check if map is created successfully or not
                if (googleMap == null) {
                    Toast.makeText(this,
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } catch (Exception exception) {
            new Dialog(this).setTitle("Exception").setMessage(exception.getMessage() + " in " + TAG + ", " + exception.getStackTrace()[0]).show();
        }
    }
    /**
     * function to load map. If map is not created it will create it for you
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap = googleMap;
        initilizeMap();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    private void addMarkerOnMap(double latitude, double longitude) {
        try {
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

            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(17).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception exception) {
            new Dialog(this).setTitle("Exception").setMessage(exception.getMessage() + " in " + TAG + ", " + exception.getStackTrace()[0]).show();
        }

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
            String DocID = data.getExtras().getString("DocID") + "";
            showImageToastAfterImageCapture(IMAGE_CAPTURED);
            if (!IMAGE_CAPTURED.equals("")) {
                setDocsIDs(DocID, true);
            }
        }
    }

    private void imageattachlistener() {

        attach_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                capturephoto();
            }
        });
    }

    private void submitcheckoutlistener() {


        text_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    addvaluetomodel();
                /*    String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                    check_out.CheckOutTime=currentTime;*/
                    submitremarksonserver();

                }
            }
        });
    }

    private void addvaluetomodel() {
        // TODO Auto-generated method stub
        check_out.Remarks = remrks;
        check_out.ForDate = GetCurrentDateInString();
        check_out.Latitude = stringLatitude;
        check_out.Longitude = stringLongitude;
        // check_out.istodayattndavl=checkattendance();

    }

    private String checkattendance() {
        if (!istodayattndavailable()) {
            return "false";

        }
        return "true";

    }

    private void capturephoto() {
        // TODO Auto-generated method stub
        Intent I = new Intent(this, SimpleCameraActivity.class);
        I.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "CheckOut");
        I.putExtra(SimpleCameraActivity.PARAMS_USERNAME, User.GetUserName());
        I.putExtra(SimpleCameraActivity.PARAMS_GUID, check_out.guid);
        I.putExtra(SimpleCameraActivity.PARAMS_MODEL_ID, "");
        I.putExtra(SimpleCameraActivity.ACTION_ENABLE_GPS, true);
        I.putExtra(SimpleCameraActivity.ACTION_ENABLE_GPS, true);
        // For getting the result in the parent activity
        // this.context.startActivityForResult(I, 1);

        // for getting the result in the fragment
        startActivityForResult(I, 1);

    }

    private void setDocsIDs(String docsIDs, boolean IsImageUploaded) {
        // TODO Auto-generated method stub
        this.check_out.DocIDs = docsIDs;
        this.Isimageuploaded = IsImageUploaded;
    }

    private void showImageToastAfterImageCapture(String result) {

        if (!result.equals(""))
            ShowToastLong(" image is attached", 0);
        else
            ShowToastLong("No image found to attach", 0);


    }


    public void submitremarksonserver() {
        if (isNetworkAvailable()) {
            BackgroundProcess bp = new BackgroundProcess(this)
                    .setProgressMessage("sending to server..");
            bp.setbackgroundProcess(new IProcess() {

                @SuppressWarnings("rawtypes")
                @Override
                public void processResponse(Object arg0) throws Exception {
                    processcheckoutresponse((Response) arg0);
                }

                @Override
                public Object underProcess() throws Exception {

                    return web.Trycheckout(check_out);
                }
            });

            bp.execute(null, null, null);
        } else
            new Dialog(this).setTitle("Message")
                    .show(Message.NO_INTERNET_FOUND);

    }

    @SuppressWarnings("rawtypes")
    private void processcheckoutresponse(Response response) {
        if (response.status.equalsIgnoreCase("true")) {
            this.ShowToast("Checkout marked successfully!");
            check_out.InsertOrUpdate();
            stopGeofencing();
            finish();
        } else {
            this.ShowToast(response.errormsg);

        }

    }

    private void stopGeofencing() {
        try {
            android.util.Log.d(TAG, "stopGeofencing: Stopping geofence monitoring");
            com.google.android.gms.location.GeofencingClient geofencingClient = 
                com.google.android.gms.location.LocationServices.getGeofencingClient(this);
            
            java.util.List<String> geofenceIds = new java.util.ArrayList<>();
            geofenceIds.add("MARK_IN_FENCE");
            
            geofencingClient.removeGeofences(geofenceIds)
                .addOnSuccessListener(aVoid -> {
                    android.util.Log.d(TAG, "stopGeofencing: Geofence removed successfully");
                    // Clear shared preferences
                    getSharedPreferences("geofence_prefs", MODE_PRIVATE).edit().clear().apply();
                })
                .addOnFailureListener(e -> 
                    android.util.Log.e(TAG, "stopGeofencing: Failed to remove geofence - " + e.getMessage())
                );
        } catch (Exception e) {
            android.util.Log.e(TAG, "stopGeofencing: Error - " + e.getMessage());
        }
    }



    private boolean getLatituteLogitute() {
        if (false/*!gpsTracker.isGPSEnable()*/) {
			/*gpsTracker.showSettingsAlert();
			return false;*/
            return false;
        } else {
            stringLatitude = String.valueOf(userLat);
            stringLongitude = String.valueOf(userLong);
            if (stringLatitude == null || stringLongitude == null
                    || stringLatitude.equalsIgnoreCase("0.0")
                    || stringLongitude.equalsIgnoreCase("0.0")) {
                this.ShowToast("Coordinates Not Available ,Turn on Gps if not Turned on");
                return false;

            }
            return true;
        }
    }

    private boolean validate() {
        // TODO Auto-generated method stub
        /*if (!remarks()) {
            return false;
        } else*/
        if (!Isimageuploaded) {
            this.ShowToast("Upload Image First");
            return false;
        } else if (!getLatituteLogitute()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean istodayattndavailable() {
        ArrayList<MDAT> mdat = new ArrayList<MDAT>();
        mdat = MainActivity.MyAttendances.where("ForDate",
                GetCurrentDateInString());
        if (mdat != null && mdat.size() != 0) {
            return true;
        }

        return false;
    }

    private boolean remarks() {
        // TODO Auto-generated method stub
        edt_remarks = (EditText) findViewById(R.id.edt_remarks);
        remrks = edt_remarks.getText().toString();
        if (remrks.isEmpty()) {
            edt_remarks.setError("Enter Remarks");
            return false;
        }
        return true;
    }

    @Override
    public void RegisterTableInfoForLocalDB() {
        // TODO Auto-generated method stub

    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CALLBACK_CONSTANT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    proceedAfterPermission();

                } else {
                    createAlertDialogInMarkIn();
                }
                break;
            default:
                ShowToast("Request Code not found");

        }
    }

    public void createAlertDialogInMarkIn() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        requestForAllPermissions();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("All Permission are Mendatory, Do you want to allow?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}
