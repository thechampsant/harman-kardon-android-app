package mob.field.harmonkardonff.BLL;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.fieldforce.harmonkardonff.AsyncResp;


public class DisplayViewApi extends AsyncTask<String, Integer, JSONObject>{

	String apiUrl;
	
	public DisplayViewApi(String apiUrl) {
		this.apiUrl = apiUrl;
	}

		@Override
		protected JSONObject doInBackground(String... params) {
			 InputStream is = null;
			  String result = "";
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
			asyResp.response(result);
		}
		
		AsyncResp asyResp;
		
		public void setResponse(AsyncResp asyResp){
			this.asyResp = asyResp;
		}
}
