package com.ariston.training_module.modules.training_module.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ariston.training_module.R;
import com.ariston.training_module.modules.dashboard.ui.activities.DashboardActivity;
import com.ariston.training_module.modules.training_module.models.TrainingModel;
import com.ariston.training_module.modules.training_module.models.tr_desc_model.TrainingDescItem;
import com.ariston.training_module.modules.training_module.ui.activities.FaqActivity;
import com.ariston.training_module.modules.training_module.ui.activities.HelpDeskactivity;
import com.ariston.training_module.modules.training_module.ui.activities.QuizActivity;
import com.ariston.training_module.modules.training_module.ui.activities.QuizeTrainerActivity;
import com.ariston.training_module.modules.training_module.ui.activities.TrainingMaterialActivity;
import com.ariston.training_module.utility.Helper;
import com.ariston.training_module.utility.widgets.RobotoBoldTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterRvTrDesc extends RecyclerView.Adapter<AdapterRvTrDesc.ViewHolder> {
    private List<TrainingDescItem> items;
    private String trainingID;
    private boolean Is_traner;
    private TrainingModel recTrainingModel;
    private AdapterRvTrDescCallbacks adapterRvTrDescCallbacks;
    Date StartDate,LastDate,CDate;
    String Time,STime,LTime,Sdate,LDate;
    public interface AdapterRvTrDescCallbacks {
        public boolean canDownloadECertificate();

        public void routePlanClick();
    }

    public void setCallbacks(AdapterRvTrDescCallbacks adapterRvTrDescCallbacks) {
        this.adapterRvTrDescCallbacks = adapterRvTrDescCallbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_rv_tr_desc, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TrainingDescItem trainingDescItem = items.get(i);
        viewHolder.tvLabel.setText(trainingDescItem.getLinkName());
        Glide.with(viewHolder.itemView.getContext()).load(trainingDescItem.getIconPath()).centerCrop().into(viewHolder.ivItem);
        viewHolder.itemView.setOnClickListener(v -> {

            switchActivityByName(items.get(viewHolder.getAdapterPosition()).getLinkName(),
                    (Activity) viewHolder.itemView.getContext(), i);
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void addData(List<TrainingDescItem> data, String trainingID, boolean isTrainer, TrainingModel recTrainingModel) {
        this.items = data;
        this.trainingID = trainingID;
        this.Is_traner = isTrainer;
        this.recTrainingModel = recTrainingModel;
        notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItem;
        RobotoBoldTextView tvLabel;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.iv_rv_tr_dsc);
            tvLabel = itemView.findViewById(R.id.tv__label_tr_desc);

        }
    }

    private void switchActivityByName(String name, Activity context, int index) {
        Time = recTrainingModel.getDuration();
        String[] separated = Time.split("-");
        STime="12:00"; // this will contain "Fruit"
        LTime="02:00";
        Sdate=recTrainingModel.getStartDate()+" " +STime;
        LDate=recTrainingModel.getEndDate()+" " +LTime;

        SimpleDateFormat dateFormat=new SimpleDateFormat( "dd-MMM-yyyy hh:mm");
        try {
            StartDate=dateFormat.parse(Sdate);
            LastDate=dateFormat.parse(LDate);
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
            String Currentdate=df.format(c);
            CDate=dateFormat.parse(Currentdate);
            System.out.println("Currenttime => " + CDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Helper.print(this.recTrainingModel.getTrainingStatus());
        switch (name.toLowerCase()) {

            case "e-certificate":
                if (adapterRvTrDescCallbacks.canDownloadECertificate()) {
                    downloadCertificate(context, items.get(index));
                } else {
                    Toast.makeText(context, "You can download E-Certificate If you had attempted quiz and scored more than equal to 80%", Toast.LENGTH_LONG).show();
                }

                break;
            case "material":
                Helper.openNextActivity(context, TrainingMaterialActivity.class, null, false, false, -1);
                break;
            case "quiz": {
                Log.e("Israiner", String.valueOf(Is_traner) + "null");
                if (Is_traner)
                    Helper.openNextActivity(context, QuizeTrainerActivity.class, null, true, false, 1098);
                else
                {
                    Helper.openNextActivity(context, QuizActivity.class, null, true, false, 1097);
              /*  if(CDate.after(StartDate) && CDate.before(LastDate)) {
                    Helper.openNextActivity(context, QuizActivity.class, null, true, false, 1097);
                }else if(CDate.equals(StartDate) || CDate.equals(LastDate)){
                    Helper.openNextActivity(context, QuizActivity.class, null, true, false, 1097);

                }*/
               /* else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    ViewGroup viewGroup = context.findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_calender_type, viewGroup, false);
                    TextView messge=dialogView.findViewById(R.id.type);
                    Button buttonOk=dialogView.findViewById(R.id.buttonOk);
                    messge.setText("Timelines for submitting is over. Please contact your manager.");
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    buttonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.hide();
                        }
                    });

                    alertDialog.show();
                }*/
                }

            }
            break;
            case "helpdesk":
                Helper.openNextActivity(context, HelpDeskactivity.class, null, false, false, -1);
                break;
            case "faq":
                Helper.openNextActivity(context, FaqActivity.class, null, false, false, -1);
                break;
            case "analytics":
                Helper.openNextActivity(context, DashboardActivity.class, null, false, false, -1);
                break;
            default:
        }

    }

    private void downloadCertificate(Activity context, TrainingDescItem trainingDescItem) {
        String url = "https://aristonfieldforce.infield.co.in/ECertificate/ViewLetter?TraineeId=" + trainingID + "&LoginId=" + trainingDescItem.getLoginId();
        Helper.print(url);
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("E-Certificate");
        request.setDescription("Downloading E-Certificate");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, "training.pdf");
        } else {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Harman.pdf");
        }
        if (downloadmanager != null) {
            try {
                Toast.makeText(context, "E-certificate downloading in your notification bar.", Toast.LENGTH_SHORT).show();
                downloadmanager.enqueue(request);
            } catch (Exception e) {
                Toast.makeText(context, "NetWork Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Network Error. Please try again", Toast.LENGTH_SHORT).show();
        }
        final ProgressDialog progressBarDialog = new ProgressDialog(context);
        progressBarDialog.setTitle("Download Certificate, Please Wait...");
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (dialog, whichButton) -> {
        });
        progressBarDialog.setProgress(0);
        new Thread(() -> {
            boolean downloading = true;
            while (downloading) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(); //filter by id which you have receieved when reqesting download from download manager
                Cursor cursor = null;
                if (downloadmanager != null) {
                    cursor = downloadmanager.query(q);
                }
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                int bytes_downloaded = 0;
                if (cursor != null) {
                    bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                }
                int bytes_total = 0;
                if (cursor != null) {
                    bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                }

                if (cursor != null && cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;
                }
                final int dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);
                context.runOnUiThread(() -> progressBarDialog.setProgress(dl_progress));
                cursor.close();
            }
        }).start();
        progressBarDialog.show();
    }


}
