package com.grid;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.core.controls.Drawable;
import app.core.utils.GUID;

public class GridItemControl {

	Activity context;
	public ImageView Icon;
	public TextView Title;
	public RelativeLayout ItemLayout;
	private int columns = 3;
	Drawable drawable=new Drawable();

	public GridItemControl(Activity context) {
		this.context = context;
		getItemLayout();
	}

	public GridItemControl(Activity context, int columns) {
		this.context = context;
		this.columns = columns;
		getItemLayout();
	}

	private GridItemControl getItemLayout() {
		ItemLayout = new RelativeLayout(context);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		ItemLayout.setId(GUID.getId());
		ItemLayout.setLayoutParams(params);
		if (columns == 1)
			ItemLayout.setPadding(20, 20, 0,0);
		else
			ItemLayout.setPadding(0, getHeight(20), 0, getHeight(20));

		ItemLayout.addView(getImageView());
		ItemLayout.addView(getTextView());
		TryAddHorizontal();
		//TryAddVertical();
		return this;

	}
    
	private void TryAddHorizontal()
	{
		if (columns != 1) return;
		
		View v = new View(context);
		RelativeLayout.LayoutParams viewLp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 1);
		viewLp.addRule(RelativeLayout.BELOW, Icon.getId());
		viewLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		viewLp.setMargins(0, 20, 0, 0);

		v.setLayoutParams(viewLp);
		v.setBackgroundColor(drawable.gray());
		
		ItemLayout.addView(v);
	}
	private void TryAddVertical()
	{
		if(columns<2)return;
		View v = new View(context);
		RelativeLayout.LayoutParams viewLp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 1);
		viewLp.addRule(RelativeLayout.BELOW, Title.getId());
		viewLp.addRule(RelativeLayout.CENTER_HORIZONTAL);

		viewLp.setMargins(0,5,0, 0);

		v.setLayoutParams(viewLp);
		v.setBackgroundColor(drawable.getList_divider());
		
		ItemLayout.addView(v);
	}
	private ImageView getImageView() {

		Icon = new ImageView(this.context);
		Icon.setId(GUID.getId());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				getWidth(100), getHeight(100));
		if (columns == 1)
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		else
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		Icon.setLayoutParams(params);
		return Icon;
	}
	public int getHeight(int weight) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		long height = displaymetrics.heightPixels;
		long percetage = (weight * 100) / 1200;

		return (int) (height * percetage) / 100;
	}
	public int getWidth(int weight) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		long width = displaymetrics.widthPixels;
		long percetage = (weight * 100) / 800;

		return (int) (width * percetage) / 100;
	}
	private TextView getTextView() {

		Title = new TextView(this.context);
		Title.setId(GUID.getId());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (columns == 1) {
			params.addRule(RelativeLayout.RIGHT_OF, Icon.getId());
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			Title.setPadding(20, 0, 0, 0);
		} else {
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.addRule(RelativeLayout.BELOW, Icon.getId());
		}
		Title.setLayoutParams(params);
		Title.setTypeface(null, Typeface.BOLD);
		return Title;
	}
}
