package tab.fragments;

import linq.ArrayList;
import tab.fragments.TabFactory;
import tabmenu.TabItem;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;


import androidx.viewpager.widget.ViewPager;

import com.example.helpers.newone.R;

import app.core.base.IFragment;
import app.core.base.IFragmentClickable;
import app.core.base.InnosolsActivity;
import app.core.model.Keys;
import app.core.utils.IntentFactory;

public abstract class FragmentTabActivity extends InnosolsActivity implements
		OnTabChangeListener, ViewPager.OnPageChangeListener {

	TabPageAdapter pageAdapter;
	private ViewPager mViewPager;
	private TabHost mTabHost;
	private LayoutInflater minfate;
	private HorizontalScrollView hrview;
	private View view;

	private ArrayList<TabItem> Items = new ArrayList<TabItem>();

	public abstract ArrayList<TabItem> setTabItems();

	@SuppressWarnings("rawtypes")
	public abstract Class setTabClass();

	public abstract int setFragmentLayout();

	private void getTabItems() {
		this.Items = this.setTabItems();
	}

	@SuppressWarnings("rawtypes")
	private Class getTabClass() {
		return setTabClass();
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private View getInflatedView() {
		if (this.setFragmentLayout() == 0) {
			showToast("Fragment layout is missing!");
			return null;
		}
		minfate = (LayoutInflater) this
				.getSystemService(this.LAYOUT_INFLATER_SERVICE);
		view = minfate.inflate(this.setFragmentLayout(), null);
		if (view == null) {
			showToast("unable to infate  the view!");
			return null;
		}
		return view;
	}

	private void getControlsFromLayout() {

		ViewGroup vgroup = ((ViewGroup) view);
		this.mTabHost = (TabHost) vgroup.getChildAt(0);
		vgroup = (ViewGroup) ((ViewGroup) mTabHost).getChildAt(0);
		this.hrview = (HorizontalScrollView) vgroup.getChildAt(0);
		this.mViewPager = (ViewPager) vgroup.getChildAt(2);

	}

	// private void setControlTabHost()
	// {
	// getTabItems();
	// this.mTabHost=IntentFactory.getData("TABPUSH");
	// FragmentTabControl tc=new FragmentTabControl(this,mTabHost);
	// this.mViewPager=tc.pager;
	// this.mTabHost=tc.tabhost;
	// //this.setContentView(tc.tabhost);
	// }
	private void handleTabClass() {
		if (getTabClass() == null)
			this.showToast("Tab class is missing!Please set tab class");
		else
			IntentFactory.putDataAsClass(Keys.TabItemClass, getTabClass());
	}

	private void trySetCurrentTab() {
		int fIndex = IntentFactory.getDataTab();
		if (IsTabFound()) {
			if (fIndex > -1)
				setTabFinally(fIndex);
			else if (Invoke > -1)
				setTabFinally(Invoke);
			else
				setTabFinally(0);
		}
	}

	public void setTabFinally(int index) {
		this.mTabHost.setCurrentTab(index);
		this.mViewPager.setCurrentItem(index);
	}

	private boolean IsTabFound() {
		if (Items == null)
			return false;
		else if (Items.Count() < 1)
			return false;
		else
			return true;
	}

	private void setResourceTabHost() {
		if (getInflatedView() != null) {
			getTabItems();
			getControlsFromLayout();
			setContentView(view);
		} else
			this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setResourceTabHost();
		// setControlTabHost();
		initialiseTabHost();

		// Fragments and ViewPager Initialization
		ArrayList<IFragment> fragments = getFragments();
		pageAdapter = new TabPageAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(pageAdapter);
		mViewPager.setOnPageChangeListener(FragmentTabActivity.this);
		handleTabClass();
		trySetCurrentTab();
	}

	// Tabs Creation
	private void initialiseTabHost() {
		mTabHost.setup();

		for (TabItem item : Items) {
			// test.AddTab(this,
			// this.mTabHost,this.mTabHost.newTabSpec(items.getTitle()));
			// mTabHost.addTab(mTabHost.newTabSpec("explore").setIndicator("Explore"));
			mTabHost.addTab(addTab(item));
		}
		mTabHost.setOnTabChangedListener(this);
	}

	int Invoke = -1;

	private TabSpec addTab(TabItem item) {
		TabSpec tab = mTabHost.newTabSpec(item.getTitle());
		if (item.IsShowIcon()) {
			Drawable icon = getResources().getDrawable(item.getIcon());
			tab.setIndicator(item.getTitle(), icon);
		} else {
			tab.setIndicator(item.getTitle());
		}

		if (item.tabInvoke())
			Invoke = Items.indexOf(item);
		if (item.IsActivityExists()) {
			// Intent intent = new Intent(this, item.getActivityClass());
			tab.setContent(new TabFactory(this));
		}
		return tab;
	}

	private ArrayList<IFragment> getFragments() {
		ArrayList<IFragment> data = new ArrayList<IFragment>();
		for (TabItem item : Items) {
			try {
				data.add((IFragment) item.getActivityClass().newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}

	// Manages the Tab changes, synchronizing it with Pages
	public void onTabChanged(String tag) {
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);

		final View tabView = mTabHost.getTabWidget().getChildTabViewAt(pos);
		final int[] locationOnScreen = new int[2];
		tabView.getLocationOnScreen(locationOnScreen);
		this.hrview.scrollTo(locationOnScreen[0], 0);
	}

	public ViewPager getPagerView() {
		return this.mViewPager;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// Manages the Page changes, synchronizing it with Tabs
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		int pos = this.mViewPager.getCurrentItem();
		this.mTabHost.setCurrentTab(pos);
	}

	@Override
	public void onPageSelected(int arg0) {
	}

}