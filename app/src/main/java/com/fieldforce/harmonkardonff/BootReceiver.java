package com.fieldforce.harmonkardonff;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "Device rebooted, checking for geofence restore");
            restoreGeofenceIfNeeded(context);
        }
    }

    private void restoreGeofenceIfNeeded(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE);
            String startDate = prefs.getString("start_date", "");
            String loginId = prefs.getString("login_id", "");
            String storeId = prefs.getString("store_id", "");
            double lat = Double.parseDouble(prefs.getString("lat", "0"));
            double lng = Double.parseDouble(prefs.getString("lng", "0"));

            String currentDate = getCurrentDate();

            if (!startDate.isEmpty() && startDate.equals(currentDate) && lat != 0 && lng != 0) {
                Log.d(TAG, "Restoring geofence after reboot: " + loginId + ", " + storeId);
                
                // Check GPS status
                android.location.LocationManager locationManager = 
                    (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                boolean isGpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
                
                if (!isGpsEnabled) {
                    Log.w(TAG, "GPS is OFF after reboot. User needs to enable it manually.");
                    // Note: Can't automatically enable GPS from BroadcastReceiver
                    // User will see GPS alert when they open the app
                } else {
                    Log.d(TAG, "GPS is ON after reboot");
                }
                
                setupGeofence(context, lat, lng);
            } else {
                Log.d(TAG, "No valid geofence to restore after reboot");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error restoring geofence after reboot: " + e.getMessage());
        }
    }

    @SuppressLint("MissingPermission")
    private void setupGeofence(Context context, double latitude, double longitude) {
        try {
            Geofence geofence = new Geofence.Builder()
                    .setRequestId("MARK_IN_FENCE")
                    .setCircularRegion(latitude, longitude, 200)
                    .setExpirationDuration(getMillisecondsUntilMidnight())
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build();

            GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT)
                    .addGeofence(geofence)
                    .build();

            Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                            ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
                            : PendingIntent.FLAG_UPDATE_CURRENT
            );

            GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Geofence restored successfully after reboot"))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to restore geofence after reboot: " + e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, "Error in setupGeofence after reboot: " + e.getMessage());
        }
    }

    private long getMillisecondsUntilMidnight() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
        calendar.set(java.util.Calendar.MINUTE, 59);
        calendar.set(java.util.Calendar.SECOND, 59);
        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
}
