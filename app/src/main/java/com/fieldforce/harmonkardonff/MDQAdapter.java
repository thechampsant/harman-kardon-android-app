package com.fieldforce.harmonkardonff;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MDQAdapter extends RecyclerView.Adapter<MDQAdapter.MyViewHolder> {

    private List<MDQSkuModel> list;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cat;
        public AppCompatTextView set_qty, slm_value;
        public AppCompatEditText actual_qty;
        public Spinner yesNoSpinner;

        public MyViewHolder(View view) {
            super(view);
            cat = view.findViewById(R.id.spin1Cap);
            set_qty = view.findViewById(R.id.set_qty);
            slm_value = view.findViewById(R.id.slm_value);
            actual_qty = view.findViewById(R.id.actual_qty);
            yesNoSpinner = view.findViewById(R.id.yes_no_spinner);
        }
    }

    public MDQAdapter(List<MDQSkuModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void updateList(List<MDQSkuModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mdq_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MDQSkuModel model = list.get(position);

        holder.cat.setText(model.value);
        holder.set_qty.setText(String.valueOf(model.set_qty));
        holder.slm_value.setText(model.SLMStatus != null && !model.SLMStatus.isEmpty() && !model.SLMStatus.equals("null") ? model.SLMStatus : "0");

        // remove old watcher before setText to avoid wrong model update
        if (holder.actual_qty.getTag() instanceof TextWatcher) {
            holder.actual_qty.removeTextChangedListener((TextWatcher) holder.actual_qty.getTag());
            holder.actual_qty.setTag(null);
        }

        holder.actual_qty.setText(String.valueOf(model.actual_qty));

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String val = s.toString().trim();
                    model.actual_qty = val.isEmpty() ? 0 : Integer.parseInt(val);
                } catch (NumberFormatException ignored) {}
            }
        };
        holder.actual_qty.setTag(watcher);
        holder.actual_qty.addTextChangedListener(watcher);

        if (holder.yesNoSpinner.getAdapter() == null) {
            ArrayAdapter<String> yesNoAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item,
                    new String[]{"Yes", "No"});
            holder.yesNoSpinner.setAdapter(yesNoAdapter);
        }

        holder.yesNoSpinner.setOnItemSelectedListener(null);
        holder.yesNoSpinner.setSelection("No".equalsIgnoreCase(model.yesNo) ? 1 : 0, false);

        holder.yesNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                model.yesNo = parent.getItemAtPosition(pos).toString();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
