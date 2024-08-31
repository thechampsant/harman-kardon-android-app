package com.fieldforce.harmonkardonff.corona_survey_module.ui;

import android.os.Bundle;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.questions_fragment.CoronaNewQuestionnaireFragment;
import com.fieldforce.harmonkardonff.corona_survey_module.ui.fragments.history_fragment.CoronaSurveyPastDataFragment;

import app.core.utils.Dialog;
import linq.ArrayList;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

public class CoronaSurveyTabsActivity extends FragmentTabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ArrayList<TabItem> setTabItems()
    {
        ArrayList<TabItem> data= new ArrayList<TabItem>();
        try{
            data.add(new TabItem().setItem("Questionnaire", CoronaNewQuestionnaireFragment.class));
            data.add(new TabItem().setItem("History Data", CoronaSurveyPastDataFragment.class));
            return data;
        }
        catch (Exception exception){
            new Dialog(this).setTitle("Exception").setMessage(exception.getMessage()+" in CoronaSurveyTabsActivity "+", "+exception.getStackTrace()[0]).show();
            return data;
        }
    }

    @Override
    public Class setTabClass() {
        return CoronaSurveyTabsActivity.class;
    }

    @Override
    public int setFragmentLayout() {
        return R.layout.tab_fragment;
    }

    @Override
    public void RegisterTableInfoForLocalDB() {
        // TODO Auto-generated method stub
    }
}
