package app.core.ibase;

public class IService<T> {

	public String Message = null;
	public String Error = null;
	public String ServiceCode = "NA";
	public boolean IsSuccess = false;
	public ExceptionModel Exception = null;
	private IEvent<T> ActiveEvent = null;
	private T args = null;

	public void finish(T args) {
		if (this.ActiveEvent != null)
			this.ActiveEvent.Completed(this, args);
	}

	public void finish(Exception ex) {
		setException(ex);
		if (this.ActiveEvent != null)
			this.ActiveEvent.Completed(this, args);
	}

	public void setResult(T args) {
		this.args = args;
	}
	
	public void setEventResult(Object args) {
		this.ActiveEvent.result=args;
	}

	public void finish() {
		if (this.ActiveEvent != null)
			this.ActiveEvent.Completed(this, this.args);
	}

	public IService(IEvent<T> event) {
		this.ActiveEvent = event;
	}

	public void setSuccess() {
		this.IsSuccess = true;
	}

	public void setSuccess(boolean IsSuccess, String Error) {
		this.IsSuccess = IsSuccess;
		this.Error = Error;
		this.Message = Error;
	}

	public void setSuccess(boolean IsSuccess, String Error, String Code) {
		this.IsSuccess = IsSuccess;
		this.Error = Error;
		this.Message = Error;
		this.ServiceCode = Code;
	}

	public IService<T> setCode(String Code) {
		this.ServiceCode = Code;
		return this;
	}

	public void setException(Exception ex) {
		setExceptionModel(new ExceptionModel().getExceptionModel(ex));
	}

	private void setExceptionModel(ExceptionModel ex) {
		this.Exception = ex;
		this.IsSuccess = false;
		this.Error = ex.Error;
		this.Message = ex.Error;
	}

}
