package app.core.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
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


import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;
import java.net.NetworkInterface;
import java.text.DateFormat;
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
import app.core.services.LoginProvider;
import app.core.sqllite.DataEntity;
import app.core.sqllite.MySQLiteOpenHelper;
import app.core.sqllite.TableInfo;
import app.core.utils.Dialog;
import app.core.utils.IntentFactory;
import app.core.utils.Message;
import app.core.utils.onDateSetListener;
import dragger.DragManager;
import dragger.IDragUpdater;
import linq.ArrayList;
import tab.fragments.FragmentTabActivity;

@SuppressLint("NewApi")
public abstract class IFragment extends Fragment implements IDataIntent {

    public View mView = null;
    public InnosolsActivity context = null;
    public LoginProvider User = null;
    public LayoutInflater inflator = null;
    public app.core.sqllite.MySQLiteOpenHelper db;
    public ProgressDialog progress;
    public TaskFragment mTaskFragment;
    public LocationHelper gps = null;
    // public GPSTracker GPS;
    SharedPreferences SP;
    SharedPreferences.Editor editor;
    InputMethodManager IMM;

    // ---------------- controls methods----------------------------//
    ArrayList<TableInfo> _Tables;
    // -------------------------drag
    // updater-----------------------------------//
    DragManager dragmanager = null;
    private CustomDatePickerDialog datepicker = null;
    private String IsFolderSaveKey = "FOLDER_SAVE_KEY_WEWEWE";
    private String ThemeKey = "MMMTHEME";
    private String BackGroundKey = "BGTBackGroundKEY";
    private String BackgroundEnabledKey = "BTTGEKEY";
    private ListView DragListView = null;
    private ArrayList<View> views = new ArrayList<View>();
    private ProgressDialog progressDialog = null;
    private boolean IsBusy = false;

    public abstract void Activate(View FragmentView);

    public abstract View onCreateView(LayoutInflater inflater,
                                      ViewGroup container, Bundle savedInstanceState);

    public View InflateView(int resource, LayoutInflater inflater,
                            ViewGroup container) {
        mView = inflater.inflate(resource, container, false);
        Invoke((InnosolsActivity) getActivity(), inflater);
        Activate(mView);
        return mView;
    }

    public void Invoke(InnosolsActivity context, LayoutInflater inflater) {
        this.context = context;
        User = context.User;
        dragmanager = new DragManager(this.context);
        InitilizeFragmentControl(inflater);
    }

    private void InitilizeFragmentControl(LayoutInflater inflater) {
        this.inflator = inflater;
        Init();
        if (IsFragmentAvilable())
            attachCallback();
    }

    public void startService(Class<?> cls) {
        Intent i = new Intent(this.context, cls);
        context.startService(i);
    }

    public void startActivity(Class<?> cls) {
        Intent i = new Intent(this.context, cls);
        this.startActivity(i);
    }

    public void EnableLocalDatabase(String DBName, int version) {
        this.RegisterTableInfoForLocalDB();
        db = new MySQLiteOpenHelper(this.context, DBName, version, _Tables);
        app.core.sqllite.DataEntity.db = db;
    }

    /**
     * Use this Method to Enable Local Database with Default Versioning.
     *
     * @param DBName The name of database for local storage
     */
    public void EnableLocalDatabase(String DBName) {
        this.EnableLocalDatabase(DBName, 1);
    }

    public void RegisterTableInfoForLocalDB() {
    }

    public void RegisterTableInfo(TableInfo _tableinfo) {
        if (_Tables == null)
            _Tables = new ArrayList<TableInfo>();
        _Tables.add(_tableinfo);
    }

    public <T extends DataEntity<T>> void RegisterTableForDataEntity(T obj) {
        this.RegisterTableInfo(obj.GetTableInfo());
    }

