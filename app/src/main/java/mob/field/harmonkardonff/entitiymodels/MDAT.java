package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

import com.fieldforce.harmonkardonff.MainActivity;


public class MDAT extends DataEntity<MDAT>{
	/**
	 * @param _Name
	 */
	static String TableName="MDAT";
	public MDAT() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}
	public int ID;
	public String option;
	public String ForDate;
	public String CheckoutForCurrentDate;
	public String IMEI;
	public String Remarks;
	public String LStartDate;
	public String LEndDate;
	public String CreatedOn;
	public String CheckInTime;
	public String CheckOutTime;
	public String Regularize="false";
	public String DocIDs="";
	public String UserName=MainActivity.MyInfo.UserID;
	public String IsOfflineOnly = "true";
	public String Longitude = "";
	public String Latitude="";

	public String StoreID;
	public String isForcefullyUpdated = "false";
	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public MDAT GetNewObject() {
		// TODO Auto-generated method stub
		return new MDAT();
	}
	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("ID", DataTypes.INTEGER_PRIMARY_KEY);
		this.RegisterMapping("option", DataTypes.TEXT);
		this.RegisterMapping("ForDate", DataTypes.TEXT);
		this.RegisterMapping("Remarks", DataTypes.TEXT);
		this.RegisterMapping("LStartDate", DataTypes.TEXT);
		this.RegisterMapping("IMEI", DataTypes.TEXT);
		this.RegisterMapping("LEndDate", DataTypes.TEXT);
		this.RegisterMapping("CreatedOn", DataTypes.TEXT);
		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("CheckInTime", DataTypes.TEXT);
		this.RegisterMapping("CheckOutTime", DataTypes.TEXT);
		this.RegisterMapping("DocIDs", DataTypes.TEXT);
		this.RegisterMapping("IsOfflineOnly", DataTypes.TEXT);
		this.RegisterMapping("StoreID",DataTypes.TEXT);
		this.RegisterMapping("Regularize",DataTypes.TEXT);
		this.RegisterMapping("Longitude",DataTypes.TEXT);
		this.RegisterMapping("Latitude",DataTypes.TEXT);
		this.RegisterMapping("isForcefullyUpdated",DataTypes.TEXT);
		this.RegisterMapping("CheckoutForCurrentDate",DataTypes.TEXT);
	}

}
