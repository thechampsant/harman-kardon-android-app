package com.fieldforce.harmonkardonff;

import linq.ArrayList;
import gridmenu.GridActivity;
import gridmenu.GridItem;

public class Helpdesk extends GridActivity {


	@Override
	public ArrayList<GridItem> setGridItems() {
		ArrayList<GridItem> data = new ArrayList<GridItem>();

		data.add(new GridItem().setItem("Complain", ComplainActivity.class,
				R.drawable.complaint));
		data.add(new GridItem().setItem("Feedback", FeedbackActivity.class,
				R.drawable.feedback_new));
		
		return data;
	}

	@Override
	public void Activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setGridHeader() {
		// TODO Auto-generated method stub
		return R.layout.activity_helpdesk;
	}

	@Override
	public int setGridFooter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

}
