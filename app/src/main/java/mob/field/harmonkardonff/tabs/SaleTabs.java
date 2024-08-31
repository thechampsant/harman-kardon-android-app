package mob.field.harmonkardonff.tabs;

import linq.ArrayList;
import mob.field.harmonkardonff.fragments.SaleDisplay;
import mob.field.harmonkardonff.fragments.SaleEnter;
import mob.field.harmonkardonff.fragments.viewsale;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;
import android.os.Bundle;

import com.fieldforce.harmonkardonff.R;

public class SaleTabs extends FragmentTabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public ArrayList<TabItem> setTabItems() {
		// TODO Auto-generated method stub
		ArrayList<TabItem> data = new ArrayList<TabItem>();
		data.add(new TabItem().setItem("Enter Sale", SaleEnter.class));
		data.add(new TabItem().setItem("Pending Sale", SaleDisplay.class));
		data.add(new TabItem().setItem("View Sale", viewsale.class));

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
