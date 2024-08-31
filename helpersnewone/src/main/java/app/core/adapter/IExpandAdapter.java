package app.core.adapter;

import android.view.View;

public interface IExpandAdapter<T>  {
	
	public void setParentItemView(T item,View convertView);
	public void setChildItemView(T item,View convertView);

}
