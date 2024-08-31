package com.fieldforce.harmonkardonff;

import linq.ArrayList;
import mob.field.harmonkardonff.quiz.QuizDisplayActivity;
import gridmenu.GridActivity;
import gridmenu.GridItem;

public class TrainningModule extends GridActivity {


	@Override
	public ArrayList<GridItem> setGridItems() {
		ArrayList<GridItem> data = new ArrayList<GridItem>();

		data.add(new GridItem().setItem("My quiz", QuizDisplayActivity.class,
				R.drawable.quiz_new));
		data.add(new GridItem().setItem("Product Info",
				PresentationActivity.class, R.drawable.posm_new));
		return data;
	}

	@Override
	public void Activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public int setGridHeader() {
		// TODO Auto-generated method stub
		return R.layout.activity_training;
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
