package com.fieldforce.harmonkardonff.homeTrainingDoc;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.models.tr_mat_model.TrMatItem;
import com.ariston.training_module.modules.training_module.ui.activities.DocViewerActivity;
import com.ariston.training_module.modules.training_module.ui.activities.TrainingMaterialActivity;
import com.ariston.training_module.modules.training_module.ui.activities.VideoPlayerActivity;
import com.ariston.training_module.utility.Helper;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.widgets.RobotoBoldTextView;
import com.fieldforce.model.GetDoctypedata;

import java.util.List;

public class AdapterTrainingMat extends RecyclerView.Adapter<AdapterTrainingMat.ViewHolder> {
    List<GetDoctypedata> items;
    RelativeLayout lv_view;

    @NonNull
    @Override
    public AdapterTrainingMat.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AdapterTrainingMat.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_rv_tr_desc, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTrainingMat.ViewHolder viewHolder, int i) {
        GetDoctypedata trMatItem = items.get(i);
        String fileName = trMatItem.DocUrl.substring(trMatItem.DocUrl.lastIndexOf('/') + 1);
        viewHolder.tvLabel.setText(fileName != null ? fileName : "");
        Glide.with(viewHolder.itemView.getContext()).load(getThumbnail(trMatItem.DocType)).into(viewHolder.ivItem);
        if(trMatItem.IsSeen.equalsIgnoreCase("true")) {
            viewHolder.iv_seen.setVisibility(View.VISIBLE);
        } else
            viewHolder.iv_seen.setVisibility(View.GONE);
        viewHolder.itemView.setOnClickListener(v -> {
            if (mediaType(items.get(viewHolder.getAdapterPosition()).DocType) == AdapterTrainingMat.MEDIATYPE.MP4MP3) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), VideoPlayerActivity.class);
                intent.putExtra(TrainingConstants.MEDIA_URL, items.get(viewHolder.getAdapterPosition()).DocUrl);
                intent.putExtra(TrainingConstants.MAT_ID, items.get(viewHolder.getAdapterPosition()).DocID);
                ((Activity) viewHolder.itemView.getContext()).startActivity(intent);
            }
            else if (mediaType(items.get(viewHolder.getAdapterPosition()).DocType) == AdapterTrainingMat.MEDIATYPE.DOC) {

                Intent docIntent = new Intent(viewHolder.itemView.getContext(), DocViewerActivity.class);
                docIntent.putExtra(TrainingConstants.DOC_URL, items.get(viewHolder.getAdapterPosition()).DocUrl);
                docIntent.putExtra(TrainingConstants.MAT_ID, items.get(viewHolder.getAdapterPosition()).DocID);
                ((Activity) viewHolder.itemView.getContext()).startActivity(docIntent);
            }
            else {
                Intent docIntent = new Intent(viewHolder.itemView.getContext(), DocViewerActivity.class);
                docIntent.putExtra("is_image", true);
                docIntent.putExtra(TrainingConstants.DOC_URL, items.get(viewHolder.getAdapterPosition()).DocUrl);
                docIntent.putExtra(TrainingConstants.MAT_ID, items.get(viewHolder.getAdapterPosition()).DocID);
                ((Activity) viewHolder.itemView.getContext()).startActivity(docIntent);
            }


//            switchActivityByName(items.get(viewHolder.getAdapterPosition()).getLinkName(), (Activity) viewHolder.itemView.getContext());
        });
    }

    private int mediaType(String fileType) {
        switch (fileType.toLowerCase()) {
            case "doc":
            case "pptx":
            case "pdf":
            case "xlsx":
            case ".doc":
            case ".pptx":
            case ".pdf":
            case ".xlsx":
                return AdapterTrainingMat.MEDIATYPE.DOC;
            case "mp3":
            case "mp4":
            case ".mp3":
            case ".mp4":
                return AdapterTrainingMat.MEDIATYPE.MP4MP3;
            default:
                return AdapterTrainingMat.MEDIATYPE.PNG_JPG;
        }


    }

    private int getThumbnail(String fileType) {
        int id = 0;
        switch (fileType.toLowerCase()) {
            case "doc":
            case ".doc":
                id = R.drawable.word_thumb;
                break;
            case "pptx":
            case ".pptx":
                id = R.drawable.ppt_thumb;
                break;
            case "pdf":
            case ".pdf":
                id = R.drawable.pdf_thumb;
                break;
            case "xlsx":
            case ".xlsx":
                id = R.drawable.excel_thumb;
                break;
            case "mp3":
            case "mp4":
            case ".mp3":
            case ".mp4":
            case "link":
                id = R.drawable.media_thumb;
                break;
            case "jpg":
            case "png":
            case ".jpg":
            case ".png":
                id = R.drawable.jpg_thumbnail;
        }
        return id;
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void addData(List<GetDoctypedata> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItem,iv_seen;
        RobotoBoldTextView tvLabel;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.iv_rv_tr_dsc);
            iv_seen = itemView.findViewById(R.id.iv_seen);
            tvLabel = itemView.findViewById(R.id.tv__label_tr_desc);

        }
    }

    void switchActivityByName(String name, Activity context) {
        switch (name.toLowerCase()) {
            case "material":
                Helper.openNextActivity(context, TrainingMaterialActivity.class, null, false, false, -1);
                break;
            default:
        }

    }

    static class MEDIATYPE {
        public static final int MP4MP3 = 2;
        public static final int PNG_JPG = 3;
        public static final int DOC = 1;

    }
}
