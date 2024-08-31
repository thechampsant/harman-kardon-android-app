package app.core.viewmodels;


public class ViewModel {
	
	public Boolean IsBusy = false;
	public String Message = "";
	public Boolean IsSuccess = false;
	public String ErrorCode = "";
	
	public ViewModel()
	{
		
	}
	
	public void setMessage(String message)
	{
		this.Message=message;
		this.IsSuccess=false;
	}
	public void setMessage(String message,String ErrorCode)
	{
		this.ErrorCode=ErrorCode;
		this.Message=message;
		this.IsSuccess=false;
	}
	public void setMessage(String message,boolean IsSuccess)
	{
		this.Message=message;
		this.IsSuccess=IsSuccess;
	}
	public Boolean IsNullOrWS(String str)
	{
		if(str == null)
			return true;
		if(str.trim().length()<1)
			return true;
		return false;
	}
}
