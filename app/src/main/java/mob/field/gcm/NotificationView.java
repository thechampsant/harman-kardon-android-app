package mob.field.gcm;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.customAdapter.NotificationSeenAdpter;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.model.SeenNotificationResonse;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.model.Response;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.MNotification;
import mob.field.harmonkardonff.services.WebService;

public class NotificationView extends Activity {

    NotificationSeenAdpter notificationSeenAdpter;
    private RecyclerView rv_notification_list;
    public static ArrayList<MNotification> data = new ArrayList<MNotification>();
    WebService web = new WebService();
    RelativeLayout iv_backView;
    public static Boolean Loaded_local = false;
    CardView cv_noDataContainer;
    RobotoTextView rtv_errorMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        InFlateListView();
        loadServerData();
        iv_backView.setOnClickListener(view -> onBackPressed());

    }

    public void loadServerData() {
        if (isNetworkAvailable()) {

            BackgroundProcess backgroundProcess = new BackgroundProcess(NotificationView.this);
            backgroundProcess.setbackgroundProcess(new IProcess() {
                @Override
                public Object underProcess() throws Exception {
                    return web.getSeenNotification();

                }

                @Override
                public void processResponse(Object response) throws Exception {
                    Response res = (Response) response;
                    if (res.status.equals("true")) {
                        ArrayList<SeenNotificationResonse> arrDocModel = res.data;
                        if (arrDocModel != null && arrDocModel.size() > 0) {
                            rv_notification_list.setVisibility(View.VISIBLE);
                            notificationSeenAdpter.setNotificationdata(arrDocModel);

                        } else {
                            rv_notification_list.setVisibility(View.GONE);

                        }


                    } else
                        cv_noDataContainer.setVisibility(View.VISIBLE);


                }
            });
            backgroundProcess.execute(null, null, null);

        } else {
            cv_noDataContainer.setVisibility(View.VISIBLE);
            rtv_errorMessage.setText("No Internet Connection!!");
        }

    }

    public boolean isNetworkAvailable() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null)
            isConnected = activeNetworkInfo.isConnected();
        return activeNetworkInfo != null && isConnected;
    }


    public void InFlateListView() {
        iv_backView = findViewById(com.ariston.training_module.R.id.iv_backView);
        cv_noDataContainer = findViewById(com.ariston.training_module.R.id.cv_noDataContainer);
        rtv_errorMessage = findViewById(com.ariston.training_module.R.id.rtv_errorMessage);
        rv_notification_list = (RecyclerView) findViewById(R.id.rv_notification_list);
        notificationSeenAdpter = new NotificationSeenAdpter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_notification_list.setLayoutManager(linearLayoutManager);
        rv_notification_list.setAdapter(notificationSeenAdpter);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }
}
