package app.core.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;
//import android.support.v4.content.WakefulBroadcastReceiver;


public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	public GcmBroadcastReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context ctx, Intent intent) {
		
		// Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(ctx.getPackageName(),
                GcmIntentService.class.getName());
        
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(ctx, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

	}
}
