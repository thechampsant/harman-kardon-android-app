package app.core.utils;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import app.core.model.NotificationModel;
import android.app.Service;

import androidx.core.app.NotificationCompat;


public class NotificationHandler {

	@SuppressWarnings("rawtypes")
	private NotificationModel format;
	private NotificationManager mgr;
	private Context activity;
	private Service service = null;
	Timer timer = new Timer();
	ArrayList<Integer> Nids = new ArrayList<Integer>();

	public void Init(Activity a) {
		activity = a;
		mgr = (NotificationManager) activity
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public NotificationHandler(Activity a) {
		Init(a);
	}

	// public NotificationHandler(Service _service) {
	// service=(IntentService) _service;
	// mgr = (NotificationManager) service
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// }
	public NotificationHandler(Service _service) {
		service = _service;
		mgr = (NotificationManager) service
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@SuppressWarnings("rawtypes")
	public void Notify(NotificationModel _NotificationFormat) {
		format = _NotificationFormat;
		this.display();
	}

	public void UnNotify(int id) {
		try {
			if (IsNotifyExist(id)) {
				mgr.cancel(id);
				Nids.remove(id);
			}
		} catch (Exception ex) {

		}
	}

	public boolean IsNotifyExist(int id) {
		return Nids.contains(id);
	}

	private void display() {
		if (this.service != null)
			this.DisplayServiceNotification();
		else
			this.DisplayActivityNotification();
	}

	@SuppressWarnings({ "unchecked" })
	private void DisplayActivityNotification() {
		if (!IsNotifyExist(format.NotifyID))
			Nids.add(format.NotifyID);
		Notification note = null;
		format.ImageResourceID = format.ImageResourceID == 0 ? android.R.drawable.ic_dialog_email
				: format.ImageResourceID;
		
		Intent intent = new Intent(activity, format.PendingIntent);
		intent.setAction(format.IntentAction);
		PendingIntent i = PendingIntent.getActivity(activity, 1, intent, 0);
		note = getNote(format.ImageResourceID,
				format.MessageHeading,
				format.Message, 
				format.NotificationMessage, 
				i, this.activity);

		note.number = format.NotifyNumber;
		// note.defaults|=Notification.DEFAULT_VIBRATE;
		note.defaults |= Notification.DEFAULT_SOUND;
		note.flags = Notification.FLAG_AUTO_CANCEL;
		mgr.notify(format.NotifyID, note);
	}

	@SuppressWarnings("unchecked")
	private void DisplayServiceNotification() {
		if (!IsNotifyExist(format.NotifyID))
			Nids.add(format.NotifyID);
		Notification note = null;
		format.ImageResourceID = format.ImageResourceID == 0 ? android.R.drawable.ic_dialog_email
				: format.ImageResourceID;

		// This pending intent will open after notification click
		Intent intent = new Intent(this.service, format.PendingIntent);
		intent.setAction(format.IntentAction);
		PendingIntent i = PendingIntent.getActivity(this.service, 1, intent, 0);
		note = getNote(format.ImageResourceID,
				format.MessageHeading,
				format.Message, 
				format.NotificationMessage, 
				i,this.service);

		note.number = format.NotifyNumber;
		// note.defaults|=Notification.DEFAULT_VIBRATE;
		note.defaults |= Notification.DEFAULT_SOUND;
		note.flags = Notification.FLAG_AUTO_CANCEL;
		mgr.notify(format.NotifyID, note);

	}

	private Notification getNote(int iconID, String messageHeading, String message,
			String nMessage, PendingIntent intent, Context context) {

		Notification note = new NotificationCompat.Builder(context)
				.setSmallIcon(iconID)
				.setTicker(nMessage)
				.setContentTitle(messageHeading)
				.setContentText(message)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setWhen(System.currentTimeMillis())
				.setContentIntent(intent)
				// At most three action buttons can be added
//				 .addAction (android.R.drawable.ic_dialog_info,"View",intent).
				.setAutoCancel(true).build();

		return note;
	}

}
