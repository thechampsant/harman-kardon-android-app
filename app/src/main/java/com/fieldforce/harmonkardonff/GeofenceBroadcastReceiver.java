package com.fieldforce.harmonkardonff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceReceiver";
    private static final long MIN_TIME_BETWEEN_EVENTS = 60000; // 1 minute in milliseconds
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "========== GEOFENCE EVENT TRIGGERED ==========");
        Log.d(TAG, "onReceive: BroadcastReceiver triggered at " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.e(TAG, "onReceive: Geofence error code = " + geofencingEvent.getErrorCode());
            showNotification(context, "Geofence Error", "Error code: " + geofencingEvent.getErrorCode());
            return;
        }

        // Check if geofence is still valid for today
        SharedPreferences prefs = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE);
        String startDate = prefs.getString("start_date", "");
        String loginId = prefs.getString("login_id", "");
        
        // If SharedPreferences is empty, user has logged out - ignore event
        if (startDate.isEmpty() || loginId.isEmpty()) {
            Log.d(TAG, "onReceive: No geofence data found - user may have logged out, ignoring event");
            return;
        }
        
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        
        Log.d(TAG, "onReceive: Start date = " + startDate + ", Current date = " + currentDate);
        
        if (!startDate.equals(currentDate)) {
            Log.d(TAG, "onReceive: Geofence expired - started on " + startDate + ", current date is " + currentDate);
            Log.d(TAG, "onReceive: Clearing geofence data");
            prefs.edit().clear().apply();
            return;
        }

        int transition = geofencingEvent.getGeofenceTransition();
        Log.d(TAG, "onReceive: Transition type = " + transition);

        if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d(TAG, "onReceive: *** EXIT DETECTED *** - User left 200m boundary");
            showNotification(context, "Geofence EXIT", "You left the 200m area!");
            processGeofenceEvent(context, "EXIT");
        } else if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d(TAG, "onReceive: *** ENTER DETECTED *** - User returned to 200m boundary");
            showNotification(context, "Geofence ENTER", "You returned to the area!");
            processGeofenceEvent(context, "ENTER");
        } else {
            Log.d(TAG, "onReceive: Unknown transition, ignoring. transition = " + transition);
        }
        
        Log.d(TAG, "========== GEOFENCE EVENT END ==========");
    }
    
    private void showNotification(Context context, String title, String message) {
        try {
            android.app.NotificationManager notificationManager = 
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            
            String channelId = "geofence_channel";
            
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                android.app.NotificationChannel channel = new android.app.NotificationChannel(
                    channelId, "Geofence Notifications", android.app.NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            
            android.app.Notification notification = new android.app.Notification.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setAutoCancel(true)
                .build();
            
            notificationManager.notify((int) System.currentTimeMillis(), notification);
            Log.d(TAG, "showNotification: Notification shown - " + title + ": " + message);
        } catch (Exception e) {
            Log.e(TAG, "showNotification: Error showing notification - " + e.getMessage());
        }
    }

    private void processGeofenceEvent(Context context, String transitionType) {
        SharedPreferences prefs = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE);
        
        // Check if this is the first event ever for this geofence session
        boolean hasAnyEventOccurred = prefs.getBoolean("has_any_event_occurred", false);
        
        // CRITICAL RULE: First event MUST be EXIT, never ENTER
        if (!hasAnyEventOccurred && transitionType.equals("ENTER")) {
            Log.d(TAG, "processGeofenceEvent: BLOCKING ENTER - First event must be EXIT, not ENTER!");
            return;
        }
        
        // Check if geofence was just created (within last 2 minutes)
        long geofenceStartTime = prefs.getLong("geofence_start_time", 0);
        long currentTime = System.currentTimeMillis();
        long timeSinceStart = currentTime - geofenceStartTime;
        
        // Skip events within first 2 minutes of geofence creation (grace period)
        if (geofenceStartTime > 0 && timeSinceStart < 120000) { // 2 minutes
            Log.d(TAG, "processGeofenceEvent: Skipping " + transitionType + " - geofence just started (grace period " + (timeSinceStart/1000) + " seconds ago)");
            return;
        }
        
        // Check if enough time has passed since last event
        long lastEventTime = prefs.getLong("last_event_time_" + transitionType, 0);
        
        if (currentTime - lastEventTime < MIN_TIME_BETWEEN_EVENTS) {
            long secondsRemaining = (MIN_TIME_BETWEEN_EVENTS - (currentTime - lastEventTime)) / 1000;
            Log.d(TAG, "processGeofenceEvent: Skipping " + transitionType + " - too soon (wait " + secondsRemaining + " seconds)");
            return;
        }
        
        // Mark that an event has occurred
        prefs.edit().putBoolean("has_any_event_occurred", true).apply();
        
        // Update last event time
        prefs.edit().putLong("last_event_time_" + transitionType, currentTime).apply();
        Log.d(TAG, "processGeofenceEvent: Processing " + transitionType + " event");
        
        // Always call API directly (offline queue disabled)
        Log.d(TAG, "processGeofenceEvent: Calling API directly");
        hitGeoLocationApi(context, transitionType, false);
        
        // OFFLINE QUEUE DISABLED - Never use offline queue
        // if (isNetworkAvailable(context)) {
        //     Log.d(TAG, "processGeofenceEvent: Calling API directly");
        //     hitGeoLocationApi(context, transitionType, false);
        // } else {
        //     Log.d(TAG, "processGeofenceEvent: No network, adding to queue");
        //     addToOfflineQueue(context, transitionType);
        // }
    }
    
    // OFFLINE QUEUE DISABLED - Network check not needed
    // private boolean isNetworkAvailable(Context context) {
    //     ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    //     NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    //     return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    // }
    
    // OFFLINE QUEUE DISABLED - Never add to queue
    /*
    private void addToOfflineQueue(Context context, String transitionType) {
        SharedPreferences prefs = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE);
        String loginId = prefs.getString("login_id", "");
        String storeId = prefs.getString("store_id", "");
        String centerLat = prefs.getString("lat", "");
        String centerLng = prefs.getString("lng", "");
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        
        double distance = 200.0;
        double currentLat = Double.parseDouble(centerLat);
        double currentLng = Double.parseDouble(centerLng);
        
        try {
            android.location.LocationManager locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            android.location.Location gpsLocation = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
            android.location.Location networkLocation = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
            
            android.location.Location currentLocation = null;
            if (gpsLocation != null && networkLocation != null) {
                currentLocation = gpsLocation.getTime() > networkLocation.getTime() ? gpsLocation : networkLocation;
            } else if (gpsLocation != null) {
                currentLocation = gpsLocation;
            } else if (networkLocation != null) {
                currentLocation = networkLocation;
            }
            
            if (currentLocation != null && !centerLat.isEmpty() && !centerLng.isEmpty()) {
                currentLat = currentLocation.getLatitude();
                currentLng = currentLocation.getLongitude();
                
                android.location.Location centerLocation = new android.location.Location("");
                centerLocation.setLatitude(Double.parseDouble(centerLat));
                centerLocation.setLongitude(Double.parseDouble(centerLng));
                
                float distanceFromCenter = currentLocation.distanceTo(centerLocation);
                distance = distanceFromCenter;
                
                Log.d(TAG, "addToOfflineQueue: Current GPS = (" + currentLat + ", " + currentLng + ")");
                Log.d(TAG, "addToOfflineQueue: Center Point = (" + centerLat + ", " + centerLng + ")");
                Log.d(TAG, "addToOfflineQueue: Distance from center = " + distance + " meters (" + String.format("%.2f", distance/1000) + " km)");
            }
        } catch (Exception e) {
            Log.e(TAG, "addToOfflineQueue: Error calculating distance - " + e.getMessage());
        }
        
        OfflineQueueManager.addToQueue(context, loginId, storeId, currentLat, currentLng, timestamp, distance, transitionType);
        Log.d(TAG, "addToOfflineQueue: Added to queue. Queue size: " + OfflineQueueManager.getQueueSize(context));
    }
    */
    
    public static void hitGeoLocationApi(Context context, String transitionType, boolean isFromSync) {
        // ========== CRITICAL LOGOUT CHECK - MUST BE FIRST THING ==========
        SharedPreferences prefs = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE);
        String loginId = prefs.getString("login_id", "");
        String storeId = prefs.getString("store_id", "");
        String centerLat = prefs.getString("lat", "");
        String centerLng = prefs.getString("lng", "");
        
        // BLOCK API CALL IF USER HAS LOGGED OUT
        if (loginId.isEmpty() || storeId.isEmpty() || centerLat.isEmpty() || centerLng.isEmpty()) {
            Log.d(TAG, "hitGeoLocationApi: ❌ BLOCKING API CALL - User has logged out! (SharedPreferences is empty)");
            return;
        }
        Log.d(TAG, "hitGeoLocationApi: ✓ Logout check PASSED - User is still logged in");
        // ================================================================
        
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        long currentTime = System.currentTimeMillis();
        
        // Calculate actual distance from center point to current location
        String distance = "200"; // Default
        String currentLat = centerLat;
        String currentLng = centerLng;
        float distanceMeters = 200f;
        float gpsAccuracy = 999f; // Default high value
        
        try {
            android.location.LocationManager locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            
            // Try to get fresh location from both GPS and Network providers
            android.location.Location gpsLocation = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
            android.location.Location networkLocation = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
            
            // Use the most recent location
            android.location.Location currentLocation = null;
            if (gpsLocation != null && networkLocation != null) {
                currentLocation = gpsLocation.getTime() > networkLocation.getTime() ? gpsLocation : networkLocation;
            } else if (gpsLocation != null) {
                currentLocation = gpsLocation;
            } else if (networkLocation != null) {
                currentLocation = networkLocation;
            }
            
            if (currentLocation != null && !centerLat.isEmpty() && !centerLng.isEmpty()) {
                currentLat = String.valueOf(currentLocation.getLatitude());
                currentLng = String.valueOf(currentLocation.getLongitude());
                
                // Get GPS accuracy
                if (currentLocation.hasAccuracy()) {
                    gpsAccuracy = currentLocation.getAccuracy();
                    Log.d(TAG, "hitGeoLocationApi: GPS Accuracy = " + gpsAccuracy + " meters");
                }
                
                android.location.Location centerLocation = new android.location.Location("");
                centerLocation.setLatitude(Double.parseDouble(centerLat));
                centerLocation.setLongitude(Double.parseDouble(centerLng));
                
                float distanceFromCenter = currentLocation.distanceTo(centerLocation);
                distanceMeters = distanceFromCenter;
                distance = String.valueOf(distanceFromCenter);
                
                Log.d(TAG, "hitGeoLocationApi: Current GPS = (" + currentLat + ", " + currentLng + ")");
                Log.d(TAG, "hitGeoLocationApi: Center Point = (" + centerLat + ", " + centerLng + ")");
                Log.d(TAG, "hitGeoLocationApi: Distance from center = " + distance + " meters (" + String.format("%.2f", Double.parseDouble(distance)/1000) + " km)");
            } else {
                Log.w(TAG, "hitGeoLocationApi: No location available, using default distance");
            }
        } catch (Exception e) {
            Log.e(TAG, "hitGeoLocationApi: Error calculating distance - " + e.getMessage());
        }
        
        // ========== LAYER 1: GPS ACCURACY CHECK ==========
        if (gpsAccuracy > 100) {
            Log.d(TAG, "hitGeoLocationApi: ❌ LAYER 1 BLOCKED - Poor GPS accuracy (" + gpsAccuracy + "m > 100m threshold)");
            return;
        }
        Log.d(TAG, "hitGeoLocationApi: ✓ LAYER 1 PASSED - GPS accuracy acceptable (" + gpsAccuracy + "m)");
        
        // ========== LAYER 2: UNREALISTIC DISTANCE JUMP CHECK ==========
        // Block if distance > 1km and last API was < 2 minutes ago
        if (distanceMeters > 1000) {
            long lastApiCallTime = prefs.getLong("last_any_api_call_time", 0);
            long timeSinceLastApi = currentTime - lastApiCallTime;
            
            if (lastApiCallTime > 0 && timeSinceLastApi < 120000) { // 2 minutes
                Log.d(TAG, "hitGeoLocationApi: ❌ LAYER 2 BLOCKED - Unrealistic jump! Distance=" + distanceMeters + "m in " + (timeSinceLastApi/1000) + " seconds");
                return;
            }
        }
        Log.d(TAG, "hitGeoLocationApi: ✓ LAYER 2 PASSED - Realistic movement pattern");
        
        // ========== LAYER 3: DUPLICATE TIMESTAMP CHECK ==========
        // Block if exact same timestamp as last API call (prevents same-second duplicates)
        String lastApiTimestamp = prefs.getString("last_api_timestamp", "");
        if (!lastApiTimestamp.isEmpty() && lastApiTimestamp.equals(timestamp)) {
            Log.d(TAG, "hitGeoLocationApi: ❌ LAYER 3 BLOCKED - Duplicate timestamp! Same second API already sent (" + timestamp + ")");
            return;
        }
        Log.d(TAG, "hitGeoLocationApi: ✓ LAYER 3 PASSED - Unique timestamp");
        
        // Update last API call tracking
        prefs.edit()
            .putLong("last_any_api_call_time", currentTime)
            .putString("last_api_timestamp", timestamp)
            .apply();
        
        Log.d(TAG, "hitGeoLocationApi: ✅ ALL 3 LAYERS PASSED - Proceeding with API call");
        
        // Get app version
        String appVersion = "";
        try {
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "Unknown";
        }

        Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "transitionType=" + transitionType + ", loginId=" + loginId + ", storeId=" + storeId
                + ", currentLat=" + currentLat + ", currentLng=" + currentLng + ", distance=" + distance + ", timestamp=" + timestamp + ", appVersion=" + appVersion);

        String url = "http://harman.infield.co.in/ISPMobile/SaveISPGeoLocation?"
                + "LoginId=" + loginId
                + "&StoreId=" + storeId
                + "&Lat=" + currentLat
                + "&Long=" + currentLng
                + "&timestap=" + timestamp
                + "&distance=" + distance
                + "&status=" + transitionType
                + "&AppVersion=" + appVersion
                + "&Source=Android";

        Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "Final URL = " + url);

        // FINAL LOGOUT CHECK BEFORE STARTING THREAD
        final String finalLoginId = loginId;
        final String finalStoreId = storeId;
        
        new Thread(() -> {
            // DOUBLE CHECK inside thread before making API call
            SharedPreferences threadPrefs = context.getSharedPreferences("GeofencePrefs", Context.MODE_PRIVATE);
            String threadLoginId = threadPrefs.getString("login_id", "");
            
            if (threadLoginId.isEmpty() || !threadLoginId.equals(finalLoginId)) {
                Log.d(TAG, "hitGeoLocationApi: ❌ BLOCKING API CALL in thread - User logged out during execution!");
                return;
            }
            
            try {
                Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "Starting API call on background thread");
                java.net.URL apiUrl = new java.net.URL(url);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) apiUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                int responseCode = conn.getResponseCode();
                Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "API response code = " + responseCode);
                
                // Read response
                if (responseCode == 200) {
                    java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "Response = " + response.toString());
                }
                
                conn.disconnect();
                Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "API call completed successfully");
            } catch (Exception e) {
                Log.e(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "API call failed - " + e.getMessage());
            }
        }).start();
    }
    
    public static void hitGeoLocationApi(Context context, String loginId, String storeId,
                                         double lat, double lng, String timestamp,
                                         double distance, String transitionType, boolean isFromSync) {
        String appVersion = "";
        try {
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "Unknown";
        }

        Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "transitionType=" + transitionType + ", loginId=" + loginId + ", storeId=" + storeId
                + ", lat=" + lat + ", lng=" + lng + ", distance=" + distance + ", timestamp=" + timestamp + ", appVersion=" + appVersion);

        String url = "http://harman.infield.co.in/ISPMobile/SaveISPGeoLocation?"
                + "LoginId=" + loginId
                + "&StoreId=" + storeId
                + "&Lat=" + lat
                + "&Long=" + lng
                + "&timestap=" + timestamp
                + "&distance=" + distance
                + "&status=" + transitionType
                + "&AppVersion=" + appVersion
                + "&Source=Android";

        Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "Final URL = " + url);

        new Thread(() -> {
            try {
                Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "Starting API call on background thread");
                java.net.URL apiUrl = new java.net.URL(url);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) apiUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                int responseCode = conn.getResponseCode();
                Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "API response code = " + responseCode);
                
                if (responseCode == 200) {
                    java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "Response = " + response.toString());
                }
                
                conn.disconnect();
                Log.d(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "API call completed successfully");
            } catch (Exception e) {
                Log.e(TAG, "hitGeoLocationApi: " + (isFromSync ? "[SYNC] " : "") + "API call failed - " + e.getMessage());
            }
        }).start();
    }
}
