package com.fieldforce.asyntask;

import mob.field.harmonkardonff.services.WebService;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import app.core.model.Response;

import com.fieldforce.harmonkardonff.MyInfoActivity;
import com.fieldforce.utility.Storage;

public class UpdateUserInfo extends AsyncTask<Void, Void, Void> {
	WebService webAPI = new WebService();
	Activity activity;
	ProgressDialog pd;
	private Response response = new Response();
	UserInfoUpdate userInfoUpdate;
	public UpdateUserInfo(Activity activity,UserInfoUpdate userInfoUpdate)
	{
		this.activity = activity;
		this.userInfoUpdate = userInfoUpdate;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd = Storage.showProgressBar(activity,"");
	}
	@SuppressWarnings("static-access")
	@Override
	protected Void doInBackground(Void... params) {
		// TODO: attempt authentication against a network service.

		try {
			// Simulate network access.
			webAPI.UserName = ((MyInfoActivity)activity).User.GetUserName();
			webAPI.Password = ((MyInfoActivity)activity).User.GetPassword();
			response = webAPI.TryLogin("");
			return null;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(final Void st) {
		Storage.dismissProgressBar(pd);
		if(response!=null)
		userInfoUpdate.dataUpdated(response);
		
	}

	
}