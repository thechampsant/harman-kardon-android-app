package com.fieldforce.gpscoord;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.CheckoutModel;
import mob.field.harmonkardonff.entitiymodels.GPSWifiPropertyModel;
import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.entitiymodels.MyInfoModel;
import mob.field.harmonkardonff.services.MobilePhoneSignal;
import mob.field.harmonkardonff.services.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import app.core.base.BaseService;
import app.core.server.Encode;

import com.fieldforce.asyntask.GPSAsyTaskBackground;
import com.fieldforce.asyntask.GPSCallBackBackgroundTask;
import com.fieldforce.harmonkardonff.NetworkNotAvailableActivity;
import com.fieldforce.harmonkardonff.TurnGpsOnActivity;
import com.fieldforce.harmonhelper.GPSTracker;
import com.fieldforce.utility.Storage;

public class SchedulerEventService extends BaseService {

	static GPSTracker gpstracker;
	Handler h = new Handler(Looper.getMainLooper());
	public static SchedulerEventService Current;
	MobilePhoneSignal myPhoneStateListener;
	WifiManager wifiManager;
	static Context context;
	private String batterystatus = "";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (gpstracker.getLocationObject() == null) {
			Log.i("test", "gpsnull");
			restartGpsVariable();
		}
		getbatterystatus();
		ShowToast("hitting service comio");
	//	hitapi();
		return Service.START_STICKY;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("APP_TAG", "reachedservice");
		this.Invoke(this);
		this.EnableLocalDatabase("MyyDbb0001",1);
		Current = SchedulerEventService.this;
		context = getApplicationContext();
		gpstracker = new GPSTracker(context);
		// plz change db in Main Activity also//
		wifiManager = (WifiManager) getApplicationContext().getSystemService(
				Context.WIFI_SERVICE);
		myPhoneStateListener = new MobilePhoneSignal();
		TelephonyManager tel = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		tel.listen(myPhoneStateListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

	
		
		
		// TODO Auto-generated method stub
	}

