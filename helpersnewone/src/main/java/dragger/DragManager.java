package dragger;

import android.app.Activity;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import app.core.base.IFragment;
import app.core.base.InnosolsActivity;

public class DragManager implements OnTouchListener {

	private View selected_item = null;
	private int offset_x = 0;
	private int offset_y = 0;
	private Boolean touchFlag = false;
	// private boolean dropFlag = false;
	private LayoutParams params;
	private View view;
	private int crashX, crashY;
	//private int topy, leftX, rightX, bottomY;
	private Activity context;
	private IDragUpdater dragUpdater;
	private boolean IsUpdating = false;


	public DragManager(Activity context) {
		this.context = context;
	}

	public void setContainer(View Item) {
		view = Item;
		mListView = (ListView) view;
		mListView.setOnTouchListener(this);
		params = mListView.getLayoutParams();
	}

	public void setOnDragUpdater(IDragUpdater dragauto) {
		this.dragUpdater = dragauto;

	}

	public void setLoading(boolean IsLoading) {
		this.IsUpdating = IsLoading;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			touchFlag = true;
			offset_x = (int) event.getX();
			offset_y = (int) event.getY();
			selected_item = v;
			//onTouchOccur(v, event);
			return true;
		case MotionEvent.ACTION_MOVE:
			onTouchOccur(v, event);
			return true;
		case MotionEvent.ACTION_UP:
			offset_x = (int) event.getX();
			offset_y = (int) event.getY();
			selected_item = v;
			touchFlag = false;
			selected_item.setLayoutParams(params);
			return false;

		default:
			return false;
		}
	}

	private boolean onTouchOccur(View v, MotionEvent event) {
		if (touchFlag == true) {
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
//				topy = view.getTop();
//				leftX = view.getLeft();
//				rightX = view.getRight();
//				bottomY = view.getBottom();
				selected_item.setLayoutParams(params);
				break;
			case MotionEvent.ACTION_MOVE:
				crashX = (int) event.getX();
				crashY = (int) event.getY();
				if (crashY > 200) {
					getDownChild(event);
					if (!IsUpdating && mDownPosition < 4) {
						IsUpdating = true;
						dragUpdater.onDrag();
					}
					selected_item.setLayoutParams(params);
					break;
				} else {
					MoveToPoint(event);
				}
				break;
			default:
				break;
			}
		} else {
			System.err.println("Display Else Part ::->" + touchFlag);
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	private void MoveToPoint(MotionEvent event) {
		int x = (int) event.getX() - offset_x;
		int y = (int) event.getY() - offset_y;
		int w = context.getWindowManager().getDefaultDisplay().getWidth() - 50;
		int h = context.getWindowManager().getDefaultDisplay().getHeight() - 10;
		if (x > w)
			x = w;
		if (y > h)
			y = h;
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				new ViewGroup.MarginLayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT));
		lp.setMargins(0, y + 50, 0, 0);
		selected_item.setLayoutParams(lp);
	}

	android.widget.ListView mListView = null;
	private int mDownPosition = 0;

	private void getDownChild(MotionEvent motionEvent) {
		View mDownView = null;
		Rect rect = new Rect();
		int childCount = mListView.getChildCount();
		int[] listViewCoords = new int[2];
		mListView.getLocationOnScreen(listViewCoords);
		int x = (int) motionEvent.getRawX() - listViewCoords[0];
		int y = (int) motionEvent.getRawY() - listViewCoords[1];
		View child;
		for (int i = 0; i < childCount; i++) {
			child = mListView.getChildAt(i);
			child.getHitRect(rect);
			if (rect.contains(x, y)) {
				mDownView = child; // This is your down view
				break;
			}
		}
		if (mDownView != null && mListView != null && touchFlag) {
			mDownPosition = mListView.getPositionForView(mDownView);
		}
	}

}
