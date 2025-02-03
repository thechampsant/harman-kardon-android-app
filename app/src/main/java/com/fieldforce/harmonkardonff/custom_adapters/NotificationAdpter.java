package com.fieldforce.harmonkardonff.custom_adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ariston.training_module.utility.widgets.RobotoTextView;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.model.NotificationResonseMode;
import com.fieldforce.utility.widgets.RobotoBoldTextView;


import java.util.List;


public class NotificationAdpter extends RecyclerView.Adapter<NotificationAdpter.ViewHolder> {
    private List<NotificationResonseMode> items;
    private NotificationAdapterCallbacks notificationAdapterCallbacks;


    @NonNull
    @Override
    public NotificationAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdpter.ViewHolder viewHolder, int i) {
        NotificationResonseMode obj = items.get(i);
        viewHolder.tvNotificationText.setText(obj.Notification);
        viewHolder.tvTitle.setText(obj.NotificationType);
        viewHolder.btIRead.setText(viewHolder.itemView.getContext().getResources().getString(R.string.i_read));

        viewHolder.btIRead.setOnClickListener(view -> {
            notificationAdapterCallbacks.updateSeenData(obj, viewHolder.getAdapterPosition());
        });

    }


    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setNotificationdata(List<NotificationResonseMode> historyModalList) {
        this.items = historyModalList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RobotoTextView tvNotificationText;
        RobotoBoldTextView tvTitle;
        RobotoBoldTextView btIRead;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationText = itemView.findViewById(R.id.tv_notification_text);
            tvTitle = itemView.findViewById(R.id.tv_title);
            btIRead = itemView.findViewById(R.id.btn_i_read);

        }
    }

    public void setCallbacks(NotificationAdapterCallbacks notificationAdapterCallbacks) {
        this.notificationAdapterCallbacks = notificationAdapterCallbacks;
    }

    public interface NotificationAdapterCallbacks {
        public void updateSeenData(NotificationResonseMode obj, int positionToRemove);
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

}

