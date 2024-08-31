package com.fieldforce.harmonkardonff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
//        viewHolder.target_category.setText(trMatItem.getSales() != null ? trMatItem.getSales() : "");
        viewHolder.targetachievementpercenatege.setText(trMatItem.getAch_perc() != null ? trMatItem.getAch_perc() : "");
        // Glide.with(viewHolder.itemView.getContext()).load(getThumbnail(trMatItem.getFileType())).into(viewHolder.ivItem);
    }



    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void addData(List<TargetModelAll> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView catName,target,targetAchievement,targetachievementpercenatege,target_category;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            targetachievementpercenatege = itemView.findViewById(R.id.target_acheivement_percentage);
            catName = itemView.findViewById(R.id.cat_name);
            target = itemView.findViewById(R.id.target_acheivement_target);

            targetAchievement = itemView.findViewById(R.id.acheivement_target);

        }
    }

}

