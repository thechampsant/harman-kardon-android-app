package app.core.model;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class DocumentInfo extends DataEntity<DocumentInfo> {

	public static String TableName="DocumentInfo";
	public DocumentInfo() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}
	public String LocalUrl;
	public String ServerUrl;
	public String IsOffline="true";
	public String CapturedOn;
	public String CapturedBy;
	public String UploadedOn;
	public String DocType;
	public String Title;
	public String Remarks="NA";
	public String DocID;
	public String ID;
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("LocalUrl",DataTypes.TEXT);
		this.RegisterMapping("ServerUrl",DataTypes.TEXT);
		this.RegisterMapping("IsOffline",DataTypes.TEXT);
		this.RegisterMapping("CapturedOn",DataTypes.TEXT);
		this.RegisterMapping("CapturedBy",DataTypes.TEXT);
		this.RegisterMapping("UploadedOn",DataTypes.TEXT);
		this.RegisterMapping("DocType",DataTypes.TEXT);
		this.RegisterMapping("Title",DataTypes.TEXT);
		this.RegisterMapping("Remarks",DataTypes.TEXT);
		this.RegisterMapping("DocID",DataTypes.TEXT);
		this.RegisterMapping("ID",DataTypes.TEXT);
	
	}
	@Override
	public DocumentInfo GetNewObject() {
		// TODO Auto-generated method stub
		return new DocumentInfo();
	}
}
