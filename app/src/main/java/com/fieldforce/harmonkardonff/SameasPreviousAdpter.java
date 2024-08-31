package com.fieldforce.harmonkardonff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import linq.ArrayList;
import mob.field.harmonkardonff.fragments.View_Module_List_new;

public class SameasPreviousAdpter extends RecyclerView.Adapter<SameasPreviousAdpter.ViewDemoViewHolder> {
    View_Module_List_new.ItemClickListener itemClickListener;
    private List<ViewModuleModel> list = new ArrayList<>();
    @NonNull
    @Override
    public SameasPreviousAdpter.ViewDemoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.same_as_previois_list,viewGroup,false);
        return new SameasPreviousAdpter.ViewDemoViewHolder(view);
    }
    public SameasPreviousAdpter(View_Module_List_new.ItemClickListener itemClickListener)
    {

        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull SameasPreviousAdpter.ViewDemoViewHolder viewDemoViewHolder, int i) {
        if(i==list.size()-1)
            viewDemoViewHolder.fabAddNew.setVisibility(View.VISIBLE);
        else
            viewDemoViewHolder.fabAddNew.setVisibility(View.GONE);
        viewDemoViewHolder.fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(i,list.get(i).BarCodeValue);
            }
        });
        viewDemoViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(i,"delete");
            }
        });
        viewDemoViewHolder.bindValues(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public void setData(ArrayList<ViewModuleModel> listWa){
        list = listWa;
        notifyDataSetChanged();
    }

    class ViewDemoViewHolder extends RecyclerView.ViewHolder
    {

        private TextView textViewCustomerName;
        private TextView textViewCustomerNumber;
        private TextView textViewCustomerMail;
        private TextView textViewCustomerAge;
        private TextView textViewProductName;
        private TextView textViewSubmittedDate;
        private Button fabAddNew;
        private Button btn_delete;
        public ViewDemoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.tv_customerNameValue);
            textViewCustomerNumber = itemView.findViewById(R.id.tv_customerNumberValue);
            textViewCustomerMail = itemView.findViewById(R.id.tv_customerMailValue);
            textViewCustomerAge = itemView.findViewById(R.id.tv_customerBarValue);
            textViewProductName = itemView.findViewById(R.id.tv_productValue);
            textViewSubmittedDate = itemView.findViewById(R.id.tv_submittedDateValue);
            fabAddNew = itemView.findViewById(R.id.fabAddNew);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }

        public void bindValues(ViewModuleModel obj){

            textViewCustomerName.setText(obj.Category);
            textViewCustomerNumber.setText(obj.SubCategory);
            textViewCustomerMail.setText(obj.SKU);
            textViewCustomerAge.setText(obj.BarCodeValue);

            textViewSubmittedDate.setText(obj.DisplayRaiseDate);
        }
    }
}


