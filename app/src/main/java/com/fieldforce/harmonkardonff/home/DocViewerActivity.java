package com.fieldforce.harmonkardonff.home;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieldforce.asyntask.WebService;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.home.service.Bannerdata;
import com.fieldforce.harmonkardonff.home.service.GetDataService;
import com.fieldforce.harmonkardonff.home.service.RetrofitClientInstance;
import com.google.gson.Gson;


import app.core.base.InnosolsActivity;
import app.core.services.LoginProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocViewerActivity extends InnosolsActivity {
    private int i = 0;

    private WebView pdfView;
    private ProgressBar progress;
    private Button btn_action1;
    private String removePdfTopIcon = "javascript:(function() {" + "document.querySelector('[role=\"toolbar\"]').remove();})()";

    private boolean isBound;
    private boolean backDisabled;

    @Override
    public void RegisterTableInfoForLocalDB() {

    }

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_viewer_1);
        pdfView = findViewById(R.id.webview);
        progress = findViewById(R.id.pb_doc);

        showPdfFile(getIntent().getStringExtra("DOC_URL"));

      /*  Uri uri = Uri.parse(getIntent().getStringExtra("DOC_URL"));

        Uri webpage = Uri.parse(url);*//*
        Uri webpage = Uri.parse(getIntent().getStringExtra("DOC_URL"));
        if (!getIntent().getStringExtra("DOC_URL").startsWith("http://") && !getIntent().getStringExtra("DOC_URL").startsWith("https://")) {
            webpage = Uri.parse("http://" + getIntent().getStringExtra("DOC_URL"));
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }*/
       /* Intent intent = new Intent();
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

           startActivity(intent);
           finish();*/
        btn_action1=(Button) findViewById(R.id.btn_action2);
        btn_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSeenVideo();
            }
        });

    }

    void isSeenVideo() {

        if (hasSubmitted) return; // prevent double call
          hasSubmitted = true;
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Bannerdata> call = service.SubmitConfuguredData(User.GetUserName(),getIntent().getStringExtra(TrainingConstants.MAT_ID));
        call.enqueue(new Callback<Bannerdata>() {
            @Override
            public void onResponse(Call<Bannerdata> call, Response<Bannerdata> response) {
                Log.e("call1233: ", call.request().toString());

                Log.e("body122", new Gson().toJson(response.body()));

                if (response.isSuccessful()) {

                    if(response.body().getmStatus())
                    {
                        onBackPressed();
                    }

                } else {

                    Toast.makeText(DocViewerActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Bannerdata> call, Throwable t) {
                Log.e("onFailure: ", call.toString());
                Log.e("onFailure: ", t.getMessage());

            }
        });
    }
    private boolean hasSubmitted = false;





    private void showPdfFile(final String imageString) {
        showProgress();
        pdfView.invalidate();
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.getSettings().setLoadWithOverviewMode(true);
        pdfView.getSettings().setUseWideViewPort(true);
        pdfView.getSettings().setBuiltInZoomControls(true);
        pdfView.getSettings().setSupportZoom(true);
        pdfView.loadUrl(getIntent().getBooleanExtra("is_image", false) ? imageString : "http://docs.google.com/gview?embedded=true&url=" + imageString);
        pdfView.setWebViewClient(new WebViewClient() {
            boolean checkOnPageStartedCalled = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                checkOnPageStartedCalled = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (checkOnPageStartedCalled) {
                    pdfView.loadUrl(removePdfTopIcon);
                    hideProgress();
                    backDisabled = true;
                } else {
                    showPdfFile(imageString);
                }
            }
        });
    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        /*if (!backDisabled)*/
        super.onBackPressed();
    }
}