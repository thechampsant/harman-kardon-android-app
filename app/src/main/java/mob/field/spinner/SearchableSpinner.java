package mob.field.spinner;

import java.util.ArrayList;
import java.util.List;

import com.fieldforce.harmonkardonff.R;

import mob.field.spinner.SearchableListDialog.OnDialogClose;
import mob.field.spinner.SearchableListDialog.OnDialogItemClickListener;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;


public class SearchableSpinner extends Spinner implements View.OnTouchListener,
		SearchableListDialog.SearchableItem,SearchableDialogOnPauseListener,OnDialogClose {

	public static final int NO_ITEM_SELECTED = -1;
	private Context _context;
	private List _items;
	private SearchableListDialog _searchableListDialog;

	private boolean _isDirty;
	private ArrayAdapter _arrayAdapter;
	private String _strHintText;
	private boolean _isFromInit;
	private boolean isOnTouch = false;

	public SearchableSpinner(Context context) {
		super(context);
		this._context = context;
		init();
	}

	public SearchableSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this._context = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SearchableSpinner);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i) {
			int attr = a.getIndex(i);
			if (attr == R.styleable.SearchableSpinner_hintText) {
				_strHintText = a.getString(attr);
			}
		}
		a.recycle();
		init();
	}

	public SearchableSpinner(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this._context = context;
		init();
	}
	@Override
	public void onDialogClose() {
		isOnTouch = false;
	}
	private void init() {
	
		initializeOnResumeListener();
		_items = new ArrayList();
		_searchableListDialog = SearchableListDialog.newInstance(_items,
				_context);
		_searchableListDialog.setOnCloseDialogListener(this);
		_searchableListDialog.setOnDialogItemClickListener(new OnDialogItemClickListener() {
			
			@Override
			public void onClick() {
				isOnTouch = false;
			}
		});
		_searchableListDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isOnTouch = false;
				
			}
		});
	
		_searchableListDialog.setOnSearchableItemClickListener(this);
		setOnTouchListener(this);

		_arrayAdapter = (CustomSearchArrayAdapter) getAdapter();
		if (!TextUtils.isEmpty(_strHintText)) {
			ArrayAdapter arrayAdapter = new ArrayAdapter(_context,
					android.R.layout.simple_list_item_1,
					new String[] { _strHintText });
			_isFromInit = true;
			setAdapter(arrayAdapter);
		}

	}

	private void initializeOnResumeListener() {
		/*AllSearchableSpinnerListeners.getInstance().register(
				new SearchableDialogOnPauseListener() {
					@Override
					public void onPause() {
						_searchableListDialog.dismiss();
					}
				});*/
		AllSearchableSpinnerListeners.getInstance().register(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP && !isOnTouch) {
			isOnTouch = true;
			if (_searchableListDialog.getDialog() != null) {
				isOnTouch = false;
				/*_searchableListDialog.dismiss();*/
				return true;
			}
			if (null != _arrayAdapter) {

				// Refresh content #6
				// Change Start
				// Description: The items were only set initially, not reloading
				// the data in the
				// spinner every time it is loaded with items in the adapter.
				_items.clear();
				for (int i = 0; i < _arrayAdapter.getCount(); i++) {
					_items.add(_arrayAdapter.getItem(i));
				}
				// Change end.


				try {
					_searchableListDialog.show(scanForActivity(_context)
							.getFragmentManager(), "TAG");
				} catch (IllegalStateException e) {
					Log.e("1","illegal");
				} catch (Exception e) {
					Log.e("1","exception");
			}
			}
		}
		return true;
	}

	@Override
	public void setAdapter(SpinnerAdapter adapter) {

		if (!_isFromInit) {
			_arrayAdapter = (CustomSearchArrayAdapter) adapter;
			if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
				ArrayAdapter arrayAdapter = new ArrayAdapter(_context,
						android.R.layout.simple_list_item_1,
						new String[] { _strHintText });
				super.setAdapter(arrayAdapter);
			} else {
				super.setAdapter(adapter);
			}

		} else {
			_isFromInit = false;
			super.setAdapter(adapter);
		}
	}

	@Override
	public void onSearchableItemClicked(Object item, int position) {
		setSelection(_items.indexOf(item));

		if (!_isDirty) {
			_isDirty = true;
			setAdapter(_arrayAdapter);
			setSelection(_items.indexOf(item));
		}
	}

	public void setTitle(String strTitle) {
		_searchableListDialog.setTitle(strTitle);
	}

	public void setPositiveButton(String strPositiveButtonText) {
		_searchableListDialog.setPositiveButton(strPositiveButtonText);
	}

	public void setPositiveButton(String strPositiveButtonText,
			DialogInterface.OnClickListener onClickListener) {
		_searchableListDialog.setPositiveButton(strPositiveButtonText,
				onClickListener);
	}

	public void setOnSearchTextChangedListener(
			SearchableListDialog.OnSearchTextChanged onSearchTextChanged) {
		_searchableListDialog
				.setOnSearchTextChangedListener(onSearchTextChanged);
	}

	private Activity scanForActivity(Context cont) {
		if (cont == null)
			return null;
		else if (cont instanceof Activity)
			return (Activity) cont;
		else if (cont instanceof ContextWrapper)
			return scanForActivity(((ContextWrapper) cont).getBaseContext());

		return null;
	}

	@Override
	public int getSelectedItemPosition() {
	//	isOnTouch = false;
		if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
			return NO_ITEM_SELECTED;
		} else {
			return super.getSelectedItemPosition();
		}
	}

	@Override
	public Object getSelectedItem() {
		if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
			return null;
		} else {
			return super.getSelectedItem();
		}
	}

	@Override
	public void onPause() {
		isOnTouch = false;
		if(_searchableListDialog.isAdded()){
		_searchableListDialog.dismiss();}
		
	}
	public void performOnItemSelection(AdapterView<?> adapter,View view,int position,long id)
	{
		listenerOnItemSelect.onItemSelected(adapter, view, position, id);
	}
	android.widget.AdapterView.OnItemSelectedListener listenerOnItemSelect;
	@Override
	public void setOnItemSelectedListener(
			android.widget.AdapterView.OnItemSelectedListener listener) {
		// TODO Auto-generated method stub
		super.setOnItemSelectedListener(listener);
		listenerOnItemSelect = listener;
	}
}