    @SuppressWarnings("deprecation")
    private void Init() {
        // GPS=new GPSTracker(this);
        SP = context.getApplicationContext().getSharedPreferences("GLOBAL",
                Context.MODE_PRIVATE);
        // SP = this.getPreferences(Context.MODE_WORLD_WRITEABLE);
        this.editor = SP.edit();
        IMM = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
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

    public String getCurrentVersion() {
        String version = "0.0";
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
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
        IMM.hideSoftInputFromWindow(mView.findViewById(controlid)
                .getWindowToken(), 0);
    }

    public void hideKeyPad() {
        context.getWindow().setSoftInputMode(
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
        if (IsNullOrWhiteSpace(Message))
            return;
        Toast toast = Toast.makeText(context, Message, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Toast Message
    public boolean IsNullOrWhiteSpace(String str) {
        if (TextUtils.isEmpty(str))
            return true;
        else if (str.startsWith("null"))
            return true;
        else
            return false;
    }

    public void ShowToastLong(String Message, int Duration) {
        if (IsNullOrWhiteSpace(Message))
            return;
        Toast toast = Toast.makeText(context, Message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void setToggleCheckedChangeListener(int id,
                                               OnCheckedChangeListener lst) {
        ToggleButton tbutton = GetToggleButton(id);
        tbutton.setOnCheckedChangeListener(lst);
    }

    public View findViewById(int id) {
        return this.mView.findViewById(id);
    }

    public boolean IsEmpty(String value) {
        return TextUtils.isEmpty(value);
    }

    // EditText Options
    public EditText GetEditText(int id) {
        return (EditText) this.mView.findViewById(id);
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
        return (TextView) this.mView.findViewById(id);
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

    public void SetTextViewAsString(int id, String text)
    {
        TextView tv = GetTextView(id);
        tv.setText(text);
    }

    // Toggle Button
    public ToggleButton GetToggleButton(int id) {
        return (ToggleButton) this.mView.findViewById(id);
    }

    // Button Options
    public Button GetButton(int id) {
        return (Button) this.mView.findViewById(id);
    }

    public ImageView getImageView(int id) {
        return (ImageView) this.mView.findViewById(id);
    }

    public View setVisibility(int id, int visibility) {
        View view = this.mView.findViewById(id);
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

    public void SetOnClickListenerOnEditText(int id, OnClickListener l) {
        GetEditText(id).setOnClickListener(l);
    }

    // CheckBox Options
    public CheckBox GetCheckBox(int id) {
        return (CheckBox) this.mView.findViewById(id);
    }

    public Boolean GetCheckBoxStatus(int id) {
        return GetCheckBox(id).isChecked();
    }

    // Integer Parsing

    public String getSpinnerAsString(int id) {
        return getSpinner(id).getSelectedItem().toString();
    }

    // ____________________________Shared Preferences

    @SuppressWarnings("unchecked")
    public void SetTxtToSpinner(int id, String text) {
        // the value you want the position for
        if (text != null) {
            Spinner mySpinner = (Spinner) this.mView.findViewById(id);

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

    public String GetCheckBoxText(int id) {
        return GetCheckBox(id).getText().toString();
    }

    public void SetCheckBoxText(int id, String text) {
        GetCheckBox(id).setText(text);
    }

    public Date getBackDate(int day) {
        Calendar cal = this.ConvertStringToCalender(this
                .GetCurrentDateInString());
        cal.add(Calendar.DAY_OF_MONTH, -day);
        Date bDate = this
                .ConvertStringToDate(this.ConvertCalenderToString(cal));
        return bDate;
    }

    // ____________Spinner_______________

    public void showHideYear(int id, int visiblity) {
        try {
            Date date = new Date();
            DatePicker dp = (DatePicker) this.mView.findViewById(id);
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
        DatePicker dp = (DatePicker) this.mView.findViewById(id);
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

    public ListView getListView(int id) {
        return (ListView) this.mView.findViewById(id);
    }

    // ____________Switch(Yes/No)_______________

    public LinearLayout getLinearLayout(int id) {
        return (LinearLayout) this.mView.findViewById(id);
    }

    // ____________Date Picker_______________

    public RelativeLayout getRelativeLayout(int id) {
        return (RelativeLayout) this.mView.findViewById(id);
    }

    // ________________Network & GPS________________

    // public String Getlongitude() {
    // // TODO Auto-generated method stub
    //
    // if (GPS.canGetLocation()) {
    //
    // double latitude = GPS.getLatitude();
    // double longitude = GPS.getLongitude();
    // return longitude + "";
    // } else {
    // GPS.showSettingsAlert();
    // }
    //
    // return "0";
    // }
    //
    // public String Getlatitude() {
    // // if(lastLocation != null) return lastLocation.getLatitude()+"";
    // if (GPS.canGetLocation()) {
    //
    // double latitude = GPS.getLatitude();
    // double longitude = GPS.getLongitude();
    // return latitude + "";
    // } else {
    // GPS.showSettingsAlert();
    // }
    //
    // return "0";
    // }

    // ___________Date___________________

    public boolean setListViewfromStringArray(String[] sdata, int lvID) {

        try {
            ListView lv = getListView(lvID);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
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
        return (RadioButton) this.mView.findViewById(id);
    }

    public Spinner getSpinner(int id) {
        return (Spinner) this.mView.findViewById(id);
    }

    public String GetTxtFromSpinner(int id) {
        // TODO Auto-generated method stub
        Spinner sp = getSpinner(id);
        return sp.getSelectedItem().toString();
    }

    // /Get Date From Date Picker

    public boolean SetSpinnerFromStringArray(String[] str, int spID) {
        try {
            Spinner sp = getSpinner(spID);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
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
            ArrayAdapter<T> adapter = new ArrayAdapter<T>(context,
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
        return (ProgressBar) this.mView.findViewById(id);
    }

    public RadioGroup getRadioGroup(int id) {
        return (RadioGroup) this.mView.findViewById(id);
    }

    public Switch getSwitch(int id) {
        return (Switch) this.mView.findViewById(id);
    }

    public DatePicker getDatePicker(int id) {
        return (DatePicker) this.mView.findViewById(id);
    }

    public CustomDatePickerDialog getDatePicker() {
        if (this.datepicker == null) {
            DateModel model = new DateModel().getCurrentDateModel();
            this.datepicker = new CustomDatePickerDialog(context, null,
                    model.Year, model.Month, model.Day);
        }
        return this.datepicker;
    }

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

    public String GetCurrentDateInString() {
        // TODO Auto-generated method stub
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String MyDate = dateFormat.format(date);
        return MyDate;
    }

    public String ConvertDateToString(Date date) {
        // TODO Auto-generated method stub
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String MyDate = dateFormat.format(date);
        return MyDate;
    }

    public Date ConvertStringToDate(String date) {

        return context.ConvertStringToDate(date);

    }

    @SuppressWarnings("deprecation")
    public Date GetCurrentDate() {
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

    public void log(String tag, String msg) {
        Log.i(tag, msg);
    }

    public String ToString(Object obj) {
        if (obj != null)
            return obj.toString();
        else
            return "Failed";
    }

    // Background Handling...........////////////////////////

    public boolean isNetworkAvailable() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
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

    public boolean isNetworkFoundToast() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
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
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null)
            isConnected = activeNetworkInfo.isConnected();
        if (activeNetworkInfo != null && isConnected)
            return true;
        else {
            new Dialog(context).show(Message.NO_INTERNET_FOUND);
            return false;
        }
    }

    // opacity control
    public View getView(int ID) {
        return this.mView.findViewById(ID);
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
        View view = (View) this.inflator.inflate(LayoutID, null);
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
                    Log.e("Setting background image", ex2.getMessage());
                }
            }
        } catch (Exception ex) {
            resetToDefaultBackground();
        }
    }

    // Themes Handling...........////////////////////////
    private int getCurrentTheme() {
        return ToInt(getSharedPreference(ThemeKey,
                String.valueOf(app.core.utils.Themes.Light)));
    }

    public void saveCurrentTheme(int themeid) {
        this.SavePreferences(ThemeKey, String.valueOf(themeid));
    }

    public void resetThemeToDefault() {
        saveCurrentTheme(app.core.utils.Themes.Light);
        context.getApplication().setTheme(app.core.utils.Themes.Light);
    }

    // --------------------- Restarting Tab-------------------------//

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setActivityTheme(int Layout, boolean IsHomeEnabled) {

        try {
            context.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            context.setTheme(getCurrentTheme());
            context.setContentView(Layout);
            if (IsHomeEnabled) {
                context.getActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                ActionBar actionBar = context.getActionBar();
                actionBar.show();
            }
        } catch (Exception Ex) {
            resetThemeToDefault();
        }
    }

    public void setApplicationTheme(int themeid, Activity current) {
        try {
            saveCurrentTheme(themeid);
            context.getApplication().setTheme(themeid);
            restartActivity(current);
        } catch (Exception ex) {
            resetThemeToDefault();
        }
    }

    public void restartActivity(Activity current) {
        Intent I = new Intent(context, current.getClass());
        startActivity(I);
        context.finish();
    }

    private boolean IsFragmentAvilable() {
        if (android.os.Build.VERSION.SDK_INT >= 11)
            return true;
        else
            return false;
    }

    private void attachCallback() {
        // TODO Auto-generated method stub
        FragmentManager fm = context.getFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag("task");

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, "task").commit();
        }
    }

    public void deAttachCallback() {
        mTaskFragment = null;
    }

    @SuppressWarnings("rawtypes")
    public void setTab(int tabIndex) {
        Class cls = IntentFactory.getDataAsClass(Keys.TabItemClass);
        if (cls == null) {
            this.ShowToast("Unable to shift tab! tab class missing!");
            return;
        } else {
            IntentFactory.putDataTab(tabIndex);
            Intent intent = new Intent(context, cls);
            this.startActivity(intent);
            context.finish();
        }
    }

    public void setTabInternal(int tabIndex) {
        FragmentTabActivity ctx = (FragmentTabActivity) context;
        ctx.setTabFinally(tabIndex);
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

    public View getCurrentView() {
        return this.mView;
    }

    public ListView getListView() {
        return this.DragListView;
    }

    public void setOnDragUpdater(IDragUpdater _IDragUpdater) {
        dragmanager.setOnDragUpdater(_IDragUpdater);
        setAutoDragUpdater();
    }

    // ---------------------- Busy indicator logic----------------------------//
    // ----------------------------------------------------------------------//

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
        hideKeyPad();
        progressDialog = new ProgressDialog(this.context);

        progressDialog.setMessage(ProgressMessage);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (ShowProgressTitle)
            progressDialog.setTitle("Please Wait!!");

        if (!IsBusy) {
            IsBusy = true;
            progressDialog.show();
        }

    }

    public void showProgress(String ProgressMessage) {
        showProgress(ProgressMessage, true);

    }

    public void showProgress(boolean ShowProgressTitle) {
        showProgress("loading", ShowProgressTitle);

    }

    public void showProgress() {
        showProgress("loading", true);

    }

/*	public GPSService gps = null;

	public void InvokeGPSService() {
		gps = new GPSService(this.context);
		gps.IsGPSActive();
	}*/

    public void hideProgress() {
        if (IsBusy && progressDialog != null) {
            IsBusy = false;
            progressDialog.cancel();
            progressDialog = null;

        }
    }

    public void InvokeGPSService() {
        gps = new LocationHelper(this.context);
        gps.togglePeriodicLocationUpdates();
    }

    // ---------------- device information------------------------//

    public String getDeviceId() {
        // TODO Auto-generated method stub
        try {
            TelephonyManager mTelephonyMgr;
            mTelephonyMgr = (TelephonyManager) this.context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String yourDeviceID = mTelephonyMgr.getDeviceId();
            return yourDeviceID;
        } catch (Exception ex) {
            return "NA";
        }
    }

    // -------------------image save information------------------//

    public boolean IsFolderSave() {
        if (getSharedPreference(IsFolderSaveKey, "false").equalsIgnoreCase(
                "false"))
            return false;
        else
            return true;
    }

    public void changeImageSavePreference(boolean IsSaveInFolder) {
        this.SavePreferences(IsFolderSaveKey, ToString(IsSaveInFolder));
    }

    // passing data from one activity to another

    @Override
    public void putDataTab(int TabIndex) {
        context.getBase().putDataTab(TabIndex);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void putDataAsClass(String key, Class cls) {
        context.getBase().putDataAsClass(key, cls);

    }

    @Override
    public <T> void putDataArrayList(String Key, ArrayList<T> data) {
        context.getBase().putDataArrayList(Key, data);
    }

    @Override
    public <T> void putData(String Key, T data) {

        context.getBase().putData(Key, data);
    }

    @Override
    public void putDataAsString(String Key, String Value) {

        context.getBase().putDataAsString(Key, Value);
    }

    @Override
    public void putDataAsBoolean(String Key, boolean Value) {

        context.getBase().putDataAsBoolean(Key, Value);
    }

    @Override
    public void putDataAsInt(String Key, int Value) {

        context.getBase().putDataAsInt(Key, Value);
    }

    @Override
    public int getDataTab() {

        return context.getBase().getDataTab();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getDataAsClass(String Key) {

        return context.getBase().getDataAsClass(Key);
    }

    @Override
    public <T> T getData(String Key) {
        return context.getBase().getData(Key);
    }

    @Override
    public String getDataAsString(String Key) {

        return context.getBase().getData(Key);
    }

    @Override
    public boolean getDataAsBoolean(String Key) {

        return context.getBase().getDataAsBoolean(Key);
    }

    @Override
    public int getDataAsInt(String Key) {

        return context.getBase().getDataAsInt(Key);
    }

    @Override
    public <T> ArrayList<T> getDataArrayList(String Key) {

        return context.getBase().getDataArrayList(Key);
    }

    @Override
    public void restartTab(Activity context) {

        this.context.getBase().restartTab(context);
    }

}
