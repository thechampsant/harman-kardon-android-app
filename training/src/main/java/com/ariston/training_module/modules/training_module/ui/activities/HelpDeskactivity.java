package com.ariston.training_module.modules.training_module.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.view_models.GetTrainingHelpDesk;
import com.ariston.training_module.utility.ConnectionDetector;
import com.ariston.training_module.utility.widgets.RobotoTextView;

public class HelpDeskactivity extends AppCompatActivity {
    private GetTrainingHelpDesk getTrainingHelpDeskModel;
    ConnectionDetector _conn = null;
    RobotoTextView tv_call,mail_text,rtv_errorMessage;
    ProgressBar progressBar;
    RelativeLayout iv_backView;
    CardView cv_noDataContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_deskactivity);
        getTrainingHelpDeskModel = ViewModelProviders.of(this).get(GetTrainingHelpDesk.class);
        _conn = new ConnectionDetector(this);
        initViews();
        if (_conn.isConnectingToInternet()) {
            initObserver();
        } else {
            cv_noDataContainer.setVisibility(View.VISIBLE);
            rtv_errorMessage.setText("No Internet Connection!!");
            progressBar.setVisibility(View.GONE);
        }
        getData();

        iv_backView.setOnClickListener(view -> onBackPressed());

    }
    public void initObserver()
    {
        getTrainingHelpDeskModel.getHelpDesk();
    }
    public void initViews()
    {
        progressBar = findViewById(R.id.pbload);
        tv_call = findViewById(R.id.tv_call);
        mail_text = findViewById(R.id.mail_text);
        iv_backView=findViewById(R.id.iv_backView);
        cv_noDataContainer = findViewById(R.id.cv_noDataContainer);
        rtv_errorMessage = findViewById(R.id.rtv_errorMessage);
    }


    private void getData() {
        getTrainingHelpDeskModel.getHelpDeskResponse().observe(this, trainingMaterialResponse -> {
            progressBar.setVisibility(View.GONE);
            if (trainingMaterialResponse.getmStatus()) {
                Log.e("Data", trainingMaterialResponse.getData().get(0).getContactNo() + "Null");
                String Mobile=trainingMaterialResponse.getData().get(0).getContactNo().replaceAll(",",",\n");
                String Email=trainingMaterialResponse.getData().get(0).getEmailId().replaceAll(","," ,\n");
                tv_call.setText( Mobile);
                mail_text.setText(Email );

            } else {
                Toast.makeText(this, trainingMaterialResponse.getmErrormsg(), Toast.LENGTH_SHORT).show();
                cv_noDataContainer.setVisibility(View.VISIBLE);
            }

        });

    }

}