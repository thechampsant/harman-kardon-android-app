package slidemenu;


import java.util.ArrayList;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import app.core.controls.DrawableItemControl;
import app.core.model.NavDrawerItem;


public class NavDrawerListAdapter extends BaseAdapter {

	private Activity context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter(Activity context,
			ArrayList<NavDrawerItem> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NavDrawerItem item = navDrawerItems.get(position);

		DrawableItemControl IC = new DrawableItemControl(context,item.getIsIconFound()).getDrawerListItemLayout();
		if (item.getIsIconFound())
			IC.Icon.setImageResource(item.getIcon());
		else
			IC.Icon.setVisibility(View.GONE);

		IC.Title.setText(item.getTitle());

		// displaying count
		// check whether it set visible or not
		if (item.getCounterVisibility()) {
			IC.Count.setText(item.getCount());
		} else {
			// hide the counter view
			IC.Count.setVisibility(View.GONE);
		}

		return IC.ItemLayout;
	}

}
