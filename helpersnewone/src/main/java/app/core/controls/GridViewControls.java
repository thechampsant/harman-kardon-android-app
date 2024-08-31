package app.core.controls;

import android.app.Activity;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import app.core.utils.GUID;

public class GridViewControls {

    Activity context;
    public RelativeLayout gridlayout;
    public GridView gridview;
    Drawable drawables = new Drawable();
    HorizontalScrollView scrollview;
    int gridcolumns = 3;


    public GridViewControls(Activity context, int gridcolumns) {
        this.context = context;
        this.gridcolumns = gridcolumns;

        getGridLayout();
    }

    private RelativeLayout getGridLayout() {
        gridlayout = new RelativeLayout(context);
        gridlayout.setId(GUID.getId());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        gridlayout.setLayoutParams(params);


//        gridlayout.setPadding(20,20,20,20);
        gridlayout.addView(getGridView());

        return gridlayout;
    }

    private HorizontalScrollView getHorizantalScrollView() {
        scrollview = new HorizontalScrollView(this.context);
        scrollview.setId(GUID.getId());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        scrollview.setLayoutParams(params);
        scrollview.setFillViewport(true);
        scrollview.setHorizontalScrollBarEnabled(true);


        scrollview.addView(getGridView());

        return scrollview;
    }

    private GridView getGridView() {
        gridview = new GridView(this.context);

//		gridlayout.setId(GUID.getId());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        params.setMargins(10, 10, 10, 0);
        gridview.setLayoutParams(params);
//        gridview.setPadding(20, 20, 20, 10);
        gridview.setNumColumns(gridcolumns);


		
		
		/*gridview.setSelector(drawable.getList_selector());
        gridview.setBackgroundColor(drawable.getList_background());*/


        return gridview;

    }


}
