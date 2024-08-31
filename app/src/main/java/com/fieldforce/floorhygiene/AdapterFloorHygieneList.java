package com.fieldforce.floorhygiene;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.floorhygiene.models.ModelStoreData;
import com.fieldforce.harmonkardonff.R;
import com.ariston.training_module.utility.ItemDecorationAlbumColumns;
import com.fieldforce.utility.widgets.RobotoTextView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterFloorHygieneList extends RecyclerView.Adapter<AdapterFloorHygieneList.ViewHolder> {
    private List<ModelStoreData> listdata;

    // RecyclerView recyclerView;
   public AdapterFloorHygieneList(List<ModelStoreData> listdata) {
        this.listdata = listdata;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_hygiene_store_list, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ModelStoreData myListData = listdata.get(position);
        viewHolder.textHead.setText(myListData.storeName);
        SimpleDateFormat dateFormat=new SimpleDateFormat( "MM/dd/yyyy hh:mm:ss a");
        SimpleDateFormat convertFormat=new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date=dateFormat.parse(myListData.dateFor);
            String Cdate=convertFormat.format(date);
            viewHolder.textDate.setText(Cdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.tvRating.setText(String.format("Rating : %s", myListData.getRating()));
        viewHolder.tvstatus.setText(String.format("Status : %s", myListData.getStatus()));
        viewHolder.rvFloorHygiene.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        viewHolder.rvFloorHygiene.addItemDecoration(new ItemDecorationAlbumColumns(32, 3));
        AdapterFloorHygieneData adapterFloorHygieneData = new AdapterFloorHygieneData();
        viewHolder.rvFloorHygiene.setAdapter(adapterFloorHygieneData);
         adapterFloorHygieneData.addData(myListData.filePath);
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RobotoTextView textHead;
        RobotoTextView textDate;
        RobotoTextView tvRating,tvstatus;

        RecyclerView rvFloorHygiene;

        ViewHolder(View itemView) {
            super(itemView);

            this.textHead = (RobotoTextView) itemView.findViewById(R.id.txtHead);
            this.textDate = (RobotoTextView) itemView.findViewById(R.id.txtDate);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvstatus = itemView.findViewById(R.id.txt_status);
            rvFloorHygiene = itemView.findViewById(R.id.rv_floor_hygiene_images);

        }
    }


}

