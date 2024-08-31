package com.fieldforce.floorhygiene;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fieldforce.floorhygiene.models.FilePath;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.utility.widgets.RobotoTextView;

import java.util.List;

public class AdapterFloorHygieneData extends RecyclerView.Adapter<AdapterFloorHygieneData.ViewHolder> {
    private List<FilePath> items;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_rv_floor_hygiene, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(viewHolder.itemView.getContext()).load(items.get(i).filePath).into(viewHolder.ivFloorHygiene);
        viewHolder.tvFloorHygiene.setText(items.get(i).typeName);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void addData(List<FilePath> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFloorHygiene;
        RobotoTextView tvFloorHygiene;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFloorHygiene = itemView.findViewById(R.id.iv_floor_hygiene);
            tvFloorHygiene = itemView.findViewById(R.id.tv_label_image);

        }
    }
}
