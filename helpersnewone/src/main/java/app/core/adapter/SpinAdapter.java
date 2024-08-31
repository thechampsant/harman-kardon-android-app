package app.core.adapter;

import linq.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinAdapter<T> extends ArrayAdapter<T> {

	// Your sent context
	private Context context;
	private T[] values;
	private ISpinAdapter<T> mListener = null;

	public SpinAdapter(Context context, int textViewResourceId, T[] values) {
		super(context, textViewResourceId, values);
		this.context = context;
		this.values = values;
	}

	@SuppressWarnings("unchecked")
	public SpinAdapter(Context context, int textViewResourceId,
			ArrayList<T> values) {
		super(context, textViewResourceId, values);
		this.context = context;
		this.values = (T[]) values.toArray();
	}

	  public int getCount() {
		return values.length;
	}

	public T getItem(int position) {
		return values[position];
	}

	public long getItemId(int position) {
		return position;
	}

	// // And the "magic" goes here
	// // This is for the "passive" state of the spinner
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // I created a dynamic TextView here, but you can reference your own
	// custom layout for each spinner item
	// TextView label = new TextView(context);
	// label.setTextColor(Color.BLACK);
	// // Then you can get the current item using the values array (Users array)
	// and the current position
	// // You can NOW reference each method you has created in your bean object
	// (User class)
	// label.setText(values[position].getName());
	//
	// // And finally return your dynamic (or custom) view for each spinner item
	// return label;
	// }

	  public View getView(int position, View convertView, ViewGroup parent) {
		// I created a dynamic TextView here, but you can reference your own
		// custom layout for each spinner item
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setTextSize(20);
		label.setPadding(0,5,0,5);
		// Then you can get the current item using the values array (Users
		// array) and the current position
		// You can NOW reference each method you has created in your bean object
		// (User class)
		 mListener.setLabel(label, values[position], position);
		// And finally return your dynamic (or custom) view for each spinner
		// item
		return label;
	}

	@SuppressWarnings({ "rawtypes" })
	public SpinAdapter setDropDownListener(ISpinAdapter<T> eventListener) {
		mListener = eventListener;
		return this;
	}

	public interface ISpinAdapter<T> {
		public void setLabel(TextView label, T item, int index);

		public void setDropLabel(TextView label, T item, int index);
	}

	// And here is when the "chooser" is popped up
	// Normally is the same view, but you can customize it if you want
	 @Override
	   public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setTextSize(20);
		label.setPadding(0,8,0,8);
		mListener.setLabel(label, values[position], position);

		return label;
	}
}
