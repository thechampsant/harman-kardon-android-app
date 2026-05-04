package com.fieldforce.harmonkardonff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: BroadcastReceiver triggered");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.e(TAG, "onReceive: Geofence error code = " + geofencingEvent.getErrorCode());
            return;
        }

        int transition = geofencingEvent.getGeofenceTransition();
        Log.d(TAG, "onReceive: Transition type = " + transition);

        if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d(TAG, "onReceive: EXIT transition detected - user left geofence boundary");
            hitGeoLocationApi(context);
        } else {
            Log.d(TAG, "onReceive: Transition is not EXIT, ignoring. transition = " + transition);
        }
    }

    private void hitGeoLocationApi(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("geofence_prefs", Context.MODE_PRIVATE);
        String loginId = prefs.getString("login_id", "");
        String storeId = prefs.getString("store_id", "");
        String lat = prefs.getString("lat", "");
        String lng = prefs.getString("lng", "");
        String distance = prefs.getString("distance", "200");
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Log.d(TAG, "hitGeoLocationApi: loginId=" + loginId + ", storeId=" + storeId
                + ", lat=" + lat + ", lng=" + lng + ", distance=" + distance + ", timestamp=" + timestamp);

        String url = "http://harman.infield.co.in/ISPMobile/SaveISPGeoLocation?"
                + "LoginId=" + loginId
                + "&StoreId=" + storeId
                + "&Lat=" + lat
                + "&Long=" + lng
                + "&timestap=" + timestamp
                + "&distance=" + distance;

        Log.d(TAG, "hitGeoLocationApi: Final URL = " + url);

        new Thread(() -> {
            try {
                Log.d(TAG, "hitGeoLocationApi: Starting API call on background thread");
                java.net.URL apiUrl = new java.net.URL(url);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) apiUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                int responseCode = conn.getResponseCode();
                Log.d(TAG, "hitGeoLocationApi: API response code = " + responseCode);
                conn.disconnect();
                Log.d(TAG, "hitGeoLocationApi: API call completed successfully");
            } catch (Exception e) {
                Log.e(TAG, "hitGeoLocationApi: API call failed - " + e.getMessage());
            }
        }).start();
    }
}
