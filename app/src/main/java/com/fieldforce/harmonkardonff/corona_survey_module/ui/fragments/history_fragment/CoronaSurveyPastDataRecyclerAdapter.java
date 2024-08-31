package com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.history_fragment;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.corona_survey_module.models.history.CoronaSurveyHistoryData;
import com.fieldforce.harmonkardonff.corona_survey_module.models.history.Value;
import com.fieldforce.utility.ColorConstants;

import java.util.List;

public class CoronaSurveyPastDataRecyclerAdapter extends RecyclerView.Adapter<CoronaSurveyPastDataRecyclerAdapter.MyCoronaPastDataViewHolder> {

    private List<CoronaSurveyHistoryData> valueList;
    @NonNull
    @Override
    public MyCoronaPastDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.corona_history_dynamic_item,viewGroup,false);
        return new MyCoronaPastDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCoronaPastDataViewHolder myCoronaPastDataViewHolder, int position) {
        myCoronaPastDataViewHolder.bindViews(valueList.get(position).getValue(),valueList.get(position));
    }

    @Override
    public int getItemCount() {
        if (valueList!=null){
            return valueList.size();
        }
        else {
            return 0;
        }
    }

    public void setData(List<CoronaSurveyHistoryData> valueList){
        this.valueList = valueList;
        notifyDataSetChanged();
    }

    static class MyCoronaPastDataViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;
        private TextView textViewDate;
         MyCoronaPastDataViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_dynamicCoronaTextViewContainer);
            textViewDate = itemView.findViewById(R.id.tv_dateValueInCoronaPastFragment);
        }
         void bindViews(List<Value> values, CoronaSurveyHistoryData object){
             textViewDate.setText(object.getDate());
            linearLayout.removeAllViews();
            if (values.size()>0){
                for (Value obj : values)
                {
                    TextView textView = new TextView(linearLayout.getContext());
                    textView.setText(obj.getTitle()+" : "+obj.getTileValue());
                    textView.setGravity(Gravity.CENTER);
                    linearLayout.addView(textView);
                }
            }
            else {
                TextView textView = new TextView(linearLayout.getContext());
                textView.setTextColor(Color.parseColor(ColorConstants.ERROR_RED));
                textView.setText("No Data Available");
                linearLayout.addView(textView);
            }
        }
    }
}
