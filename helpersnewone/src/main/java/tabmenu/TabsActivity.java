package tabmenu;

import linq.ArrayList;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import app.core.controls.TabControl;
import app.core.model.Keys;
import app.core.utils.IntentFactory;

@SuppressWarnings("deprecation")
public abstract class TabsActivity extends TabActivity {

	public abstract ArrayList<TabItem> setTabItems();

	private TabControl tc = null;
	private ArrayList<TabItem> Items = new ArrayList<TabItem>();

	@SuppressWarnings("rawtypes")
	public abstract Class setTabClass();

	

	@SuppressWarnings("rawtypes")
	private Class getTabClass() {
		return setTabClass();
	}


	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private boolean IsTabFound() {
		if (Items == null)
			return false;
		else if (Items.Count() < 1)
			return false;
		else
			return true;
	}

	private void setItems() {
		Items = setTabItems();
		if (IsTabFound()) {
			for (TabItem item : Items) {
				tc.tabhost.addTab(addTab(item));
			}
		} else {
			showToast("No tabs found to display!");
		}
	}

	private int Invoke = 0;

	private TabSpec addTab(TabItem item) {
		TabSpec tab = tc.tabhost.newTabSpec(item.getTitle());
		if (item.IsShowIcon()) {
			Drawable icon = getResources().getDrawable(item.getIcon());
			tab.setIndicator(item.getTitle(), icon);
		} else {
			tab.setIndicator(item.getTitle(), null);
		}
		if (item.tabInvoke())
			Invoke = Items.indexOf(item);
		if (item.IsActivityExists()) {
			Intent intent = new Intent(this, item.getActivityClass());
			tab.setContent(intent);
		}
		return tab;
	}

	private void trySetCurrentTab() {
		int fIndex = IntentFactory.getDataTab();
		if (IsTabFound()) {
			if (fIndex > -1)
				this.tc.tabhost.setCurrentTab(fIndex);
			else if (Invoke > -1)
				this.tc.tabhost.setCurrentTab(Invoke);
			else
				this.tc.tabhost.setCurrentTab(0);
		}
	}

	private void handleTabClass() {
		if (getTabClass() == null)
			this.showToast("Tab class is missing!Please set tab class");
		else
			IntentFactory.putDataAsClass(Keys.TabItemClass, getTabClass());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tc = new TabControl(this, getTabHost());
		handleTabClass();
		setItems();
		trySetCurrentTab();

	}

}
