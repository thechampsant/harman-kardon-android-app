package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class CompProductModel extends DataEntity<CompProductModel>{
	/**
	 * @param _Name
	 */
	static String TableName="CompProductModel";
	public CompProductModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	public int _ID;
	public int PID;
	public String Name;
	public String Code;
	public Integer ParentID = null;	
	public String Cat1;
	public String Cat2;
	public String Cat3;
	public String Cat4;
	public String IsBlocked="false";
	
	public String toString()
	{
		return Name;
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public CompProductModel GetNewObject() {
		// TODO Auto-generated method stub
		return new CompProductModel();
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("_ID", DataTypes.INTEGER_PRIMARY_KEY);
		this.RegisterMapping("PID", DataTypes.INT);
		this.RegisterMapping("Name", DataTypes.TEXT);
		this.RegisterMapping("Code", DataTypes.TEXT);
		this.RegisterMapping("ParentID", DataTypes.INT_NULL);
		this.RegisterMapping("Cat1", DataTypes.TEXT);
		this.RegisterMapping("Cat2", DataTypes.TEXT);
		this.RegisterMapping("Cat3", DataTypes.TEXT);
		this.RegisterMapping("Cat4", DataTypes.TEXT);
		this.RegisterMapping("IsBlocked", DataTypes.TEXT);
	}
}
