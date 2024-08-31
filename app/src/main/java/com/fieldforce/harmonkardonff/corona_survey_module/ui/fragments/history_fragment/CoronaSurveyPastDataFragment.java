package com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.history_fragment;

import android.app.DatePickerDialog;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldforce.customAdapter.corona_adapters.VerticalItemDecorator;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.corona_survey_module.models.history.CoronaSurveyHistoryContainer;
import com.fieldforce.harmonkardonff.corona_survey_module.view_models.CoronaSurveyHistoryViewModel;
import com.fieldforce.utility.ColorConstants;
import com.fieldforce.utility.Resource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.core.base.IFragment;

public class CoronaSurveyPastDataFragment extends IFragment {

    private String startDate;
    private String endDate;
    private CoronaSurveyHistoryViewModel mViewModel;
    private RecyclerView recyclerView;
    private CoronaSurveyPastDataRecyclerAdapter mAdapter;
    private LinearLayout linearLayoutProgressBarContainer;
    private LinearLayout linearLayoutErrorContainer;
    private TextView textViewResponseTitle;
    private TextView textViewErrorValue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.fragment_past_corona_survey, inflater, container);
    }
    @Override
    public void Activate(View FragmentView) {
        initWidgets();
        subscribeToViewModel();
        initializeDatesAndServerRequest();
        startDateProcess(R.id.txt_for_dates, R.id.btn_for_dates);
        endDateProcessed(R.id.txt_to_dates, R.id.btn_to_dates);
    }

    private void initWidgets(){
        mViewModel = ViewModelProviders.of(getActivity()).get(CoronaSurveyHistoryViewModel.class);
        recyclerView = (RecyclerView) findViewById(R.id.rv_coronaSurveyPastData);
        linearLayoutProgressBarContainer = (LinearLayout) findViewById(R.id.ll_progressBarContainerInPastHistory);
        linearLayoutErrorContainer = (LinearLayout) findViewById(R.id.ll_errorContainerInCoronaHistory);
        textViewResponseTitle = (TextView) findViewById(R.id.tv_responseTitleInCoronaHistory);
        textViewErrorValue = (TextView) findViewById(R.id.tv_errorValueInCoronaHistory);
        mAdapter = new CoronaSurveyPastDataRecyclerAdapter();
        initRecycler();
    }

    private void initRecycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalItemDecorator(20));
        recyclerView.setAdapter(mAdapter);
    }

    private void subscribeToViewModel(){
        mViewModel.getCoronaHistoryDataResponse().removeObservers(getViewLifecycleOwner());
        mViewModel.getCoronaHistoryDataResponse().observe(getViewLifecycleOwner(), new Observer<Resource<CoronaSurveyHistoryContainer>>() {
            @Override
            public void onChanged(@Nullable Resource<CoronaSurveyHistoryContainer> object) {
                if (object!=null){
                    switch (object.status){
                        case LOADING:{
                            showView(linearLayoutProgressBarContainer);
                            hideView(linearLayoutErrorContainer);
                            hideView(recyclerView);
                            break;
                        }
                        case ERROR:{
                            prepareErrorContainerLayout(object.message);
                            showView(linearLayoutErrorContainer);
                            hideView(linearLayoutProgressBarContainer);
                            hideView(recyclerView);
                            break;
                        }
                        case SUCCESS:{
                            mAdapter.setData(object.data.getData());
                            showView(recyclerView);
                            hideView(linearLayoutProgressBarContainer);
                            hideView(linearLayoutErrorContainer);
                        }
                    }
                }
            }
        });
    }

    private void prepareErrorContainerLayout(String errorMessage){
        textViewResponseTitle.setText("Error");
        textViewResponseTitle.setBackgroundColor(Color.parseColor(ColorConstants.ERROR_RED));
        textViewErrorValue.setText(errorMessage);
    }

    private void initializeDatesAndServerRequest() {
        // TODO Auto-generated method stub
        initializeStartDate();
        initializeEndDate();
        if (isNetworkAvailable())
            mViewModel.getSurveyHistoryData(startDate,endDate);
        else {
            ShowToast("No Network available for View attendance");

        }
    }

    private void initializeStartDate() {
        String date = GetCurrentDateInString();
        startDate = date;
        SetTextViewAsString(R.id.txt_for_dates, date);
    }

    private void initializeEndDate() {

        String date = GetCurrentDateInString();
        endDate = date;
        SetTextViewAsString(R.id.txt_to_dates, date);
    }

    private void startDateProcess(int idForDateTxt, int idForDateButton) {
        final TextView dateText = (TextView) findViewById(idForDateTxt);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));
                startDate = dateFormatter.format(newDate.getTime());

                if (endDate == null || endDate.equalsIgnoreCase(""))
                    ShowToast("Please Enter To Date");

                else {
                    mViewModel.getSurveyHistoryData(startDate,endDate);
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar
                .get(Calendar.MONTH), newCalendar
                .get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.setCancelable(false);
        findViewById(idForDateButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        fromDatePickerDialog.show();
                    }
                });
    }

    private void endDateProcessed(int idForDateTxt, int idForDateButton) {
        final TextView dateText = (TextView) findViewById(idForDateTxt);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));
                endDate = dateFormatter.format(newDate.getTime());
                if (startDate != null
                        && !startDate.equalsIgnoreCase(""))
                    mViewModel.getSurveyHistoryData(startDate,endDate);
                else
                    ShowToast("Please Enter From Date");

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar
                .get(Calendar.MONTH), newCalendar
                .get(Calendar.DAY_OF_MONTH));
        endDatePickerDialog.setCancelable(false);
        findViewById(idForDateButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        endDatePickerDialog.show();
                    }
                });
    }

    private void showView(View view){
        view.setVisibility(View.VISIBLE);
    }
    private void hideView(View view){
        view.setVisibility(View.GONE);
    }
}
