/**
 * 
 */
package mob.field.harmonkardonff.entitiymodels;
import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

import com.fieldforce.harmonkardonff.MainActivity;

/**
 * @author newINNOSOLS
 *
 */
public class MOPModel extends DataEntity<MOPModel>{
	
	/**
	 * @param _Name
	 */
	static String TableName="MOPModel";
	public MOPModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	public int _ID;
	public int HeaderID;

	public int OutID;
	public String OutName;

	public int PID;
	public String ProductName;
	public String ProductCode;

	public String ForDate;
	
	public String Price;
	public Integer Value;
	public String Value2;

	public String Remarks;

	public String Cat1;
	public String Cat2;
	public String Cat3;
	public String Cat4;

	public String IsOfflineOnly = "true";

	public String CreatedOn;
	public String DocIDs;
	public String UserName=MainActivity.MyInfo.EmployeeCode;
	
	
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		
		this.RegisterMapping("_ID", DataTypes.INTEGER_PRIMARY_KEY);
		this.RegisterMapping("HeaderID", DataTypes.INT_NULL);
		
		this.RegisterMapping("OutID", DataTypes.INT);
		this.RegisterMapping("OutName", DataTypes.TEXT);
		
		this.RegisterMapping("PID", DataTypes.INT);
		this.RegisterMapping("ProductName", DataTypes.TEXT);
		this.RegisterMapping("ProductCode", DataTypes.TEXT);
		
		this.RegisterMapping("ForDate", DataTypes.TEXT);
		this.RegisterMapping("Price", DataTypes.TEXT);
		
		this.RegisterMapping("Value", DataTypes.INT_NULL);
		this.RegisterMapping("Value2", DataTypes.TEXT);
		
		this.RegisterMapping("Remarks", DataTypes.TEXT);
		
		this.RegisterMapping("Cat1", DataTypes.TEXT);
		this.RegisterMapping("Cat2", DataTypes.TEXT);
		this.RegisterMapping("Cat3", DataTypes.TEXT);
		this.RegisterMapping("Cat4", DataTypes.TEXT);
		
		this.RegisterMapping("IsOfflineOnly", DataTypes.TEXT);
		
		this.RegisterMapping("DocIDs", DataTypes.TEXT);
		this.RegisterMapping("CreatedOn", DataTypes.TEXT);
		this.RegisterMapping("UserName", DataTypes.TEXT);
	}
	
	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public MOPModel GetNewObject() {
		// TODO Auto-generated method stub
		return new MOPModel();
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	

}
