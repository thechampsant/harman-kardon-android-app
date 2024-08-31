package mob.field.harmonkardonff.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.Viewsalemodel;
import mob.field.harmonkardonff.services.WebService;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;

import com.fieldforce.harmonkardonff.R;

public class viewsale extends IFragment {

	private String startDate;
	private String endDate;
	WebService webapi;
	private GenricAdapter<Viewsalemodel> adapter;
	ArrayList<Viewsalemodel> viewsale_Arry = new ArrayList<Viewsalemodel>();

	@Override
	public void Activate(View FragmentView) {
		webapi = new WebService();
		initializeDatesAndServerRequest();
		startDateProcess(R.id.txt_for_dates, R.id.btn_for_dates);
		endDateProcessed(R.id.txt_to_dates, R.id.btn_to_dates);
		((TextView) findViewById(R.id.view_sale_current_user_textview)).setText("Current User : "+WebService.UserName);

	}

	private void initializeDatesAndServerRequest() {
		// TODO Auto-generated method stub
		initializeStartDate();
		initializeEndDate();
		if (isNetworkAvailable())
			hitapi(startDate, endDate);

		else {
			ShowToast("No Network available for View attendance");

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return this.InflateView(R.layout.activity_sale_view, inflater,
				container);
	}

	private void initializeStartDate() {
		String date = GetCurrentDateInString();
		startDate = date;
		SetTextViewAsString(R.id.txt_for_dates, date);
	}

	private void initializeEndDate() {

		String date = GetCurrentDateInString();
		endDate = date;
		SetTextViewAsString(R.id.txt_to_dates, date);
	}

	private void startDateProcess(int idForDateTxt, int idForDateButton) {
		final TextView dateText = (TextView) findViewById(idForDateTxt);
		final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd", Locale.US);
		Calendar newCalendar = Calendar.getInstance();
		final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
				getActivity(), new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar newDate = Calendar.getInstance();
						newDate.set(year, monthOfYear, dayOfMonth);
						dateText.setText(dateFormatter.format(newDate.getTime()));
						startDate = dateFormatter.format(newDate.getTime());

						if (endDate == null || endDate.equalsIgnoreCase(""))
							ShowToast("Please Enter To Date");

						else {
							hitapi(startDate, endDate);
						}
					}
				}, newCalendar.get(Calendar.YEAR), newCalendar
						.get(Calendar.MONTH), newCalendar
						.get(Calendar.DAY_OF_MONTH));
		fromDatePickerDialog.setCancelable(false);
		findViewById(idForDateButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						fromDatePickerDialog.show();
					}
				});
	}

	private void endDateProcessed(int idForDateTxt, int idForDateButton) {
		final TextView dateText = (TextView) findViewById(idForDateTxt);
		final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd", Locale.US);
		Calendar newCalendar = Calendar.getInstance();
		final DatePickerDialog endDatePickerDialog = new DatePickerDialog(
				getActivity(), new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar newDate = Calendar.getInstance();
						newDate.set(year, monthOfYear, dayOfMonth);
						dateText.setText(dateFormatter.format(newDate.getTime()));
						endDate = dateFormatter.format(newDate.getTime());
						if (startDate != null
								&& !startDate.equalsIgnoreCase(""))
							hitapi(startDate, endDate);
						else
							ShowToast("Please Enter From Date");

					}

				}, newCalendar.get(Calendar.YEAR), newCalendar
						.get(Calendar.MONTH), newCalendar
						.get(Calendar.DAY_OF_MONTH));
		endDatePickerDialog.setCancelable(false);
		findViewById(idForDateButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						endDatePickerDialog.show();
					}
				});
	}

	private void hitapi(final String start, final String end) {
		if (!isNetworkFoundToast()) {

			return;

		}

		BackgroundProcess bp = new BackgroundProcess(this)
				.setHorizantalScreenProgress().setbackgroundProcess(
						new IProcess() {

							@Override
							public Object underProcess() throws Exception {
								// TODO Auto-generated method stub
								return webapi.getsale(startDate, endDate);
							}

							@Override
							public void processResponse(Object response)
									throws Exception {
								Response resp = (Response) response;

								if (response == null) {
									return;
								}
								if (resp.isSuccess()) {
									clearlist();

									setdataonlist(resp.data);

								} else {
									clearlist();

									new Dialog(getActivity())
											.show(resp.errormsg);
								}
							}
						});
		bp.execute();

	}

	protected void setdataonlist(ArrayList data) {
		viewsale_Arry = data;

		ListView lv = getListView(R.id.list);
		lv.setClickable(true);
		adapter = new GenricAdapter<Viewsalemodel>(getActivity(),
				R.layout.list_item_sale);

		adapter.setGenricAdapter(new IAdapter<Viewsalemodel>() {

			@Override
			public void setItemView(final Viewsalemodel item, View view,
					int index) {
				((ImageView) view.findViewById(R.id.myimg))
						.setVisibility(View.GONE);
				if (item.CustomerName != null) {
					((TextView) view.findViewById(R.id.txt_customer_name))
							.setText(item.CustomerName);

				}
				if (item.Email != null&&!item.Email.equalsIgnoreCase("null")) {
					((TextView) view.findViewById(R.id.txt_customer_Email))
							.setText(item.Email);

				}
				if (item.MobileN0 != null) {
					((TextView) view.findViewById(R.id.txt_customer_mobile))
							.setText(item.MobileN0);

				}
				if (item.Date != null) {
					((TextView) view.findViewById(R.id.tbxdate))
							.setText(item.Date);

				}
				if (item.SKU != null) {
					((TextView) view.findViewById(R.id.tbxscat))
							.setText(item.SKU);

				}
				if (item.Qty != null) {
					((TextView) view.findViewById(R.id.txt_item))
							.setText("Qty\n"+item.Qty);

				}

			}
		});
		adapter.setData(viewsale_Arry);
		lv.setAdapter(adapter);

	}

	protected void clearlist() {
		viewsale_Arry.clear();
		if (adapter != null) {
			adapter.notifyDataSetChanged();

		}
	}

}
