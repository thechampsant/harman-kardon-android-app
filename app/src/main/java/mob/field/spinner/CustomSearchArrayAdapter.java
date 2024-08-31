package mob.field.spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

/**
 * Custom {@link ArrayAdapter} which supports in text searching
 * 
 * @author Himanshu
 * @version 1.0
 * 
 */
public class CustomSearchArrayAdapter<T> extends ArrayAdapter<T> {

	/**
	 * List which was provided at the start
	 */
	List<T> originalList;

	/**
	 * Instance of filter
	 */
	private CustomFilter customFilter;

	public CustomSearchArrayAdapter(Context context, int resource,
			List<T> objects) {
		super(context, resource, objects);
		originalList = (List<T>) ((ArrayList<T>) objects).clone();
	}

	@Override
	public Filter getFilter() {

		if (customFilter == null)
			customFilter = new CustomFilter();

		return customFilter;

	}

	class CustomFilter extends Filter {

		@Override
		protected FilterResults performFiltering(
		/* Nullable */CharSequence constraint) {
			FilterResults filteredResults = new FilterResults();

			/* If constraint is null we will the original list */
			if (constraint == null || constraint.length()==0) {
				filteredResults.values = originalList;
				filteredResults.count = originalList.size();
				return filteredResults;
			}

			ArrayList<Object> filteredItems = new ArrayList<Object>();

			for (Object currentValue : originalList) {
				if (currentValue
						.toString()
						.trim()
						.toLowerCase(Locale.US)
						.contains(
								constraint.toString().trim()
										.toLowerCase(Locale.US)))
					filteredItems.add(currentValue);
			}

			filteredResults.values = filteredItems;
			filteredResults.count = filteredItems.size();

			return filteredResults;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			clear();
			addAll((ArrayList<T>) results.values);
			notifyDataSetChanged();

		}

	}

}
