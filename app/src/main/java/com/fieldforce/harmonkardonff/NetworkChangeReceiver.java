// OFFLINE QUEUE DISABLED - NetworkChangeReceiver not needed anymore
/*
package com.fieldforce.harmonkardonff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import org.json.JSONObject;
import java.util.List;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkChange";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
            Log.d(TAG, "Internet connected! Syncing queue...");
            syncQueue(context);
        }
    }
    
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    
    private void syncQueue(Context context) {
        List<JSONObject> queue = OfflineQueueManager.getQueue(context);
        if (queue.isEmpty()) {
            Log.d(TAG, "Queue is empty, nothing to sync");
            return;
        }
        
        Log.d(TAG, "Syncing " + queue.size() + " items from queue");
        
        for (JSONObject item : queue) {
            try {
                String loginId = item.getString("LoginId");
                String storeId = item.getString("StoreId");
                double lat = item.getDouble("Lat");
                double lng = item.getDouble("Long");
                String timestamp = item.getString("timestap");
                double distance = item.getDouble("distance");
                String transitionType = item.getString("transitionType");
                
                GeofenceBroadcastReceiver.hitGeoLocationApi(context, loginId, storeId, 
                    lat, lng, timestamp, distance, transitionType, true);
                
                Log.d(TAG, "Synced: " + transitionType + " at " + timestamp);
            } catch (Exception e) {
                Log.e(TAG, "Error syncing item", e);
            }
        }
        
        OfflineQueueManager.clearQueue(context);
        Log.d(TAG, "Queue sync completed");
    }
}
*/
