package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.List;

public class ChampionshipPodiumActivity extends Activity {

    private LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship_podium);
        llContainer = findViewById(R.id.ll_championship_container);

        String type = getIntent().getStringExtra("type");
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(type != null ? type + " Board" : "Championship Board");

        // T ANC Ka Badshah, Bar Ka Badshah, Partybox AI Ka Badshah show Quantity + EntryTime instead of Rating
        showRanking = "T ANC Ka Badshah".equalsIgnoreCase(type)
                || "Bar Ka Badshah".equalsIgnoreCase(type)
                || "BAPPA KA CHAMPION".equalsIgnoreCase(type);

        List<JSONObject> items = ChampionshipBoardActivity.groups.get(type);
        if (items == null || items.isEmpty()) {
            showMessage("No data available");
        } else {
            buildPodium(items);
        }
    }

    public void actionBarBackButtonClicked(View v) { onBackPressed(); }

    // Whether the current board type should show Quantity + EntryTime instead of Rating
    private boolean showRanking = false;

    private void buildPodium(List<JSONObject> items) {
        llContainer.removeAllViews();

        JSONObject rank1 = getByRank(items, 1);
        JSONObject rank2 = getByRank(items, 2);
        JSONObject rank3 = getByRank(items, 3);

        if (rank1 != null) llContainer.addView(inflateCard(rank1, true, null));

        if (rank2 != null || rank3 != null) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);

            if (rank2 != null) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                lp.setMargins(0, 0, dpToPx(6), 0);
                row.addView(inflateCard(rank2, false, lp));
            }
            if (rank3 != null) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                lp.setMargins(dpToPx(6), 0, 0, 0);
                row.addView(inflateCard(rank3, false, lp));
            }
            llContainer.addView(row);
        }
    }

    private JSONObject getByRank(List<JSONObject> items, int rank) {
        for (JSONObject o : items) if (o.optInt("RankId") == rank) return o;
        return null;
    }

    private View inflateCard(JSONObject obj, boolean big, LinearLayout.LayoutParams lp) {
        int rank = obj.optInt("RankId");
        String[] medals = {"🥇", "🥈", "🥉"};
        String[] bgColors = {"#FFF8DC", "#F0F0F0", "#FDEBD0"};

        View card = LayoutInflater.from(this).inflate(
                big ? R.layout.champ_card_rank1 : R.layout.champ_card_rank23, llContainer, false);

        if (lp != null) card.setLayoutParams(lp);

        androidx.cardview.widget.CardView cv = (androidx.cardview.widget.CardView) card;
        cv.setCardBackgroundColor(Color.parseColor(bgColors[rank - 1]));
        cv.setCardElevation(dpToPx(8));

        ((TextView) card.findViewById(R.id.tv_rank_label)).setText(medals[rank - 1] + "  Rank " + rank);
        ((TextView) card.findViewById(R.id.tv_name)).setText("Name : " + val(obj.optString("Name")));
        ((TextView) card.findViewById(R.id.tv_userid)).setText("Employee ID : " + val(obj.optString("EmployeeID")));
        ((TextView) card.findViewById(R.id.tv_account)).setText("Account : " + val(obj.optString("Account")));
        ((TextView) card.findViewById(R.id.tv_store_location)).setText("Store Location : " + val(obj.optString("StoreLocation")));

        TextView tvRating   = card.findViewById(R.id.tv_rating);
        TextView tvEntryTime = card.findViewById(R.id.tv_entry_time);
        TextView tvQuantity  = card.findViewById(R.id.tv_quantity);

        if (showRanking) {
            // T ANC Ka Badshah / Bar Ka Badshah / Partybox AI Ka Badshah — show Quantity + EntryTime
            tvRating.setVisibility(View.GONE);
            tvEntryTime.setVisibility(View.VISIBLE);
            tvEntryTime.setText("Entry Time : " + val(obj.optString("EntryTime")));
            tvQuantity.setVisibility(View.VISIBLE);
            tvQuantity.setText("Quantity : " + val(obj.optString("Quantity")));
        } else {
            // All India / Region / Channel — show Rating
            tvRating.setVisibility(View.VISIBLE);
            tvRating.setText("Rating : " + val(obj.optString("Rating")));
            tvEntryTime.setVisibility(View.GONE);
            tvQuantity.setVisibility(View.GONE);
        }

        ImageView avatar = card.findViewById(R.id.iv_avatar);
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(Color.WHITE);
        circle.setStroke(dpToPx(2), Color.BLACK);
        avatar.setBackground(circle);
        avatar.setClipToOutline(true);

        String picPath = obj.optString("ProfilePicture", "");
        if (!picPath.isEmpty() && !picPath.equals("null")) {
            String picUrl = "http://harman.infield.co.in/" + picPath;
            Glide.with(this).load(picUrl).circleCrop().placeholder(R.drawable.profile_new).into(avatar);
        }

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

    private String val(String s) {
        return (s == null || s.trim().isEmpty() || s.equalsIgnoreCase("null")) ? "--" : s;
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
