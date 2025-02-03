package app.core.server;

import java.lang.reflect.Field;
import java.util.Set;

import linq.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import app.core.model.Response;
import app.core.sqllite.DataEntity;

import com.google.gson.Gson;

public class Decode<T extends DataEntity<T>> {

	public T ConvertFrom(T obj, JSONObject JObj) {
		Field[] fields = obj.getClass().getFields();
		for (Field fld : fields) {
			String name = fld.getName();
			try {
				obj.SetValue(name, JObj.get(name).toString());
			} catch (JSONException ex) {
				// Do Nothing
			}
		}
		return obj;
	}

	public ArrayList<T> ConvertFromArray(T obj, JSONArray Jarray)
			throws JSONException {
		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < Jarray.length(); i++) {
			list.add(this.ConvertFrom(obj.GetNewObject(),
					Jarray.getJSONObject(i)));
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes" })
	public Response ToResponse(JSONObject Jobj) throws Exception {
		Response R = new Response();
		Gson g = new Gson();
		R = g.fromJson(Jobj.toString(), Response.class);	
		return R;
	}
	
	@SuppressWarnings("unchecked")
	public <E> E ToClass(E obj,JSONObject Jobj) throws Exception {
		Gson g = new Gson();
		obj = (E) g.fromJson(Jobj.toString(), obj.getClass());	
		return obj;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response ToResponse(T type, JSONObject Jobj) throws Exception {
		Response R = new Response();
		Gson g = new Gson();
		// TypeToken<ArrayList<T>> token = new TypeToken<ArrayList<T>>(){};
		R = g.fromJson(Jobj.toString(), Response.class);
		if(type==null) return R;
		JSONArray array = new JSONArray();
		try {
			array = Jobj.getJSONArray("data");
			/*array.getJSONArray()*/
			//Jobj.getJSONArray("data").getJSONObject(0).getJSONArray("value")
		} catch (Exception ex) {
		}
		R.data = new ArrayList<T>();
		for (int i = 0; i < array.length(); i++) {
			T item = type.GetNewObject();
			item = ConvertFrom(item, array.getJSONObject(i));
			R.data.add(item);
		}
		return R;
	}



	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response ToResponse(T type, JSONObject Jobj, String nestedArrayKeyName) throws Exception {
		Response R = new Response();
		Gson g = new Gson();
		// TypeToken<ArrayList<T>> token = new TypeToken<ArrayList<T>>(){};
		R = g.fromJson(Jobj.toString(), Response.class);
		if(type==null) return R;
		JSONArray array = new JSONArray();
		JSONArray arrayFromArray = new JSONArray();
		try {
			array = Jobj.getJSONArray("data");
			arrayFromArray = Jobj.getJSONArray("data").getJSONObject(0).getJSONArray("value");
		} catch (Exception ex) {
		}
		R.data = new ArrayList<T>();
		for (int i = 0; i < arrayFromArray.length(); i++) {
			T item = type.GetNewObject();
			item = ConvertFrom(item, array.getJSONObject(i));
			R.data.add(item);
		}
		return R;
	}*/
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response ToResponse(T data,T edata, JSONObject Jobj) throws Exception {
		Response R = new Response();
		Gson g = new Gson();
		// TypeToken<ArrayList<T>> token = new TypeToken<ArrayList<T>>(){};
		
		R = g.fromJson(Jobj.toString(), Response.class);
		JSONArray array = new JSONArray();
		try {
			array = Jobj.getJSONArray("data");
		} catch (Exception ex) {
		}
		R.data = new ArrayList<T>();
		for (int i = 0; i < array.length(); i++) {
			T item = data.GetNewObject();
			
			item = ConvertFrom(item, array.getJSONObject(i));
			R.data.add(item);
		}
		
		try {
			array = new JSONArray();
			array = Jobj.getJSONArray("EData");
		} catch (Exception ex) {
		}
		/*R.EData = new ArrayList<T>();
		for (int i = 0; i < array.length(); i++) {
			T item = edata.GetNewObject();
			
			item = ConvertFrom(item, array.getJSONObject(i));
			R.EData.add(item);
		}*/
		return R;
	}

	public T ToResponse(T type,Bundle extra) throws Exception {
		
		JSONObject Jobj = new JSONObject();
		Set<String> keys = extra.keySet();
		for (String key : keys) {
		    try {
		        // json.put(key, bundle.get(key)); see edit below
		    	Jobj.put(key,extra.get(key));
		    } catch(JSONException e) {
		        //Handle exception here
		    	continue;
		    }
		}
		return this.ConvertFrom(type,Jobj);
	}
	

}
