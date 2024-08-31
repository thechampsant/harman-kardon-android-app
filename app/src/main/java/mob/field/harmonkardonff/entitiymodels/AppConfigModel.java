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
public class AppConfigModel extends DataEntity<AppConfigModel> {

	/**
	 * @param _Name
	 */
	public AppConfigModel() {
		super("AppConfigModel");
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public AppConfigModel GetNewObject() {
		// TODO Auto-generated method stub
		return new AppConfigModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	public String UserName = MainActivity.MyInfo.EmployeeCode;
	public String AppConfigVersion;

	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("AppConfigVersion", DataTypes.TEXT);
	}

}
