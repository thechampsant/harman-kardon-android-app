package mob.field.harmonkardonff.tabs;

import android.os.Bundle;

import com.fieldforce.harmonkardonff.Module_Display;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SubmitDownStock;
import com.fieldforce.harmonkardonff.ViewDownStock;
import com.fieldforce.harmonkardonff.View_Module_List;

import linq.ArrayList;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

public class DownStock  extends FragmentTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ArrayList<TabItem> setTabItems() {
        // TODO Auto-generated method stub
        ArrayList<TabItem> data = new ArrayList<TabItem>();
        data.add(new TabItem().setItem("Down Stock", SubmitDownStock.class));
        data.add(new TabItem().setItem("View Down Stock", ViewDownStock.class));


        // data.add(new TabItem().setItem("MTD Sale",SaleDisplayMTD.class));

        // data.add(new TabItem().setItem("LMTD Sale",SaleDisplayLMTD.class));
        return data;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class setTabClass() {
        // TODO Auto-generated method stub
        return SaleTabs.class;
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
