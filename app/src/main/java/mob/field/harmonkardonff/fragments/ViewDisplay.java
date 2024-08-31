package mob.field.harmonkardonff.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import linq.ArrayList;
import mob.field.harmonkardonff.BLL.DisplayViewApi;
import mob.field.harmonkardonff.BLL.WebCall;
import mob.field.harmonkardonff.entitiymodels.MDisplay;
import mob.field.harmonkardonff.entitiymodels.SalesModel;
import mob.field.harmonkardonff.entitiymodels.ViewDisplayModel;
import mob.field.harmonkardonff.services.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import app.core.base.IFragment;
import app.core.utils.onDateSetListener;

import com.fieldforce.harmonkardonff.AsyncResp;
import com.fieldforce.harmonkardonff.CustomSpinner;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SpinnerResponse;
import com.fieldforce.utility.Storage;

public class ViewDisplay extends IFragment {

	List<String> temp = new ArrayList<String>();
	SalesModel CurrentDisplayModel = new SalesModel();
	ArrayList<MDisplay> CurrentDisplay = new ArrayList<MDisplay>();
	LinearLayout linearlayout;

	WebService webService = new WebService();
	private String currentDate;

	@Override
	public void Activate(View FragmentView) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		currentDate = dateFormat.format(date);
		spinnerBrand();
		startDateProcess(R.id.txt_for_date, R.id.btn_for_date);
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	private JSONObject responseJson;
	public class GetDisplayAsy extends AsyncTask<Void, Void, Void> {

		  private Dialog mDialog;

		  @Override
		  protected void onPreExecute() {
			   try {
			    mDialog = ProgressDialog.show(getActivity(),
			      getString(R.string.app_name),
			      "please wait", true);
			    mDialog.setCancelable(false);
			   } catch (Exception e) {
			    e.printStackTrace();
			   }
		  }

		  @Override
		  protected Void doInBackground(Void... params) {
			   try {
				   responseJson = WebCall.getJsonObjectResponse(apiUrl);
			    return null;
			   } catch (Exception e) {
			    e.printStackTrace();
	
			    return null;
			   }
		  }

