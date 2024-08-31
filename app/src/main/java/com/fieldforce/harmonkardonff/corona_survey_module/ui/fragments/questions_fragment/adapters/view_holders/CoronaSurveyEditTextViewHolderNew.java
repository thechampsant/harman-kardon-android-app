package com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.questions_fragment.adapters.view_holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.question_response.CoronaSurveyQuestionResponseData;
import com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.questions_fragment.CoronaNewQuestionnaireFragment;

import mob.field.harmonkardonff.fragments.CoronasurveyQuestionnaire;

public class CoronaSurveyEditTextViewHolderNew extends RecyclerView.ViewHolder
{
    private static final String TAG = "CoronaSurveyEditTextVie";
    private TextView textViewQuestionValue;
    private EditText editTextAnswerContainer;
    private CoronaSurveyQuestionResponseData model;

    public CoronaSurveyEditTextViewHolderNew(@NonNull View itemView)
    {
        super(itemView);
        textViewQuestionValue = itemView.findViewById(R.id.tv_questionValueInEditTextViewType);
        editTextAnswerContainer = itemView.findViewById(R.id.et_answerValueInsideEditTextViewType);
        editTextAnswerContainer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    public void bindValues(CoronaSurveyQuestionResponseData model){
        try{
            this.model = model;
            textViewQuestionValue.setText("Q"+(getAdapterPosition()+1)+" : "+model.getQuestion());
            setListener();
        }
        catch (Exception exception){
            Log.d(TAG, "bindValues: "+exception.getMessage());
            Toast.makeText(textViewQuestionValue.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setListener(){
        editTextAnswerContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                CoronaNewQuestionnaireFragment.mHap.put(model.getQID(),s.toString());
                Log.d(TAG, "afterTextChanged: Position : "+getAdapterPosition()+" : "+CoronasurveyQuestionnaire.mHap);
            }
        });
    }


}
