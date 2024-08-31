package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

import com.fieldforce.harmonkardonff.MainActivity;

public class Viewsalemodel extends DataEntity<Viewsalemodel> {

	static String TableName = "Viewsalemodel";

	public Viewsalemodel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	public String CustomerName;
	public String MobileN0;
	public String Date;
	public String SKU;
	public String Qty;
	public String Email;
	public String UserName = MainActivity.MyInfo.EmployeeCode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public Viewsalemodel GetNewObject() {
		// TODO Auto-generated method stub
		return new Viewsalemodel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub

		this.RegisterMapping("CustomerName", DataTypes.TEXT);
		this.RegisterMapping("MobileN0", DataTypes.TEXT);
		this.RegisterMapping("Date", DataTypes.TEXT);
		this.RegisterMapping("SKU", DataTypes.TEXT);
		this.RegisterMapping("Qty", DataTypes.TEXT);
		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("Email", DataTypes.TEXT);
	}
}
