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
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fieldforce.harmonkardonff.LocalStorage;
import com.fieldforce.harmonkardonff.MainActivity;

public class SendRegisterId extends AsyncTask<String, Integer, JSONObject>{

	static String Web = WebService.Web;
	static String WebController = WebService.WebController;

	// Actions
	static String RegisterDevice = "RegisterDevice?";
	static String ApiUrl = Web + WebController;
	static String UserName = MainActivity.MyInfo.EmployeeCode;
	
	private String app;
	Activity activity;
	
	public SendRegisterId(String msg,Activity activity) {
		this.app = msg;
		this.activity = activity;
	}

		@Override
		protected JSONObject doInBackground(String... params) {
			 InputStream is = null;
			  String result = "";
			  String apiUrl = ApiUrl;
				apiUrl += RegisterDevice + "UserName=" + UserName + "&RegistrationID=" + app;
			  try {
			   HttpClient httpclient = new DefaultHttpClient();
			   HttpGet httpPost = new HttpGet(apiUrl);
			   HttpResponse response = httpclient.execute(httpPost);
			   HttpEntity entity = response.getEntity();
			   is = entity.getContent();

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
			try {
				if(result.getString("status").equalsIgnoreCase("true")){
					 LocalStorage localStrObj =   new LocalStorage(activity);
//		             String regId = localStrObj.getMessage(LocalStorage.REGID);
//		             if(regId==null){
		             localStrObj.setMessage(LocalStorage.REGID, app);
//		             }
					Toast.makeText(activity, "Registered successfully", Toast.LENGTH_LONG).show();  
				  }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
