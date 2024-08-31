package app.core.server;

import android.app.ProgressDialog;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FileUploader {
	String crlf = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";

	private String UploadURL = null;
	private String webServiceURL;
	private String FileHandlerName;
	private String WebFileHandler;
	private String BaseUrl = null;
	private boolean IsUploadUrlAdded = false;

	public void RemoveAllParameters() {
		UploadURL = null;
		IsUploadUrlAdded=false;
	}

	public FileUploader() {
	};

	public FileUploader setFileUploadUrl(String UploadUrl) {
		this.BaseUrl = UploadUrl;
		return this;
	}

	public void SetWebFileHandler(String PathWithName) {

	}

	public FileUploader(String _webServiceURL, String _FileHandlerName) {
		this.webServiceURL = _webServiceURL;
		this.FileHandlerName = _FileHandlerName;
	}

	public void addParamters(String Param, String Value)
			throws UnsupportedEncodingException {
		TryCreateUrl();
		UploadURL = UploadURL + Param + "=" + URLEncoder.encode(Value, "UTF-8")
				+ "&";
		Log.e("urllll",UploadURL+"null");
	}

	private void TryCreateUrl() {
		if (BaseUrl != null && !IsUploadUrlAdded) {
			UploadURL = BaseUrl;
			IsUploadUrlAdded = true;
		}
		if (UploadURL == null)
			this.UploadURL = this.webServiceURL + "/" + this.FileHandlerName
					+ "?";
	}

	public String Uploadfile(File f) {
		try {
			TryCreateUrl();
			HttpURLConnection httpUrlConnection = null;

			URL url = new URL(UploadURL);
			httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setDoOutput(true);

			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");

			httpUrlConnection
					.setRequestProperty("Content-Type", "octet-stream");
			DataOutputStream request = new DataOutputStream(
					httpUrlConnection.getOutputStream());

			FileInputStream reader = new FileInputStream(f);
			// FileReader reader = new FileReader(f);
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = reader.read(buffer, 0, 1024)) >= 0) {
				request.write(buffer, 0, length);
			}

			reader.close();
			request.flush();
			request.close();

			// Read the Response
			BufferedInputStream responseStream = new BufferedInputStream(
					httpUrlConnection.getInputStream());
			BufferedReader responseStreamReader = new BufferedReader(
					new InputStreamReader(responseStream));
			String line = "";
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = responseStreamReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			responseStreamReader.close();

			String response = stringBuilder.toString();
			System.out.println(response);
			httpUrlConnection.disconnect();
			return response;
		} catch (Exception ex) {
			Log.e("Webfile Uploder", ex.getMessage());
			Log.e("Webfile Uploder", ex.toString());
			return ex.toString();
		}
	}

	public String Uploadfile(File f, ProgressDialog progress) {
		try {
			TryCreateUrl();
			HttpURLConnection httpUrlConnection = null;

			URL url = new URL(UploadURL);
			System.out.println("!--!UploadURL = " + UploadURL);
			httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setDoOutput(true);

			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
			httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");

			httpUrlConnection
					.setRequestProperty("Content-Type", "octet-stream");
			DataOutputStream request = new DataOutputStream(
					httpUrlConnection.getOutputStream());

			FileInputStream reader = new FileInputStream(f);
			// FileReader reader = new FileReader(f);
			long size = f.length();
			long TotalSize = size;
			byte[] buffer = new byte[1024];
			int length = 0;
			int Done = 0;
			progress.setProgress(6);
			while ((length = reader.read(buffer, 0, 1024)) >= 0) {
				request.write(buffer, 0, length);
				size = size - length;
				if (size >= 1)
					Done = (int) (TotalSize * 10 / size);
				if (Done < 90)
					progress.setProgress(Done);
			}

			reader.close();
			request.flush();
			request.close();

			// Read the Response
			BufferedInputStream responseStream = new BufferedInputStream(
					httpUrlConnection.getInputStream());

			BufferedReader responseStreamReader = new BufferedReader(
					new InputStreamReader(responseStream));
			progress.setProgress(95);
			String line = "";
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = responseStreamReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			responseStreamReader.close();

			String response = stringBuilder.toString();
			System.out.println(response);
			httpUrlConnection.disconnect();
			return response;
		} catch (Exception ex) {
			Log.e("Webfile Uploder", ex.getMessage());
			Log.e("Webfile Uploder", ex.toString());
			return ex.toString();
		}
	}

	public void Upload(File f) throws IOException {

		TryCreateUrl();
		HttpURLConnection httpUrlConnection = null;

		URL url = new URL(UploadURL);
		httpUrlConnection = (HttpURLConnection) url.openConnection();
		httpUrlConnection.setUseCaches(false);
		httpUrlConnection.setDoOutput(true);

		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
		httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
		// httpUrlConnection.setRequestProperty("Content-Type",
		// "multipart/form-data;boundary=" + this.boundary);
		httpUrlConnection.setRequestProperty("Content-Type", "octet-stream");
		DataOutputStream request = new DataOutputStream(
				httpUrlConnection.getOutputStream());
		// request.writeBytes("Content-Disposition: form-data; name=\"" +
		// f.getName() + "\";filename=\"" + "trial.jpg" + "\"" + this.crlf);
		// request.writeBytes(this.twoHyphens + this.boundary + this.crlf);

		// request.writeBytes(this.crlf);

		//
		FileInputStream reader = new FileInputStream(f);
		// FileReader reader = new FileReader(f);
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = reader.read(buffer, 0, 1024)) >= 0) {
			request.write(buffer, 0, length);
		}

		// request.writeBytes(this.crlf);
		// request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens
		// + this.crlf);
		reader.close();
		request.flush();
		request.close();

		// Read the Response
		BufferedInputStream responseStream = new BufferedInputStream(
				httpUrlConnection.getInputStream());
		BufferedReader responseStreamReader = new BufferedReader(
				new InputStreamReader(responseStream));
		String line = "";
		StringBuilder stringBuilder = new StringBuilder();
		while ((line = responseStreamReader.readLine()) != null) {
			stringBuilder.append(line).append("\n");
		}
		responseStreamReader.close();

		String response = stringBuilder.toString();
		System.out.println(response);
		httpUrlConnection.disconnect();

	}
}