		  @SuppressWarnings("unchecked")
		  @Override
		  protected void onPostExecute(final Void st) {
			   if (responseJson!=null) {
				    if (mDialog != null) {
					     mDialog.dismiss();
					    }
					    try {
							JSONArray jsonArray = responseJson.getJSONArray("data");
							if (jsonArray.length() != 0) {
								getDisplayValues(jsonArray);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
				   }
			  }
		 }
	String apiUrl;
	private void sendApiRequest() {
		apiUrl = WebService.ApiUrl;
		apiUrl += "GetDisplayView?";
		apiUrl += "username=" + MainActivity.MyInfo.EmployeeCode;
		apiUrl += "&date=" + currentDate;
		if(Storage.isNetworkFoundToast(getActivity()))
		{
			new GetDisplayAsy().execute();
		}
		
	}

	private void deleteWhirlApiRequest(int id, final int childNum,
			final LinearLayout ll) {
		String apiUrl = WebService.ApiUrl;
		apiUrl += "DeleteDisplay?";
		apiUrl += "DocIDs=" + id;
		final ProgressDialog pd = Storage.showProgressBar(getActivity(), "");
		DisplayViewApi displayViewApi = new DisplayViewApi(apiUrl);
		displayViewApi.setResponse(new AsyncResp() {
			@Override
			public void response(JSONObject result) {
				Storage.dismissProgressBar(pd);

				try {
					if (result != null) {
						if (result.getString("status").equalsIgnoreCase("true")) {
							ll.removeViewAt(childNum);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		displayViewApi.execute();
	}

	ArrayList<ViewDisplayModel> whrlpoolArr = new ArrayList<ViewDisplayModel>();
	ArrayList<ViewDisplayModel> otherArr = new ArrayList<ViewDisplayModel>();

	private void getDisplayValues(JSONArray jarr) {
		whrlpoolArr.clear();
		otherArr.clear();
		for (int i = 0; i < jarr.length(); i++) {
			try {
				JSONObject jObject = (JSONObject) jarr.getJSONObject(i);
				ViewDisplayModel viewDisplayModel = new ViewDisplayModel();
				viewDisplayModel.ID = jObject.getInt("ID");
				viewDisplayModel.PID = jObject.getInt("PID");
				viewDisplayModel.DisplayQTY = jObject.getInt("DisplayQTY");
				viewDisplayModel.BrandID = jObject.getInt("BrandID");
				// viewDisplayModel.InsertOrUpdate();
				if (viewDisplayModel.BrandID == 2) {
					whrlpoolArr.add(viewDisplayModel);
				} else {
					otherArr.add(viewDisplayModel);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		whirlpoolView(whrlpoolArr);
		otherBrandView(otherArr);
	}

	OnClickListener editTextClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setTab(0);
		}
	};

	private void attachDatePicker() {

		this.attachDatePicker(R.id.btn_for_date, new onDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, Date date, String sdate) {
				SetTextViewAsString(R.id.txt_for_date, sdate);
				currentDate = sdate;
				sendApiRequest();
			}
		});
	}

	OnClickListener deleteTextClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			LinearLayout ll = ((LinearLayout) findViewById(R.id.lyt_sale70));
			searchAndDeleteClickedView(v, ll, true);
		}
	};

	private void searchAndDeleteClickedView(View v, LinearLayout ll,
			boolean whrlpoolOrOther) {

		for (int i = 0; i < ll.getChildCount(); i++) {
			if (((View) v.getParent().getParent()).getId() == ll.getChildAt(i)
					.getId()) {
				int pid;
				if (whrlpoolOrOther)
					pid = whrlpoolArr.get(i).ID;
				else
					pid = otherArr.get(i).ID;
				deleteWhirlApiRequest(pid, i, ll);
			}
		}
	}

	OnClickListener deleteotherClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			LinearLayout ll = ((LinearLayout) findViewById(R.id.lyt_sal80));
			searchAndDeleteClickedView(v, ll, false);
		}
	};

	private void whirlpoolView(ArrayList<ViewDisplayModel> whrlpoolArr) {

		// View view = Storage.getView(getActivity().getApplicationContext(),
		// R.layout.inflate_catttt);
		// ((LinearLayout) findViewById(R.id.lyt_sale7)).addView(view);
		((LinearLayout) findViewById(R.id.lyt_sale70)).removeAllViews();
		for (int i = 0; i < whrlpoolArr.size(); i++) {
			View views = Storage.getView(getActivity().getApplicationContext(),
					R.layout.category_view);
			views.setId(i);
			int pid = whrlpoolArr.get(i).PID;
			getProductDetail(pid + "", views);
			((TextView) views.findViewById(R.id.TextView011))
					.setText(whrlpoolArr.get(i).DisplayQTY + "");

			// ((TextView) views.findViewById(R.id.TextView021))
			// .setOnClickListener(editTextClick);
			((TextView) views.findViewById(R.id.TextView031))
					.setOnClickListener(deleteTextClick);
			((LinearLayout) findViewById(R.id.lyt_sale70)).addView(views);

		}
	}

	private void otherBrandView(ArrayList<ViewDisplayModel> otherArr) {

		// View view = Storage.getView(getActivity().getApplicationContext(),
		// R.layout.other_brand);
		((LinearLayout) findViewById(R.id.lyt_sal80)).removeAllViews();

		for (int i = 0; i < otherArr.size(); i++) {
			View views = Storage.getView(getActivity().getApplicationContext(),
					R.layout.other_cat_brand);
			views.setId(i);
			int pid = otherArr.get(i).PID;
			getotherProductDetail(pid + "", views);
			((TextView) views.findViewById(R.id.model11)).setText(otherArr
					.get(i).DisplayQTY + "");
			((TextView) views.findViewById(R.id.TextView031))
					.setOnClickListener(deleteotherClick);
			((LinearLayout) findViewById(R.id.lyt_sal80)).addView(views);
		}
	}

	private void getotherProductDetail(String skuID, View views) {
		String categoryName = MainActivity.MyBrandCat.where("ID", skuID)
				.Select("Name").ToString().get(0);
		((TextView) views.findViewById(R.id.model210)).setText(categoryName);
	}

	private void getProductDetail(String skuID, View views) {
		String categoryName = MainActivity.MyProductList.where("PID", skuID)
				.Select("Cat1").ToString().get(0);
		String subCatName = MainActivity.MyProductList.where("PID", skuID)
				.Select("Cat2").ToString().get(0);
		String modelName = MainActivity.MyProductList.where("PID", skuID)
				.Select("Cat3").ToString().get(0);
		String skuName = MainActivity.MyProductList.where("PID", skuID)
				.Select("Name").ToString().get(0);
		((TextView) views.findViewById(R.id.model21)).setText(categoryName);
		((TextView) views.findViewById(R.id.model11)).setText(subCatName);
		((TextView) views.findViewById(R.id.model131)).setText(modelName);
		((TextView) views.findViewById(R.id.txt_mtd_ach1)).setText(skuName);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return this.InflateView(R.layout.view_display, inflater, container);
	}

	private void startDateProcess(int idForDateTxt, int idForDateButton) {
		final TextView dateText = (TextView) findViewById(idForDateTxt);
		dateText.setText("" + Storage.getCurrentDateYMD());
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
						currentDate = dateFormatter.format(newDate.getTime());
						sendApiRequest();
					}

				}, newCalendar.get(Calendar.YEAR), newCalendar
						.get(Calendar.MONTH), newCalendar
						.get(Calendar.DAY_OF_MONTH));

		findViewById(idForDateButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						fromDatePickerDialog.show();
					}
				});
	}

	private void spinnerBrand() {
		temp.clear();
		temp = MainActivity.MyBrand.Select("Name");
		CustomSpinner cSpinner = new CustomSpinner(getActivity(),
				findViewById(R.id.tbxdemonst), "Select Brand", temp);
		cSpinner.setSpinnerListener(new SpinnerResponse() {

			@Override
			public void onItemSelectListener(Spinner spinner) {
				showRelatedLayout(spinner);
			}

		});
	}

	private void showRelatedLayout(Spinner spinner) {
		String selectedActi = spinner.getSelectedItem().toString();
		if (selectedActi.equalsIgnoreCase("Whirlpool")) {
			findViewById(R.id.lyt_sale6).setVisibility(View.VISIBLE);
			findViewById(R.id.lyt_sale5).setVisibility(View.GONE);
		} else {
			findViewById(R.id.lyt_sale5).setVisibility(View.VISIBLE);
			findViewById(R.id.lyt_sale6).setVisibility(View.GONE);
		}
	}
	
}