package com.ariston.training_module.modules.dashboard.ui.activities;

//import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.dashboard.ui.fragments.DashboardGraphFragment;
import com.ariston.training_module.modules.dashboard.ui.fragments.DashboardListFragment;
import com.ariston.training_module.modules.dashboard.ui.listeners.IDashboardFragmentsListener;
import com.ariston.training_module.modules.dashboard.view_models.DashboardViewModel;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.services.UserDetailsService;

public class DashboardActivity extends AppCompatActivity implements IDashboardFragmentsListener {
    private static final String TAG = "DashboardActivity";
    DashboardGraphFragment dashboardGraphFragment;
    DashboardListFragment dashboardListFragment;
    private DashboardViewModel mViewModel;
    private UserDetailsService boundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        initClicks();

    }

    private void initClicks() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());

    }

    private void init() {
        mViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        initService();
        //mViewModel.setLoginId(boundService.getUserBundle().getString(TrainingConstants.USER_ID));
    }

    private void initService() {
        Intent intent = new Intent(this, UserDetailsService.class);
        if (getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
        }
        startService(intent);
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
    }

    private boolean isBound;
    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UserDetailsService.MyBinder binderBridge = (UserDetailsService.MyBinder) service;
            boundService = binderBridge.getService();
            isBound = true;
            //hitapi(startDate, endDate);
            mViewModel.setLoginId(boundService.getUserBundle().getString(TrainingConstants.USER_ID));
            mViewModel.setTrainingId(boundService.getUserBundle().getString(TrainingConstants.TRN_ID));
            initFragments();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            boundService = null;
        }
    };


    private void initFragments() {
        dashboardGraphFragment = new DashboardGraphFragment();
        dashboardListFragment = new DashboardListFragment();
        doFragmentTransaction(dashboardGraphFragment, false, TrainingConstants.DASH_BOARD_GRAPH_FRAGMENT_TAG);
    }

    private void doFragmentTransaction(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }


    @Override
    public void inflateFragment(String fragmentTag) {
        if (fragmentTag.equalsIgnoreCase(TrainingConstants.DASH_BOARD_LIST_FRAGMENT_TAG)) {
            doFragmentTransaction(dashboardListFragment, true, TrainingConstants.DASH_BOARD_LIST_FRAGMENT_TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(boundServiceConnection);
            isBound = false;
        }
    }
}
