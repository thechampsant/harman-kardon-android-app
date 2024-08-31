package com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.questions_fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.answer_response.CoronaSurveyAnswerResponseContainer;
import com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.question_response.CoronaSurveyQuestionResponseContainer;
import com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.question_response.CoronaSurveyQuestionResponseData;
import com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.questions_fragment.adapters.CoronaQuestionsRecyclerAdapter;
import com.fieldforce.harmonkardonff.corona_survey_module.view_models.CoronaSurveyViewModel;
import com.fieldforce.utility.ColorConstants;
import com.fieldforce.utility.Resource;

import java.util.HashMap;
import java.util.List;

import app.core.base.IFragment;
import app.core.utils.Dialog;
import mob.field.harmonkardonff.services.WebService;

public class CoronaNewQuestionnaireFragment extends IFragment
{

    public static final String MANDATORY ="Mandatory:";
    public static final String NON_MANDATORY ="NonMandatory:";
    private LinearLayout linearLayoutProgressBarContainer;
    private LinearLayout linearLayoutErrorContainer;
    private RecyclerView recyclerView;
    private TextView textViewSomeThingWentWrong;
    private TextView textViewResponseTitle;
    private TextView textViewSubmit;
    private CoronaQuestionsRecyclerAdapter mAdapter;
    private CoronaSurveyViewModel coronaSurveyViewModel;
    public static HashMap<String,String> mHap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.coronasurvey_fragment, inflater, container);
    }

    @Override
    public void Activate(View FragmentView) {
        initWidgets();
    }

    private void initWidgets(){
        linearLayoutProgressBarContainer = (LinearLayout) findViewById(R.id.ll_progressBarContainer);
        linearLayoutErrorContainer = (LinearLayout) findViewById(R.id.ll_errorContainerInCoronaQuestionnaire);
        recyclerView = (RecyclerView) findViewById(R.id.rv_coronaSurvey);
        textViewSomeThingWentWrong = (TextView) findViewById(R.id.tv_warning);
        textViewResponseTitle = (TextView) findViewById(R.id.tv_responseTitleInCoronaQuestionnaire);
        textViewSubmit = (TextView) findViewById(R.id.tv_submitCoronaQuestionnaireNew);
        setListener();
        mAdapter = new CoronaQuestionsRecyclerAdapter();
        coronaSurveyViewModel = ViewModelProviders.of(getActivity()).get(CoronaSurveyViewModel.class);
        initRecycler();
        subscribeViewModel(WebService.UserName);
    }

    private void setListener(){
        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswerToServer();
            }
        });
    }

    private void initRecycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
    }

    private static final String TAG = "CoronaNewQuestionnaiOO";
    private void subscribeViewModel(String userName)
    {
        coronaSurveyViewModel.observeResponse(userName).removeObservers(getViewLifecycleOwner());
        coronaSurveyViewModel.observeResponse(userName).observe(getViewLifecycleOwner(), new Observer<Resource<CoronaSurveyQuestionResponseContainer>>() {
            @Override
            public void onChanged(@Nullable Resource<CoronaSurveyQuestionResponseContainer> resObj) {
                observeQuestionsResponseFromServer(resObj);
            }
        });

        coronaSurveyViewModel.getAndObserveAnswerSubmittionResponse().removeObservers(getViewLifecycleOwner());
        coronaSurveyViewModel.getAndObserveAnswerSubmittionResponse().observe(getViewLifecycleOwner(), new Observer<Resource<CoronaSurveyAnswerResponseContainer>>() {
            @Override
            public void onChanged(@Nullable Resource<CoronaSurveyAnswerResponseContainer> stringModelResource) {
                observeAnswersResponse(stringModelResource);
            }
        });
    }

    private void observeQuestionsResponseFromServer(@Nullable Resource<CoronaSurveyQuestionResponseContainer> resObj){
        if (resObj!=null)
        {
            switch (resObj.status){
                case LOADING:{
                    Log.d(TAG, "onChanged: LOADING...");
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
                    mAdapter.setDataToAdapter(resObj.data.getData());
                    initializeHashMapWithDefaultValues(resObj.data.getData());
                    showView(recyclerView);
                    hideView(linearLayoutProgressBarContainer);
                    hideView(linearLayoutErrorContainer);
                    showView(textViewSubmit);
                    break;
                }
            }
        }
    }

    private void observeAnswersResponse(@Nullable Resource<CoronaSurveyAnswerResponseContainer> stringModelResource){
        if (stringModelResource!=null){
            switch (stringModelResource.status){
                case LOADING:{
                    showView(linearLayoutProgressBarContainer);
                    hideView(linearLayoutErrorContainer);
                    break;
                }
                case ERROR:{
                    new Dialog(getActivity()).setTitle("Error").setMessage(stringModelResource.message).show();
                    hideView(linearLayoutProgressBarContainer);
                    hideView(linearLayoutErrorContainer);
                    break;
                }
                case SUCCESS:{
                    prepareSuccessLayoutContainer(stringModelResource.data.getData().getMessage());
                    showView(linearLayoutErrorContainer);
                    hideView(linearLayoutProgressBarContainer);
                    hideView(recyclerView);
                    break;
                }
            }
        }
    }

    private void initializeHashMapWithDefaultValues(List<CoronaSurveyQuestionResponseData> dataList){
        for (CoronaSurveyQuestionResponseData obj : dataList)
        {
            if (obj.getIsMandatory().equalsIgnoreCase("true")){
                mHap.put(obj.getQID(),"");
            }
            else {
                mHap.put(obj.getQID(),NON_MANDATORY);
            }
        }
    }

    private void prepareErrorLayoutContainer(String errorMessage){
        textViewSomeThingWentWrong.setText(errorMessage);
        textViewResponseTitle.setText("ERROR");
        textViewResponseTitle.setBackgroundColor(Color.parseColor(ColorConstants.ERROR_RED));
    }

    private void prepareSuccessLayoutContainer(String successMessage){
        textViewSomeThingWentWrong.setText(successMessage);
        textViewResponseTitle.setText("Success");
        textViewResponseTitle.setBackgroundColor(Color.parseColor(ColorConstants.SUCCESS_GREEN));
    }

    private boolean allAnswersValidated = true;
    private boolean validate(){
        StringBuilder stringBuilder = new StringBuilder();
        String tempRange = "";
        try{
            allAnswersValidated = true;
            Log.d(TAG, "validate: "+mHap);
            List<CoronaSurveyQuestionResponseData> questionListt = mAdapter.getRequestList();
            for (int i = 0;i<questionListt.size();i++){
                String value = mHap.get(questionListt.get(i).getQID());
                if (value.equalsIgnoreCase(""))
                {
                    stringBuilder.append("Q"+(i+1)+",");
                    allAnswersValidated = false;
                }
                else {
                    if (questionListt.get(i).getQID().equalsIgnoreCase("1")){
                        double db = Double.parseDouble(value);
                        if (db<96)
                        {
                            tempRange = "Temperature value must be in between 96 - 104.";
                            allAnswersValidated = false;
                        }
                        else {
                            if (db>104){
                                tempRange = "Temperature value must be in between 96 - 104.";
                                allAnswersValidated = false;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception exception){
            allAnswersValidated = false;
            new Dialog(getActivity()).setTitle("Exception").setMessage(exception.getMessage()+" in "+TAG+", "+exception.getStackTrace()[0]).show();
        }
        Log.d(TAG, "No Answer found for : "+stringBuilder.toString());

        if (allAnswersValidated){
            return true;
        }
        else {
            if (stringBuilder.length()>0)
                new Dialog(getActivity()).setTitle("No Answer Found").setMessage("Please provide answers for Question : "+stringBuilder.substring(0, stringBuilder.length() - 1)).show();
            if (tempRange.length()>2)
                new Dialog(getActivity()).setTitle("Temperature Range").setMessage(tempRange).show();
            return false;
        }
    }

    private void sendAnswerToServer()
    {
        try{
            if (validate()){
                prepareQuestionIdsInAKey();
            }
            else {
                Log.d(TAG, "Something went wrong...");
            }
        }
        catch (Exception exception){
            Log.d(TAG, "sendAnswerToServer: "+exception.getMessage());
            new Dialog(getActivity()).setTitle("Exception").setMessage(exception.getMessage()+" in "+TAG+", "+exception.getStackTrace()[0]).show();
        }
    }

    private void prepareQuestionIdsInAKey()
    {
        List<CoronaSurveyQuestionResponseData> questionListt = mAdapter.getRequestList();
        StringBuilder questionIds = new StringBuilder();
        StringBuilder answers = new StringBuilder();

        for (CoronaSurveyQuestionResponseData obj : questionListt){
            if (obj!=null){
                questionIds.append(obj.getQID()).append(",");
                String answerValue = mHap.get(obj.getQID());
                if (answerValue.equalsIgnoreCase(NON_MANDATORY)){
                    answerValue = "";
                }
                answers.append(answerValue).append(",");
            }
        }
        questionIds = new StringBuilder(questionIds.substring(0, questionIds.length() - 1));
        answers = new StringBuilder(answers.substring(0, answers.length() - 1));
        Log.d(TAG, "prepareQuestionIdsInAKey: QuestionIds -> "+questionIds);
        Log.d(TAG, "prepareQuestionIdsInAKey: Answers -> "+answers);

        coronaSurveyViewModel.submitAnswersToServer(questionIds.toString(),answers.toString());
    }


    private void showView(View view){
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view){
        view.setVisibility(View.GONE);
    }
}
