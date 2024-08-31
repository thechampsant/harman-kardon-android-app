package search;

import java.lang.reflect.Field;

import linq.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import app.core.adapter.GenricAdapter;
import app.core.controls.SearchDialogControl;

@SuppressLint("DefaultLocale")
public class SearchDiloag<T> {

	@SuppressWarnings("rawtypes")
	private OnClickDiloagItemListener DiloagResult = null;
	private Dialog diloag = null;
	@SuppressWarnings("rawtypes")
	private GenricAdapter adapter;
	private String SearchField = "Name";
	private ArrayList<T> BackUpData;
	private boolean IsGenric = false;
	private boolean IsNumeric = false;

	SearchDialogControl searchDialogControl;

	@SuppressWarnings("rawtypes")
	public SearchDiloag(GenricAdapter _GenricAdapter) {

		adapter = _GenricAdapter;
		searchDialogControl = new SearchDialogControl(adapter.context);
		BackUpData = new ArrayList<T>();
		diloag = new Dialog(adapter.context);
		diloag.requestWindowFeature(Window.FEATURE_NO_TITLE);
		diloag.setContentView(searchDialogControl.getSearchLayout());
		diloag.setCancelable(true);
		searchDialogControl.Back.setOnClickListener(backb);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SearchDiloag ActivateSearch(String _SearchField) {
		SearchField = _SearchField;
		if (!processForDataType())
			return this;
		BackUpData = adapter.data;
		setListViewData();
		searchDialogControl.SearchBox.addTextChangedListener(tw);
		diloag.show();
		return this;
	}

	private boolean checkSearchField() {
		if (IsGenric && SearchField == null) {
			showToast("Search field is mandatory for genric object!");
			return false;
		} else if (!IsFieldFound()) {
			showToast(SearchField+" search field doesn't exists in "+adapter.data.First().getClass().getSimpleName());
			return false;
		} else
			return true;
	}

	private boolean IsFieldFound() {
		Field[] fields=adapter.data.First().getClass().getDeclaredFields();
		for(Field field:fields)
		{
			if(field.getName().contentEquals(SearchField.trim()))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean processForDataType() {
		if (adapter.data == null) {
			showToast("Can not initate search!Because data item is null");
			return false;
		} else if (adapter.data.Count() < 1) {
			showToast("Can not initate search!Because data item is empty");
			return false;
		} else {

			T obj = (T) adapter.data.First();
			if (obj instanceof String) {
				adapter.data = adapter.data.OrderBy();
				return true;
				// process for string
			} else if (obj instanceof Integer) {
				IsNumeric = true;
				return true;
				// process for int
			} else {
				IsGenric = true;
				if (!checkSearchField())
					return false;
				adapter.data = adapter.data.OrderBy(SearchField);

				return true;
				// process normally
			}
		}
	}

	private void showToast(String Message) {
		Toast.makeText(adapter.context, Message, Toast.LENGTH_LONG).show();
	}

	@SuppressWarnings({"unchecked"})
	private void setListViewData() {
		if (adapter.data.Count() > 0) {
			adapter.setData(adapter.data);
			searchDialogControl.lv.setAdapter(adapter);
			searchDialogControl.lv.setOnItemClickListener(ocl);
			searchDialogControl.lv.setOnTouchListener(hideKeyPad);
			searchDialogControl.lv.setVisibility(View.VISIBLE);
			searchDialogControl.msg.setVisibility(View.GONE);
		} else {
			searchDialogControl.lv.setVisibility(View.GONE);
			searchDialogControl.msg.setVisibility(View.VISIBLE);
		}
	}

	@SuppressWarnings("unchecked")
	private void search(String cs) {

		if (cs.length() > 0) {
			if (IsGenric)
				adapter.data = BackUpData.like(SearchField, cs).OrderBy(
						SearchField);
			else if (!IsNumeric)
				adapter.data = BackUpData.like(cs).OrderBy();
			else
				adapter.data = BackUpData.like(cs);
		} else {
			adapter.data = BackUpData;
		}

		setListViewData();

	}

	OnTouchListener hideKeyPad = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			InputMethodManager inputManager = (InputMethodManager) adapter.context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager
					.hideSoftInputFromWindow(searchDialogControl.lv.getWindowToken(), 0);
			return false;
		}
	};
	OnItemClickListener ocl = new OnItemClickListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			try {
				DiloagResult.ResultFromDiloag((T) adapter.data.get(arg2));
				diloag.dismiss();
			} catch (Exception ex) {
				ex.printStackTrace();
				diloag.dismiss();
				showToast(ex.toString());

			}
		}
	};
	OnClickListener backb = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			diloag.dismiss();
		}
	};

	@SuppressWarnings("rawtypes")
	public SearchDiloag setOnClickDiloagItemListener(
			OnClickDiloagItemListener _IDiloagResult) {
		DiloagResult = _IDiloagResult;
		return this;
	}

	TextWatcher tw = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			// adapter.data = new ArrayList<T>();
			search(searchDialogControl.SearchBox.getText().toString());

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}
	};

}
