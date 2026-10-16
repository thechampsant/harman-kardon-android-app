package com.fieldforce.harmonkardonff.fixture_resolution_module.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.fixture_resolution_module.adapter.FixtureLogsRecyclerAdapter;
import com.fieldforce.harmonkardonff.fixture_resolution_module.models.FixtureLogItem;
import com.fieldforce.utility.VerticalItemDecorator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import mob.field.harmonkardonff.services.WebService;

public class ViewFixtureLogsFragment extends IFragment implements FixtureLogsRecyclerAdapter.OnCloseClickListener {

    private WebService webService = new WebService();
    private String startDate;
    private String endDate;
    private String statusFilter = "All";

    private RecyclerView recyclerView;
    private FixtureLogsRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return InflateView(R.layout.fragment_view_fixture_logs, inflater, container);
    }

    @Override
    public void Activate(View fragmentView) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_fixture_logs);
        adapter = new FixtureLogsRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalItemDecorator(16));
        recyclerView.setAdapter(adapter);

        Spinner statusSpinner = getSpinner(R.id.spinner_fixture_status_filter);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                new String[]{"All", "Open", "Closed"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusFilter = parent.getItemAtPosition(position).toString();
                if (startDate != null && endDate != null) {
                    loadLogs();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        initializeStartDate();
        initializeEndDate();
        startDateProcess(R.id.txt_fixture_from_date, R.id.btn_fixture_from_date);
        endDateProcessed(R.id.txt_fixture_to_date, R.id.btn_fixture_to_date);
        loadLogs();
    }

    private void initializeStartDate() {
        String date = GetCurrentDateInString();
        startDate = date;
        SetTextViewAsString(R.id.txt_fixture_from_date, date);
    }

    private void initializeEndDate() {
        String date = GetCurrentDateInString();
        endDate = date;
        SetTextViewAsString(R.id.txt_fixture_to_date, date);
    }

    private void startDateProcess(int idForDateTxt, int idForDateButton) {
        final TextView dateText = GetTextView(idForDateTxt);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            dateText.setText(dateFormatter.format(newDate.getTime()));
            startDate = dateFormatter.format(newDate.getTime());
            if (endDate != null && !endDate.isEmpty()) {
                loadLogs();
            } else {
                ShowToast("Please enter To Date");
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.setCancelable(false);
        findViewById(idForDateButton).setOnClickListener(arg0 -> fromDatePickerDialog.show());
    }

    private void endDateProcessed(int idForDateTxt, int idForDateButton) {
        final TextView dateText = GetTextView(idForDateTxt);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            dateText.setText(dateFormatter.format(newDate.getTime()));
            endDate = dateFormatter.format(newDate.getTime());
            if (startDate != null && !startDate.isEmpty()) {
                loadLogs();
            } else {
                ShowToast("Please enter From Date");
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        endDatePickerDialog.setCancelable(false);
        findViewById(idForDateButton).setOnClickListener(arg0 -> endDatePickerDialog.show());
    }

    public void refreshLogs() {
        loadLogs();
    }

    private void loadLogs() {
        if (!isNetworkFoundToast()) {
            return;
        }
        final String from = startDate;
        final String to = endDate;
        final String filter = statusFilter;

        BackgroundProcess bp = new BackgroundProcess(this)
                .setProgressMessage("Loading logs...")
                .setbackgroundProcess(new IProcess() {
                    @Override
                    public Object underProcess() throws Exception {
                        return webService.getFixtureLogsWrapper(from, to, filter);
                    }

                    @Override
                    public void processResponse(Object response) throws Exception {
                        JSONObject wrapper = (JSONObject) response;
                        if (wrapper == null) {
                            adapter.setItems(new ArrayList<>());
                            new Dialog(getActivity()).setTitle("Error").show("Unable to load logs.");
                            return;
                        }
                        if (!wrapper.optBoolean("status", false)) {
                            adapter.setItems(new ArrayList<>());
                            new Dialog(getActivity()).setTitle("Error")
                                    .show(wrapper.optString("errormsg", "Unable to load logs."));
                            return;
                        }
                        JSONArray data = wrapper.optJSONArray("data");
                        List<FixtureLogItem> items = new ArrayList<>();
                        if (data != null) {
                            for (int i = 0; i < data.length(); i++) {
                                items.add(FixtureLogItem.fromJson(data.getJSONObject(i)));
                            }
                        }
                        adapter.setItems(items);
                    }
                });
        bp.execute();
    }

    @Override
    public void onCloseComplaint(FixtureLogItem item) {
        if (item == null || !item.isOpen()) {
            return;
        }
        if (!isNetworkFoundToast()) {
            return;
        }
        final int complaintId = item.complaintId;
        BackgroundProcess bp = new BackgroundProcess(this)
                .setProgressMessage("Closing complaint...")
                .setbackgroundProcess(new IProcess() {
                    @Override
                    public Object underProcess() throws Exception {
                        return webService.closeFixtureComplaint(complaintId);
                    }

                    @Override
                    public void processResponse(Object response) throws Exception {
                        Response resp = (Response) response;
                        if (resp == null) {
                            new Dialog(getActivity()).setTitle("Error").show("No response from server.");
                            return;
                        }
                        if (resp.isSuccess()) {
                            ShowToast("Complaint closed.");
                            loadLogs();
                        } else {
                            new Dialog(getActivity()).setTitle("Error").show(resp.errormsg);
                        }
                    }
                });
        bp.execute();
    }
}
