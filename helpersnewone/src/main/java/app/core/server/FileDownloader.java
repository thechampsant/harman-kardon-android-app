package app.core.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.util.ByteArrayBuffer;
import android.os.Environment;
import android.util.Log;
import app.core.async.BackgroundProcess;
import app.core.model.Response;

public class FileDownloader {

	private String FileUrl;
	@SuppressWarnings("rawtypes")
	Response response = new Response();
	int timeoutConnection = 10000;
	int readtimeout = 15000;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Response GetFileName() {
		try {
			String[] t = FileUrl.split("/");
			response.FileName = t[t.length - 1];
			return response;
		} catch (Exception ex) {

			return new Response(ex.toString());
		}
	}

	private String getFileSizeText(int size) {
		if (size < 1024)
			return "size:-" + String.valueOf(size) + " bytes";
		else if (size < 1024 * 1024)
			return "size:-" + String.valueOf((size / 1024)) + " kb";
		else if (size < 1024 * 1024 * 1024)
			return "size:-" + String.valueOf((size / 1024 * 1024)) + " Mb";
		else
			return "size:-" + String.valueOf(size) + " bytes";
	}

	@SuppressWarnings("rawtypes")
	public Response downloadFile(String ServerUrl) {

		FileUrl = ServerUrl;
		URL url = null;
		try {
			url = new URL(ServerUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String PATH = Environment.getExternalStorageDirectory() + "/Download/";
		URLConnection uconn = null;
		try {
			uconn = url.openConnection();
			setTimeOutConnection(uconn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File(PATH);
		if (file.exists() == false) {
			file.mkdirs();
		}
		try {
			File outputFile = new File(file, this.GetFileName().FileName);

			InputStream is = uconn.getInputStream();
			BufferedInputStream bufferinstream = new BufferedInputStream(is);

			ByteArrayBuffer baf = new ByteArrayBuffer(5000);
			int current = 0;
			while ((current = bufferinstream.read()) != -1) {
				baf.append((byte) current);
			}

			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(baf.toByteArray());
			fos.flush();
			fos.close();
			response.file = outputFile;
			return response;
		} catch (Exception ex) {
			return new Response(ex.toString());
		}
	}

	private void setTimeOutConnection(URLConnection connection) {
		//connection.setDoOutput(true);
		connection.setConnectTimeout(timeoutConnection);
		connection.setReadTimeout(readtimeout);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response downloadFile(String ServerUrl, String localurl) {

		try {
			FileUrl = ServerUrl;
			URL url = null;
			try {
				url = new URL(ServerUrl);
			} catch (MalformedURLException e) {
				Log.e("FileDownloader: ",e.toString());
			}
			String PATH = localurl;
			URLConnection uconn = null;
			try {
				uconn = url.openConnection();
				setTimeOutConnection(uconn);
			} catch (IOException e) {
				Log.e("FileDownloader: ",e.toString());
			}
			File outputFile = new File(PATH);
			// if (file.exists() == false) {
			// file.mkdirs();
			// }
			try {

				InputStream is = uconn.getInputStream();
				BufferedInputStream bufferinstream = new BufferedInputStream(is);

				ByteArrayBuffer baf = new ByteArrayBuffer(5000);
				int current = 0;
				while ((current = bufferinstream.read()) != -1) {
					baf.append((byte) current);
				}

				FileOutputStream fos = new FileOutputStream(outputFile);
				fos.write(baf.toByteArray());
				fos.flush();
				fos.close();
				response.status = "true";
				response.file = outputFile;
				return response;
			} catch (Exception ex) {
				Log.e("FileDownloader: ",ex.toString());
				return new Response(ex.toString());
			}
		} catch (Exception ex) {
			Log.e("FileDownloader: ",ex.toString());
			return new Response(ex.toString());
		} finally {
			if (new File(localurl).length()<1)
				new File(localurl).delete();
		}
	}

	@SuppressWarnings("rawtypes")
	public Response downloadFile(String ServerUrl, String localurl,
			BackgroundProcess bp) {

		// variables----------
		FileUrl = ServerUrl;
		URL url = null;
		URLConnection uconn = null;
		InputStream input = null;
		FileOutputStream outputFile = null;
		String PATH = localurl;

		try {
			url = new URL(ServerUrl);
		} catch (MalformedURLException e) {
			Log.e("Download file url error: ", e.toString());
			return new Response(e.toString());
		}

		try {
			uconn = url.openConnection();
			setTimeOutConnection(uconn);
		} catch (IOException e) {
			Log.e("Download file connection opening error: ", e.toString());
			return new Response(e.toString());
		}
		byte data[] = new byte[4096];

		try {
			outputFile = new FileOutputStream(PATH);
		} catch (FileNotFoundException e) {

			Log.e("FileDownloader: ",e.toString());
			return new Response(e.toString());
		}
		try {

			// might be -1: server did not report the length
			int fileLength = uconn.getContentLength();

			input = uconn.getInputStream();

			int current = 0;
			long total = 0;

			while ((current = input.read(data)) != -1) {
				total += current;
				// publishing the progress....
				if (fileLength > 0) // only if total length is known
					bp.setProgress(((int) (total * 100 / fileLength)));
				outputFile.write(data, 0, current);
			}
			if (total < 1)
				response.status = "false";
			return response;
		} catch (Exception ex) {
			Log.e("FileDownloader: ",ex.toString());
			return new Response(ex.toString());
		} finally {
			try {
				if (outputFile != null)
					outputFile.close();
				if (input != null)
					outputFile.close();
				if (new File(localurl).length()<1)
					new File(localurl).delete();
			} catch (IOException ignored) {
			}
		}
	}
}
