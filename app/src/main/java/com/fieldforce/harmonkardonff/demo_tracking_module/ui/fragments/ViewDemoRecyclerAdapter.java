package com.fieldforce.harmonkardonff.demo_tracking_module.ui.fragments;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.demo_tracking_module.models.ViewDemoResponseModel;
import com.fieldforce.utility.CommonUtility;

import java.util.List;

import linq.ArrayList;

public class ViewDemoRecyclerAdapter extends RecyclerView.Adapter<ViewDemoRecyclerAdapter.ViewDemoViewHolder> {

    private List<ViewDemoResponseModel> list = new ArrayList<>();

    @NonNull
    @Override
    public ViewDemoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_demo_item, viewGroup, false);
        return new ViewDemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDemoViewHolder viewDemoViewHolder, int i) {
        viewDemoViewHolder.bindValues(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setData(ArrayList<ViewDemoResponseModel> listWa) {
        list = listWa;
        notifyDataSetChanged();
    }

    class ViewDemoViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView textViewCustomerName;
        private TextView textViewCustomerNumber;
        private TextView textViewCustomerMail;
        private TextView textViewCustomerAge;
        private TextView textViewProductName;
        private TextView textViewSubmittedDate;
        private TextView textViewLeadType;
        private TextView textViewRemarks;
        private ImageButton buttonWhatsApp;
        private ImageButton buttonCall;

        public ViewDemoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.demo_item_card);
            textViewCustomerName = itemView.findViewById(R.id.tv_customerNameValue);
            textViewCustomerNumber = itemView.findViewById(R.id.tv_customerNumberValue);
            textViewCustomerMail = itemView.findViewById(R.id.tv_customerMailValue);
            textViewCustomerAge = itemView.findViewById(R.id.tv_customerAgeValue);
            textViewProductName = itemView.findViewById(R.id.tv_productValue);
            textViewSubmittedDate = itemView.findViewById(R.id.tv_submittedDateValue);
            textViewLeadType = itemView.findViewById(R.id.tv_leadTypeValue);
            textViewRemarks = itemView.findViewById(R.id.tv_remarksValue);
            buttonWhatsApp = itemView.findViewById(R.id.btn_whatsapp);
            buttonCall = itemView.findViewById(R.id.btn_call);
        }

        public void bindValues(final ViewDemoResponseModel obj) {
            if (obj.CustomerName != null && !obj.CustomerName.equalsIgnoreCase(""))
                textViewCustomerName.setText(obj.CustomerName);
            else
                textViewCustomerName.setText("NA");

            if (obj.CustomerMob != null && !obj.CustomerMob.equalsIgnoreCase(""))
                textViewCustomerNumber.setText(obj.CustomerMob);
            else
                textViewCustomerNumber.setText("NA");

            if (obj.NoDemo != null && obj.NoDemo.equalsIgnoreCase("true"))
                textViewCustomerAge.setText("No Demo");
            else
                textViewCustomerAge.setText("NA");

            if (obj.ProductName != null && !obj.ProductName.equalsIgnoreCase(""))
                textViewProductName.setText(obj.ProductName);
            else
                textViewProductName.setText("NA");

            textViewSubmittedDate.setText(obj.SubmittedOn != null ? obj.SubmittedOn : "NA");
            textViewLeadType.setText(obj.LeadType != null && !obj.LeadType.equalsIgnoreCase("") ? obj.LeadType : "NA");
            textViewRemarks.setText(obj.Remarks != null && !obj.Remarks.equalsIgnoreCase("") ? obj.Remarks : "NA");

            // Color code the entire card based on Lead Type
            if (obj.LeadType != null) {
                String leadTypeLower = obj.LeadType.trim().toLowerCase();
                android.util.Log.d("LeadTypeDebug", "LeadType value: '" + obj.LeadType + "'");
                if (leadTypeLower.contains("hot")) {
                    cardView.setCardBackgroundColor(Color.parseColor("#FFCDD2")); // Light Red
                } else if (leadTypeLower.contains("warm")) {
                    cardView.setCardBackgroundColor(Color.parseColor("#FFF9C4")); // Light Yellow
                } else if (leadTypeLower.contains("cold")) {
                    cardView.setCardBackgroundColor(Color.parseColor("#C8E6C9")); // Light Green
                } else {
                    cardView.setCardBackgroundColor(Color.WHITE);
                }
            } else {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            final String phoneNumber = obj.CustomerMob != null ? obj.CustomerMob.trim() : "";

            buttonCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phoneNumber.isEmpty()) {
                        Toast.makeText(v.getContext(), "Phone number not available", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        if (v.getContext() instanceof Activity) {
                            CommonUtility.connectCAll((Activity) v.getContext(), phoneNumber);
                        }
                    } catch (SecurityException ex) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                        v.getContext().startActivity(dialIntent);
                    }
                }
            });

            buttonWhatsApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phoneNumber.isEmpty()) {
                        Toast.makeText(v.getContext(), "Phone number not available", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if WhatsApp is installed on the device
                    boolean isWhatsAppInstalled;
                    try {
                        v.getContext().getPackageManager()
                                .getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        isWhatsAppInstalled = true;
                    } catch (PackageManager.NameNotFoundException e) {
                        isWhatsAppInstalled = false;
                    }

                    if (!isWhatsAppInstalled) {
                        Toast.makeText(v.getContext(), "WhatsApp is not installed on this device.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // WhatsApp is installed — open chat directly (WhatsApp will handle unregistered numbers)
                    String cleanedPhone = phoneNumber.replaceAll("[^0-9]", "");
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://wa.me/" + cleanedPhone));
                    intent.setPackage("com.whatsapp");
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
