package com.fieldforce.harmonkardonff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.demo_tracking_module.models.ViewDemoResponseModel;


import java.util.List;

import linq.ArrayList;

public class ViewDownStockAdpter extends RecyclerView.Adapter<ViewDownStockAdpter.ViewDemoViewHolder> {

    private List<ViewModuleModel> list = new ArrayList<>();
    @NonNull
    @Override
    public ViewDownStockAdpter.ViewDemoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_down_list,viewGroup,false);
        return new ViewDownStockAdpter.ViewDemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDownStockAdpter.ViewDemoViewHolder viewDemoViewHolder, int i) {
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
        private TextView tv_productIssueValue;
        public ViewDemoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.tv_customerNameValue);
            textViewCustomerNumber = itemView.findViewById(R.id.tv_customerNumberValue);
            textViewCustomerMail = itemView.findViewById(R.id.tv_customerMailValue);
            textViewCustomerAge = itemView.findViewById(R.id.tv_customerAgeValue);
            textViewProductName = itemView.findViewById(R.id.tv_productValue);
            textViewSubmittedDate = itemView.findViewById(R.id.tv_submittedDateValue);
            tv_productIssueValue = itemView.findViewById(R.id.tv_productIssueValue);
        }

        public void bindValues(ViewModuleModel obj){
            textViewCustomerName.setText(obj.Category);
            textViewCustomerNumber.setText(obj.SubCategory);
            textViewCustomerMail.setText(obj.SKU);
            textViewCustomerAge.setText(obj.StockQtyRequired);
            textViewProductName.setText(obj.CurrentStock);
            tv_productIssueValue.setText(obj.DescribeIssue);
            textViewSubmittedDate.setText(obj.DateOfOutStock);
        }
    }
}
