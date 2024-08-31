package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.Calendar;
import java.util.regex.Pattern;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.base.InnosolsActivity;
import app.core.model.Response;
import app.core.utils.Dialog;
import mob.field.harmonkardonff.entitiymodels.NewSaleModel;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.services.WebService;

public class SubmitDownStock extends IFragment {
    NewSaleModel CurrentSalesModel = new NewSaleModel();


    String MastCat = "";
    String SubCat = "";
    ImageView scann;
    // ProductModel SelectedProduct = null;
    ProductModel SelectedProduct = new ProductModel();

    WebService server = new WebService();

    @Override
    public void Activate(View FragmentView) {
        MastCat = MainActivity.GetMasterCategories().get(0);
        SetTextViewAsString(R.id.tbxInvoiceNumber,GetCurrentDateInString());
        attachDatePicker(R.id.tbxInvoiceNumber);
        tbxInvoiceNumber=(TextView) findViewById(R.id.tbxInvoiceNumber);
        tbxInvoiceNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                tbxInvoiceNumber.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                // tbxInvoiceNumber.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.activity_submit_down_stock, inflater, container);
    }

    @Override
    public void RegisterTableInfoForLocalDB() {

    }
   TextView tbxInvoiceNumber;

    public void OnSubmitClick() {

        SubmitSaleOnServer();
    }
    private boolean validateCustomer() {

        CurrentSalesModel.OutStockDate=GetTextViewAsString(R.id.tbxInvoiceNumber);
        CurrentSalesModel.StockQty=GetEditTextAsString(R.id.tbxStockQty);
        CurrentSalesModel.CurrentStock=GetEditTextAsString(R.id.tbxCurrentStock);
        CurrentSalesModel.DescribeIssue=GetEditTextAsString(R.id.tbxDescribeIssue);

        if(CurrentSalesModel.OutStockDate.equalsIgnoreCase(""))
        {
            ShowToast("Please Enter Out of Stock Date Value !");
            return false;
        }
        else if(  CurrentSalesModel.StockQty.equalsIgnoreCase(""))
        {
            ShowToast("Please Enter Stock Qty Value !");
            return false;
        }
        else if(CurrentSalesModel.CurrentStock.equalsIgnoreCase(""))
        {
            ShowToast("Please Enter Current Stock !");
            return false;
        }
        else if(CurrentSalesModel.DescribeIssue.equalsIgnoreCase(""))
        {
            ShowToast("Please Enter Describe the Issue !");
            return false;
        }
        else
            return true;
        /*
         * } else return true;
         */
    }
    private void SubmitSaleOnServer() {
        if (isNetworkAvailable()) {
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
                    return server.SubmitDownStock(CurrentSalesModel);
                }
            });

            bp.execute(null, null, null);
        } else {
            new Dialog(getActivity()).setTitle("Message").show(
                    "No network found!Try later");

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
//        Spinner sp = (Spinner) getActivity().findViewById(R.id.spsubcat);
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

        switch (requestCode) {

            //Response From Bar code Scanner

            case BarcodeScannerActivity.REQUEST_CODE:

                if (resultCode == Activity.RESULT_OK) {
                    Log.e("Code",data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_FORMAT));
                    switch (data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_FORMAT)) {

                        case "UPC_A":
                            String scannedCode = data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_DATA);
                            SetTextViewAsString(R.id.tbxInvoiceNumber,scannedCode);
                            new Dialog(getActivity()).show("Scanned Code : " + scannedCode);
                        case "Code-128":
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

//        Spinner sp = (Spinner) getActivity().findViewById(R.id.spproducts);
        ArrayAdapter<ProductModel> spinnerArrayAdapter = new ArrayAdapter<ProductModel>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetProducts(MastCat, SubCat, model));
        sp.setAdapter(spinnerArrayAdapter);
    }
    private void AttachOnItemSelectedEventOnAutocompleteProduct() {

//        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spproducts);
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
//        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spmodels);
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

        /*Spinner spinner = (Spinner) getActivity().findViewById(R.id.spsubcat);
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
//        Spinner sp = (Spinner) getActivity().findViewById(R.id.spmodels);
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
                sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmodels);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetModels(MastCat, SubCat));
        sp.setAdapter(spinnerArrayAdapter);
    }
}
