package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
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

import mob.field.harmonkardonff.services.WebService;

public class ScoreCardActivity extends Activity {

    private LinearLayout llContainer;
    private Spinner spinnerMonth, spinnerYear;
    private int selectedMonth, selectedYear;
    private boolean spinnerReady = false;

    private final String[] MONTHS = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_card);
        llContainer = findViewById(R.id.ll_score_container);
        spinnerMonth = findViewById(R.id.spinner_month);
        spinnerYear = findViewById(R.id.spinner_year);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        selectedMonth = cal.get(java.util.Calendar.MONTH) + 1;
        selectedYear = cal.get(java.util.Calendar.YEAR);

        setupSpinners();
    }

    public void actionBarBackButtonClicked(View v) {
        onBackPressed();
    }

    private void setupSpinners() {
        // Month spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, MONTHS);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);
        spinnerMonth.setSelection(selectedMonth - 1);

        // Year spinner — last 5 years to current year only
        int endYear = selectedYear;
        int startYear = 1900;
        String[] years = new String[endYear - startYear + 1];
        for (int i = 0; i < years.length; i++) years[i] = String.valueOf(startYear + i);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
        spinnerYear.setSelection(years.length - 1); // current year selected

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                if (!spinnerReady) { spinnerReady = true; return; }
                selectedMonth = spinnerMonth.getSelectedItemPosition() + 1;
                selectedYear = Integer.parseInt((String) spinnerYear.getSelectedItem());
                new LoadScoreCard().execute();
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        };
        spinnerMonth.setOnItemSelectedListener(listener);
        spinnerYear.setOnItemSelectedListener(listener);

        new LoadScoreCard().execute();
    }

    // score 1-6 = sad(red), 7-8 = neutral(orange), 9-10 = happy(green)
    private int getEmojiRes(int score, boolean active) {
        if (score <= 6) return active ? R.drawable.ic_emoji_sad : R.drawable.ic_emoji_sad_grey;
        if (score <= 8) return active ? R.drawable.ic_emoji_neutral : R.drawable.ic_emoji_neutral_grey;
        return active ? R.drawable.ic_emoji_happy : R.drawable.ic_emoji_happy_grey;
    }

    private String getEmojiColor(int score) {
        if (score <= 6) return "#E8536A";
        if (score <= 8) return "#F0A500";
        return "#4CAF50";
    }

    private void buildScoreCard(String rating) {
        llContainer.removeAllViews();

        int score = 0;
        try { score = (int) Double.parseDouble(rating); } catch (Exception ignored) {}
        final int finalScore = score;

        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 8, 0, 16);
        card.setLayoutParams(cardParams);
        card.setRadius(dpToPx(20));
        card.setCardElevation(dpToPx(8));
        card.setCardBackgroundColor(Color.WHITE);

        LinearLayout inner = new LinearLayout(this);
        inner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setGravity(Gravity.CENTER_HORIZONTAL);
        card.addView(inner);

        // Single big emoji
        ImageView emojiView = new ImageView(this);
        LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(dpToPx(64), dpToPx(64));
        ep.setMargins(0, dpToPx(20), 0, dpToPx(8));
        ep.gravity = Gravity.CENTER_HORIZONTAL;
        emojiView.setLayoutParams(ep);
        // no data → grey emoji
        emojiView.setImageResource(getEmojiRes(finalScore, finalScore > 0));
        inner.addView(emojiView);

        // Rating text
        TextView tvRating = new TextView(this);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rp.setMargins(0, 4, 0, dpToPx(20));
        tvRating.setLayoutParams(rp);
        tvRating.setText(finalScore > 0 ? "Rating : " + rating : "No Data");
        tvRating.setTextSize(18);
        tvRating.setTypeface(null, Typeface.BOLD);
        tvRating.setTextColor(finalScore > 0 ? Color.parseColor(getEmojiColor(finalScore)) : Color.parseColor("#AAAAAA"));
        inner.addView(tvRating);

        llContainer.addView(card);
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

    public class LoadScoreCard extends AsyncTask<Void, Void, JSONObject> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(ScoreCardActivity.this, "", "Loading...");
            mDialog.setCancelable(true);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                WebService server = new WebService();
                if (!server.isNetworkAvailable(ScoreCardActivity.this)) return null;
                JSONArray result = server.getScoreRatingData(selectedMonth, selectedYear);
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
                showMessage(wrapper.optString("errormsg", "No data available"));
                return;
            }
            try {
                JSONArray data = wrapper.getJSONArray("data");
                if (data.length() > 0) {
                    buildScoreCard(data.getJSONObject(0).optString("Rating"));
                } else {
                    showMessage("No data available");
                }
            } catch (Exception e) {
                showMessage("Error loading data");
            }
        }
    }
}
