package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;


public class ProductModel extends DataEntity<ProductModel>{
	/**
	 * @param _Name
	 */
	static String TableName="ProductModel";
	public ProductModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	public int _ID;
	public String PID;
	public String ID;
	public String ProductName;
	public String Name;
	public String Code;
	public String ParentID = null;	
	public String Cat1;
	public String Cat2;
	public String Cat3;
	public String Cat4;
	public String MOP;

	public String IsBlocked="false";
	
	public String toString()
	{
		return Name;
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public ProductModel GetNewObject() {
		// TODO Auto-generated method stub
		return new ProductModel();
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("_ID", DataTypes.INTEGER_PRIMARY_KEY);
		this.RegisterMapping("PID", DataTypes.TEXT);
		this.RegisterMapping("ID", DataTypes.TEXT);
		this.RegisterMapping("ProductName", DataTypes.TEXT);
		this.RegisterMapping("Name", DataTypes.TEXT);
		this.RegisterMapping("Code", DataTypes.TEXT);
		this.RegisterMapping("ParentID", DataTypes.TEXT);
		this.RegisterMapping("Cat1", DataTypes.TEXT);
		this.RegisterMapping("Cat2", DataTypes.TEXT);
		this.RegisterMapping("Cat3", DataTypes.TEXT);
		this.RegisterMapping("Cat4", DataTypes.TEXT);
		this.RegisterMapping("IsBlocked", DataTypes.TEXT);
		this.RegisterMapping("MOP", DataTypes.TEXT);

	}
}
