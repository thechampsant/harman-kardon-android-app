package app.core.adapter;

import android.view.View;

public interface ICustomAdapter<T> {
	/**
	 * 
	 * @param item is the single object from data  that will be passed during rendering the ListView items.
	 * @param index is data ArrayList position.
	 * @return
	 */
	public View setItemView(T item,int index);
}
