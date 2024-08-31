package app.core.adapter;


import linq.ArrayList;
import android.app.Activity;
import android.content.Context;

/*import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;*/

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;



public class ImageAdapter<T> extends PagerAdapter {

	public Context context;
	private LayoutInflater mInflater;
	public int CustomListItemLayoutID = -1;
	private IAdapterImage mListener = null;
	public ArrayList<T> data;
	public View view;
	private int IsError = 0;
	
	public ImageAdapter(Activity activity, int LayoutID) {
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = new ArrayList<T>();
		CustomListItemLayoutID = LayoutID;
		context = activity;
	}
	public ImageAdapter(Activity activity,View _view) {
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = new ArrayList<T>();
		context = activity;
		view=_view;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public ImageAdapter setData(ArrayList<T> _data) {
		data = _data;
		return this;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.Count();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		try {
			container = (ViewGroup) mInflater.inflate(CustomListItemLayoutID, null);
			mListener.setItemView(this.data.get(position),container);
			return null;
		} catch (Exception ex) {
			IsError += 1;
			if (IsError < 2) {
				data = new ArrayList<T>();
				Toast.makeText(context,
						"setItemView suffering from invalid casting",
						Toast.LENGTH_LONG).show();
				Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG)
						.show();

				ex.printStackTrace();
			}
			return null;
		}
		//((ViewPager) container).addView(null);

	}


	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}
	@SuppressWarnings({ "rawtypes" })
	public ImageAdapter setImageAdapter(IAdapterImage eventListener) {
		mListener = eventListener;
		return this;
	}

}
