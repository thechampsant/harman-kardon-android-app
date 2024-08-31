package com.ariston.training_module.modules.training_module.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.adapters.TrainerAdpterQuestion;
import com.ariston.training_module.modules.training_module.models.trainer_quize.TrainerQuizeResonse;

import com.ariston.training_module.modules.training_module.view_models.ResultViewModel;
import com.ariston.training_module.modules.training_module.view_models.TrainerQuizeViewModel;
import com.ariston.training_module.utility.ConnectionDetector;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.services.UserDetailsService;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.ArrayList;
import java.util.List;

public class QuizeTrainerActivity extends AppCompatActivity {
    private RecyclerView rv_quetionanslist;

    private TrainerQuizeViewModel trainerQuizeViewModel;
    private ResultViewModel resultViewModel;
    private UserDetailsService boundService;
    private boolean isBound;
    private ProgressBar progressBar, progressBar_pr;
    private final List<TrainerQuizeResonse> quizeResponses = new ArrayList<>();
    private RecyclerView viewPager;

    RobotoTextView   rtv_errorMessage;

    private RelativeLayout examsubmitView;
    private ImageView iv_backView;
    private  CardView cv_noDataContainer;
    private String ScorePercentage;
    private ConnectionDetector _conn = null;
    private TrainerAdpterQuestion adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_quize);
        trainerQuizeViewModel = ViewModelProviders.of(this).get(TrainerQuizeViewModel.class);
        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        _conn = new ConnectionDetector(this);
        initViews();
        intalizeViewpager();

        if (_conn.isConnectingToInternet()) {
            initObserver();
        } else {
            examsubmitView.setVisibility(View.GONE);
            cv_noDataContainer.setVisibility(View.VISIBLE);
            rtv_errorMessage.setText("No Internet Connection!!");
            progressBar.setVisibility(View.GONE);
        }

        initService();
        //setupRecycler();

        iv_backView.setOnClickListener(view -> onBackPressed());


    }


    private void initService() {
        Intent intent = new Intent(this, UserDetailsService.class);
        startService(intent);
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
    }


    private void initObserver() {
        trainerQuizeViewModel.getTrainerQuizeResonse().observe(this, trainingMaterialResponse -> {
            progressBar.setVisibility(View.GONE);
            if (trainingMaterialResponse.getmStatus()) {
                //    adapter.addData(trainingMaterialResponse.getData().get(0).getQuestions().get(0).getOptions());
                quizeResponses.addAll(trainingMaterialResponse.getData());
                adapter.addData(quizeResponses,this);

            } else {
                examsubmitView.setVisibility(View.GONE);
                cv_noDataContainer.setVisibility(View.VISIBLE);
                Toast.makeText(boundService, trainingMaterialResponse.getmErrormsg(), Toast.LENGTH_SHORT).show();
            }

        });

    }


    private void intalizeViewpager() {
        adapter = new TrainerAdpterQuestion();
        viewPager.setAdapter(adapter);
        viewPager.setLayoutManager(new LinearLayoutManager(this));
    }


    private void initViews() {
        //rv_quetionanslist = findViewById(R.id.rv_quetionanslist_tr);
        progressBar = findViewById(R.id.pbload);
        progressBar_pr = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.rv_quetionanslist_pager);
        iv_backView = findViewById(R.id.iv_back);
        cv_noDataContainer = findViewById(R.id.cv_noDataContainer);
        rtv_errorMessage = findViewById(R.id.rtv_errorMessage);



    }

    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UserDetailsService.MyBinder binderBridge = (UserDetailsService.MyBinder) service;
            boundService = binderBridge.getService();
            isBound = true;
            trainerQuizeViewModel.getTrainnerQuize(boundService.getUserBundle().getString(TrainingConstants.TRN_ID));

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(boundServiceConnection);
            isBound = false;
        }
        trainerQuizeViewModel.dispose();

    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();

        }

}