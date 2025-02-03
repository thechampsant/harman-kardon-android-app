package com.fieldforce.harmonkardonff.demo_tracking_module.ui.fragments;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.demo_tracking_module.models.ViewDemoResponseModel;

import java.util.List;

import linq.ArrayList;

public class ViewDemoRecyclerAdapter extends RecyclerView.Adapter<ViewDemoRecyclerAdapter.ViewDemoViewHolder> {

    private List<ViewDemoResponseModel> list = new ArrayList<>();
    @NonNull
    @Override
    public ViewDemoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_demo_item,viewGroup,false);
        return new ViewDemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDemoViewHolder viewDemoViewHolder, int i) {
        viewDemoViewHolder.bindValues(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public void setData(ArrayList<ViewDemoResponseModel> listWa){
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
        public ViewDemoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.tv_customerNameValue);
            textViewCustomerNumber = itemView.findViewById(R.id.tv_customerNumberValue);
            textViewCustomerMail = itemView.findViewById(R.id.tv_customerMailValue);
            textViewCustomerAge = itemView.findViewById(R.id.tv_customerAgeValue);
            textViewProductName = itemView.findViewById(R.id.tv_productValue);
            textViewSubmittedDate = itemView.findViewById(R.id.tv_submittedDateValue);
        }

        public void bindValues(ViewDemoResponseModel obj){
            if(!obj.CustomerName.equalsIgnoreCase(""))
                textViewCustomerName.setText(obj.CustomerName);
            else
                textViewCustomerName.setText("NA");


            if(!obj.CustomerMob.equalsIgnoreCase(""))
                textViewCustomerNumber.setText(obj.CustomerMob);
            else
                textViewCustomerNumber.setText("NA");



           // textViewCustomerMail.setText(obj.CustomerEmail);
            if(obj.NoDemo.equalsIgnoreCase("true"))
             textViewCustomerAge.setText("No Demo");
            else
                textViewCustomerAge.setText("NA");

            if(!obj.ProductName.equalsIgnoreCase(""))
                textViewProductName.setText(obj.ProductName);
            else
                textViewProductName.setText("NA");


            textViewSubmittedDate.setText(obj.SubmittedOn);
        }
    }
}
