package app.core.model;


public  class NotificationModel<T>  {
	
	
	public int NotifyNumber=0;
	public int NotifyID=0;
	public String Message="Message";
	public String MessageHeading="No message heading found!";
	public String NotificationMessage="Welcome to notification world";
	public Class<T> PendingIntent;
	public int ImageResourceID=0;
	public String IntentAction="";
	

	

}
