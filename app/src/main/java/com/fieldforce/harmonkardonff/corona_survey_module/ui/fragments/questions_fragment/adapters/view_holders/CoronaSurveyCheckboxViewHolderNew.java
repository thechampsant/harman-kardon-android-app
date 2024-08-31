package com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.questions_fragment.adapters.view_holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.question_response.CoronaSurveyQuestionResponseData;
import com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.questions_fragment.CoronaNewQuestionnaireFragment;

public class CoronaSurveyCheckboxViewHolderNew extends RecyclerView.ViewHolder {

    private static final String TAG = "CoronaSurveyCheckboxVie";
    private TextView textViewQuestion;
    private CheckBox checkBox;

    private CoronaSurveyQuestionResponseData model;

    public CoronaSurveyCheckboxViewHolderNew(@NonNull View itemView) {
        super(itemView);
        textViewQuestion = itemView.findViewById(R.id.tv_questionValueInCheckboxViewType);
        checkBox = itemView.findViewById(R.id.cb_inCheckboxViewType);
    }
    public void bindItemsInCheckboxViewHolder(final CoronaSurveyQuestionResponseData model)
    {
        this.model = model;
        textViewQuestion.setText(model.getQuestion());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked())
                {
                    CoronaNewQuestionnaireFragment.mHap.put(model.getQID(),"true");
                }
                else
                {
                    CoronaNewQuestionnaireFragment.mHap.put(model.getQID(),"false");
                }
            }
        });
    }
}
