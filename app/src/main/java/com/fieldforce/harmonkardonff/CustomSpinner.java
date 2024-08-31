package com.fieldforce.harmonkardonff;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CustomSpinner {
	Spinner spinner;
	SpinnerResponse sResponse = null;

	
	public CustomSpinner(Activity activity, View spin, String prompt,
			List<String> list) {
		spinner = (Spinner) spin;
//		spinner.setPopupBackgroundDrawable(activity.getResources().getDrawable(R.drawable.spinnerlistbg));
		spinner.setPrompt(prompt);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener(listner);
	}
	
	OnItemSelectedListener listner = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			if(sResponse != null)
			sResponse.onItemSelectListener(spinner);
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	public void setSpinnerListener(SpinnerResponse sResponse) {
		this.sResponse = sResponse;
	}

}
