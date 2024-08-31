package app.core.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateModel {
	
	public int Year=2011;
	public int Month=05;
	public int Day=1;
	
	public DateModel getCurrentDateModel()
	{
		String[] vals=getCurrentDateInString().split("-");
		this.Year=ToInt(vals[0]);
		this.Month=ToInt(vals[1]);
		this.Day=ToInt(vals[2]);
		return this;
	}
	private double ToDouble(String input) {
		// TODO Auto-generated method stub
		double output = 0;

		try {
			// go on as normal
			
			output = Double.parseDouble(input);
			return output;

		} catch (Exception e) {
			// handle error
			return output;
		}
	}
	private int ToInt(String input) {
		// TODO Auto-generated method stub
		int output = 0;

		try {
			// go on as normal
			
			output = Integer.parseInt(input);
			return output;

		} catch (Exception e) {
			// handle error
			return output;
		}
	}
	public String getCurrentDateInString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

}
