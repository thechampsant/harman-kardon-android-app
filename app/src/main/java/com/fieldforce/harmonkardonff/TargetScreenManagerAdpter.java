package com.fieldforce.harmonkardonff;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.List;

import mob.field.harmonkardonff.entitiymodels.TargetModelAll;


public class TargetScreenManagerAdpter  extends RecyclerView.Adapter<TargetScreenManagerAdpter.ViewHolder> {
    List<TargetModelAll> items;
    boolean flag=false;

    @NonNull
    @Override
    public TargetScreenManagerAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TargetScreenManagerAdpter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.target_screen_manager_adpter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TargetScreenManagerAdpter.ViewHolder viewHolder, int i) {
        TargetModelAll trMatItem = items.get(i);
        viewHolder.catName.setText(trMatItem.getCategory() != null ? trMatItem.getCategory() : "");
        viewHolder.target.setText(trMatItem.getTarget() != null ? trMatItem.getTarget() : "");
        viewHolder.targetAchievement.setText(trMatItem.getSales() != null ? trMatItem.getSales() : "");
        viewHolder.target_acheivement_LY.setText(trMatItem.getLYsales() != null ? trMatItem.getLYsales() : "");
        viewHolder.targetachievementpercenatege.setText(trMatItem.getAch_perc() != null ? trMatItem.getAch_perc() + " %" : "");
        boolean isTotal = trMatItem.getCategory() != null && trMatItem.getCategory().equalsIgnoreCase("Total");

        if (isTotal) {
            viewHolder.contribution.setVisibility(View.GONE);
            viewHolder.llLastYear.setVisibility(View.GONE);
            // Move premium contribution to last position
            ViewGroup parent = (ViewGroup) viewHolder.llPremiumContribution.getParent();
            parent.removeView(viewHolder.llPremiumContribution);
            parent.addView(viewHolder.llPremiumContribution);
        } else {
            viewHolder.llLastYear.setVisibility(View.GONE);
            String contribution = trMatItem.getContribution();
            if (contribution != null && !contribution.trim().isEmpty() && !contribution.equalsIgnoreCase("null")) {
                viewHolder.contribution.setVisibility(View.VISIBLE);
                viewHolder.target_acheivement_contribution.setText(contribution + " %");
            } else {
                viewHolder.contribution.setVisibility(View.GONE);
            }
        }

        String premiumContribution = trMatItem.getPremiumContribution();
        if (premiumContribution != null && !premiumContribution.equalsIgnoreCase("null")) {
            viewHolder.llPremiumContribution.setVisibility(View.VISIBLE);
            viewHolder.target_acheivement_premium_contribution.setText(premiumContribution);
        } else {
            viewHolder.llPremiumContribution.setVisibility(View.GONE);
        }

        // Set bottom margin on last visible item
        ViewGroup parent = (ViewGroup) viewHolder.llTarget.getParent();
        LinearLayout lastVisible = null;
        for (int j = 0; j < parent.getChildCount(); j++) {
            View child = parent.getChildAt(j);
            if (child.getVisibility() == View.VISIBLE && child instanceof LinearLayout) {
                lastVisible = (LinearLayout) child;
            }
        }
        int[] childIndices = new int[parent.getChildCount()];
        for (int j = 0; j < parent.getChildCount(); j++) {
            View child = parent.getChildAt(j);
            if (child instanceof LinearLayout) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                lp.bottomMargin = (child == lastVisible) ? dpToPx(child.getContext(), 20) : 0;
                child.setLayoutParams(lp);
            }
        }
    }



    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void addData(List<TargetModelAll> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    private int dpToPx(android.content.Context context, int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView catName,target,targetAchievement,targetachievementpercenatege,target_category,target_acheivement_contribution,target_acheivement_LY,target_acheivement_premium_contribution;
        LinearLayout contribution, llPremiumContribution, llLastYear, llAchievement, llAchievementPerc, llTarget;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            targetachievementpercenatege = itemView.findViewById(R.id.target_acheivement_percentage);
            catName = itemView.findViewById(R.id.cat_name);
            target = itemView.findViewById(R.id.target_acheivement_target);
            target_acheivement_contribution = itemView.findViewById(R.id.target_acheivement_contribution);
            target_acheivement_LY = itemView.findViewById(R.id.target_acheivement_LY);
            contribution = itemView.findViewById(R.id.contribution);
            llPremiumContribution = itemView.findViewById(R.id.ll_premium_contribution);
            target_acheivement_premium_contribution = itemView.findViewById(R.id.target_acheivement_premium_contribution);
            targetAchievement = itemView.findViewById(R.id.acheivement_target);
            llLastYear = itemView.findViewById(R.id.ll_last_year);
            llAchievement = itemView.findViewById(R.id.ll_achievement);
            llAchievementPerc = itemView.findViewById(R.id.ll_achievement_perc);
            llTarget = itemView.findViewById(R.id.ll_target);
        }
    }

}

