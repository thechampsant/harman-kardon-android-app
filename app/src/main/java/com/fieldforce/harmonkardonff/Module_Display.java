package com.fieldforce.harmonkardonff;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fieldforce.harmonhelper.GPSTracker;
import com.fieldforce.utility.PermissionUtils;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.regex.Pattern;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.base.InnosolsActivity;
import app.core.image.slider.AppConstant;
import app.core.model.Response;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.NewSaleModel;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.fragments.View_Module_List_new;
import mob.field.harmonkardonff.services.WebService;

 public class Module_Display extends IFragment {
    NewSaleModel CurrentSalesModel = new NewSaleModel();


    String MastCat = "";
    String SubCat = "";
    CardView scann;
    // ProductModel SelectedProduct = null;
    ProductModel SelectedProduct = new ProductModel();
     LocalStorage localStrObj;
    WebService server = new WebService();
     CardView btn_recent_entries1;
     @Override
     public void Activate(View FragmentView) {
         gpsService = new GPSTracker(getActivity());
         scann=(CardView) findViewById(R.id.scann);
         MastCat = MainActivity.GetMasterCategories().get(0);
          localStrObj = new LocalStorage(getActivity());
        // localStrObj.getMessage("add");

         btn_recent_entries1=(CardView) findViewById(R.id.btn_recent_entries1);
         if(localStrObj.getMessage("add").equalsIgnoreCase("AddMore"))
             btn_recent_entries1.setVisibility(View.GONE);
         else
             btn_recent_entries1.setVisibility(View.VISIBLE);

         if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
             // Do something for lollipop and above versions
             checkForAllPermissions();
         }
         scann.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(getActivity(), BarcodeScannerActivity.class);
                 startActivityForResult(intent,BarcodeScannerActivity.REQUEST_CODE);
             }
         });
         AttachOnItemSelectedEventOnAutocompleteSubCat();
         AttachOnItemSelectedEventOnAutocompleteModel();
         AttachOnItemSelectedEventOnAutocompleteProduct();

         FillSubCatCombo();
         GetButton(R.id.btn_submit_sale).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 CurrentSalesModel.SKU=SelectedProduct.PID;
                 if(validateCustomer())
                 {
                     OnSubmitClick();
                 }

             }
         });
         GetButton(R.id.btn_recent_entries).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                setTab(2);

             }
         });

     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         return this.InflateView(R.layout.activity_model_display, inflater, container);
        
     }

     @Override
    public void RegisterTableInfoForLocalDB() {

    }
     private String[] permissionsRequired = new String[]
             {
                     android.Manifest.permission.ACCESS_FINE_LOCATION,
                     android.Manifest.permission.CAMERA,
                     Manifest.permission.ACCESS_FINE_LOCATION,
             };
   

     private void checkForAllPermissions() {
         boolean isAllPermissionGranted = false;
         isAllPermissionGranted = PermissionUtils.checkForPermission(getActivity(), AppConstant.PERM_REQ_CODE, permissionsRequired);
         if (isAllPermissionGranted) {
         } else {

             Toast.makeText(getActivity(), "All Permission not granted...", Toast.LENGTH_SHORT).show();
         }
     }



     @Override
     public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == AppConstant.PERM_REQ_CODE && PermissionUtils.permissionsGrantedCheck(grantResults)) {
             //ShowToast("Permissions Granted");
             Toast.makeText(getActivity(), "Permissions Granted", Toast.LENGTH_SHORT).show();
             //splashHandler();
         } else {
             checkForAllPermissions();
         }
     }
     private GPSTracker gpsService;
     public void OnSubmitClick() {
        CurrentSalesModel.Latitude= String.valueOf(gpsService.getLatitude());
        CurrentSalesModel.Longitude= String.valueOf(gpsService.getLongitude());
         SubmitSaleOnServer();
     }
     private boolean validateCustomer() {

         CurrentSalesModel.BarCodeValue=GetTextViewAsString(R.id.tbxInvoiceNumber);

         if(CurrentSalesModel.BarCodeValue.equalsIgnoreCase(""))
         {
             ShowToast("Please Enter Barcode Value !");
             return false;
         }
         else
             return true;
         /*
          * } else return true;
          */
     }
     private void SubmitSaleOnServer() {
         if(localStrObj.getMessage("add").equalsIgnoreCase("AddMore"))
         {
             Log.e("IsManual",localStrObj.getMessage("IsManual"));
             ViewModuleModel viewModuleModel=new ViewModuleModel();
             viewModuleModel.SKUID=CurrentSalesModel.SKU;
             viewModuleModel.SKU=getSpinner(R.id.searchableSpinner_spproducts).getSelectedItem().toString();
             viewModuleModel.Category=getSpinner(R.id.searchableSpinner_spmodels).getSelectedItem().toString();;
             viewModuleModel.SubCategory=getSpinner(R.id.searchableSpinner_spsubcat).getSelectedItem().toString();;
             viewModuleModel.IsManual=localStrObj.getMessage("IsManual");
             viewModuleModel.BarCodeValue=CurrentSalesModel.BarCodeValue;
             viewModuleModel.DisplayRaiseDate=GetCurrentDateInString();
             viewModuleModel.Longitude=CurrentSalesModel.Longitude;
             viewModuleModel.Latitude=CurrentSalesModel.Latitude;
             View_Module_List_new.dataList.add(viewModuleModel);
             localStrObj.setMessage("add","local");
             setTab(2);
         }
         else{
             if (isNetworkAvailable()) {
                 CurrentSalesModel.IsManual=localStrObj.getMessage("IsManual");
                 BackgroundProcess bp = new BackgroundProcess(getActivity())
                         .setProgressMessage("sending to server..");
                 bp.setProgressDailogCancellable(false);
                 bp.setbackgroundProcess(new IProcess() {

                     @SuppressWarnings("rawtypes")
                     @Override
                     public void processResponse(Object arg0) throws Exception {
                         ProcessNewSaleResponse((Response) arg0);
                     }

                     @Override
                     public Object underProcess() throws Exception {
                         return server.SubmitDisplayModel(CurrentSalesModel);
                     }
                 });

                 bp.execute(null, null, null);
             } else {
                 new Dialog(getActivity()).setTitle("Message").show(
                         "No network found!Try later");

             }
         }


     }
     public static boolean isValid(String email)
     {
         Pattern pattern = Patterns.EMAIL_ADDRESS;
         return pattern.matcher(email).matches();
     }

     private void ProcessNewSaleResponse(Response response) {
         if (response.status.equalsIgnoreCase("true")) {
             this.ShowToast("Updated successfully!");
            

         } else {
             // AddNewSaleLocally(CurrentSalesModel);
             new Dialog(getActivity()).show(response.errormsg);
             //gotoSaleDisplay(true);

         }

     }
    private void FillSubCatCombo() {
//        Spinner sp = (Spinner) this.findViewById(R.id.spsubcat);
        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spsubcat);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetSubCategories(MainActivity.GetMasterCategories().get(0)));
        sp.setAdapter(spinnerArrayAdapter);


    }
     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         Log.e("Req", String.valueOf(requestCode));
         if (requestCode == AppConstant.PERM_REQ_CODE) {
             checkForAllPermissions();
         }
         switch (requestCode) {

             //Response From Bar code Scanner

             case BarcodeScannerActivity.REQUEST_CODE:

                 if (resultCode == Activity.RESULT_OK) {
                     Log.e("CodeCode",data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_FORMAT));
                     Log.e("CodeCode",data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_DATA));
                     switch (data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_FORMAT)) {

                         case "UPC_A":
                             String scannedCode = data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_DATA);
                             SetTextViewAsString(R.id.tbxInvoiceNumber,scannedCode);
                             new Dialog(getActivity()).show("Scanned Code : " + scannedCode);
                             case "CODE_128":
                             String scannedCode1 = data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_DATA);
                             SetTextViewAsString(R.id.tbxInvoiceNumber,scannedCode1);
                             new Dialog(getActivity()).show("Scanned Code : " + scannedCode1);
                             break;
                             default:
                             new Dialog(getActivity()).show("Invalid Bar Code Scanned");
                     }

                 }

                 else if (resultCode == Activity.RESULT_CANCELED)
                     new Dialog(getActivity()).show("Bar Code Scanning Cancelled");


                 break;
         }
     }

    private void FillProductcombo(String model) {
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
                sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spproducts);

