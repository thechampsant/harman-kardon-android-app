package app.core.events;

public abstract class IEvent    {
	public  abstract void Completed(Object Sender, Object Args);
//	public IEvent setProgress(boolean showProgress)
//	{
//		this.showProgress=showProgress;
//		return this;
//	}
//	public IEvent setProgress(boolean showProgress,boolean onErrorDialog)
//	{
//		this.showProgress=showProgress;
//		this.onErrorDialog=onErrorDialog;
//		return this;
//	}
//	public boolean isShowProgress()
//	{
//		return this.showProgress;
//	}
//	public boolean isShowError()
//	{
//		return this.onErrorDialog;
//	}
//
//	private boolean showProgress=false;
//	private boolean onErrorDialog=false;
	
}
