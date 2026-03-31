package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import app.core.model.Response;
import mob.field.harmonkardonff.services.WebService;

public class MDQActivity extends Activity {

    private Button btn_Submit;
    private AppCompatEditText searchField;
    private SearchableSpinner store_category;
    private TextView storeName, dpsalefor;
    private LinearLayout searchView, llview, llItemsContainer;
    private boolean categorySelected = false;

    private List<MDQSkuModel> allSkuList = new ArrayList<>();
    private List<MDQSkuModel> categoryFilteredList = new ArrayList<>();
    private String selectedCategory = "ALL";
    private List<String> categoryList = new ArrayList<>();

    private org.json.JSONArray mdqRawData = null;
    private String currentStoreId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mdq_view);

        dpsalefor = findViewById(R.id.dpsalefor);
        dpsalefor.setText("Date : " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

        storeName = findViewById(R.id.store_name);
        if (MainActivity.MyInfo != null) {
            storeName.setText(MainActivity.MyInfo.CurrentStore);
            currentStoreId = MainActivity.MyInfo.StoreID != null ? MainActivity.MyInfo.StoreID : "";
        }

        store_category = findViewById(R.id.store_category);
        searchField = findViewById(R.id.searchField);
        btn_Submit = findViewById(R.id.bt_submit);
        searchView = findViewById(R.id.search_view);
        llview = findViewById(R.id.llview);
        llItemsContainer = findViewById(R.id.ll_items_container);

        searchField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        btn_Submit.setOnClickListener(v -> {
            if (!categorySelected) {
                Toast.makeText(MDQActivity.this, "Please select category", Toast.LENGTH_SHORT).show();
                return;
            }
            submitData();
        });

        new LoadMDQData().execute();
    }

    private void filter(String text) {
        List<MDQSkuModel> source = selectedCategory.equalsIgnoreCase("ALL") ? allSkuList : categoryFilteredList;
        if (text.isEmpty()) {
            updateRecyclerView(source);
            return;
        }
        List<MDQSkuModel> temp = new ArrayList<>();
        for (MDQSkuModel d : source) {
            if (d.value != null && d.value.toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        updateRecyclerView(temp);
    }

    private void setupCategorySpinner() {
        Set<String> categorySet = new HashSet<>();
        for (MDQSkuModel item : allSkuList) {
            if (item.parent_value != null) categorySet.add(item.parent_value);
        }
        categoryList = new ArrayList<>(categorySet);
        Collections.sort(categoryList);
        categoryList.add(0, "ALL");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categoryList);
        store_category.setAdapter(adapter);

        store_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirstCall = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstCall) {
                    isFirstCall = false;
                    updateRecyclerView(allSkuList);
                    return;
                }
                String selected = categoryList.get(position);
                categorySelected = true;
                llItemsContainer.setVisibility(View.VISIBLE);
                selectedCategory = selected;
                categoryFilteredList.clear();
                if (selected.equalsIgnoreCase("ALL")) {
                    updateRecyclerView(allSkuList);
                } else {
                    for (MDQSkuModel item : allSkuList) {
                        if (selected.equals(item.parent_value)) categoryFilteredList.add(item);
                    }
                    updateRecyclerView(categoryFilteredList);
                }
                searchField.setText("");
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateRecyclerView(List<MDQSkuModel> list) {
        llItemsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (MDQSkuModel sku : list) {
            View row = inflater.inflate(R.layout.mdq_layout, llItemsContainer, false);
            ((TextView) row.findViewById(R.id.spin1Cap)).setText(sku.value);
            ((AppCompatTextView) row.findViewById(R.id.set_qty)).setText(String.valueOf(sku.set_qty));
            ((AppCompatTextView) row.findViewById(R.id.slm_value)).setText(sku.SLMStatus != null ? sku.SLMStatus : "0");
            AppCompatEditText actualQty = row.findViewById(R.id.actual_qty);
            actualQty.setText(String.valueOf(sku.actual_qty));
            actualQty.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void afterTextChanged(Editable s) {
                    try { sku.actual_qty = s.toString().isEmpty() ? 0 : Integer.parseInt(s.toString()); }
                    catch (NumberFormatException ignored) {}
                }
            });
            Spinner spinner = row.findViewById(R.id.yes_no_spinner);
            ArrayAdapter<String> yesNoAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, new String[]{"Yes", "No"});
            spinner.setAdapter(yesNoAdapter);
            spinner.setSelection("No".equalsIgnoreCase(sku.yesNo) ? 1 : 0, false);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                    sku.yesNo = p.getItemAtPosition(pos).toString();
                }
                @Override public void onNothingSelected(AdapterView<?> p) {}
            });
            llItemsContainer.addView(row);
        }
    }

    private void submitData() {
        new SubmitMDQData().execute();
    }


    public class LoadMDQData extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(MDQActivity.this, "", "Loading...");
            mDialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                WebService server = new WebService();
                if (server.isNetworkAvailable(MDQActivity.this))
                    mdqRawData = server.getMDQData(currentStoreId);
                else
                    mdqRawData = null;
            } catch (Exception e) {
                e.printStackTrace();
                mdqRawData = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (mDialog != null) mDialog.dismiss();
            if (mdqRawData == null) {
                Toast.makeText(MDQActivity.this, "Internet not available", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject wrapper = mdqRawData.getJSONObject(0);
                boolean status = wrapper.optBoolean("status", false);
                if (status) {
                    allSkuList.clear();
                    JSONArray data = wrapper.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        MDQSkuModel sku = new MDQSkuModel();
                        sku.PID = obj.optInt("PID");
                        sku.parent_value = obj.optString("parent_value");
                        sku.value = obj.optString("value");
                        sku.set_qty = obj.optInt("set_qty");
                        sku.actual_qty = obj.optInt("actual_qty");
                        sku.SLMStatus = obj.optString("SLMStatus");
                        sku.yesNo = "Yes";
                        allSkuList.add(sku);
                    }
                    updateRecyclerView(allSkuList);
                    setupCategorySpinner();
                } else {
                    Toast.makeText(MDQActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MDQActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ---------------------- Submit MDQ Data AsyncTask ----------------------
    public class SubmitMDQData extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;
        private Response submitResponse;

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(MDQActivity.this, "", "Submitting...");
            mDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                JSONArray jsonArray = new JSONArray();
                for (MDQSkuModel sku : allSkuList) {
                    JSONObject skuJson = new JSONObject();
                    skuJson.put("StoreId", currentStoreId);
                    skuJson.put("PID", sku.PID);
                    skuJson.put("Qty", sku.actual_qty);
                    skuJson.put("Display", sku.yesNo);
                    skuJson.put("UserName", WebService.UserName);
                    jsonArray.put(skuJson);
                }

                WebService server = new WebService();
                if (server.isNetworkAvailable(MDQActivity.this))
                    submitResponse = server.submitMDQData(jsonArray.toString());
                else
                    submitResponse = null;
            } catch (Exception e) {
                e.printStackTrace();
                submitResponse = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (mDialog != null) mDialog.dismiss();
            if (submitResponse == null) {
                Toast.makeText(MDQActivity.this, "Internet not available", Toast.LENGTH_SHORT).show();
                return;
            }
            if (submitResponse.status != null && submitResponse.status.equalsIgnoreCase("true")) {
                Toast.makeText(MDQActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(MDQActivity.this, "Submission failed: " + submitResponse.errormsg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
