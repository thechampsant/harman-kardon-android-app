package com.fieldforce.harmonkardonff;

import mob.field.harmonkardonff.entitiymodels.Complain;
import mob.field.harmonkardonff.services.WebService;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.model.ImageCaptureResult;
import app.core.model.Response;
import app.core.utils.Dialog;
import app.core.utils.Message;


public class ComplainActivity extends InnosolsActivity {
//	GetCurrentDateInString()
	Complain CMP = new Complain();
	WebService web = new WebService();
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
	
			ImageCaptureResult result =getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);

			if (result.getTotalImagesCount() > 0) {
				String a = result.getDocsIDs();
				CMP.DocIDs= a;
				
			}
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain);
		SetCurrentDate();
		this.SetOnClickListenerOnButton(R.id.btn_submit_complain,new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OnSubmitclick(null);
			}});
		this.SetOnClickListenerOnButton(R.id.btn_img,new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CapturePhoto(null);
			}});
		// Show the Up button in the action bar.
		// setupActionBar();
	}

	private void SetCurrentDate() {
		// TODO Auto-generated method stub
		this.SetTextViewAsString(R.id.date, this.GetCurrentDateInString());
	}

	public void CapturePhoto(View view) {
		Intent I = new Intent(this, ImageCaptureActivity.class);
		I.putExtra(ImageCaptureActivity.PARAMS_DOC_TYPE, "Complain");
		I.putExtra(ImageCaptureActivity.PARAMS_USERNAME,User.GetUserName());
		I.putExtra(ImageCaptureActivity.PARAMS_GUID, CMP.guid);
		
		startActivityForResult(I, 1);
	}


	public void OnSubmitclick(View view) {
		// TODO Auto-generated method stub
		if (Validate() && this.isNetworkFoundDialog()) {
			BackgroundProcess bp = new BackgroundProcess(this)
					.setProgressMessage("sending complain...");
			bp.setbackgroundProcess(new IProcess() {

				@SuppressWarnings("rawtypes")
				@Override
				public void processResponse(Object arg0) throws Exception {
					// TODO Auto-generated method stub
					ProcessComplainResponse((Response)arg0);
				}

				@Override
				public Object underProcess() throws Exception {
					// TODO Auto-generated method stub
					return web.TryCreateNewComplain(CMP);
				}
			});

			bp.execute();
		}
	}

	@SuppressWarnings("rawtypes")
	public void ProcessComplainResponse(Response response) {
		if (response.status.equalsIgnoreCase("true")) {
			this.ShowToast(Message.SUCCESS_SERVER);
			this.finish();
		} else {
			new Dialog(this).setTitle("Error").show(response.errormsg);
		}
	}
	
	public void homebutton(View v){
		finish();
	}
	public Complain GetObject() {
		// TODO Auto-generated method stub

		CMP.Name = this.GetEditTextAsString(R.id.tbxname);
		CMP.Number = this.GetEditTextAsString(R.id.tbxnumber);
		CMP.Remarks = this.GetEditTextAsString(R.id.tbxcomplaint);
		CMP.Date = GetCurrentDateTimeInString();
		return CMP;
	}

	private boolean Validate() {
		// TODO Auto-generated method stub
		this.GetObject();
		if (CMP.Remarks == null || CMP.Remarks.equals("")) {
			ShowToast("Enter Complain");
			return false;
		} else if (CMP.Name == null || CMP.Name.equals("")) {
			ShowToast("Enter Name");
			return false;
		} else if (CMP.Number == null || CMP.Number.equals("")) {
			ShowToast("Enter Number");
			return false;
		} else
			return true;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

}
