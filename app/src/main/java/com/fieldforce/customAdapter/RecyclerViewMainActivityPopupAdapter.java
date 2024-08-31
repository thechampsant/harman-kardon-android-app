package com.fieldforce.customAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fieldforce.entities.MainActivityPopupResponse;
import com.fieldforce.harmonkardonff.R;


import linq.ArrayList;

public class RecyclerViewMainActivityPopupAdapter extends RecyclerView.Adapter<RecyclerViewMainActivityPopupAdapter.MyPopupViewHolder> {

    private static final String TAG = "RecyclerViewMainActivO";
    private ArrayList<MainActivityPopupResponse> responseArrayList = new ArrayList<>();
    private OnPopupReadButtonClickListener onPopupReadButtonClickListener;
    public RecyclerViewMainActivityPopupAdapter(OnPopupReadButtonClickListener onPopupReadButtonClickListener){
        this.onPopupReadButtonClickListener = onPopupReadButtonClickListener;
    }
    @NonNull
    @Override
    public MyPopupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_popup_row,viewGroup,false);
        return new MyPopupViewHolder(view,onPopupReadButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPopupViewHolder myPopupViewHolder, int i) {
        myPopupViewHolder.populateViews(responseArrayList.get(i));
    }

    @Override
    public int getItemCount() {
        if (responseArrayList!=null && responseArrayList.size()>0){
            Log.d(TAG, "getItemCount: "+responseArrayList);
            return responseArrayList.size();
        }
        else {
            return 0;
        }
    }

    public void setData(ArrayList<MainActivityPopupResponse> list){
        responseArrayList = list;
        notifyDataSetChanged();
    }

    class MyPopupViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private Button button;
        private OnPopupReadButtonClickListener onPopupReadButtonClickListener;
        private View view;
        public MyPopupViewHolder(@NonNull View itemView,OnPopupReadButtonClickListener onPopupReadButtonClickListener) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.iv_popupImg);
            button = itemView.findViewById(R.id.btn_popupRead);
            this.onPopupReadButtonClickListener = onPopupReadButtonClickListener;
        }

        public void populateViews(final MainActivityPopupResponse datum){
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
            Glide.with(imageView.getContext()).setDefaultRequestOptions(requestOptions).load(datum.DocURL).into(imageView);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPopupReadButtonClickListener.submitPopupStatusToServer(datum.ScheamId,"true");
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPopupReadButtonClickListener.submitPopupStatusToServer(datum.ScheamId,"true");
                }
            });
        }
    }
}
