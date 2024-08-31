package app.core.services;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import app.core.entitymodels.ErrorModel;
import app.core.model.Response;
import app.core.server.WebClient;

public class ErrorSender {

	public String Message = "";
	public boolean IsSuccess = false;
	private Activity context;

	public ErrorSender(Activity context) {
		this.context = context;
	}
	
	private  String getApplicationVersion() {
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendErrorOnServer(final ErrorModel error) {
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				try {
					Response response=new WebClient().TrySendError(error);
					Log.i("Send error response status: "+response.status,response.errormsg);
					context.finish();
				} catch (Exception ex) {
					Message = ex.getMessage();
					context.finish();
				}
				return null;
			}
		}.execute("");
	}

	public void sendErrorOnServer(String UserName, Throwable ex) {

		sendErrorOnServer(getErrorModel(UserName, ex));
	}
	public void sendErrorOnServer(Throwable ex) {
		sendErrorOnServer(getErrorModel("NA", ex));
	}

	private ErrorModel getErrorModel(String UserName, Throwable ex) {
		try {
			StackTraceElement ele=ex.getStackTrace()[0];
			ErrorModel model = new ErrorModel();
			model.PacakgeName = context.getPackageName();
			model.Error = ex.toString();
			model.UserName = UserName;
			model.MobileModel = android.os.Build.MODEL;
			model.AndroidVersion = android.os.Build.VERSION.SDK_INT;
			model.ApplicationVersion=getApplicationVersion();
			model.MethodName=ele.getMethodName();
			model.LineNumber=ele.getLineNumber();
			model.ClassName=ele.getClassName();
			model.FileName=ele.getFileName();
			return model;
		   } catch (Exception e) {
			Log.i("Send error model failure: ",e.getMessage());
			return new ErrorModel();
		}
	}

}
