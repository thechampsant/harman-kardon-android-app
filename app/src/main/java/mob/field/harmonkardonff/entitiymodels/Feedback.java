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
public class Feedback extends DataEntity<Feedback> {

	/**
	 * @param _Name
	 */
	public Feedback() {
		super("MFeedback");
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public Feedback GetNewObject() {
		// TODO Auto-generated method stub
		return new Feedback();
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("_ID", DataTypes.INT);
		this.RegisterMapping("Remarks", DataTypes.TEXT);		
		this.RegisterMapping("Date", DataTypes.TEXT);
		this.RegisterMapping("DocIDs", DataTypes.TEXT);
		this.RegisterMapping("PID", DataTypes.TEXT);
	}
	
	public int _ID;
	public String UserName=MainActivity.MyInfo.EmployeeCode;
	public String Remarks;
	public String Date;
	public String DocIDs;
	public String PID;
}
