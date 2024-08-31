package com.fieldforce.harmonkardonff;

import mob.field.harmonkardonff.entitiymodels.NeftModel;
import mob.field.harmonkardonff.services.WebService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.model.ImageCaptureResult;
import app.core.model.Response;
import app.core.utils.Dialog;
import app.core.utils.Message;

public class NeftActivity extends InnosolsActivity {

	NeftModel model = new NeftModel();
	WebService web = new WebService();
	private boolean IsImageUploaded = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			ImageCaptureResult result = getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);

			if (result.getTotalImagesCount() > 0) {
				setDocsIDs(result.getDocsIDs(),
						result.getOnlineImagesCount() > 0);
			}
		}
	}

	public void setDocsIDs(String docsIDs, boolean IsImageUploaded) {

		this.model.DocID = docsIDs;
		this.IsImageUploaded = IsImageUploaded;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neft_main);
		
		((TextView) findViewById(R.id.neft_current_user_textview)).setText("Current User : "+WebService.UserName);
		
		this.SetOnClickListenerOnButton(R.id.btn_sbmt, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OnSubmitclick(null);
			}
		});

		this.SetOnClickListenerOnButton(R.id.btn_img, new OnClickListener() {

			@Override
			public void onClick(View v) {
				CapturePhoto(null);
			}
		});
		attachccheckbxlistenr();

		// Show the Up button in the action bar.
		// setupActionBar();

	}

	private void attachccheckbxlistenr() {
		GetCheckBox(R.id.checkbx).setOnCheckedChangeListener(
				new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							GetEditText(R.id.edt_uan).setVisibility(
									View.VISIBLE);

						} else {
							GetEditText(R.id.edt_uan).setVisibility(View.GONE);
						}
					}
				});
	}

	public void CapturePhoto(View view) {
		Intent I = new Intent(this, ImageCaptureActivity.class);
		I.putExtra(ImageCaptureActivity.PARAMS_DOC_TYPE, "cheque");
		I.putExtra(ImageCaptureActivity.PARAMS_USERNAME, User.GetUserName());
		I.putExtra(ImageCaptureActivity.PARAMS_GUID, model.guid);
		startActivityForResult(I, 1);
	}

	public void OnSubmitclick(View view) {
		// TODO Auto-generated method stub
		if (Validate() && this.isNetworkFoundDialog()) {

			BackgroundProcess bp = new BackgroundProcess(this)
					.setProgressMessage("loading...");
			bp.setbackgroundProcess(new IProcess() {

				@SuppressWarnings("rawtypes")
				@Override
				public void processResponse(Object arg0) throws Exception {
					// TODO Auto-generated method stub
					ProcessFeedbackResponse((Response) arg0);
				}

				@Override
				public Object underProcess() throws Exception {
					// TODO Auto-generated method stub

					return web.TryaddNeftdetails(model);
				}
			});

			bp.execute();
		}
	}

	@SuppressWarnings("rawtypes")
	public void ProcessFeedbackResponse(Response response) {
		if (response.status.equalsIgnoreCase("true")) {
			this.ShowToast(Message.SUCCESS_SERVER);
			this.finish();
		} else {
			new Dialog(this).setTitle("Error").show(response.errormsg);
		}
	}

	private boolean Validate() {
		// TODO Auto-generated method stub
		GetObject();
		if (model.Name.isEmpty()) {
			this.ShowToast("Please Enter Name");
			return false;
		}
		if (GetCheckBox(R.id.checkbx).isChecked() && model.UanNo.length() != 12) {
			this.ShowToast("Please Enter 12 Digit UAN Number");
			return false;
		}
		if (model.AccountNo.isEmpty()) {
			this.ShowToast("Please Enter Account Number");
			return false;
		}
		if (model.BankName.isEmpty()) {
			this.ShowToast("Please Enter Bank Name");
			return false;
		}
		if (model.IFSC.isEmpty() || model.IFSC.length() != 11) {
			this.ShowToast("Please enter 11 digit IFSC Code");
			return false;
		}
		if (model.BranchLocation.isEmpty()) {
			this.ShowToast("Please Enter Branch Location");
			return false;
		}
		if (!IsImageUploaded) {
			this.ShowToast("Please Upload Image");
			return false;
		} else
			return true;
	}

	public void GetObject() {
		// TODO Auto-generated method stub
		if (GetCheckBox(R.id.checkbx).isChecked()) {
			model.UanNo = GetEditTextAsString(R.id.edt_uan);

		} else {
			model.UanNo = "";
		}
		model.Name = this.GetEditTextAsString(R.id.edt_name);
		model.AccountNo = this.GetEditTextAsString(R.id.edt_bnkno);
		model.BankName = this.GetEditTextAsString(R.id.edt_bnkname);
		model.IFSC = this.GetEditTextAsString(R.id.edt_ifsc);
		model.BranchLocation = this.GetEditTextAsString(R.id.edt_brnchlocation);

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

}
