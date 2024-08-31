package mob.field.harmonkardonff.entitiymodels;

import com.fieldforce.harmonkardonff.MainActivity;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class StoreImage extends DataEntity<StoreImage>{

	public StoreImage() {
		super("StoreImage");
	}

	public String UserID=MainActivity.MyInfo.UserID;
	public String StoreID=MainActivity.MyInfo.StoreID;
	public String ForDate;
	
	@Override
	public void RegisterMappings() {
		this.RegisterMapping("StoreID",DataTypes.TEXT);
		this.RegisterMapping("ForDate",DataTypes.TEXT);
		
	}

	@Override
	public StoreImage GetNewObject() {
		return new StoreImage();
	}

}
