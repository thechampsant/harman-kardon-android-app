package com.fieldforce.harmonkardonff.Comptition;

import android.os.Bundle;


import com.fieldforce.harmonkardonff.R;

import linq.ArrayList;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

public class CompetitionTab extends FragmentTabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public ArrayList<TabItem> setTabItems() {

        // TODO Auto-generated method stub

        ArrayList<TabItem> data= new ArrayList<TabItem>();
        data.add(new TabItem().setItem("Enter Counter share (MTD)", CompetitionEnter.class));
        data.add(new TabItem().setItem("View Counter share (MTD)", ViewCompetition.class));
        return data;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public Class setTabClass() {
        // TODO Auto-generated method stub
        return CompetitionTab.class;
    }

    @Override
    public int setFragmentLayout() {
        // TODO Auto-generated method stub

        return R.layout.tab_fragment;
    }



    @Override
    public void RegisterTableInfoForLocalDB() {
        // TODO Auto-generated method stub

    }



}

