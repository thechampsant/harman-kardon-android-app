package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

import com.fieldforce.harmonkardonff.MainActivity;

public class CustomInfoModel extends DataEntity<CustomInfoModel> {
	static String TableName="CustomInfoModel";
	public CustomInfoModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	//For Local Use
	public String ProductName;
	public String Cat1;
	public String Cat2;
	public String Cat3;
	
	
	public String CustomerName;
	public String MobileNo;
	public String Email;
	public String City;
	public String Location;
	
	public String PID;
	public String ForDate;
	public int Qty;
	public String Remarks;
	public String IsUpdated="false";
	public String SerialNo;
	public String CreatedOn;
	public String UserName=MainActivity.MyInfo.EmployeeCode;
	
	@Override
	public CustomInfoModel GetNewObject() {
		return new CustomInfoModel();
	}
	@Override
	public void RegisterMappings() {

		this.RegisterMapping("CustomerName", DataTypes.TEXT);
		this.RegisterMapping("MobileNo", DataTypes.TEXT);
		this.RegisterMapping("Email", DataTypes.TEXT);
		this.RegisterMapping("City", DataTypes.TEXT);
		this.RegisterMapping("Location", DataTypes.TEXT);
		this.RegisterMapping("ProductName", DataTypes.TEXT);
		
		this.RegisterMapping("PID", DataTypes.TEXT);
		this.RegisterMapping("ForDate", DataTypes.TEXT);
		this.RegisterMapping("Qty", DataTypes.INT);		
		this.RegisterMapping("Remarks", DataTypes.TEXT);
		this.RegisterMapping("IsUpdated", DataTypes.TEXT);
		this.RegisterMapping("SerialNo", DataTypes.TEXT);
		this.RegisterMapping("CreatedOn", DataTypes.TEXT);
		this.RegisterMapping("UserName", DataTypes.TEXT);
		
		this.RegisterMapping("Cat1", DataTypes.TEXT);
		this.RegisterMapping("Cat2", DataTypes.TEXT);
		this.RegisterMapping("Cat3", DataTypes.TEXT);
	}
	
	
}
