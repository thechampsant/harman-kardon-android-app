package app.core.navigator;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import linq.ArrayList;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import app.core.base.ApplicationBase;
import app.core.ibase.IDataIntent;
import app.core.sqllite.DataEntity;
import app.core.sqllite.MySQLiteOpenHelper;
import app.core.sqllite.TableInfo;

public abstract class InnosolsActivity extends Activity implements IDataIntent {

	public static InnosolsActivity CurrentActivity;
	public static NavigationManager Navigator;
	protected InputMethodManager IMM;
	private ApplicationBase AppBase = null;

	   public InnosolsActivity() {
		super();
		CurrentActivity = this;
		Navigator = new NavigationManager(this);
		setPolicy();

	}

	     public void HideKeyPad(int controlid) {
		this.IMM.hideSoftInputFromWindow(this.findViewById(controlid)
				.getWindowToken(), 0);
	}

	public void invokeApplicationContext() {
		AppBase = (ApplicationBase) getApplicationContext();
	}

	// Local Database Options
	public app.core.sqllite.MySQLiteOpenHelper db;

	/**
	 * Use this Method to Enable Local Database
	 * 
	 * @param DBName
	 *            The name of database for local storage
	 * @param version
	 *            The Version of the database. Default: 1
	 */
	public void EnableLocalDatabase(String DBName, int version) {
		this.RegisterTableInfoForLocalDB();
		db = new MySQLiteOpenHelper(this, DBName, 1, _Tables);
		app.core.sqllite.DataEntity.db = db;
	}

	/**
	 * Use this Method to Enable Local Database with Default Versioning.
	 * 
	 * @param DBName
	 *            The name of database for local storage
	 */
	public void EnableLocalDatabase(String DBName) {
		this.EnableLocalDatabase(DBName, 1);
	}

	public abstract void RegisterTableInfoForLocalDB();

	ArrayList<TableInfo> _Tables;

	public void RegisterTableInfo(TableInfo _tableinfo) {
		if (_Tables == null)
			_Tables = new ArrayList<TableInfo>();
		_Tables.add(_tableinfo);
	}

	public <T extends DataEntity<T>> void RegisterTableForDataEntity(T obj) {
		this.RegisterTableInfo(obj.GetTableInfo());
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Toast.makeText(
				this,
				"Low RAM!!! Please close running applications \n Or Restart your phone ",
				Toast.LENGTH_LONG).show();
	}

	@SuppressLint("NewApi")
	private void setPolicy() {
		// TODO Auto-generated method stub
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Ask the user if they want to quit
			/*
			 * new AlertDialog.Builder(this)
			 * .setIcon(android.R.drawable.ic_dialog_alert)
			 * .setTitle(R.string.app_exit) .setMessage(R.string.app_exit_msg)
			 * .setPositiveButton(R.string.yes, new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * {
			 * 
			 * // Stop the activity finish(); }
			 * 
			 * }).setNegativeButton(R.string.no, null).show();
			 */

			IScreenManager current = Navigator.Screens.peek();

			/*
			 * boolean IsActivityScreen = ((Integer) (R.layout.newactivity))
			 * .equals(current.GetObjectID());
			 */
			// Show Dialog box for home and Login Screen
			/*
			 * boolean IsLoginScreen = ((Integer) (R.layout.activity_main))
			 * .equals(current.GetObjectID());
			 */
			if (current.IsHome()) {
				ShowDialog();
			}

			// Go To Main Page from New Activity Page
			/*
			 * else if (IsActivityScreen) {
			 * this.CurrentNavigationManager.NavigateToHome(); }
			 */

			// Navigate to Last Screen in case of all Screens except Home and
			// Login Screen
			else {
				Navigator.Screens.pop();
				NavigateTo(Navigator.Screens.peek(), null);

			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	public int ToInt(String value) {
		try {
			int val = Integer.parseInt(value);
			return val;
		} catch (Exception ex) {
			return -1;
		}

	}

	public String ToString(Object value) {
		try {
			return String.valueOf(value);
		} catch (Exception ex) {
			return null;
		}
	}

	private void ShowDialog() {
		// TODO Auto-generated method stub
		// Ask the user if they want to quit

		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Exit")
				.setMessage("Are you sure?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// Stop the activity
								finish();
								// gps.stopUsingGPS();
							}

						}).setNegativeButton("No", null).show();
	}

