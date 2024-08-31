package com.ariston.training_module.modules.training_module.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.models.trainer_quize.TrainerQuizeResonse;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.List;

public class TrainerAdpterQuestion extends RecyclerView.Adapter<TrainerAdpterQuestion.ViewHolder> {
    List<TrainerQuizeResonse> items;
    public int isSelected = -1;
    private RecyclerView rv_quetionanslist;
    private TrainerAdpterQuestAnswerList adapter;
    @NonNull
    @Override
    public TrainerAdpterQuestion.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TrainerAdpterQuestion.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_answer, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerAdpterQuestion.ViewHolder viewHolder, int i) {
        TrainerQuizeResonse Option = items.get(i);
        viewHolder.answer.setText(Option.getQuestionName() != null ? Option.getQuestionName() : "");

        adapter = new TrainerAdpterQuestAnswerList();
        viewHolder.rv_quetionanslist_tr.setAdapter(adapter);
        viewHolder.rv_quetionanslist_tr.setLayoutManager(new LinearLayoutManager(context));
        adapter.addData(Option.getAnswerTamplate(),context);
    }


    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public Context context;

    public void addData(List<TrainerQuizeResonse> data, Context activity) {
        this.items = data;
        this.context = activity;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RobotoTextView answer;
        RecyclerView rv_quetionanslist_tr;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.question);
            rv_quetionanslist_tr = itemView.findViewById(R.id.rv_quetionanslist_tr);

        }
    }


}

