package com.ariston.training_module.modules.training_module.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.models.tr_quize_model.Option;
import com.ariston.training_module.modules.training_module.ui.activities.QuizActivity;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.List;

public class AdpterQuestAnswerList extends RecyclerView.Adapter<AdpterQuestAnswerList.ViewHolder> {
    List<Option> items;
    public int isSelected = -1;

    @NonNull
    @Override
    public AdpterQuestAnswerList.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AdpterQuestAnswerList.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_anslist, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdpterQuestAnswerList.ViewHolder viewHolder, int i) {
        Option Option = items.get(i);
        viewHolder.answer.setText(Option.getOption() != null ? Option.getOption() : "");
        if (isSelected == i) {
            viewHolder.ivItem.setImageResource(R.drawable.tickbutton);
        } else {
            viewHolder.ivItem.setImageResource(R.drawable.untick);
        }

        viewHolder.wholeview.setOnClickListener(view -> {
            //TODO Neve user position directly in on click
            isSelected = viewHolder.getAdapterPosition();
            //TODO Do this via interface
            ((QuizActivity) context).selectAnswer(i);
            notifyDataSetChanged();

        });
        viewHolder.wholeview.setBackground(context.getResources().getDrawable(R.drawable.shape_quiz_questions));
    }


    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public Context context;

    public void addData(List<Option> data, Context activity, int isSelectedpos) {
        isSelected = isSelectedpos;
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
