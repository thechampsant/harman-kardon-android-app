package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

import com.fieldforce.harmonkardonff.MainActivity;


public class MTDSalesModel extends DataEntity<MTDSalesModel> {
	
	static String TableName="MTDSalesModel";
	public MTDSalesModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}
    public String Category;
	public String Qty;
	public String Tgt;
	public String Ach;
	public String UserName=MainActivity.MyInfo.EmployeeCode;
	
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("Category",DataTypes.TEXT);
		this.RegisterMapping("Qty",DataTypes.TEXT);
		this.RegisterMapping("Tgt",DataTypes.TEXT);
		this.RegisterMapping("Ach",DataTypes.TEXT);
		this.RegisterMapping("UserName",DataTypes.TEXT);
	}

	@Override
	public MTDSalesModel GetNewObject() {
		// TODO Auto-generated method stub
		return new MTDSalesModel();
	}

}
