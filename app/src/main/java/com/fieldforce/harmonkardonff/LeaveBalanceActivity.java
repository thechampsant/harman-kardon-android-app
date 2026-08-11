package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import mob.field.harmonkardonff.services.WebService;

public class LeaveBalanceActivity extends Activity {

    private LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_balance);
        llContainer = findViewById(R.id.ll_leave_container);
        new LoadLeaveBalance().execute();
    }

    public void actionBarBackButtonClicked(View v) {
        onBackPressed();
    }

    private void buildTable(JSONArray data) throws Exception {
        llContainer.removeAllViews();

        // Header row
        addRow(llContainer, "Type", "Allocated", "Used", "Balance", true);

        // Divider
        addDivider(llContainer);

        DecimalFormat df = new DecimalFormat("0.00");

        // Data rows
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);

            addRow(llContainer,
                    item.optString("LeaveType", "-"),
                    df.format(item.optDouble("Allocated", 0)),
                    df.format(item.optDouble("Used", 0)),
                    df.format(item.optDouble("Balance", 0)),
                    false);

            if (i < data.length() - 1) {
                addDivider(llContainer);
            }
        }
    }

    private void addRow(LinearLayout parent, String col1, String col2, String col3, String col4, boolean isHeader) {
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dpToPx(12), dpToPx(10), dpToPx(12), dpToPx(10));
        if (isHeader) row.setBackgroundColor(Color.parseColor("#F5F5F5"));

        row.addView(makeCell(col1, isHeader, Gravity.START));
        row.addView(makeCell(col2, isHeader, Gravity.CENTER));
        row.addView(makeCell(col3, isHeader, Gravity.CENTER));
        row.addView(makeCell(col4, isHeader, Gravity.CENTER));
        parent.addView(row);
    }

    private TextView makeCell(String text, boolean isHeader, int gravity) {
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        tv.setLayoutParams(lp);
        tv.setText(text);
        tv.setTextSize(14);
        tv.setGravity(gravity);
        if (isHeader) {
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(Color.parseColor("#333333"));
        } else {
            tv.setTextColor(Color.parseColor("#555555"));
        }
        return tv;
    }

    private void addDivider(LinearLayout parent) {
        View divider = new View(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(dpToPx(12), 0, dpToPx(12), 0);
        divider.setLayoutParams(lp);
        divider.setBackgroundColor(Color.parseColor("#E0E0E0"));
        parent.addView(divider);
    }

    private void showMessage(String msg) {
        llContainer.removeAllViews();
        TextView tv = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(dpToPx(16), dpToPx(24), dpToPx(16), 0);
        tv.setLayoutParams(lp);
        tv.setText(msg);
        tv.setTextSize(15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.parseColor("#888888"));
        llContainer.addView(tv);
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    public class LoadLeaveBalance extends AsyncTask<Void, Void, JSONObject> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(LeaveBalanceActivity.this, "", "Loading...");
            mDialog.setCancelable(true);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                WebService server = new WebService();
                if (!server.isNetworkAvailable(LeaveBalanceActivity.this)) return null;
                JSONArray result = server.getLeaveBalance();
                if (result != null && result.length() > 0)
                    return result.getJSONObject(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject wrapper) {
            if (mDialog != null) mDialog.dismiss();
            if (wrapper == null) {
                showMessage("Internet not available");
                return;
            }
            if (!wrapper.optBoolean("status", false)) {
                showMessage("No Leave Balance Available.");
                return;
            }
            try {
                JSONArray data = wrapper.getJSONArray("data");
                if (data.length() > 0) {
                    buildTable(data);
                } else {
                    showMessage("No Leave Balance Available.");
                }
            } catch (Exception e) {
                showMessage("No Leave Balance Available.");
            }
        }
    }
}
