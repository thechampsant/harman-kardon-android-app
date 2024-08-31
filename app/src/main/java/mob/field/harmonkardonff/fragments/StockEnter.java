package mob.field.harmonkardonff.fragments;

import java.util.ArrayList;
import java.util.Date;

import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.entitiymodels.StocksModel;
import mob.field.harmonkardonff.services.WebService;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import app.core.utils.onDateSetListener;

import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

@SuppressWarnings("unused")
public class StockEnter extends IFragment {

    StocksModel NewStock = new StocksModel();
    WebService web = new WebService();
    EditText stockQty;
    String MastCat = "";
    String SubCat = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return this.InflateView(R.layout.activity_stock_enter, inflater, container);
    }

    @Override
    public void Activate(View arg0) {
        try {
            SetOutletName();
            NewStock.ForDate = GetCurrentDateInString();
            SetTextViewAsString(R.id.txt_for_date, NewStock.ForDate);

            stockQty = GetEditText(R.id.txt_stockqty);
            SetOnClickListenerOnButton(R.id.btn_newstock, savestockclick);
            SetOnClickListenerOnButton(R.id.btn_nostock, nostock);
            MastCat = MainActivity.GetMasterCategories().get(0);
            AttachOnItemSelectedEventOnAutocompleteProduct();
            //	AttachOnItemSelectedEventOnAutocompleteMastCat();
            AttachOnItemSelectedEventOnAutocompleteSubCat();
            AttachOnItemSelectedEventOnAutocompleteModel();
            attachDatePicker();
//			FillMastCatCombo();
            FillSubCatCombo();
        } catch (Exception ex) {
            ShowToastLong(ex.getMessage(), 0);
            this.context.finish();
        }
    }

    private void AttachOnItemSelectedEventOnAutocompleteModel() {
        // TODO Auto-generated method stub
        /*Spinner spinner = (Spinner) this.findViewById(R.id.spmodels);
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

        });*/

        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmodels);
        //For set Title to Spinner
//        sp.setTitle("Select Company");
        // Creating ArrayAdapter using the string array and default spinner layout
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item,
//                MainActivity.GetMasterCategories());
        // Specify layout to be used when list of choices appears
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
//        sp.setAdapter(arrayAdapter);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String model = (String) parent.getItemAtPosition(position);
                FillProductcombo(model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        searchableSpinner_spmodels
    }

    private void FillProductcombo(String model) {
       /* Spinner sp = (Spinner) this.findViewById(R.id.spproducts);
        ArrayAdapter<ProductModel> spinnerArrayAdapter = new ArrayAdapter<ProductModel>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetProducts(MastCat, SubCat, model));
        sp.setAdapter(spinnerArrayAdapter);*/

        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spproducts);
        //For set Title to Spinner
