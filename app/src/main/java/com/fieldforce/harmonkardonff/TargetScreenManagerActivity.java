package com.fieldforce.harmonkardonff;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.TargetModelAll;
import mob.field.harmonkardonff.services.WebService;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ariston.training_module.modules.training_module.adapters.FaqAdpter;

import app.core.base.InnosolsActivity;
import app.core.model.Response;
import app.core.utils.Dialog;

public class TargetScreenManagerActivity extends InnosolsActivity {

	public Response resTargetVsAchivement = new Response();
	TextView monthTextView;
	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
	private TargetScreenManagerAdpter adapter;
	RecyclerView targetlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.target_achievement_display);
		activate();
		((TextView) findViewById(R.id.target_vs_ach_current_user_textview)).setText("Current User : "+WebService.UserName);
		((TextView) findViewById(R.id.target_vs_ach_isp_cat_textview)).setText("ISP Category : "+MainActivity.MyInfo.ISPCategory);

		targetlist=findViewById(R.id.targetlist);
		setupRecycler();
	}

	public void actionBarBackButtonClicked(View v) {
		onBackPressed();
		finish();
	}

	public void activate() {
		new AllTarget().execute((Void) null);
	}

	public class AllTarget extends AsyncTask<Void, Void, Void> {
		private ProgressDialog mDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			try {
				mDialog = ProgressDialog.show(TargetScreenManagerActivity.this,
						"", "Please wait..");
				mDialog.setCancelable(true);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				// WebAPI webAPI = new WebAPI(MainActivity.Current);
				WebService server = new WebService();
				// String userid = "" + MainActivity.MyInfo.UserID;
				if (server.isNetworkAvailable(TargetScreenManagerActivity.this))
					resTargetVsAchivement = server
							.getTargetAchievementFromWeb();
				else {
					resTargetVsAchivement = null;
				}
				return null;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resTargetVsAchivement = new Response(e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(final Void st) {

			if (resTargetVsAchivement != null)

				targetVsAchivementResponse(resTargetVsAchivement);

			else {
				Toast.makeText(getApplicationContext(),
						"Internet is not available", Toast.LENGTH_LONG).show();
			}

			if (mDialog != null) {
				mDialog.dismiss();
			}
		}

		@Override
		protected void onCancelled() {
			// showProgress(false);
		}
	}

	public void targetVsAchivementResponse(Response response) {
		if (response.status.equalsIgnoreCase("true")) {

			if (response.data == null || response.data.size() == 0) {
				new Dialog(this).show("No Target Achivement Found");
				return;
			} else
				initializeListViewAndMsg();

		} else {
			new Dialog(this).show(response.errormsg);
		}

	}
	Date d = null;
	private void initializeListViewAndMsg() {
		ArrayList<TargetModelAll> revArrayList = new ArrayList<TargetModelAll>();
		for (int i = resTargetVsAchivement.data.size() - 1; i >= 0; i--) {
			revArrayList.add((TargetModelAll) resTargetVsAchivement.data.get(i));
		}
			TargetModelAll target = ((ArrayList<TargetModelAll>) resTargetVsAchivement.data).First();
		adapter.addData(revArrayList);
	}
	private void setupRecycler() {
		adapter = new TargetScreenManagerAdpter();
		targetlist.setAdapter(adapter);
		targetlist.setLayoutManager(new LinearLayoutManager(this));
		//  faqlist.addItemDecoration(new ItemDecorationAlbumColumns((int) Helper.getSizeInDp(this, 16), 3));
	}

	@Override
	public void RegisterTableInfoForLocalDB() {}
}
