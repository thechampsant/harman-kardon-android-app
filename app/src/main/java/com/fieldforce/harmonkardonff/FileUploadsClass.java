package com.fieldforce.harmonkardonff;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import mob.field.gcm.AsynResponse;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileUploadsClass {

	Activity activity;

	public FileUploadsClass(Activity activity) {
		this.activity = activity;
	}

	public String saveImageToPhone(Bitmap finalBitmap) {

		String root = activity.getFilesDir().getAbsolutePath();
		File myDir = new File(root + "/saved_images");
		myDir.mkdirs();
		String fname = "Image-" + System.currentTimeMillis() + ".png";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {

		}
		return root + "/saved_images/" + fname;
	}

	AsynResponse asyRes;

	public void setAsyResponse(AsynResponse asyRes) {
		this.asyRes = asyRes;
	}

	public void uploadFileWithAsy(final String upLoadServerUri,
			final String sourceFileUri) {
		AsyncTask<String, String, ImageUploadResponseRecord> asyTask = new AsyncTask<String, String, ImageUploadResponseRecord>() {

			@Override
			protected ImageUploadResponseRecord doInBackground(String... params) {
				ImageUploadResponseRecord imgUploadRes = uploadFile(upLoadServerUri, sourceFileUri);
				return imgUploadRes;
			}

			@Override
			protected void onPostExecute(ImageUploadResponseRecord result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result.status.equalsIgnoreCase("true")) {
					Toast.makeText(activity, result.msg, Toast.LENGTH_SHORT)
							.show();
				}
				else if(result.status.equalsIgnoreCase("false"))
				{
					Toast.makeText(activity, result.err, Toast.LENGTH_SHORT)
					.show();
				}
				if (asyRes != null && result.jobj != null)
					asyRes.response(result.jobj);
			}
		};
		asyTask.execute();
	}



	public ImageUploadResponseRecord uploadFile(final String upLoadServerUri, final String sourceFileUri) {
		ImageUploadResponseRecord imgUpResRec = new ImageUploadResponseRecord();
		JSONObject fileUploadResponse = null;
		final Context context = activity;
		final Boolean show_toast = false;
		int serverResponseCode = 0;
		String fileName = sourceFileUri;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File not exist :" + sourceFileUri);
			imgUpResRec.setError("Source File not exist :" + sourceFileUri);
			return null;
		} else {
			try {
				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);
				conn.setRequestProperty("source", "mobile-app");

				dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				switch (serverResponseCode) {
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					String responseJson = sb.toString();
					fileUploadResponse = new JSONObject(responseJson);
				}

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {
					imgUpResRec.setSuccess("File Upload Completed.",
							fileUploadResponse);
					/*
					 * activity.runOnUiThread(new Runnable() {
					 * 
					 * @Override public void run() { String msg =
					 * "File Upload Completed.\n\n See uploaded file here : \n\n"
					 * + " http://www.androidexample.com/media/uploads/" +
					 * sourceFileUri; if (show_toast) Toast.makeText(context,
					 * "File Upload Complete.", Toast.LENGTH_LONG).show(); } });
					 */
				} else {
					Log.e("uploader", "status code: " + serverResponseCode);
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

				/*
				 * Toast.makeText(activity, "Image Uploaded",
				 * Toast.LENGTH_SHORT) .show();
				 */
			} catch (MalformedURLException ex) {
				ex.printStackTrace();

				/*
				 * activity.runOnUiThread(new Runnable() {
				 * 
				 * @Override public void run() { if (show_toast)
				 * Toast.makeText(context, "MalformedURLException",
				 * Toast.LENGTH_SHORT).show(); } });
				 */
				imgUpResRec.setError("MalformedURLException");
				Log.e("uploader", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {
				e.printStackTrace();
				imgUpResRec.setError("Got Exception : see logcat ");
				/*
				 * activity.runOnUiThread(new Runnable() {
				 * 
				 * @Override public void run() { if (show_toast)
				 * Toast.makeText(context, "Got Exception : see logcat ",
				 * Toast.LENGTH_SHORT).show(); } });
				 */
				Log.e("uploader", "Exception : " + e.getMessage(), e);
			}
			// dialog.dismiss();
			// return serverResponseCode;
			// return fileUploadResponse;//this was working
			return imgUpResRec;
		} // End else block
	}
}
