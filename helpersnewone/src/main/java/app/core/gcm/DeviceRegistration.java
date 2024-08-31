package app.core.gcm;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.geoservice.LocationHelper;
import app.core.geoservice.LocationUtils;
import app.core.model.Response;
import app.core.server.Encode;
import app.core.server.Server;
import app.core.utils.Dialog;

public class DeviceRegistration extends Service  {
	LocationHelper locationHelper;// = new LocationClient(this, this, this);
	private String ApiUrl = null;
	private String UserName = null;
	Server server = new Server();

	public DeviceRegistration(Activity _context) {
		this.context = _context;
		locationHelper = new LocationHelper(context);
	}

	public DeviceRegistration setServerAPI(String _DeviceRegistrationID_URL,
			String _UserName) {
		ApiUrl = _DeviceRegistrationID_URL;
		this.UserName = _UserName;
		return this;
	}

	public boolean IsDeviceRegistered() {
		final SharedPreferences prefs = getGCMPreferences(context);
		return prefs.getBoolean(UserName, false);
	}

	public boolean isservicesAvilable() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this.context);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d(LocationUtils.APPTAG, "Google Play services is available");

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			android.app.Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode, this.context, 0);
			if (dialog != null) {
				dialog.show();
				// ErrorDialogFragment errorFragment = new
				// ErrorDialogFragment();
				// errorFragment.setDialog(dialog);
				// errorFragment.show(BaseActivity.getSupportFragmentManager(),
				// LocationUtils.APPTAG);
			}
			return false;
		}
	}

	public void TryRegisterDevice(boolean isforce) {

		if (IsDeviceRegistered() && !isforce)
			return;
		if (!isservicesAvilable())
			return;
		BackgroundProcess bp = new BackgroundProcess(this.context)
				.setProgressMessage("Getting Registration ID");
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings("rawtypes")
			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				Response response = (Response) arg0;
				if (response.status.equalsIgnoreCase("true"))
					ProcessDeviceRegistrationOnServer();
				else
					new Dialog(context).show(response.errormsg);

			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				// if (checkPlayServicesAvilability())
				// return new Response("Google Play service is not avilable!");

				return TrygetRegistrationID();
			}
		});
		bp.execute(null, null, null);
	}

	private void ProcessDeviceRegistrationOnServer() {
		BackgroundProcess bp = new BackgroundProcess(this.context)
				.setProgressMessage("sending registrationid on server");
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings("rawtypes")
			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				Response response = (Response) arg0;
				if (response.status.equalsIgnoreCase("true"))
					Toast.makeText(context, "Registered Sucessfully",
							Toast.LENGTH_LONG).show();
				else
					new Dialog(context).show(response.errormsg);
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				return TrySendRegistrationIDOnServer();
			}
		});
		bp.execute(null, null, null);
	}

	@SuppressWarnings({ "rawtypes" })
	private Response TrySendRegistrationIDOnServer() {
		try {
			String apiUrl = ApiUrl;
			DeviceRegisterModel gcm = new DeviceRegisterModel();
			gcm.RegistrationID = this.regid;
			gcm.UserName = this.UserName;
			gcm.MacAddress = "NA";

			apiUrl += Encode.ToObject(gcm);
			return server.getResponse(apiUrl);
		} catch (Exception ex) {
			return new Response(ex.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	private Response TrygetRegistrationID() {

		try {
			gcm = GoogleCloudMessaging.getInstance(context);
			regid = getRegistrationId(context);

			if (regid == null) {
				Log.i("Device registration!", "No Reg FOUND, Registering");
				return registerInBackground();
			} else if (regid.trim().length() < 1) {
				Toast.makeText(context, "No Reg FOUND, Registering",
						Toast.LENGTH_SHORT).show();
				return registerInBackground();
			} else {

				Log.w("REGID", regid);
				Log.i("REGID", regid);
				Response response = new Response();
				response.status = "true";
				return response;
			}
		} catch (Exception ex) {
			Log.e("getting Registration:", ex.toString());
			return registerInBackground();
		}
	}

	@SuppressWarnings("unused")
	private boolean checkPlayServicesAvilability() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode == ConnectionResult.SUCCESS)
			return true;
		else
			return false;
	}



	/*
	 * ------------------------------------------------- GOOGLE GCM METHODS
	 * ------------------------------------------------- PROJECT ID:
	 * chirag87-002 | PROJECT NO: 771053062953 API ID:
	 * AIzaSyBfuuJKmHX_XtuWp94ucmmg9MjmhRS8I9E
	 */
	// Check Google Play Services
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";

	String SENDER_ID = "401198413764";
	static final String TAG = "GCMDemo";

	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Activity context;
	String regid;

	private String getRegistrationId(Context context2) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");

		if (registrationId == null) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		if (registrationId.trim().length() < 1) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);

		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(
				DeviceRegistration.class.getSimpleName(), Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private Response registerInBackground() {

		Response response = new Response();
		String msg = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			regid = gcm.register(SENDER_ID);
			msg = "Device registered, registration ID=" + regid;
			storeRegistrationId(context, regid);
			response.status = "true";
			return response;
		} catch (IOException ex) {
			msg = "Error :" + ex.getMessage();
			Log.e("Device rgistration fail", msg);
			return new Response(msg);
		}
	}

	/*
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context application's context.
	 * 
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);

		editor.putBoolean(UserName, true);
		editor.commit();
	}

	/*
	 * ------------------------------------------------- OTHER METHODS
	 * -------------------------------------------------
	 */



	/*----------------------------------------------
	 * PACKAGE TRACKER
	 *----------------------------------------------- 
	 */

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Bundle extras = arg0.getExtras();
		Log.d("service", "onBind");
		// Get messager from the Activity
		if (extras != null) {
			Log.d("service", "onBind with extra");
			// Messenger outMessenger = (Messenger) extras.get("MESSENGER");
		}
		// return mBinder;
		return null;
	}
}
