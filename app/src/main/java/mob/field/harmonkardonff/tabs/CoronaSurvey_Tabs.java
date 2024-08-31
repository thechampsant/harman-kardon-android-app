package mob.field.harmonkardonff.tabs;

import android.os.Bundle;

import com.fieldforce.harmonkardonff.R;

import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.fragments.CoronaSurveyHistoryFragment;
import mob.field.harmonkardonff.fragments.CoronasurveyQuestionnaire;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

public class CoronaSurvey_Tabs extends FragmentTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ArrayList<TabItem> setTabItems()
    {
        ArrayList<TabItem> data= new ArrayList<TabItem>();
        try{
            data.add(new TabItem().setItem("Questionnaire", CoronasurveyQuestionnaire.class));
            data.add(new TabItem().setItem("History Data", CoronaSurveyHistoryFragment.class));
            return data;
        }
        catch (Exception exception){
            new Dialog(this).setTitle("Exception").setMessage(exception.getMessage()+" in CoronaSurvey_Tabs "+", "+exception.getStackTrace()[0]).show();
            return data;
        }
    }

    @Override
    public Class setTabClass() {
        return CoronaSurvey_Tabs.class;
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
