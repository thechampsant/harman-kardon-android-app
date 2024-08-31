package com.fieldforce.harmonkardonff;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.custom_adapters.NewTrainingModuleRecyclerAdapter;

import app.core.base.InnosolsActivity;
import app.core.model.Response;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.DocumentModel;
import mob.field.harmonkardonff.services.WebService;

public class NewTrainingActivity extends InnosolsActivity {

    @Override
    public void RegisterTableInfoForLocalDB() {

    }

    private RecyclerView recyclerView;
    private WebService server = new WebService();
    public Response resTargetVsAchivement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training);
        initRecycler();
        webAPICall();
    }

    private void initRecycler(){
        recyclerView = findViewById(R.id.rv_inTraining);
    }

    private void webAPICall()
    {
        if (server.isNetworkAvailable(NewTrainingActivity.this))
        {
            new NewTrainingActivity.RequestForIncentive().execute((Void) null);

        } else
            Toast.makeText(getApplicationContext(),
                    "Internet is not available", Toast.LENGTH_LONG).show();
    }


    public class RequestForIncentive extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            try {
                mDialog = ProgressDialog.show(NewTrainingActivity.this, "", "Please wait..");
                mDialog.setCancelable(true);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                // WebAPI webAPI = new WebAPI(MainActivity.Current);

                // String userid = "" + MainActivity.MyInfo.UserID;

                resTargetVsAchivement = server.GetSchemes("Incentive");

                return null;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                resTargetVsAchivement = new Response(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Void st) {


            reflectDataOnView();


            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

    }



    private void reflectDataOnView()
    {
        if (resTargetVsAchivement != null)
        {
            ArrayList<DocumentModel> arrDocModel = resTargetVsAchivement.data;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new NewTrainingModuleRecyclerAdapter(arrDocModel, this,true));
        }
    }
}
