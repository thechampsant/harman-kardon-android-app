package com.grid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.APIService.SliderAdapter;
import com.fieldforce.harmonkardonff.LoginActivity;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.utility.Helper;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import app.core.adapter.GenricAdapter;
import app.core.adapter.ICustomAdapter;
import app.core.base.InnosolsActivity;
import app.core.controls.GridViewControls;
import app.core.entitymodels.AppDetail;
import app.core.utils.GUID;
import app.core.utils.IntentFactory;
import linq.ArrayList;

public abstract class GridActivity extends InnosolsActivity {

    public GridView grd = null;
    @SuppressWarnings("rawtypes")
    public ArrayList<GridItem> data = new ArrayList<GridItem>();

    public RelativeLayout mLayout = null;
    private int gridBackground = 0;
    private int TitleColor = 0;
    private int GridColumns = 0;
    public int header = 0;
    private int footer = 0;
    private LayoutInflater mInflater;
    private String ColumnKey = "GRID_COLUMN_KEY_COUNT";
    private boolean IsApplicationGrid = false;

    private PackageManager manager;
    private ArrayList<AppDetail> apps;
    public View headerview;
    public View footerview;

    @SuppressWarnings("rawtypes")
    public abstract ArrayList<GridItem> setGridItems();

    public abstract void Activate();

    public void setApplicationGrid() {
        this.IsApplicationGrid = true;
    }

    public int setGridbackground() {
        return 0;
    }

    ;

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

    public PackageManager getAppManager() {
        return this.manager;
    }

    public ArrayList<AppDetail> getAppDetails() {
        return this.apps;
    }

    private int getColumns() {
        this.GridColumns = ToInt(getSharedPreference(ColumnKey, "3"));
        return GridColumns;
    }

    public abstract int setGridHeader();

    public abstract int setGridFooter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Activate();

            loadNessacarryDataIfApplciationGrid();
            getGridItemData();
            InitilizeGVC();
            getDataFromAbstract();

            InitilizeCustomGridLayout();
            mLayout.setBackgroundResource(R.drawable.bg_harman);
            setContentView(mLayout);

            setGridList();
        } catch (Exception ex) {
            ShowToastLong(ex.getMessage(), 0);
            this.finish();
        }
    }

    private void loadNessacarryDataIfApplciationGrid() {
        if (!this.IsApplicationGrid)
            return;
        loadApps();
    }

    private void loadApps() {
        manager = getPackageManager();
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
        this.mInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private GridViewControls InitilizeGVC() {
        GridViewControls GVC = new GridViewControls(this, getColumns());
        grd = GVC.gridview;
        grd.setHorizontalSpacing((int) Helper.getSizeInDp(this, 20));
        grd.setVerticalSpacing((int) Helper.getSizeInDp(this, 20));
        mLayout = GVC.gridlayout;
        return GVC;
    }

    public boolean IsDataAvilable() {
        if (data == null)
            return false;
        else if (data.Count() < 1)
            return false;
        else
            return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setGridList() {
        // TODO Auto-generated method stub

        if (!IsDataAvilable()) {
            Toast.makeText(this, "No items found to display", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        GenricAdapter adapter = new GenricAdapter(this, R.layout.layout_gv_home).setData(data);


        adapter.setCustomAdapter(new ICustomAdapter() {

            @Override
            public View setItemView(Object arg0, int index) {
                // TODO Auto-generated method stub
                GridItem item = (GridItem) arg0;

                GridItemControl GIC = new GridItemControl(GridActivity.this,
                        GridColumns);

                GIC.Title.setText(item.getTitle());
                if (TitleColor != 0)
                    GIC.Title.setTextColor(TitleColor);
                if (item.IsShowIcon()) {
                    GIC.Icon.setImageResource(item.getIcon());
                    GIC.Icon.setVisibility(View.VISIBLE);
                } else if (item.IsShowIconDrawable()) {
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
    public void gotToItemActivity(GridItem item) {

        try {
            if (item.isApplication()) {
                openApplciation(item.getAppIndex());
                return;
            }
            if (item.IsMethodCallable()) {
                item.func.call();
                return;
            }
            if (!item.IsActivityExists())
                ShowToast("No activity associated with " + item.getTitle());
            else {
                IntentFactory.putDataTab(item.getInitialNav());
                Intent i = new Intent(this, item.getActivityClass());
                i.putExtra("title", item.getTitle().toString());
                if (item.getBundle() != null) {
                    i.putExtras(item.getBundle());
                }
                this.startActivity(i);
                ShowToast(item.getTitle());

                if (item.isfinishable())
                    this.finish();
            }
        } catch (Exception ex) {
            Log.e("GRID_ACTIVITY", ex.getMessage());
        }
    }

    private void openApplciation(int position) {
        Intent i = manager.getLaunchIntentForPackage(apps.get(position).name.toString());
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

    @SuppressWarnings("static-access")
    public void TrysetGridHeader() {

        if (header != 0) {
            headerview = this.mInflater.inflate(header, null);
            if (headerview == null)
                return;
            headerview.setId(GUID.getId());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.addRule(mLayout.ALIGN_PARENT_TOP);
            headerview.setLayoutParams(params);
            // headerview.setPadding(0, 0, 0, 10);
            mLayout.addView(headerview);
            RelativeLayout.LayoutParams gdparams = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            gdparams.addRule(RelativeLayout.BELOW, headerview.getId());
            gdparams.setMargins(0, (int) Helper.getSizeInDp(this, 10), 0, 0);
            grd.setLayoutParams(gdparams);

        }
    }


    public void TrysetGridFooter() {
        footerview = null;
        if (footer != 0) {
            footerview = this.mInflater.inflate(footer, null);
            if (footerview == null)
                return;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, mLayout.getId());
            footerview.setLayoutParams(params);
            footerview.setPadding(0, 10, 0, 10);
           //TrysetGridFooterSlider(footerview);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) grd.getLayoutParams();
            layoutParams.setMargins(0, 10, 0,  170);
            grd.setLayoutParams(layoutParams);
            mLayout.addView(footerview);

        } else
            grd.setPadding(0, 0, 0, 0);
    }


    private void TrysetGridFooterSlider(View footerview) {
        try {
            SliderView sliderView;
            int[] images = {R.drawable.imageone, R.drawable.imagetwo};
            if (footer != 0) {
            /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, mLayout.getId());
            footerview.setLayoutParams(params);
            footerview.setPadding(0, 20, 0, 0);*/
                sliderView=footerview.findViewById(R.id.slider);
                if (sliderView == null)
                    return;;

                SliderAdapter adapter = new SliderAdapter(GridActivity.this, images);

                // below method is used to set auto cycle direction in left to
                // right direction you can change according to requirement.
                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

                // below method is used to
                // setadapter to sliderview.
                sliderView.setSliderAdapter(adapter);

                // below method is use to set
                // scroll time in seconds.
                sliderView.setScrollTimeInSec(3);

                // to set it scrollable automatically
                // we use below method.
                sliderView.setAutoCycle(true);

                // to start autocycle below method is used.
                sliderView.startAutoCycle();
                sliderView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(GridActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                });
                mLayout.addView(footerview);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
