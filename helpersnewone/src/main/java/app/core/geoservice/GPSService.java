///*
// * Copyright (C) 2013 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package app.core.geoservice;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesClient;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import android.content.IntentSender;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.wifi.WifiManager;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.Settings;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.support.v4.app.DialogFragment;
//import android.util.Log;
//import android.view.View;
//
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
///**
// * This the app's main Activity. It provides buttons for requesting the various
// * features of the app, displays the current location, the current address, and
// * the status of the location client and updating services.
// *
// * {@link #getLocation} gets the current location using the Location Services
// * getLastLocation() function. {@link #getAddress} calls geocoding to get a
// * street address for the current location. {@link #startUpdates} sends a
// * request to Location Services to send periodic location updates to the
// * Activity. {@link #stopUpdates} cancels previous periodic update requests.
// *
// * The update interval is hard-coded to be 5 seconds.
// */
//public class GPSService implements LocationListener, ILocationUpdate,
//		GoogleApiClient.ConnectionCallbacks,
//		GoogleApiClient.OnConnectionFailedListener {
//	private boolean IsGPSSupported = false;
//	private boolean IsWifiSupported = false;
//	private boolean IsNetworkSupported = false;
//	private boolean isGPSEnabled = false;
//	private boolean IsWifiEnabled = false;
//
//	// flag for network status
//	private boolean isNetworkEnabled = false;
//	// A request to connect to Location Services
//	private LocationRequest mLocationRequest;
//
//	// Stores the current instantiation of the location client in this object
//	private LocationClient mLocationClient;
//	private LocationManager locationManager;
//	// Handle to SharedPreferences for this app
//	SharedPreferences mPrefs;
//
//	// Handle to a SharedPreferences editor
//	SharedPreferences.Editor mEditor;
//
//	/*
//	 * Note if updates have been turned on. Starts out as "false"; is set to
//	 * "true" in the method handleRequestSuccess of LocationUpdateReceiver.
//	 */
//	boolean mUpdatesRequested = false;
//
//	/*
//	 * Initialize the Activity
//	 */
//
//	private Activity BaseActivity;
//
//	public GPSService(Activity _activity) {
//		BaseActivity = _activity;
//		StartConnection();
//		CheckForGPSandNetwork();
//		// this.startPeriodicUpdates();
//	}
//
//	private void StartConnection() {
//
//		locationManager = (LocationManager) BaseActivity
//				.getSystemService("location");
//
//		// Create a new global location parameters object
//
//		mLocationRequest = LocationRequest.create();
//
//		/*
//		 * Set the update interval
//		 */
//		mLocationRequest
//				.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
//
//		// Use high accuracy
//		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//		// Set the interval ceiling to one minute
//		mLocationRequest
//				.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
//
//		// Note that location updates are off until the user turns them on
//		mUpdatesRequested = false;
//
//		// Open Shared Preferences
//		mPrefs = BaseActivity.getSharedPreferences(
//				LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
//
//		// Get an editor
//		mEditor = mPrefs.edit();
//
//		/*
//		 * Create a new location client, using the enclosing class to handle
//		 * callbacks.
//		 */
//		mLocationClient = new LocationClient(BaseActivity, this, this);
//		mLocationClient.connect();
//	}
//
//	/**
//	 * Verify that Google Play services is available before making a request.
//	 *
//	 * @return true if Google Play services is available, otherwise false
//	 */
//	public boolean servicesConnected() {
//
//		// Check that Google Play services is available
//		int resultCode = GooglePlayServicesUtil
//				.isGooglePlayServicesAvailable(BaseActivity);
//
//		// If Google Play services is available
//		if (ConnectionResult.SUCCESS == resultCode) {
//			// In debug mode, log the status
//			Log.d(LocationUtils.APPTAG, "Google Play services is available");
//
//			// Continue
//			return true;
//			// Google Play services was not available for some reason
//		} else {
//			// Display an error dialog
//			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
//					BaseActivity, 0);
//			if (dialog != null) {
//				dialog.show();
//				// ErrorDialogFragment errorFragment = new
//				// ErrorDialogFragment();
//				// errorFragment.setDialog(dialog);
//				// errorFragment.show(BaseActivity.getSupportFragmentManager(),
//				// LocationUtils.APPTAG);
//			}
//			return false;
//		}
//	}
//
//	private void CheckForGPSandNetwork() {
//		try {
//
//			PackageManager pm = this.BaseActivity.getPackageManager();
//			IsGPSSupported = pm
//					.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
//			IsNetworkSupported = pm
//					.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK);
//
//			IsWifiSupported = pm.hasSystemFeature(PackageManager.FEATURE_WIFI);
//
//			isNetworkEnabled = locationManager
//					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//			isGPSEnabled = locationManager
//					.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//			if (IsWifiSupported) {
//				WifiManager wifi = (WifiManager) BaseActivity
//						.getSystemService(Context.WIFI_SERVICE);
//				if (wifi != null)
//					IsWifiEnabled = wifi.isWifiEnabled();
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	public boolean canGetLocation() {
//		if (IsWifiSupported && this.IsWifiEnabled)
//			return true;
//		else if (!IsGPSSupported && !IsNetworkSupported)
//			return true;
//		else if (!IsGPSSupported && !isNetworkEnabled)
//			return false;
//		else if (!IsNetworkSupported && !isGPSEnabled)
//			return false;
//		else
//			return true;
//
//	}
//
//	public boolean IsGPSActive() {
//		boolean value = canGetLocation();
//		if (!value) {
//			showSettingsAlert();
//		}
//		return value;
//	}
//
//	public void showSettingsAlert() {
//		AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity);
//
//		// Setting Dialog Title
//		alertDialog.setTitle("Location Settings");
//
//		// Setting Dialog Message
//		alertDialog
//				.setMessage("GPS is not enabled. Do you want to go to location settings menu?");
//
//		// On pressing Settings button
//		alertDialog.setPositiveButton("Settings",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						Intent intent = new Intent(
//								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//						BaseActivity.startActivity(intent);
//					}
//				});
//
//		// on pressing cancel button
//		alertDialog.setNegativeButton("Cancel",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.cancel();
//					}
//				});
//
//		// Showing Alert Message
//		alertDialog.show();
//
//	}
//
//	/**
//	 * Invoked by the "Get Location" button.
//	 *
//	 * Calls getLastLocation() to get the current location
//	 *
//	 * @param v
//	 *            The view object associated with this method, in this case a
//	 *            Button.
//	 */
//	public void getLocation(View v) {
//
//		// If Google Play Services is available
//		if (servicesConnected()) {
//
//			// Get the current location
//			Location currentLocation = mLocationClient.getLastLocation();
//
//			// Display the current location in the UI
//			// mLatLng.setText(LocationUtils.getLatLng(BaseActivity,
//			// currentLocation));
//		}
//	}
//
//	private String lat = "0.0";
//	private String log = "0.0";
//
//	public void setLocationCords() {
//
//		try {
//			// If Google Play Services is available
//			if (servicesConnected()) {
//
//				// Get the current location
//				Location currentLocation = mLocationClient.getLastLocation();
//
//				// Display the current location in the UI
//				String loc = LocationUtils.getLatLng(currentLocation);
//				lat = loc.split(",")[0].trim();
//				log = loc.split(",")[1].trim();
//				latitude = Double.parseDouble(lat);
//				longitude = Double.parseDouble(log);
//			}
//		} catch (Exception ex) {
//			lat = "0.0";
//			log = "0.0";
//		}
//	}
//
//	private boolean IsNewAccess = false;
//
//	public String getlatitude() {
//		if (!IsNewAccess) {
//			IsNewAccess = true;
//			setLocationCords();
//			return lat;
//		} else {
//			IsNewAccess = false;
//			return lat;
//		}
//
//	}
//
//	public String getlongitude() {
//		if (!IsNewAccess) {
//			IsNewAccess = true;
//			setLocationCords();
//			return log;
//		} else {
//			IsNewAccess = false;
//			return log;
//		}
//	}
//
//	public String GetLatLongString() {
//		return "latitude:" + lat + "\n" + "longitude:" + log + " are captured!";
//	}
//
//	/**
//	 * Invoked by the "Get Address" button. Get the address of the current
//	 * location, using reverse geocoding. This only works if a geocoding service
//	 * is available.
//	 *
//	 * @param v
//	 *            The view object associated with this method, in this case a
//	 *            Button.
//	 */
//	// For Eclipse with ADT, suppress warnings about Geocoder.isPresent()
//	@SuppressLint("NewApi")
//	public void getAddress(View v) {
//
//		// In Gingerbread and later, use Geocoder.isPresent() to see if a
//		// geocoder is available.
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
//				&& !Geocoder.isPresent()) {
//			// No geocoder is present. Issue an error message
//			Toast.makeText(BaseActivity,
//					"Cannot get address. No geocoder available",
//					Toast.LENGTH_LONG).show();
//			return;
//		}
//
//		if (servicesConnected()) {
//
//			// Get the current location
//			Location currentLocation = mLocationClient.getLastLocation();
//
//			// Turn the indefinite activity indicator on
//			// mActivityIndicator.setVisibility(View.VISIBLE);
//
//			// Start the background task
//			(new GPSService.GetAddressTask(BaseActivity))
//					.execute(currentLocation);
//		}
//	}
//
//	// public void getAddress() {
//	//
//	// // In Gingerbread and later, use Geocoder.isPresent() to see if a
//	// // geocoder is available.
//	// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
//	// && !Geocoder.isPresent()) {
//	// // No geocoder is present. Issue an error message
//	// Toast.makeText(BaseActivity,
//	// "Cannot get address. No geocoder available",
//	// Toast.LENGTH_LONG).show();
//	// return;
//	// }
//	//
//	// if (servicesConnected()) {
//	//
//	// // Get the current location
//	// Location currentLocation = mLocationClient.getLastLocation();
//	//
//	// // Turn the indefinite activity indicator on
//	// // mActivityIndicator.setVisibility(View.VISIBLE);
//	//
//	// // Start the background task
//	// (new GPSService.GetAddressTask(BaseActivity))
//	// .execute(currentLocation);
//	// }
//	// }
//
//	/**
//	 * Invoked by the "Start Updates" button Sends a request to start location
//	 * updates
//	 *
//	 * @param v
//	 *            The view object associated with this method, in this case a
//	 *            Button.
//	 */
//	public void startUpdates(View v) {
//		mUpdatesRequested = true;
//
//		if (servicesConnected()) {
//			startPeriodicUpdates();
//		}
//	}
//
//	/**
//	 * Invoked by the "Stop Updates" button Sends a request to remove location
//	 * updates request them.
//	 *
//	 * @param v
//	 *            The view object associated with this method, in this case a
//	 *            Button.
//	 */
//	public void stopUpdates(View v) {
//		mUpdatesRequested = false;
//
//		if (servicesConnected()) {
//			stopPeriodicUpdates();
//		}
//	}
//
//	/*
//	 * Called by Location Services when the request to connect the client
//	 * finishes successfully. At this point, you can request the current
//	 * location or start periodic updates
//	 */
//	@Override
//	public void onConnected(Bundle bundle) {
//
//		if (mUpdatesRequested) {
//			startPeriodicUpdates();
//		}
//	}
//
//	@Override
//	public void onConnectionSuspended(int i) {
//
//	}
//
//
//	/*
//	 * Called by Location Services if the attempt to Location Services fails.
//	 */
//	@Override
//	public void onConnectionFailed(ConnectionResult connectionResult) {
//
//		/*
//		 * Google Play services can resolve some errors it detects. If the error
//		 * has a resolution, try sending an Intent to start a Google Play
//		 * services activity that can resolve error.
//		 */
//		if (connectionResult.hasResolution()) {
//			try {
//
//				// Start an Activity that tries to resolve the error
//				connectionResult.startResolutionForResult(BaseActivity,
//						LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
//
//				/*
//				 * Thrown if Google Play services canceled the original
//				 * PendingIntent
//				 */
//
//			} catch (IntentSender.SendIntentException e) {
//
//				// Log the error
//				e.printStackTrace();
//			}
//		} else {
//
//			// If no resolution is available, display a dialog to the user with
//			// the error.
//			showErrorDialog(connectionResult.getErrorCode());
//		}
//	}
//
//	public double latitude = 0.0;
//	public double longitude = 0.0;
//
//	/**
//	 * Report location updates to the UI.
//	 *
//	 * @param location
//	 *            The updated location.
//	 */
//	@Override
//	public void onLocationChanged(Location location) {
//
//		// Report to the UI that the location was updated
//		// mConnectionStatus.setText(R.string.location_updated);
//		UpdateLocation(true);
//		// In the UI, set the latitude and longitude to the value received
//		// mLatLng.setText(LocationUtils.getLatLng(BaseActivity, location));
//	}
//
//	private Marker _Marker = null;
//
//	public void UpdateLocation(boolean IsActiveLoad) {
//		try {
//			if (!IsActiveLoad) {
//				this.latitude = Double.parseDouble(this.getlatitude());
//				this.longitude = Double.parseDouble(this.getlongitude());
//			}
//			LatLng loc = new LatLng(latitude, longitude);
//			MarkerOptions a = new MarkerOptions().position(loc).title(
//					addressText);
//			if (_Marker != null)
//				_Marker.remove();
//			_Marker = _mMap.addMarker(a);
//			_Marker.setPosition(loc);
//
//		} catch (Exception ex) {
//			Log.e("Update location error:","");
//		}
//	}
//
//	/**
//	 * In response to a request to start updates, send a request to Location
//	 * Services
//	 */
//	protected class PositionUpdater extends AsyncTask<Void, Void, Void> {
//		int i = 0;
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			while (i < 1) {
//
//				try {
//					Thread.sleep(Long.parseLong("10"));
//					// latitude = Double.parseDouble(Getlatitude());
//					// longitude = Double.parseDouble(Getlongitude());
//					getAddress();
//
//					i++;
//				} catch (NumberFormatException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					i++;
//					break;
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					i++;
//					break;
//				}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void sd) {
//
//			// Turn off the progress bar
//			// mActivityIndicator.setVisibility(View.GONE);
//			UpdateLocation(true);
//			_PositionUpdater = new PositionUpdater();
//			_PositionUpdater.execute((Void) null);
//			// Set the address in the UI
//			// mAddress.setText(address);
//		}
//
//	}
//
//	private PositionUpdater _PositionUpdater;
//
//	public void startPeriodicUpdates() {
//
//		try {
//			mLocationClient.requestLocationUpdates(mLocationRequest, this);
//			_PositionUpdater = new PositionUpdater();
//			_PositionUpdater.execute((Void) null);
//		} catch (Exception ex) {
//			Toast.makeText(BaseActivity, ex.getMessage(), Toast.LENGTH_LONG)
//					.show();
//		}
//
//		// handler= new Handler(BaseActivity.getMainLooper());
//		// handler.post(_PositionUpdater);
//
//		// mConnectionState.setText(R.string.location_requested);
//	}
//
//	/**
//	 * In response to a request to stop updates, send a request to Location
//	 * Services
//	 */
//	public void stopPeriodicUpdates() {
//		try {
//			mLocationClient.removeLocationUpdates(this);
//			// handler.removeCallbacksAndMessages(_PositionUpdater);
//			_PositionUpdater.cancel(true);
//			// mConnectionState.setText(R.string.location_updates_stopped);
//
//		} catch (Exception ex) {
//			Toast.makeText(BaseActivity, ex.getMessage(), Toast.LENGTH_LONG)
//					.show();
//		}
//	}
//
//	String addressText = "No address found!";
//
//	private void getAddress() {
//
//		/*
//		 * Get a new geocoding service instance, set for localized addresses.
//		 * This example uses android.location.Geocoder, but other geocoders that
//		 * conform to address standards can also be used.
//		 */
//		Geocoder geocoder = new Geocoder(BaseActivity, Locale.getDefault());
//
//		// Create a list to contain the result address
//		List<Address> addresses = null;
//
//		// Try to get an address for the current location. Catch IO or
//		// network problems.
//		try {
//
//			/*
//			 * Call the synchronous getFromLocation() method with the latitude
//			 * and longitude of the current location. Return at most 1 address.
//			 */
//			addresses = geocoder.getFromLocation(
//					Double.parseDouble(getlatitude()), longitude, 1);
//
//			// Catch network or other I/O problems.
//		} catch (IOException exception1) {
//
//			// Log an error and return an error message
//			Log.e(LocationUtils.APPTAG,
//					"IO Exception in Geocoder.getFromLocation()");
//
//			// print the stack trace
//			exception1.printStackTrace();
//
//			// Return an error message
//			addressText = "IO Exception in Geocoder.getFromLocation()";
//
//			// Catch incorrect latitude or longitude values
//		} catch (IllegalArgumentException exception2) {
//
//			// Construct a message containing the invalid arguments
//			String errorString = "Illegal arguments: Latitude %1$.8f Longitude %2$.8f"
//					+ this.lat + this.log;
//			// Log the error and print the stack trace
//			Log.e(LocationUtils.APPTAG, errorString);
//			exception2.printStackTrace();
//			addressText = errorString;
//			//
//			return;
//		}
//		// If the reverse geocode returned an address
//		if (addresses != null && addresses.size() > 0) {
//
//			// Get the first address
//			Address address = addresses.get(0);
//
//			// Format the first line of address
//			addressText =
//
//			// If there's a street address, add it
//			(address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0)
//					: "") +
//
//			// Locality is usually a city
//					address.getLocality() +
//
//					// The country of the address
//					address.getCountryName();
//
//			// Return the text
//
//			// If there aren't any addresses, post a message
//		} else {
//			addressText = "No address found for location";
//		}
//	}
//
//	/**
//	 * An AsyncTask that calls getFromLocation() in the background. The class
//	 * uses the following generic types: Location - A
//	 * {@link android.location.Location} object containing the current location,
//	 * passed as the input parameter to doInBackground() Void - indicates that
//	 * progress units are not used by this subclass String - An address passed
//	 * to onPostExecute()
//	 */
//	protected class GetAddressTask extends AsyncTask<Location, Void, String> {
//
//		// Store the context passed to the AsyncTask when the system
//		// instantiates it.
//		Context localContext;
//
//		// Constructor called by the system to instantiate the task
//		public GetAddressTask(Context context) {
//
//			// Required by the semantics of AsyncTask
//			super();
//
//			// Set a Context for the background task
//			localContext = context;
//		}
//
//		/**
//		 * Get a geocoding service instance, pass latitude and longitude to it,
//		 * format the returned address, and return the address to the UI thread.
//		 */
//		@Override
//		protected String doInBackground(Location... params) {
//			/*
//			 * Get a new geocoding service instance, set for localized
//			 * addresses. This example uses android.location.Geocoder, but other
//			 * geocoders that conform to address standards can also be used.
//			 */
//			Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());
//
//			// Get the current location from the input parameter list
//			Location location = params[0];
//
//			// Create a list to contain the result address
//			List<Address> addresses = null;
//
//			// Try to get an address for the current location. Catch IO or
//			// network problems.
//			try {
//
//				/*
//				 * Call the synchronous getFromLocation() method with the
//				 * latitude and longitude of the current location. Return at
//				 * most 1 address.
//				 */
//				addresses = geocoder.getFromLocation(location.getLatitude(),
//						location.getLongitude(), 1);
//
//				// Catch network or other I/O problems.
//			} catch (IOException exception1) {
//
//				// Log an error and return an error message
//				Log.e(LocationUtils.APPTAG,
//						"IO Exception in Geocoder.getFromLocation()");
//
//				// print the stack trace
//				exception1.printStackTrace();
//
//				// Return an error message
//				return ("IO Exception in Geocoder.getFromLocation()");
//
//				// Catch incorrect latitude or longitude values
//			} catch (IllegalArgumentException exception2) {
//
//				// Construct a message containing the invalid arguments
//				String errorString = "Illegal arguments: Latitude %1$.8f Longitude %2$.8f"
//						+ location.getLatitude() + location.getLongitude();
//				// Log the error and print the stack trace
//				Log.e(LocationUtils.APPTAG, errorString);
//				exception2.printStackTrace();
//
//				//
//				return errorString;
//			}
//			// If the reverse geocode returned an address
//			if (addresses != null && addresses.size() > 0) {
//
//				// Get the first address
//				Address address = addresses.get(0);
//
//				// Format the first line of address
//				String addressText = "%1$s %2$s %3$s" +
//
//				// If there's a street address, add it
//						(address.getMaxAddressLineIndex() > 0 ? address
//								.getAddressLine(0) : "") +
//
//						// Locality is usually a city
//						address.getLocality() +
//
//						// The country of the address
//						address.getCountryName();
//
//				// Return the text
//				return addressText;
//
//				// If there aren't any addresses, post a message
//			} else {
//				return "No address found for location";
//			}
//		}
//
//		/**
//		 * A method that's called once doInBackground() completes. Set the text
//		 * of the UI element that displays the address. This method runs on the
//		 * UI thread.
//		 */
//		@Override
//		protected void onPostExecute(String address) {
//
//			// Turn off the progress bar
//			// mActivityIndicator.setVisibility(View.GONE);
//
//			// Set the address in the UI
//			// mAddress.setText(address);
//		}
//	}
//
//	/**
//	 * Show a dialog returned by Google Play services for the connection error
//	 * code
//	 *
//	 * @param errorCode
//	 *            An error code returned from onConnectionFailed
//	 */
//	private void showErrorDialog(int errorCode) {
//
//		// Get the error dialog from Google Play services
//		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
//				BaseActivity,
//				LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
//
//		// If Google Play services can provide an error dialog
//		if (errorDialog != null) {
//
//			// Create a new DialogFragment in which to show the error dialog
//			ErrorDialogFragment errorFragment = new ErrorDialogFragment();
//
//			// Set the dialog in the DialogFragment
//			errorFragment.setDialog(errorDialog);
//
//			// Show the error dialog in the DialogFragment
//			// errorFragment.show(getSupportFragmentManager(),
//			// LocationUtils.APPTAG);
//		}
//	}
//
//	/**
//	 * Define a DialogFragment to display the error dialog generated in
//	 * showErrorDialog.
//	 */
//	public static class ErrorDialogFragment extends DialogFragment {
//
//		// Global field to contain the error dialog
//		private Dialog mDialog;
//
//		/**
//		 * Default constructor. Sets the dialog field to null
//		 */
//		public ErrorDialogFragment() {
//			super();
//			mDialog = null;
//		}
//
//		/**
//		 * Set the dialog to display
//		 *
//		 * @param dialog
//		 *            An error dialog
//		 */
//		public void setDialog(Dialog dialog) {
//			mDialog = dialog;
//		}
//
//		/*
//		 * This method must return a Dialog to the DialogFragment.
//		 */
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			return mDialog;
//		}
//	}
//
//	private GoogleMap _mMap;
//
//	@Override
//	public void setUpdateLocationListener(GoogleMap mMap, Marker marker) {
//		// TODO Auto-generated method stub
//		_mMap = mMap;
//		_Marker = marker;
//	}
//}
