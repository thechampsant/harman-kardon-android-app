package com.ariston.training_module.modules.dashboard.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.dashboard.models.list_models.TraineeDetailModel;
import com.ariston.training_module.modules.dashboard.view_models.DashboardViewModel;
import com.ariston.training_module.utility.GenericRecyclerAdapter;
import com.ariston.training_module.utility.VerticalItemDecorator;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardListFragment extends Fragment {

    private DashboardViewModel mViewModel;
    private String graphKey = "";
    private GenericRecyclerAdapter<TraineeDetailModel> mAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout linearLayoutError;
    private RobotoTextView robotoTextView;

    //--------------------- ItemViews ---------------------
    private RobotoTextView textViewName;
    private RobotoTextView textViewV5Id;
    private RobotoTextView textViewQuizAttempted;
    private RobotoTextView textViewEducation;
    private RobotoTextView textViewStatus;
    private RobotoTextView textViewPercentage;

    public DashboardListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.pb_insideListFragment);
        recyclerView = view.findViewById(R.id.rv_insideListDashboard);
        linearLayoutError = view.findViewById(R.id.ll_errorContainerInListFragment);
        robotoTextView = view.findViewById(R.id.rtv_errorMessageInListFragment);
        init();
        initRecycler();
        observeViewModel();
        hitTheApi();
    }

    private void hitTheApi() {
        Log.d(TAG, "hitTheApi: " + mViewModel.getGraphKey());
        mViewModel.getTraineesStatusDataFromServer(mViewModel.getLoginId(), mViewModel.getTrainingId(), mViewModel.getGraphKey(), 1, "");

    }

    private static final String TAG = "Rama";

    private void init() {
        if (getActivity() != null)
            mViewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        mAdapter = new GenericRecyclerAdapter<>(getActivity(), R.layout.dashboard_list_item);
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalItemDecorator(30));
        recyclerView.setAdapter(mAdapter);
    }

    private void observeViewModel() {
        mViewModel.observeTraineeList().observe(getViewLifecycleOwner(), resourceTraineeList -> {
            if (resourceTraineeList != null) {
                switch (resourceTraineeList.status) {
                    case LOADING: {
                        showView(progressBar);
                        hideView(linearLayoutError);
                        hideView(recyclerView);
                        break;
                    }
                    case ERROR: {
                        hideView(progressBar);
                        showView(linearLayoutError);
                        hideView(recyclerView);
                        manageError(resourceTraineeList.message);
                        break;
                    }
                    case SUCCESS: {
                        hideView(progressBar);
                        hideView(linearLayoutError);
                        showView(recyclerView);
                        manageSuccess(resourceTraineeList.data);
                        break;
                    }
                }
            }
        });
    }

    private void manageError(String errMessage) {
        robotoTextView.setText(errMessage);
    }

    private void manageSuccess(List<TraineeDetailModel> modelList) {
        mAdapter.addData(modelList);
        manageItemViews();
    }


    private void manageItemViews() {
        mAdapter.setBindListener((obj, viewHolder) -> {
            textViewName = viewHolder.itemView.findViewById(R.id.rtv_traineeNameInDashboardItem);
            textViewV5Id = viewHolder.itemView.findViewById(R.id.v5Id);
            textViewQuizAttempted = viewHolder.itemView.findViewById(R.id.rtv_quizAttempted);
            textViewEducation = viewHolder.itemView.findViewById(R.id.rtv_material);
            textViewStatus = viewHolder.itemView.findViewById(R.id.rtv_status);
            textViewPercentage = viewHolder.itemView.findViewById(R.id.rtv_percentage);

            textViewName.setText(obj.getName());
            textViewV5Id.setText(obj.getV5Id());
            textViewQuizAttempted.setText(obj.getQuizAttempted());
            textViewEducation.setText(obj.getEducation());
            textViewStatus.setText(obj.getQuizStatus());
            textViewPercentage.setText(obj.getPercentage());
        });
    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        view.setVisibility(View.GONE);
    }
}
