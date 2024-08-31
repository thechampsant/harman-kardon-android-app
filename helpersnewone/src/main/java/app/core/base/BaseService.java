package app.core.base;

import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import linq.ArrayList;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import app.core.services.LoginProvider;
import app.core.sqllite.DataEntity;
import app.core.sqllite.MySQLiteOpenHelper;
import app.core.sqllite.TableInfo;
import app.core.utils.Message;

public abstract class BaseService<T, E extends BaseService<T, E>> extends
		Service {


	LocalBroadcastManager uibroadcaster = null;
	private app.core.sqllite.MySQLiteOpenHelper ConnectDBDataContext;
	public static LoginProvider User = null;
	@SuppressWarnings("rawtypes")
	public static BaseService service = null;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(setThreadPoolSize());
	public ScheduledExecutorService getScheduler()
	{
		return this.scheduler;
	}
	public int setThreadPoolSize(){
		return 1;
	}
	
	public T db;

	public app.core.sqllite.MySQLiteOpenHelper getDataContextDb() {
		return ConnectDBDataContext;
	}

	public LocalBroadcastManager getUIBroadCaster() {
		return this.uibroadcaster;
	}

	public  void sendBroadCastToUI(Intent intent) {
		this.uibroadcaster.sendBroadcast(intent);
	}


	
	public void keepAlive() {
		scheduler.schedule(new Runnable() {
			public void run() {
				// beeperHandle.cancel(true);
			}
		}, 1, TimeUnit.MINUTES);
	}

	@SuppressWarnings("rawtypes")
	private void InvokeUser(BaseService service) {
		User = new LoginProvider(service);
		InitPreference();
	}

	@SuppressWarnings({ "static-access", "rawtypes" })
	public void Invoke(BaseService service) {
		this.service = service;
		uibroadcaster = LocalBroadcastManager.getInstance(this.service);
		InvokeUser(service);

	}

	@SuppressWarnings({ "static-access", "rawtypes" })
	public void Invoke(BaseService service, T _databaseLayer) {
		this.service = service;
		this.uibroadcaster = LocalBroadcastManager.getInstance(this.service);
		this.db = _databaseLayer;
		InvokeUser(service);

	}

	public void EnableLocalDatabase(String DBName, int version) {

		this.RegisterTableInfoForLocalDB();
		ConnectDBDataContext = new MySQLiteOpenHelper(this, DBName,version, _Tables);
		app.core.sqllite.DataEntity.db = ConnectDBDataContext;
	}

	/**
	 * Use this Method to Enable Local Database with Default Versioning.
	 * 
	 * @param DBName
	 *            The name of database for local storage
	 */
	public void EnableLocalDatabase(String DBName) {
		this.EnableLocalDatabase(DBName, 1);
	}

	public abstract void RegisterTableInfoForLocalDB();

	ArrayList<TableInfo> _Tables;

	public void RegisterTableInfo(TableInfo _tableinfo) {
		if (_Tables == null)
			_Tables = new ArrayList<TableInfo>();
		_Tables.add(_tableinfo);
	}

	public <T extends DataEntity<T>> void RegisterTableForDataEntity(T obj) {
		this.RegisterTableInfo(obj.GetTableInfo());
	}

	// Toast--------------

	public void ShowToast(String Message) {
		Toast toast = Toast.makeText(this, Message, Toast.LENGTH_SHORT);
		toast.show();
	}

	// Network avilability..............
	public boolean isNetworkAvailable() {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null)
			isConnected = activeNetworkInfo.isConnected();
		if (activeNetworkInfo != null && isConnected)
			return true;
		else
			return false;
	}

	public boolean isNetworkFoundToast() {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null)
			isConnected = activeNetworkInfo.isConnected();
		if (activeNetworkInfo != null && isConnected)
			return true;
		else {
			this.ShowToast(Message.NO_INTERNET_FOUND);
			return false;
		}
	}

	public boolean isNetworkFoundDialog() {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null)
			isConnected = activeNetworkInfo.isConnected();
		if (activeNetworkInfo != null && isConnected)
			return true;
		else {
			this.ShowToast(Message.NO_INTERNET_FOUND);
			return false;
		}
	}

	// prefrence.....
	SharedPreferences SP;
	SharedPreferences.Editor editor;

	@SuppressWarnings("deprecation")
	private void InitPreference() {
		SP = getApplicationContext().getSharedPreferences("GLOBAL",
				Context.MODE_PRIVATE);
		this.editor = SP.edit();
	}

	public boolean SavePreferences(String Key, String Value) {
		try {
			editor.putString(Key, Value);
			editor.commit();
			return true;
		} catch (Exception ex) {
			Log.e("Error:", ex.toString());
			return false;
		}
	}

	public String getPreference(String key, String defValue) {
		return SP.getString(key, defValue);
	}

	public boolean RemovePrefrences(String Key) {
		editor.remove(Key);
		return true;
	}
	
	// get hardware mac address
	public String getMACaddress() {
		// add following persimisin
		// <uses-permission android:name="android.permission.INTERNET" />
		// <uses-permission
		// android:name="android.permission.ACCESS_NETWORK_STATE" />
		String address = null;
		address = getMAC("wlan0"); // using wifi available
		if (address != null)
			return address;
		address = getMAC("eth0"); // using ethernet connection availale
		return address;
	}

	public String getMAC(String interfaceName) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName))
						continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac == null)
					return null;
				StringBuilder buf = new StringBuilder();
				for (int idx = 0; idx < mac.length; idx++)
					buf.append(String.format("%02X:", mac[idx]));
				if (buf.length() > 0)
					buf.deleteCharAt(buf.length() - 1);
				return buf.toString();
			}
		} catch (Exception ex) {
		}
		return null;

	}

	public String GetCurrentDateTimeInString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		String MyDate = dateFormat.format(date);
		return MyDate;
	}
	public String GetCurrentDateInString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String MyDate = dateFormat.format(date);
		return MyDate;
	}
	
	
}
