package com.fieldforce.harmonkardonff.Comptition;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.APIService.ApiClient;
import com.fieldforce.harmonkardonff.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vspl.docopd.API.Apiintefacec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import mob.field.harmonkardonff.services.WebService;
import retrofit2.Call;
import retrofit2.Callback;

public class CompetitionEnter extends IFragment {
    RecyclerView recyclerView;
    List<CompitionModel> displaypic = new ArrayList<>();
    ComptitionAdpter mAdapter;
    mob.field.harmonkardonff.services.WebService web = new mob.field.harmonkardonff.services.WebService();
    Button btn_Submit;

    @Override
    public void Activate(View FragmentView) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_pic_cat);
        btn_Submit= (Button) findViewById(R.id.btn_Submit);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                {
                    submitonserver();
                }
            }
        });
        SetTextViewAsString(R.id.tv_date,"Current Date:- "+GetCurrentDateInString());
        if (recyclerView != null) {
            ShowToast("working");

            loadFromServer();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return this.InflateView(R.layout.comptition_layout, inflater,
                container);
    }
    public void submitonserver(){
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        JSONArray jsonArray = new JSONArray();



        {
            try {

                if(displaypic.size()>0){
                    CompitionModel diplayComModelMain=new CompitionModel();
                    for (int i = 0; i < displaypic.size(); i++) {
                        JSONObject params = new JSONObject();
                        diplayComModelMain=   (CompitionModel) displaypic.get(i);

                        params.put("CompQuestID", diplayComModelMain.ID);
                        params.put("Qty",diplayComModelMain.Qty);
                        params.put("Username", WebService.UserName);
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
            Call<String> call = apiService.SubmitDisplayPic(jsonArray.toString());
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
                                setTab(2);
                                // onBackPressed();
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
    private boolean validate() {
        for(int i=0;i<displaypic.size();i++)
        {
            if(displaypic.get(i).IsMandatory.equalsIgnoreCase("true")&&
                    displaypic.get(i).Qty.equalsIgnoreCase("")) {
                ShowToast("Please enter qty first of " + displaypic.get(i).Question);
                return false;
            }
        }

        return true;
    }
    public void loadFromServer() {

        // this.getListView(R.id.lv_sale_mtd).setVisibility(View.GONE);
        BackgroundProcess bp = new BackgroundProcess(this);

        bp.setbackgroundProcess(new IProcess() {

            @Override
            public void processResponse(Object arg0) throws Exception {
                // TODO Auto-generated method stub
                ProcessServerResponse((Response) arg0);
            }

            @Override
            public Object underProcess() throws Exception {
                // TODO Auto-generated method stub
                return web.GetCompetitionQuestion();
            }
        });
        bp.execute(null, null, null);

    }
    private void ProcessServerResponse(Response response) {
        if (response.status.equalsIgnoreCase("true")) {
            displaypic = response.data;
            mAdapter = new ComptitionAdpter(displaypic, this,this.context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            /* mAdapter.notifyDataSetChanged();*/

        } else {
            new Dialog(getActivity()).setTitle("Error").show(response.errormsg);
        }
    }


}

