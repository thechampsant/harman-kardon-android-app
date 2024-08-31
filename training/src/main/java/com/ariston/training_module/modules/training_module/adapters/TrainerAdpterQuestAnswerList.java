package com.ariston.training_module.modules.training_module.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.models.trainer_quize.AnswerTamplate;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.List;

public class TrainerAdpterQuestAnswerList extends RecyclerView.Adapter<TrainerAdpterQuestAnswerList.ViewHolder> {
    List<AnswerTamplate> items;
    public int isSelected = -1;

    @NonNull
    @Override
    public TrainerAdpterQuestAnswerList.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TrainerAdpterQuestAnswerList.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_anslist, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerAdpterQuestAnswerList.ViewHolder viewHolder, int i) {
        AnswerTamplate Option = items.get(i);
        viewHolder.answer.setText(Option.getAnswer() != null ? Option.getAnswer() : "");


            if(Option.getValue().equals("1")) {
                viewHolder.ivItem.setImageResource(R.drawable.tickbutton);
                viewHolder.wholeview.setBackground(context.getResources().getDrawable(R.drawable.shape_trainer_quiz_questions));
            }
         else {
            viewHolder.wholeview.setBackground(context.getResources().getDrawable(R.drawable.shape_quiz_questions));
        }
    }


    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public Context context;

    public void addData(List<AnswerTamplate> data, Context activity) {
        this.items = data;
        this.context = activity;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItem;
        RobotoTextView answer;
        RelativeLayout wholeview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.selectans);
            answer = itemView.findViewById(R.id.answer);
            wholeview = itemView.findViewById(R.id.wholeview);

        }
    }

}
