package com.fieldforce.harmonkardonff;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.AllSalesSKUDetailModel;
import mob.field.harmonkardonff.services.WebService;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.base.InnosolsActivity;
import app.core.model.Response;

public class SaleDetailsScreenManagerActivity extends InnosolsActivity {
	private String CatID;
	public String date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_sale_details);
		activate();
	}

	public void activate() {
		CatID = getIntent().getExtras().getString("catid");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");
		date = new SimpleDateFormat("EEE, d MMM yyyy").format(Calendar
				.getInstance().getTime());

		Date dateLast = new Date(System.currentTimeMillis());
		String dateLastString = simpleDateFormat.format(dateLast);

		Date dateFirst = new Date(System.currentTimeMillis()
				- (604800000 - 86400000));
		String dateFirstString = simpleDateFormat.format(dateFirst);

		TextView beforesevendaysLable = (TextView) findViewById(R.id.weekdates);

		beforesevendaysLable.setText("Sales from " + dateFirstString + " To "
				+ dateLastString);

		Button HmButton = ((Button) findViewById(R.id.btn_go_home));
		HmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				/*
				 * IScreenManager DAT = new SaleScreenManager();
				 * BaseActivity.NavigateTo(DAT, null);
				 */
			}
		});
		new AllSalesDetail().execute();
	}

	Response allSalesDetailResponse;

	public class AllSalesDetail extends AsyncTask<Void, Void, Void> {
		private ProgressDialog mDialog;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				WebService server = new WebService();

				// String userid = "" + MainActivity.MyInfo.UserID;

				allSalesDetailResponse = server.getSalesSKUDetails(CatID);

				return null;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				allSalesDetailResponse = new Response(e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			try {
				mDialog = ProgressDialog.show(
						SaleDetailsScreenManagerActivity.this, "",
						"Please wait..");
				mDialog.setCancelable(true);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(final Void st) {

			if (allSalesDetailResponse != null
					&& allSalesDetailResponse.status.trim().equalsIgnoreCase(
							"true")) {

				if (mDialog != null) {
					mDialog.dismiss();
				}

				showListView();
			}
		}

		private void showListView() {
			ListView listView = ((ListView) findViewById(R.id.sale_list));

			if (listView == null)
				return;

			final GenricAdapter<AllSalesSKUDetailModel> adaptor = new GenricAdapter<AllSalesSKUDetailModel>(
					SaleDetailsScreenManagerActivity.this,
					R.layout.sales_detail_display_row);

			adaptor.setGenricAdapter(new IAdapter() {

				@Override
				public void setItemView(Object arg0, View arg1, int position) {
					// TODO Auto-generated method stub

					AllSalesSKUDetailModel obj = (AllSalesSKUDetailModel) arg0;
					TextView saleName = (TextView) arg1
							.findViewById(R.id.sale_name);
					TextView Oty = (TextView) arg1
							.findViewById(R.id.total_sale);
					saleName.setText(obj.getSKU());
					Oty.setText(obj.getQty());

					TextView totalSale = (TextView) arg1
							.findViewById(R.id.date);
					totalSale.setText(getDate(obj.getSaleFor()));
				}
			});

			@SuppressWarnings("unchecked")
			ArrayList<AllSalesSKUDetailModel> allSalesSKUDetailModels = (ArrayList<AllSalesSKUDetailModel>) allSalesDetailResponse.data;

			adaptor.setData(allSalesSKUDetailModels);
			listView.setAdapter(adaptor);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

				}
			});
		}

		private String getDate(String str) {
			String milliInStr = "";

			for (int i = 0; i < str.length(); i++) {
				char temp = str.charAt(i);
				if (Character.isDigit(temp)) {
					milliInStr += temp + "";
				}
			}

			return convertMilliToDate(milliInStr);
		}

		@SuppressLint("SimpleDateFormat")
		private String convertMilliToDate(String milliInStr) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"d MMM yyyy");
			Date dateLast = new Date(Long.valueOf(milliInStr));
			String date = simpleDateFormat.format(dateLast);
			return date;
		}

		@Override
		protected void onCancelled() {
			// showProgress(false);
		}
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}
}
