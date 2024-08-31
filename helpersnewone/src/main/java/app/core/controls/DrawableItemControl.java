package app.core.controls;




import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.core.utils.GUID;

public class DrawableItemControl {

	private Activity context;
	public RelativeLayout ItemLayout;
	public ImageView Icon;
	public TextView Title;
	public TextView Count;
	private boolean IsImageFound;
	Drawable drawable = new Drawable();

	public DrawableItemControl(Activity context, boolean IsImageFound) {
		this.context = context;
		this.IsImageFound = IsImageFound;
	}

	public DrawableItemControl getDrawerListItemLayout() {
		ItemLayout = new RelativeLayout(context);
		ItemLayout.setId(GUID.getId());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, 70);
		ItemLayout.setLayoutParams(params);

		ItemLayout.addView(getImageView());
		ItemLayout.addView(getTitleTextView());
		ItemLayout.addView(getCountTextView());

		return this;

	}

	private ImageView getImageView() {

		Icon = new ImageView(this.context);
		Icon.setId(GUID.getId());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				35, 35);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.leftMargin = 12;
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		Icon.setLayoutParams(params);

		return Icon;
	}

	private TextView getTitleTextView() {
		Title = new TextView(context);
		Title.setId(GUID.getId());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		// testing
		int iconId = Icon.getId();
		Log.e("Icon Id :-", "" + iconId);

		params.leftMargin = 24;

		params.addRule(RelativeLayout.RIGHT_OF, iconId);
		params.addRule(RelativeLayout.CENTER_VERTICAL, ItemLayout.getId());
		Title.setLayoutParams(params);
		Title.setTextSize(18);
		Title.setTextColor(drawable.getList_item_title_Color());

		return Title;
	}

	private TextView getCountTextView() {
		Count = new TextView(context);
		Count.setId(GUID.getId());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		params.rightMargin = 8;
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, ItemLayout.getId());
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		Count.setLayoutParams(params);
		Count.setBackground(drawable.getcounter_text_bg());
		// tv.setMinHeight(minHeight);
		Count.setTextColor(drawable.getcounter_text_color());

		return Count;

	}

}