	private void hitapi() {
		if (isAttMarkedToday() && !isCheckoutMarked() && !checkgps()) {
			Intent i = new Intent(SchedulerEventService.this,
					TurnGpsOnActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			SchedulerEventService.this.startActivity(i);
		} else if (isAttMarkedToday() && !isCheckoutMarked()
				&& !isNetworkAvailable()) {
			Intent i = new Intent(SchedulerEventService.this,
					NetworkNotAvailableActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			SchedulerEventService.this.startActivity(i);
		}
		if (gpstracker.getLocationObject() == null) {
			Log.i("test", "gpsnull");
			restartGpsVariable();
		}
		if (isAttMarkedToday() && !isCheckoutMarked()) {
			double latitude = gpstracker.getLatitude();
			double longitude = gpstracker.getLongitude();
			GPSWifiPropertyModel gpsWifiPrModel = loadData(
					Double.toString(latitude), Double.toString(longitude));
			// getbatterystatus();
			if (latitude == 0.0 || longitude == 0.0 || !checkgps()) {
				gpsWifiPrModel.Latitude = null;
				gpsWifiPrModel.Longitude = null;
				gpsWifiPrModel.CheckIn_Latitude = null;
				gpsWifiPrModel.CheckIn_Longitude = null;
			}
			saveToLocalDB(gpsWifiPrModel);

			webcallTosendLocation(gpsWifiPrModel);

		}

	}

	protected void saveToLocalDB(GPSWifiPropertyModel gpsWifiPrModel) {
		// gpsWifiPrModel.CheckIn_Latitude = null;
		// gpsWifiPrModel.CheckIn_Longitude = null;
		// gpsWifiPrModel.Latitude = null;
		// gpsWifiPrModel.Longitude = null; Will get the last location when
		// commented
		gpsWifiPrModel.InsertOrUpdate();

	}

	private void webcallTosendLocation(GPSWifiPropertyModel gpsWifiObj) {
		if (gpsWifiObj == null)
			return;
		ArrayList<MyInfoModel> arrMyinfoModel = getDataContextDb()
				.FetchAllData(new MyInfoModel());
		if (arrMyinfoModel == null || arrMyinfoModel.size() == 0)
			return;
		String userName = arrMyinfoModel.First().UserID;
		gpsWifiObj.UserName = userName;
		try {
			gpsWifiObj.version = Current.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		gpsWifiObj.imei = Storage.getPhoneIMEI(this);

		String gpsLocation = "UpdateMyLocation/?";
		String apiUrl = WebService.ApiUrl + gpsLocation;
		apiUrl += Encode.ToObject(gpsWifiObj);
		if (!isNetworkAvailable())
			return;
		GPSAsyTaskBackground commAsyBg = new GPSAsyTaskBackground(apiUrl,
				gpsWifiObj);
		commAsyBg.setBackgroundTask(new GPSCallBackBackgroundTask() {

			@Override
			public void callBackBackgroundTask(JSONObject jobj,
					GPSWifiPropertyModel gpsWifiObj) {
				try {
					if (jobj != null
							&& jobj.getString("status")
									.equalsIgnoreCase("true")) {
						if (gpsWifiObj.isLocal.equalsIgnoreCase("true"))
							gpsWifiObj.Delete();
						updateOnServer();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		commAsyBg.execute();
	}

	private GPSWifiPropertyModel loadData(String latitude, String longitude) {
		GPSWifiPropertyModel gpsWifiPrModel = new GPSWifiPropertyModel();
		try {
			int mobileStrngth = myPhoneStateListener.signalStrengthPercent;
			int wifiStrngth = getPercentage(getWifiStrength());

			gpsWifiPrModel.networkStrngth = mobileStrngth + "";
			gpsWifiPrModel.wifiStrngth = wifiStrngth + "";
			gpsWifiPrModel.batterystatus = batterystatus;
			gpsWifiPrModel.gpsOn = checkgps() + "";
			gpsWifiPrModel.Latitude = latitude + "";
			gpsWifiPrModel.Longitude = longitude + "";
			MDAT mdat = loadTodayAttendanceFromLocal();
			if (mdat != null) {
				gpsWifiPrModel.CheckIn_Latitude = mdat.Latitude;
				gpsWifiPrModel.CheckIn_Longitude = mdat.Longitude;
			}
			gpsWifiPrModel.DeviceTime = GetCurrentDateTimeInString();
		} catch (Exception e) {
			ShowToast("GPSWifiPropertyModel creation exception!!");
		}
		return gpsWifiPrModel;
	}

	private int getWifiStrength() {

		int linkSpeed = wifiManager.getConnectionInfo().getRssi();
		Log.i("test", "wifiink");
		return linkSpeed;
	}

	private int getPercentage(int wifiStrength) {
		if (wifiStrength == -200) {
			return 0;
		} else if (wifiStrength >= -100 && wifiStrength <= 0) {
			return 100 + wifiStrength;
		}
		return 0;
	}

	protected void updateOnServer() {
		ArrayList<GPSWifiPropertyModel> arrGPSWifiProp = getDataContextDb()
				.FetchAllData(new GPSWifiPropertyModel());
		webcallTosendLocation(arrGPSWifiProp.First());
	}

	public void restartGpsVariable() {
		h.post(new Runnable() {
			public void run() {
				Log.i("test", "checkgps");
				gpstracker.stopUsingGPS();
				gpstracker.refreshGps();
			}
		});
	}

	private boolean isAttMarkedToday() {
		return isPresent(loadTodayAttendanceFromLocal());
	}

	private boolean isPresent(MDAT mdatArr) {
		if (mdatArr != null && mdatArr.option.equalsIgnoreCase("P")) {
			return true;
		}
		return false;
	}

	private MDAT loadTodayAttendanceFromLocal() {
		ArrayList<MDAT> mdatArr = getDataContextDb().FetchAllData(new MDAT());
		if (mdatArr != null) {
			ArrayList<MDAT> mdatArr1 = mdatArr.where("ForDate",
					GetCurrentDateInString()).where("UserName",
					User.GetUserName());
			if (mdatArr1 != null && mdatArr1.size() > 0) {
				Log.i("test", "loadtodayattnd");
				return mdatArr1.Last();

			} else
				return null;
		}
		return null;
	}

	private boolean isCheckoutMarked() {
		try {
			ArrayList<CheckoutModel> checkoutArr = getDataContextDb()
					.FetchAllData(new CheckoutModel());
			if (checkoutArr != null) {
				ArrayList<CheckoutModel> checkoutArr1 = checkoutArr.where(
						"ForDate", GetCurrentDateInString()).where("UserName",
						User.GetUserName());
				if (checkoutArr1 != null && checkoutArr1.size() > 0) {
					return true;
				} else
					return false;
				// mdatArr.where("username", ""+User.GetUserName());
			} else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	protected LocationManager locationManager;

	public boolean checkgps() {

		boolean isGPSEnabled = false;
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return isGPSEnabled;
	}

	private void getbatterystatus() {
		// TODO Auto-generated method stub
		SchedulerEventService.this.registerReceiver(
				SchedulerEventService.this.mBatInfoReceiver, new IntentFilter(
						Intent.ACTION_BATTERY_CHANGED));
	}

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			// contentTxt.setText(String.valueOf(level) + "%");
			batterystatus = String.valueOf(level);
		}
	};

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ShowToast("service hogyi destroy");
	}
@Override
public void onTaskRemoved(Intent rootIntent) {
	// TODO Auto-generated method stub
	super.onTaskRemoved(rootIntent);
	Log.d("APP_TAG","ontasktriggered");
//	Intent intent = new Intent( this, testactivity.class );
//	   intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
//	   startActivity( intent );
}
}
