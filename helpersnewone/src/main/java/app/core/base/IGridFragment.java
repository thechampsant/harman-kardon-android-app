package app.core.base;


import java.util.List;

import gridmenu.GridItem;
import linq.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import app.core.adapter.GenricAdapter;
import app.core.adapter.ICustomAdapter;
import app.core.controls.GridItemControl;
import app.core.controls.GridViewControls;
import app.core.entitymodels.AppDetail;
import app.core.utils.GUID;
import app.core.utils.IntentFactory;

public abstract  class IGridFragment extends IFragment {

	GridView grd = null;
	@SuppressWarnings("rawtypes")
	ArrayList<GridItem> data = new ArrayList<GridItem>();;
	public RelativeLayout mLayout = null;
	private int gridBackground = 0;
	private int TitleColor = 0;
	private int GridColumns = 0;
	private int header = 0;
	private int footer = 0;
	private LayoutInflater mInflater;
	private String ColumnKey = "GRID_COLUMN_KEY_COUNT";
	
	private boolean IsApplicationGrid = false;

	private PackageManager manager;
	private ArrayList<AppDetail> apps;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Activate();
		Invoke((InnosolsActivity)this.getActivity(),inflater);
		InitilizeGVC();
		getDataFromAbstract();
		loadNessacarryDataIfApplciationGrid();
		getGridItemData();
		InitilizeCustomGridLayout();
		setGridList();
		this.mView=this.mLayout;
		Activate(this.mView);
		return this.mLayout;
	}

	private void loadNessacarryDataIfApplciationGrid() {
		if (!this.IsApplicationGrid)
			return;
		loadApps();
	}
	private void loadApps() {
		manager = context.getPackageManager();
		apps = new ArrayList<AppDetail>();

		Intent i = new Intent(Intent.ACTION_MAIN, null);
		i.addCategory(Intent.CATEGORY_LAUNCHER);

		List<ResolveInfo> availableActivities = manager.queryIntentActivities(
				i, 0);
		for (ResolveInfo ri : availableActivities) {
			AppDetail app = new AppDetail();
			app.label = ri.loadLabel(manager);
			app.name = ri.activityInfo.packageName;
			app.icon = ri.activityInfo.loadIcon(manager);
			apps.add(app);
		}
	}
	@SuppressWarnings("rawtypes")
	public abstract ArrayList<GridItem> setGridItems();

	public int setGridbackground() {
		return 0;
	};

	public int setGridItemTitleColor() {
		return 0;
	}

	public int setGridColumns() {
		return 0;
	}

	public void setGridColumns(int columns) {

		if (columns == 0)
			return;
		this.SavePreferences(ColumnKey, ToString(columns));
		this.GridColumns = columns;
		InitilizeCustomGridLayout();
		setGridList();

	}

	private int getColumns() {
		this.GridColumns = ToInt(getSharedPreference(ColumnKey, "2"));
		return GridColumns;
	}

	public abstract int setGridHeader();

	public void Activate(){};
	public abstract int setGridFooter();

	public void setApplicationGrid() {
		this.IsApplicationGrid = true;
	}
	public PackageManager getAppManager() {
		return this.manager;
	}
	public ArrayList<AppDetail> getAppDetails() {
		return this.apps;
	}
	private void getGridItemData() {
		data = setGridItems();
	}
	private void getDataFromAbstract() {
		gridBackground = setGridbackground();
		TitleColor = setGridItemTitleColor();
		if (setGridColumns() != 0)
			GridColumns = setGridColumns();
		
		header = setGridHeader();
		footer = setGridFooter();
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	private GridViewControls InitilizeGVC() {
		GridViewControls GVC = new GridViewControls(context, getColumns());
		grd = GVC.gridview;
		mLayout = GVC.gridlayout;
		return GVC;
	}

	private boolean IsDataAvilable() {
		if (data == null)
			return false;
		else if (data.Count() < 1)
			return false;
		else
			return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setGridList() {
		// TODO Auto-generated method stub

		if (!IsDataAvilable()) {
			Toast.makeText(context, "No items found to display", Toast.LENGTH_LONG)
					.show();
			return;
		}
		GenricAdapter adapter = new GenricAdapter(context).setData(data);

		adapter.setCustomAdapter(new ICustomAdapter() {

			@Override
			public View setItemView(Object arg0,int index) {
				// TODO Auto-generated method stub
				GridItem item = (GridItem) arg0;

				GridItemControl GIC = new GridItemControl(context,
						GridColumns);

				GIC.Title.setText(item.getTitle());
				if (TitleColor != 0)
					GIC.Title.setTextColor(TitleColor);
				if (item.IsShowIcon()) {
					GIC.Icon.setImageResource(item.getIcon());
					GIC.Icon.setVisibility(View.VISIBLE);
				} else
					GIC.Icon.setVisibility(View.GONE);
				
				if (item.IsShowIconDrawable()) {
					GIC.Icon.setImageDrawable(item.getIconDrawable());
					GIC.Icon.setVisibility(View.VISIBLE);
				} else
					GIC.Icon.setVisibility(View.GONE);

				return GIC.ItemLayout;
			}
		});
		grd.setAdapter(adapter);

		grd.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				gotToItemActivity(data.get(arg2));
			}
		});

	}

	@SuppressWarnings("rawtypes")
	private void gotToItemActivity(GridItem item) {

		try {
			if (item.isApplication()) {
				openApplciation(item.getAppIndex());
				return;
			}
			if (item.IsMethodCallable())
				item.func.call();
			if (!item.IsActivityExists())
				ShowToast("No activity associated with " + item.getTitle());
			else {
				IntentFactory.putDataTab(item.getInitialNav());
				Intent i = new Intent(context, item.getActivityClass());
				this.startActivity(i);
				ShowToast(item.getTitle());

				if (item.isfinishable())
					context.finish();
			}
		} catch (Exception ex) {
			Log.e("GRID ACTIVITY", ex.getMessage());
		}
	}
	private void openApplciation(int position) {
		Intent i = manager.getLaunchIntentForPackage(apps.get(position).name
				.toString());
		this.startActivity(i);
	}
	private void InitilizeCustomGridLayout() {

		if (GridColumns != 0)
			grd.setNumColumns(GridColumns);
		if (this.gridBackground != 0)
			mLayout.setBackgroundResource(gridBackground);

		this.TrysetGridHeader();
		this.TrysetGridFooter();

	}

	private void TrysetGridHeader() {
		View headerview = null;
		if (header != 0) {
			headerview = this.mInflater.inflate(header, null);
			if (headerview == null)
				return;
			headerview.setId(GUID.getId());
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			params.addRule(mLayout.ALIGN_PARENT_TOP);
			headerview.setLayoutParams(params);
			//headerview.setPadding(0, 0, 0, 10);
			mLayout.addView(headerview);

			RelativeLayout.LayoutParams gdparams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			gdparams.addRule(RelativeLayout.BELOW, headerview.getId());
			gdparams.setMargins(0, 10, 0, 0);
			grd.setLayoutParams(gdparams);

		}
	}

	private void TrysetGridFooter() {
		View footerview = null;
		if (footer != 0) {
			footerview = this.mInflater.inflate(footer, null);
			if (footerview == null)
				return;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, mLayout.getId());
			footerview.setLayoutParams(params);
			footerview.setPadding(0, 10, 0, 10);
			mLayout.addView(footerview);
		}
		else
			grd.setPadding(0, 0, 0, 0);
	}
	
	
	

}
