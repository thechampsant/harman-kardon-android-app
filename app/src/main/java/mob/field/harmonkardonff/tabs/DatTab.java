package mob.field.harmonkardonff.tabs;

import android.content.Intent;
import android.os.Bundle;

import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;

import linq.ArrayList;
import mob.field.harmonkardonff.fragments.PendingAttendance;
import mob.field.harmonkardonff.fragments.ViewAttendance;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

public class DatTab extends FragmentTabActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public Class setTabClass() {
		// TODO Auto-generated method stub
		return DatTab.class;
	}

	@Override
	public ArrayList<TabItem> setTabItems() {
		// TODO Auto-generated method stub
		ArrayList<TabItem> data= new ArrayList<TabItem>();
	//	data.add(new TabItem().setItem("Mark Attendance",AttendanceMark.class));
		data.add(new TabItem().setItem("View Attendance",ViewAttendance.class));
		data.add(new TabItem().setItem("Pending Attendance",PendingAttendance.class));
		
		return data;
	}

	@Override
	public void onBackPressed() {

		Intent i = new Intent(DatTab.this, MainActivity.class);

		// set the new task and clear flags
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
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
