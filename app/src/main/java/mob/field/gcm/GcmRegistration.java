package mob.field.gcm;

import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmRegistration extends AsyncTask<String, String, String> {

    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "401198413764";
	private Activity activity;

    public GcmRegistration(Activity activity) {
		this.activity = activity;
	}
//    public void getRegId(){
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(activity);
//                    }
//                    regid = gcm.register(PROJECT_NUMBER);
//                    msg = regid;
//                    Log.i("GCM",  msg);
//                    new SendRegisterId(msg,MainActivity.this).execute();
//
//                } catch (IOException ex) {
////                    msg = "Error :" + ex.getMessage();
//
//                }
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(final String msg) {
//            	 new SendRegisterId(msg,activity).execute();
//
//            }
//        }.execute();
//    }
//    
	
	@Override
	protected String doInBackground(String... params) {
		 String msg = "";
         try {
             if (gcm == null) {
                 gcm = GoogleCloudMessaging.getInstance(activity);
             }
             regid = gcm.register(PROJECT_NUMBER);
             msg = regid;
             Log.i("GCM",  msg);
            
//             new SendRegisterId(msg,activity).execute();

         } catch (IOException ex) {
//             msg = "Error :" + ex.getMessage();

         }
         return msg;
	}

	 @Override
     protected void onPostExecute(final String msg) {
     	 new SendRegisterId(msg,activity).execute();

     }
}
