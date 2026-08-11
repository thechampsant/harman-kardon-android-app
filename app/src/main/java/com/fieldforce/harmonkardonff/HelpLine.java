package com.fieldforce.harmonkardonff;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import app.core.base.InnosolsActivity;
import mob.field.harmonkardonff.services.WebService;

public class HelpLine extends InnosolsActivity {

    private RelativeLayout iv_backView;
    private TextView tvContactNo;
    private LinearLayout llEmailContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_line);

        iv_backView = findViewById(R.id.iv_backView);
        tvContactNo = findViewById(R.id.tv_contact_no_value);
        llEmailContainer = findViewById(R.id.ll_email_container);

        iv_backView.setOnClickListener(v -> onBackPressed());

        new LoadHelpDesk().execute();
    }

    public class LoadHelpDesk extends AsyncTask<Void, Void, JSONArray> {

        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(HelpLine.this, "", "Loading...");
            mDialog.setCancelable(false);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            try {
                WebService server = new WebService();

                if (server.isNetworkAvailable(HelpLine.this)) {
                    return server.getHelpDeskData();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            if (result == null) {
                Toast.makeText(HelpLine.this, "Internet not available", Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                JSONObject wrapper = result.getJSONObject(0);

                if (!wrapper.optBoolean("status")) {
                    Toast.makeText(HelpLine.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONArray data = wrapper.optJSONArray("data");

                if (data == null || data.length() == 0) {
                    Toast.makeText(HelpLine.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject item = data.getJSONObject(0);

                // Contact Number
                tvContactNo.setText(item.optString("ContactNo", "-"));

                // Email IDs
                llEmailContainer.removeAllViews();

                String emailIds = item.optString("EmailId", "");

                if (emailIds.trim().isEmpty()) {

                    TextView tv = new TextView(HelpLine.this);
                    tv.setText("-");
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    llEmailContainer.addView(tv);

                } else {

                    String[] emails = emailIds.split(",");

                    for (String email : emails) {

                        TextView tv = new TextView(HelpLine.this);

                        LinearLayout.LayoutParams params =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);

                        tv.setLayoutParams(params);
                        tv.setText(email.trim());
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tv.setPadding(0, 4, 0, 4);

                        llEmailContainer.addView(tv);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(HelpLine.this, "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void RegisterTableInfoForLocalDB() {

    }
}