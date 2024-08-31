package app.core.ibase;

import android.util.Log;

public class ExceptionModel {

	public String ApplicationName = "N.A";
	public String PacakgeName = "N.A";
	public String UserName = "N.A";
	public String Error = "N.A";
	public String ErrorTime;
	public int AndroidVersion = 0;
	public String MobileModel = "N.A";
	public String ApplicationVersion = "0.0";
	public String MethodName = "N.A";
	public String ClassName = "N.A";
	public String FileName = "N.A";
	public int LineNumber = 0;

	public ExceptionModel getExceptionModel(String UserName, Throwable ex) {
		try {
			StackTraceElement ele = ex.getStackTrace()[0];
			ExceptionModel model = new ExceptionModel();
			// model.PacakgeName =
			model.Error = ex.toString();
			model.UserName = UserName;
			model.MobileModel = android.os.Build.MODEL;
			model.AndroidVersion = android.os.Build.VERSION.SDK_INT;
			// model.ApplicationVersion=getApplicationVersion();
			model.MethodName = ele.getMethodName();
			model.LineNumber = ele.getLineNumber();
			model.ClassName = ele.getClassName();
			model.FileName = ele.getFileName();
			return model;
		} catch (Exception e) {
			Log.i("Create exception model failure: ", e.getMessage());
			return new ExceptionModel();
		}
	}

	public ExceptionModel getExceptionModel(Throwable ex) {
		try {
			StackTraceElement ele = ex.getStackTrace()[0];
			ExceptionModel model = new ExceptionModel();
			// model.PacakgeName =
			model.Error = ex.toString();
			// model.UserName = UserName;
			model.MobileModel = android.os.Build.MODEL;
			model.AndroidVersion = android.os.Build.VERSION.SDK_INT;
			// model.ApplicationVersion=getApplicationVersion();
			model.MethodName = ele.getMethodName();
			model.LineNumber = ele.getLineNumber();
			model.ClassName = ele.getClassName();
			model.FileName = ele.getFileName();
			return model;
		} catch (Exception e) {
			Log.i("Create exception model failure: ", e.getMessage());
			return new ExceptionModel();
		}
	}

}
