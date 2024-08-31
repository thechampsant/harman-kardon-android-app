package mob.field.harmonkardonff.fragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import linq.ArrayList;
import mob.field.harmonkardonff.BLL.WebCall;
import mob.field.harmonkardonff.entitiymodels.ViewDisplayModel;
import mob.field.harmonkardonff.services.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import app.core.base.IFragment;

import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.customAdapter.CustomAdapter;
import com.fieldforce.customAdapter.CustomAdaterInterface;
import com.fieldforce.utility.Storage;
import com.google.gson.Gson;

public class DisplayEnters extends IFragment {

	WebService server = new WebService();
	private ListView listvw;
	ArrayList<ViewDisplayModel> whplvwdisplymodel = new ArrayList<ViewDisplayModel>();
	ArrayList<ViewDisplayModel> vwdisplymodel = new ArrayList<ViewDisplayModel>();
	private Button btn_add;

	@Override
	public void Activate(View arg0) {
		// hitapifordisplay();
		// onsubmitclick(arg0);
		// onaddclick(arg0);
		// initialise(arg0);

	}

	Activity activity;
	static DisplayEnters displayEnterObj;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = InflateView(R.layout.activity_viewdisplay, inflater, container);
		displayEnterObj = this;
		activity = getActivity();
		hitapifordisplay();
		onsubmitclick(v);
		onaddclick(v);
		initialise(v);
		return v;

	}

	private void onaddclick(View v) {
		btn_add = (Button) v.findViewById(R.id.add);
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setTab(0);

			}
		});

	}

	private void initialise(View v) {
		listvw = (ListView) v.findViewById(R.id.list_vwdsply);

	}

	String PIDValue = "";
	String QTYValue = "";
	String BrandidValue = "";
	String DisplyidValue = "";

	private void onsubmitclick(View v) {

		v.findViewById(R.id.Submit).setOnClickListener(new OnClickListener() {
			String PID = "";
			String QTY = "";
			String Brandid = "";
			String DisplyId = "";

			@Override
			public void onClick(View v) {

				if (whplvwdisplymodel.size() != 0) {
					Brandid = String.valueOf(whplvwdisplymodel.get(0)
							.getBrandID());
					for (int i = 0; i < whplvwdisplymodel.size(); i++) {
						boolean checkupdatewdvalues = whplvwdisplymodel.get(i).isupdated;
						if (checkupdatewdvalues) {
							try {

								int pid = whplvwdisplymodel.get(i).getPID();
								int qty = whplvwdisplymodel.get(i)
										.getDisplayQTY();
								int id = whplvwdisplymodel.get(i).getID();
								PID += String.valueOf(pid) + ",";
								QTY += String.valueOf(qty) + ",";
								DisplyId += String.valueOf(id) + ",";
							} catch (Exception e) {
								// TODO: handle exception
							}

						}
					}

					if (!PID.equalsIgnoreCase("") || !QTY.equalsIgnoreCase("")
							|| !DisplyId.equalsIgnoreCase("")) {
						PIDValue = PID.substring(0, PID.length() - 1);
						QTYValue = QTY.substring(0, QTY.length() - 1);
						DisplyidValue = DisplyId.substring(0,
								DisplyId.length() - 1);

						hitapionsubmit();
					} else {
						Storage.ShowToast("Plz make Changes to Submit",
								activity);
					}

				}
			}
		});

	}

	protected void hitapionsubmit() {

		if (Storage.isNetworkFoundToast(activity)) {
			new submitapi().execute();
		}

	}

	public static DisplayEnters getDisplayEnterObj() {
		return displayEnterObj;
	}

	public void hitapifordisplay() {
		if (Storage.isNetworkFoundToast(activity)) {
			hitdisplayapi hit = new hitdisplayapi();
			hit.execute();

		}

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
		((TextView) views.findViewById(R.id.txt_categoryvlue))
				.setText(categoryName);

		((TextView) views.findViewById(R.id.txt_sbcategoryvlue))
				.setText(subCatName);

		((TextView) views.findViewById(R.id.txt_modelvlue)).setText(modelName);

		((TextView) views.findViewById(R.id.txt_Skuvlue)).setText(skuName);
		views.findViewById(R.id.lnr_hide_unhide).setVisibility(View.GONE);

	}

	protected void setdatatolistvw() {
		final CustomAdapter adapter = new CustomAdapter<ViewDisplayModel>(
				activity, whplvwdisplymodel, R.layout.activity_vwdisplay);
		adapter.setAdapterInterface(new CustomAdaterInterface<ViewDisplayModel>() {

			@Override
			public void getView(final ViewDisplayModel model, final View rowview) {
				getProductDetail(String.valueOf(model.getPID()), rowview);
				EditText edt = (EditText) rowview.findViewById(R.id.edtext_qty);
				edt.setText(String.valueOf(model.getDisplayQTY()));
				LinearLayout lnr_lyt = (LinearLayout) rowview
						.findViewById(R.id.lnr_hide_unhide);

				rowview.findViewById(R.id.imgvw).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								int visblty = rowview.findViewById(
										R.id.lnr_hide_unhide).getVisibility();
								if (visblty == View.VISIBLE) {
									rowview.findViewById(R.id.lnr_hide_unhide)
											.setVisibility(View.GONE);
									rowview.findViewById(R.id.imgvw)
											.setBackgroundResource(
													R.drawable.down);
								} else {
									rowview.findViewById(R.id.lnr_hide_unhide)
											.setVisibility(View.VISIBLE);
									rowview.findViewById(R.id.imgvw)
											.setBackgroundResource(
													R.drawable.arrow_upward);
								}

							}
						});
				rowview.findViewById(R.id.imgvw_incqty).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								String qtyvlue = ((EditText) rowview
										.findViewById(R.id.edtext_qty))
										.getText().toString();
								if (qtyvlue != null && !qtyvlue.isEmpty()) {
									int value = Integer.valueOf(qtyvlue);
									if (value > 0 || value == 0) {
										value++;
										int newvalue = value;
										((EditText) rowview
												.findViewById(R.id.edtext_qty))
												.setText(String
														.valueOf(newvalue));
										model.setIsupdated(true);
										model.setDisplayQTY(newvalue);

									}
								}

							}
						});
				rowview.findViewById(R.id.imgvw_lssqty).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								String qtyvlue = ((EditText) rowview
										.findViewById(R.id.edtext_qty))
										.getText().toString();
								if (qtyvlue != null && !qtyvlue.isEmpty()) {
									int value = Integer.valueOf(qtyvlue);
									if (value > 0) {
										value--;
										int newvalue = value;
										((EditText) rowview
												.findViewById(R.id.edtext_qty))
												.setText(String
														.valueOf(newvalue));
										model.setDisplayQTY(newvalue);
										model.setIsupdated(true);

									}
								}

							}
						});

			}

		});

		listvw.setAdapter(adapter);
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

	private JSONObject responseJson;
	private Dialog mDialog;

	public class hitdisplayapi extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mDialog = ProgressDialog.show(activity,
					getString(R.string.app_name), "please wait", true);
			mDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			String apiurl = server.hitdisplayRequest();
			responseJson = WebCall.getJsonObjectResponse(apiurl);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (responseJson != null) {
				String status;
				try {
					status = responseJson.getString("status");
					if (status.equalsIgnoreCase("true")) {
						JSONArray data = responseJson.getJSONArray("data");
						vwdisplymodel.clear();
						convertgsontoarray(data);
						getdataforwhpl();
						setdatatolistvw();
						mDialog.dismiss();

					} else {
						mDialog.dismiss();
						ShowToast(responseJson.getString("errormsg"));

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				ShowToast("Network Problem");
			}
		}

		public void convertgsontoarray(JSONArray data) {

			Gson g = new Gson();
			try {
				for (int i = 0; i < data.length(); i++) {

					JSONObject jobj = data.getJSONObject(i);
					ViewDisplayModel proSkuPozo = g.fromJson(jobj.toString(),
							ViewDisplayModel.class);
					vwdisplymodel.add(proSkuPozo);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	Dialog pgd;

	class submitapi extends AsyncTask<Void, Void, Void> {
		private JSONObject jsnobject;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pgd = ProgressDialog.show(activity, getString(R.string.app_name),
					"please wait", true);
			pgd.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			String apiUrl = server.ApiUrl;
			apiUrl += "EditDisplay?";
			try {
				apiUrl += "BrandID=" + URLEncoder.encode(BrandidValue, "UTF-8");
				apiUrl += "&UserName="
						+ URLEncoder.encode(MainActivity.MyInfo.EmployeeCode,
								"UTF-8");
				apiUrl += "&ForDate="
						+ URLEncoder.encode(
								MainActivity.Current.GetCurrentDateInString(),
								"UTF-8");
				apiUrl += "&PID=" + URLEncoder.encode(PIDValue, "UTF-8");
				apiUrl += "&Qty=" + URLEncoder.encode(QTYValue, "UTF-8");
				apiUrl += "&DisplayId="
						+ URLEncoder.encode(DisplyidValue, "UTF-8");
				jsnobject = WebCall.getJsonObjectResponse(apiUrl);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (jsnobject != null) {
				try {
					String status = jsnobject.getString("status");
					pgd.dismiss();
					if (status.equalsIgnoreCase("true")) {
						Storage.ShowToast("Display Updated Successfully",
								activity);
						setdisplaytab(true);

					} else {
						Storage.ShowToast(jsnobject.getString("errormsg"),
								activity);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Storage.ShowToast("Network Problem", activity);
			}

		}

	}

	public void getdataforwhpl() {
		whplvwdisplymodel.clear();
		for (int i = 0; i < vwdisplymodel.size(); i++) {
			int brandid = vwdisplymodel.get(i).getBrandID();
			if (brandid == 2) {
				whplvwdisplymodel.add(vwdisplymodel.get(i));
			}
		}

	}

	public void setdisplaytab(boolean b) {
		if (b == true) {
			setTab(2);
		}

	}
	// @Override
	// public void onPause() {
	// // TODO Auto-generated method stub
	// super.onPause();
	// hitapifordisplay();
	// Log.d("refreshing", "refreshing");
	// }
}
