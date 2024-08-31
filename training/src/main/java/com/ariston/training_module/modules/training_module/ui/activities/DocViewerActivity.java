package com.ariston.training_module.modules.training_module.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.view_models.DocViewerViewModel;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.services.UserDetailsService;

public class DocViewerActivity extends AppCompatActivity {
    private int i = 0;

    private WebView pdfView;
    private ProgressBar progress;
    private String removePdfTopIcon = "javascript:(function() {" + "document.querySelector('[role=\"toolbar\"]').remove();})()";
    private DocViewerViewModel mViewModel;
    private UserDetailsService boundService;
    private boolean isBound;
    private boolean backDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_viewer);
        pdfView = findViewById(R.id.webview);
        progress = findViewById(R.id.pb_doc);
        mViewModel = ViewModelProviders.of(this).get(DocViewerViewModel.class);
        Log.e("Dataaa","DAAAAAA");
        Uri uri = Uri.parse(getIntent().getStringExtra(TrainingConstants.DOC_URL));
        Intent intent = new Intent();
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();

        initService();
        observeVm();


    }

    private void observeVm() {
        mViewModel.getTrainingMatLiveData().observe(this, baseResponse -> {
            if (baseResponse.getmStatus()) {
                backDisabled = false;
            }
        });
    }

    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UserDetailsService.MyBinder binderBridge = (UserDetailsService.MyBinder) service;
            boundService = binderBridge.getService();
            isBound = true;

            showPdfFile(getIntent().getStringExtra(TrainingConstants.DOC_URL));


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private void initService() {
        Intent intent = new Intent(this, UserDetailsService.class);
        startService(intent);
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
    }

    private void showPdfFile(final String imageString) {
        showProgress();
        /*pdfView.invalidate();
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.getSettings().setLoadWithOverviewMode(true);
        pdfView.getSettings().setUseWideViewPort(true);
        pdfView.getSettings().setBuiltInZoomControls(true);
        pdfView.getSettings().setSupportZoom(true);
        pdfView.loadUrl(getIntent().getBooleanExtra("is_image", false) ? imageString : "http://docs.google.com/gview??key=AIzaSyCAExpBViS995AP7cTrV_fvBi2bqQu68WE&embedded=true&url=" + imageString);
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
                    mViewModel.trainMatCompleted(boundService.getUserBundle().getString(TrainingConstants.USER_ID), getIntent().getLongExtra(TrainingConstants.MAT_ID, 0));
                } else {
                    showPdfFile(imageString);
                }
            }
        });*/
        pdfView.loadUrl(removePdfTopIcon);
        hideProgress();
        backDisabled = true;
        mViewModel.trainMatCompleted(boundService.getUserBundle().getString(TrainingConstants.USER_ID), getIntent().getLongExtra(TrainingConstants.MAT_ID, 0));

        Uri uri = Uri.parse(getIntent().getStringExtra(TrainingConstants.DOC_URL));
        Intent intent = new Intent();
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
           // finish();

    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(boundServiceConnection);
            isBound = false;
        }
        mViewModel.disposeApis();

    }

    @Override
    public void onBackPressed() {
        if (!backDisabled)
            super.onBackPressed();
    }
}