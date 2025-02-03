package com.fieldforce.harmonkardonff.Comptition;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;

import java.util.List;

public class ComptitionAdpter extends RecyclerView.Adapter<ComptitionAdpter.MyViewHolder> {

    private List<CompitionModel> displaypic;
    private Fragment fragment;
    Context context;

    public ComptitionAdpter(List<CompitionModel> displaypic, Fragment fragment,Context context) {
        this.displaypic = displaypic;
        this.fragment = fragment;
        this.context=context;
    }

    @Override
    public ComptitionAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comp_layout, parent, false);

        return new ComptitionAdpter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ComptitionAdpter.MyViewHolder holder, int position) {
        CompitionModel CompitionModel = displaypic.get(position);
        holder.cat.setText(CompitionModel.IsMandatory.equalsIgnoreCase("true") ?
                CompitionModel.Question + " (Mandatory)" :
                CompitionModel.Question + " (Non-Mandatory)");

       /* holder.image1Cap.setImageDrawable(context.getResources().getDrawable(
                CompitionModel.IsUpload.equalsIgnoreCase("true") ?
                        R.drawable.done :
                        R.drawable.ic_camera_48dp));*/

        holder.ed_qty.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            CompitionModel.Qty=holder.ed_qty.getText().toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
               /* if(s.length() != 0)
                    holder.ed_qty.setText("");*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return displaypic.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cat;
        public EditText ed_qty;

        public MyViewHolder(View view) {
            super(view);
            cat = view.findViewById(R.id.spin1Cap);
            ed_qty = view.findViewById(R.id.image1Cap);
        }
    }
}
