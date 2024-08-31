package com.fieldforce.webasyntask;

import org.json.JSONObject;

import android.text.TextUtils;

public class ResponseParser {
	public String status = "false";
	public String errormsg = "";
	public JSONObject jsonObjResponse;
	public JSONObject payload;
	public String url = "";

	public ResponseParser(String error) {
		this.errormsg = error;
	}

	public ResponseParser() {
		// TODO Auto-generated constructor stub
	}

	public ResponseParser(String apiUrl, String error) {
		url = apiUrl;
		errormsg = error;
		jsonObjResponse = null;

	}

	public boolean isSuccess() {
		if (TextUtils.isEmpty(status))
			return false;
		else if (this.status.toLowerCase().trim().equalsIgnoreCase("true")
				|| this.status.toLowerCase().trim().equalsIgnoreCase("success"))
			return true;
		else
			return false;
	}

	public JSONObject getResJsonObject() {
		return jsonObjResponse;
	}
}
