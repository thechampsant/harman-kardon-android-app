package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

import com.fieldforce.harmonkardonff.MainActivity;

public class CheckoutModel extends DataEntity<CheckoutModel> {
	static String TableName = "CheckoutModel";

	public CheckoutModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	public String UserName = MainActivity.MyInfo.EmployeeCode;;
	public String Remarks;
	public String CheckOutTime;
	public String ForDate;
	public String DocIDs;
	public String Latitude;
	public String Longitude;
	public String istodayattndavl;
	

	@Override
	public void RegisterMappings() {
		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("Remarks", DataTypes.TEXT);
		this.RegisterMapping("ForDate", DataTypes.TEXT);
		this.RegisterMapping("CheckOutTime", DataTypes.TEXT);
		this.RegisterMapping("DocIDs", DataTypes.TEXT);
		this.RegisterMapping("Latitude", DataTypes.TEXT);
		this.RegisterMapping("Longitude", DataTypes.TEXT);
		this.RegisterMapping("istodayattndavl", DataTypes.TEXT);
	}

	@Override
	public CheckoutModel GetNewObject() {
		// TODO Auto-generated method stub
		return new CheckoutModel();
	}

}
