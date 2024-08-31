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
public class AadharModel extends DataEntity<AadharModel> {

	/**
	 * @param _Name
	 */
	public AadharModel() {
		super("AadharModel");
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public AadharModel GetNewObject() {
		// TODO Auto-generated method stub
		return new AadharModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub

		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("PanCardNo", DataTypes.TEXT);
		this.RegisterMapping("AadhaarCardNo", DataTypes.TEXT);
		this.RegisterMapping("AadhaarDocID", DataTypes.TEXT);

		this.RegisterMapping("PanDocID", DataTypes.TEXT);

	}

	public String UserName = MainActivity.MyInfo.EmployeeCode;
	public String PanCardNo;
	public String AadhaarCardNo;
	public String PanDocID;
	public String AadhaarDocID;

}
