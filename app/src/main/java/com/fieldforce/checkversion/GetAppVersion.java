package com.fieldforce.checkversion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;


/**
 * Created by Dharmendra kumar on 9/4/2017.
 */

public class GetAppVersion extends AsyncTask<String, String, String> {

	Context cntxt;
	String packageName;
	AppIntrfce appintfc;

	public GetAppVersion(Context cntxt, AppIntrfce appintfc) {
		this.appintfc = appintfc;
		this.cntxt = cntxt;

	}

	boolean responseResult;

	private long value(String string) {
		string = string.trim();
		if (string.contains(".")) {
			final int index = string.lastIndexOf(".");
			return value(string.substring(0, index)) * 100
					+ value(string.substring(index + 1));
		} else {
			return Long.valueOf(string);
		}
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			responseResult = web_update();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (responseResult) {
			appintfc.dialog("Updated Version Available. Want to Update");
		}

	}

	private boolean web_update() {
		try {
			packageName = cntxt.getPackageName();
			String curVersion = cntxt.getPackageManager().getPackageInfo(
					packageName, 0).versionName;
			String newVersion = curVersion;
	/*		newVersion = Jsoup
					.connect(
							"https://play.google.com/store/apps/details?id="
									+ packageName + "&hl=en")
					.timeout(30000)
					.userAgent(
							"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
					.referrer("http://www.google.com").get()
					.select("div[itemprop=softwareVersion]").first().ownText();*/

			long first = value(curVersion);
			long second = value(newVersion);
			boolean result = (first < second) ? true : false;
			return result;
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}
}
