package com.fieldforce.harmonkardonff;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.Feedback;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.model.ImageCaptureResult;
import app.core.model.Response;
import app.core.utils.Dialog;
import app.core.utils.Message;

public class FeedbackActivity extends InnosolsActivity {
	String cat;
	int PID = 0;
	Feedback fd = new Feedback();
	WebService web = new WebService();
	private ArrayList<ProductModel> arry_prdctlist;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			ImageCaptureResult result = getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);

			if (result.getTotalImagesCount() > 0) {
				fd.DocIDs = result.getDocsIDs();
			}
		}
	}

	private boolean IsCatSelected() {
		Spinner sp = getSpinner(R.id.spdat);
		cat = sp.getSelectedItem().toString();
		if (cat.equals("Select")) {
			ShowToast("Please select Category");
			return false;
		} else
			return true;
	}

	private void GetDropDown() {
		// TODO Auto-generated method stub

		fd.PID = GetPID(cat) + "";
	}

	public String GetPID(String Category) {
		String Pid = "";
		if (arry_prdctlist != null) {
			ArrayList<ProductModel> model = arry_prdctlist.where("Cat3",
					Category);
			if (model.size() != 0) {
				Pid = model.First().PID;
			}

		}
		// if (Category.equals("Direct Cool"))
		// PID = 7;
		// if (Category.equals("Frost Free"))
		// PID = 8;
		// if (Category.equals("FLT"))
		// PID = 10;
		// if (Category.equals("FATL"))
		// PID = 9;
		// if (Category.equals("SATT"))
		// PID = 11;
		// if (Category.equals("Microwave Oven"))
		// PID = 5;
		// if (Category.equals("Air Conditioner"))
		// PID = 4;
		// if (Category.equals("RO"))
		// PID = 6;
		// if (Category.equals("Built In"))
		// PID = 1139;

		return Pid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		this.SetOnClickListenerOnButton(R.id.btn_submit_feedback,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						OnSubmitclick(null);
					}
				});

		this.SetOnClickListenerOnButton(R.id.btn_img, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CapturePhoto(null);
			}
		});

		// Show the Up button in the action bar.
		// setupActionBar();
		SetCurrentDate();
		Setmodel();
	}

	private void Setmodel() {
		arry_prdctlist = MainActivity.MyProductList;
		if (arry_prdctlist != null) {
			Spinner sp = (Spinner) this.findViewById(R.id.spdat);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item,
					getmodels());
			sp.setAdapter(spinnerArrayAdapter);

		}
	}

	public ArrayList<String> getmodels() {

		ArrayList<String> data = arry_prdctlist.Select("Cat3").Distinct()
				.ToString();
		return data;
	}

	private void SetCurrentDate() {
		// TODO Auto-generated method stub
		this.SetTextViewAsString(R.id.date, this.GetCurrentDateInString());
	}

	public void CapturePhoto(View view) {
		Intent I = new Intent(this, ImageCaptureActivity.class);
		I.putExtra(ImageCaptureActivity.PARAMS_DOC_TYPE, "Feedback");
		I.putExtra(ImageCaptureActivity.PARAMS_USERNAME, User.GetUserName());
		I.putExtra(ImageCaptureActivity.PARAMS_GUID, fd.guid);
		startActivityForResult(I, 1);
	}

	public void Homebutton(View v) {
		finish();
	}

	public void OnSubmitclick(View view) {
		// TODO Auto-generated method stub
		if (Validate() && this.isNetworkFoundDialog() && IsCatSelected()) {

			BackgroundProcess bp = new BackgroundProcess(this)
					.setProgressMessage("sending feedback...");
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
					IsCatSelected();
					GetDropDown();
					return web.TryCreateNewFeedback(fd);
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
		Feedback fd = GetObject();
		if (fd.Remarks == null || fd.Remarks.equals("")) {
			this.ShowToast("Please enter feedback");
			return false;
		} else
			return true;
	}

	public Feedback GetObject() {
		// TODO Auto-generated method stub

		fd.Remarks = this.GetEditTextAsString(R.id.tbxfeedback);
		return fd;
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
		getMenuInflater().inflate(R.menu.feedback, menu);
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
