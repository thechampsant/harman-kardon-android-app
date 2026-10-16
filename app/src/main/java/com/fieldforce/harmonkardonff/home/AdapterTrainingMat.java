package com.fieldforce.harmonkardonff.home;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.home.service.Banner;
import com.bumptech.glide.Glide;
import com.fieldforce.model.GetDoctypedata;

import java.util.List;

public class AdapterTrainingMat extends RecyclerView.Adapter<AdapterTrainingMat.ViewHolder> {

    public interface ConfiguredDocOpenListener {
        void onOpenDoc(Intent intent);
    }

    List<GetDoctypedata> items;
    RelativeLayout lv_view;
    boolean isClicked = false;
    private ConfiguredDocOpenListener configuredDocOpenListener;

    public void setConfiguredDocOpenListener(ConfiguredDocOpenListener listener) {
        this.configuredDocOpenListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_rv_tr_desc, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GetDoctypedata trMatItem = items.get(i);
        String fileName = trMatItem.DocUrl.substring(trMatItem.DocUrl.lastIndexOf('/') + 1);
        viewHolder.tvLabel.setText(fileName != null ? fileName : "");
        Glide.with(viewHolder.itemView.getContext()).load(getThumbnail(trMatItem.DocType)).into(viewHolder.ivItem);
        if (trMatItem.IsSeen != null && trMatItem.IsSeen.equalsIgnoreCase("true")) {
            viewHolder.iv_seen.setVisibility(View.VISIBLE);
        } else
            viewHolder.iv_seen.setVisibility(View.GONE);

        viewHolder.itemView.setOnClickListener(v -> {
            if (isClicked) {
                return;
            }
            isClicked = true;
            GetDoctypedata doc = items.get(viewHolder.getAdapterPosition());
            Intent launchIntent;
            if (mediaType(doc.DocType) == MEDIATYPE.MP4MP3) {
                launchIntent = new Intent(viewHolder.itemView.getContext(), VideoPlayerActivity.class);
                launchIntent.putExtra(TrainingConstants.MEDIA_URL, doc.DocUrl);
                launchIntent.putExtra(TrainingConstants.MAT_ID, doc.DocID);
            } else {
                launchIntent = new Intent(viewHolder.itemView.getContext(), DocViewerActivity.class);
                launchIntent.putExtra(TrainingConstants.DOC_URL, doc.DocUrl);
                launchIntent.putExtra(TrainingConstants.MAT_ID, doc.DocID);
                if (mediaType(doc.DocType) != MEDIATYPE.DOC) {
                    launchIntent.putExtra("is_image", true);
                }
            }
            if (configuredDocOpenListener != null) {
                configuredDocOpenListener.onOpenDoc(launchIntent);
            } else {
                viewHolder.itemView.getContext().startActivity(launchIntent);
            }
            isClicked = false;
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
                return MEDIATYPE.DOC;
            case "mp3":
            case "mp4":
            case ".mp3":
            case ".mp4":
                return MEDIATYPE.MP4MP3;
            default:
                return MEDIATYPE.PNG_JPG;
        }


    }

    private int getThumbnail(String fileType) {
        int id = 0;
        switch (fileType.toLowerCase()) {
            case "doc":
            case ".doc":
                id = com.ariston.training_module.R.drawable.word_thumb;
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
            case "jpeg":
            case "png":
            case ".jpg":
            case ".jpeg":
            case ".png":
                id = R.drawable.jpg_thumbnail;
                break;
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
        TextView tvLabel;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.iv_rv_tr_dsc);
            iv_seen = itemView.findViewById(R.id.iv_seen);
            tvLabel = itemView.findViewById(R.id.tv__label_tr_desc);

        }
    }



    static class MEDIATYPE {
        public static final int MP4MP3 = 2;
        public static final int PNG_JPG = 3;
        public static final int DOC = 1;

    }
}

