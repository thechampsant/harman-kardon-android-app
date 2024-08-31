package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class BrandModel extends DataEntity<BrandModel> {

	static String TableName="BrandModel";
	public String Name;
	public String ID;
	public BrandModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("Name",DataTypes.TEXT);
		this.RegisterMapping("ID",DataTypes.TEXT);
	}
	@Override
	public BrandModel GetNewObject() {
		// TODO Auto-generated method stub
		return new BrandModel();
	}


}
