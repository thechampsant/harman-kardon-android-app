package com.fieldforce.asyntask;

import mob.field.harmonkardonff.entitiymodels.GPSWifiPropertyModel;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.fieldforce.networkconnection.Parser;




public class GPSAsyTaskBackground extends AsyncTask<Void, Void, Void>{
	
	private ProgressDialog pDialog;
	private String apiUrl;
	private JSONObject jobj;
	GPSCallBackBackgroundTask callBTask = null;
	GPSWifiPropertyModel gpsWifiObj;
	public GPSAsyTaskBackground(String apiUrl,GPSWifiPropertyModel gpsWifiObj) {
		this.apiUrl = apiUrl;
		this.gpsWifiObj = gpsWifiObj;
	}
	public void setBackgroundTask(GPSCallBackBackgroundTask callBTask)
	{
		this.callBTask = callBTask;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			// dummy network call
			jobj = Parser.parserGET(apiUrl);;
			if(callBTask!=null)
				callBTask.callBackBackgroundTask(jobj,gpsWifiObj);
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jobj = null;
			return null;
		}
	}
	
}
