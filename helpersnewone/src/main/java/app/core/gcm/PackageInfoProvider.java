package app.core.gcm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;



public class PackageInfoProvider {

	public Context ctx;
	
	public PackageInfoProvider(Context _ctx) {
		// TODO Auto-generated constructor stub
		ctx = _ctx;
	}
	

	@SuppressWarnings("unused")
	private List<PackageInfo> getPackages() {
        final List<PackageInfo> apps = getInstalledApps(false);
        /*final int max = apps.size();
        for (int i=0; i < max; i++) {
            
        }*/
        return apps;
    }

    @SuppressWarnings("unused")
	private List<PackageInfo> getInstalledApps(boolean getSysPackages) {

    	String[] app_labels;
    	PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        try{
            app_labels = new String[packs.size()];
        }catch(Exception e){
            Toast.makeText(ctx.getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        for(int i=0;i < packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            
            //newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
            //newInfo.pname = p.packageName;
            //newInfo.versionName = p.versionName;
            //newInfo.versionCode = p.versionCode;
            //newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
            //res.add(newInfo);

            //app_labels[i] = newInfo.appname;
            //mDisplay.append(" | ");
            //mDisplay.append(p.applicationInfo.loadLabel(getPackageManager()).toString());
            //mDisplay.append(p.packageName);
        }
        return packs;
    }
    
    public void Uninstall(String packagename)
    {
    	Intent intent = new Intent(Intent.ACTION_DELETE);
    	intent.setData(Uri.parse("package:" + packagename));
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	ctx.startActivity(intent);
    }
    
    public void wrapoff()
    {
    	
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void TryPostData()
	{
		new AsyncTask() {
			
	        @Override
	        protected Object doInBackground(Object... params) {
	        	
		try {
			final List<PackageInfo> apps = getInstalledApps(false);
		
		HttpPost httppost = new HttpPost("http://wpremobapp1.innosols.co.in/api1/UploadAppInfo");
		httppost.setHeader("Content-type", "application/json");
		
		Payload payload = new Payload();
		payload.data.add(new AppInfo("App12","com.app12","1"));
		payload.data.add(new AppInfo("App13","com.app13","2"));
		
		JSONObject parent=new JSONObject();
		parent.put("devid", "1");
		
		JSONArray data = new JSONArray();
		
		JSONObject datapoint = new JSONObject();
		int ti = 0;
		for(PackageInfo app : apps)
		{
			datapoint = new JSONObject();
			datapoint.put("Name", app.applicationInfo.loadLabel(ctx.getPackageManager()).toString());
			datapoint.put("PackageName", app.packageName);
			datapoint.put("CurrentVersion", app.versionName);
			data.put(ti, datapoint);
			ti = ti+1;
		}
		
		/*
		datapoint.put("Name", "App12");
		datapoint.put("PackageName", "com.app2");
		datapoint.put("CurrentVersion", "1.2");
		data.put(0, datapoint);
		
		datapoint = new JSONObject();
		datapoint.put("Name", "App14");
		datapoint.put("PackageName", "com.app4");
		datapoint.put("CurrentVersion", "1.2");
		data.put(1, datapoint);
		*/
		
		parent.put("data", data);
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("devid", "1"));
		nameValuePairs.add(new BasicNameValuePair("data", data.toString()));
		Log.i("JSON_COM","DATA:"+data.toString());
		//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		
		HttpParams httpParams = new BasicHttpParams();
		//HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		//HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		//httpParams.setParameter("devid", "3");
		//httpParams.setParameter("data", data.toString());
		HttpClient client = new DefaultHttpClient(httpParams);
		
		// Execute HTTP Post Request
		Log.i("JSON_COM","Request Sent!");
	    
		String respString =parent.toString();
		Log.i("JSON_COM", respString);
	    httppost.setEntity(new ByteArrayEntity(
	    		respString.getBytes("UTF8")));
	    
	    HttpResponse response = client.execute(httppost);
	    Log.i("JSON_COM","Response Code:" + response.getStatusLine().getStatusCode());
	    HttpEntity entity = response.getEntity();
	    
	    if(entity != null) {
	        String responseBody = EntityUtils.toString(entity);
	        Log.i("JSON_COM","Response: "+ responseBody);
	    }else
	    {
	    	Log.i("JSON_COM","Null Entity:");
	    }
	    
	    Log.i("JSON_COM","Response Received!");
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return null;   
        }
	        @Override
	        protected void onPostExecute(Object msg) {
	        	Toast.makeText(ctx, "Execution Done!", Toast.LENGTH_LONG).show();
	        }
	    }.execute(null, null, null);
    }

    class Payload
    {
    	public String devid = "2"; 
    	public List<AppInfo> data = new ArrayList<AppInfo>();
    }
    
    class AppInfo
    {
    	public String Name;
    	public String PackageName;
    	public String Version;
    	
    	public AppInfo(){}
    	
    	public AppInfo(String _name, String _pname, String _version)
    	{
    		Name = _name;
    		PackageName = _pname;
    		Version = _version;
    	}
    }
}
