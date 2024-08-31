package app.core.entitymodels;

import android.graphics.drawable.Drawable;
import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class AppDetail extends DataEntity<AppDetail> {
	
	public AppDetail() {
		super("AppDetail");
		// TODO Auto-generated constructor stub
	}

	public CharSequence label;
	public CharSequence name;
	public Drawable icon;
	public String CanSeeApp="false";
	
	public boolean getCanSeeApp() {
		return CanSeeApp.trim().toLowerCase().equalsIgnoreCase("true");
	}
	
	
	
	@Override
	public void RegisterMappings() {
		this.RegisterMapping("label", DataTypes.TEXT);
		this.RegisterMapping("name", DataTypes.TEXT);
		this.RegisterMapping("CanSeeApp", DataTypes.TEXT);
	}
	@Override
	public AppDetail GetNewObject() {
		return new AppDetail();
	}
}
