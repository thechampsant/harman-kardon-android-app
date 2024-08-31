package app.core.entitymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;



public class ImageInfo extends DataEntity<ImageInfo> {

	public static String TableName="ImageInfo";
	public ImageInfo() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}
	
	public String ModelID;
	public String HeaderID;
	public String UserName="NA";
	public String DocID;	
	public String DocType;
	public String DocTitle;
	
	public String IsOffline="true";
	
	public String LocalUrl;
	public String ServerUrl;
	public String CapturedOn;
	public String UploadedOn;
	public String Remarks="NA";
	public String Latitude="0.0";
	public String Longitude="0.0";
	
	
	public boolean isUpdated()
	{
		return this.IsOffline.equalsIgnoreCase("false");
	}
	
	@Override
	public void RegisterMappings() {
		this.RegisterMapping("ModelID",DataTypes.TEXT);
		this.RegisterMapping("HeaderID",DataTypes.TEXT);
		this.RegisterMapping("UserName",DataTypes.TEXT);
		this.RegisterMapping("DocID",DataTypes.TEXT);
		this.RegisterMapping("DocType",DataTypes.TEXT);
		this.RegisterMapping("DocTitle",DataTypes.TEXT);
		
		this.RegisterMapping("IsOffline",DataTypes.TEXT);
		
		this.RegisterMapping("LocalUrl",DataTypes.TEXT);
		this.RegisterMapping("ServerUrl",DataTypes.TEXT);
		this.RegisterMapping("CapturedOn",DataTypes.TEXT);
		this.RegisterMapping("UploadedOn",DataTypes.TEXT);
		this.RegisterMapping("Remarks",DataTypes.TEXT);
		this.RegisterMapping("Latitude",DataTypes.TEXT);
		this.RegisterMapping("Longitude",DataTypes.TEXT);
	}
	@Override
	public ImageInfo GetNewObject() {
		// TODO Auto-generated method stub
		return new ImageInfo();
	}
}