//        sp.setTitle("Select Company");
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<ProductModel> arrayAdapter = new ArrayAdapter<ProductModel>(getActivity(), android.R.layout.simple_spinner_item,
                MainActivity.GetProducts(MastCat));
        // Specify layout to be used when list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        sp.setAdapter(arrayAdapter);

    }

    private void attachDatePicker() {

        this.attachDatePicker(R.id.btn_for_date, new onDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, Date date, String sdate) {
                SetTextViewAsString(R.id.txt_for_date, sdate);
                NewStock.ForDate = sdate;
            }
        });
    }

    OnClickListener savestockclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            hideKeyPad(R.id.btn_newstock);
            GetObject();
            if (!validate())
                return;
            SubmitStockOnServer();

        }
    };

    private void SubmitStockOnServer() {
        if (isNetworkAvailable()) {
            BackgroundProcess bp = new BackgroundProcess(this.context)
                    .setProgressMessage("sending to server..");
            bp.setbackgroundProcess(new IProcess() {

                @SuppressWarnings("rawtypes")
                @Override
                public void processResponse(Object arg0) throws Exception {
                    ProcessNewStockResponse((Response) arg0);
                }

                @Override
                public Object underProcess() throws Exception {
                    return web.TryUpdateStock(NewStock);
                }
            });

            bp.execute(null, null, null);
        } else {
            new Dialog(this.context).setTitle("Message").show(
                    "No network found!Try later");
            AddNewStockLocally(NewStock);
            gotoStockDisplay();
        }
    }

    @SuppressWarnings("rawtypes")
    private void ProcessNewStockResponse(Response response) {
        if (response.status.equalsIgnoreCase("true")) {
            this.ShowToast("Stock updated successfully!");
            NewStock.IsUpdated = "true";
            gotoStockDisplay();
        } else {
            new Dialog(this.context).show(response.errormsg);
        }
        AddNewStockLocally(NewStock);
        gotoStockDisplay();

    }

    private void AddNewStockLocally(StocksModel newStock) {
        if (MainActivity.MyStocks.size() < 1)
            newStock.InsertOrUpdate();
        else
            updateOldStock(newStock);
    }

    private void updateOldStock(StocksModel newStock) {
        StocksModel OldStock = MainActivity.MyStocks
                .where("ForDate", newStock.ForDate)
                .where("IsUpdated", newStock.IsUpdated.trim().toLowerCase())
                .where("PID", newStock.PID).First();
        if (OldStock != null) {
            OldStock.Qty += newStock.Qty;
            OldStock.InsertOrUpdate();
        } else {
            newStock.InsertOrUpdate();
        }
    }

    private void gotoStockDisplay() {
        setTab(1);
    }

    private StocksModel GetObject() {
        NewStock.Qty = GetEditTextAsInt(R.id.txt_stockqty);
        return NewStock;
    }

    OnClickListener nostock = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ShowDialog();

        }
    };

    private void ShowDialog() {
        new AlertDialog.Builder(this.context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("No sale")
                .setMessage("Are you sure for zero stock?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                GetObject();
                                if (validateDates())
                                    submitZeroStock();
                            }
                        }).setNegativeButton("No", null).show();

    }

    private void submitZeroStock() {
        this.NewStock.IsNoStock = "true";
        if (isNetworkAvailable()) {
            BackgroundProcess bp = new BackgroundProcess(this.context)
                    .setProgressMessage("sending to server..");
            bp.setbackgroundProcess(new IProcess() {

                @SuppressWarnings("rawtypes")
                @Override
                public void processResponse(Object arg0) throws Exception {
                    Response response = (Response) arg0;
                    if (response.isSuccess())
                        ShowToast("Stock updated successfully!");
                    else
                        ShowToastLong(response.errormsg, 0);

                    gotoStockDisplay();
                }

                @Override
                public Object underProcess() throws Exception {

                    return web.TryUpdateStock(NewStock);
                }
            });

            bp.execute(null, null, null);
        } else {
            new Dialog(this.context).setTitle("Message").show(
                    "No network found!Try later");
        }

    }

    private boolean validateDates() {
        int backday = 7;
        if (NewStock.ForDate == null) {
            ShowToast("Please select the date..");
            return false;
        }
        if (ConvertStringToDate(NewStock.ForDate).after(GetCurrentDate())) {
            ShowToast("Future Dates are not allowed");
            return false;
        } else if (ConvertStringToDate(NewStock.ForDate).before(
                getBackDate(backday))) {
            ShowToast("Only past " + backday + " days stock is allowed");
            return false;
        } else
            return true;
    }

    private boolean validate() {
        if (!validateDates())
            return false;
        else if (NewStock.Qty < 0) {
            stockQty.setError("Invalid Input");
            return false;
        } else
            return true;
    }

    private void SetOutletName() {
        ((TextView) this.findViewById(R.id.txt_outletname))
                .setText(MainActivity.MyInfo.CurrentStore);
    }

    private void AttachOnItemSelectedEventOnAutocompleteProduct() {
        /*Spinner spinner = (Spinner) this.findViewById(R.id.spproducts);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                ProductModel _SelectedProduct = (ProductModel) arg0
                        .getSelectedItem();
                NewStock.PID = ToInt(_SelectedProduct.PID);
                NewStock.ProductName = _SelectedProduct.Name;
                NewStock.Cat1 = _SelectedProduct.Cat1;
                NewStock.Cat2 = _SelectedProduct.Cat2;
                NewStock.Cat3 = _SelectedProduct.Cat3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });*/

        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spproducts);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                ProductModel _SelectedProduct = (ProductModel) arg0
                        .getSelectedItem();
                NewStock.PID = ToInt(_SelectedProduct.PID);
                NewStock.ProductName = _SelectedProduct.Name;
                NewStock.Cat1 = _SelectedProduct.Cat1;
                NewStock.Cat2 = _SelectedProduct.Cat2;
                NewStock.Cat3 = _SelectedProduct.Cat3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressWarnings("unused")
    private void AttachOnItemSelectedEventOnAutocompleteSubCat() {

        /*Spinner spinner = (Spinner) this.findViewById(R.id.spsubcat);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                SubCat = (String) arg0.getItemAtPosition(arg2);
                FillModelcombo();
                //FillProductcombo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });*/
        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                SubCat = (String) arg0.getItemAtPosition(arg2);
                FillModelcombo();
                //FillProductcombo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void FillModelcombo() {
        // TODO Auto-generated method stub
        /*Spinner sp = (Spinner) this.findViewById(R.id.spmodels);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetModels(MastCat, SubCat));
        sp.setAdapter(spinnerArrayAdapter);*/

        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmodels);
        //For set Title to Spinner
