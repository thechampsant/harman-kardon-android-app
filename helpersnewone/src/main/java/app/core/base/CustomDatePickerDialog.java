package app.core.base;

import java.util.Date;
import linq.ArrayList;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import app.core.model.DateControlModel;
import app.core.model.DateModel;
import app.core.utils.onDateSetListener;

public class CustomDatePickerDialog extends DatePickerDialog {


	private Context context;
	private DatePickerDialog datepicker=null;
	ArrayList<DateControlModel> datepickers=new ArrayList<DateControlModel>();
	DateModel date = new DateModel().getCurrentDateModel();

	public CustomDatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);
		this.context=context;
	}
	public CustomDatePickerDialog(Context context,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.context=context;
	}
	
	private DatePickerDialog.OnDateSetListener setDateListener(final onDateSetListener callback) {

		DatePickerDialog.OnDateSetListener listen = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				callback.onDateSet(view,
						getDate(year, monthOfYear, dayOfMonth),
						getDateInString(year, monthOfYear, dayOfMonth));
			}
		};
		return listen;
	}
	public void addListener(int controlid,onDateSetListener listener)
	{
		if(isDatePickerAttached(controlid))return;
		CustomDatePickerDialog dialog= new CustomDatePickerDialog(context,setDateListener(listener),date.Year, date.Month, date.Day);
		DateControlModel model= new DateControlModel(controlid,listener,dialog);
		datepickers.add(model);
		this.datepicker=this;
	}
	public boolean isDatePickerAttached(int controlid)
	{
		return datepickers.Any("ControlID",controlid);
	}
	public boolean isDatePickerAttached(onDateSetListener listener)
	{
		return datepickers.Any("listener",listener);
	}

	public void show(int controlid)
	{
		if(getDatePickerForControl(controlid)!=null);
		 datepicker.show();		 
	}
	
	private DatePickerDialog getDatePickerForControl(int controlid)
	{
		DateControlModel model= datepickers.where("ControlID",controlid).First();
		if(model==null)
		{
			Toast.makeText(context,"Datepicker is not attached to controlid :"+controlid,Toast.LENGTH_LONG).show();
			return null;
		}
		else
		{
			this.datepicker=model.datepicker;
			return model.datepicker;
		}
	}
	
	
	
	public DatePickerDialog addListenerWithoutDateField(int controlid,
			onDateSetListener callBack) {
		addListener(controlid,callBack);
		datepicker = this;
		try {
			java.lang.reflect.Field[] datePickerDialogFields = datepicker
					.getClass().getDeclaredFields();
			for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
				if (datePickerDialogField.getName().equals("mDatePicker")) {
					datePickerDialogField.setAccessible(true);
					DatePicker datePicker = (DatePicker) datePickerDialogField
							.get(datepicker);
					java.lang.reflect.Field[] datePickerFields = datePickerDialogField
							.getType().getDeclaredFields();
					for (java.lang.reflect.Field datePickerField : datePickerFields) {
						Log.i("test", datePickerField.getName());
						if ("mDaySpinner".equals(datePickerField.getName())) {
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							((View) dayPicker).setVisibility(View.GONE);
						}
					}
				}

			}
		} catch (Exception ex) {
		}
		return datepicker;

	}
	
	public DatePickerDialog attachDatePicker(int controlid,onDateSetListener callBack) {

		addListener(controlid,callBack);
		return datepicker;
	}
	
	@SuppressWarnings("deprecation")
	public Date getDate(int year, int monthOfYear, int dayOfMonth) {

		Date date = new Date();
		date.setMonth(monthOfYear);
		date.setYear(year);
		date.setDate(dayOfMonth);

		return date;
	}
	public String getDateInString(int year, int monthOfYear, int dayOfMonth) {
		StringBuilder sb = new StringBuilder();
		sb.append(year).append("-").append(monthOfYear + 1).append("-")
				.append(dayOfMonth);
		return sb.toString();
	}

}
