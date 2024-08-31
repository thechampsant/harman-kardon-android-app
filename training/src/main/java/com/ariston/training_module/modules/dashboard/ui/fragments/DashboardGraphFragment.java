package com.ariston.training_module.modules.dashboard.ui.fragments;

//import android.arch.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.dashboard.models.graph_models.GraphData;
import com.ariston.training_module.modules.dashboard.models.graph_models.GraphParent;
import com.ariston.training_module.modules.dashboard.models.graph_models.QuizDashboard;
import com.ariston.training_module.modules.dashboard.ui.listeners.IDashboardFragmentsListener;
import com.ariston.training_module.modules.dashboard.view_models.DashboardViewModel;
import com.ariston.training_module.utility.ColorConstants;
import com.ariston.training_module.utility.GenericRecyclerAdapter;
import com.ariston.training_module.utility.Helper;
import com.ariston.training_module.utility.HorizontalItemDecorator;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.widgets.RobotoTextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import androidx.cardview.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardGraphFragment extends Fragment implements OnChartValueSelectedListener {

    IDashboardFragmentsListener listener;
    private RecyclerView recyclerView;
    private GenericRecyclerAdapter<QuizDashboard> mAdapter;
    private DashboardViewModel mViewModel;
    private TextView textView;
    private ProgressBar progressBar;
    private CardView cardViewError;
    private CardView cardViewPieChart;
    private RobotoTextView robotoTextViewErrorMessage;
    private PieChart pieChart;
    private PieDataSet pieDataSet;
    private ArrayList<PieEntry> pieEntryArrayList;
    final int[] MY_COLORS = {Color.parseColor(ColorConstants.COLOR1), Color.parseColor(ColorConstants.COLOR2), Color.parseColor(ColorConstants.COLOR3)};

    //-------------------ItemViews---------------------
    private RobotoTextView robotoTextViewTitle;
    private RobotoTextView robotoTextViewValue;
    int screenWidth=0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IDashboardFragmentsListener) {
            listener = (IDashboardFragmentsListener) context;
        } else {
            throw new RuntimeException("" + context + " Must implement IDashboardFragmentsListener");
        }
    }

    public DashboardGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObjects();
        screenWidth = Helper.getScreenWidth(getActivity());
        init(view);
        initRecycler();
        observeViewModel();
    }

    private void initObjects() {
        if (getActivity() != null)
            mViewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        Log.d(TAG, "init: " + mViewModel);
        mAdapter = new GenericRecyclerAdapter<>(getActivity(), R.layout.graph_fragment_card_items);
    }

    private static final String TAG = "Rama";

    private void init(View view) {
        textView = view.findViewById(R.id.tv_fake);
        progressBar = view.findViewById(R.id.pb_insideGraphFragment);
        cardViewError = view.findViewById(R.id.cv_errorInsideGraphFragment);
        cardViewPieChart = view.findViewById(R.id.cv_graphContainer);
        robotoTextViewErrorMessage = view.findViewById(R.id.rtv_errorMessageInGraphFragment);
        recyclerView = view.findViewById(R.id.rv_graphFragment);
        pieChart = view.findViewById(R.id.pc_inGraphFragment);
        pieChart.setTouchEnabled(true);
        pieChart.setOnChartValueSelectedListener(this);
        Log.d(TAG, "MP_Chart Child count : " + pieChart.getChildCount());
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if (screenWidth>500)
            recyclerView.addItemDecoration(new HorizontalItemDecorator(30));
        else
            recyclerView.addItemDecoration(new HorizontalItemDecorator(10));
        recyclerView.setAdapter(mAdapter);
        textView.setOnClickListener(v -> listener.inflateFragment(TrainingConstants.DASH_BOARD_LIST_FRAGMENT_TAG));
    }

    private void observeViewModel() {
        mViewModel.getDataFromServerAndObserve(mViewModel.getTrainingId(), mViewModel.getLoginId()).observe(getActivity(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING: {
                        showView(progressBar);
                        hideView(cardViewError);
                        hideView(cardViewPieChart);
                        hideView(recyclerView);
                        break;
                    }
                    case ERROR: {
                        hideView(progressBar);
                        showView(cardViewError);
                        hideView(cardViewPieChart);
                        hideView(recyclerView);
                        manageError(resource.message);
                        break;
                    }
                    case SUCCESS: {
                        hideView(progressBar);
                        hideView(cardViewError);
                        showView(recyclerView);
                        showView(cardViewPieChart);
                        manageSuccess(resource.data);
                        managePieChart(resource.data.getData().get(0));
                        break;
                    }
                }
            }
        });
    }

    private void manageError(String errorMessage) {
        robotoTextViewErrorMessage.setText(errorMessage);
    }

    private void manageSuccess(GraphParent model) {
        mAdapter.addData(model.getData().get(0).getQuizDashboard());
        manageItemViews();
    }

    private void manageItemViews() {
        mAdapter.setBindListener((obj, viewHolder) -> {
            robotoTextViewTitle = viewHolder.itemView.findViewById(R.id.rtv_paramTitle);
            robotoTextViewValue = viewHolder.itemView.findViewById(R.id.rtv_paramsValue);
            robotoTextViewTitle.setText(obj.getTitle());
            robotoTextViewValue.setText(obj.getValue());
        });
    }


    ArrayList<Integer> colors = new ArrayList<Integer>();

    private void managePieChart(GraphData data) {
        float fail = Float.parseFloat(data.getFailed().replace('%', ' ').trim());
        float pass = Float.parseFloat(data.getPassed().replace('%', ' ').trim());
        float notAttempted = Float.parseFloat(data.getNotAttempted().replace('%', ' ').trim());
        pieEntryArrayList = new ArrayList<>();
        if (fail > 0)
            pieEntryArrayList.add(new PieEntry(fail, "Failed", TrainingConstants.SLICE_2));
        if (pass > 0)
            pieEntryArrayList.add(new PieEntry(pass, "Passed", TrainingConstants.SLICE_1));
        if (notAttempted > 0)
            pieEntryArrayList.add(new PieEntry(notAttempted, "Not Attempted", TrainingConstants.SLICE_3));
        pieDataSet = new PieDataSet(pieEntryArrayList, "");
        for (int c : MY_COLORS) {
            colors.add(c);
        }
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(14f);
        PieData pieData = new PieData(pieDataSet);
        pieData.setHighlightEnabled(true);
        pieChart.setCenterText("Summary");
        pieChart.setData(pieData);
        pieChart.animateXY(1700, 1700);
        pieChart.getDescription().setEnabled(false);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setUsePercentValues(true);
    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d(TAG, "onValueSelected: " + h);
        Log.d(TAG, "onValueSelected: " + pieChart.getHighlighter());
        Log.d(TAG, "onValueSelected: " + e.getData().toString());
        mViewModel.setGraphKey(e.getData().toString());
        //mViewModel.observeGraphKey().setValue(e.getData().toString());
        listener.inflateFragment(TrainingConstants.DASH_BOARD_LIST_FRAGMENT_TAG);
    }

    @Override
    public void onNothingSelected() {

    }
}
