package mob.field.harmonkardonff.entitiymodels;

import com.fieldforce.harmonkardonff.MainActivity;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class StocksModel extends DataEntity<StocksModel>
{
	static String TableName="StockEntry";
	public StocksModel()
	{
		super(TableName);
		// TODO Auto-generated constructor stub
	}
	public String UserName= MainActivity.MyInfo.EmployeeCode;
	public String ForDate;
	public String IsUpdated="false";
	public String IsNoStock="false";
	public int PID;
	public int Qty;
	
	public String ProductName;
	public String Cat1;
	public String Cat2;
	public String Cat3;
	public String Remarks;
	
	
	@Override
	public void RegisterMappings() {
		this.RegisterMapping("UserName",DataTypes.TEXT);
		this.RegisterMapping("ForDate",DataTypes.TEXT);
		this.RegisterMapping("IsUpdated",DataTypes.TEXT);
		this.RegisterMapping("IsNoStock",DataTypes.TEXT);
		this.RegisterMapping("PID",DataTypes.INT);
		this.RegisterMapping("Qty",DataTypes.INT);
		
		this.RegisterMapping("ProductName",DataTypes.TEXT);
		this.RegisterMapping("Cat1",DataTypes.TEXT);
		this.RegisterMapping("Cat2",DataTypes.TEXT);
		this.RegisterMapping("Cat3",DataTypes.TEXT);
		this.RegisterMapping("Remarks",DataTypes.TEXT);
		
	}
	@Override
	public StocksModel GetNewObject() {
		return new StocksModel();
	}
	
	public String toString()
	{
		return this.ProductName;
	}
}
