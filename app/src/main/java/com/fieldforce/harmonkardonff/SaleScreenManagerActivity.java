package com.fieldforce.harmonkardonff;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.AllSalesDetailModel;
import mob.field.harmonkardonff.services.WebService;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class SaleScreenManagerActivity extends InnosolsActivity {

	public String date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_details);
		activate();
	}

	public void activate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" d MMM yyyy");
		date = new SimpleDateFormat(" d MMM yyyy").format(Calendar
				.getInstance().getTime());

		Date dateLast = new Date(System.currentTimeMillis());
		String dateLastString = simpleDateFormat.format(dateLast);

		Date dateFirst = new Date(System.currentTimeMillis() - (604800000-86400000));
		String dateFirstString = simpleDateFormat.format(dateFirst);

		TextView beforesevendaysLable = (TextView) findViewById(R.id.weekdates);

		beforesevendaysLable.setText("Sales from " + dateFirstString + " To "
				+ dateLastString);

		Button HmButton = ((Button) findViewById(R.id.btn_go_home));
		HmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				/*
				 * IScreenManager DAT = new HomeManager(); NavigateTo(DAT,
				 * null);
				 */
			}
		});

		new AllSalesDetail().execute((Void) null);

	}

	Response allSalesDetailResponse;

	public class AllSalesDetail extends AsyncTask<Void, Void, Void> {

		private ProgressDialog mDialog;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				// WebAPI webAPI = new WebAPI(MainActivity.Current);
				WebService server = new WebService();
				// String userid = "" + MainActivity.MyInfo.UserID;

				allSalesDetailResponse = server.getSales(0);

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
				mDialog = ProgressDialog.show(SaleScreenManagerActivity.this,
						"", "Please wait..");
				mDialog.setCancelable(true);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPostExecute(final Void st) {

			if (allSalesDetailResponse.status.trim().equalsIgnoreCase("true")) {

				// Toast.makeText(BaseActivity,"" +
				// allSalesDetailResponse.data.Count(),Toast.LENGTH_LONG).show();
				// ArrayList<AllSalesDetailModel> allSalesDetailModel =
				// allSalesDetailResponse.data;
				if (mDialog != null) {
					mDialog.dismiss();
				}
				showListView();
			}
		}

		@Override
		protected void onCancelled() {
			// showProgress(false);
		}
	}

	@SuppressWarnings("unchecked")
	private void showListView() {
		ListView listView = ((ListView) findViewById(R.id.sale_list));

		if (listView == null)
			return;

		final GenricAdapter<AllSalesDetailModel> adaptor = new GenricAdapter<AllSalesDetailModel>(
				SaleScreenManagerActivity.this, R.layout.sales_display_row);

		adaptor.setGenricAdapter(new IAdapter() {

			@Override
			public void setItemView(Object arg0, View arg1, int position) {
				// TODO Auto-generated method stub

				AllSalesDetailModel obj = (AllSalesDetailModel) arg0;
				TextView saleName = (TextView) arg1
						.findViewById(R.id.sale_name);

				TextView totalSale = (TextView) arg1
						.findViewById(R.id.total_sale);

				saleName.setText(obj.getCategory());

				totalSale.setText(obj.getQty() + "");

			}
		});

		@SuppressWarnings("unchecked")
		final ArrayList<AllSalesDetailModel> allSales = (ArrayList<AllSalesDetailModel>) allSalesDetailResponse.data;

		adaptor.setData(allSales);

		listView.setAdapter(adaptor);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				;
				/*
				 * IScreenManager screen = new SaleDetailScreenManager();
				 * BaseActivity.NavigateTo(screen, null);
				 */
				Intent i = new Intent(SaleScreenManagerActivity.this,
						SaleDetailsScreenManagerActivity.class);
				i.putExtra("catid", allSales.get(position).getCatID());
				startActivity(i);

			}
		});
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}
}
