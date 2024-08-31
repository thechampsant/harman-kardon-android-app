package com.fieldforce.gpscoord;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SchedulerEvent extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("APP_TAG", "schedulerevent.onReceive() called");
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, SchedulerSetupReceiver.class); // explicit

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, 234324243, i, 0);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),
				 1000 * 60 * 14,pendingIntent);
		
	}

}
