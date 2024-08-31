package com.fieldforce.harmonkardonff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.core.base.InnosolsActivity;
import app.core.model.Response;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.DocumentModel;
import mob.field.harmonkardonff.services.WebService;

public class NewTrainingDetailsActivity extends InnosolsActivity {

    @Override
    public void RegisterTableInfoForLocalDB() {

    }

    public Response resTargetVsAchivement;
    int position;
    TextView titleTextView;
    WebService server = new WebService();
    private static final String TAG = "NewTrainingDetailO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training_details);
        titleTextView = (TextView) findViewById(R.id.titleIncentive);
        Intent intent = getIntent();
        position = intent.getIntExtra("Position", 0);
        showIncentive(true, false);
        webAPICall();
    }

    private void openUrlWithTheHelpOfIntent(DocumentModel documentModel){
        String url = documentModel.DocURL;
        Log.d(TAG, "openUrlWithTheHelpOfIntent: "+url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void showIncentive(boolean showNothing, boolean incentiveFound) {
        if (showNothing) {
            findViewById(R.id.no_incentive).setVisibility(View.GONE);
        } else if (incentiveFound) {
            findViewById(R.id.no_incentive).setVisibility(View.GONE);
        } else {
            findViewById(R.id.no_incentive).setVisibility(View.VISIBLE);
        }
    }

    private void webAPICall() {
        if (server.isNetworkAvailable(NewTrainingDetailsActivity.this)) {
            new RequestForIncentive().execute((Void) null);

        } else
            Toast.makeText(getApplicationContext(),
                    "Internet is not available", Toast.LENGTH_LONG).show();
    }

    private void reflectDataOnView(int position) {
        int urlPosition = position;
        if (resTargetVsAchivement != null) {
            /*ArrayList<DocumentModel> arrDocModel = resTargetVsAchivement.data;
            //recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //recyclerView.setAdapter(new IncentiveRecyclerAdapter(arrDocModel, this));

            recyclerVieW.setLayoutManager(new LinearLayoutManager(this));
            recyclerVieW.setAdapter(new IncentiveRecyclerAdapter(arrDocModel,this));*/

            ArrayList<DocumentModel> arrDocModel = resTargetVsAchivement.data;

            if (arrDocModel != null && arrDocModel.size() > 0) {
                showIncentive(false, true);
                DocumentModel documentModel = arrDocModel.get(urlPosition);
                if (documentModel.DocURL != null && documentModel.DocURL.length() > 4)
                {
                    openUrlWithTheHelpOfIntent(documentModel);

                   /* WebView browser = (WebView) findViewById(R.id.webview);
                    browser.setInitialScale(1);
                    WebSettings webSettings = browser.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setBuiltInZoomControls(true);
                    webSettings.setLoadWithOverviewMode(true);
                    webSettings.setUseWideViewPort(true);
                    //documentModel.DocUrl="https://mindorks.s3.ap-south-1.amazonaws.com/courses/MindOrks_Android_Online_Professional_Course-Syllabus.pdf";
                    String url = App.checkPdf(documentModel.DocUrl) ? AppConstant.PDF_PREFIX + documentModel.DocUrl: documentModel.DocUrl;
                    //browser.loadUrl(documentModel.DocUrl);
                    browser.loadUrl(App.checkPdf(documentModel.DocUrl) ? AppConstant.PDF_PREFIX + documentModel.DocUrl: documentModel.DocUrl);
                    Log.d(TAG, "shouldOverrideUrlLoading: "+url);
                    *//*browser.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(App.checkPdf(url) ? AppConstant.PDF_PREFIX + url : url);
                            Log.d(TAG, "shouldOverrideUrlLoading: "+view.getOriginalUrl());
                            return true;
                        }
                    });*/

                    titleTextView.setText(documentModel.DocName);


                    //********************
                    //**********************************
                }
            } else {
                showIncentive(false, false);
            }
        }

    }




    public class RequestForIncentive extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            try {
                mDialog = ProgressDialog.show(NewTrainingDetailsActivity.this,
                        "", "Please wait..");
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


            reflectDataOnView(position);


            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {
            // showProgress(false);
        }
    }


}
