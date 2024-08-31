package app.core.adapter;
import linq.ArrayList;
import android.app.Activity;
import android.content.Context;

/*import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * 
 * @author Sandeep
 * 
 * @param <T>
 *            T can be any java class object.
 * @since It is inherited from BaseAdapter and have nested class PageAdapter
 *        which is inherited from PagerAdapter. This class can used to display
 *        custom ListView or dynamic listView and also viewpager.
 *        
 * @example
 *        GenricAdapter adaptor = new GenricAdapter(this.context, // Activity
  			R.layout.listitem_attendance	// View for List Item (Template)
				).setData(model.getAttendance()); // Model (List)
				
			adaptor.setGenricAdapter(new IAdapter<AttendanceModel>() {

			@Override
			public void setItemView(AttendanceModel arg0, View arg1, int index) {
				// TODO Auto-generated method stub
				MDAT info = (MDAT) arg0; 				// Not Required
				TextView ForDate = (TextView) arg1
						.findViewById(R.id.txt_fordate); // Date
				TextView Option = (TextView) arg1.findViewById(R.id.txt_option); // Count
				ImageView img = (ImageView) arg1.findViewById(R.id.imgl);// Image

				if (info.IsOfflineOnly.equalsIgnoreCase("false")) {
					img.setVisibility(View.INVISIBLE);
				}
				ForDate.setText(info.ForDate);
				Option.setText(info.option);
			}
		});
 * 
 */
public class GenricAdapter<T> extends BaseAdapter {

	// Injections
	public Context context;
	public ArrayList<T> data;
	private IAdapter<T> mListener = null;
	public int CustomListItemLayoutID = -1;		// Resource ID for List Item Template
	
	private ICustomAdapter<T> cListener = null;
	private LayoutInflater mInflater = null;
	private int IsError = 0;
	private boolean IsCustomView = false;
	private PageAdapter madapter = null;

	/**
	 * 
	 * @param activity
	 * @param LayoutID
	 *            is the xml resource id which you want to display as ListView
	 *            item
	 */
    public GenricAdapter(Activity activity, int LayoutID) {
		this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = new ArrayList<T>();
		
		CustomListItemLayoutID = LayoutID;
		context = activity;
		IsCustomView = false;
	}

	/**
	 * 
	 * @param activity
	 * @since As in this case no layout id being passed so you have to set
	 *        listener i.e. setCustomAdapter for displaying the dynamic controls
	 *        in ListView.
	 */
	public GenricAdapter(Activity activity) {
		data = new ArrayList<T>();
		context = activity;
		IsCustomView = true;
	}

    public void add(T object) {
		data.add(object);
		super.notifyDataSetChanged();
		/*
		this.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				notifyDataSetChanged();
			}
		});
		*/
	}

	/**
	 * 
	 * @param _data
	 *            will be consumed by the adaptor for rendering the ListView
	 *            item.
	 * @return current instance of adaptor for chaining the methods.
	 */
	@SuppressWarnings({ "rawtypes" })
	public GenricAdapter setData(ArrayList<T> _data) {
		this.data.clear();
		this.notifyDataSetChanged();
		for(T item : _data)
			this.add(item);
		return this;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			if (IsCustomView) {
				convertView = cListener.setItemView(this.data.get(position),
						position);
				return convertView;
			} else {
				convertView = mInflater.inflate(CustomListItemLayoutID, null);
				mListener.setItemView(this.data.get(position), convertView, position);
				return convertView;
			}
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
			return convertView;
		}
	}

	/**
	 * 
	 * @param eventListener
	 *            is responsible for calling your code written in within loop.
	 * @return current instance of adaptor for chaining the methods.
	 */
	@SuppressWarnings({ "rawtypes" })
	public GenricAdapter setGenricAdapter(IAdapter<T> eventListener) {
		mListener = eventListener;
		return this;
	}

	/**
	 * 
	 * @param eventListener
	 *            is used when your ListView item controls is dynamic or not
	 *            fixed.
	 * @since In this case you no need to pass the LayoutID in object
	 *        initiation.
	 * @return current instance of adaptor for chaining the methods.
	 */
	@SuppressWarnings({ "rawtypes" })
	public GenricAdapter setCustomAdapter(ICustomAdapter<T> eventListener) {
		cListener = eventListener;
		this.IsCustomView = true;
		return this;
	}

	/**
	 * @since It is mandatory to call when you want adaptor for ViewPager.
	 * @return  current instance of adaptor for chaining the methods.
	 */
	@SuppressWarnings("rawtypes")
	public GenricAdapter setPageAdapter() {
		madapter = new PageAdapter();
		return this;
	}

	/**
	 * 
	 * @return  current instance of PagerAdaptor adaptor.
	 */
	public PageAdapter getPageAdapter() {
		return this.madapter;
	}

	public class PageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.Count();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(View pager, int position) {
			View view = null;
			try {
				if (IsCustomView) {
					view = cListener.setItemView(data.get(position), position);
					((ViewPager) pager).addView(view, 0);
					return view;
				} else {
					view = mInflater.inflate(CustomListItemLayoutID, null);
					mListener.setItemView(data.get(position), view, position);
					((ViewPager) pager).addView(view, 0);
					return view;
				}
			} catch (Exception ex) {
				IsError += 1;
				if (IsError < 2) {
					data = new ArrayList<T>();
					Toast.makeText(context,
							"setItemView suffering from invalid casting",
							Toast.LENGTH_SHORT).show();
					Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG)
							.show();

					ex.printStackTrace();
				}
				return view;
			}
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0.equals(arg1);
		}
	}

}
