package slidemenu;

import linq.ArrayList;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

/*import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;*/
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import app.core.controls.DrawerControl;
import app.core.model.NavDrawerItem;

public abstract class SlideMenuActivity  extends FragmentActivity {

	public abstract ArrayList<NavDrawerItem> setNavbarItems();
	public abstract int setNavbarIcon();

	public abstract int setOpenDrawerTitle();
	public abstract boolean onCreateOptionsMenu(Menu menu);

	public abstract int setClosedDrawerTitle();
	public DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerControl control=null;
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		control=new DrawerControl(this).getDrawerLayout();
		this.setContentView(control.mDrawerLayout);
		InitilizeNavbar(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	private void Init() {
		//mTitle = mDrawerTitle = getTitle();		
		mDrawerLayout=control.mDrawerLayout;
		mDrawerList=control.mDrawerList;
		
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	public void InitilizeNavbar(Bundle savedInstanceState)
	{
		Init();
		AddNavItems();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		setAdapter();

		// enabling action bar app icon and behaving it as toggle button
		try
		{
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		}
		catch(Exception ex){}
		
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setHomeButtonEnabled(true);
	
		InitilizeNavToggle();
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayFragmentView(navDrawerItems.get(0).getFragmentClass(),
					0);
		}
	}
	private void InitilizeNavToggle()
	{
		Toolbar toolbar = new Toolbar(this);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				toolbar,
				//setNavbarIcon(), // nav menu toggle icon
				setOpenDrawerTitle(), // nav drawer open - description for
										// accessibility
				setClosedDrawerTitle() // nav drawer close - description for
										// accessibility
		)
		{
			@SuppressLint("NewApi")
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			@SuppressLint("NewApi")
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
	}



	private void AddNavItems() {
		navDrawerItems = new ArrayList<NavDrawerItem>();
		navDrawerItems=this.setNavbarItems();
	}

	
	private void setAdapter() {
		adapter = new NavDrawerListAdapter(this,navDrawerItems);
		mDrawerList.setAdapter(adapter);
	}
	

	/**
	 * ment Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayFragmentView(
					navDrawerItems.get(position).getFragmentClass(), position);
		}
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case 1:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(1).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
    public void inflate(int id,Menu menu)
    {
    	getMenuInflater().inflate(id, menu);
    	menu.add(0, 1, Menu.NONE, "Nav");
    	
    }
	
	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	@SuppressLint("NewApi")
	private void displayFragmentView(Class<IFragment> FragmentClass, int position) {

		try {
			// update the main content by replacing fragments
			Fragment fragment = null;
			fragment =FragmentClass.newInstance();

			if (fragment != null) {
				FragmentManager fragmentManager =getSupportFragmentManager();
						//getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(control.frLayout.getId(), fragment).commit();
				

				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				setTitle(navDrawerItems.get(position).getTitle());
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				// error in creating fragment
				Log.e("MainActivity", "Error in creating fragment");
			}
		} catch (Exception ex) {
			Log.e("Raising Fragment Class", ex.getMessage());
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
