package mob.field.harmonkardonff.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverOnReboot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		startservice(context);
		startgpsservice(context);

	}

	private void startgpsservice(Context context) {

		startbroadcast(context);

	}

	private void startbroadcast(Context context) {
		Intent in = new Intent();
		in.setAction("startgpsbroadcast");
		context.sendBroadcast(in);
		Log.d("debug", "startgpsbroadcast");

	}

	private void startservice(Context context) {

		Intent in = new Intent(context, WHPL_MainService.class);
		context.startService(in);
	}
}
