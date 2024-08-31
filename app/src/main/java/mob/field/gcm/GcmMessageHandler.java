package mob.field.gcm;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mob.field.harmonkardonff.entitiymodels.MNotification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.fieldforce.harmonkardonff.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmMessageHandler extends IntentService {


    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmMessageHandler() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {

        final Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
//				sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
//				sendNotification("Deleted messages on server: "
//						+ extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                displayNotification(extras);
     //           Log.i(TAG, "Received: " + extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void displayNotification(Bundle extras) {
        Log.i("Start", "notification");

      //  Log.e("New Notification Received", "");


        if (extras.containsKey("message")) {
            String isMessage = extras.getString("isMessage");
            String value = extras.getString("message");

            String isImageUrl = extras.getString("isImageUrl");
            String imageUrl = extras.getString("imageUrl");

            String isTextTitle = extras.getString("isTextTitle");
            String textTitle = extras.getString("textTitle");

            String header = extras.getString("HeaderID");

            
            if (header != null) {
                new AcknowledgeForHeaderID(header).execute();
            }

            MNotification nf = new MNotification();
            nf.isMessage = isMessage;
            nf.Message = value;
            nf.isImageUrl = isImageUrl;
            nf.imageUrl = imageUrl;
            nf.isTextTitle = isTextTitle;
            nf.textTitle = textTitle;
            nf.Date = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm").format(Calendar.getInstance().getTime());

            nf.InsertOrUpdate();
//			   LocalStorage localStrObj =   new LocalStorage(getApplicationContext());
//			   String count = localStrObj.getMessage(LocalStorage.COUNT);
//			   int getcount = Integer.valueOf(count)+1;
//			   localStrObj.setMessage(LocalStorage.COUNT, getcount+"");
//			   localStrObj.setMessage(LocalStorage.MESSAGE+getcount, value);
//			   localStrObj.setMessage(LocalStorage.HEADERID+getcount, header);

            Log.i("Message", value);
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
               /* Invoking the default notification service */
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this, NotificationView.class), 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setContentTitle("Message from Comio");
            mBuilder.setContentText(value);
            mBuilder.setTicker("New Message Alert!");
            mBuilder.setAutoCancel(true);
            mBuilder.setSmallIcon(R.drawable.message_icon);

            mBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(0, mBuilder.build());
			  
			  
			   
			   /* Creates an explicit intent for an Activity in your app */
//			   Intent resultIntent = new Intent(this, NotificationView.class);
////			   resultIntent.putExtra("GCM Message", value);
//			   
//			   TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//			   stackBuilder.addParentStack(NotificationView.class);
//
//			   /* Adds the Intent that starts the Activity to the top of the stack */
//			   stackBuilder.addNextIntent(resultIntent);
//			   PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
//			   
//			   mBuilder.setContentIntent(resultPendingIntent);
//			   mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//			   mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
//			   /* notificationID allows you to update the notification later on. */
//			   mNotificationManager.notify(10, mBuilder.build());
        }
    }
}

