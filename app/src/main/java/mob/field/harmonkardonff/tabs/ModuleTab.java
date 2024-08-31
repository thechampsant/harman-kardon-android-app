package mob.field.harmonkardonff.tabs;

import android.os.Bundle;

import com.fieldforce.harmonkardonff.Module_Display;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.View_Module_List;

import linq.ArrayList;
import mob.field.harmonkardonff.fragments.SaleDisplay;
import mob.field.harmonkardonff.fragments.SaleEnter;
import mob.field.harmonkardonff.fragments.View_Module_List_new;
import mob.field.harmonkardonff.fragments.viewsale;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

public class ModuleTab extends FragmentTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ArrayList<TabItem> setTabItems() {
        // TODO Auto-generated method stub
        ArrayList<TabItem> data = new ArrayList<TabItem>();
        data.add(new TabItem().setItem("Module Display", Module_Display.class));
        data.add(new TabItem().setItem("View Module Display", View_Module_List.class));
        data.add(new TabItem().setItem("Same As Previous", View_Module_List_new.class));


        // data.add(new TabItem().setItem("MTD Sale",SaleDisplayMTD.class));

        // data.add(new TabItem().setItem("LMTD Sale",SaleDisplayLMTD.class));
        return data;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class setTabClass() {
        // TODO Auto-generated method stub
        return ModuleTab.class;
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
