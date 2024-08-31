package com.ariston.training_module.modules.training_module.adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.models.faq_hep.FaqresponseModel;

import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.List;

public class FaqAdpter  extends RecyclerView.Adapter<FaqAdpter.ViewHolder> {
    List<FaqresponseModel> items;
    boolean flag=false;

    @NonNull
    @Override
    public FaqAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FaqAdpter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.faqlist_ques_ans, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FaqAdpter.ViewHolder viewHolder, int i) {
        FaqresponseModel trMatItem = items.get(i);
        viewHolder.question_text.setText(trMatItem.getQuestion() != null ? trMatItem.getQuestion() : "");
        viewHolder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flag)
                {
                    flag=true;
                    viewHolder.ans_txt.setVisibility(View.VISIBLE);
                    viewHolder.ans_txt.setText(trMatItem.getAnswer() != null ? trMatItem.getAnswer() : "");
                    viewHolder.ivItem.setImageResource(R.drawable.minus);

                }
                else {
                    flag=false;
                    viewHolder.ans_txt.setVisibility(View.GONE);
                    viewHolder.ivItem.setImageResource(R.drawable.plus);

                }


            }
        });
       // Glide.with(viewHolder.itemView.getContext()).load(getThumbnail(trMatItem.getFileType())).into(viewHolder.ivItem);
    }



    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void addData(List<FaqresponseModel> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItem;
        RobotoTextView question_text,ans_txt;
        CardView question;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.iv_add);
            question_text = itemView.findViewById(R.id.question_text);
            ans_txt = itemView.findViewById(R.id.ans_txt);
            question = itemView.findViewById(R.id.question);

        }
    }

}

