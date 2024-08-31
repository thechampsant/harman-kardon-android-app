package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class GPSWifiPropertyModel extends DataEntity<GPSWifiPropertyModel> {
	static String TableName = "GPSWifiPropertyModel";

	public GPSWifiPropertyModel() {
		super(TableName);
	}

	public String UserName ;
	public String wifiStrngth;
	public String networkStrngth;
	public String gpsOn;
	public String batterystatus;
	public String Latitude = "0.0";
	public String Longitude = "0.0";
	public String CheckIn_Latitude="0.0";
	public String CheckIn_Longitude = "0.0";
	public String DeviceTime;
	public String isLocal = "true";
	public String version="";
	public String imei="";
	
	@Override
	public void RegisterMappings() {
		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("wifiStrngth", DataTypes.TEXT);
		this.RegisterMapping("networkStrngth", DataTypes.TEXT);
		this.RegisterMapping("gpsOn", DataTypes.TEXT);
		this.RegisterMapping("batterystatus", DataTypes.TEXT);
		this.RegisterMapping("Latitude", DataTypes.TEXT);
		this.RegisterMapping("Longitude", DataTypes.TEXT);
		this.RegisterMapping("CheckIn_Latitude", DataTypes.TEXT);
		this.RegisterMapping("CheckIn_Longitude", DataTypes.TEXT);
		this.RegisterMapping("DeviceTime", DataTypes.TEXT);
		this.RegisterMapping("isLocal", DataTypes.TEXT);
		this.RegisterMapping("version", DataTypes.TEXT);
		this.RegisterMapping("imei", DataTypes.TEXT);
	}

	@Override
	public GPSWifiPropertyModel GetNewObject() {
		// TODO Auto-generated method stub
		return new GPSWifiPropertyModel();
	}

}
