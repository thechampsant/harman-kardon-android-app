package app.core.utils;

import java.util.HashMap;
import java.util.Map;

import linq.ArrayList;
import android.app.Activity;
import android.content.Intent;
import app.core.model.Keys;

public class IntentFactory {
	static Map<String, Object> map = new HashMap<String, Object>();

	
	public static void putDataTab(int TabIndex)
	{
		putDataAsInt(Keys.TabIndex,TabIndex);
	}
	
	@SuppressWarnings("rawtypes")
	public static void putDataAsClass(String key,Class cls)
	{
		map.put(key,cls);
	}
	
	// setting data of array list
	public static <T> void putDataArrayList(String Key, ArrayList<T> data) {
		map.put(Key, data);
	}

	// setting single object data
	public static <T> void putData(String Key, T data) {

		ArrayList<T> temp = new ArrayList<T>();
		temp.add(data);
		map.put(Key, temp);
	}

	// setting single object data as string
	public static void putDataAsString(String Key, String Value) {

		map.put(Key, Value);
	}

	// setting single object data as boolean
	public static void putDataAsBoolean(String Key, boolean Value) {

		map.put(Key, Value);
	}

	// setting single object data as int
	public static void putDataAsInt(String Key, int Value) {
		map.put(Key, Value);
	}

	public static int getDataTab()
	{
		return getDataAsInt(Keys.TabIndex);
	}
	
	@SuppressWarnings("rawtypes")
	public static Class  getDataAsClass(String Key) {
		if (map.containsKey(Key)) {
			Class cls = (Class) map.get(Key);
			map.remove(Key);
			return cls;
		} else
			return null;
	}
	// getting single object data
	@SuppressWarnings("unchecked")
	public static <T> T getData(String Key) {
		if (map.containsKey(Key)) {
			ArrayList<T> data = (ArrayList<T>) map.get(Key);

			map.remove(Key);
			return data.First();
		} else
			return null;
	}

	// getting single object data as string
	public static String getDataAsString(String Key) {

		if (map.containsKey(Key)) {
			String value = (String) map.get(Key);
			map.remove(Key);
			return value;
		} else
			return null;
	}

	// getting single object data as string
	public static boolean getDataAsBoolean(String Key) {
		if (map.containsKey(Key)) {
			Boolean value = (Boolean) map.get(Key);
			map.remove(Key);
			return value;
		} else
			return false;
	}

	// getting single object data as string
	public static int getDataAsInt(String Key) {

		if (map.containsKey(Key)) {
			int value = (Integer) map.get(Key);

			map.remove(Key);
			return value;
		} else
			return -1;

	}
	

	// getting array list object data
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> getDataArrayList(String Key) {

		if (map.containsKey(Key)) {
			ArrayList<T> data = (ArrayList<T>) map.get(Key);

			map.remove(Key);
			return data;
		} else
			return null;
	}
	
	public static void restartTab(Activity context)
	{
		Intent i= new Intent(context,null);
		context.finish();
	}
	
}