	// ------------------------------------------------------------------
	// --------------------- NAVIGATION OPTIONS -------------------------
	// ------------------------------------------------------------------
	public void NavigateTo(IScreenManager _screen, Object arg0) {
		Navigator.NavigateTo(_screen, arg0);
	}

	public void SetDateToDatePicker(int id, Calendar date) {
		this.SetDateToDatePicker(id, this.ConvertCalenderToString(date));
	}

	public void SetDateToDatePicker(int id, String date) {
		this.SetDateToDatePicker(id, ConvertStringToDate(date));
	}

	public void SetDateToDatePicker(int id, Date date) {
		DatePicker dp = (DatePicker) this.findViewById(id);
		// Calendar cal=this.ConvertStringToCalender(date);
		int year = date.getYear() + 1900;
		int month = date.getMonth();
		int days = date.getDay() + 1;

		dp.updateDate(year, month, days);
		// dp.init(year,month,days,null);
	}

	public String CurrentMonthName() {
		return new SimpleDateFormat("MMMM").format(new Date());
	}

	public void ShowToast(String Message) {
		Toast toast = Toast.makeText(CurrentActivity, Message,
				Toast.LENGTH_SHORT);
		toast.show();
	}

	public Date GetBackDate(int day) {
		Calendar cal = this.ConvertStringToCalender(this
				.GetCurrentDateInString());
		cal.add(Calendar.DAY_OF_MONTH, -day);
		Date bDate = this
				.ConvertStringToDate(this.ConvertCalenderToString(cal));
		return bDate;
	}

	public Date GetNextDate(int day) {
		Calendar cal = this.ConvertStringToCalender(this
				.GetCurrentDateInString());
		cal.add(Calendar.DAY_OF_MONTH, day);
		Date bDate = this
				.ConvertStringToDate(this.ConvertCalenderToString(cal));
		return bDate;
	}

	// Toast Message

	public void ShowToastLong(String Message, int Duration) {
		Toast toast = Toast.makeText(CurrentActivity, Message,
				Toast.LENGTH_LONG);
		toast.show();
	}

	// EditText Options
	public EditText GetEditText(int id) {
		return (EditText) this.findViewById(id);
	}
	
	
	

	public String GetEditTextAsString(int id) {
		return this.GetEditText(id).getText().toString();
	}

	public int GetEditTextAsInt(int id) {
		String txt = this.GetEditText(id).getText().toString();
		return Integer.parseInt(txt);
	}

	public double GetEditTextAsDouble(int id) {
		String txt = this.GetEditTextAsString(id);
		return Double.parseDouble(txt);
	}

	public void SetEditTextAsString(int id, String txt) {
		this.GetEditText(id).setText(txt);
	}

	public void SetOnClickListenerOnCheckBox(int id, OnCheckedChangeListener l) {
		CheckBox cbx = GetCheckBox(id);
		cbx.setOnCheckedChangeListener(l);
	}

	public void SetEditTextFromObject(int id, Object obj) {
		String txt = "";
		if (obj != null)
			txt = obj.toString();
		this.GetEditText(id).setText(txt);
	}

	// TextView Options
	public TextView GetTextView(int id) {
		return (TextView) this.findViewById(id);
	}

	public String GetTextViewAsString(int id) {
		TextView tv = this.GetTextView(id);
		return tv.getText().toString();
	}

	public int GetTextViewAsInt(int id) {
		String txt = this.GetTextViewAsString(id);
		return Integer.parseInt(txt);
	}

	public double GetTextViewAsDouble(int id) {
		String txt = this.GetTextViewAsString(id);
		return Double.parseDouble(txt);
	}

