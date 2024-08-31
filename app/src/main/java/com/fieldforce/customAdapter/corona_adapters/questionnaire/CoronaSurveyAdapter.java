package com.fieldforce.customAdapter.corona_adapters.questionnaire;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldforce.customAdapter.corona_adapters.questionnaire.ViewHolders.CoronaSurveyCheckboxViewHolder;
import com.fieldforce.customAdapter.corona_adapters.questionnaire.ViewHolders.CoronaSurveyEditTextViewHolder;
import com.fieldforce.customAdapter.corona_adapters.questionnaire.ViewHolders.CoronaSurveyRadioButtonViewHolder;
import com.fieldforce.harmonkardonff.R;


import java.util.List;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.CoronaSurveyRequestModel;

public class CoronaSurveyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int EDIT_TEXT_TYPE =1;
    private static final int RADIO_BUTTON_TYPE =2;
    private static final int CHECKBOX_TYPE = 3;
    private static final int UNDEFINED_TYPE = 0;
    private List<CoronaSurveyRequestModel> modelList;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        switch (viewType){
            case EDIT_TEXT_TYPE :{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coronasurvey_edittext_item,parent,false);
                return new CoronaSurveyEditTextViewHolder(view);
            }
            case RADIO_BUTTON_TYPE :{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coronasurvey_radiobutton_item,parent,false);
                return new CoronaSurveyRadioButtonViewHolder(view);
            }
            case CHECKBOX_TYPE : {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coronasurvey_checkbox_item,parent,false);
                return new CoronaSurveyCheckboxViewHolder(view);
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
                ((CoronaSurveyEditTextViewHolder)holder).bindValues(modelList.get(position));
                break;
            }
            case RADIO_BUTTON_TYPE:{
                ((CoronaSurveyRadioButtonViewHolder)holder).bindValues(modelList.get(position));
                break;
            }
            case CHECKBOX_TYPE :{
                ((CoronaSurveyCheckboxViewHolder)holder).bindItemsInCheckboxViewHolder(modelList.get(position));
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

    public void setDataToAdapter(List<CoronaSurveyRequestModel> list)
    {
        modelList = new ArrayList<>();
        modelList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(modelList.get(position).Type.equalsIgnoreCase("textbox")){
            return EDIT_TEXT_TYPE;
        }
        else if (modelList.get(position).Type.equalsIgnoreCase("radiobutton")){
            return RADIO_BUTTON_TYPE;
        }
        else if (modelList.get(position).Type.equalsIgnoreCase("checkbox")){
            return CHECKBOX_TYPE;
        }
        else {
            return 0;
        }
    }

    public ArrayList<CoronaSurveyRequestModel> getRequestList(){
        return (ArrayList<CoronaSurveyRequestModel>) modelList;
    }

}
