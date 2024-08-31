package com.suveyform.ui.fragments.questions_fragment.adapters.view_holders;


import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SimpleCameraActivity;
import com.suveyform.ImageResponse;
import com.suveyform.models.questionaire.question_response.CoronaSurveyQuestionResponseData;
import com.suveyform.ui.fragments.questions_fragment.CoronaNewQuestionnaireFragment;


public class CoronaSurveyEditTextViewHolderNew extends RecyclerView.ViewHolder
{
    private static final String TAG = "CoronaSurveyEditTextVie";
    private TextView textViewQuestionValue;
    private EditText editTextAnswerContainer;
    private Button btnClick;
    private CoronaSurveyQuestionResponseData model;

    public CoronaSurveyEditTextViewHolderNew(@NonNull View itemView)
    {
        super(itemView);
        textViewQuestionValue = itemView.findViewById(R.id.tv_questionValueInEditTextViewType);
        editTextAnswerContainer = itemView.findViewById(R.id.et_answerValueInsideEditTextViewType);
        btnClick = itemView.findViewById(R.id.btn_click);
       // editTextAnswerContainer.setInputType(InputType.TYPE_CLASS_TEXT);
       // editTextAnswerContainer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

  String SurveyDdate;

    public void bindValues(CoronaSurveyQuestionResponseData model, String surveyDate){

        try{
            this.model = model;
            SurveyDdate=surveyDate;
            textViewQuestionValue.setText("Q"+(getAdapterPosition()+1)+" : "+model.getQuestion());
            if(model.getImage().equalsIgnoreCase("True"))
            {
                ImageResponse imageResponse=new ImageResponse(Integer.parseInt(model.getQID()),"");
                CoronaNewQuestionnaireFragment.imageResponses.add(imageResponse);
                btnClick.setVisibility(View.VISIBLE);
            }

            if(model.getOption().equalsIgnoreCase("textCN"))
                editTextAnswerContainer.setInputType(InputType.TYPE_CLASS_TEXT);
            else if(model.getOption().equalsIgnoreCase("textC"))
                editTextAnswerContainer.setInputType(InputType.TYPE_CLASS_TEXT);
            else
                editTextAnswerContainer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);



            setListener();
        }
        catch (Exception exception){
            Log.d(TAG, "bindValues: "+exception.getMessage());
            Toast.makeText(textViewQuestionValue.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void setListener(){
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent I = new Intent(textViewQuestionValue.getContext(), SimpleCameraActivity.class);
                I.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "Vac");

                I.putExtra(SimpleCameraActivity.PARAMS_GUID, "");
                // I.putExtra(ImageActivity.PARAMS_MODEL_ID, "");
                I.putExtra(SimpleCameraActivity.ACTION_ENABLE_GPS, true);
                I.putExtra("QuestionId", model.getQID());
                I.putExtra("AnswerId", editTextAnswerContainer.getText().toString());
                I.putExtra("Date", SurveyDdate);
                // For getting the result in the parent activity
                // this.context.startActivityForResult(I, 1);

                // for getting the result in the fragment
                Activity x = (Activity) textViewQuestionValue.getContext();
                x.startActivityForResult(I, 15);
            }
        });
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
            }
        });
    }


}
