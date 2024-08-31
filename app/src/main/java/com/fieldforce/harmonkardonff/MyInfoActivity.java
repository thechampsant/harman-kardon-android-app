package com.fieldforce.harmonkardonff;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.MyInfoModel;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import app.core.base.InnosolsActivity;
import app.core.model.Response;

import com.fieldforce.asyntask.UpdateUserInfo;
import com.fieldforce.asyntask.UserInfoUpdate;

public class MyInfoActivity extends InnosolsActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);

		// SetTextViewAsString(R.id.txt_name,MainActivity.MyInfo.ISPName);
		// SetTextViewAsString(R.id.txt_empcode,MainActivity.MyInfo.EmployeeCode);
		// SetTextViewAsString(R.id.txt_mobile,MainActivity.MyInfo.Pmobile1);
		// SetTextViewAsString(R.id.txt_counter,MainActivity.MyInfo.CurrentStore);
		// SetTextViewAsString(R.id.txt_Address,MainActivity.MyInfo.CurrentStoreLocation);
		// SetTextViewAsString(R.id.txt_assignedOn,MainActivity.MyInfo.AssignedOnS);
		// String
		// msg="Showing my info as per last Updation i.e.:-"+MainActivity.MyInfo.LastUpdatedOn;
		// SetTextViewAsString(R.id.txt_lastupdateinfo,msg);
		// attachCustomerSupport();
		initialize();
		new UpdateUserInfo(this, new UserInfoUpdate() {
			
			@Override
			public void dataUpdated(Response response) {
				if (response.isSuccess()) {
					ProcesssResult(response.getRequestStatus(),response);
				} 
				
			}
		}).execute();
	}
	private void ProcesssResult(boolean status,Response response) {

		try {
			if (status) {
				setMyInfoToDb(response);
				initialize();
	/*			User.SetLoginWithRememberMe(GetEditTextAsString(R.id.username),
						GetEditTextAsString(R.id.password));
				User.setUserLogged(true);
				showProgressWait(false);
				gotToMain();*/
			} 
		} catch (Exception ex) {
//			ShowToastLong(ex.getMessage(), 0);
		}
	}
	private void setMyInfoToDb(Response response) {
		@SuppressWarnings("unchecked")
		ArrayList<MyInfoModel> myinfo = (ArrayList<MyInfoModel>) response.data;
		if (myinfo == null)
			return;
		if (myinfo.Count() > 0) {
			MainActivity.MyInfo = myinfo.First();
			MainActivity.MyInfo.EmployeeCode = myinfo.First().UserID;
			MainActivity.MyInfo.InsertOrUpdate();
		}
	}
	public void initialize() {
		SetTextViewAsString(R.id.txt_name, MainActivity.MyInfo.ISPName);
		SetTextViewAsString(R.id.txt_empcode, MainActivity.MyInfo.EmployeeCode);
		SetTextViewAsString(R.id.txt_mobile, MainActivity.MyInfo.Pmobile1);
		SetTextViewAsString(R.id.txt_counter, MainActivity.MyInfo.CurrentStore);
		SetTextViewAsString(R.id.txt_Address,
				MainActivity.MyInfo.CurrentStoreLocation);
		SetTextViewAsString(R.id.txt_assignedOn,
				MainActivity.MyInfo.AssignedOnS);
		String msg = "Showing my info as per last Updation i.e.:-"
				+ MainActivity.MyInfo.LastUpdatedOn;
		SetTextViewAsString(R.id.txt_lastupdateinfo, msg);
		attachCustomerSupport();
	}
	
	public void homebutton(View v){
		finish();
	}

	private void attachCustomerSupport() {
		this.getView(R.id.btn_call_one).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						callCustomerOne();
					}
				});

		/*
		 * this.getView(R.id.btn_call_two).setOnClickListener( new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * callCustomerTwo(); } });
		 */
	}

	private void callCustomerOne() {
		call("9650664007");
	}

	/* private void callCustomerTwo() {
	 call("011-40655637"); }
*/
	private void call(String number) {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + number));
			startActivity(callIntent);
		} catch (Exception ex) {
			ShowToastLong(ex.getMessage(), 0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_info, menu);
		return true;
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

}
