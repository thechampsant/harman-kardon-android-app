package com.fieldforce.customAdapter.corona_adapters.history;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.fieldforce.harmonkardonff.R;

import java.util.List;

import mob.field.harmonkardonff.entitiymodels.CoronaHistoryModal;

public class CoronaHistoryRecyclerAdapter extends RecyclerView.Adapter<CoronaHistoryRecyclerAdapter.CoronaHistoryViewHolder> {
    private List<CoronaHistoryModal> historyModalList;
    @NonNull
    @Override
    public CoronaHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.corona_history_item,viewGroup,false);
        return new CoronaHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoronaHistoryViewHolder coronaHistoryViewHolder, int position) {
        coronaHistoryViewHolder.bindItems(historyModalList.get(position));
    }

    @Override
    public int getItemCount() {
        if (historyModalList!=null){
            return historyModalList.size()>0?historyModalList.size():0;
        }
        else {
            return 0;
        }
    }

    public void setHistoryData(List<CoronaHistoryModal> historyModalList){
        this.historyModalList = historyModalList;
        notifyDataSetChanged();
    }

    class CoronaHistoryViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewDate;
        private TextView textViewTemp;
        private TextView textViewFever;
        private TextView textViewDiarrhea;
        private TextView textViewCough;
        private TextView textViewRespiratory;
        private TextView textViewSorethroat;
        private TextView Breath;
        public CoronaHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.tv_dateValueInCoronaHistory);
            textViewTemp = itemView.findViewById(R.id.tv_tempValueInCoronaHistory);
            textViewFever = itemView.findViewById(R.id.tv_feverValueInCoronaHistory);
            textViewDiarrhea = itemView.findViewById(R.id.tv_diarrheaValueInCoronaHistory);
            textViewCough = itemView.findViewById(R.id.tv_coughValueInCoronaHistory);
            textViewRespiratory = itemView.findViewById(R.id.tv_respiratoryValueInCoronaHistory);
            textViewSorethroat = itemView.findViewById(R.id.tv_sorethroatInCoronaHistory);
            Breath = itemView.findViewById(R.id.tv_breathInCoronaHistory);
        }

        public void bindItems(CoronaHistoryModal modal){
            textViewDate.setText(modal.Date);
            textViewTemp.setText(modal.Temp);
            textViewFever.setText(modal.Fever);
            textViewDiarrhea.setText(modal.Diarrhea);
            textViewCough.setText(modal.Cough);

            textViewRespiratory.setText(modal.Respiratory);
            textViewSorethroat.setText(modal.Sorethroat);
            Breath.setText(modal.Breath);
        }
    }
}
