package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mob.field.harmonkardonff.services.WebService;

public class ChampionshipBoardActivity extends Activity {

    private LinearLayout llContainer;
    private Spinner spinnerType;
    private Map<String, List<JSONObject>> groups = new LinkedHashMap<>();
    private boolean spinnerReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship_board);
        llContainer = findViewById(R.id.ll_championship_container);
        spinnerType = findViewById(R.id.spinner_champ_type);
        new LoadChampionship().execute();
    }

    public void actionBarBackButtonClicked(View v) { onBackPressed(); }

    private void setupSpinner() {
        List<String> types = new ArrayList<>(groups.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                if (!spinnerReady) { spinnerReady = true; return; }
                buildPodium(groups.get(types.get(pos)));
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
        // show first group
        if (!types.isEmpty()) buildPodium(groups.get(types.get(0)));
    }

    private void buildPodium(List<JSONObject> items) {
        llContainer.removeAllViews();
        if (items == null || items.isEmpty()) { showMessage("No data available"); return; }

        JSONObject rank1 = getByRank(items, 1);
        JSONObject rank2 = getByRank(items, 2);
        JSONObject rank3 = getByRank(items, 3);

        if (rank1 != null) llContainer.addView(makePodiumCard(rank1));

        if (rank2 != null || rank3 != null) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);
            if (rank2 != null) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                lp.setMargins(0, 0, dpToPx(6), 0);
                View c = makePodiumCard(rank2); c.setLayoutParams(lp); row.addView(c);
            }
            if (rank3 != null) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                lp.setMargins(dpToPx(6), 0, 0, 0);
                View c = makePodiumCard(rank3); c.setLayoutParams(lp); row.addView(c);
            }
            llContainer.addView(row);
        }
    }

    private JSONObject getByRank(List<JSONObject> items, int rank) {
        for (JSONObject o : items) if (o.optInt("RankId") == rank) return o;
        return null;
    }

    private View makePodiumCard(JSONObject obj) {
        int rank = obj.optInt("RankId");
        boolean big = rank == 1;
        String[] bgColors = {"#FFF8DC", "#F0F0F0", "#FDEBD0"};
        String[] medals = {"🥇", "🥈", "🥉"};

        CardView card = new CardView(this);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cp.setMargins(0, 0, 0, dpToPx(12));
        card.setLayoutParams(cp);
        card.setRadius(dpToPx(16));
        card.setCardElevation(dpToPx(4));
        card.setCardBackgroundColor(Color.parseColor(bgColors[rank - 1]));

        LinearLayout inner = new LinearLayout(this);
        inner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setGravity(Gravity.CENTER_HORIZONTAL);
        inner.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(16));
        card.addView(inner);

        TextView tvRank = new TextView(this);
        tvRank.setText(medals[rank - 1] + "  Rank " + rank);
        tvRank.setTextSize(big ? 16 : 13);
        tvRank.setTypeface(null, Typeface.BOLD);
        tvRank.setTextColor(Color.parseColor("#333333"));
        inner.addView(tvRank);

        ImageView avatar = new ImageView(this);
        int size = dpToPx(big ? 80 : 60);
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(size, size);
        ap.setMargins(0, dpToPx(8), 0, dpToPx(8));
        ap.gravity = Gravity.CENTER_HORIZONTAL;
        avatar.setLayoutParams(ap);
        avatar.setImageResource(R.drawable.profile_new);
        avatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(Color.WHITE);
        circle.setStroke(dpToPx(2), Color.parseColor("#DDDDDD"));
        avatar.setBackground(circle);
        avatar.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        avatar.setClipToOutline(true);
        inner.addView(avatar);

        TextView tvName = new TextView(this);
        tvName.setText(obj.optString("Name"));
        tvName.setTextSize(big ? 15 : 13);
        tvName.setTypeface(null, Typeface.BOLD);
        tvName.setTextColor(Color.parseColor("#222222"));
        tvName.setGravity(Gravity.CENTER);
        inner.addView(tvName);

        TextView tvRating = new TextView(this);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rp.setMargins(0, dpToPx(2), 0, 0);
        tvRating.setLayoutParams(rp);
        tvRating.setText(obj.optString("Rating"));
        tvRating.setTextSize(big ? 14 : 12);
        tvRating.setTextColor(Color.parseColor("#555555"));
        tvRating.setGravity(Gravity.CENTER);
        inner.addView(tvRating);

        return card;
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
            if (wrapper == null) { showMessage("Internet not available"); return; }
            if (!wrapper.optBoolean("status", false)) {
                showMessage(wrapper.optString("errormsg", "No data available")); return;
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
                if (groups.isEmpty()) { showMessage("No data available"); return; }
                setupSpinner();
            } catch (Exception e) { showMessage("Error loading data"); }
        }
    }
}
