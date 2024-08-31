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
import com.ariston.training_module.modules.training_module.view_models.ProfileViewModel;
import com.ariston.training_module.utility.ConnectionDetector;
import com.ariston.training_module.utility.widgets.RobotoTextView;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel profileViewModel;
    ConnectionDetector _conn = null;
     RobotoTextView rtv_errorMessage,userName,userMobile,userID,education,counter,address,assigned,helpline;
    ProgressBar progressBar;
    RelativeLayout iv_backView;
    CardView cv_noDataContainer;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
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

    public void initObserver() {
        profileViewModel.getProfile(UserID);
    }

    public void initViews() {
        UserID=getIntent().getStringExtra("UserID");
        progressBar = findViewById(R.id.pbload);
        iv_backView = findViewById(R.id.iv_backView);
        cv_noDataContainer = findViewById(R.id.cv_noDataContainer);
        rtv_errorMessage = findViewById(R.id.rtv_errorMessage);
        userID = findViewById(R.id.userID);
        userMobile = findViewById(R.id.userMobile);
        userName = findViewById(R.id.userName);
        counter = findViewById(R.id.counter);
        education = findViewById(R.id.education);
        address = findViewById(R.id.address);
        assigned = findViewById(R.id.assigned);
        helpline = findViewById(R.id.helpline);
    }

    public void getData(){
        profileViewModel.getProfileResponse().observe(this, trainingMaterialResponse -> {
            progressBar.setVisibility(View.GONE);
            if (trainingMaterialResponse.getmStatus()) {
                Log.e("Data", trainingMaterialResponse.getData().get(0).getUserName() + "Null");
                userID.setText(trainingMaterialResponse.getData().get(0).getUserId() != null ? trainingMaterialResponse.getData().get(0).getUserId().toString() : "");
                userName.setText(trainingMaterialResponse.getData().get(0).getUserName() != null ? trainingMaterialResponse.getData().get(0).getUserName().toString() : "");
                userMobile.setText(trainingMaterialResponse.getData().get(0).getHelpline() != null ? trainingMaterialResponse.getData().get(0).getHelpline().toString() : "");
                helpline.setText(trainingMaterialResponse.getData().get(0).getHelpline() != null ? trainingMaterialResponse.getData().get(0).getHelpline().toString() : "");
                counter.setText(trainingMaterialResponse.getData().get(0).getCounter() != null ? trainingMaterialResponse.getData().get(0).getCounter().toString() : "");
                assigned.setText(trainingMaterialResponse.getData().get(0).getAssignedOn() != null ? trainingMaterialResponse.getData().get(0).getAssignedOn().toString() : "");
                education.setText(trainingMaterialResponse.getData().get(0).getEducation() != null ? trainingMaterialResponse.getData().get(0).getEducation().toString() : "");
                address.setText(trainingMaterialResponse.getData().get(0).getAddress() != null ? trainingMaterialResponse.getData().get(0).getAddress().toString() : "");

            } else {
                Toast.makeText(this, trainingMaterialResponse.getmErrormsg(), Toast.LENGTH_SHORT).show();
                cv_noDataContainer.setVisibility(View.VISIBLE);
            }

        });

    }
}