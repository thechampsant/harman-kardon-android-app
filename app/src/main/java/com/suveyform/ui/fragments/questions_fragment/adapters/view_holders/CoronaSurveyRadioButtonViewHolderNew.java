package com.suveyform.ui.fragments.questions_fragment.adapters.view_holders;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SimpleCameraActivity;
import com.suveyform.ImageResponse;
import com.suveyform.models.questionaire.question_response.CoronaSurveyQuestionResponseData;
import com.suveyform.ui.fragments.questions_fragment.CoronaNewQuestionnaireFragment;

import java.util.Random;

public class CoronaSurveyRadioButtonViewHolderNew extends RecyclerView.ViewHolder
{
    private static final String TAG = "CoronaSurveyRadioButton";

    private TextView textViewQuestionValue;
    private RadioGroup radioGroup;
    private Button btnClick;
    private CoronaSurveyQuestionResponseData model;
    private View view;

    public CoronaSurveyRadioButtonViewHolderNew(@NonNull View itemView)
    {
        super(itemView);
        view = itemView;
        textViewQuestionValue = itemView.findViewById(R.id.tv_questionValueInRadioButtonViewType);
        radioGroup = itemView.findViewById(R.id.radioGroup_insideCoronaSurvey);
        btnClick = itemView.findViewById(R.id.btn_click);
    }
    String SurveyDAte;

    public void bindValues(CoronaSurveyQuestionResponseData model, String surveyDate){
        Log.e("Date111",surveyDate);
        SurveyDAte=surveyDate;
        try {
            if(model.getImage().equalsIgnoreCase("True"))
            {
                ImageResponse imageResponse=new ImageResponse(Integer.parseInt(model.getQID()),"");
                CoronaNewQuestionnaireFragment.imageResponses.add(imageResponse);
                btnClick.setVisibility(View.VISIBLE);
            }

            textViewQuestionValue.setText("Q"+(getAdapterPosition()+1)+" : "+model.getQuestion());
            this.model = model;

            createRadioButton(model);
            setListener();
        }
        catch (Exception exception){
            Log.d(TAG, "bindValues: "+exception.getMessage());
            Toast.makeText(textViewQuestionValue.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void createRadioButton(CoronaSurveyQuestionResponseData model)
    {
        try {
            String[] values = model.getOption().split(",");
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
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(textViewQuestionValue.getContext(),"ged", Toast.LENGTH_LONG).show();
                Intent I = new Intent(textViewQuestionValue.getContext(), SimpleCameraActivity.class);
                I.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "Vac");

                I.putExtra(SimpleCameraActivity.PARAMS_GUID, "");
                I.putExtra("Date", SurveyDAte);
                I.putExtra(SimpleCameraActivity.ACTION_ENABLE_GPS, true);
                I.putExtra("QuestionId", model.getQID());
                I.putExtra("AnswerId", "");
                // I.putExtra(ImageActivity.ACTION_ENABLE_GPS, true);
                // For getting the result in the parent activity
                // this.context.startActivityForResult(I, 1);

                // for getting the result in the fragment
                Activity x = (Activity) textViewQuestionValue.getContext();
                x.startActivityForResult(I, 15);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton rb=(RadioButton)view.findViewById(checkedId);
                CoronaNewQuestionnaireFragment.mHap.put(model.getQID(),rb.getText().toString());
            }
        });
    }
}
