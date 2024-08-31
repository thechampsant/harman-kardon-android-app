package com.fieldforce.customAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomAdapter<E> extends BaseAdapter {

	Activity context;
	ArrayList<E> dataattend = new ArrayList<E>();
	public int layoutid;
	public int layoutheader;

	private static LayoutInflater inflater = null;

	public CustomAdapter(Activity mainActivity, ArrayList<E> data, int id) {
		layoutid = id;

		// TODO Auto-generated constructor stub

		dataattend.clear();
		dataattend = data;
		context = mainActivity;
		//
		// inflater = (LayoutInflater) context
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataattend.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataattend.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	CustomAdaterInterface custAdapterInterface;

	public void setAdapterInterface(CustomAdaterInterface custAdapterInterface) {
		this.custAdapterInterface = custAdapterInterface;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = null;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(layoutid, null);

		custAdapterInterface.getView(dataattend.get(position), rowView);
		return rowView;

	}

}
