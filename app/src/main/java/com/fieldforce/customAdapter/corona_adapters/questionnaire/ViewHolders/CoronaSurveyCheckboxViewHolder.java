package com.fieldforce.customAdapter.corona_adapters.questionnaire.ViewHolders;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fieldforce.harmonkardonff.R;

import mob.field.harmonkardonff.entitiymodels.CoronaSurveyRequestModel;
import mob.field.harmonkardonff.fragments.CoronasurveyQuestionnaire;

public class CoronaSurveyCheckboxViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "CoronaSurveyCheckboxVie";
    private TextView textViewQuestion;
    private CheckBox checkBox;

    private CoronaSurveyRequestModel model;

    public CoronaSurveyCheckboxViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewQuestion = itemView.findViewById(R.id.tv_questionValueInCheckboxViewType);
        checkBox = itemView.findViewById(R.id.cb_inCheckboxViewType);
    }
    public void bindItemsInCheckboxViewHolder(final CoronaSurveyRequestModel model)
    {
        this.model = model;
        textViewQuestion.setText(model.Question);
        if (model.SetError.equalsIgnoreCase("")){
            textViewQuestion.setTextColor(Color.parseColor("#000000"));
        }
        else {
            textViewQuestion.setTextColor(Color.parseColor("#F44336"));
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked())
                {
                    CoronasurveyQuestionnaire.mHap.put(model.QID,"true");
                }
                else
                    {
                    CoronasurveyQuestionnaire.mHap.put(model.QID,"false");
                }
            }
        });
    }
}
