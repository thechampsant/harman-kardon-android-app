// OFFLINE QUEUE DISABLED - OfflineQueueManager not needed anymore
/*
package com.fieldforce.harmonkardonff;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class OfflineQueueManager {
    private static final String TAG = "OfflineQueue";
    private static final String PREFS_NAME = "GeofenceQueue";
    private static final String QUEUE_KEY = "pending_requests";
    
    public static void addToQueue(Context context, String loginId, String storeId, 
                                   double lat, double lng, String timestamp, 
                                   double distance, String transitionType) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String queueJson = prefs.getString(QUEUE_KEY, "[]");
            JSONArray queue = new JSONArray(queueJson);
            
            JSONObject item = new JSONObject();
            item.put("LoginId", loginId);
            item.put("StoreId", storeId);
            item.put("Lat", lat);
            item.put("Long", lng);
            item.put("timestap", timestamp);
            item.put("distance", distance);
            item.put("transitionType", transitionType);
            item.put("queued_at", System.currentTimeMillis());
            
            queue.put(item);
            prefs.edit().putString(QUEUE_KEY, queue.toString()).apply();
            Log.d(TAG, "Added to queue. Total items: " + queue.length());
        } catch (JSONException e) {
            Log.e(TAG, "Error adding to queue", e);
        }
    }
    
    public static List<JSONObject> getQueue(Context context) {
        List<JSONObject> items = new ArrayList<>();
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String queueJson = prefs.getString(QUEUE_KEY, "[]");
            JSONArray queue = new JSONArray(queueJson);
            
            for (int i = 0; i < queue.length(); i++) {
                items.add(queue.getJSONObject(i));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error reading queue", e);
        }
        return items;
    }
    
    public static void clearQueue(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(QUEUE_KEY, "[]").apply();
        Log.d(TAG, "Queue cleared");
    }
    
    public static int getQueueSize(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String queueJson = prefs.getString(QUEUE_KEY, "[]");
            return new JSONArray(queueJson).length();
        } catch (JSONException e) {
            return 0;
        }
    }
}
*/
