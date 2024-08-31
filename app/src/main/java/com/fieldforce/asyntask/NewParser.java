package com.fieldforce.asyntask;

import java.io.IOException;

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

import android.util.Log;

import com.fieldforce.webasyntask.ResponseParser;
import com.google.gson.Gson;

public class NewParser {
	static final int timeoutConnection = 50000;
	static final int timeoutSocket = 50000;
	public boolean IsTimeout = false;
	public String ConnectionError = null;
	private static final String TAG = "NewParserO";

	public ResponseParser getResponse(String apiUrl, JSONObject jobject) {
		try {
			Log.d(TAG, "getResponse: "+apiUrl);
			Gson gson = new Gson();
			String responsee = gson.toJson(jobject);
			Log.d(TAG, "RES ->  "+responsee);
			JSONObject obj = (JSONObject) getServerResponse(apiUrl, jobject).get(0);
			if (IsTimeout)
				return new ResponseParser(ConnectionError);
			return getResponseObject(obj);
		} catch (Exception ex) {
			if (IsTimeout)
				return new ResponseParser(ConnectionError);
			else {
				setError(ex.getMessage());
				return new ResponseParser(ConnectionError);
			}
		}
	}

	public JSONArray getServerResponse(String apiURL, JSONObject jobject)
			throws IOException, JSONException {

		return GetJSONArray(ResponseString(newPostResponse(apiURL, jobject)));
	}

	private HttpResponse newPostResponse(String url, JSONObject jobject) {
		refresh();
		HttpResponse response = null;
		HttpPost httppost = new HttpPost(url);

		httppost.setHeader("Content-type", "application/json");
		HttpParams httpParams = new BasicHttpParams();

		HttpConnectionParams
				.setConnectionTimeout(httpParams, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);

		HttpClient client = new DefaultHttpClient(httpParams);
		try {
			httppost.setEntity(new ByteArrayEntity(jobject.toString().getBytes(
					"UTF8")));
			response = client.execute(httppost);
			Log.i("JSON_COM", "Response Received!");
		} catch (ClientProtocolException e) {
			if (e != null)
				e.printStackTrace();
			setError(e.getMessage());
			Log.e("Connection-Error", e.getMessage());
			return response;

		} catch (IOException e) {

			if (e != null)
				e.printStackTrace();
			setError(e.getMessage());
			Log.e("Connection-Error", e.getMessage());
			return response;
		}
		return response;
	}

	public ResponseParser getResponse(String apiUrl) {
		try {
			JSONObject obj = (JSONObject) getServerResponse(apiUrl).get(0);
			if (IsTimeout)
				return new ResponseParser(ConnectionError);
			return getResponseObject(obj);
		} catch (Exception ex) {
			if (IsTimeout)
				return new ResponseParser(ConnectionError);
			else {
				setError(ex.getMessage());
				return new ResponseParser(ConnectionError);
			}
		}
	}

	private ResponseParser getResponseObject(JSONObject obj) {
		ResponseParser response = new ResponseParser();
		response.jsonObjResponse = obj;
		response.status = "true";
		return response;
	}

	public JSONObject Response(String str) {

		try {
			return new JSONObject(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String ResponseString(HttpResponse response) throws ParseException,
			IOException {
		return EntityUtils.toString(response.getEntity());
	}

	public JSONArray getServerResponse(String apiURL) throws IOException,
			JSONException {

		return GetJSONArray(ResponseString(newGetResponse(apiURL)));
	}

	private JSONArray GetJSONArray(String response) throws JSONException {
		try {
			JSONArray jArray = new JSONArray(response);
			return jArray;
		} catch (Exception e) {
			if (e != null)
				e.printStackTrace();
			Log.e("JSONParseException", response);
			return new JSONArray("[" + response + "]");
		}
	}

	private void refresh() {
		IsTimeout = false;
		ConnectionError = null;
	}

	private HttpResponse newGetResponse(String url) {

		refresh();
		HttpGet httpGet = new HttpGet(url);
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
			if (e != null)
				e.printStackTrace();
			setError(e.getMessage());
			Log.e("Connection-Error", e.getMessage());
			return response;
		} catch (IOException e) {

			if (e != null)
				e.printStackTrace();
			setError(e.getMessage());
			Log.e("Connection-Error", e.getMessage());
			return response;
		}
	}

	private void setError(String msg) {
		IsTimeout = true;
		if (msg == null) {
			ConnectionError = slowNetwork;
			return;
		} else if (msg.isEmpty()) {
			ConnectionError = slowNetwork+"!!\n\n";
			return;
		} else if (!msg.isEmpty() && msg.toLowerCase().contains("refused")
				|| msg.toLowerCase().contains("timed out")) {
			ConnectionError = "\n"+slowNetwork+"!!\n\n";
			return;
		} else if (!msg.isEmpty() && msg.toLowerCase().contains("Fortinet")
				|| msg.toLowerCase().contains("blocked")) {
			ConnectionError = "\n\n"+fortinetOrBlocked+"!!";
			return;
		} else {
			ConnectionError = msg;
			return;
		}
	}

	public static String slowNetwork = "May be your network is too slow or not working!Please try again";
	public static String fortinetOrBlocked = "Please change the network connection!! \nConnection is Restricted by the Network";

}
