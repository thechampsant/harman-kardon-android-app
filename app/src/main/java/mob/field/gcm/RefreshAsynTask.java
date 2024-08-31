package mob.field.gcm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mob.field.harmonkardonff.services.WebService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.fieldforce.harmonkardonff.MainActivity;

public class RefreshAsynTask extends AsyncTask<String, Integer, JSONObject>{

	static String Web = WebService.Web;
	static String WebController = WebService.WebController;

	// Actions
	static String GetRegisterDevice = "getPushNotificationMsgsForRE?";
	static String ApiUrl = Web + WebController;
	static String UserName = MainActivity.MyInfo.EmployeeCode;
	
	private String app;
	Activity activity;
	private ProgressDialog dialog;
	
	
	public RefreshAsynTask(Activity activity) {
		this.activity = activity;
	}
	
	protected void onPreExecute(){
        dialog=ProgressDialog.show(activity, "Loading", "Loading the message");
        dialog.setMax(1000);
    }

		@Override
		protected JSONObject doInBackground(String... params) {
			 InputStream is = null;
			  String result = "";
			  String apiUrl = ApiUrl;
				apiUrl += GetRegisterDevice + "UserName=" + UserName;
			  try {
			   HttpClient httpclient = new DefaultHttpClient();
			   HttpGet httpPost = new HttpGet(apiUrl);
			   HttpResponse response = httpclient.execute(httpPost);
			   HttpEntity entity = response.getEntity();
			   is = entity.getContent();
				Log.i("Noti",apiUrl);
			  } catch (Exception e) {
			   Log.e("log_tag", "Error in http connection " + e.toString());
			  }

			  // convert response to string
			  try {
			   BufferedReader reader = new BufferedReader(new InputStreamReader(
			     is, "UTF-8"), 8);
			   StringBuilder sb = new StringBuilder();
			   String line = null;
			   while ((line = reader.readLine()) != null) {
			    sb.append(line + "\n");
			   }
			   is.close();
			   result = sb.toString();
			  } catch (Exception e) {
			  }

			  JSONObject obj = null;
			  try {
			   obj = new JSONObject(result);
			   
			  } catch (JSONException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			  }
			  
			  return obj;
			 }
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			asyResp.response(result);
			dialog.dismiss();
		}
		
		AsynResponse asyResp;
		
		public void setResponse(AsynResponse asyResp){
			this.asyResp = asyResp;
		}
		
		
}