//        sp.setTitle("Select Company");
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                MainActivity.GetModels(MastCat, SubCat));
        // Specify layout to be used when list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        sp.setAdapter(arrayAdapter);


    }

    private void AttachOnItemSelectedEventOnAutocompleteMastCat() {
       /* Spinner spinner = (Spinner) this.findViewById(R.id.spmastcat);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                MastCat = (String) arg0.getItemAtPosition(arg2);
                FillProductcombo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });*/
        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmastcat);

        sp.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                MastCat = (String) arg0.getItemAtPosition(arg2);
                FillProductcombo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    private void FillMastCatCombo() {
        /*Spinner sp = (Spinner) this.findViewById(R.id.spmastcat);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetMasterCategories());

        sp.setAdapter(spinnerArrayAdapter);*/

        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmastcat);
        //For set Title to Spinner
//        sp.setTitle("Select Company");
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                MainActivity.GetMasterCategories());
        // Specify layout to be used when list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        sp.setAdapter(arrayAdapter);

    }

    private void FillSubCatCombo() {
        /*Spinner sp = (Spinner) this.findViewById(R.id.spsubcat);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetSubCategories(MastCat));
        sp.setAdapter(spinnerArrayAdapter);*/

        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner);
        //For set Title to Spinner
//        sp.setTitle("Select Company");
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                MainActivity.GetSubCategories(MastCat));
        // Specify layout to be used when list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        sp.setAdapter(arrayAdapter);
    }


    private void FillProductcombo() {
        /*Spinner sp = (Spinner) this.findViewById(R.id.spproducts);
        ArrayAdapter<ProductModel> spinnerArrayAdapter = new ArrayAdapter<ProductModel>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetProducts(MastCat));
        sp.setAdapter(spinnerArrayAdapter);*/

        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spproducts);
        //For set Title to Spinner
//        sp.setTitle("Select Company");
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<ProductModel> arrayAdapter = new ArrayAdapter<ProductModel>(getActivity(), android.R.layout.simple_spinner_item,
                MainActivity.GetProducts(MastCat));
        // Specify layout to be used when list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        sp.setAdapter(arrayAdapter);
    }

}
