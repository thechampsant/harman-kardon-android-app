package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONObject;

import mob.field.harmonkardonff.services.WebService;

public class ISPRatingActivity extends Activity {

    private LinearLayout llContainer;
    private JSONArray rawData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isp_rating);
        llContainer = findViewById(R.id.ll_dashboard_container);
        new LoadISPDash().execute();
    }

    public void actionBarBackButtonClicked(View v) {
        onBackPressed();
    }

    private LinearLayout startCard() {
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
        card.addView(inner);
        llContainer.addView(card);
        return inner;
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void addSectionHeader(LinearLayout card, String col1, String col2, String col3, String col4) {
        View header = LayoutInflater.from(this).inflate(R.layout.isp_table_header, card, false);
        ((TextView) header.findViewById(R.id.tv_col1)).setText(col1);
        ((TextView) header.findViewById(R.id.tv_col2)).setText(col2);
        ((TextView) header.findViewById(R.id.tv_col3)).setText(col3);
        ((TextView) header.findViewById(R.id.tv_col4)).setText(col4);
        card.addView(header);
    }

    private void addDataRow(LinearLayout card, String col1, String col2, String col3, String col4, boolean alternate) {
        View row = LayoutInflater.from(this).inflate(R.layout.isp_table_row, card, false);
        ((TextView) row.findViewById(R.id.tv_col1)).setText(col1);
        ((TextView) row.findViewById(R.id.tv_col2)).setText(col2);
        ((TextView) row.findViewById(R.id.tv_col3)).setText(col3);
        ((TextView) row.findViewById(R.id.tv_col4)).setText(col4);
        card.addView(row);
    }

    private void addEmojiRow(LinearLayout card, String scoreStr) {
        int[] resIds = {
                R.drawable.ic_emoji_sad_grey, R.drawable.ic_emoji_sad_grey,
                R.drawable.ic_emoji_sad_grey, R.drawable.ic_emoji_sad_grey,
                R.drawable.ic_emoji_sad_grey, R.drawable.ic_emoji_sad_grey,
                R.drawable.ic_emoji_neutral_grey, R.drawable.ic_emoji_neutral_grey,
                R.drawable.ic_emoji_happy_grey, R.drawable.ic_emoji_happy_grey
        };
        int[] activeResIds = {
                R.drawable.ic_emoji_sad, R.drawable.ic_emoji_sad,
                R.drawable.ic_emoji_sad, R.drawable.ic_emoji_sad,
                R.drawable.ic_emoji_sad, R.drawable.ic_emoji_sad,
                R.drawable.ic_emoji_neutral, R.drawable.ic_emoji_neutral,
                R.drawable.ic_emoji_happy, R.drawable.ic_emoji_happy
        };
        String[] bgColors = {
                "#E8536A","#E8536A","#E8536A","#E8536A","#E8536A","#E8536A",
                "#F0A500","#F0A500","#4CAF50","#4CAF50"
        };
        int score = 0;
        try { score = (int) Double.parseDouble(scoreStr); } catch (Exception ignored) {}
        final int selectedRating = score;

        LinearLayout emojiRow = new LinearLayout(this);
        emojiRow.setOrientation(LinearLayout.HORIZONTAL);
        emojiRow.setGravity(android.view.Gravity.CENTER);
        LinearLayout.LayoutParams erp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        erp.setMargins(8, 8, 8, 4);
        emojiRow.setLayoutParams(erp);

        LinearLayout numberRow = new LinearLayout(this);
        numberRow.setOrientation(LinearLayout.HORIZONTAL);
        numberRow.setGravity(android.view.Gravity.CENTER);
        LinearLayout.LayoutParams nrp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        nrp.setMargins(8, 0, 8, 8);
        numberRow.setLayoutParams(nrp);

        for (int i = 0; i < 10; i++) {
            boolean active = (i + 1) <= selectedRating;
            android.widget.ImageView emoji = new android.widget.ImageView(this);
            LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(80, 80);
            ep.setMargins(4, 4, 4, 4);
            emoji.setLayoutParams(ep);
            emoji.setImageResource(active ? activeResIds[i] : resIds[i]);
            emojiRow.addView(emoji);

            TextView numBtn = new TextView(this);
            LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(80, 60);
            np.setMargins(4, 0, 4, 0);
            numBtn.setLayoutParams(np);
            numBtn.setText(String.valueOf(i + 1));
            numBtn.setGravity(android.view.Gravity.CENTER);
            numBtn.setTypeface(null, android.graphics.Typeface.BOLD);
            numBtn.setTextSize(13);
            numBtn.setTextColor(active ? Color.parseColor(bgColors[i]) : Color.parseColor("#AAAAAA"));
            numberRow.addView(numBtn);
        }
        card.addView(emojiRow);
        card.addView(numberRow);
    }

    private void buildDashboard() throws Exception {
        llContainer.removeAllViews();
        for (int i = 0; i < rawData.length(); i++) {
            JSONObject item = rawData.getJSONObject(i);
            String category = item.optString("Category");
            Object value = item.opt("Value");
            LinearLayout card = startCard();

            if (value instanceof JSONObject) {
                JSONObject val = (JSONObject) value;
                java.util.Iterator<String> keys = val.keys();
                java.util.List<String> keyList = new java.util.ArrayList<>();
                while (keys.hasNext()) keyList.add(keys.next());
                String k1 = keyList.size() > 0 ? keyList.get(0) : "";
                String k2 = keyList.size() > 1 ? keyList.get(1) : "";
                String k3 = keyList.size() > 2 ? keyList.get(2) : "";
                addSectionHeader(card, category, k1, k2, k3);
                addDataRow(card, category, val.optString(k1), val.optString(k2), val.optString(k3), false);

            } else if (value instanceof JSONArray) {
                JSONArray arr = (JSONArray) value;
                addSectionHeader(card, category, "Target", "Ach", "Ach %");
                for (int j = 0; j < arr.length(); j++) {
                    JSONObject sub = arr.getJSONObject(j);
                    String type = sub.optString("Type");
                    if (type.isEmpty()) type = category;
                    JSONObject subVal = sub.optJSONObject("Value");
                    if (subVal != null)
                        addDataRow(card, type, subVal.optString("Target"), subVal.optString("Achievement"), subVal.optString("AchPer") + "%", j % 2 != 0);
                }

            } else if (value instanceof String) {
                addSectionHeader(card, category, "", "", "");
                addDataRow(card, "Total Score", (String) value, "", "", false);
                addEmojiRow(card, (String) value);
            }
        }
    }

    public class LoadISPDash extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(ISPRatingActivity.this, "", "Loading...");
            mDialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                WebService server = new WebService();
                if (server.isNetworkAvailable(ISPRatingActivity.this))
                    rawData = server.getISPDashData();
                else
                    rawData = null;
            } catch (Exception e) {
                e.printStackTrace();
                rawData = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (mDialog != null) mDialog.dismiss();
            if (rawData == null) {
                Toast.makeText(ISPRatingActivity.this, "Internet not available", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject wrapper = rawData.getJSONObject(0);
                if (wrapper.optBoolean("status", false)) {
                    rawData = wrapper.getJSONArray("data");
                    buildDashboard();
                } else {
                    Toast.makeText(ISPRatingActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ISPRatingActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
