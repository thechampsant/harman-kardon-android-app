package com.fieldforce.harmonkardonff;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import mob.field.harmonkardonff.entitiymodels.CompProductModel;
import mob.field.harmonkardonff.entitiymodels.MDisplay;
import mob.field.harmonkardonff.entitiymodels.MOPModel;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.entitiymodels.SalesModel;
import mob.field.harmonkardonff.services.WebService;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import app.core.base.InnosolsActivity;
import app.core.model.ImageCaptureResult;
import app.core.model.Response;

import com.fieldforce.harmonhelper.App;

public class MOPFormManagerActivity extends InnosolsActivity {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newdisplay);
		activate();
	}

	public void activate() {
		initializeName();
		SetHomeButtonHandler();
		SetSubmitButtonHandler();
		SetOutletName();
		AttachOnItemSelectedEventOnAutocompleteMastCat();
		AttachOnItemSelectedEventOnAutocompleteSubCat();

		AttachOnItemSelectedEventOnAutocompleteModel();
		// AttachOnItemSelectedEventOnAutocompleteProductD();
		AttachOnItemSelectedEventOnAutocompleteProduct();

		FillMastCatCombo();

		CustomizeDatePicker();
		// App.SetOnClickListenerOnButton(R.id.btn_img, img);
		findViewById(R.id.btn_img).setOnClickListener(img);
	}
	

	private void initializeName() {
		((TextView)findViewById(R.id.itemsalefordate)).setText("MOP");
		((TextView)findViewById(R.id.TextView01)).setText("Price");
		((EditText)findViewById(R.id.tbxqty)).setHint("Enter Price");
		
	}

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

	private void AttachOnItemSelectedEventOnAutocompleteProductD() {

		// TODO Auto-generated method stub

		Spinner spinner = (Spinner) this.findViewById(R.id.tbxdemonst);
		// Spinner spinner = (Spinner) this.findViewById(R.id.spmodels);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				SelectedProduct = (ProductModel) arg0.getSelectedItem();
				// String model = (String) arg0.getItemAtPosition(arg2);
				// FillProductcombo(model);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

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
				this, android.R.layout.simple_spinner_dropdown_item,
				MainActivity.GetProducts(MastCat, SubCat, model));
		sp.setAdapter(spinnerArrayAdapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			ImageCaptureResult result = getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);
			// showImageToastAfterImageCapture(result);
			if (result.getTotalImagesCount() > 0) {
				setDocsIDs(result.getDocsIDs(),
						result.getOnlineImagesCount() > 0);
			}
		}
	}

	public void setDocsIDs(String docsIDs, boolean IsImageUploaded) {
		// this.CurrentMDAT.DocIDs = docsIDs;
		// this.IsImageUploaed = IsImageUploaded;
		CurrentDisplayModel.DocIDs = docsIDs;

	}

	OnClickListener img = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent I = new Intent(MOPFormManagerActivity.this,
					ImageCaptureActivity.class);
			I.putExtra("DocType", "MOP");
			I.putExtra("GUID", CurrentDisplayModel.guid);
			I.putExtra(ImageCaptureActivity.PARAMS_USERNAME, User.GetUserName());
			startActivityForResult(I, 1);

		}

	};

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

	private void SetOutletName() {
		// TODO Auto-generated method stub

		((TextView) findViewById(R.id.outname))
				.setText(MainActivity.MyInfo.CurrentStore);

		// Fill Details in Model
		CurrentDisplayModel.OutName = MainActivity.MyInfo.CurrentStore;
		CurrentDisplayModel.OutID = App.ToInt(MainActivity.MyInfo.StoreID);
	}

	private void FillMastCatCombo() {
		// TODO Auto-generated method stub
		Spinner sp = (Spinner) findViewById(R.id.spmastcat);

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				MainActivity.GetMasterCategories());
		sp.setAdapter(spinnerArrayAdapter);

	}

	private void FillSubCatCombo(String MastCat) {
		// TODO Auto-generated method stub

		Spinner sp = (Spinner) findViewById(R.id.spsubcat);

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				MainActivity.GetMOPSubCategories(MastCat));
		sp.setAdapter(spinnerArrayAdapter);
	}

	private void AttachOnItemSelectedEventOnAutocompleteSubCat() {
		// TODO Auto-generated method stub

		Spinner spinner = (Spinner) findViewById(R.id.spsubcat);
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

		Button btn_btnhome = (Button) findViewById(R.id.btnhomee);

		btn_btnhome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				// TODO Auto-generated method stub

				// NavigateTo(new HomeManager(), null);
			}
		});

	}

	private void FillModelcombo() {
		// TODO Auto-generated method stub
		Spinner sp = (Spinner) this.findViewById(R.id.spmodels);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				MainActivity.GetModels(MastCat, SubCat));
		sp.setAdapter(spinnerArrayAdapter);
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
		LinearLayout lay = new LinearLayout(this);

		// Create Label For Model.
		TextView lbl = new TextView(this);
		lbl.setText(product.Name + " :");

		// Create TextBox For Model Price.
		EditText tbx = new EditText(this);
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
		Spinner sp = (Spinner) this.findViewById(R.id.spsubcat);

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				MainActivity.GetSubCategories(MastCat));
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

	public void OnSubmitClick() throws IOException {
		ShowAllModelOfThisCategory();
		// UpdateDisplayModel();
		// updateDisplayModel();
		SendMessageForUpdatingDP();
	}

	private boolean Validate(MOPModel mdispaly) {
		// TODO Auto-generated method stub
		if (Integer.valueOf(mdispaly.Price) == 0) {
			ShowToast("Please Enter a Valid Price");
			return false;
		} else if(Integer.valueOf(mdispaly.Price)<=0){
			ShowToast("Price can not be negative");
			return false;}
			
			
		 else
			return true;

	}

	private MOPModel updateDisplayModel() {
		MOPModel model = new MOPModel();
		model.OutName = MainActivity.MyInfo.CurrentStore;
		model.OutID = App.ToInt(MainActivity.MyInfo.StoreID);

		// Fill Product Details
		/*
		 * model.PID = product.PID; model.ProductName = product.Name;
		 */
		model.PID = Integer.valueOf(SelectedProduct.PID);
		model.ProductName = SelectedProduct.Name;
		// Basic Details
		model.CreatedOn = GetCurrentDateTimeInString();
		// model.ForDate = GetCurrentDateTimeInString();
		model.ForDate = GetDateInString(R.id.dpmopfordate);
		model.IsOfflineOnly = "true";

		// FillCategories
		model.Cat1 = MastCat;
		model.Cat2 = SubCat;
		model.Price = getEditTextAsString();
		model.DocIDs = CurrentDisplayModel.DocIDs;
		// Qty

		return model;
	}

	private String getEditTextAsString() {
		String price = ((EditText)findViewById(R.id.tbxqty)).getText().toString();
		if(price.equalsIgnoreCase("")){
			return "0";
		}
		else
		return price;
	}

	private void UpdateDisplayModel() throws IOException {

		IsSucc = true;
		CurrentDisplay = new ArrayList<MDisplay>();
		int index = 0;
		for (CompProductModel product : MyModels) {
			MDisplay model = new MDisplay();

			// Fill Outlet Details

			model.OutName = MainActivity.MyInfo.CurrentStore;
			model.OutID = App.ToInt(MainActivity.MyInfo.StoreID);

			// Fill Product Details
			model.PID = product.PID;
			model.ProductName = product.Name;

			// Basic Details
			model.CreatedOn = GetCurrentDateTimeInString();
			// model.ForDate = GetCurrentDateTimeInString();
			model.ForDate = GetDateInString(R.id.dpmopfordate);
			model.IsOfflineOnly = "true";

			// FillCategories
			model.Cat1 = MastCat;
			model.Cat2 = SubCat;

			// Qty
			model.Price = (GetEditTextAsString(index));
			if (Integer.valueOf(model.Price) == 0)
				IsSucc = false;

			CurrentDisplay.add(model);
			index++;
		}
		if (IsSucc) {
			AddLocally(CurrentDisplay);
			OnUpdateComplete();
		} else {
			ShowToast("Please Enter a Valid  Qty");
		}

	}

	private void AddLocally(ArrayList<MDisplay> currentDisplay2) {
		// TODO Auto-generated method stub
		try {
			for (MDisplay model : currentDisplay2) {
				AddNewDisplayLocally(model);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SetMessage("Not able to save");
			e.printStackTrace();
		}
	}

	private void AddNewDisplayLocally(MDisplay newDisplay) {
		// TODO Auto-generated method stub
		newDisplay.IsOfflineOnly = "true";
		// MainActivity.Database.LoadMOPFromDb();
		boolean IsUpdated = false;
		if (MainActivity.MyDisplays.size() > 0) {
			for (MDisplay model : MainActivity.MyDisplays) {
				if (model.ForDate.equalsIgnoreCase(newDisplay.ForDate)) {
					if (model.IsOfflineOnly.equalsIgnoreCase("true")) {
						if (model.PID == newDisplay.PID) {
							model.Price += newDisplay.Price;
							IsUpdated = true;
							UpdateIntoDB(model);
						}
					}
				}
			}
		}

		if (!IsUpdated) {
			// MainActivity.MyMOPs.add(newMOP);
			InsertIntoDB(newDisplay);
		}

	}

	private void InsertIntoDB(MDisplay newDisplay) {
		// TODO Auto-generated method stub

		try {
			newDisplay.DocIDs = MainActivity.Current
					.GetDocIDs(CurrentDisplayModel.guid);
			newDisplay.InsertOrUpdate();
			ShowToast("Display Updated In local Db");
		} catch (Exception ex) {
			Log.e("Error:", ex.toString());
		}

	}

	public void SetMessage(String _msg) {
		TextView txt = (TextView) findViewById(R.id.tvmessage);
		txt.setText(_msg);
	}

	private void UpdateIntoDB(MDisplay newDP) {
		// TODO Auto-generated method stub

		ContentValues values = new ContentValues();
		values.put("Qty", newDP.Price);

		// Update Entry in Database.
		String selection = "guid" + " LIKE ?";
		String[] selectionArgs = { String.valueOf(newDP.guid) };

		newDP.UpdateEntr(values, selection, selectionArgs);
		ShowToast("Display Updated in Local Db");
	}

	private void OnUpdateComplete() {
		// NavigationManager.GoToMySalesForm2(service.overallsalesdata.SelectedSaleData);
		/*
		 * ArrayList<MDisplay> localdisplays = new ArrayList<MDisplay>(); for
		 * (MDisplay model : MainActivity.MyDisplays) { if (model.IsOfflineOnly)
		 * { localdisplays.add(model); } }
		 */

		// NavigationManager.GoToMySalesForm2(salesforupdate);

		/*
		 * IScreenManager displayForm = new DisplayManager();
		 * NavigateTo(displayForm, null);
		 */

		// CurrentNavigationManager.NavigateToHome();
		SendMessageForUpdatingDP();
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

	/*
	 * public void LoadDisplaysFromDb() {
	 * 
	 * // TODO Auto-generated method stub MDisplay dummy = new MDisplay();
	 * ArrayList<MDisplay> data = MainActivity.db.FetchAllData(dummy).where(
	 * "UserName", MainActivity.MyInfo.EmployeeCode); MainActivity.MyDisplays =
	 * new ArrayList<MDisplay>(); MainActivity.MyDisplays = data; }
	 */
	WebService server = new WebService();
	

	private void DO_GPRS_Communication() {
		// TODO Auto-generated method stub
		Response resp = null;
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
			MOPModel mdispaly = updateDisplayModel();
			if (Validate(mdispaly)){
				showProgress("loading..");
			 resp = server.CreateNewMOP(mdispaly);}
		} catch (Exception e) {
			Log.e("Error:", e.toString());
		}
		if (resp!=null) {
			hideProgress();
			if(resp.isSuccess()){
			ShowToast("update successfully");
			finish();}
		 else {
			ShowToast(resp.errormsg);
		}}
		// ProcesResponse(String.valueOf(result));
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}
}
