package app.core.adapter;
import android.view.View;

/**
 * 
 * @author Sandeep
 *
 * @param <T> is any java class object type.
 */
public  interface  IAdapter<T> {
	
	/**
	 * 
	 * @param item is the single object from data  that will be passed during rendering the ListView items. 
	 * @param view is inflated and ready to set you item in this. 
	 * @param index is data ArrayList position.
	 */
	 public void setItemView(T item,View view,int index);

}
