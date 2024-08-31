package app.core.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class BaseReceiver extends BroadcastReceiver {

	private Context context = null;
	private boolean IsBoot = false;
	private boolean IsSMS=false;
	private boolean IsDateChange=false;
	

	public abstract void onSystemBoot();
	public abstract void onSMSReceive(Bundle bundle);
	public abstract void onDateChange();

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			this.IsBoot = true;
			this.onSystemBoot();
		} else if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
			IsSMS=true;
			onSMSReceive(intent.getExtras());
		}
		else if("android.intent.action.DATE_CHANGED".equals(intent.getAction()))
		{
			this.IsDateChange=true;
			this.onDateChange();
		}
	}

	public boolean IsSystemBoot() {
		return this.IsBoot;
	}
	public boolean IsSMS(){
		return this.IsSMS;
	}
	public boolean IsDateChanged(){
		return this.IsDateChange;
	}

	public void startService(Class<?> cls) {
		Intent pushIntent = new Intent(context, cls);
		context.startService(pushIntent);
	}

}