package com.fieldforce.harmonkardonff;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.ImeiStockModel;
import mob.field.harmonkardonff.services.WebService;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.utils.Dialog;

public class ViewStockActivity extends InnosolsActivity {

	public final byte PJP_LOADING_LAYOUT = 1;
	public final byte ERROR_LOADING_PJP_LAYOUT = 2;
	public final byte NO_PJP_FOUND_LAYOUT = 3;
	public final byte HIDE_ALL_LAYOUT = 4;

	/** Adapter for ListView */
	GenricAdapter<ImeiStockModel> viewPjpListAdapter;

	ListView pjpViewListView;

	String userID;

	/** WebCall instance for Making web calls */
	WebService webCall = new WebService();

	/**
	 * Small Layouts which lie on the center of {@linkp pjpViewListView} in a
	 * Relative layout and shown on various events like while loading
	 * PJP,pjpLoadingLayout is made Visible ..
	 */
	LinearLayout errorInLoadingPjpLayout, pjpLoadingLayout, noDataFoundLayout;

	ArrayList<ImeiStockModel> newPjpList = new ArrayList<ImeiStockModel>();

	ImeiStockModel pjp = null;
	String selectedDate;
	DatePickerDialog fromDatePickerDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_stock);

		errorInLoadingPjpLayout = (LinearLayout) findViewById(R.id.view_pjp_retry_layout);
		pjpLoadingLayout = (LinearLayout) findViewById(R.id.view_pjp_loading_layout);
		noDataFoundLayout = (LinearLayout) findViewById(R.id.view_pjp_no_data_found_layout);

		pjpViewListView = (ListView) findViewById(R.id.view_pjp_listview);
		setAdapterOnListView();

		selectedDate = GetCurrentDateInString();

		((TextView) findViewById(R.id.view_pjp_selected_date_textview))
				.setText(selectedDate);

		try {
			userID = MainActivity.MyInfo.UserID;
		} catch (Exception e) {
			userID = "";
		}

		refreshPjpDataFromServer(null);
	}

	/**
	 * Fetches New Pjp Data from Server and executes @extractPjpListFromJson
	 * which then executes @updateDataInPjpListview
	 * 
	 * @param v
	 *            calling view .It can be null too
	 */
	public void refreshPjpDataFromServer(View v) {
		getPjpDataFromServer(userID, selectedDate);
	}

	/** Shows the calendar at the start */
	public void showCalendar(View v) {
		final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd", Locale.US);
		Calendar newCalendar = Calendar.getInstance();

		selectedDate = dateFormatter.format(newCalendar.getTime());

		fromDatePickerDialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar newDate = Calendar.getInstance();
						newDate.set(year, monthOfYear, dayOfMonth);
						selectedDate = dateFormatter.format(newDate.getTime());
						((TextView) findViewById(R.id.view_pjp_selected_date_textview))
								.setText(selectedDate);
						fromDatePickerDialog.hide();
						refreshPjpDataFromServer(null);
					}

				}, newCalendar.get(Calendar.YEAR), newCalendar
						.get(Calendar.MONTH), newCalendar
						.get(Calendar.DAY_OF_MONTH));

		fromDatePickerDialog.setTitle("Select PJP Date");
		fromDatePickerDialog.show();
	}

	private void updateDataInPjpListview(ArrayList<ImeiStockModel> newPjpData) {
		viewPjpListAdapter.setData(newPjpData);
		viewPjpListAdapter.notifyDataSetChanged();
	}

	public void setAdapterOnListView() {
		viewPjpListAdapter = new GenricAdapter<ImeiStockModel>(this,
				R.layout.view_pjp_list_row // View for List Item (Template)
		); // Model (List)

		viewPjpListAdapter.setGenricAdapter(new IAdapter<ImeiStockModel>() {

			@Override
			public void setItemView(ImeiStockModel item, View view, int index) {

				((TextView) view
						.findViewById(R.id.view_stock_model_name))
						.setText(item.ModelName);
				((TextView) view
						.findViewById(R.id.view_stock_color))
						.setText(item.ColorName);
				((TextView) view.findViewById(R.id.view_stock_imei1))
						.setText(item.SerialNumber1);
				((TextView) view.findViewById(R.id.view_stock_imei2))
						.setText(item.SerialNumber2);


			}

		});

		pjpViewListView.setAdapter(viewPjpListAdapter);
	}

	/**
	 * Show/hides helper layout
	 * 
	 * @param layout
	 */
	private void showHelperLayout(byte layout) {
		switch (layout) {
		case PJP_LOADING_LAYOUT:

			noDataFoundLayout.setVisibility(View.GONE);
			errorInLoadingPjpLayout.setVisibility(View.GONE);
			pjpLoadingLayout.setVisibility(View.VISIBLE);

			break;
		case ERROR_LOADING_PJP_LAYOUT:

			pjpLoadingLayout.setVisibility(View.GONE);
			noDataFoundLayout.setVisibility(View.GONE);
			errorInLoadingPjpLayout.setVisibility(View.VISIBLE);

			break;

		case NO_PJP_FOUND_LAYOUT:

			errorInLoadingPjpLayout.setVisibility(View.GONE);
			pjpLoadingLayout.setVisibility(View.GONE);
			noDataFoundLayout.setVisibility(View.VISIBLE);

			break;

		default:

			errorInLoadingPjpLayout.setVisibility(View.GONE);
			pjpLoadingLayout.setVisibility(View.GONE);
			noDataFoundLayout.setVisibility(View.GONE);

			break;
		}
	}

	/**
	 * 
	 * @param date
	 */
	private void getPjpDataFromServer(final String userId, final String date) {

		// Clearing previous Items from list others wise loading progress bar
		// will blend in
		if (viewPjpListAdapter != null) {
			viewPjpListAdapter.setData( new ArrayList<ImeiStockModel>());
			viewPjpListAdapter.notifyDataSetChanged();
		}

		/** showing pjp loading layout */
		showHelperLayout(PJP_LOADING_LAYOUT);

		/** Creating background process for loading PJPs */
		BackgroundProcess getPjpBP = new BackgroundProcess(this,false);
		getPjpBP.setbackgroundProcess(new IProcess() {

			@Override
			public Object underProcess() throws Exception {

				try {
					return webCall.tryGetStock(MainActivity.MyInfo.CounterCode);
				} catch (Exception e) {
					return null;
				}
			}

			@Override
			public void processResponse(Object response) {

				if (response == null) {
					showHelperLayout(ERROR_LOADING_PJP_LAYOUT);


					return;
				}

				try {
					JSONObject viewPjpResponse = (JSONObject) response;

						extractPjpListFromJson(viewPjpResponse);
			
				} catch (Exception e) {

					showHelperLayout(ERROR_LOADING_PJP_LAYOUT);
					new Dialog(ViewStockActivity.this).setMessage(e.getMessage())
							.setTitle("Error").show();
				}
			}
		});
		getPjpBP.execute();
	}

	private void extractPjpListFromJson(JSONObject response) {
		try {

			if (response == null) {

				new Dialog(ViewStockActivity.this)
						.setMessage(
								"Unable To Fetch Stock List ,Error In Conecting to Server")
						.setTitle("Error").show();

				return;
			}
			JSONObject viewPjpResponse = (JSONObject) response;

			if (viewPjpResponse.has("StatusCode")
					&& viewPjpResponse.getString("StatusCode")
							.equalsIgnoreCase("0")) {

				ArrayList<ImeiStockModel> stockList = extractImeiListFromJson(viewPjpResponse);

				if (stockList.size() == 0) {
					new Dialog(ViewStockActivity.this)
							.setMessage("Your Stock List is Empty")
							.setTitle("Information").show();
					showHelperLayout(NO_PJP_FOUND_LAYOUT);
					return;
				}

				showHelperLayout(HIDE_ALL_LAYOUT);
				updateDataInPjpListview(stockList);

			} else {
				if (viewPjpResponse.has("StatusMsg"))
					new Dialog(ViewStockActivity.this).setMessage(
							viewPjpResponse.getString("StatusMsg")).show();
				
				showHelperLayout(ERROR_LOADING_PJP_LAYOUT);

			}

		} catch (Exception e) {

			new Dialog(ViewStockActivity.this)
					.setMessage("Unable To Fetch Stock List ," + e.getMessage())
					.setTitle("Error").show();

			showHelperLayout(ERROR_LOADING_PJP_LAYOUT);
		}

	}

	private ArrayList<ImeiStockModel> extractImeiListFromJson(
			JSONObject responseJsonObject) {

		ArrayList<ImeiStockModel> stockList = new ArrayList<ImeiStockModel>();
		ImeiStockModel stock;

		try {

			JSONArray dataArray = responseJsonObject.getJSONArray("data");

			if (dataArray == null || dataArray.length() == 0) {

				return stockList;
			}

			for (int i = 0; i < dataArray.length(); i++) {
				stock = new ImeiStockModel();
				JSONObject object = dataArray.getJSONObject(i);
				stock.SerialNumber1 = object.getString("SerialNumber1");
				stock.SerialNumber2 = object.getString("SerialNumber2");
				stock.ColorName = object.getString("ColorName");
				stock.ModelCode = object.getString("ModelCode");
				stock.ModelName = object.getString("ModelName");
				stock.ProductCategoryName = object
						.getString("ProductCategoryName");
				stock.SKUCode = object.getString("SKUCode");
				stock.SKUName = object.getString("SKUName");
				stock.ProductName = object.getString("ProductName");

				stockList.add(stock);
			}

		} catch (Exception e) {

			new Dialog(ViewStockActivity.this).setMessage(e.getMessage())
					.setTitle("Error").show();
		}

		return stockList;
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
	}
}