//        Spinner sp = (Spinner) this.findViewById(R.id.spproducts);
        ArrayAdapter<ProductModel> spinnerArrayAdapter = new ArrayAdapter<ProductModel>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetProducts(MastCat, SubCat, model));
        sp.setAdapter(spinnerArrayAdapter);
    }
    private void AttachOnItemSelectedEventOnAutocompleteProduct() {

//        Spinner spinner = (Spinner) this.findViewById(R.id.spproducts);
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
                spinner = (SearchableSpinner) findViewById(R.id.searchableSpinner_spproducts);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                SelectedProduct = (ProductModel) arg0.getSelectedItem();
                // CurrentSalesModel.PID = SelectedProduct.PID;
                // fillmop(SelectedProduct);

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
    private void AttachOnItemSelectedEventOnAutocompleteModel() {
        // TODO Auto-generated method stub
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
                spinner = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmodels);
//        Spinner spinner = (Spinner) this.findViewById(R.id.spmodels);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

    private void AttachOnItemSelectedEventOnAutocompleteSubCat() {

        /*Spinner spinner = (Spinner) this.findViewById(R.id.spsubcat);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                SubCat = (String) arg0.getItemAtPosition(arg2);
                FillModelcombo();
                // FillProductcombo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });*/
        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spsubcat);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                SubCat = (String) arg0.getItemAtPosition(arg2);
                FillModelcombo();
                // FillProductcombo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    private void FillModelcombo() {
        // TODO Auto-generated method stub
//        Spinner sp = (Spinner) this.findViewById(R.id.spmodels);
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
                sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmodels);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetModels(MastCat, SubCat));
        sp.setAdapter(spinnerArrayAdapter);
    }
}