package com.fieldforce.harmonkardonff.demo_tracking_module.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.services.WebService;

/**
 */
public class EnterDemoFragment extends IFragment {


    private Spinner spinnerCat;
    private Spinner spinnerSubCat;
    private Spinner spinnerModel;
    private Spinner spinnerLeadType;

    private EditText editTextName;
    private Button btn_no_sale;
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
      //  MastCat = MainActivity.GetMasterCategories().get(0);
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
        spinnerLeadType = (Spinner) findViewById(R.id.spinner_leadTypeInDemo);
        btn_no_sale = (Button) findViewById(R.id.btn_no_sale);

        editTextName = (EditText) findViewById(R.id.et_custNameInDemo);
        editTextNumber = (EditText) findViewById(R.id.et_custPhoneInDemo);
        editTextMail = (EditText) findViewById(R.id.et_mailInDemo);
        editTextAge = (EditText) findViewById(R.id.et_ageInDemo);
        textViewSubmit = (TextView) findViewById(R.id.tv_submitDemo);
        btn_no_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();

            }
        });
        initSpinnersTask();
    }

    private void initSpinnersTask(){

        getProductDemoList();
        getLeadTypeList();
        attachListenerToModelSpinner();
      /*  populateCategorySpinner();
        AttachListenerToCatSpinner();
        attachListenerToSubCatSpinner();
        attachListenerToModelSpinner();*/
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
    ArrayList<String> arrayList=new ArrayList<>();
    private void populateModelSpinner(String subCat){

        for(ProductModel e :resdata)
        {
            arrayList.add(e.ProductName);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item, arrayList);
      //  Log.d(TAG, "populateModelSpinner: MastCat : "+MastCat+" SubCat : "+Cat+" Model : "+subCat);
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


                SelectedProduct = resdata.get(parent.getSelectedItemPosition());

                Log.e("SelectedProduct",SelectedProduct.ID+"nulll");
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
        if (spinnerLeadType.getSelectedItem() == null || spinnerLeadType.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            ShowToast("Please select Lead Type...");
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



    private void ShowDialog() {
        new AlertDialog.Builder(this.context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("No demo")
                .setMessage("Are you sure you want to submit “No demo” for the day? ")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                model.UserName = WebService.UserName;
                                model.CustomerName = "";
                                model.CustomerMob = "";
                                model.NoDemo = "true";

                                model.DemoProdId ="0";
                                makeApiCall(model);
                            }
                        }).setNegativeButton("No", null).show();

    }

    private EnterDemoRequestModel model = new EnterDemoRequestModel();
    private void prepareModel(){
        model.UserName = WebService.UserName;
        model.CustomerName = editTextName.getText().toString();
        model.CustomerMob = editTextNumber.getText().toString();
        model.NoDemo = "false";
       /* model.CustomerPhone = editTextNumber.getText().toString();
        model.CustomerEmail = editTextMail.getText().toString();
        model.CustomerAge = editTextAge.getText().toString();*/
        model.DemoProdId = SelectedProduct.ID;
        model.LeadType = spinnerLeadType.getSelectedItem() != null ? spinnerLeadType.getSelectedItem().toString() : "";
    }

    private void getLeadTypeList() {
        BackgroundProcess bp = new BackgroundProcess(this).showProgress(false);
        bp.setbackgroundProcess(new IProcess() {
            @Override
            public void processResponse(Object arg0) throws Exception {
                java.util.List<String> list = (java.util.List<String>) arg0;
                if (list != null && !list.isEmpty()) {
                    list.add(0, "Select");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, list);
                    spinnerLeadType.setAdapter(adapter);
                }
            }
            @Override
            public Object underProcess() throws Exception {
                return webService.GetLeadType();
            }
        });
        bp.execute();
    }

    private void getProductDemoList() {
        BackgroundProcess bp = new BackgroundProcess(this).showProgress(false);
        bp.setbackgroundProcess(new IProcess() {

            @SuppressWarnings("rawtypes")
            @Override
            public void processResponse(Object arg0) throws Exception {
                // TODO Auto-generated method stub
                processMinAppVersionResponse((Response) arg0);

            }

            @Override
            public Object underProcess() throws Exception {

                return webService.TryUpdateDemoProducts();
            }
        });

        bp.execute();

    }


    public ArrayList<ProductModel> resdata = new ArrayList<ProductModel>();
    protected void processMinAppVersionResponse(Response response) {

        // TODO Auto-generated method stub
        Response res = (Response)response;

        if (res.data != null && res.data.size() > 0) {
            resdata =  res.data;
            Log.e("Sizeeeee", String.valueOf(resdata.size()));

            populateModelSpinner("");
            hideProgress();
        } else {
            new Dialog(getActivity()).setTitle("Error").show(response.errormsg);
        }

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

                    return webService.SubmitDemoToServerNew(model);
                }
            });

            bp.execute(null, null, null);
        }
        else {
            ShowToast("No internet...");
        }
    }
}
