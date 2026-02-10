package com.fieldforce.harmonkardonff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mob.field.harmonkardonff.entitiymodels.TargetModelAll;

public class TargetScreenManagerAdpterQTY extends RecyclerView.Adapter<TargetScreenManagerAdpterQTY.ViewHolder> {

    List<TargetModelAll> list = new ArrayList<>();

    public void addData(List<TargetModelAll> listData) {
        this.list = listData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_target_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        TargetModelAll m = list.get(position);

        h.category.setText(m.category);
        h.ly.setText(m.getLYsales() + "");
        h.tgt.setText(m.getTarget() + "");
        h.ach.setText(m.getSales() + "");

        try {
            String raw = m.getAch_perc();

            double val = parseDoubleSafe(raw);

            h.achPer.setText(String.format("%.1f%%", val));


        } catch (Exception e) {
            h.achPer.setText("0%");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private double parseDoubleSafe(String s) {
        if (s == null || s.trim().isEmpty() || s.equalsIgnoreCase("null"))
            return 0;

        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView category, ly, tgt, ach, achPer;

        public ViewHolder(View v) {
            super(v);
            category = v.findViewById(R.id.tv_category);
            ly = v.findViewById(R.id.tv_ly);
            tgt = v.findViewById(R.id.tv_tgt);
            ach = v.findViewById(R.id.tv_ach);
            achPer = v.findViewById(R.id.tv_ach_per);
        }
    }
}
