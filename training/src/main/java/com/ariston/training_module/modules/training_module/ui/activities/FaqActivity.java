package com.ariston.training_module.modules.training_module.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.adapters.FaqAdpter;
import com.ariston.training_module.modules.training_module.view_models.FaQViewModel;
import com.ariston.training_module.utility.ConnectionDetector;
import com.ariston.training_module.utility.widgets.RobotoTextView;

public class FaqActivity extends AppCompatActivity {
    private FaQViewModel faQViewModel;
    ConnectionDetector _conn = null;
    RecyclerView faqlist;
    RobotoTextView rtv_errorMessage;
    ProgressBar progressBar;
    RelativeLayout iv_backView;
    CardView cv_noDataContainer;
    private FaqAdpter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        faQViewModel = ViewModelProviders.of(this).get(FaQViewModel.class);
        _conn = new ConnectionDetector(this);
        initViews();
        if (_conn.isConnectingToInternet()) {
            initObserver();
        } else {
            cv_noDataContainer.setVisibility(View.VISIBLE);
            rtv_errorMessage.setText("No Internet Connection!!");
            progressBar.setVisibility(View.GONE);
        }
        setupRecycler();
        getData();

        iv_backView.setOnClickListener(view -> onBackPressed());
    }

    public void initViews(){
        progressBar = findViewById(R.id.pbload);
        iv_backView=findViewById(R.id.iv_backView);
        cv_noDataContainer = findViewById(R.id.cv_noDataContainer);
        rtv_errorMessage = findViewById(R.id.rtv_errorMessage);
        faqlist=findViewById(R.id.faqlist);
    }

    public void getData(){
        faQViewModel.getFaqresponse().observe(this, trainingMaterialResponse -> {
            progressBar.setVisibility(View.GONE);
            if (trainingMaterialResponse.getmStatus()) {
                adapter.addData(trainingMaterialResponse.getData());
            } else {

                Toast.makeText(this, trainingMaterialResponse.getmErrormsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  void initObserver(){
        faQViewModel.getHelpDesk();
    }

    private void setupRecycler() {
        adapter = new FaqAdpter();
        faqlist.setAdapter(adapter);
        faqlist.setLayoutManager(new LinearLayoutManager(this));
      //  faqlist.addItemDecoration(new ItemDecorationAlbumColumns((int) Helper.getSizeInDp(this, 16), 3));
    }
}