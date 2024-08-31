package com.fieldforce.customAdapter.corona_adapters.questionnaire.ViewHolders;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldforce.harmonkardonff.R;

import java.util.Random;

import mob.field.harmonkardonff.entitiymodels.CoronaSurveyRequestModel;
import mob.field.harmonkardonff.fragments.CoronasurveyQuestionnaire;

public class CoronaSurveyRadioButtonViewHolder extends RecyclerView.ViewHolder
{
    private static final String TAG = "CoronaSurveyRadioButton";

    private TextView textViewQuestionValue;
    private RadioGroup radioGroup;

    private CoronaSurveyRequestModel model;
    private View view;

    public CoronaSurveyRadioButtonViewHolder(@NonNull View itemView)
    {
        super(itemView);
        view = itemView;
        textViewQuestionValue = itemView.findViewById(R.id.tv_questionValueInRadioButtonViewType);
        radioGroup = itemView.findViewById(R.id.radioGroup_insideCoronaSurvey);
    }

    public void bindValues(CoronaSurveyRequestModel model){
        try {
            textViewQuestionValue.setText("Q"+(getAdapterPosition()+1)+" : "+model.Question);
            if (model.SetError.equalsIgnoreCase(""))
            {
                textViewQuestionValue.setTextColor(Color.parseColor("#000000"));
            }
            else {
                textViewQuestionValue.setTextColor(Color.parseColor("#F44336"));
                Toast.makeText(textViewQuestionValue.getContext(),"Please select options...",Toast.LENGTH_SHORT).show();
            }
            this.model = model;
            createRadioButton(model);
            setListener();
        }
        catch (Exception exception){
            Log.d(TAG, "bindValues: "+exception.getMessage());
            Toast.makeText(textViewQuestionValue.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void createRadioButton(CoronaSurveyRequestModel model)
    {
        try {
            String[] values = model.Option.split(",");
            for(String str : values)
            {
                RadioButton radioButton = new RadioButton(textViewQuestionValue.getContext());
                radioButton.setText(str);
                radioButton.setId(getRandomNumber());
                radioGroup.addView(radioButton);
            }
        }
        catch (Exception ex){
            Log.d(TAG, "createRadioButton: "+ex.getMessage());
        }
    }

    private int getRandomNumber(){
        Random rand = new Random();
        int rValue = rand.nextInt(1000);
        return rValue;
    }

    private void setListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton rb=(RadioButton)view.findViewById(checkedId);
                CoronasurveyQuestionnaire.mHap.put(model.QID,rb.getText().toString());
                Log.d(TAG, "onCheckedChanged: Position : "+getAdapterPosition()+" -> "+CoronasurveyQuestionnaire.mHap);
            }
        });
    }
}
