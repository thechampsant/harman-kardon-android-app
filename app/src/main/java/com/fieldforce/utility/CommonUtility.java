package com.fieldforce.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class CommonUtility {

	public static View getView(Context context, int layoutID) {
		View view;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layoutID, null);
		return view;
	}
	public static int getSystemDisplayHeight(Activity context)
	{
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		long height = displaymetrics.heightPixels;
		return (int)height;
	}
	public static int getSystemDisplayWidth(Activity context)
	{
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		long width = displaymetrics.widthPixels;
		return (int)width;
	}
	public static int getHeight(int weight,Activity context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		long height = displaymetrics.heightPixels;
		long percetage = (weight * 100) / 1200;

		return (int) (height * percetage) / 100;
	}
	public static int getWidth(int weight,Activity context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		long width = displaymetrics.widthPixels;
		long percetage = (weight * 100) / 800;

		return (int) (width * percetage) / 100;
	}
	public static boolean isMyServiceRunning(Class<?> serviceClass,
			Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				Log.i("Service already", "running");
				return true;
			}
		}
		Log.i("Service not", "running");
		return false;
	}

	public static int getHeight(Activity activity, int weight) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		long height = displaymetrics.heightPixels;
		long percetage = (weight * 100) / 1200;

		return (int) (height * percetage) / 100;
	}

	public static String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}

	public static String getCurrentDateYMD() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}

	public static boolean isNetworkFoundToast(Activity activity) {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null)
			isConnected = activeNetworkInfo.isConnected();
		if (activeNetworkInfo != null && isConnected)
			return true;
		else {
			ShowToast("No Internet Found", activity);
			return false;
		}
	}

	public static boolean isNetworkFoundToast(Context activity) {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null)
			isConnected = activeNetworkInfo.isConnected();
		if (activeNetworkInfo != null && isConnected)
			return true;
		else {
			ShowToast("No Internet Found", activity);
			return false;
		}
	}

	public static void ShowToast(String Message, Context activity) {
		Toast toast = Toast.makeText(activity, Message, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void ShowToast(String Message, Activity activity) {
		Toast toast = Toast.makeText(activity, Message, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void ShowToastLong(String Message, Activity activity) {
		Toast toast = Toast.makeText(activity, Message, Toast.LENGTH_LONG);
		toast.show();
	}

	public static void showDialogSingleButton(final Activity activity) {
		AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setTitle("Alert Dialog");
		alertDialog.setMessage("Consumer Not Available");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}
		});
		alertDialog.show();
	}

	public static void connectCAll(Activity activity, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL);

		intent.setData(Uri.parse("tel:" + phoneNumber));
		activity.startActivity(intent);
	}

	public static ProgressDialog showProgressBar(Context context, String message) {
		ProgressDialog pd = new ProgressDialog(context,
				AlertDialog.THEME_HOLO_LIGHT);
		if (message == null || message.equalsIgnoreCase(""))
			pd.setMessage("loading...");
		else
			pd.setMessage(message);
		pd.show();
		return pd;
	}

	public static void dismissProgressBar(ProgressDialog pd) {
		if (pd != null) {
			pd.dismiss();
		}
	}

	public static void cancelProgressBar(ProgressDialog pd) {
		if (pd != null) {
			pd.cancel();
		}
	}

	
	
}
