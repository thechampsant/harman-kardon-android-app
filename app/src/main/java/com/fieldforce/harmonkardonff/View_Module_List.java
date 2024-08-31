package com.fieldforce.harmonkardonff;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonhelper.GPSTracker;
import com.fieldforce.harmonkardonff.demo_tracking_module.models.ViewDemoResponseModel;
import com.fieldforce.harmonkardonff.demo_tracking_module.ui.fragments.ViewDemoRecyclerAdapter;
import com.fieldforce.utility.VerticalItemDecorator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.base.InnosolsActivity;
import app.core.model.Response;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.services.WebService;

public class View_Module_List extends IFragment {

    private String startDate;
    private String endDate;
    WebService webapi;
    private RecyclerView recyclerView;
    private TextView textViewCurrentUser;
    private ViewModuleRecyclerAdapter mAdapter;

    @Override
    public void Activate(View FragmentView) {
        startDateProcess(R.id.txt_for_dates, R.id.btn_for_dates);
        endDateProcessed(R.id.txt_to_dates, R.id.btn_to_dates);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.view_salee, inflater, container);
    }

    @Override
    public void RegisterTableInfoForLocalDB() {

    }



    private void init(){
        webapi = new WebService();
        textViewCurrentUser = (TextView) findViewById(R.id.tv_currentUserValue);
        recyclerView = (RecyclerView) findViewById(R.id.rv_viewDemo);

        mAdapter = new ViewModuleRecyclerAdapter();
//        textViewCurrentUser.setText(WebService.UserName);
        initializeDatesAndServerRequest();
        initRecycler();

    }

    private void initRecycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalItemDecorator(30));
        recyclerView.setAdapter(mAdapter);
    }

    private void initializeDatesAndServerRequest() {
        // TODO Auto-generated method stub
        initializeStartDate();
        initializeEndDate();
        if (isNetworkAvailable())
            hitapi(startDate, endDate);

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
                    hitapi(startDate, endDate);
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
                    hitapi(startDate, endDate);
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

    private void hitapi(final String start, final String end) {
        if (!isNetworkFoundToast()) {
            return;
        }

        BackgroundProcess bp = new BackgroundProcess(this).showProgress(false);
                bp.setbackgroundProcess(
                        new IProcess() {

                            @Override
                            public Object underProcess() throws Exception {
                                // TODO Auto-generated method stub
                                return webapi.getModuleFromServer(startDate, endDate);
                            }

                            @Override
                            public void processResponse(Object response)
                                    throws Exception {
                                Response resp = (Response) response;

                                if (response == null) {
                                    return;
                                }
                                if (resp.isSuccess())
                                {
                                    if (resp.status.equalsIgnoreCase("true")){
                                        //setdataonlist(resp.data);
                                        ArrayList<ViewModuleModel> dataList = resp.data;
                                        mAdapter.setData(dataList);
                                    }
                                    else {
                                        new Dialog(getActivity()).setTitle("Server Response").setMessage(resp.errormsg).show();
                                    }
                                } else {
                                    new Dialog(getActivity()).setTitle("Server Response").setMessage(resp.errormsg).show();
                                }
                            }
                        });
        bp.execute();

    }


}
