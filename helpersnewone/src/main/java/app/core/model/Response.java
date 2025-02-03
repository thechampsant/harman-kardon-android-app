package app.core.model;

import android.text.TextUtils;

import java.io.File;

import linq.ArrayList;

public class Response<T, E> {

	public String type;
	public String AppVersion;
	public String status = "false";
	public String errormsg = "";
	public String FileUrl;
	public String  Message="";//done by himanshu//


	public PageModel PageInfo;
	public ArrayList<T> data;
	//public ArrayList<E> EData;

	public File file;
	public String FileName;

	public Response(String error) {
		this.errormsg = error;
	}

	public Response() {
		PageInfo=new PageModel();
	}

	public boolean isSuccess() {
		if (TextUtils.isEmpty(status))
			return false;
		else if (this.status.toLowerCase().trim().equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}
	public boolean getRequestStatus()
	{
		return this.isSuccess();
	}

	public boolean isDataFound() {
		if (this.data == null || this.data.Count() < 1)
			return false;
		else
			return true;
	}

	/*public boolean isEDataFound() {
		if (this.EData == null || this.EData.Count() < 1)
			return false;
		else
			return true;
	}*/

	public boolean isDataExists() {
		return this.isDataFound() /*&& this.isEDataFound()*/;
	}

	public String getMessage() {
		if (!this.isDataFound() && TextUtils.isEmpty(this.errormsg))
			return "No Data found !";
		/*else if (!this.isEDataFound() && TextUtils.isEmpty(this.errormsg))
			return "No Data found !";*/
		else
			return this.errormsg;
	}
	public String message(){
		return this.Message ;
	}

}
