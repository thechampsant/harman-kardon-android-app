package com.fieldforce.harmonhelper;


import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import linq.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fieldforce.harmonkardonff.MainActivity;

@SuppressLint("SimpleDateFormat")
public class App {

	public static Activity activity;
	public static GPSTracker GPS;
	static SharedPreferences SP;
	static SharedPreferences.Editor editor;
	static InputMethodManager IMM;
	public App()
	{
		Init(MainActivity.Current);
	}
	public App(Activity _baseactivity)
	{
		Init(_baseactivity);
	}
	public void Init(Activity _baseactivity)
	{
		activity = _baseactivity;
		SP=activity.getPreferences(Context.MODE_PRIVATE);
		editor=SP.edit();
		IMM=(InputMethodManager)activity.getSystemService(
			      Context.INPUT_METHOD_SERVICE);
	
	}

	public static String getCurrentVersion()
	{
		
		String version="0.0";
		try {
			
			version = activity.getPackageManager().
					getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return	version;
	}
	
	// get hardware mac address
	public static String getMACaddress()
	{
		// add following persimisin
		//<uses-permission android:name="android.permission.INTERNET" />
		//<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
		String address=null;
		address=getMAC("wlan0"); //using wifi available
		if(address!=null)return address;
		address=getMAC("eth0"); //using ethernet connection availale
		return address;
	}
	public static String getMAC(String interfaceName) {
	    try {
	        List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
	        for (NetworkInterface intf : interfaces) {
	            if (interfaceName != null) {
	                if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
	            }
	            byte[] mac = intf.getHardwareAddress();
	            if (mac==null) return null;
	            StringBuilder buf = new StringBuilder();
	            for (int idx=0; idx<mac.length; idx++)
	                buf.append(String.format("%02X:", mac[idx]));       
	            if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
	            return buf.toString();
	        }
	    } catch (Exception ex) { } 
	    return null;

	}
	
	//Preference  handling
    public static void HideKeyPad(int controlid)
    {
    	IMM.hideSoftInputFromWindow(activity.findViewById(controlid).getWindowToken(), 0);
    }
	public static boolean SavePreferences(String Key,String Value)
	{
		try
		{
		editor.putString(Key,Value);
		editor.commit();
		return true;
		}
		catch (Exception ex) {
			Log.e("Error:", ex.toString());
			return false;
		}
	}
	public static String getSharedPreference(String key, String defValue) {
		return SP.getString(key, defValue);
	}
	public static boolean RemovePrefrences(String Key)
	{
	  editor.remove(Key);
	  editor.commit();
	  return true;
	}
	
	// methods.....
	public  static void ShowToast(String Message) {
		Toast toast = Toast.makeText(activity, Message,
				Toast.LENGTH_SHORT);
		toast.show();
	}
	// Toast Message
	public static boolean IsNullOrWhiteSpace(String str)
	{
		if(str==null)
			return true;
		else if(str.trim()=="")
			return true;
		else if(TextUtils.isEmpty(str))
			return true;
		else 
			return false;
	}
	public static void ShowToastLong(String Message, int Duration) {
		Toast toast = Toast.makeText(activity, Message,
				Toast.LENGTH_LONG);
		toast.show();
	}
    public static void setToggleCheckedChangeListener(int id,OnCheckedChangeListener lst)
    {
    	ToggleButton tbutton=GetToggleButton(id);
    	tbutton.setOnCheckedChangeListener(lst);
    }
    public static boolean IsEmpty(String value)
    {
    	return TextUtils.isEmpty(value);
    }
	// EditText Options
	public static EditText GetEditText(int id) {
		return (EditText) activity.findViewById(id);
	}

	public static String GetEditTextAsString(int id) {
		return GetEditText(id).getText().toString();
	}

	public static int GetEditTextAsInt(int id) {
		String txt = GetEditText(id).getText().toString();
		return Integer.parseInt(txt);
	}

	public static double GetEditTextAsDouble(int id) {
		String txt = GetEditTextAsString(id);
		return Double.parseDouble(txt);
	}

	public static void SetEditTextAsString(int id, String txt) {
		GetEditText(id).setText(txt);
	}

	public static void SetEditTextFromObject(int id, Object obj) {
		String txt = "";
		if (obj != null)
			txt = obj.toString();
		GetEditText(id).setText(txt);
	}

	// TextView Options
	public static TextView GetTextView(int id) {
		return (TextView) activity.findViewById(id);
	}

	public static String GetTextViewAsString(int id) {
		TextView tv = GetTextView(id);
		return tv.getText().toString();
	}

	public static int GetTextViewAsInt(int id) {
		String txt = GetTextViewAsString(id);
		return Integer.parseInt(txt);
	}

	public static double GetTextViewAsDouble(int id) {
		String txt = GetTextViewAsString(id);
		return Double.parseDouble(txt);
	}

	public static void SetTextViewAsString(int id, String text) {
		TextView tv = GetTextView(id);
		tv.setText(text);
	}
	
	// Toggle Button
	public static ToggleButton GetToggleButton(int id)
	{
		return (ToggleButton) activity.findViewById(id);
	}

	// Button Options
	public static Button GetButton(int id) {
		return (Button) activity.findViewById(id);
	}

	public static void SetButtonLabel(int id, String text) {
		Button btn = GetButton(id);
		btn.setText(text);
	}

	public static void SetButtonLabel(int id, int textResID) {
		Button btn = GetButton(id);
		btn.setText(textResID);
	}

	public static void SetOnClickListenerOnButton(int id, OnClickListener l) {
		Button btn = GetButton(id);
		btn.setOnClickListener(l);
	}

	// CheckBox Options
	public static CheckBox GetCheckBox(int id) {
		return (CheckBox) activity.findViewById(id);
	}

	public static Boolean GetCheckBoxStatus(int id) {
		return GetCheckBox(id).isChecked();
	}
	@SuppressWarnings("unchecked")
	public  static void SetTxtToSpinner(int id, String text) {
		// the value you want the position for
		if (text != null) {
			Spinner mySpinner = (Spinner) activity.findViewById(id);

			ArrayAdapter<String> myAdap = (ArrayAdapter<String>) mySpinner
					.getAdapter(); // cast to an ArrayAdapter

			int spinnerPosition = myAdap.getPosition(text);

			// set the default according to value
			mySpinner.setSelection(spinnerPosition);
		}
	}
	public static void SetCheckBoxStatus(int id, Boolean Status) {
		GetCheckBox(id).setChecked(Status);
	}

	public static String GetCheckBoxText(int id) {
		return GetCheckBox(id).getText().toString();
	}

	public static void SetCheckBoxText(int id, String text) {
		GetCheckBox(id).setText(text);
	}

	// Date Time Options
	public static Calendar getDatePart(Date date) {
		Calendar cal = Calendar.getInstance(); // get calendar instance
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
		cal.set(Calendar.MINUTE, 0); // set minute in hour
		cal.set(Calendar.SECOND, 0); // set second in minute
		cal.set(Calendar.MILLISECOND, 0); // set millisecond in second
		return cal; // return the date part
	}

	public static long GetDaysBetween(Date startDate, Date endDate) {
		Calendar sDate = getDatePart(startDate);
		Calendar eDate = getDatePart(endDate);

		long daysBetween = 0;
		while (sDate.before(eDate)) {
			sDate.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	// Integer Parsing

	public static Integer ToInt(String input) {
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


	// ____________________________Shared Preferences

	public static ListView getListView(int id) {
		return (ListView) activity.findViewById(id);
	}

	public static boolean setListViewfromStringArray(String[] sdata, int lvID) {

		try {
			ListView lv = getListView(lvID);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					sdata);
			lv.setAdapter(adapter);
			return true;
		} catch (Exception ex) {
			Log.e("Error", ex.toString());
			// Do Nothing
			return false;
		}

	}

	public static RadioButton getRadioButton(int id) {
		return (RadioButton) activity.findViewById(id);
	}

	// ____________Spinner_______________

	public static Spinner getSpinner(int id) {
		return (Spinner) activity.findViewById(id);
	}

	public static String GetTxtFromSpinner(int id) {
		// TODO Auto-generated method stub
		Spinner sp = getSpinner(id);
		return sp.getSelectedItem().toString();
	}
	public static void SetOnClickListenerOnCheckBox(int id,
			OnCheckedChangeListener l) {
		CheckBox cbx = GetCheckBox(id);
		cbx.setOnCheckedChangeListener(l);
	}

	public static boolean SetSpinnerFromStringArray(String[] str, int spID) {
		try {
			Spinner sp = getSpinner(spID);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
					android.R.layout.simple_spinner_item, str);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			return true;
		} catch (Exception ex) {
			Log.e("Error: ", ex.toString());
			return false;
		}

	}

	public static <T>  boolean SetSpinnerFromArrayList(ArrayList<T> array, int spID) {
		try {
			Spinner sp = getSpinner(spID);
			ArrayAdapter<T> adapter = new ArrayAdapter<T>(activity,
					android.R.layout.simple_spinner_item, array);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			return true;
		} catch (Exception ex) {
			Log.e("Error: ", ex.toString());
			return false;
		}
	}

	// ____________Progresss Bar_______________


	public static ProgressBar getProgressBar(int id) {
		return (ProgressBar) activity.findViewById(id);
	}

	// ____________Radio Group_______________

	public static RadioGroup getRadioGroup(int id) {
		return (RadioGroup) activity.findViewById(id);
	}

	// ____________Switch(Yes/No)_______________

	public static Switch getSwitch(int id) {
		return (Switch) activity.findViewById(id);
	}

	// ____________Date Picker_______________

	public static DatePicker getDatePicker(int id) {
		return (DatePicker) activity.findViewById(id);
	}

	// ________________Network & GPS________________

	public static String Getlongitude() {
		// TODO Auto-generated method stub

		if (GPS.canGetLocation()) {

			double latitude = GPS.getLatitude();
			double longitude = GPS.getLongitude();
			return longitude + "";
		} else {
			GPS.showSettingsAlert();
		}

		return "0";
	}

	public static String Getlatitude() {
		// if(lastLocation != null) return lastLocation.getLatitude()+"";
		if (GPS.canGetLocation()) {

			double latitude = GPS.getLatitude();
			double longitude = GPS.getLongitude();
			return latitude + "";
		} else {
			GPS.showSettingsAlert();
		}

		return "0";
	}

	// ___________Date___________________

	@SuppressWarnings("deprecation")
	public static String GetDateInString(int id) {
		DatePicker myDp = getDatePicker(id);

		Date dt = new Date(myDp.getYear() - 1900, myDp.getMonth(),
				myDp.getDayOfMonth());
		DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		return targetFormat.format(dt);
	}

	// /Get Date From Date Picker
	
	public static  Date GetDateFDP(int id) {
		DatePicker myDp = getDatePicker(id);

		@SuppressWarnings("deprecation")
		Date date = new Date(myDp.getYear() - 1900, myDp.getMonth(),
				myDp.getDayOfMonth());
		return date;

	}

	public static String GetCurrentDateTimeInString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		String MyDate = dateFormat.format(date);
		return MyDate;
	}
	public static long ToLong(String value)
	{
		try
		{
			return   Long.parseLong(value);
		}
		catch(Exception ex)
		{
			return -1;
		}
	}

	public static String GetCurrentDateInString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

	public static String ConvertDateToString(Date date) {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

	public static Date ConvertStringToDate(String saleFor) {
		// TODO Auto-generated method stub
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = dateFormat.parse(saleFor);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DateFormat edateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			try {
				date = new Date();
				date = edateFormat.parse(saleFor);
				return date;
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return date;
	}

	@SuppressWarnings("deprecation")
	public static Date GetCurrentDate() {
		// TODO Auto-generated method stub
		Date MyDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			MyDate = dateFormat.parse(dateFormat.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MyDate;
	}

	public static Calendar ConvertStringToCalender(String date) {
		// TODO Auto-generated method stub
		Date dt = ConvertStringToDate(date);
		return getDatePart(dt);
	}

	public static String ConvertCalenderToString(Calendar cale) {
		// TODO Auto-generated method stub
		Date date = ConvertCalenderTODate(cale);
		return ConvertDateToString(date);
	}

	private static Date ConvertCalenderTODate(Calendar cale) {
		// TODO Auto-generated method stub
		return cale.getTime();

	}

	public static Boolean ToBool(String _value)
	{
		return _value.equalsIgnoreCase("true");
	}
	public static void log(String tag,String msg)
	{
		Log.i(tag, msg);
	}
	public static String ToString(Object obj)
	{
		if(obj!=null)
			return obj.toString();
		else
			return "Failed";
	}
	public static  boolean isNetworkAvailable() {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager)activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null)
			isConnected = activeNetworkInfo.isConnected();
		if (activeNetworkInfo != null && isConnected)
			return true;
		else
			return false;
		}


	private static final String TAG = "Appwa";
	public static boolean checkPdf(String url) {
		if (url == null)
			return false;
		String lastUrl = url.substring(url.lastIndexOf("."), url.length());
		if (url.length() > 3 && url.substring(url.length() - 3, url.length()).equals("pdf")){
			Log.d(TAG, "it is PDF");
			return true;
		}
		else {
			if (url.length() > 3 && url.substring(url.lastIndexOf(".")+1, url.length()).equals("pptx")){
				Log.d(TAG, "it is PPT");
				return true;
			}
			else {
				if (url.length() > 3 && url.substring(url.lastIndexOf(".")+1, url.length()).equals("ppt")){
					Log.d(TAG, "it is PPT");
					return true;
				}
				else {
					return false;
				}
			}
		}
	}
}
