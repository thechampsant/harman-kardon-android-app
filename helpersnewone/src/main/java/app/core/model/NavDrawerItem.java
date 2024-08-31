package app.core.model;

import slidemenu.IFragment;

public class NavDrawerItem {
	
	private String title;
	private boolean IsIcon=false;
	private Integer icon=null;
	private String count = "0";
	@SuppressWarnings("rawtypes")
	private Class FragmentClass;
	// boolean to set visibility of the counter
	private boolean isCounterVisible = false;
	
	public NavDrawerItem(){}

	
	
	@SuppressWarnings("rawtypes")
	public NavDrawerItem set(String title,Class FragmentClass){
		this.title = title;
		this.FragmentClass=FragmentClass;
		return this;
	}
	@SuppressWarnings("rawtypes")
	public NavDrawerItem set(String title,Class FragmentClass,String count){
		this.title = title;
		this.FragmentClass=FragmentClass;
		this.count=count;
		this.isCounterVisible=true;
		return this;
	}
	@SuppressWarnings("rawtypes")
	public NavDrawerItem set(String title,Class FragmentClass,int icon)
	{
		this.title = title;
		this.icon = icon;
		this.FragmentClass=FragmentClass;
		this.IsIcon=true;
		return this;
	}
	@SuppressWarnings("rawtypes")
	public NavDrawerItem set(String title,Class FragmentClass,int icon,String count){
		this.title = title;
		this.icon = icon;
		this.count=count;
		this.isCounterVisible=true;
		this.FragmentClass=FragmentClass;
		this.IsIcon=true;
		return this;
	}
	@SuppressWarnings("rawtypes")
	public NavDrawerItem set(String title,Class FragmentClass,String count,boolean isCounterVisible){
		this.title = title;
		this.IsIcon=false;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
		this.FragmentClass=FragmentClass;
		return this;
	}
	@SuppressWarnings("rawtypes")
	public NavDrawerItem set(String title,Class FragmentClass,int icon,String count,boolean isCounterVisible){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
		this.FragmentClass=FragmentClass;
		this.IsIcon=true;
		return this;
	}

	
	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}
	
	@SuppressWarnings("rawtypes")
	public Class getFragmentClass(){
		return this.FragmentClass;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public boolean getIsIconFound()
	{
		return this.IsIcon;
	}
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}
