package app.core.controls;



import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
//import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ListView;


import androidx.drawerlayout.widget.DrawerLayout;

import app.core.utils.GUID;


public class DrawerControl {

	private Activity context;
	public ListView mDrawerList;
	public DrawerLayout mDrawerLayout;
	public FrameLayout frLayout;

	Drawable drawable= new Drawable();
	public DrawerControl(Activity context)
	{
		this.context=context;
	}

	public DrawerControl getDrawerLayout()
	{

		mDrawerLayout= new DrawerLayout(this.context);
		mDrawerLayout.setId(GUID.getId());
		DrawerLayout.LayoutParams params= 
				new DrawerLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		
		mDrawerLayout.setLayoutParams(params);
		
		mDrawerLayout.addView(getFrame());
		mDrawerLayout.addView(getDrawerList());
		
		
		
		return this;
	}
	
	private FrameLayout getFrame()
	{
		frLayout= new FrameLayout(this.context);
		frLayout.setId(GUID.getId());
		DrawerLayout.LayoutParams params= 
				new DrawerLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		frLayout.setLayoutParams(params);
		return frLayout;
		
	}
	private ListView getDrawerList()
	{
		mDrawerList= new ListView(this.context);
		mDrawerList.setId(GUID.getId());
		DrawerLayout.LayoutParams params= 
				new DrawerLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	
		
		params.gravity=Gravity.START;   /**************** 5 start field***************/
		mDrawerList.setLayoutParams(params);
		mDrawerList.setDividerHeight(1);
		mDrawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mDrawerList.setSelector(drawable.getList_selector());
     	mDrawerList.setBackgroundColor(drawable.getList_background());
     	mDrawerList.setDivider( new ColorDrawable(drawable.getList_divider()));
     	mDrawerList.setDividerHeight(2);
		return mDrawerList;
 
	
	}
	
	
}
