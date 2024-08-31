package mob.field.harmonkardonff.tabs;

import linq.ArrayList;
import mob.field.harmonkardonff.fragments.HeroCTNs;
import mob.field.harmonkardonff.fragments.NoStockCTNs;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

import com.fieldforce.harmonkardonff.R;

public class CTNTabs extends  FragmentTabActivity {

	@Override
	public int setFragmentLayout() {
		// TODO Auto-generated method stub
		return R.layout.tab_fragment;
	}

	@Override
	public Class setTabClass() {
		// TODO Auto-generated method stub
		return CTNTabs.class;
	}

	@Override
	public ArrayList<TabItem> setTabItems() {
		// TODO Auto-generated method stub
		ArrayList<TabItem> data= new ArrayList<TabItem>();
		data.add(new TabItem().setItem("No Stock CTN",NoStockCTNs.class));
		data.add(new TabItem().setItem("Hero CTN",HeroCTNs.class));
		return data;
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub
		
	}

}
