package app.core.server;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;



import linq.ArrayList;


public class Encode {
	public static String Data = "";
	
	public static void Refresh()
	{
		Data="";
	}
	public static void ToParam(String _Parameter, String _Value) {
		try {
			Data += "&" + _Parameter + "=" + URLEncoder.encode(_Value, "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void ToValue(String Value) {
		try {
			Data += URLEncoder.encode(Value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param obj
	 * obj is a model class that can be serialized.
	 * @return the json string with encoded values and ignore the encoding if value is null
	 */
	public static <T>  String ToObject(T obj) {
		Refresh();
		Field[] fields = obj.getClass().getFields();
		for (Field field : fields) {
			try {
				Object fld_value = field.get(obj);
				String Value = "";
				if(fld_value != null){
					Value = fld_value.toString();
					ToParam(field.getName(), Value);
				}
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return Data;
	}
	public static <T> JSONObject ToObjectArray(ArrayList<T> objs)
	{
		JSONObject parent=new JSONObject();
		JSONArray data = new JSONArray();
		JSONObject datapoint = new JSONObject();
	    try
	    {
		int i = 0;
		for(T obj : objs)
		{
			datapoint = new JSONObject();
			
			Field[] fields = obj.getClass().getFields();
			for (Field field : fields) {
				try {
					String Value = field.get(obj).toString();
					if (Value != null)
						datapoint.put(field.getName(),Value);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			data.put(i, datapoint);
			i++;
		}
		
		parent.put("data", data);
		String respString =parent.toString();
        //return respString;
		return parent;
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    	Log.e("Encode Problem",ex.getMessage());
	    	return null;
	    }
        
	}
	
	
	public  <T> String ToObjectValue(T obj) {

		Refresh();
		Field[] fields = obj.getClass().getFields();
		for (Field field : fields) {
			try {
				String Value = field.get(obj).toString();
				if (Value == null)
					ToValue("na");
				else
					ToValue(Value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return Data;
	}

}
