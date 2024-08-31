package com.suveyform.ui.fragments.questions_fragment.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;
import com.suveyform.models.questionaire.question_response.CoronaSurveyQuestionResponseData;
import com.suveyform.ui.fragments.questions_fragment.adapters.view_holders.CoronaSurveyCheckboxViewHolderNew;
import com.suveyform.ui.fragments.questions_fragment.adapters.view_holders.CoronaSurveyEditTextViewHolderNew;
import com.suveyform.ui.fragments.questions_fragment.adapters.view_holders.CoronaSurveyRadioButtonViewHolderNew;

import java.util.ArrayList;
import java.util.List;
//import android.support.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView ;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.fieldforce.eureka.corona_survey_module.models.questionaire.question_response.CoronaSurveyQuestionResponseData;
//import com.fieldforce.eureka.corona_survey_module.ui.fragments.questions_fragment.adapters.view_holders.CoronaSurveyCheckboxViewHolderNew;
//import com.fieldforce.eureka.corona_survey_module.ui.fragments.questions_fragment.adapters.view_holders.CoronaSurveyEditTextViewHolderNew;
//import com.fieldforce.eureka.corona_survey_module.ui.fragments.questions_fragment.adapters.view_holders.CoronaSurveyRadioButtonViewHolderNew;
//import com.fieldforce.efisd.R;
//
//import java.util.List;
//import linq.ArrayList;java.util.List;

public class CoronaQuestionsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int EDIT_TEXT_TYPE =1;
    private static final int RADIO_BUTTON_TYPE =2;
    private static final int CHECKBOX_TYPE = 3;
    private static final int UNDEFINED_TYPE = 0;
    private List<CoronaSurveyQuestionResponseData> modelList;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        switch (viewType){
            case EDIT_TEXT_TYPE :{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coronasurvey_edittext_item,parent,false);
                return new CoronaSurveyEditTextViewHolderNew(view);
            }
            case RADIO_BUTTON_TYPE :{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coronasurvey_radiobutton_item,parent,false);
                return new CoronaSurveyRadioButtonViewHolderNew(view);
            }
            case CHECKBOX_TYPE : {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coronasurvey_checkbox_item,parent,false);
                return new CoronaSurveyCheckboxViewHolderNew(view);
            }
            default:{
                return null;
            }
        }
    }

    //https://stackoverflow.com/questions/37523308/when-onbindviewholder-is-called-and-how-it-works
    private static final String TAG = "CoronaSurveyAdapter";
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case EDIT_TEXT_TYPE :{
                Log.e("Date",SurveyDate);
                ((CoronaSurveyEditTextViewHolderNew)holder).bindValues(modelList.get(position),SurveyDate);
                break;
            }
            case RADIO_BUTTON_TYPE:{
                Log.e("Date",SurveyDate);
                ((CoronaSurveyRadioButtonViewHolderNew)holder).bindValues(modelList.get(position),SurveyDate);
                break;
            }
            case CHECKBOX_TYPE :{
                Log.e("Date",SurveyDate);
                ((CoronaSurveyCheckboxViewHolderNew)holder).bindItemsInCheckboxViewHolder(modelList.get(position),SurveyDate);
                break;
            }
        }
    }


    @Override
    public int getItemCount() {
        if (modelList!=null){
            return modelList.size()>0?modelList.size():0;
        }
        else {
            return 0;
        }
    }
   String SurveyDate;
    public void setDataToAdapter(List<CoronaSurveyQuestionResponseData> list, String s)
    {
        Log.e("Date55",s);
        modelList = new ArrayList<>();
        modelList = list;
        SurveyDate=s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(modelList.get(position).getType().equalsIgnoreCase("textbox")){
            return EDIT_TEXT_TYPE;
        }
        else if (modelList.get(position).getType().equalsIgnoreCase("radiobutton")){
            return RADIO_BUTTON_TYPE;
        }
        else if (modelList.get(position).getType().equalsIgnoreCase("checkbox")){
            return CHECKBOX_TYPE;
        }
        else {
            return 0;
        }
    }

    public List<CoronaSurveyQuestionResponseData> getRequestList(){
        return  modelList;
    }

}
