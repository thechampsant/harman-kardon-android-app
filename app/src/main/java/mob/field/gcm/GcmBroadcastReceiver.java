package mob.field.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	public GcmBroadcastReceiver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context ctx, Intent intent) {
		
		// Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(ctx.getPackageName(), GcmMessageHandler.class.getName());
        Log.i("Successfull", "Successfullyy" +
        		"");
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(ctx, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

	}
}
