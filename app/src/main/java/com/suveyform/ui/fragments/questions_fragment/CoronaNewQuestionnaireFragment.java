package com.suveyform.ui.fragments.questions_fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.utility.ColorConstants;
import com.suveyform.ImageResponse;
import com.suveyform.SurveytypeActivity;
import com.suveyform.models.questionaire.answer_response.CoronaSurveyAnswerResponseContainer;
import com.suveyform.models.questionaire.question_response.CoronaSurveyQuestionResponseContainer;
import com.suveyform.models.questionaire.question_response.CoronaSurveyQuestionResponseData;
import com.suveyform.ui.fragments.questions_fragment.adapters.CoronaQuestionsRecyclerAdapter;
import com.suveyform.utils.Resource;
import com.suveyform.view_models.SurveyFormViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import app.core.base.IFragment;
import app.core.utils.Dialog;

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
    ImageView btn_for_dates;
    private SurveyFormViewModel coronaSurveyViewModel;
    public static HashMap<String, String> mHap = new HashMap<>();
    TextView txt_for_dates;
    public static List<ImageResponse> imageResponses=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.coronasurvey_fragment_new, inflater, container);
    }

    @Override
    public void Activate(View FragmentView) {
        initWidgets();
    }

    private void initWidgets(){
        linearLayoutProgressBarContainer = (LinearLayout) findViewById(R.id.ll_progressBarContainer);
        linearLayoutErrorContainer = (LinearLayout) findViewById(R.id.ll_errorContainerInCoronaQuestionnaire);
        recyclerView = (RecyclerView) findViewById(R.id.rv_coronaSurvey);
        txt_for_dates= (TextView)findViewById(R.id.txt_for_dates);
        textViewSomeThingWentWrong = (TextView) findViewById(R.id.tv_warning);
        textViewResponseTitle = (TextView) findViewById(R.id.tv_responseTitleInCoronaQuestionnaire);
        textViewSubmit = (TextView) findViewById(R.id.tv_submitCoronaQuestionnaireNew);

        btn_for_dates=(ImageView) findViewById(R.id.btn_for_dates);
        setListener();
        mAdapter = new CoronaQuestionsRecyclerAdapter();
        String date = GetCurrentDateInString();
        startDate = date;
        CoronaNewQuestionnaireFragment.SelectDate=startDate;
        SetTextViewAsString(R.id.txt_for_dates, date);
       btn_for_dates.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setDatePicker();
           }
       });
        coronaSurveyViewModel = ViewModelProviders.of(getActivity()).get(SurveyFormViewModel.class);
        Log.e("Data", SurveytypeActivity.sarveyList.get(0));
        initRecycler();
        subscribeViewModel();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };
    private void updateDate() {
//        2021-03-08
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        txt_for_dates.setText(sdf.format(myCalendar.getTime()));
        startDate=sdf.format(myCalendar.getTime());
        CoronaNewQuestionnaireFragment.SelectDate=startDate;
    }
    public static String SelectDate="";
    void setDatePicker() {
        myCalendar = Calendar.getInstance();
//Get yesterday's date
        myCalendar.add(Calendar.DATE, -2);
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
//Set yesterday time milliseconds as date pickers minimum date
        datePickerDialog = new DatePickerDialog(context, date, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private Calendar myCalendar;
    private DatePickerDialog datePickerDialog;
    private String startDate;
    private void setListener(){
        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Set<ImageResponse> s= new HashSet<ImageResponse>();
                s.addAll(imageResponses);
                imageResponses = new ArrayList<ImageResponse>();
                imageResponses.addAll(s);

                List<ImageResponse> noRepeat1 = new ArrayList<ImageResponse>();
                noRepeat1.addAll(imageResponses);

                for(int i=0;i<imageResponses.size();i++)
                {
                    for(int j=1;j<noRepeat1.size();j++)
                    {
                        if(imageResponses.get(i).getQID()==noRepeat1.get(j).getQID())
                        {
                            if(imageResponses.get(i).getImageURl().equalsIgnoreCase(""))
                              imageResponses.remove(i);
                        }
                    }
                }
                /*List<ImageResponse> noRepeat = new ArrayList<ImageResponse>();


                for (ImageResponse event : imageResponses) {
                    boolean isFound = false;
                    // check if the event name exists in noRepeat
                    for (ImageResponse e : noRepeat) {
                        if (e.getQID() == event.getQID() || (e.equals(event))) {
                            isFound = true;
                            break;
                        }
                    }

                    if (!isFound) noRepeat.add(event);
                }

                Log.e("Image", String.valueOf(noRepeat.size())+"rj");
                imageResponses.clear();
                imageResponses.addAll(noRepeat);*/
                sendAnswerToServer();
            }
        });
    }

    private void initRecycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
    }

    private static final String TAG = "CoronaNewQuestionnaiOO";
    private void subscribeViewModel()
    {

        coronaSurveyViewModel.observeResponse().removeObservers(getViewLifecycleOwner());
        coronaSurveyViewModel.observeResponse().observe(getViewLifecycleOwner(), new Observer<Resource<CoronaSurveyQuestionResponseContainer>>() {
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
                    try {
                        List<CoronaSurveyQuestionResponseData> employees1 = new linq.ArrayList<>();
                        employees1.addAll(resObj.data.getData());
                        SurveytypeActivity.sarveyList.get(0);
                        resObj.data.getData().clear();
                        for(int i=0;i<employees1.size();i++)
                        {
                            if(employees1.get(i).getSurveyType().equalsIgnoreCase(SurveytypeActivity.sarveyList.get(0)))
                                resObj.data.getData().add(employees1.get(i));
                        }
                        mAdapter.setDataToAdapter(resObj.data.getData(),txt_for_dates.getText().toString());
                        initializeHashMapWithDefaultValues(resObj.data.getData());
                        showView(recyclerView);
                        hideView(linearLayoutProgressBarContainer);
                        hideView(linearLayoutErrorContainer);
                        showView(textViewSubmit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    new Dialog(getActivity()).setTitle("Server Response").setMessage(stringModelResource.message).show();
                    hideView(linearLayoutProgressBarContainer);
                    hideView(linearLayoutErrorContainer);
                    break;
                }
                case SUCCESS:{
                    //prepareSuccessLayoutContainer(stringModelResource.data.getData().get(0).getMessage());
                    prepareSuccessDialog(stringModelResource.data.getData().get(0).getMessage());
                    showView(linearLayoutErrorContainer);
                    hideView(linearLayoutProgressBarContainer);
                    hideView(recyclerView);
                    hideView(textViewSubmit);
                    hideView(textViewSomeThingWentWrong);
                    break;
                }
            }
        }
    }

    private void prepareSuccessDialog(String successMessage){
        AlertDialog.Builder db = new AlertDialog.Builder(getActivity());
        db.setTitle("Server Response");
        db.setMessage(successMessage);
        db.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                setTab(1);
            }
        });
        db.show();

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
    private String imageUpload="";

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 15 && resultCode == Activity.RESULT_OK) {
            Log.e("fnjvf","fvmnfvnf");
            String GUID = data.getExtras().getString("QID");
            String IMAGE_CAPTURED = data.getExtras().getString("IMAGE_CAPTURED") + "";
          imageUpload=IMAGE_CAPTURED;
            ImageResponse imageResponse1=new ImageResponse(Integer.parseInt(GUID),IMAGE_CAPTURED);
            imageResponses.add(imageResponse1);
            // ImageCaptureResult result = getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);
            showImageToastAfterImageCapture(IMAGE_CAPTURED);

        }

    }
    private void showImageToastAfterImageCapture(String result) {

        if (!result.equals(""))
            ShowToastLong(" image is attached", 0);
        else
            ShowToastLong("No image found to attach", 0);

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
                 if(imageResponses.size()>0)
                 {
                     for (int j=0;j<imageResponses.size();j++)
                     {
                         if(questionListt.get(i).getQID().equalsIgnoreCase(String.valueOf(imageResponses.get(j).getQID())))
                         {

                              if(value.equalsIgnoreCase("Yes"))
                              {
                                  if(imageResponses.get(j).getImageURl().equalsIgnoreCase("")) {
                                      Toast.makeText(getActivity(), "Image is Mandatory", Toast.LENGTH_LONG).show();
                                      allAnswersValidated = false;
                                  }
                              }


                         }

                     }

                 }

                if (value.equalsIgnoreCase(""))
                {
                    stringBuilder.append("Q"+(i+1)+",");
                    allAnswersValidated = false;
                }
/*
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
*/
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
          //  prepareQuestionIdsInAKey();
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
        textViewSubmit.setVisibility(View.GONE);
        coronaSurveyViewModel.submitAnswersToServer(startDate,questionIds.toString(),answers.toString());
    }


    public void showView(View view){
        view.setVisibility(View.VISIBLE);
    }

    public void hideView(View view){
        view.setVisibility(View.GONE);
    }

}
