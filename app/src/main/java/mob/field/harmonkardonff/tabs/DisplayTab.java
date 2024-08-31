package mob.field.harmonkardonff.tabs;

import linq.ArrayList;
import mob.field.harmonkardonff.fragments.DisplayEnterForm;
import mob.field.harmonkardonff.fragments.DisplayEnters;
import mob.field.harmonkardonff.fragments.ViewDisplay;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;
import android.os.Bundle;

import com.fieldforce.harmonkardonff.R;

public class DisplayTab extends FragmentTabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public ArrayList<TabItem> setTabItems() {
		// TODO Auto-generated method stub
		ArrayList<TabItem> data = new ArrayList<TabItem>();
		data.add(new TabItem().setItem("Enter Display" , DisplayEnterForm.class));
		data.add(new TabItem().setItem("Todays Display", DisplayEnters.class));
		data.add(new TabItem().setItem("View Display", ViewDisplay.class));
		return data;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class setTabClass() {
		// TODO Auto-generated method stub
		return DisplayTab.class;
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
	@Override
	public void onTabChanged(String tag) {
		
		super.onTabChanged(tag);
		
		refreshDataInFragment(tag);
		
	
		
	}
	
	private void refreshDataInFragment(String tag) {
		if(tag.equalsIgnoreCase("Todays Display"))
		{
		Reinitialiseenterdisplay();
		}
	}

	private void Reinitialiseenterdisplay() {
		DisplayEnters.getDisplayEnterObj().hitapifordisplay();
		
	}

}
