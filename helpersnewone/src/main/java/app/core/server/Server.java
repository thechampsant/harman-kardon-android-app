package app.core.server;

import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import app.core.model.Response;
import app.core.sqllite.DataEntity;

public class Server {

	public static String TAG_LOG = "SERVER_HIT";
	public static String TAG_LOGPOST = "serverpost";
	
	@SuppressWarnings("rawtypes")
	Decode decoder = new Decode();
	
	JSONArray jArray = new JSONArray();
	JSONObject jobj = new JSONObject();
	
	public boolean IsTimeout = false;
	public String ConnectionError = null;
	int timeoutConnection = 60000;
	int timeoutSocket = 60000;

	private void refresh() {
		IsTimeout = false;
		ConnectionError = null;
	}

	private void setError(String msg) {
		IsTimeout = true;
		if (msg == null) {
			ConnectionError = "May be your network is too slow or not working!Please try again";
			return;
		} else if (msg.isEmpty()) {
			ConnectionError = "May be your network is too slow or not working!Please try again";
			return;
		} else {
			ConnectionError = msg;
		}
	}

	private HttpResponse newPostResponse(String url, JSONObject jobject){
		refresh();
		HttpResponse response = null;
		HttpPost httppost = new HttpPost(url);
		
		Log.i(TAG_LOGPOST,url);
		
		httppost.setHeader("Content-type", "application/json");
		HttpParams httpParams = new BasicHttpParams();
		
		HttpConnectionParams.setConnectionTimeout(httpParams,timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		
		HttpClient client = new DefaultHttpClient(httpParams);
		try {
			httppost.setEntity(new ByteArrayEntity(jobject.toString().getBytes("UTF8")));
			Log.i(TAG_LOGPOST,jobject.toString());
			response = client.execute(httppost);
			Log.i("JSON_COM", "Response Received!");
		} catch (ClientProtocolException e) {
			if(e != null) e.printStackTrace();
			setError(e.getMessage());
			Log.e("Connection-Error", e.getMessage());
			return response;
			
		} catch (IOException e) {
			
			if(e != null) e.printStackTrace();
			setError(e.getMessage());
			Log.e("Connection-Error", e.getMessage());
			return response;
		}
		return response;
	}

	private HttpResponse newGetResponse(String url) {
		
		refresh();
		HttpGet httpGet = new HttpGet(url);
		Log.i(TAG_LOG,url);
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.

		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpResponse response = null;
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		try {
			response = httpClient.execute(httpGet);
			return response;
		} catch (ClientProtocolException e) {
			if(e != null) e.printStackTrace();
			setError(e.getMessage());
			Log.e("Connection-Error", e.getMessage());
			return response;
		} catch (IOException e) {
			
			if(e != null) e.printStackTrace();
			setError(e.getMessage());
			Log.e("Connection-Error", e.getMessage());
			return response;
		}
	}
	
	private String ResponseString(HttpResponse response) throws ParseException, IOException{
		return EntityUtils.toString(response.getEntity());
	}
	
	private JSONArray GetJSONArray(String response) throws JSONException{
		try{
			jArray = new JSONArray(response);
			return jArray;
		}catch(Exception e){
			if(e != null) e.printStackTrace();
			Log.e("JSONParseException11", response);
			return new JSONArray("["+response+"]");
		}
	}

	public JSONArray getServerResponse(String apiURL) throws IOException,
			JSONException {
		
		return GetJSONArray(ResponseString(newGetResponse(apiURL))); 
	}

	public JSONArray getServerResponse(String apiURL, JSONObject jobject)
			throws IOException, JSONException {
		
		return GetJSONArray(ResponseString(newPostResponse(apiURL, jobject)));
	}

	@SuppressWarnings({ "rawtypes" })
	public Response getResponse(String apiurl) {
		try {
			JSONObject obj = (JSONObject) getServerResponse(apiurl).get(0);
			if (IsTimeout)
				return new Response(ConnectionError);
			Response response = decoder.ToResponse(obj);
			return response;
		} catch (Exception ex) {
			if (IsTimeout)
				return new Response(ConnectionError);
			else
				return new Response(ex.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public Response getResponse(String apiurl, JSONObject jobject) {
		try {

			JSONObject obj = (JSONObject) getServerResponse(apiurl,jobject).get(0);
			if (IsTimeout)
				return new Response(ConnectionError);
			Response response = decoder.ToResponse(obj);
			return response;
		} catch (Exception ex) {
			if (IsTimeout)
				return new Response(ConnectionError);
			else
				return new Response(ex.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Response getResponse(String apiurl,JSONObject jobject, T data) {
		try {

			JSONObject obj = (JSONObject) getServerResponse(apiurl,jobject).get(0);
			if (IsTimeout)
				return new Response(ConnectionError);
			Response response = decoder.ToResponse((DataEntity) data, obj);
			return response;
		} catch (Exception ex) {
			if (IsTimeout)
				return new Response(ConnectionError);
			else
				return new Response(ex.getMessage());
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Response getResponse(String apiurl, T data) {
		try {

			JSONObject obj = (JSONObject) getServerResponse(apiurl).get(0);
			if (IsTimeout)
				return new Response(ConnectionError);
			Response response = decoder.ToResponse((DataEntity) data, obj);
			return response;
		} catch (Exception ex) {
			if (IsTimeout)
				return new Response(ConnectionError);
			else
				return new Response(ex.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T, E> Response getResponse(String apiurl, T data, E edata) {
		try {

			JSONObject obj = (JSONObject) getServerResponse(apiurl).get(0);
			if (IsTimeout)
				return new Response(ConnectionError);
			Response response = decoder.ToResponse((DataEntity) data,
					(DataEntity) edata, obj);
			return response;
		} catch (Exception ex) {
			if (IsTimeout)
				return new Response(ConnectionError);
			else
				return new Response(ex.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T getResponse(T type, Bundle extra) {
		try {
			return (T) decoder.ToResponse((DataEntity) type, extra);
		} catch (Exception ex) {
			return null;
		}
	}

}
