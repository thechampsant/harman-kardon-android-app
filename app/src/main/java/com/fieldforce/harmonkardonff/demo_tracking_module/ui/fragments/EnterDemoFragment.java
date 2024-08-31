package com.fieldforce.harmonkardonff.demo_tracking_module.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.demo_tracking_module.models.EnterDemoRequestModel;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.services.WebService;

/**
 */
public class EnterDemoFragment extends IFragment {

    public EnterDemoFragment() {
        // Required empty public constructor
    }

    private Spinner spinnerCat;
    private Spinner spinnerSubCat;
    private Spinner spinnerModel;

    private EditText editTextName;
    private EditText editTextNumber;
    private EditText editTextMail;
    private EditText editTextAge;
    private TextView textViewSubmit;

    private String MastCat="";
    private String Cat="";
    private String SubCat ="";

    private WebService webService = new WebService();

    private static final String TAG = "EnterDemoFragmentO";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.fragment_enter_demo, inflater, container);
    }

    @Override
    public void Activate(View FragmentView) {
        init();
    }

    private void init(){
        findIds();
        MastCat = MainActivity.GetMasterCategories().get(0);
        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    prepareModel();
                    makeApiCall(model);
                }
            }
        });
    }

    private void findIds(){
        spinnerCat = (Spinner) findViewById(R.id.spinner_categoryInDemo);
        spinnerSubCat = (Spinner) findViewById(R.id.spinner_subCategoryInDemo);
        spinnerModel = (Spinner) findViewById(R.id.spinner_modelInDemo);

        editTextName = (EditText) findViewById(R.id.et_custNameInDemo);
        editTextNumber = (EditText) findViewById(R.id.et_custPhoneInDemo);
        editTextMail = (EditText) findViewById(R.id.et_mailInDemo);
        editTextAge = (EditText) findViewById(R.id.et_ageInDemo);
        textViewSubmit = (TextView) findViewById(R.id.tv_submitDemo);
        initSpinnersTask();
    }

    private void initSpinnersTask(){
        populateCategorySpinner();
        AttachListenerToCatSpinner();
        attachListenerToSubCatSpinner();
        attachListenerToModelSpinner();
    }

    private void populateCategorySpinner(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetSubCategories(MainActivity.GetMasterCategories().get(0)));
        spinnerCat.setAdapter(spinnerArrayAdapter);
    }

    private void populateSubCatSpinner(String MastCat, String cat){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetModels(MastCat, cat));
        spinnerSubCat.setAdapter(spinnerArrayAdapter);
    }

    private void populateModelSpinner(String subCat){
        ArrayAdapter<ProductModel> spinnerArrayAdapter = new ArrayAdapter<ProductModel>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetProducts(MastCat, Cat, subCat));
        Log.d(TAG, "populateModelSpinner: MastCat : "+MastCat+" SubCat : "+Cat+" Model : "+subCat);
        spinnerModel.setAdapter(spinnerArrayAdapter);
    }

    private void AttachListenerToCatSpinner(){
        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cat = spinnerCat.getItemAtPosition(position).toString();
                populateSubCatSpinner(MastCat,Cat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void attachListenerToSubCatSpinner(){
        spinnerSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubCat = spinnerSubCat.getItemAtPosition(position).toString();
                populateModelSpinner(SubCat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ProductModel SelectedProduct = new ProductModel();
    private void attachListenerToModelSpinner(){
        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedProduct = (ProductModel) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validation(){
        if (editTextName.getText().toString().trim().length()<2){
            ShowToast("Please provide valid customer name...");
            return false;
        }
        else {
            if (editTextNumber.getText().toString().trim().length()<10){
                ShowToast("Please provide valid phone number...");
                return false;
            }
            else {
                /*if (editTextAge.length()<2 || editTextAge.length()>3){
                    ShowToast("Please provide valid age...");
                    return false;
                }*/
                Log.d(TAG, "validation: "+editTextAge.getText().toString());
            }
        }
        return true;
    }

    private EnterDemoRequestModel model = new EnterDemoRequestModel();
    private void prepareModel(){
        model.UserName = WebService.UserName;
        model.CustomerName = editTextName.getText().toString();
        model.CustomerPhone = editTextNumber.getText().toString();
        model.CustomerEmail = editTextMail.getText().toString();
        model.CustomerAge = editTextAge.getText().toString();
        model.PID =SelectedProduct.PID;
    }


    private void makeApiCall(final EnterDemoRequestModel model){
        if (isNetworkAvailable()){
            BackgroundProcess bp = new BackgroundProcess(this.context).setProgressMessage("sending to server..");
            bp.setbackgroundProcess(new IProcess() {

                @SuppressWarnings("rawtypes")
                @Override
                public void processResponse(Object arg0) throws Exception {
                    Response response = (Response) arg0;
                    if (response.isSuccess())
                    {
                        if (response.status.equalsIgnoreCase("true")){
                            ShowToast("Demo Submitted Successfully...");
                            setTab(1);
                        }
                        else {
                            new Dialog(getActivity()).setTitle("Server Response").setMessage(response.errormsg);
                        }
                    }
                    else
                        ShowToastLong(response.errormsg, 0);

                }

                @Override
                public Object underProcess() throws Exception {

                    return webService.SubmitDemoToServer(model);
                }
            });

            bp.execute(null, null, null);
        }
        else {
            ShowToast("No internet...");
        }
    }
}
