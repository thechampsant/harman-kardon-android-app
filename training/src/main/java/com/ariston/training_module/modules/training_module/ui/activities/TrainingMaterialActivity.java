package com.ariston.training_module.modules.training_module.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.adapters.AdapterTrainingMat;
import com.ariston.training_module.modules.training_module.view_models.ViewModelTrainingMat;
import com.ariston.training_module.utility.Helper;
import com.ariston.training_module.utility.ItemDecorationAlbumColumns;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.services.UserDetailsService;

public class TrainingMaterialActivity extends AppCompatActivity {

    private RecyclerView rvTrMat;
    private AdapterTrainingMat adapter;
    private ViewModelTrainingMat viewModelTrainingMat;
    private UserDetailsService boundService;
    private boolean isBound;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_material);
        viewModelTrainingMat = ViewModelProviders.of(this).get(ViewModelTrainingMat.class);
        initObserver();
        initService();
        initViews();
        setupRecycler();
        initClicks();

    }

    private void initClicks() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());

    }

    private void initService() {
        Intent intent = new Intent(this, UserDetailsService.class);
        startService(intent);
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
    }

    private void initObserver() {
        viewModelTrainingMat.getTrMatLiveResponse().observe(this, trainingMaterialResponse -> {
            progressBar.setVisibility(View.GONE);
            if (trainingMaterialResponse.getmStatus()) {
                adapter.addData(trainingMaterialResponse.getData());
            } else {
                Toast.makeText(boundService, trainingMaterialResponse.getmErrormsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecycler() {
        adapter = new AdapterTrainingMat();
        rvTrMat.setAdapter(adapter);
        rvTrMat.setLayoutManager(new GridLayoutManager(this, 3));
        rvTrMat.addItemDecoration(new ItemDecorationAlbumColumns((int) Helper.getSizeInDp(this, 16), 3));
    }

    private void initViews() {
        rvTrMat = findViewById(R.id.rv_tr_mat);
        progressBar = findViewById(R.id.pb_tr_desc);
    }

    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UserDetailsService.MyBinder binderBridge = (UserDetailsService.MyBinder) service;
            boundService = binderBridge.getService();
            isBound = true;
            viewModelTrainingMat.getTrainingMaterialItems(boundService.getUserBundle().getString(TrainingConstants.TRN_ID),boundService.getUserBundle().getString(TrainingConstants.USER_ID));
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
        viewModelTrainingMat.dispose();

    }


}
