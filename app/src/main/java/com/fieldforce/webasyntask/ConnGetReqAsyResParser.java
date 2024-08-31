package com.fieldforce.webasyntask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.fieldforce.asyntask.NewParser;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.utility.CommonUtility;

public class ConnGetReqAsyResParser extends
		AsyncTask<String, Integer, ResponseParser> {

	private Dialog mDialog = null;
	Activity activity;
	String apiUrl;
	boolean dialog = true;
	public void setDialog(boolean dialog) {
		this.dialog = dialog;
	}

	private static final String TAG = "ConnGetReqAsyResParser";
	public ConnGetReqAsyResParser(Activity activity, String apiUrl) {
		this.apiUrl = apiUrl;
		this.activity = activity;
		Log.d(TAG, "ConnGetReqAsyResParser: "+apiUrl);
		Log.d(TAG, "ConnGetReqAsyResParser: "+activity.getLocalClassName());
	}

	@Override
	protected void onPreExecute() {
		try {
			if (dialog) {
				if (activity != null) {
					mDialog = ProgressDialog.show(activity, "inField",
							"please wait...");
					mDialog.setCancelable(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected ResponseParser doInBackground(String... params) {
		// if (!CommonUtility.isNetworkFoundToast(activity)) {
		// return null;
		// }
		addDeviceSourceIsMobile();
		if (!apiUrl.contains(" ")){
			Log.d(TAG, "doInBackground: "+apiUrl);
			return new NewParser().getResponse(apiUrl);
		}
		else
			return new ResponseParser(apiUrl, apiUrl+" \nUrl connection contains incorrect format i.e. contain blank space.");

	}

	private void addDeviceSourceIsMobile() {
		if (andOperatorExist()) {
			apiUrl = apiUrl + "dSource=APP";
		} else {
			apiUrl = apiUrl + "&dSource=APP";
		}

	}

	private boolean andOperatorExist() {
		String lastStr = apiUrl.charAt(apiUrl.length() - 1) + "";
		if (lastStr.equalsIgnoreCase("&")) {
			return true;
		}
		return false;
	}

	@Override
	protected void onPostExecute(ResponseParser response) {
		super.onPostExecute(response);
		if (response == null)
			return;
		sendAutoMail(response);
		sendResponseIFNoError(response);
	}

	private void sendResponseIFNoError(ResponseParser response) {
		String loginMsg = "";
		if (apiUrl.contains("login\\")||apiUrl.contains("login?")) {
			loginMsg = "Login Failed!!";
		}
		if (checkResponseStatus(response, activity, loginMsg)) {
			if (asyResp != null)
				asyResp.response(response);
			try {
				if (mDialog != null) {
					mDialog.dismiss();
				}
			}catch (Exception e){

			}
		} else if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	private void sendAutoMail(ResponseParser response) {
		if (response != null && response.getResJsonObject() == null) {
			response.url = apiUrl;
		}
	}

	AsynResponseParser asyResp = null;

	public void setResponse(AsynResponseParser asyResp) {
		this.asyResp = asyResp;
	}

	boolean needToShowMsgDialog = true;

	public void needToShowResponseMessage(boolean needToShowMsgDialog) {
		this.needToShowMsgDialog = needToShowMsgDialog;
	}

	boolean checkResponseStatus(ResponseParser res, Activity activity,
			String someOtherMsg) {
		if (someOtherMsg == null) {
			someOtherMsg = "";
		} else if (someOtherMsg.length() > 0) {
			someOtherMsg += "\n";
		}
		if (res != null) {
			if (res.isSuccess() && res.getResJsonObject() != null) {
				return true;
			} else {
				if (needToShowMsgDialog) {

					if(isStyleRequired) {
						ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.AlertDialogCustom);
						new app.core.utils.Dialog(activity).show("" + someOtherMsg
								+ res.errormsg);
					}else{
						try {
							new app.core.utils.Dialog(activity).show("" + someOtherMsg
									+ res.errormsg);
						}
						catch (Exception e){

						}

					}
				}
				return false;
			}
		} else {
			CommonUtility.ShowToast("Connection problem!!", activity);
			return false;
		}
	}
	boolean isStyleRequired = false;
	public void isStyleRequired(boolean isStyleRequired) {
		this.isStyleRequired = isStyleRequired;
	}
}
