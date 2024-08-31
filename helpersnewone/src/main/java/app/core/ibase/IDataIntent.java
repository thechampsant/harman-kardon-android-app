package app.core.ibase;
import linq.ArrayList;
import android.app.Activity;

public interface IDataIntent {
	
	public void putDataTab(int TabIndex);

	@SuppressWarnings("rawtypes")
	public void putDataAsClass(String key, Class cls);

	// setting data of array list
	public  <T> void putDataArrayList(String Key, ArrayList<T> data);


	// setting single object data
	public  <T> void putData(String Key, T data);

	// setting single object data as string
	public  void putDataAsString(String Key, String Value);

	// setting single object data as boolean
	public  void putDataAsBoolean(String Key, boolean Value);

	// setting single object data as int
	public void putDataAsInt(String Key, int Value);

	public  int getDataTab();

	@SuppressWarnings("rawtypes")
	public  Class getDataAsClass(String Key);
	

	// getting single object data
	public  <T> T getData(String Key);
	

	// getting single object data as string
	public  String getDataAsString(String Key);

	// getting single object data as string
	public  boolean getDataAsBoolean(String Key);
	
	// getting single object data as string
	public  int getDataAsInt(String Key);


	// getting array list object data
	public  <T> ArrayList<T> getDataArrayList(String Key);
	public  void restartTab(Activity context);
	
	
	
}
