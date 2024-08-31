package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class ViewDisplayModel extends DataEntity<ViewDisplayModel> {

	public boolean isIsupdated() {
		return isupdated;
	}

	public void setIsupdated(boolean isupdated) {
		this.isupdated = isupdated;
	}

	public static String getTableName() {
		return TableName;
	}

	public static void setTableName(String tableName) {
		TableName = tableName;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public int getDisplayQTY() {
		return DisplayQTY;
	}

	public void setDisplayQTY(int displayQTY) {
		DisplayQTY = displayQTY;
	}

	public int getBrandID() {
		return BrandID;
	}

	public void setBrandID(int brandID) {
		BrandID = brandID;
	}

	static String TableName="ViewDisplayModel";
	public int ID;
	public int PID;
	public int DisplayQTY;
	public int BrandID;
	public boolean isupdated=false;
	public ViewDisplayModel() {
		super(TableName);
	}

	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("ID", DataTypes.INT);
		this.RegisterMapping("PID", DataTypes.INT);
		this.RegisterMapping("DisplayQTY", DataTypes.INT);
		this.RegisterMapping("BrandID", DataTypes.INT);
	}

	@Override
	public ViewDisplayModel GetNewObject() {
		// TODO Auto-generated method stub
		return new ViewDisplayModel();
	}

}
