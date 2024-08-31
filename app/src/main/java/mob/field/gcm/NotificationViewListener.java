package mob.field.gcm;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.jmedeisis.draglinearlayout.DragLinearLayout;

/**
 * Created by Akshay on 6/27/2018.
 */

public class NotificationViewListener implements View.OnClickListener, DragLinearLayout.OnViewSwapListener {
    View view;
    Context context;

    public NotificationViewListener(View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onSwap(View v, int i, View view1, int i1) {
        Toast.makeText(context,"workign",Toast.LENGTH_SHORT).show();
        if (view != null) {
            view.setVisibility(View.GONE);
/*                    view.animate()
                            .alpha(0.0f)
                            .setDuration(400);*/
        }
    }

    @Override
    public void onClick(View v) {
        view.setVisibility(View.GONE);
        if (notificationRead != null) notificationRead.okButtonClick();
 /*           view.animate()
                    .alpha(0.0f)
                    .setDuration(400);*/
    }


    public interface NotificationRead {
        public void okButtonClick();
    }

    NotificationRead notificationRead;

    public void setListener(NotificationRead notificationRead) {
        this.notificationRead = notificationRead;
    }

}