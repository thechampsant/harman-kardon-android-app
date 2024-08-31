package com.suveyform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.harmonkardonff.R;
import com.suveyform.ui.fragments.SurveyFormTabsActivity;

import java.util.ArrayList;
import java.util.List;

public class Servay_Type extends RecyclerView.Adapter<Servay_Type.MyView> {

    List<String> CoronaSurveyQuestionResponseDataArrayList;
    Activity main;
    LayoutInflater layoutInflater;
    boolean fromOffers;

    public Servay_Type( Activity activity) {
        main = activity;

        layoutInflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.servay_type_list, null, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyView holder, final int position) {
        holder.docName.setText(CoronaSurveyQuestionResponseDataArrayList.get(position));


        holder.docName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = CoronaSurveyQuestionResponseDataArrayList.get(position);
                if(SurveytypeActivity.sarveyList.size()>0)
                    SurveytypeActivity.sarveyList.clear();

                   SurveytypeActivity.sarveyList.add(url);
                    Intent intent=new Intent(main, SurveyFormTabsActivity.class);
                    main.startActivity(intent);




            }
        });
    }

    @Override
    public int getItemCount() {
        return CoronaSurveyQuestionResponseDataArrayList != null ? CoronaSurveyQuestionResponseDataArrayList.size() : 0;
    }

    public void setDataToAdapter(List<String> data) {
        CoronaSurveyQuestionResponseDataArrayList = new ArrayList<>();
        CoronaSurveyQuestionResponseDataArrayList = data;

    }

    class MyView extends RecyclerView.ViewHolder {
        TextView docName;


        public MyView(View itemView) {
            super(itemView);
            docName = (TextView) itemView.findViewById(R.id.servay_type);

        }
    }
}
