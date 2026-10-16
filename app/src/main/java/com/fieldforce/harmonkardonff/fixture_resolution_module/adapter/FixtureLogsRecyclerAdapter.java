package com.fieldforce.harmonkardonff.fixture_resolution_module.adapter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.fixture_resolution_module.models.FixtureLogItem;

import java.util.ArrayList;
import java.util.List;

public class FixtureLogsRecyclerAdapter extends RecyclerView.Adapter<FixtureLogsRecyclerAdapter.LogViewHolder> {

    public interface OnCloseClickListener {
        void onCloseComplaint(FixtureLogItem item);
    }

    private final OnCloseClickListener closeListener;
    private List<FixtureLogItem> items = new ArrayList<>();

    public FixtureLogsRecyclerAdapter(OnCloseClickListener closeListener) {
        this.closeListener = closeListener;
    }

    public void setItems(List<FixtureLogItem> list) {
        items = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fixture_log, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvStoreId;
        private final TextView tvAgingDate;
        private final TextView tvCloseDate;
        private final TextView tvFixtureType;
        private final TextView tvIssueType;
        private final TextView tvRemark;
        private final TextView tvStatus;
        private final View photoRow;
        private final ImageView ivPhoto;
        private final Button btnClose;

        LogViewHolder(@NonNull View itemView) {
            super(itemView);
            photoRow = itemView.findViewById(R.id.ll_log_photo_row);
            tvStoreId = itemView.findViewById(R.id.tv_log_store_id);
            tvAgingDate = itemView.findViewById(R.id.tv_log_aging_date);
            tvCloseDate = itemView.findViewById(R.id.tv_log_close_date);
            tvFixtureType = itemView.findViewById(R.id.tv_log_fixture_type);
            tvIssueType = itemView.findViewById(R.id.tv_log_issue_type);
            tvRemark = itemView.findViewById(R.id.tv_log_remark);
            tvStatus = itemView.findViewById(R.id.tv_log_status);
            ivPhoto = itemView.findViewById(R.id.iv_log_photo);
            btnClose = itemView.findViewById(R.id.btn_log_close);
        }

        void bind(final FixtureLogItem item) {
            tvStoreId.setText(String.valueOf(item.storeId));
            tvAgingDate.setText(item.issueAgingDate);
            tvCloseDate.setText(TextUtils.isEmpty(item.issueCloseDate) ? "—" : item.issueCloseDate);
            tvFixtureType.setText(item.fixtureType);
            tvIssueType.setText(item.issueType);
            tvRemark.setText(item.remark);
            tvStatus.setText(item.status);

            String photoUrl = item.photoUrl != null ? item.photoUrl.trim() : "";
            if (!TextUtils.isEmpty(photoUrl) && !"null".equalsIgnoreCase(photoUrl)) {
                photoRow.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(photoUrl)
                        .into(ivPhoto);
                final String openUrl = photoUrl;
                ivPhoto.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(openUrl));
                    itemView.getContext().startActivity(intent);
                });
            } else {
                photoRow.setVisibility(View.GONE);
                Glide.with(itemView.getContext()).clear(ivPhoto);
                ivPhoto.setImageDrawable(null);
                ivPhoto.setOnClickListener(null);
            }

            if (item.isOpen()) {
                btnClose.setVisibility(View.VISIBLE);
                btnClose.setOnClickListener(v -> {
                    if (closeListener != null) {
                        closeListener.onCloseComplaint(item);
                    }
                });
            } else {
                btnClose.setVisibility(View.GONE);
                btnClose.setOnClickListener(null);
            }
        }
    }
}
