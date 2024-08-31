package com.suveyform.ui.fragments.history_fragment;

import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fieldforce.harmonkardonff.R;
import com.ariston.training_module.utility.ColorConstants;
import com.suveyform.SurveytypeActivity;
import com.suveyform.models.history.CoronaSurveyHistoryData;
import com.suveyform.models.history.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CoronaSurveyPastDataRecyclerAdapter extends RecyclerView.Adapter<CoronaSurveyPastDataRecyclerAdapter.MyCoronaPastDataViewHolder> {

    private List<CoronaSurveyHistoryData> valueList;
    @NonNull
    @Override
    public MyCoronaPastDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.corona_history_dynamic_item_survey_new,viewGroup,false);
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
        private TextView textViewDate;
        private TextView textViewDayOfTheWeek;
        private LinearLayout linearLayout;
         MyCoronaPastDataViewHolder(@NonNull View itemView) {
            super(itemView);
             textViewDate = itemView.findViewById(R.id.tv_dateValueInSurvey);
             textViewDayOfTheWeek = itemView.findViewById(R.id.tv_dayOfTheWeek);
             linearLayout = itemView.findViewById(R.id.ll_parent);
        }
         void bindViews(List<Value> values, CoronaSurveyHistoryData object)
         {
             String sDate1=object.getDate();
             String dayOfTheWeek = "";
             try {
                 Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
                 Log.d("olaWa", "bindViews: "+date1);
                 dayOfTheWeek = (String) DateFormat.format("EEEE", date1); // Thursday
                 textViewDayOfTheWeek.setText(dayOfTheWeek);
                 textViewDayOfTheWeek.setTextColor(Color.parseColor(ColorConstants.BLUE));
             } catch (ParseException e) {
                 e.printStackTrace();
                textViewDayOfTheWeek.setVisibility(View.GONE);
             }
             linearLayout.removeAllViews();
             textViewDate.setText(object.getDate());
             linearLayout.addView(textViewDate);
            if (values.size()>0)
            {
                for (Value obj : values)
                {
                    if(obj.getSurveyType().equalsIgnoreCase(SurveytypeActivity.sarveyList.get(0)))
                    {
                        TextView textView = new TextView(itemView.getContext());
                        textView.setId(View.generateViewId());
                        textView.setText(obj.getTitle()+" : "+obj.getTileValue());
                        linearLayout.addView(textView);
                    }

                }
            }
            else {
                TextView textView = new TextView(itemView.getContext());
                textView.setId(View.generateViewId());
                textView.setText("No Data Found");
                textView.setTextColor(Color.parseColor(ColorConstants.ERROR_RED));
                linearLayout.addView(textView);
            }
        }
    }
}
