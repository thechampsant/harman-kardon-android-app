package com.suveyform;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.utility.ColorConstants;
import com.suveyform.models.questionaire.question_response.CoronaSurveyQuestionResponseContainer;
import com.suveyform.utils.Resource;
import com.suveyform.view_models.SurveyFormViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import linq.ArrayList;

public class SurveytypeActivity extends AppCompatActivity {
    private SurveyFormViewModel coronaSurveyViewModel;
    private LinearLayout linearLayoutProgressBarContainer;
    private LinearLayout linearLayoutErrorContainer;
    private RecyclerView recyclerView;
    private TextView textViewSubmit,textViewResponseTitle;
    private TextView textViewSomeThingWentWrong;
    Servay_Type mAdapter;
    static public java.util.ArrayList<String> sarveyList = new java.util.ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveytype);
        linearLayoutProgressBarContainer = (LinearLayout) findViewById(R.id.ll_progressBarContainer);
        linearLayoutErrorContainer = (LinearLayout) findViewById(R.id.ll_errorContainerInCoronaQuestionnaire);
        recyclerView = (RecyclerView) findViewById(R.id.rv_coronaSurvey);
        textViewSomeThingWentWrong = (TextView) findViewById(R.id.tv_warning);
        textViewResponseTitle = (TextView) findViewById(R.id.tv_responseTitleInCoronaQuestionnaire);
        coronaSurveyViewModel = ViewModelProviders.of(this).get(SurveyFormViewModel.class);
        mAdapter = new Servay_Type(this);
        SurveytypeActivity.sarveyList.clear();
        initRecycler();
        subscribeViewModel();

    }
    private void subscribeViewModel()
    {
        coronaSurveyViewModel.observeResponse().removeObservers(this);
        coronaSurveyViewModel.observeResponse().observe(this, new Observer<Resource<CoronaSurveyQuestionResponseContainer>>() {
            @Override
            public void onChanged(@Nullable Resource<CoronaSurveyQuestionResponseContainer> resObj) {
                observeQuestionsResponseFromServer(resObj);
            }
        });


    }


    private void initRecycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }
    private void observeQuestionsResponseFromServer(@Nullable Resource<CoronaSurveyQuestionResponseContainer> resObj){
        if (resObj!=null)
        {
            switch (resObj.status){
                case LOADING:{
                    Log.d("TAG", "onChanged: LOADING...");
                    showView(linearLayoutProgressBarContainer);
                    hideView(recyclerView);
                    hideView(linearLayoutErrorContainer);
                    break;
                }
                case ERROR:{
                    prepareErrorLayoutContainer(resObj.message);
                    showView(linearLayoutErrorContainer);
                    hideView(linearLayoutProgressBarContainer);
                    hideView(recyclerView);
                    break;
                }
                case SUCCESS:{
                    List<String> employees = new ArrayList<>();
                    List<String> employees1 = new ArrayList<>();
                    for (int i = 0; i < resObj.data.getData().size(); i++) {
                        employees.add(resObj.data.getData().get(i).getSurveyType());
                    }
                    Set<String> set = new HashSet<>(employees);
                    employees.clear();
                    employees.addAll(set);
                    SurveytypeActivity.sarveyList.clear();

                    mAdapter.setDataToAdapter(employees);
                    showView(recyclerView);
                    hideView(linearLayoutProgressBarContainer);
                    hideView(linearLayoutErrorContainer);
                   // showView(textViewSubmit);
                    break;
                }
            }
        }
    }

    public Set<String> findDuplicates(List<String> listContainingDuplicates)
    {
        final Set<String> setToReturn = new HashSet<>();
        final Set<String> set1 = new HashSet<>();

        for (String yourInt : listContainingDuplicates)
        {
            if (!set1.add(yourInt))
            {
                setToReturn.add(yourInt);
            }
        }
        return setToReturn;
    }

    private void prepareErrorLayoutContainer(String errorMessage){
        textViewSomeThingWentWrong.setText(errorMessage);
        textViewResponseTitle.setText("ERROR");
        textViewResponseTitle.setBackgroundColor(Color.parseColor(ColorConstants.ERROR_RED));
    }
    private void showView(View view){
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view){
        view.setVisibility(View.GONE);
    }

}