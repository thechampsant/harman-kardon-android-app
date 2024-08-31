package mob.field.harmonkardonff.fragments;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.CompProductModel;
import mob.field.harmonkardonff.entitiymodels.MDisplay;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.entitiymodels.SalesModel;
import mob.field.harmonkardonff.services.WebService;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.ImageCaptureResult;
import app.core.model.Response;

import com.fieldforce.harmonkardonff.CustomSpinner;
import com.fieldforce.harmonkardonff.ImageCaptureActivity;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SpinnerResponse;
import com.fieldforce.harmonhelper.App;

//import java.util.ArrayList;

public class DisplayEnterForm extends IFragment {

	List<String> temp = new ArrayList<String>();
	private static boolean IsSucc = true;
	CharSequence lastamount = "0";
	SalesModel CurrentDisplayModel = new SalesModel();
	ArrayList<MDisplay> CurrentDisplay = new ArrayList<MDisplay>();
	String MastCat = "";
	String SubCat = "";
	ProductModel SelectedProduct = null;
	ArrayList<CompProductModel> MyModels = new ArrayList<CompProductModel>();
	LinearLayout linearlayout;

	ArrayList<EditText> tbxList = new ArrayList<EditText>();
	private String selectedActi;

	@Override
	public void onResume() {
		super.onResume();
//		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			ImageCaptureResult result = getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);
			showImageToastAfterImageCapture(result);
			if (result.getTotalImagesCount() > 0) {
				setDocsIDs(result.getDocsIDs(),
						result.getOnlineImagesCount() > 0);
			}
		}
	}
	
	public void setDocsIDs(String docsIDs, boolean IsImageUploaded) {
		this.CurrentDisplayModel.DocIDs = docsIDs;
		this.IsImageUploaed = IsImageUploaded;

	}
	private boolean IsImageUploaed = false;
	private void showImageToastAfterImageCapture(ImageCaptureResult result) {
		if (result.getTotalImagesCount() == 1)
			ShowToastLong(result.getTotalImagesCount() + " image is attached",
					0);
		else if (result.getTotalImagesCount() > 1)
			ShowToastLong(result.getTotalImagesCount() + " image are attached",
					0);
		else
			ShowToastLong("No image found to attach", 0);

		if (result.getOfflineImagesCount() == 1)
			ShowToastLong(result.getOfflineImagesCount()
					+ " image is still pending to upload", 0);
		else if (result.getOfflineImagesCount() > 1)
			ShowToastLong(result.getOfflineImagesCount()
					+ " image are still pending to upload", 0);
	}
	@Override
	public void Activate(View FragmentView) {
		// TODO Auto-generated method stub
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		activate();
		spinnerSoName();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return this.InflateView(R.layout.displayenter, inflater, container);
	}

	private void spinnerSoName() {
		temp.clear();
		/*
		 * for(int i =0;i<MainActivity.MyBrand.size();i++){ BrandModel arrayList
		 * = MainActivity.MyBrand.get(i); temp.add(arrayList.NAME); }
		 */
		temp = MainActivity.MyBrand.Select("Name");
		CustomSpinner cSpinner = new CustomSpinner(getActivity(),
				findViewById(R.id.tbxdemonst), "Select Brand", temp);
		cSpinner.setSpinnerListener(new SpinnerResponse() {

			@Override
			public void onItemSelectListener(Spinner spinner) {
				String selectedItem = spinner.getSelectedItem().toString();

				startOrinflateActivity(spinner);
			}

		});
	}

	private void startOrinflateActivity(Spinner spinner) {
		int id[] = { R.id.model1, R.id.spnnr_subcateg, R.id.model13, R.id.spmodels,
				R.id.txt_mtd_ach, R.id.spproducts };
		selectedActi = spinner.getSelectedItem().toString();
		if (selectedActi.equalsIgnoreCase("Whirlpool")) {
			for (int i = 0; i < id.length; i++) {
				findViewById(id[i]).setVisibility(View.VISIBLE);
			}
			refreshDataForWhirlpool();
		} else {
			for (int i = 0; i < id.length; i++) {
				findViewById(id[i]).setVisibility(View.GONE);
			}
			refreshDataForOtherProduct();
		}

		/*
		 * if (selectedActi.equalsIgnoreCase("Whirlpool")) {
		 * findViewById(R.id.RelativeLayout1).setVisibility( View.VISIBLE);
		 * findViewById(R.id.RelativeLayout2).setVisibility( View.GONE);
		 * activate(); } else {
		 * findViewById(R.id.RelativeLayout2).setVisibility( View.VISIBLE);
		 * findViewById(R.id.RelativeLayout1).setVisibility( View.GONE);
		 * activate1(); }
		 */
	}

	private String brandID = "";
	private boolean isWhirlpoolSelect = true;

	private void refreshDataForOtherProduct() {
		isWhirlpoolSelect = false;
		spinnerMasterArray.clear();
		String id = MainActivity.MyBrand.where("Name", selectedActi).get(0).ID;
		brandID = id;
		ArrayList<String> otherProductData = MainActivity.MyBrandCat
				.where("BrandID", id).Select("Name").ToString();// Select("Name").Distinct().ToString();
		for (String val : otherProductData)
			spinnerMasterArray.add(val);
		spinnerMasterArrayAdapter.notifyDataSetChanged();
	}

	private void refreshDataForWhirlpool() {
		String id = MainActivity.MyBrand.where("Name", selectedActi).get(0).ID;
		brandID = id;
		isWhirlpoolSelect = true;
		spinnerMasterArray.clear();
		ArrayList<String> otherProductData = MainActivity.GetMasterCategories();
		for (String val : otherProductData)
			spinnerMasterArray.add(val);
		spinnerMasterArrayAdapter.notifyDataSetChanged();
	}

	public void activate() {
		SetHomeButtonHandler();
		
		SetOutletName();
		FillMastCatCombo();
		AttachOnItemSelectedEventOnAutocompleteMastCat();
		
		AttachOnItemSelectedEventOnAutocompleteSubCat();

		AttachOnItemSelectedEventOnAutocompleteModel();
		// AttachOnItemSelectedEventOnAutocompleteProductD();
		AttachOnItemSelectedEventOnAutocompleteProduct();

		

		CustomizeDatePicker();
		// App.SetOnClickListenerOnButton(R.id.btn_img, img);
		findViewById(R.id.btn_img).setOnClickListener(img);
		SetSubmitButtonHandler();
	
	}

	
	public void activate1() {
		// App.SetOnClickListenerOnButton(R.id.btn_img, img);
		findViewById(R.id.btn_im).setOnClickListener(img);
	}

	private void SetHomeButtonHandler() {
		// TODO Auto-generated method stub
		Button btnfinishSixthPage = (Button) findViewById(R.id.btnhome);
		btnfinishSixthPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// NavigationManager.GoToHome();
				// NavigateTo(new HomeManager(), null);
			}
		});
	}

	OnClickListener img = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent I = new Intent(getActivity(), ImageCaptureActivity.class);
			I.putExtra("DocType", "Display");
			I.putExtra("GUID", CurrentDisplayModel.guid);
			I.putExtra(ImageCaptureActivity.PARAMS_USERNAME, User.GetUserName());
			startActivityForResult(I, 1);

		}

	};

	private void AttachOnItemSelectedEventOnAutocompleteProduct() {

		Spinner spinner = (Spinner) this.findViewById(R.id.spproducts);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				SelectedProduct = (ProductModel) arg0.getSelectedItem();
				ShowAllModelOfThisCategory();
				/*
				 * CurrentSalesModel.PID = _SelectedProduct.PID;
				 * CurrentSalesModel.ProductName = _SelectedProduct.Name;
				 * CurrentSalesModel.Cat1 = _SelectedProduct.Cat1;
				 * CurrentSalesModel.Cat2 = _SelectedProduct.Cat2;
				 * CurrentSalesModel.Cat3 = _SelectedProduct.Cat3;
				 */
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

	}

	private void CustomizeDatePicker() {
		// TODO Auto-generated method stub
		DatePicker picker = (DatePicker) findViewById(R.id.dpmopfordate);
		try {
			Field f[] = picker.getClass().getDeclaredFields();
			for (Field field : f) {
				if (field.getName().equals("mDayPicker")) {
					field.setAccessible(true);
					Object yearPicker = new Object();
					yearPicker = field.get(picker);
					((View) yearPicker).setVisibility(View.GONE);
				}
			}
		} catch (SecurityException e) {
			Log.d("ERROR", e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.d("ERROR", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.d("ERROR", e.getMessage());
		}
	}

	private void AttachOnItemSelectedEventOnAutocompleteModel() {
		// TODO Auto-generated method stub
		Spinner spinner = (Spinner) this.findViewById(R.id.spmodels);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String model = (String) arg0.getItemAtPosition(arg2);
				FillProductcombo(model);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void FillProductcombo(String model) {
		Spinner sp = (Spinner) this.findViewById(R.id.spproducts);
		ArrayAdapter<ProductModel> spinnerArrayAdapter = new ArrayAdapter<ProductModel>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item,
				MainActivity.GetProducts(MastCat, SubCat, model));
		sp.setAdapter(spinnerArrayAdapter);
	}

	private void SetSubmitButtonHandler() {
		// TODO Auto-generated method stub
		Button btnfinishSixthPage = (Button) findViewById(R.id.btnSubmit);
		btnfinishSixthPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * ShowSToast("MOP Saved Successfully");
				 * CurrentNavigationManager.NavigateToHome();
				 */
				try {
					OnSubmitClick();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	public void OnSubmitClick() throws IOException {
		ShowAllModelOfThisCategory();
		// UpdateDisplayModel();
		// updateDisplayModel();
		SendMessageForUpdatingDP();
	}

	protected void ShowAllModelOfThisCategory() {
		// TODO Auto-generated method stub
		// CurrentDisplay = new ArrayList<MDisplay>();
		MyModels = MainActivity.GetMOPProducts(this.MastCat, this.SubCat);
		linearlayout = (LinearLayout) findViewById(R.id.rll);

		linearlayout.removeAllViews();
		linearlayout.setVisibility(View.VISIBLE);
		int i = 0;
		for (CompProductModel product : MyModels) {
			ShowPriceOptionForModel(product, i);
			i++;
		}
	}

	private void ShowPriceOptionForModel(CompProductModel product, int i) {
		// TODO Auto-generated method stub
		// Create a Horizontal Layout.
		LinearLayout lay = new LinearLayout(getActivity());

		// Create Label For Model.
		TextView lbl = new TextView(getActivity());
		lbl.setText(product.Name + " :");

		// Create TextBox For Model Price.
		EditText tbx = new EditText(getActivity());
		// if (!lastamount.equals("0"))
		// tbx.setText(lastamount);

		// tbx.setHint("Enter Qty (Not More than 100)");
		// tbx.setTag(index, model.PID);
		tbx.setId(i);
		tbx.setInputType(InputType.TYPE_CLASS_NUMBER);

		// tbxList.add(tbx);
		// linearLayout.setBackgroundColor(Color.TRANSPARENT);
		lay.addView(lbl);
		lay.addView(tbx);
		linearlayout.addView(lay);
	}

	private void SendMessageForUpdatingDP() {
		try {

			if (!isNetworkAvailable())
				return;
			// InitialStartup();
			// MainActivity.Database.LoadDisplaysFromDb();
			DO_GPRS_Communication();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	WebService server = new WebService();
	private MDisplay mdispaly;

	private void DO_GPRS_Communication() {
		// TODO Auto-generated method stub
		boolean result = false;
		try {
			/*
			 * ArrayList<MDisplay> MDisplays = MainActivity.MyDisplays.where(
			 * "IsOfflineOnly", "true");
			 */
			/*
			 * ArrayList<MDisplay> MDisplays = CurrentDisplay; for (MDisplay
			 * model : MDisplays) { result = server.CreateNewDP(model);
			 * 
			 * }
			 */
		mdispaly = updateDisplayModel();
			if (Validate(mdispaly))
				submitonserver();
		
		} catch (Exception e) {
			Log.e("Error:", e.toString());
		}
		
		// ProcesResponse(String.valueOf(result));
	}

	private void submitonserver() {
		BackgroundProcess bp=new BackgroundProcess(getActivity()).setProgressMessage("loading..");
		bp.setbackgroundProcess(new IProcess() {
			
			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				return server.CreateNewDP(mdispaly);
			}
			
			@Override
			public void processResponse(Object arg0) throws Exception {
			Response resp=(Response) arg0;
			if(resp!=null){
			if(resp.isSuccess()){
				ShowToast("Data updated successfully");
				setTab(2);
				
			}
			else{
				ShowToast(resp.errormsg);
			}
				
			}
			else{
				ShowToast("Network Problem");
			}
			}
		}).execute();
		
	}
	private void gotoViewDisplay(boolean Status) {
		if (Status == true) {
			setTab(1);
		}
	}

	private boolean Validate(MDisplay model) {
		// TODO Auto-generated method stub
		if (Integer.valueOf(model.Price) == 0) {
			ShowToast("Please Enter a Valid Display Quantity");
			return false;
		} else if (Integer.valueOf(model.Price) <= 0) {
			ShowToast("Display Quantity can not be negative");
			return false;
		} else
			return true;

	}

	private MDisplay updateDisplayModel() {
		MDisplay model = new MDisplay();
		model.OutName = MainActivity.MyInfo.CurrentStore;
		model.OutID = App.ToInt(MainActivity.MyInfo.StoreID);

		// Fill Product Details
		/*
		 * model.PID = product.PID; model.ProductName = product.Name;
		 */

		if (isWhirlpoolSelect)
			model.PID = Integer.valueOf(SelectedProduct.PID);
		else {
			String name = ((Spinner) findViewById(R.id.spmastcat))
					.getSelectedItem().toString();
			String id = MainActivity.MyBrandCat.where("Name", name).get(0).ID;
			model.PID = Integer.valueOf(id);
		}
//		model.ProductName = SelectedProduct.Name;
		// Basic Details
		model.CreatedOn = GetCurrentDateTimeInString();
		// model.ForDate = GetCurrentDateTimeInString();
		model.ForDate = GetDateInString(R.id.dpmopfordate);
		model.IsOfflineOnly = "true";

		model.BrandID = brandID;
		// FillCategories
//		model.Cat1 = MastCat;
//		model.Cat2 = SubCat;
		model.Price =getEditTextAsString();
		
		model.DocIDs = CurrentDisplayModel.DocIDs;
		// Qty

		return model;
	}

	private String getEditTextAsString() {
		String price = ((EditText) findViewById(R.id.tbxqty)).getText()
				.toString();
		if(price.equalsIgnoreCase("")){
			return "0";
		}
		return price;
	}

	private void SetOutletName() {
		// TODO Auto-generated method stub

		((TextView) findViewById(R.id.outname))
				.setText(MainActivity.MyInfo.CurrentStore);

		// Fill Details in Model
		CurrentDisplayModel.OutName = MainActivity.MyInfo.CurrentStore;
		CurrentDisplayModel.OutID = App.ToInt(MainActivity.MyInfo.StoreID);
	}

	ArrayAdapter<String> spinnerMasterArrayAdapter = null;
	ArrayList<String> spinnerMasterArray = null;

	private void FillMastCatCombo() {
		// TODO Auto-generated method stub
		Spinner sp = (Spinner) findViewById(R.id.spmastcat);
		spinnerMasterArray = MainActivity.GetMasterCategories();
		/*
		 * if(MyBrandCat!=null) { spinnerMasterArrayAdapter = new
		 * ArrayAdapter<String>( getActivity(),
		 * android.R.layout.simple_spinner_dropdown_item,
		 * MyBrandCat.Select("Name").Distinct().ToString()); } else {
		 */
		spinnerMasterArrayAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item,
				spinnerMasterArray);
		// }
		sp.setAdapter(spinnerMasterArrayAdapter);

	}

	private void AttachOnItemSelectedEventOnAutocompleteSubCat() {
		// TODO Auto-generated method stub

		Spinner spinner = (Spinner) findViewById(R.id.spnnr_subcateg);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				SubCat = (String) arg0.getItemAtPosition(arg2);
				// FillProductcombo(MastCat, SubCat);
				// ShowAllModelOfThisCategory();
				FillModelcombo();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

	private void FillModelcombo() {
		// TODO Auto-generated method stub
		Spinner sp = (Spinner) this.findViewById(R.id.spmodels);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item,
				MainActivity.GetModels(MastCat, SubCat));
		sp.setAdapter(spinnerArrayAdapter);
	}

	private void AttachOnItemSelectedEventOnAutocompleteMastCat() {
		// TODO Auto-generated method stub

		Spinner spinner = (Spinner) findViewById(R.id.spmastcat);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				MastCat = (String) arg0.getItemAtPosition(arg2);
				FillSubCatCombo();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

	private void FillSubCatCombo() {
		Spinner spinn = (Spinner) this.findViewById(R.id.spnnr_subcateg);
	
		ArrayAdapter<String> spinnrArrayAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item,MainActivity.GetSubCategories(MastCat)
				);
		spinn.setAdapter(spinnrArrayAdapter);
	}

}
