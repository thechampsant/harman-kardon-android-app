package tabmenu;

public class TabItem {

	private String Title;
	private int IconResourceID;
	private boolean IsIconExists=false;
	@SuppressWarnings("rawtypes")
	private Class cls;
	private boolean isInvoke=false;
	
	
	public TabItem Invoke()
	{
		this.isInvoke=true;
		return this;
	}
	public boolean tabInvoke()
	{
		return this.isInvoke;
	}
	
	@SuppressWarnings("rawtypes")
	public TabItem setItem(String Title,Class activity)
	{
		this.Title=Title;
		this.cls=activity;
		this.IsIconExists=false;
		return this;
	}
	@SuppressWarnings("rawtypes")
	public TabItem setItem(String Title,Class activity,int IconResourceID)
	{
		this.Title=Title;
		this.cls=activity;
		this.IsIconExists=true;
		this.IconResourceID=IconResourceID;
		return this;
	}
	@SuppressWarnings("rawtypes")
	public TabItem setItem(String Title,Class activity,int IconResourceID,boolean showIcon)
	{
		this.Title=Title;
		this.cls=activity;
		this.IsIconExists=showIcon;
		this.IconResourceID=IconResourceID;
		return this;
	}
	
	public boolean IsShowIcon()
	{
		return this.IsIconExists;
	}
	public String getTitle()
	{
		return this.Title;
	}
	public int getIcon()
	{
		return this.IconResourceID;
	}
	public boolean IsActivityExists()
	{
		if(getActivityClass()==null)
			return false;
		else
			return true;
	}
	@SuppressWarnings("rawtypes")
	public Class getActivityClass()
	{
		return this.cls;
	}
	
}
