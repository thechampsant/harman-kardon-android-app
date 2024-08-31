package com.fieldforce.customAdapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;

import com.fieldforce.model.SeenNotificationResonse;
import com.ariston.training_module.utility.widgets.RobotoBoldTextView;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.List;

public class NotificationSeenAdpter extends RecyclerView.Adapter<NotificationSeenAdpter.ViewHolder> {
    private List<SeenNotificationResonse> items;



    @NonNull
    @Override
    public NotificationSeenAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NotificationSeenAdpter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seen_notification_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationSeenAdpter.ViewHolder viewHolder, int i) {
        SeenNotificationResonse obj = items.get(i);
        viewHolder.tvNotificationText.setText(obj.Notification);
        viewHolder.tvTitle.setText(obj.NotificationType);
        viewHolder.tv_notification_date.setText(obj.NotificationAt);


    }


    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setNotificationdata(List<SeenNotificationResonse> historyModalList) {
        this.items = historyModalList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RobotoTextView tvNotificationText;
        RobotoTextView tvTitle;
        RobotoTextView tv_notification_date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationText = itemView.findViewById(R.id.tv_notification_text);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tv_notification_date = itemView.findViewById(R.id.tv_notification_date);

        }
    }



 
   

}

