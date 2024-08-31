package app.core.model;

import android.app.DatePickerDialog;
import app.core.utils.onDateSetListener;

public class DateControlModel {

		public int ControlID;
		public onDateSetListener listener=null;
		public DatePickerDialog datepicker=null;
		
		public DateControlModel(int ControlID,onDateSetListener listener,DatePickerDialog datepicker)
		{
			this.ControlID=ControlID;
			this.listener=listener;
			this.datepicker=datepicker;
		}
}
