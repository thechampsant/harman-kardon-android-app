package com.fieldforce.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class RawStorageProvider {
	public static final String PREFS_NAME = "Raghukaka";
	public static RawStorageProvider instance = null;
	private Context activity;

	public RawStorageProvider(Context _activity) {
		activity = _activity;
	}

	public static RawStorageProvider getInstance(Context activity) {
		if (instance == null)
			instance = new RawStorageProvider(activity);
		return instance;
	}

	public void dumpDataToStorage(String storageCallName, String dataToSave) {
		SharedPreferences storage = activity
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = storage.edit();
		editor.putString(storageCallName, dataToSave);

		editor.commit();
	}

	public void dumpDataToStorage(String storageCallName, boolean dataToSave) {
		SharedPreferences storage = activity
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = storage.edit();
		editor.putBoolean(storageCallName, dataToSave);
		editor.commit();
	}

	public void dumpDataToStorage(String storageCallName, int dataToSave) {
		SharedPreferences storage = activity
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = storage.edit();
		editor.putInt(storageCallName, dataToSave);
		editor.commit();
	}

	public String getDataFromStorage(String storageCallName) {
		SharedPreferences storage = activity
				.getSharedPreferences(PREFS_NAME, 0);
		String data = storage.getString(storageCallName, null);

		return data;
	}

	public boolean getBooleanFromStorage(String storageCallName) {
		SharedPreferences storage = activity
				.getSharedPreferences(PREFS_NAME, 0);
		boolean data = storage.getBoolean(storageCallName, false);
		return data;
	}

	public int getNumberFromStorage(String storageCallName) {
		SharedPreferences storage = activity
				.getSharedPreferences(PREFS_NAME, 0);
		int data = storage.getInt(storageCallName, 0);
		return data;
	}

	public void delete(String storageCallName) {
		SharedPreferences storage = activity
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = storage.edit();
		editor.remove(storageCallName);
		editor.commit();
	}
}
