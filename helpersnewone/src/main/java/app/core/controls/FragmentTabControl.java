package app.core.controls;

import android.app.Activity;
//import android.support.v4.view.ViewPager;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;


import androidx.viewpager.widget.ViewPager;

import app.core.utils.GUID;


public class FragmentTabControl {
	private Activity context;
	public TabHost tabhost;
	public LinearLayout lyt;
	public TabWidget tabwidget;
	public FrameLayout frame;
	public HorizontalScrollView scrollview;
    public ViewPager pager;
	public FragmentTabControl(Activity context, TabHost tabhost) {
		this.context = context;
		this.tabhost = tabhost;
		// FrameLayout.LayoutParams params= new
		// FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		// this.tabhost.setLayoutParams(params);
		getTabHost();
	}

	private TabHost getTabHost() {
		// this.tabhost.setId(GUID.getId());
		this.tabhost.addView(this.getTabLayout());
		return this.tabhost;

	}

	private HorizontalScrollView getHorizantalScrollView() {
		scrollview = new HorizontalScrollView(this.context);
		scrollview.setId(GUID.getId());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		scrollview.setLayoutParams(params);
		scrollview.setFillViewport(true);
		scrollview.setHorizontalScrollBarEnabled(true);

		scrollview.addView(getTabWidget());

		return scrollview;
	}

	private LinearLayout getTabLayout() {
		lyt = new LinearLayout(this.context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lyt.setLayoutParams(params);
		lyt.setOrientation(LinearLayout.VERTICAL);

		lyt.addView(this.getHorizantalScrollView());
		lyt.addView(this.getframe());
		lyt.addView(this.pager);
		return lyt;

	}

	private TabWidget getTabWidget() {
		tabwidget = new TabWidget(this.context);
		tabwidget.setId(GUID.getId());
		tabwidget.setOrientation(LinearLayout.HORIZONTAL);
		tabwidget.setHorizontalScrollBarEnabled(true);
		tabwidget.setStripEnabled(true);
		// LinearLayout.LayoutParams params= new
		// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		// tabwidget.setLayoutParams(params);
		return tabwidget;
	}

	private FrameLayout getframe() {
		frame = new FrameLayout(context);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		frame.setLayoutParams(params);
		return frame;
	}
	@SuppressWarnings("deprecation")
	private ViewPager getViewPager()
	{
		pager= new ViewPager(context);
		ViewPager.LayoutParams params = new ViewPager.LayoutParams();
		params.width=LayoutParams.FILL_PARENT;
		params.height=LayoutParams.WRAP_CONTENT;
		
		pager.setLayoutParams(params);
		return pager;
		
	}
}
