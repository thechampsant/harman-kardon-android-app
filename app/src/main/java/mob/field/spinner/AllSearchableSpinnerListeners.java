package mob.field.spinner;

import linq.ArrayList;

public class AllSearchableSpinnerListeners {
	ArrayList<SearchableDialogOnPauseListener> arr;
	public static AllSearchableSpinnerListeners allsearchaableInstance;

	public AllSearchableSpinnerListeners() {
		arr = new ArrayList<SearchableDialogOnPauseListener>();
	}

	public static AllSearchableSpinnerListeners getInstance() {
		if (allsearchaableInstance == null) {
			allsearchaableInstance = new AllSearchableSpinnerListeners();
		}
		return allsearchaableInstance;
	}

	public void register(SearchableDialogOnPauseListener searchableInterface) {
		if (searchableInterface != null) {
			arr.add(searchableInterface);
		}
	}

	public void requestForAllSearchableSpinner() {
		for (SearchableDialogOnPauseListener searchdialog : arr) {
			searchdialog.onPause();
		}
		// arr = new ArrayList<SearchableDialogOnPauseListener>();
	}

	public void requestToClearAllSearchableSpinner() {
		arr = new ArrayList<SearchableDialogOnPauseListener>();
	}
}
