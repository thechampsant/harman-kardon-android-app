package app.core.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

/*import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;*/
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.lang.reflect.Field;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import app.core.async.TaskFragment;
import app.core.geoservice.LocationHelper;
import app.core.ibase.IDataIntent;
import app.core.model.DateModel;
import app.core.model.Keys;
import app.core.services.ErrorSender;
import app.core.services.LoginProvider;
import app.core.sqllite.DataEntity;
import app.core.sqllite.MySQLiteOpenHelper;
import app.core.sqllite.TableInfo;
import app.core.utils.Dialog;
import app.core.utils.IntentFactory;
import app.core.utils.Message;
import app.core.utils.Themes;
import app.core.utils.onDateSetListener;
import dragger.DragManager;
import dragger.IDragUpdater;
import linq.ArrayList;

@SuppressLint("SimpleDateFormat")
public abstract class InnosolsActivity extends FragmentActivity implements
		IDataIntent {

	// public GPSTracker GPS;
	public LocationHelper locationHelper;
    public LayoutInflater inflator = null;
    public LoginProvider User = null;
    public app.core.sqllite.MySQLiteOpenHelper db;
    public ProgressDialog progress;
    public TaskFragment mTaskFragment;
    SharedPreferences SP;
	SharedPreferences.Editor editor;
	InputMethodManager IMM;
    ArrayList<TableInfo> _Tables;
    // -------------------------drag
    // updater-----------------------------------//
    DragManager dragmanager = new DragManager(this);
    private View mView = null;
	private String LoginKey = "23283782_LOGGED_KEY";
	private String LoginUserNameKey = "34793274_absbas";
	private String IsFolderSaveKey = "FOLDER_SAVE_KEY_WEWEWE";
	private ApplicationBase AppBase = null;
	private LocalBroadcastManager uibroadcaster = null;
	private CustomDatePickerDialog datepicker = null;
    private String ThemeKey = "MMMTHEME";
    private String BackGroundKey = "BGTBackGroundKEY";
    private String BackgroundEnabledKey = "BTTGEKEY";
    private int SPLASH_TIME_OUT = 3000;
    private ListView DragListView = null;
    private ArrayList<View> views = new ArrayList<View>();
    private ProgressDialog progressDialog = null;
    private boolean IsBusy = false;
    // ---------- Un handled Exception-----------------------//
    private Thread.UncaughtExceptionHandler handleAppCrash = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            new ErrorSender(InnosolsActivity.this).sendErrorOnServer(
                    User.GetUserName(), ex);
            Log.e("error", ex.toString());

        }
    };

	public void EnableLocalDatabase(String DBName, int version) {
		this.RegisterTableInfoForLocalDB();
		db = new MySQLiteOpenHelper(this, DBName, version, _Tables);
		app.core.sqllite.DataEntity.db = db;
		// updated
	}

	@Override
	public void setContentView(int layoutResID) {
		mView = this.inflator.inflate(layoutResID, null);
		super.setContentView(mView);

	}

	public void setContentView(int layoutResID, boolean isViewGroup) {
		mView = this.inflator.inflate(layoutResID, null, true);
		super.setContentView(mView);

	}

	@Override
	public void setContentView(View view) {
		mView = view;
		super.setContentView(view);
	}


	public void startActivity(Class<?> cls) {
		Intent i = new Intent(this, cls);
		this.startActivity(i);
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

	/** Register model for tables */
	public abstract void RegisterTableInfoForLocalDB();

	public void RegisterTableInfo(TableInfo _tableinfo) {
		if (_Tables == null)
			_Tables = new ArrayList<TableInfo>();
		_Tables.add(_tableinfo);
	}

	public <T extends DataEntity<T>> void RegisterTableForDataEntity(T obj) {
		this.RegisterTableInfo(obj.GetTableInfo());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Init();
		if (IsFragmentAvilable())
			attachCallback();
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@SuppressLint({ "CommitPrefEdits", "WorldWriteableFiles" })
	private void Init() {
		// GPS=new GPSTracker(this);
        //	Thread.setDefaultUncaughtExceptionHandler(handleAppCrash);
        User = new LoginProvider(InnosolsActivity.this);
        SP = getApplicationContext().getSharedPreferences("GLOBAL_1", Context.MODE_PRIVATE);
        // SP = this.getPreferences(Context.MODE_WORLD_WRITEABLE);
		this.editor = SP.edit();
		IMM = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inflator = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);

		try
		{
			AppBase = (ApplicationBase) getApplicationContext();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public View inflateView(int resourceID) {
		return this.inflator.inflate(resourceID, null);
	}

	public void minimizeApp() {
		BringToHome();
	}

	private void BringToHome() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(startMain);
	}

	public void dismiss() {
		if (progress == null)
			return;
		progress.dismiss();
	}

	public ProgressDialog GetProgressDialog(String Message) {
		progress = new ProgressDialog(this);
		progress.setTitle("Please Wait!!");
		progress.setMessage(Message);
		progress.setCancelable(true);

		// progress.setButton("Cancel", CancelUpload);
		// progress.setProgress(0);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		return progress;

	}

	public String getCurrentVersion() {
		String version = "0.0";
		try {
			version = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return version;
	}

	// get hardware mac address
	public String getMACaddress() {
		// add following persimisin
		// <uses-permission android:name="android.permission.INTERNET" />
		// <uses-permission
		// android:name="android.permission.ACCESS_NETWORK_STATE" />
		String address = null;
		address = getMAC("wlan0"); // using wifi available
		if (address != null)
			return address;
		address = getMAC("eth0"); // using ethernet connection availale
		return address;
	}

	public String getMAC(String interfaceName) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName))
						continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac == null)
					return null;
				StringBuilder buf = new StringBuilder();
				for (int idx = 0; idx < mac.length; idx++)
					buf.append(String.format("%02X:", mac[idx]));
				if (buf.length() > 0)
					buf.deleteCharAt(buf.length() - 1);
				return buf.toString();
			}
		} catch (Exception ex) {
		}
		return null;

	}

	// Preference handling
	public void hideKeyPad(int controlid) {
		IMM.hideSoftInputFromWindow(this.findViewById(controlid)
				.getWindowToken(), 0);
	}

	public void hideKeyPad() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public boolean SavePreferences(String Key, String Value) {
		try {
			editor.putString(Key, Value);
			editor.commit();
			return true;
		} catch (Exception ex) {
			Log.e("Error:", ex.toString());
			return false;
		}
	}

	public String getSharedPreference(String key, String defValue) {
		return SP.getString(key, defValue);
	}

	public boolean RemovePrefrences(String Key) {
		editor.remove(Key);
		return true;
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup)
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// methods.....
	public void ShowToast(String Message) {
		Toast toast = Toast.makeText(this, Message, Toast.LENGTH_SHORT);
		toast.show();
	}

	// Toast Message
	public boolean IsNullOrWhiteSpace(String str) {
		if (TextUtils.isEmpty(str))
			return true;
        else return str.startsWith("null");
    }

	public void ShowToastLong(String Message, int Duration) {
		Toast toast = Toast.makeText(this, Message, Toast.LENGTH_LONG);
		toast.show();

	}

	public void setToggleCheckedChangeListener(int id,
			OnCheckedChangeListener lst) {
		ToggleButton tbutton = GetToggleButton(id);
		tbutton.setOnCheckedChangeListener(lst);
	}

	public boolean IsEmpty(String value) {
		return TextUtils.isEmpty(value);
	}

	// EditText Options
	public EditText GetEditText(int id) {
		return (EditText) this.findViewById(id);
	}

	public String GetEditTextAsString(int id) {
		return GetEditText(id).getText().toString();
	}

	public int GetEditTextAsInt(int id) {
		String txt = GetEditText(id).getText().toString();
		return ToInt(txt);
	}

	public double GetEditTextAsDouble(int id) {
		String txt = GetEditTextAsString(id);
		return Double.parseDouble(txt);
	}

	public void SetEditTextAsString(int id, String txt) {
		GetEditText(id).setText(txt);
	}

	public void SetEditTextFromObject(int id, Object obj) {
		String txt = "";
		if (obj != null)
			txt = obj.toString();
		GetEditText(id).setText(txt);
	}

	// TextView Options
	public TextView GetTextView(int id) {
		return (TextView) this.findViewById(id);
	}

	public String GetTextViewAsString(int id) {
		TextView tv = GetTextView(id);
		return tv.getText().toString();
	}

	public int GetTextViewAsInt(int id) {
		String txt = GetTextViewAsString(id);
		return Integer.parseInt(txt);
	}

	public double GetTextViewAsDouble(int id) {
		String txt = GetTextViewAsString(id);
		return Double.parseDouble(txt);
	}

	public void SetTextViewAsString(int id, String text) {
		TextView tv = GetTextView(id);
		tv.setText(text);
	}
	public void showView(View view){
		view.setVisibility(View.VISIBLE);
	}
	public void hideView(View view){
		view.setVisibility(View.GONE);
	}

	// Toggle Button
	public ToggleButton GetToggleButton(int id) {
		return (ToggleButton) this.findViewById(id);
	}

	// Button Options
	public Button GetButton(int id) {
		return (Button) this.findViewById(id);
	}

	public ImageButton getImageButton(int id) {
		return (ImageButton) this.findViewById(id);
	}

	public ImageView getImageView(int id) {
		return (ImageView) this.findViewById(id);
	}

	public View setVisibility(int id, int visibility) {
		View view = this.findViewById(id);
		view.setVisibility(visibility);
		return view;
	}

	public void SetButtonLabel(int id, String text) {
		Button btn = GetButton(id);
		btn.setText(text);
	}

	public void SetButtonLabel(int id, int textResID) {
		Button btn = GetButton(id);
		btn.setText(textResID);
	}

	public void SetOnClickListenerOnButton(int id, OnClickListener l) {
		Button btn = GetButton(id);
		btn.setOnClickListener(l);
	}

	public void setOnClickListenerOnImageButton(int id, OnClickListener l) {
		ImageButton btn = getImageButton(id);
		btn.setOnClickListener(l);
	}

	public void SetOnClickListenerOnEditText(int id, OnClickListener l) {
		GetEditText(id).setOnClickListener(l);
	}

	// CheckBox Options
	public CheckBox GetCheckBox(int id) {
		return (CheckBox) this.findViewById(id);
	}

	public Boolean GetCheckBoxStatus(int id) {
		return GetCheckBox(id).isChecked();
	}

    // Integer Parsing

	@SuppressWarnings("unchecked")
	public void SetTxtToSpinner(int id, String text) {
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

	public void SetCheckBoxStatus(int id, Boolean Status) {
		GetCheckBox(id).setChecked(Status);
	}

    // ____________________________Shared Preferences

	public String GetCheckBoxText(int id) {
		return GetCheckBox(id).getText().toString();
	}

	public void SetCheckBoxText(int id, String text) {
		GetCheckBox(id).setText(text);
	}

	public void showHideYear(int id, int visiblity) {
		try {
			Date date = new Date();
			DatePicker dp = (DatePicker) this.findViewById(id);
			Field f[] = dp.getClass().getDeclaredFields();
			for (Field field : f) {
				if (field.getName().equals("mYearSpinner")
						|| field.getName().equals("mYearPicker")) {
					field.setAccessible(true);
					Object yearPicker = new Object();
					yearPicker = field.get(date);
					((View) yearPicker).setVisibility(visiblity);
				}
			}
		} catch (SecurityException e) {
			Log.d("ERROR", e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.d("ERROR", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.d("ERROR", e.getMessage());
		}
	}

	public void test(int id) {
		DatePicker dp = (DatePicker) this.findViewById(id);
		Field[] fields = dp.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("mYearSpinner")
					|| field.getName().equals("mYearPicker")) {
				field.setAccessible(true);
				Object yearPicker = new Object();
				try {
					yearPicker = field.get(dp);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ((View) yearPicker).setVisibility(View.GONE);
				((View) yearPicker).setEnabled(false);

				// String name=field.getName();
			}
			if (field.getName().equals("DEFAULT_START_YEAR")) {
				try {
					field.setAccessible(true);
					Object startyear = new Object();
					try {
						startyear = field.get(dp);

					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// ((View) startyear).setEnabled(false);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (field.getName().equals("DEFAULT_END_YEAR")) {
				try {
					field.setAccessible(true);
					Object endyear = new Object();
					field.set(dp, 2013);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public CustomDatePickerDialog getDatePicker() {
		if (this.datepicker == null) {
			DateModel model = new DateModel().getCurrentDateModel();
			this.datepicker = new CustomDatePickerDialog(this, null,
					model.Year, model.Month, model.Day);
		}
		return this.datepicker;
	}

    // ____________Spinner_______________

	public void attachDatePicker(int id) {
		this.getView(id).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getDatePicker().show(v.getId());
			}
		});
	}

	public void attachDatePicker(int id, onDateSetListener listener) {
		this.getView(id).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getDatePicker().show(v.getId());
			}
		});
		this.getDatePicker().addListener(id, listener);
	}

	// Date Time Options
	public Calendar getDatePart(Date date) {
		Calendar cal = Calendar.getInstance(); // get calendar instance
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
		cal.set(Calendar.MINUTE, 0); // set minute in hour
		cal.set(Calendar.SECOND, 0); // set second in minute
		cal.set(Calendar.MILLISECOND, 0); // set millisecond in second
		return cal; // return the date part
	}

	public Date getBackDate(int day) {
		Calendar cal = this.ConvertStringToCalender(this
				.GetCurrentDateInString());
		cal.add(Calendar.DAY_OF_MONTH, -day);
		Date bDate = this
				.ConvertStringToDate(this.ConvertCalenderToString(cal));
		return bDate;
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

    // ____________Progresss Bar_______________

	public Integer ToInt(String input) {
		// TODO Auto-generated method stub
		int output = -1;

		try {
			// go on as normal
			output = Integer.parseInt(input);
			return output;

		} catch (Exception e) {
			// handle error
			return output;
		}
	}

    // ____________Radio Group_______________

	public double ToDouble(String input) {
		// TODO Auto-generated method stub
		double output = 0.0;

		try {
			// go on as normal

			output = Double.parseDouble(input);
			return output;

		} catch (Exception e) {
			// handle error
			return output;
		}
	}

    // ____________Switch(Yes/No)_______________

	public ListView getListView(int id) {
		return (ListView) this.findViewById(id);
	}

    // ____________Date Picker_______________

	public LinearLayout getLinearLayout(int id) {
		return (LinearLayout) this.findViewById(id);
	}

	public RelativeLayout getRelativeLayout(int id) {
		return (RelativeLayout) this.findViewById(id);
	}

	public boolean setListViewfromStringArray(String[] sdata, int lvID) {

		try {
			ListView lv = getListView(lvID);
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

	public RadioButton getRadioButton(int id) {
		return (RadioButton) this.findViewById(id);
	}

    // /Get Date From Date Picker

	public Spinner getSpinner(int id) {
		return (Spinner) this.findViewById(id);
	}

	public String getSpinnerAsString(int id) {
		return getSpinner(id).getSelectedItem().toString();
	}

	public String GetTxtFromSpinner(int id) {
		// TODO Auto-generated method stub
		Spinner sp = getSpinner(id);
		return sp.getSelectedItem().toString();
	}

	public boolean SetSpinnerFromStringArray(String[] str, int spID) {
		try {
			Spinner sp = getSpinner(spID);
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

	public <T> boolean SetSpinnerFromArrayList(ArrayList<T> array, int spID) {
		try {
			Spinner sp = getSpinner(spID);
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

	public ProgressBar getProgressBar(int id) {
		return (ProgressBar) this.findViewById(id);
	}

	public RadioGroup getRadioGroup(int id) {
		return (RadioGroup) this.findViewById(id);
	}

	public Switch getSwitch(int id) {
		return (Switch) this.findViewById(id);
	}

	public DatePicker getDatePicker(int id) {
		return (DatePicker) this.findViewById(id);
	}

	// ___________Date___________________
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

	@SuppressWarnings("deprecation")
	public String GetDateInString(int id) {
		DatePicker myDp = getDatePicker(id);
		Date dt = new Date(myDp.getYear() - 1900, myDp.getMonth(),
				myDp.getDayOfMonth());
		DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		return targetFormat.format(dt);
	}

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
		Date date = new Date();
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

	public String getTimeInHours(String date) {
		return getTimeInHours(ConvertStringToDateTime(date));
	}

	public String getTimeInHours(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		String time = dateFormat.format(date);
		return time;
	}

	public String getCurrentTimeInHours() {
		return getTimeInHours(GetCurrentDateTimeInString());
	}

	public String GetCurrentDateInString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();

		String MyDate = dateFormat.format(date);
		return MyDate;

	}

	public Date getYesterdayDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);
		Date newdate = cal.getTime();
		return newdate;

	}

	@SuppressWarnings("deprecation")
	public String getMonth(String date) {
		Date mdate = ConvertStringToDate(date);
		return ToString(mdate.getMonth());
	}

	@SuppressWarnings("deprecation")
	public String getYear(String date) {
		Date mdate = ConvertStringToDate(date);
		return ToString(mdate.getYear());
	}

	public String getMonthYear(String date) {
		Date mdate = ConvertStringToDate(date);
		return getMonthYear(mdate);
	}

	@SuppressWarnings("deprecation")
	public String getDateInDayMonthFormat(Date date) {
		return date.getDate() + "/" + getMonthNameForInt(date.getMonth());
	}

	public String getDateInDayMonthFormat(String date) {
		return getDateInDayMonthFormat(ConvertStringToDate(date));
	}

	public String getDateInDayMonthYearFormat(String date) {
		return getDateInDayMonthYearFormat(this.ConvertStringToDate(date));
	}

	@SuppressWarnings("deprecation")
	public String getDateInDayMonthYearFormat(Date date) {
		return date.getDate() + "/" + getMonthYear(date);
	}

	public String getMonthYear(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("MMM/yyyy");
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

	public String getMonthNameForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num].substring(0, 3);
		}
		return month;
	}

	public String getDateString(String date) {
		return ConvertDateToString(ConvertStringToDate(date));
	}

	public String ConvertDateToString(Date date) {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String MyDate = dateFormat.format(date);
		return MyDate;
	}

	private List<SimpleDateFormat> getDateFormats() {
		ArrayList<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>();
		dateFormats.add(new SimpleDateFormat("yyyy-MM-dd"));
		dateFormats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
		dateFormats.add(new SimpleDateFormat("dd-MMM-yyyy"));
		dateFormats.add(new SimpleDateFormat("dd MMM,yyyy"));
		dateFormats.add(new SimpleDateFormat("M/dd/yyyy"));
		dateFormats.add(new SimpleDateFormat("dd.M.yyyy"));
		dateFormats.add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
		dateFormats.add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
		dateFormats.add(new SimpleDateFormat("dd.MMM.yyyy"));

		return dateFormats;
	}

	public Date ConvertStringToDate(String sdate) {
		Date date = null;

		if (null == sdate) {
			return null;
		}
		for (SimpleDateFormat format : getDateFormats()) {
			try {
				format.setLenient(false);
				date = format.parse(sdate);
			} catch (ParseException e) {
				// Shhh.. try other formats
			}
			if (date != null) {
				break;
			}
		}
		return date;
	}

	public Date ConvertStringToDateTime(String date) {
		// TODO Auto-generated method stub
		Date cdate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			return cdate = dateFormat.parse(date);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return cdate;
	}

	@SuppressWarnings("deprecation")
	public Date getCurrentDate() {
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

	private Date ConvertCalenderTODate(Calendar cale) {
		// TODO Auto-generated method stub
		return cale.getTime();

	}

	public Boolean ToBool(String _value) {
		return _value.equalsIgnoreCase("true");
	}

    // Background Handling...........////////////////////////

	public void log(String tag, String msg) {
		Log.i(tag, msg);
	}

	public String ToString(Object obj) {
		if (obj != null)
			return obj.toString();
		else
			return "Failed";
	}

	public boolean isNetworkAvailable() {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null)
			isConnected = activeNetworkInfo.isConnected();
        return activeNetworkInfo != null && isConnected;
    }

	public boolean isNetworkFoundToast() {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null)
			isConnected = activeNetworkInfo.isConnected();
		if (activeNetworkInfo != null && isConnected)
			return true;
		else {
			this.ShowToast(Message.NO_INTERNET_FOUND);
			return false;
		}
	}

	public boolean isNetworkFoundDialog() {
		boolean isConnected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null)
			isConnected = activeNetworkInfo.isConnected();
		if (activeNetworkInfo != null && isConnected)
			return true;
		else {
			new Dialog(this).show(Message.NO_INTERNET_FOUND);
			return false;
		}
	}

	// opacity control
	public View getView(int ID) {
		return this.findViewById(ID);
	}

	public View setOpacity(View view) {
		// TODO Auto-generated method stub
		setOpacity(view, 0.5F);
		return view;
	}

	public View setOpacity(View view, float value) {
		setOpacity(view, value, value);
		return view;
	}

	public View setOpacity(int LayoutID) {
		// TODO Auto-generated method stub
		return setOpacity(getView(LayoutID));
	}

	public View setOpacity(int LayoutID, float value) {
		return setOpacity(getView(LayoutID), value);
	}

    // Themes Handling...........////////////////////////

	public View setOpacity(int LayoutID, float toleft, float toRight) {
		// TODO Auto-generated method stub
		return setOpacity(getView(LayoutID), toleft, toRight);

	}

	public View setOpacity(View view, float toleft, float toRight) {
		AlphaAnimation alpha = new AlphaAnimation(toleft, toRight);
		alpha.setDuration(1); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		// And then on your layout
		view.startAnimation(alpha);
		return view;
	}

	public void saveCurrentBackground(int backgroundid) {
		this.enableBackground(true);
		this.SavePreferences(BackGroundKey, String.valueOf(backgroundid));
	}

	public boolean IsBackGroundEnable() {
		return ToBool(getSharedPreference(BackgroundEnabledKey, "false"));
	}

	private void enableBackground(boolean enable) {
		this.SavePreferences(BackgroundEnabledKey, String.valueOf(enable));
	}

	private int getCurrentBackground() {
		return ToInt(getSharedPreference(BackGroundKey, "-1"));
	}

	public void resetToDefaultBackground() {
		this.enableBackground(false);
	}

	public View getInflatedView(int LayoutID) {
        View view = this.inflator.inflate(LayoutID, null);
        return view;
	}

    // ------------------------------------------End --------------------------

    // ------------------------------Fragement Activity----------------------//

	public void setBackground(int LayoutID) {
		try {
			try {
				LinearLayout lyt = getLinearLayout(LayoutID);
				if (IsBackGroundEnable() && lyt != null)
					lyt.setBackgroundResource(getCurrentBackground());
				else if (lyt != null)
					lyt.setBackgroundDrawable(null);
			} catch (Exception ex) {
				try {
					RelativeLayout lyt = getRelativeLayout(LayoutID);
					if (IsBackGroundEnable() && lyt != null)
						lyt.setBackgroundResource(getCurrentBackground());
					else if (lyt != null)
						lyt.setBackgroundDrawable(null);
				} catch (Exception ex2) {
					Log.e("Setting background", ex2.getMessage());
				}
			}
		} catch (Exception ex) {
			resetToDefaultBackground();
		}
	}

	public void setSupportedTheme() {
		if (this.getAndroidVersion() > 11)
			this.setApplicationTheme(Themes.HoloLight);
		else
			this.setApplicationTheme(Themes.Light);
	}

	private int getCurrentTheme() {
		return ToInt(getSharedPreference(ThemeKey,
				String.valueOf(app.core.utils.Themes.Light)));
	}

	public void saveCurrentTheme(int themeid) {
		this.SavePreferences(ThemeKey, String.valueOf(themeid));
	}

	public void resetThemeToDefault() {
		saveCurrentTheme(app.core.utils.Themes.Light);
		getApplication().setTheme(app.core.utils.Themes.Light);
	}

    // ------------------- Splash screen
    // logic----------------------------------/////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setActivityTheme(int Layout, boolean IsHomeEnabled) {

		try {
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
			setTheme(getCurrentTheme());
			setContentView(Layout);
			if (IsHomeEnabled) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			} else {
				ActionBar actionBar = getActionBar();
				actionBar.show();
			}
		} catch (Exception Ex) {
			resetThemeToDefault();
		}
	}

	public void setApplicationTheme(int themeid, Activity current) {
		try {
			saveCurrentTheme(themeid);
			getApplication().setTheme(themeid);
			restartActivity(current);
		} catch (Exception ex) {
			resetThemeToDefault();
		}
	}

	public void setApplicationTheme(int themeid) {
		try {
			saveCurrentTheme(themeid);
			getApplication().setTheme(themeid);
		} catch (Exception ex) {
			resetThemeToDefault();
		}
	}

	public void restartActivity(Activity current) {
		Intent I = new Intent(this, current.getClass());
		startActivity(I);
		finish();
	}

    // --------------------- Restarting Tab-------------------------//

	public int getAndroidVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	private boolean IsFragmentAvilable() {
        return Build.VERSION.SDK_INT >= 11;
    }

	@SuppressLint("NewApi")
	private void attachCallback() {
		// TODO Auto-generated method stub

		FragmentManager fm = getFragmentManager();
		mTaskFragment = (TaskFragment) fm.findFragmentByTag("task");

		// If the Fragment is non-null, then it is currently being
		// retained across a configuration change.
		if (mTaskFragment == null) {
			mTaskFragment = new TaskFragment();
			fm.beginTransaction().add(mTaskFragment, "task").commit();
		}
	}

    // ---------------------------login handling----------------------------//

	public void deAttachCallback() {
		mTaskFragment = null;
	}

	public void setSplashView(int layoutResID, int SplashLayoutResID,
			int displayTime) {
		SPLASH_TIME_OUT = displayTime;
		SplashScreen(layoutResID, SplashLayoutResID);
	}

	public void setSplashView(int layoutResID, int SplashLayoutResID) {
		SplashScreen(layoutResID, SplashLayoutResID);
	}

	private void SplashScreen(final int layoutResID, int SplashLayoutResID) {
		setContentView(SplashLayoutResID);
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				// Intent i = new Intent(this, MainActivity.class);
				// startActivity(i);
				setContentView(layoutResID);
				// close this activity
				// finish();
			}
		}, SPLASH_TIME_OUT);
	}

	@SuppressWarnings("rawtypes")
	public void setTab(int tabIndex) {
		Class cls = IntentFactory.getDataAsClass(Keys.TabItemClass);
		if (cls == null) {
			this.ShowToast("Unable to shift tab! tab class missing!");
			return;
		} else {
			IntentFactory.putDataTab(tabIndex);
			Intent intent = new Intent(this, cls);
			this.startActivity(intent);
			this.finish();
		}
	}

	// ----------------------------dp to pixel convert----------------//
	public float convertDpToPixel(float dp) {
		Resources resources = this.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public float convertPixelsToDp(float px) {
		Resources resources = this.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public void setUserLogged(String UserName, boolean islogged) {
		this.LoginKey = UserName;
		this.RemovePrefrences(LoginKey);
		this.RemovePrefrences(LoginUserNameKey);
		this.SavePreferences(LoginKey, this.ToString(islogged));
		this.SavePreferences(LoginUserNameKey, UserName);
	}

	public void setUserLogged(boolean islogged) {
		this.SavePreferences(LoginKey, this.ToString(islogged));
	}

	public boolean isUserLoggedIn(boolean haveuser) {
		if (haveuser) {
			LoginKey = this.getSharedPreference(LoginUserNameKey, "NA");
			return ToBool(this.getSharedPreference(LoginKey, "false"));
		} else {
			return ToBool(this.getSharedPreference(LoginKey, "false"));
		}
	}

	public View getCurrentView() {
		return this.mView;
	}

	public ListView getListView() {
		return this.DragListView;
	}

    // ---------------------- Busy indicator logic----------------------------//
    // ----------------------------------------------------------------------//

	public void setOnDragUpdater(IDragUpdater _IDragUpdater) {
		dragmanager.setOnDragUpdater(_IDragUpdater);
		setAutoDragUpdater();
	}

	public void setLoading(boolean IsLoading) {
		dragmanager.setLoading(false);
	}

	private void setAutoDragUpdater() {
		this.getAllChildren(this.mView);
		for (View view : views) {
			if ((view instanceof ListView)) {
				DragListView = (ListView) view;
				dragmanager.setContainer(view);
				break;
			}
		}
	}

	private ArrayList<View> getAllChildren(View v) {

		if (!(v instanceof ViewGroup)) {
			ArrayList<View> viewArrayList = new ArrayList<View>();
			viewArrayList.add(v);
			return viewArrayList;
		}

		ArrayList<View> result = new ArrayList<View>();

		ViewGroup viewGroup = (ViewGroup) v;
		for (int i = 0; i < viewGroup.getChildCount(); i++) {

			View child = viewGroup.getChildAt(i);
			views.add(child);
			ArrayList<View> viewArrayList = new ArrayList<View>();
			viewArrayList.add(v);
			viewArrayList.addAll(getAllChildren(child));

			// views.addAll(viewArrayList);
		}
		return result;
	}

	public void showProgress(String ProgressMessage, boolean ShowProgressTitle) {
		try {
			if (IsBusy)
				return;

			progressDialog = new ProgressDialog(this);

			progressDialog.setMessage(ProgressMessage);
			progressDialog.setCancelable(true);
			progressDialog.setCancelable(false);

			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			if (ShowProgressTitle)
				progressDialog.setTitle("Please Wait!!");

			if (!IsBusy || !progressDialog.isShowing()) {
				IsBusy = true;
				progressDialog.show();
			}
		} catch (Exception ex) {
			Log.e("Progress popup","generation error "+ ex.getMessage());
		}

	}

	public void showProgress(String ProgressMessage) {
		showProgress(ProgressMessage, true);

	}

	public void showProgress(boolean ShowProgressTitle) {
		showProgress("loading", ShowProgressTitle);

	}

    // --------------------GPS Tracker----------------------------//

	public void showProgress() {
		showProgress("loading", true);

	}

	public void hideProgress() {
		try {
			if (IsBusy && progressDialog != null) {
				IsBusy = false;
				progressDialog.cancel();
				progressDialog = null;

			}
		} catch (Exception ex) {
			Log.e("Progress cancelation", ex.getMessage());
		}
	}

    // ---------------- device information------------------------//

	public void InvokeGPSService() {
		locationHelper = new LocationHelper(this);
		/*gps = new GPSService(this);
		gps.IsGPSActive();*/
	}

    // -------------------image save information------------------//
/*
	public String getDeviceId() {
		// TODO Auto-generated method stub
		try {
			TelephonyManager mTelephonyMgr;
			mTelephonyMgr = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);

			String yourDeviceID = mTelephonyMgr.getDeviceId();
			return yourDeviceID;
		} catch (Exception ex) {
			return "NA";
		}
	}*/

	public boolean IsFolderSave() {
        return !getSharedPreference(IsFolderSaveKey, "false").equalsIgnoreCase(
                "false");

	}

	public void changeImageSavePreference(boolean IsSaveInFolder) {
		this.SavePreferences(IsFolderSaveKey, ToString(IsSaveInFolder));

	}

	// --------------- Phone Auto time checking---------//

	public boolean isAutoTimeActive() {
		return android.provider.Settings.System.getInt(getContentResolver(),
				android.provider.Settings.System.AUTO_TIME, 0) != 0;
	}

	public boolean autoTimeAlert(DialogInterface.OnClickListener listener) {
		boolean isactive = isAutoTimeActive();
		if (!isactive) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setNegativeButton("Cancel", listener)
					.setTitle("Phone date is invalid")
					.setMessage("set Automatic date & time..!")
					.setPositiveButton("Set Auto",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									goToAutoTimeActivity();
								}
							}).show();
		}
		return isactive;
	}

	private void goToAutoTimeActivity() {
		startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
	}

	// passing custom data from one activity to another activity..........
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

	public void sendBroadCastToUI(Intent intent) {
		this.uibroadcaster.sendBroadcast(intent);
	}

	public void invokeUiBroadCastor() {
		this.uibroadcaster = LocalBroadcastManager.getInstance(this);
	}

	// @Override
	// protected void onStop() {
	// if (AppBase != null)
	// //AppBase.map.clear();
	// super.onStop();
	// }

}
