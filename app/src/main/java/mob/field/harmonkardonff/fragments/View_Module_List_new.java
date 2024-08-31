package mob.field.harmonkardonff.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.APIService.ApiClient;
import com.fieldforce.harmonhelper.GPSTracker;
import com.fieldforce.harmonkardonff.LocalStorage;
import com.fieldforce.harmonkardonff.LoginActivity;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SameasPreviousAdpter;
import com.fieldforce.harmonkardonff.ViewModuleModel;
import com.fieldforce.harmonkardonff.ViewModuleRecyclerAdapter;
import com.fieldforce.utility.VerticalItemDecorator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vspl.docopd.API.Apiintefacec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.services.WebService;
import retrofit2.Call;
import retrofit2.Callback;

public class View_Module_List_new extends IFragment {

    private String startDate;
    private String endDate;
    WebService webapi;
    private RecyclerView recyclerView;
    private TextView textViewCurrentUser;
    private SameasPreviousAdpter mAdapter;
    ItemClickListener itemClickListener;
    private GPSTracker gpsService;
    public  static  ArrayList<ViewModuleModel> dataList;
    LocalStorage localStrObj;
    @Override
    public void Activate(View FragmentView) {
         localStrObj = new LocalStorage(getActivity());
        gpsService = new GPSTracker(getActivity());
        itemClickListener=new ItemClickListener() {
            @Override
            public void onClick(int position, String value) {

            if(value.equalsIgnoreCase("delete"))
            {
                dataList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
            else {

                localStrObj.setMessage("add","AddMore");
                setTab(0);
            }


            }
        };
        init();

        GetButton(R.id.btn_submit_sale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable())
                submitonserver();
                else {
                    new Dialog(getActivity()).setTitle("Message").show(
                            "No network found!Try later");
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.same_as_previous, inflater, container);
    }

    @Override
    public void RegisterTableInfoForLocalDB() {

    }



    private void init(){
        webapi = new WebService();
        textViewCurrentUser = (TextView) findViewById(R.id.tv_currentUserValue);
        recyclerView = (RecyclerView) findViewById(R.id.rv_viewDemo);

        mAdapter = new SameasPreviousAdpter(itemClickListener);
//        textViewCurrentUser.setText(WebService.UserName);
        initializeDatesAndServerRequest();
        initRecycler();

    }

    private void initRecycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalItemDecorator(30));
        recyclerView.setAdapter(mAdapter);
    }
    private void initializeDatesAndServerRequest() {
        // TODO Auto-generated method stub

        if (isNetworkAvailable())
            hitapi();

        else {
            ShowToast("No Network available for View attendance");

        }
    }
    private void hitapi() {
        if (!isNetworkFoundToast()) {
            return;
        }
        if(localStrObj.getMessage("add").equalsIgnoreCase("local"))
        {
            mAdapter.setData(dataList);
            return;
        }

        BackgroundProcess bp = new BackgroundProcess(this).showProgress(false);
        bp.setbackgroundProcess(
                new IProcess() {

                    @Override
                    public Object underProcess() throws Exception {
                        // TODO Auto-generated method stub
                        return webapi.getModuleFromServer();
                    }

                    @Override
                    public void processResponse(Object response)
                            throws Exception {
                        Response resp = (Response) response;

                        if (response == null) {
                            return;
                        }
                        if (resp.isSuccess())
                        {
                            if (resp.status.equalsIgnoreCase("true")){
                                //setdataonlist(resp.data);
                                dataList = resp.data;
                                mAdapter.setData(dataList);
                            }
                            else {
                                new Dialog(getActivity()).setTitle("Server Response").setMessage(resp.errormsg).show();
                            }
                        } else {
                            new Dialog(getActivity()).setTitle("Server Response").setMessage(resp.errormsg).show();
                        }
                    }
                });
        bp.execute();

    }

    public void submitonserver(){
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        JSONArray jsonArray = new JSONArray();



        {
            try {

                if(dataList.size()>0){
                    ViewModuleModel diplayComModelMain=new ViewModuleModel();
                    for (int i = 0; i < dataList.size(); i++) {
                        JSONObject params = new JSONObject();

                        diplayComModelMain=   (ViewModuleModel) dataList.get(i);
                        diplayComModelMain.Latitude= String.valueOf(gpsService.getLatitude());
                        diplayComModelMain.Longitude= String.valueOf(gpsService.getLongitude());
                        params.put("SKU", diplayComModelMain.SKU);
                        params.put("SKUID", diplayComModelMain.SKUID);
                        params.put("BarCodeValue", diplayComModelMain.BarCodeValue);
                        params.put("Username",WebService.getUsernameForUrl());
                        params.put("IsManual", diplayComModelMain.IsManual);
                        params.put("Longitude",diplayComModelMain.Longitude);
                        params.put("Latitude", diplayComModelMain.Latitude);
                        jsonArray.put(params);
                    }

                }


                Log.e("JSONArray", String.valueOf(jsonArray));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Type listType = new TypeToken<JSONArray>() {}.getType();
            Log.e("ServerData",new Gson().toJson(jsonArray, listType));
            Apiintefacec apiService = ApiClient.getClient1().create(Apiintefacec.class);
            Call<String> call = apiService.SubmitBulkDisplayModel(jsonArray.toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response.body());
                            boolean Status = jsonObject.getBoolean("status");
                            if (Status) {
                                progressDialog.hide();
                                ShowToast("Submit Successfully");
                                Intent intent=new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                // onBackPressed();
                            }
                            else {
                                progressDialog.hide();
                                new Dialog(getActivity()).show(jsonObject.getString("errormsg"));
                            }
                        } catch (JSONException e) {
                            progressDialog.hide();
                            e.printStackTrace();
                        }


                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.hide();
                    Log.e("Error",t.getMessage());

                }
            });

        }


        // Log.e("params", params.toString() + "Null");
    }
    public interface ItemClickListener {
        void onClick(int position,String value);
    }

}

