package app.core.adapter;

import linq.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Toast;

public class ExpandAdapter<T> extends BaseExpandableListAdapter {

	
	public ArrayList<T> data;
	private IExpandAdapter<T> mListener = null;
	private LayoutInflater mInflater = null;
	public Context context;
	private int IsError = 0;
	private int ParentViewID=-1;
	private int ChildViewID=-1;
	
	

	
	public ExpandAdapter(Activity activity) {
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = new ArrayList<T>();
		context = activity;
	}
	@SuppressWarnings("rawtypes")
	public ExpandAdapter setLayout(int _ParentLayout,int _ChildLayout)
	{
		this.ParentViewID=_ParentLayout;
		this.ChildViewID=_ChildLayout;
		return this;
	}
	@SuppressWarnings({ "rawtypes" })
	public ExpandAdapter setData(ArrayList<T> _data) {
		data = _data;
		return this;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.data.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		try {
			if (convertView == null) {
				convertView = this.mInflater.inflate(this.ChildViewID, null);
			}
			mListener.setChildItemView(this.data.get(groupPosition), convertView);
			return convertView;
		} catch (Exception ex) {
			IsError += 1;
			if (IsError < 2) {
				data = new ArrayList<T>();
				Toast.makeText(context, ex.toString(),6000).show();
			}
			return convertView;
		}
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.data.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.data.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		try {
			if (convertView == null) {
				convertView = this.mInflater.inflate(this.ParentViewID, null);
			}
			mListener.setParentItemView(this.data.get(groupPosition), convertView);
			return convertView;
		} catch (Exception ex) {
			IsError += 1;
			if (IsError < 2) {
				data = new ArrayList<T>();
				Toast.makeText(context, ex.toString(),6000).show();
			}
			return convertView;
		}
		

	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@SuppressWarnings({ "rawtypes" })
	public ExpandAdapter setExpandAdapter(IExpandAdapter<T> eventListener) {
		mListener = eventListener;
		return this;
	}
	
}
