package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class BrandCategoryModel extends DataEntity<BrandCategoryModel> {

	static String TableName="BrandCatModel";
	public String Name;
	public String ID;
	public String BrandID;
	
	public BrandCategoryModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("Name",DataTypes.TEXT);
		this.RegisterMapping("ID",DataTypes.TEXT);
		this.RegisterMapping("BrandID",DataTypes.TEXT);
	}
	@Override
	public BrandCategoryModel GetNewObject() {
		// TODO Auto-generated method stub
		return new BrandCategoryModel();
	}


}
