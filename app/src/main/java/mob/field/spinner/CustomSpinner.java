package mob.field.spinner;

import java.util.List;

import android.app.Activity;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CustomSpinner {
	Spinner spinner;
	SpinnerResponse sResponse = null;

	public CustomSpinner(Activity activity, int id, String prompt,
			List<String> list,OnItemSelectedListener onItemSelectedListener) {
		spinner = (Spinner) activity.findViewById(id);
		spinner.setPrompt(prompt);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		if(onItemSelectedListener!=null)
		spinner.setOnItemSelectedListener(onItemSelectedListener);
	}

	public void setSpinnerListener(SpinnerResponse sResponse) {
		this.sResponse = sResponse;
	}

}
