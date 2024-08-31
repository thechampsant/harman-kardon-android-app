package com.fieldforce.harmonkardonff;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
	public static final String MESSAGE ="message";
	public static final String COUNT ="count";
	public static final String HEADERID ="header";
	public static final String REGID ="regid";
	public static final String DATE ="date";
	private Context activity;
	
	public LocalStorage(Context activity){
		this.activity = activity;
	}

	public void setMessage(String key,String value){
		SharedPreferences sharedPreference = activity.getSharedPreferences("MyPreference", 0);
			
		   SharedPreferences.Editor editor = sharedPreference.edit();
		   editor.putString(key, value);
		   editor.commit();
	}
	
	public String getMessage(String value){
		 SharedPreferences sharedPreference = activity.getSharedPreferences("MyPreference", 0);
		   String data = sharedPreference.getString(value, "0");
		   return data;
	}
}
