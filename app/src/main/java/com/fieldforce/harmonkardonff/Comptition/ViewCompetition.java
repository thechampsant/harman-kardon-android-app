package com.fieldforce.harmonkardonff.Comptition;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.APIService.ApiClient;
import com.fieldforce.harmonkardonff.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vspl.docopd.API.Apiintefacec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import mob.field.harmonkardonff.services.WebService;
import retrofit2.Call;
import retrofit2.Callback;

public class ViewCompetition extends IFragment {
    RecyclerView recyclerView;
    List<CompitionModel> displaypic = new ArrayList<>();
    ViewCompAdpter mAdapter;
    WebService web = new WebService();
    Button btn_Submit;
    private String startDate;
    private String endDate;
    @Override
    public void Activate(View FragmentView) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_pic_cat);


        initializeStartDate();
        initializeEndDate();

        startDateProcess(R.id.txt_for_dates, R.id.btn_for_dates);
        endDateProcessed(R.id.txt_to_dates, R.id.btn_to_dates);
        //  SetTextViewAsString(R.id.tv_date,"Current Date:- "+GetCurrentDateInString());
        // GetTextView(R.id.tv_date).setVisibility(View.GONE);
        if (recyclerView != null) {
            ShowToast("working");

            loadFromServer(startDate, endDate);
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
                    loadFromServer(startDate, endDate);
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
                    loadFromServer(startDate, endDate);
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return this.InflateView(R.layout.view_compition, inflater,
                container);
    }



    public void loadFromServer(String startDate, String endDate) {

        // this.getListView(R.id.lv_sale_mtd).setVisibility(View.GONE);
        BackgroundProcess bp = new BackgroundProcess(this);

        bp.setbackgroundProcess(new IProcess() {

            @Override
            public void processResponse(Object arg0) throws Exception {
                // TODO Auto-generated method stub
                ProcessServerResponse((Response) arg0);
            }

            @Override
            public Object underProcess() throws Exception {
                // TODO Auto-generated method stub
                return web.ViewCompetitionData(startDate,endDate);
            }
        });
        bp.execute(null, null, null);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ProcessServerResponse(Response response) {
        if (response.status.equalsIgnoreCase("true")) {
            displaypic = response.data;
            Map<String, List<CompitionModel>> groupedData = DataUtils.groupByDate(displaypic);
            mAdapter = new ViewCompAdpter(groupedData, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            /* mAdapter.notifyDataSetChanged();*/

        } else {
            new Dialog(getActivity()).setTitle("Error").show(response.errormsg);
        }
    }




}
