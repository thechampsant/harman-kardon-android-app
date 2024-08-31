//package app.core.geoservice;
//
//import com.example.helpers.newone.R;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//
//import android.support.v4.app.FragmentActivity;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.ViewGroup;
//import app.core.base.InnosolsActivity;
//
//public class ViewMapActivity extends InnosolsActivity {
//
//	private static final String TAG = ViewMapActivity.class.getSimpleName();
//	private GoogleMap mMap;
//	private Marker marker;
//	public static String LATITUDE = "APP_LAT";
//	public static String LONGITUDE = "APP_LONG";
//	private String LAT = null;
//	private String LONG = null;
//
//
//	
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		try {
//			InvokeGPSService();
//			LAT = getIntent().getStringExtra(ViewMapActivity.LATITUDE);
//			LONG = getIntent().getStringExtra(ViewMapActivity.LONGITUDE);
//			setContentView(R.layout.viewmap);
//			setUpMapIfNeeded();
//		} catch (Exception ex) {
//			ShowToastLong(ex.getMessage(), 0);
//			Log.e(TAG,ex.getMessage());
//			this.finish();
//		}
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.viewmapactivity, menu);
//		return true;
//	}
//
//	private void setUpMapIfNeeded() {
//		// Do a null check to confirm that we have not already instantiated the
//		// map.
//		if (mMap == null) {
//			// Try to obtain the map from the SupportMapFragment.
//			mMap = ((SupportMapFragment) getSupportFragmentManager()
//					.findFragmentById(R.id.map)).getMap();
//			setOnPositionListener();
//			setUpMap(ToDouble(LAT),ToDouble(LONG));
//			mMap.setMyLocationEnabled(true);
//			mMap.setIndoorEnabled(true);
//			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//			// Check if we were successful in obtaining the map.
//			if (isGoogleMapsInstalled()) {
//				if (mMap != null) {
//					setUpMap(ToDouble(LAT),ToDouble(LONG));
//				}
//			} else {
//				Builder builder = new AlertDialog.Builder(this);
//				builder.setMessage("Install Google Maps");
//				builder.setCancelable(false);
//				builder.setPositiveButton("Install", getGoogleMapsListener());
//				AlertDialog dialog = builder.create();
//				dialog.show();
//			}
//		}
//	}
//
//	private void setUpMap(double lat, double log) {
//		 mMap.addMarker(new MarkerOptions().position(new
//		 LatLng(lat,log)));
//		this.setUpMarker();
//	}
//
//	private void setOnPositionListener() {
//		gps.setUpdateLocationListener(mMap, marker);
//	}
//
//	private void setUpMarker() {
//		gps.UpdateLocation(false);
//	}
//
//	public boolean isGoogleMapsInstalled() {
//		try {
//			ApplicationInfo info = getPackageManager().getApplicationInfo(
//					"com.google.android.apps.maps", 0);
//
//			return true;
//		} catch (PackageManager.NameNotFoundException e) {
//			return false;
//		}
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle item selection
//
//		if (item.getItemId() == R.id.action_geo) {
//			GetMyGeoLocation();
//			return true;
//		} else if (item.getItemId() == R.id.action_startupdates) {
//			startUpdateGeoLocation();
//			return true;
//		}
//		if (item.getItemId() == R.id.action_stopupdates) {
//			stopUpdateGeoLocation();
//			return true;
//		} else
//			return super.onOptionsItemSelected(item);
//	}
//
//	public void GetMyGeoLocation() {
//		this.setUpMap(ToDouble(gps.getlatitude()), ToDouble(gps.getlongitude()));
//	}
//
//	public void startUpdateGeoLocation() {
//		gps.startPeriodicUpdates();
//	}
//
//	public void stopUpdateGeoLocation() {
//		gps.stopPeriodicUpdates();
//	}
//
//	public OnClickListener getGoogleMapsListener() {
//		return new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(
//						Intent.ACTION_VIEW,
//						Uri.parse("market://details?id=com.google.android.apps.maps"));
//				startActivity(intent);
//
//				// Finish the activity so they can't circumvent the check
//				finish();
//			}
//		};
//	}
//
//	@Override
//	public void RegisterTableInfoForLocalDB() {
//		// TODO Auto-generated method stub
//
//	}
//
//}
