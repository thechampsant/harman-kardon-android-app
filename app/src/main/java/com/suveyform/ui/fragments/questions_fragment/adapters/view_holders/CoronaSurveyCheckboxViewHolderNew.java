package com.suveyform.ui.fragments.questions_fragment.adapters.view_holders;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SimpleCameraActivity;
import com.suveyform.ImageResponse;
import com.suveyform.models.questionaire.question_response.CoronaSurveyQuestionResponseData;
import com.suveyform.ui.fragments.questions_fragment.CoronaNewQuestionnaireFragment;

public class CoronaSurveyCheckboxViewHolderNew extends RecyclerView.ViewHolder {

    private static final String TAG = "CoronaSurveyCheckboxVie";
    private TextView textViewQuestion;
    private CheckBox checkBox;
    private Button btnClick;
    private CoronaSurveyQuestionResponseData model;

    public CoronaSurveyCheckboxViewHolderNew(@NonNull View itemView) {
        super(itemView);
        textViewQuestion = itemView.findViewById(R.id.tv_questionValueInCheckboxViewType);
        checkBox = itemView.findViewById(R.id.cb_inCheckboxViewType);
        btnClick = itemView.findViewById(R.id.btn_click);
    }
    String SurveyDate;
    public void bindItemsInCheckboxViewHolder(final CoronaSurveyQuestionResponseData model, String surveyDate)
    {
        this.model = model;
        SurveyDate=surveyDate;
        textViewQuestion.setText(model.getQuestion());
        if(model.getImage().equalsIgnoreCase("True"))
        {
            ImageResponse imageResponse=new ImageResponse(Integer.parseInt(model.getQID()),"");
            CoronaNewQuestionnaireFragment.imageResponses.add(imageResponse);
            btnClick.setVisibility(View.VISIBLE);
        }

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent I = new Intent(textViewQuestion.getContext(), SimpleCameraActivity.class);
                I.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "Vac");

                I.putExtra(SimpleCameraActivity.PARAMS_GUID, "");
                 I.putExtra("Date", SurveyDate);
                I.putExtra(SimpleCameraActivity.ACTION_ENABLE_GPS, true);
                I.putExtra("QuestionId", model.getQID());
                I.putExtra("AnswerId","");
                // I.putExtra(ImageActivity.ACTION_ENABLE_GPS, true);
                // For getting the result in the parent activity
                // this.context.startActivityForResult(I, 1);

                // for getting the result in the fragment
                Activity x = (Activity) textViewQuestion.getContext();
                x.startActivityForResult(I, 15);
            }
        });

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
