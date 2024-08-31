package app.core.base;
import java.util.HashMap;
import java.util.Map;
import linq.ArrayList;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.google.android.gms.analytics.Tracker;

import app.core.ibase.IDataIntent;
import app.core.model.Keys;
import app.core.services.ProductManager;
import app.core.tracker.ITracker;
import app.core.tracker.TrackerManager;

public  class ApplicationBase extends Application implements IDataIntent,ITracker {

	Map<String, Object> map = new HashMap<String, Object>();
	TrackerManager tracker=new TrackerManager(this);
	ProductManager productManager=new ProductManager();
	
	
	public void putDataTab(int TabIndex) {
		putDataAsInt(Keys.TabIndex, TabIndex);
	}

	@SuppressWarnings("rawtypes")
	public void putDataAsClass(String key, Class cls) {
		map.put(key, cls);
	}

	// setting data of array list
	public  <T> void putDataArrayList(String Key, ArrayList<T> data) {
		map.put(Key, data);
	}

	// setting single object data
	public  <T> void putData(String Key, T data) {

		ArrayList<T> temp = new ArrayList<T>();
		temp.add(data);
		map.put(Key, temp);
	}

	// setting single object data as string
	public  void putDataAsString(String Key, String Value) {

		map.put(Key, Value);
	}

	// setting single object data as boolean
	public  void putDataAsBoolean(String Key, boolean Value) {

		map.put(Key, Value);
	}

	// setting single object data as int
	public void putDataAsInt(String Key, int Value) {
		map.put(Key, Value);
	}

	public  int getDataTab() {
		return getDataAsInt(Keys.TabIndex);
	}

	@SuppressWarnings("rawtypes")
	public  Class getDataAsClass(String Key) {
		if (map.containsKey(Key)) {
			Class cls = (Class) map.get(Key);
			removeKey(Key);
			return cls;
		} else
			return null;
	}

	// getting single object data
	@SuppressWarnings("unchecked")
	public  <T> T getData(String Key) {
		if (map.containsKey(Key)) {
			ArrayList<T> data = (ArrayList<T>) map.get(Key);

			removeKey(Key);
			return data.First();
		} else
			return null;
	}

	// getting single object data as string
	public  String getDataAsString(String Key) {

		if (map.containsKey(Key)) {
			String value = (String) map.get(Key);
			removeKey(Key);
			return value;
		} else
			return null;
	}

	// getting single object data as string
	public  boolean getDataAsBoolean(String Key) {
		if (map.containsKey(Key)) {
			Boolean value = (Boolean) map.get(Key);
			removeKey(Key);
			return value;
		} else
			return false;
	}

	// getting single object data as string
	public  int getDataAsInt(String Key) {

		if (map.containsKey(Key)) {
			int value = (Integer) map.get(Key);

			removeKey(Key);
			return value;
		} else
			return -1;

	}

	// getting array list object data
	@SuppressWarnings("unchecked")
	public  <T> ArrayList<T> getDataArrayList(String Key) {

		if (map.containsKey(Key)) {
			ArrayList<T> data = (ArrayList<T>) map.get(Key);
			removeKey(Key);
			return data;
		} else
			return null;
	}

	public  void restartTab(Activity context) {
		Intent i = new Intent(context, null);
		context.finish();
	}
	
	private void removeKey(String Key)
	{
		map.remove(Key);
	}

	@Override
	public Tracker getTracker(String trackerId) {
		
		return tracker.getTracker(trackerId);
	}

	@Override
	public void enableLogger(int LogLevel) {
		
		this.tracker.enableLogger(LogLevel);
		
	}
	
	public ProductManager getProductManager()
	{
		return this.productManager;
	}


	
}
