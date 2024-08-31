package com.fieldforce.networkconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Parser {

	static final int timeoutConnection = 15000;
	static final int timeoutSocket = 15000;
	
	public static String text = "";
	
	public static JSONObject parserGET(String apiUrl) {
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

	public static JSONObject parserPost(String apiUrl,
			List<NameValuePair> nameValuePairs) {
		InputStream is = null;
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(apiUrl);
		httppost.setHeader(HTTP.CONTENT_TYPE, "application/Json");
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

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
	
	public static JSONObject parserPostRequest(String apiUrl, JSONObject jObj) {
		InputStream inputStream = null;
		String result = "";
		JSONObject jobj = null;
		try {

			// 1. create HttpClient
			HttpParams httpParams = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(httpParams,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParams);

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(apiUrl);

			String json = "";

			json = jObj.toString();

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			// EntityUtils.toString(httpResponse.getEntity());
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
			try {
				jobj = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
				
				jobj = new JSONObject(getErrorString(result));
			}

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());

		}

		// 11. return result
		return jobj;

	}

	
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}
	
	
	private static String getErrorString(String result) {
//		Toast.makeText(LoginActivity.current.getApplicationContext(), result, Toast.LENGTH_LONG).show();
		text = result;
		
		String resultUTF = "";
		if (result.contains("Forti") || result.contains("blocked")) {
			result = "Please change the network connection " + result;
		} else {
			result = "Connection problem!! \n" + result;
		}
		resultUTF = result;

		return "{\"status\":\"err\",\"msg\":\""
				+ convertToEncodedStirng(resultUTF) + "\"}";
		/*return "{\"status\":\"err\",\"msg\":\""
		+ resultUTF + "\"}";*/
	}
	
	public static String convertToEncodedStirng(String text) {
		try {

			return URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return text;
		}
	}
	
}
