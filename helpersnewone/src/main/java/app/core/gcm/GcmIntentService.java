package app.core.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import app.core.gcm.IGcm;
import app.core.model.NotificationModel;
import app.core.utils.NotificationHandler;

public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 0;
	public static final String TAG = "GCM_INTENT_SERVICE";
	private NotificationHandler nhandler = null;
	private static IGcm igcm = null;

	public GcmIntentService() {
		super("GcmIntentService");
		// TODO Auto-generated constructor stub
	}

	public void setGcmIntentService(IGcm _igcm) {
		igcm = _igcm;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		nhandler = new NotificationHandler(this);
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);
        if(extras==null)
        {
        	Log.w("GCM Recevier says: ","extras is not found!");
        	return;
        }
		if (extras!=null) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {

				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				if (igcm != null)
				   igcm.getGcmExtra(this, extras, nhandler);
				else
					showUnimplementedNotification();

				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				Log.i(TAG, "Received: " + extras.toString());
			} 
		} 
		//showFailedNotification();
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showFailedNotification() {
		NotificationModel model = new NotificationModel();
		model.Message = "GCM is not working";
		model.MessageHeading = "application is not registred";
		model.NotificationMessage = "Please contact with admin";
		model.NotifyID = 11111;
		model.PendingIntent = this.getClass();
		nhandler.Notify(model);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showUnimplementedNotification() {
		NotificationModel model = new NotificationModel();
		model.Message = "GCM intent is not implemented";
		model.MessageHeading = "intent service is not implemented!";
		model.NotificationMessage = "Please contact with admin";
		model.NotifyID = 11111;
		model.PendingIntent = this.getClass();
		nhandler.Notify(model);
	}

}
