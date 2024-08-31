package mob.field.harmonkardonff.tabs;

import linq.ArrayList;
import mob.field.harmonkardonff.fragments.FootFallEnter;
import mob.field.harmonkardonff.fragments.FootfallDisplay;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

import com.fieldforce.harmonkardonff.R;

public class FootfallTab extends FragmentTabActivity {

	@Override
	public int setFragmentLayout() {
		return R.layout.tab_fragment;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class setTabClass() {
		return FootfallTab.class;
	}

	@Override
	public ArrayList<TabItem> setTabItems() {
		ArrayList<TabItem> data = new ArrayList<TabItem>();
		data.add(new TabItem().setItem("Enter Footfall", FootFallEnter.class));
		data.add(new TabItem().setItem("View Footfall", FootfallDisplay.class));
		return data;
	}

	@Override
	public void RegisterTableInfoForLocalDB() {

	}

}
