package com.fieldforce.harmonkardonff.demo_tracking_module.ui.activities;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.demo_tracking_module.ui.fragments.EnterDemoFragment;
import com.fieldforce.harmonkardonff.demo_tracking_module.ui.fragments.ViewDemoFragment;

import app.core.utils.Dialog;
import linq.ArrayList;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

public class DemoTrackingFragmentsContainer extends FragmentTabActivity {
    @Override
    public ArrayList<TabItem> setTabItems() {
        ArrayList<TabItem> data= new ArrayList<TabItem>();
        try{
            data.add(new TabItem().setItem("Enter Demo", EnterDemoFragment.class));
            data.add(new TabItem().setItem("View Demo", ViewDemoFragment.class));
            return data;
        }
        catch (Exception exception){
            new Dialog(this).setTitle("Exception").setMessage(exception.getMessage()+" in DemoTrackingFragmentsContainer "+", "+exception.getStackTrace()[0]).show();
            return data;
        }
    }

    @Override
    public Class setTabClass() {
        return DemoTrackingFragmentsContainer.class;
    }

    @Override
    public int setFragmentLayout() {
        return R.layout.tab_fragment;
    }

    @Override
    public void RegisterTableInfoForLocalDB() {

    }
}
