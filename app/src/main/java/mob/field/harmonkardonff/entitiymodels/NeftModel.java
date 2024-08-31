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
public class NeftModel extends DataEntity<NeftModel> {

	/**
	 * @param _Name
	 */
	public NeftModel() {
		super("NeftModel");
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public NeftModel GetNewObject() {
		// TODO Auto-generated method stub
		return new NeftModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub

		this.RegisterMapping("Name", DataTypes.TEXT);
		this.RegisterMapping("DocID", DataTypes.TEXT);
		this.RegisterMapping("AccountNo", DataTypes.TEXT);
		this.RegisterMapping("BankName", DataTypes.TEXT);
		this.RegisterMapping("BranchLocation", DataTypes.TEXT);
		this.RegisterMapping("IFSC", DataTypes.TEXT);
		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("UanNo", DataTypes.TEXT);


	}

	public String UserName = MainActivity.MyInfo.EmployeeCode;
	public String Name;
	public String DocID;
	public String AccountNo;
	public String BranchLocation;
	public String UanNo;


	public String BankName;

	public String IFSC;

}
