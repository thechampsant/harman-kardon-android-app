package com.fieldforce.harmonkardonff;

import mob.field.harmonkardonff.entitiymodels.AadharModel;
import mob.field.harmonkardonff.services.WebService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.entitymodels.ImageInfo;
import app.core.model.ImageCaptureResult;
import app.core.model.Response;
import app.core.utils.Dialog;
import app.core.utils.Message;

public class AadharActivity extends InnosolsActivity {

	AadharModel model = new AadharModel();
	WebService web = new WebService();
	private boolean IsImageupldaadhar = false;
	private boolean IsImageupldPan = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == 1 || requestCode == 2)
				&& resultCode == Activity.RESULT_OK) {

			ImageCaptureResult result = getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);

			if (result.getTotalImagesCount() > 0) {
				setDocsIDs(result.getDocsIDs(), result.getOnlineImagesCount() > 0, requestCode);
			}

		}
	}

	public void setDocsIDs(String docsIDs, boolean IsImageUploaded,
			int requestCode) {
		String docid = docsIDs;

		if (requestCode == 1 && IsImageUploaded) {

			model.PanDocID = docid;
			IsImageupldPan = IsImageUploaded;
		} else if (requestCode == 2 && IsImageUploaded) {

			model.AadhaarDocID = docid;
			IsImageupldaadhar = IsImageUploaded;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aadhar);
		this.SetOnClickListenerOnButton(R.id.btn_sbmt, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OnSubmitclick(null);
			}
		});

		this.SetOnClickListenerOnButton(R.id.btn_panimg, new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity.Current.db.DeleteAll(new ImageInfo());
				CapturePhoto("Pancard", 1);
			}
		});
		this.SetOnClickListenerOnButton(R.id.btn_aadhrimg,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						MainActivity.Current.db.DeleteAll(new ImageInfo());

						CapturePhoto("aadhar", 2);
					}
				});

		// Show the Up button in the action bar.
		// setupActionBar();

	}

	public void CapturePhoto(String Doctype, int activityid) {
		Intent I = new Intent(this, ImageCaptureActivity.class);
		I.putExtra(ImageCaptureActivity.PARAMS_DOC_TYPE, Doctype);
		I.putExtra(ImageCaptureActivity.PARAMS_USERNAME, User.GetUserName());
		I.putExtra(ImageCaptureActivity.PARAMS_GUID, model.guid);
		startActivityForResult(I, activityid);
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

					return web.TryaddAdhardetails(model);
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

		if (model.PanCardNo.isEmpty() || model.PanCardNo.length() != 10) {
			this.ShowToast("Please enter 10 digits Pan Card Number");
			return false;
		}
		if (model.AadhaarCardNo.isEmpty() || model.AadhaarCardNo.length() != 12) {
			this.ShowToast("Please enter 12 digits Aadhar Card Number");
			return false;
		}

		if (!IsImageupldPan) {
			this.ShowToast("Please Upload Pan Card Image");
			return false;
		}
		if (!IsImageupldaadhar) {
			this.ShowToast("Please Upload Aadhar Card Image");
			return false;
		}

		else
			return true;
	}

	public void GetObject() {
		// TODO Auto-generated method stub

		model.PanCardNo = this.GetEditTextAsString(R.id.edt_pannumber);
		model.AadhaarCardNo = this.GetEditTextAsString(R.id.edt_aadhrno);
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

}
