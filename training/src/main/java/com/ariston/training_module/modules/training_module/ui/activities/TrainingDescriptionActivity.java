package com.ariston.training_module.modules.training_module.ui.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.adapters.AdapterRvTrDesc;
import com.ariston.training_module.modules.training_module.models.TrainingModel;
import com.ariston.training_module.modules.training_module.view_models.TrDescViewModel;
import com.ariston.training_module.utility.Helper;
import com.ariston.training_module.utility.ItemDecorationAlbumColumns;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.services.UserDetailsService;
import com.ariston.training_module.utility.widgets.ReadMoreTextView;
import com.ariston.training_module.utility.widgets.RobotoBoldTextView;
import com.ariston.training_module.utility.widgets.RobotoTextView;

public class TrainingDescriptionActivity extends AppCompatActivity implements AdapterRvTrDesc.AdapterRvTrDescCallbacks {
    private RobotoTextView tvTimings;
    private RobotoTextView tvDate;
    private RobotoBoldTextView tvTitle;
    private ReadMoreTextView tvDesc;
    private RobotoBoldTextView tvTrainerName;
    private ProgressBar progressBar;


    private RecyclerView rvTrDesc;
    private UserDetailsService boundService;
    private boolean isBound;
    private TrDescViewModel mViewModel;
    private AdapterRvTrDesc adapter;
    private TrainingModel recTrainingModel;
    private boolean canDownloadECertificate;
    private boolean refreshTrainingList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_desc);
        initViews();
        setupRecycler();
        setDescription();
        initService();
        initClicks();


    }

    private void initClicks() {
        findViewById(R.id.iv_back_tr_dsc).setOnClickListener(v -> onBackPressed());
    }

    private void setDescription() {
        recTrainingModel = getIntent().getParcelableExtra(TrainingConstants.TR_DESC_ITEM);
        tvDate.setText(String.format("%s-%s", recTrainingModel.getStartDate(), recTrainingModel.getEndDate()));
        tvTimings.setText(recTrainingModel.getDuration());
        tvDesc.setText(recTrainingModel.getDescription());
        tvTitle.setText(recTrainingModel.getTrainingName());
        tvTrainerName.setText(recTrainingModel.getTrainerName());
        if(recTrainingModel.getTrainingStatus()!=null)
        canDownloadECertificate = recTrainingModel.getTrainingStatus().equals("Passed");


    }

    private void setupRecycler() {

        adapter = new AdapterRvTrDesc();
        adapter.setCallbacks(this);
        rvTrDesc.setAdapter(adapter);
        rvTrDesc.setLayoutManager(new GridLayoutManager(this, 3));
        rvTrDesc.addItemDecoration(new ItemDecorationAlbumColumns((int) Helper.getSizeInDp(this, 16), 3));
    }

    private void initViews() {
        tvTimings = findViewById(R.id.tv_timings);
        tvTimings.setSelected(true);
        tvDate = findViewById(R.id.tv_date);
        tvDate.setSelected(true);
        Helper.setDrawableTextView(tvDate, R.drawable.ic_access_time_training_blue_24dp, 0);
        tvTitle = findViewById(R.id.tv_title_tr_desc);
        tvDesc = findViewById(R.id.tv_description_tr_desc);
        tvTrainerName = findViewById(R.id.tv_trainer_name_tr_desc);
        rvTrDesc = findViewById(R.id.rv_tr_desc);
        progressBar = findViewById(R.id.pb_tr_desc);
    }

    private void initService() {
        Intent intent = new Intent(this, UserDetailsService.class);
        startService(intent);
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
        mViewModel = ViewModelProviders.of(this).get(TrDescViewModel.class);
        mViewModel.getTrainingDescRes().observe(this, trainingDescResponse -> {
            progressBar.setVisibility(View.GONE);
            if (trainingDescResponse.getmStatus()) {
                adapter.addData(trainingDescResponse.getData(),
                        boundService.getUserBundle().getString(TrainingConstants.TRN_ID),boundService.getUserBundle().getBoolean(TrainingConstants.IS_TRAINER), recTrainingModel);
            } else {
                Toast.makeText(boundService, trainingDescResponse.getmErrormsg(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UserDetailsService.MyBinder binderBridge = (UserDetailsService.MyBinder) service;
            boundService = binderBridge.getService();
            isBound = true;
            mViewModel.getTrainingItems(boundService.getUserBundle().getString(TrainingConstants.USER_ID));
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
        mViewModel.disposeApis();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getStringExtra("Percentage") != null) {
            int scorepercentage = Integer.parseInt(data.getStringExtra("Percentage"));
            if (scorepercentage >= 80) {
                canDownloadECertificate = true;
                refreshTrainingList = true;
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(TrainingConstants.REFRESH_LIST, refreshTrainingList);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();

    }

    @Override
    public boolean canDownloadECertificate() {
        return canDownloadECertificate;
    }

    @Override
    public void routePlanClick() {
        if (recTrainingModel.getTrainingType().equals("Virtual")) {
            Toast.makeText(this, "Virtual Training Doesn't Have Route Plan", Toast.LENGTH_SHORT).show();
        }
    }
}
