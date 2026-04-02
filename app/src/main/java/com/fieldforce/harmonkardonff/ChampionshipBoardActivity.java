package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mob.field.harmonkardonff.services.WebService;

public class ChampionshipBoardActivity extends Activity {

    private LinearLayout llButtons;
    public static Map<String, List<JSONObject>> groups = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship_board);
        llButtons = findViewById(R.id.ll_buttons);
        new LoadChampionship().execute();
    }

    public void actionBarBackButtonClicked(View v) { onBackPressed(); }

    private void buildButtons() {
        llButtons.removeAllViews();
        String[] labels = new String[groups.size()];
        groups.keySet().toArray(labels);

        for (String label : labels) {
            Button btn = new Button(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(48));
            lp.setMargins(0, 0, 0, dpToPx(12));
            btn.setLayoutParams(lp);
            btn.setText(label);
            btn.setTextColor(Color.WHITE);
            btn.setTextSize(16);
            btn.setAllCaps(false);
            btn.setBackground(getResources().getDrawable(R.drawable.custom_button));

            btn.setOnClickListener(v -> {
                Intent intent = new Intent(this, ChampionshipPodiumActivity.class);
                intent.putExtra("type", label);
                startActivity(intent);
            });
            llButtons.addView(btn);
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    public class LoadChampionship extends AsyncTask<Void, Void, JSONObject> {
        private ProgressDialog mDialog;

        @Override protected void onPreExecute() {
            mDialog = ProgressDialog.show(ChampionshipBoardActivity.this, "", "Loading...");
            mDialog.setCancelable(true);
        }

        @Override protected JSONObject doInBackground(Void... p) {
            try {
                WebService server = new WebService();
                if (!server.isNetworkAvailable(ChampionshipBoardActivity.this)) return null;
                JSONArray result = server.getChampionShipData();
                if (result != null && result.length() > 0) return result.getJSONObject(0);
            } catch (Exception e) { e.printStackTrace(); }
            return null;
        }

        @Override protected void onPostExecute(JSONObject wrapper) {
            if (mDialog != null) mDialog.dismiss();
            if (wrapper == null) { Toast.makeText(ChampionshipBoardActivity.this, "Internet not available", Toast.LENGTH_SHORT).show(); return; }
            if (!wrapper.optBoolean("status", false)) {
                Toast.makeText(ChampionshipBoardActivity.this, wrapper.optString("errormsg", "No data available"), Toast.LENGTH_SHORT).show(); return;
            }
            try {
                JSONArray data = wrapper.getJSONArray("data");
                groups.clear();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject obj = data.getJSONObject(i);
                    String type = obj.optString("ChampType", "Other");
                    if (type.equals("PAN")) type = "All India";
                    if (!groups.containsKey(type)) groups.put(type, new ArrayList<>());
                    groups.get(type).add(obj);
                }
                buildButtons();
            } catch (Exception e) {
                Toast.makeText(ChampionshipBoardActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