	public void SetTextViewAsString(int id, String text) {
		TextView tv = this.GetTextView(id);
		tv.setText(text);
	}

	// Button Options
	public Button GetButton(int id) {
		return (Button) this.findViewById(id);
	}

	public void SetButtonLabel(int id, String text) {
		Button btn = this.GetButton(id);
		btn.setText(text);
	}

	public void SetButtonLabel(int id, int textResID) {
		Button btn = this.GetButton(id);
		btn.setText(textResID);
	}

	public void SetOnClickListenerOnButton(int id, OnClickListener l) {
		Button btn = this.GetButton(id);
		btn.setOnClickListener(l);
	}

	// CheckBox Options
	     public CheckBox GetCheckBox(int id) {
		return (CheckBox) this.findViewById(id);
	}

	public Boolean GetCheckBoxStatus(int id) {
		return this.GetCheckBox(id).isChecked();
	}

	public void SetCheckBoxStatus(int id, Boolean Status) {
		this.GetCheckBox(id).setChecked(Status);
	}

	public String GetCheckBoxText(int id) {
		return this.GetCheckBox(id).getText().toString();
	}

	public void SetCheckBoxText(int id, String text) {
		this.GetCheckBox(id).setText(text);
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

	public long GetDaysBetween(Date startDate, Date endDate) {
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
	/**
	 * <br>
	 * <br>
	 * <b><I>public Integer parseInt(String input)</I></b> <br>
	 * Parse any string into integer.<br>
	 * In Case of Parse Exception return 0
	 * 
	 * @param input
	 *            To parse into integer.
	 * @return integer
	 */
	public Integer parseInt(String input) {
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

	public boolean isNumeric(String input) {
		try {
			int output = 0;
			output = Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// ____________________________Shared Preferences

	/**
	 * <br>
	 * <br>
	 * <b><I>public boolean saveStringInSharedPreferences(String key,String
	 * value)</I></b> <br>
	 * 
	 * @param Key
	 *            The name of preference to insert or modify
	 * @param value
	 *            The new value for the preference
	 * @return True if Pass, False if fail.
	 */
	public boolean saveStringInSharedPreferences(String key, String value) {
		try {
			SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor Editor = sp.edit();
			Editor.putString(key, value);
			Editor.commit();
			return true;
		} catch (Exception ex) {
			Log.e("Error:", ex.toString());
			return false;
		}
	}

	/**
	 * <br>
	 * <br>
	 * <b><I>public boolean saveIntegerInSharedPreferences(String key, int
	 * value)</I></b> <br>
	 * 
	 * @param Key
	 *            The name of preference to insert or modify
	 * @param value
	 *            The new integer value for the preference
	 * @return True if Pass, False if fail.
	 */
	public boolean saveIntegerInSharedPreferences(String key, int value) {
		try {
			SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor Editor = sp.edit();
			Editor.putInt(key, value);
			Editor.commit();
			return true;
		} catch (Exception ex) {
			Log.e("Error:", ex.toString());
			return false;
		}
	}

	/**
	 * 
	 * <br>
	 * <br>
	 * <b>public String getStringFromSharedPreferences(String key,String
	 * defValue)</b>
	 * 
	 * @param key
	 *            The name of preference to retrieve
	 * @param defValue
	 *            Value to return if this preference is does't exist.
	 * @return the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a String.
	 */
	public String getStringFromSharedPreferences(String key, String defValue) {
		SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	/**
	 * 
	 * <br>
	 * <br>
	 * <b>public String getStringFromSharedPreferences(String key,String
	 * defValue)</b>
	 * 
	 * @param key
	 *            The name of preference to retrieve
	 * @param defValue
	 *            Value to return if this preference is does't exist.
	 * @return the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a int.
	 */
	public int getIntegerFromSharedPreferences(String key, int defValue) {
		SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}

	// _____________________________ListView_________________________________

	
	/**
	 * <br>
	 * <br>
	 * <b>public ListView getListView(int id)</b> <br>
	 * Finds a view that was identified by the id attribute from the XML that
	 * was processed in onCreate(Bundle).
	 * 
	 * @param id
	 *            Pass ListView ID.
	 * @return The view if found or null otherwise.
	 */
	public ListView getListView(int id) {
		return (ListView) this.findViewById(id);
	}

	/**
	 * <br>
	 * <br>
	 * <b><I>boolean setStringListIntoListView(ArrayList<String> data, int
	 * lvID)</I></b> <br>
	 * 
	 * @param sdata
	 *            The string array to show in ListView.
	 * @param lvId
	 *            The ListView ID to fill in.
	 * @return True if Pass, False otherwise.
	 */
	public boolean setListViewfromStringArray(String[] sdata, int lvID) {

		try {
			ListView lv = this.getListView(lvID);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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

	// ____________Toggle Button_______________

	/**
	 * <br>
	 * <br>
	 * <b>public ToggleButton getToggleButton(int id)</b> <br>
	 * Finds a view that was identified by the id attribute from the XML that
	 * was processed in onCreate(Bundle).
	 * 
	 * @param id
	 *            Pass Toggle Button ID.
	 * @return The view if found or null otherwise.
	 */
	public ToggleButton getToggleButton(int id) {
		return (ToggleButton) this.findViewById(id);
	}

	// ____________Radio Button_______________

	/**
	 * <br>
	 * <br>
	 * <b>public RadioButton getRadioButton(int id)</b> <br>
	 * Finds a view that was identified by the id attribute from the XML that
	 * was processed in onCreate(Bundle).
	 * 
	 * @param id
	 *            Pass Radio Button ID.
	 * @return The view if found or null otherwise.
	 */
	public RadioButton getRadioButton(int id) {
		return (RadioButton) this.findViewById(id);
	}

	// ____________Spinner_______________

	/**
	 * <br>
	 * <br>
	 * <b>public Spinner getSpinner(int id)</b> <br>
	 * Finds a view that was identified by the id attribute from the XML that
	 * was processed in onCreate(Bundle).
	 * 
	 * @param id
	 *            Pass Spinner ID.
	 * @return The view if found or null otherwise.
	 */
	public Spinner getSpinner(int id) {
		return (Spinner) this.findViewById(id);
	}

	public String GetTxtFromSpinner(int id) {
		// TODO Auto-generated method stub
		Spinner sp = getSpinner(id);
		return sp.getSelectedItem().toString();
	}

	/**
	 * <br>
	 * <br>
	 * <b>public boolean SetSpinnerFromStringArray(String[] str, int spID)</b> <br>
	 * Fill Spinner with provided String array.
	 * 
	 * @param array
	 *            Pass String array
	 * @param spID
	 *            Pass Spinner ID.
	 * @return true if pass , fail otherwise.
	 */
	public boolean SetSpinnerFromStringArray(String[] str, int spID) {
		try {
			Spinner sp = this.getSpinner(spID);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, str);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			return true;
		} catch (Exception ex) {
			Log.e("Error: ", ex.toString());
			return false;
		}

	}

	/**
	 * <br>
	 * <br>
	 * <b>public boolean SetSpinnerFromArrayList(ArrayList<Object> array, int
	 * spID)</b> <br>
	 * Fill Spinner with provided ArrayList of AnyType Do not forget to write
	 * toString method in Type (used to display item in spinner) *
	 * 
	 * @param array
	 *            Pass ArrayList of any type
	 * @param spID
	 *            Pass Spinner ID.
	 * @return true if pass , fail otherwise.
	 */
	public <T> boolean SetSpinnerFromArrayList(ArrayList<T> array, int spID) {
		try {
			Spinner sp = this.getSpinner(spID);
			ArrayAdapter<T> adapter = new ArrayAdapter<T>(this,
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

	/**
	 * <br>
	 * <br>
	 * <b>public ProgressBar getProgressBar(int id)</b> <br>
	 * Finds a view that was identified by the id attribute from the XML that
	 * was processed in onCreate(Bundle).
	 * 
	 * @param id
	 *            Pass Progress Bar ID.
	 * @return The view if found or null otherwise.
	 */
	public ProgressBar getProgressBar(int id) {
		return (ProgressBar) this.findViewById(id);
	}

	// ____________Radio Group_______________

	/**
	 * <br>
	 * <br>
	 * <b>public RadioGroup getRadioGroup(int id)</b> <br>
	 * Finds a view that was identified by the id attribute from the XML that
	 * was processed in onCreate(Bundle).
	 * 
	 * @param id
	 *            Pass Radio Group ID.
	 * @return The view if found or null otherwise.
	 */
	public RadioGroup getRadioGroup(int id) {
		return (RadioGroup) this.findViewById(id);
	}

	// ____________Switch(Yes/No)_______________

	/**
	 * <br>
	 * <br>
	 * <b>public Switch getSwitch(int id)</b> <br>
	 * Finds a view that was identified by the id attribute from the XML that
	 * was processed in onCreate(Bundle).
	 * 
	 * @param id
	 *            Pass Radio Group ID.
	 * @return The view if found or null otherwise.
	 */
	public Switch getSwitch(int id) {
		return (Switch) this.findViewById(id);
	}

	// ____________Date Picker_______________

	/**
	 * <br>
	 * <br>
	 * <b>public DatePicker getDatePicker(int id)</b> <br>
	 * Finds a view that was identified by the id attribute from the XML that
	 * was processed in onCreate(Bundle).
	 * 
	 * @param id
	 *            Pass Date Picker ID.
	 * @return The view if found or null otherwise.
	 */
	public DatePicker getDatePicker(int id) {
		return (DatePicker) this.findViewById(id);
	}

	// ________________Network & GPS________________
	public boolean isNetworkAvailable() {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) this
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

	// ___________Date___________________

	@SuppressWarnings("deprecation")
	public String GetDateInString(int id) {
		DatePicker myDp = getDatePicker(id);

		Date dt = new Date(myDp.getYear() - 1900, myDp.getMonth(),
				myDp.getDayOfMonth());
		DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		return targetFormat.format(dt);
	}

	// /Get Date From Date Picker
	/**
	 * Get Date from Date Picker
	 * 
	 * @param id
	 * @return Date Object
	 */
	public Date GetDateFDP(int id) {
		DatePicker myDp = getDatePicker(id);

		@SuppressWarnings("deprecation")
		Date date = new Date(myDp.getYear() - 1900, myDp.getMonth(),
				myDp.getDayOfMonth());
		return date;

	}

	public String GetCurrentDateTimeInString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// get current date time with Date()
		Date date = new Date();
		// System.out.println(dateFormat.format(date)); don't print it, but save
		// it!
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

	public String GetCurrentDateInString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// get current date time with Date()
		Date date = new Date();
		// System.out.println(dateFormat.format(date)); don't print it, but save
		// it!
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

	public boolean IsNullOrWhiteSpace(String str) {
		if (str == null)
			return true;
		else if (str.trim() == "")
			return true;
		else if (TextUtils.isEmpty(str))
			return true;
		else if (str.contains("NA"))
			return true;
		else
			return false;
	}

	@SuppressWarnings("unchecked")
	public void setTxtToSpinner(int id, String text) {
		// the value you want the position for
		if (text != null) {
			Spinner mySpinner = (Spinner) this.findViewById(id);

			ArrayAdapter<String> myAdap = (ArrayAdapter<String>) mySpinner
					.getAdapter(); // cast to an ArrayAdapter

			int spinnerPosition = myAdap.getPosition(text);

			// set the default according to value
			mySpinner.setSelection(spinnerPosition);
		}
	}

	public String ConvertDateToString(Date date) {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// System.out.println(dateFormat.format(date)); don't print it, but save
		// it!
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

	@SuppressLint("SimpleDateFormat") public Date ConvertStringToDate(String saleFor) {
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
	public Date GetCurrentDate() {
		// TODO Auto-generated method stub
		Date MyDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// get current date time with Date()
		Date date = new Date();
		// System.out.println(dateFormat.format(date)); don't print it, but save
		// it!
		try {
			MyDate = dateFormat.parse(dateFormat.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MyDate;
	}

	public Calendar ConvertStringToCalender(String date) {
		// TODO Auto-generated method stub
		Date dt = ConvertStringToDate(date);
		return getDatePart(dt);
	}

	public String ConvertCalenderToString(Calendar cale) {
		// TODO Auto-generated method stub
		Date date = ConvertCalenderTODate(cale);
		return ConvertDateToString(date);
	}

	private static Date ConvertCalenderTODate(Calendar cale) {
		// TODO Auto-generated method stub
		return cale.getTime();

	}

	// /Get MIN And MAXDate From ARRAY LIST
	public Date getMINDateFromArryList(ArrayList<?> salesforupdate)
			throws IllegalArgumentException, IllegalAccessException {
		Date MyDate = new Date();
		// TODO Auto-generated method stub

		int i = 0;
		for (Object o : salesforupdate) {
			// Class<? extends Object> Type=o.getClass();
			Field f = null;
			try {
				f = o.getClass().getDeclaredField("ForDate");
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Date date = ConvertStringToDate(f.get(o).toString());
			if (i == 0)
				MyDate = date;
			else {
				if (date.before(MyDate))
					MyDate = date;
			}

			i++;
		}
		return MyDate;
	}

	public Date getMAXDateFromArryList(ArrayList<?> salesforupdate)
			throws IllegalArgumentException, IllegalAccessException {
		Date MyDate = new Date();
		// TODO Auto-generated method stub

		int i = 0;
		for (Object o : salesforupdate) {
			// Class<? extends Object> Type=o.getClass();
			Field f = null;
			try {
				f = o.getClass().getDeclaredField("ForDate");
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Date date = ConvertStringToDate(f.get(o).toString());
			if (i == 0)
				MyDate = date;
			else {
				if (date.after(MyDate))
					MyDate = date;
			}

			i++;
		}
		return MyDate;
	}

	/**
	 * Reverse Any Type ArrayList
	 * 
	 * @param List
	 * @return Reverse List.
	 */

	protected void Activate() {
		// TODO Auto-generated method stub

	}

	// passing data from one activity to another activity..
	public ApplicationBase getBase() {
		return this.AppBase;
	}

	@Override
	public void putDataTab(int TabIndex) {
		AppBase.putDataTab(TabIndex);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void putDataAsClass(String key, Class cls) {
		AppBase.putDataAsClass(key, cls);

	}

	@Override
	public <T> void putDataArrayList(String Key, ArrayList<T> data) {
		AppBase.putDataArrayList(Key, data);
	}

	@Override
	public <T> void putData(String Key, T data) {

		AppBase.putData(Key, data);
	}

	@Override
	public void putDataAsString(String Key, String Value) {

		AppBase.putDataAsString(Key, Value);
	}

	@Override
	public void putDataAsBoolean(String Key, boolean Value) {

		AppBase.putDataAsBoolean(Key, Value);
	}

	@Override
	public void putDataAsInt(String Key, int Value) {

		AppBase.putDataAsInt(Key, Value);
	}

	@Override
	public int getDataTab() {

		return AppBase.getDataTab();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getDataAsClass(String Key) {

		return AppBase.getDataAsClass(Key);
	}

	@Override
	public <T> T getData(String Key) {
		return AppBase.getData(Key);
	}

	@Override
	public String getDataAsString(String Key) {

		return AppBase.getData(Key);
	}

	@Override
	public boolean getDataAsBoolean(String Key) {

		return AppBase.getDataAsBoolean(Key);
	}

	@Override
	public int getDataAsInt(String Key) {

		return AppBase.getDataAsInt(Key);
	}

	@Override
	public <T> ArrayList<T> getDataArrayList(String Key) {

		return AppBase.getDataArrayList(Key);
	}

	@Override
	public void restartTab(Activity context) {

		AppBase.restartTab(context);
	}

}
