package mob.field.harmonkardonff.tabs;

import mob.field.harmonkardonff.fragments.StockDisplay;
import mob.field.harmonkardonff.fragments.StockEnter;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

import com.fieldforce.harmonkardonff.R;


public class StockTabs extends FragmentTabActivity {

    @Override
    public linq.ArrayList<TabItem> setTabItems()
    {
        linq.ArrayList<TabItem> data = new linq.ArrayList<TabItem>();
        data.add(new TabItem().setItem("Enter Stock", StockEnter.class));
        data.add(new TabItem().setItem("View Stock", StockDisplay.class));
        return data;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class setTabClass() {
        return StockTabs.class;
    }

    @Override
    public int setFragmentLayout() {
        return R.layout.tab_fragment;
    }


    @Override
    public void RegisterTableInfoForLocalDB() {


    }

}
