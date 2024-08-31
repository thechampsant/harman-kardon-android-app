package com.ariston.training_module.modules.training_module.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ariston.training_module.R;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.List;

public class WrongQuestionAdpter extends RecyclerView.Adapter<WrongQuestionAdpter.ViewHolder> {
    List<String> items;
    boolean flag=false;

    @NonNull
    @Override
    public WrongQuestionAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WrongQuestionAdpter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wrong_question_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WrongQuestionAdpter.ViewHolder viewHolder, int i) {

        String pos= String.valueOf(i+1);
        viewHolder.question_text.setText(pos+": "+items.get(i));

    }



    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void addData(List<String> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RobotoTextView question_text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question_text = itemView.findViewById(R.id.tv_question);

        }
    }

}

