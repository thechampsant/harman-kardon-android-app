package com.fieldforce.asyntask;

import mob.field.harmonkardonff.entitiymodels.GPSWifiPropertyModel;

import org.json.JSONObject;

public interface GPSCallBackBackgroundTask {
	public void callBackBackgroundTask(JSONObject jobj,GPSWifiPropertyModel gpsWifiObj);
}
