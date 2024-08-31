package com.fieldforce.customAdapter.corona_adapters.questionnaire.ViewHolders;

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

import mob.field.harmonkardonff.entitiymodels.CoronaSurveyRequestModel;
import mob.field.harmonkardonff.fragments.CoronasurveyQuestionnaire;

public class CoronaSurveyEditTextViewHolder extends RecyclerView.ViewHolder
{
    private static final String TAG = "CoronaSurveyEditTextVie";
    private TextView textViewQuestionValue;
    private EditText editTextAnswerContainer;
    private CoronaSurveyRequestModel model;

    public CoronaSurveyEditTextViewHolder(@NonNull View itemView)
    {
        super(itemView);
        textViewQuestionValue = itemView.findViewById(R.id.tv_questionValueInEditTextViewType);
        editTextAnswerContainer = itemView.findViewById(R.id.et_answerValueInsideEditTextViewType);
        editTextAnswerContainer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    public void bindValues(CoronaSurveyRequestModel model){
        try{
            this.model = model;
            textViewQuestionValue.setText("Q"+(getAdapterPosition()+1)+" : "+model.Question);
            if (model.SetError.equalsIgnoreCase("")){

            }
            else {
                editTextAnswerContainer.setError(model.SetError);
            }
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
                CoronasurveyQuestionnaire.mHap.put(model.QID,s.toString());
                Log.d(TAG, "afterTextChanged: Position : "+getAdapterPosition()+" : "+CoronasurveyQuestionnaire.mHap);
            }
        });
    }


}
