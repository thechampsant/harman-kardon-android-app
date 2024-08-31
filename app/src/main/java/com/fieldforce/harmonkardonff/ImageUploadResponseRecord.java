package com.fieldforce.harmonkardonff;

import org.json.JSONObject;

public class ImageUploadResponseRecord {
	public String status;
	public String msg;
	public String err;
	public JSONObject jobj = null;

	void setError(String err) {
		status = "false";
		this.err = err;
	}

	void setSuccess(String msg, JSONObject jobj) {
		this.msg = msg;
		status = "true";
		this.jobj = jobj;
	}
}