package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;

import com.fieldforce.harmonkardonff.MainActivity;


public class Complain extends DataEntity<Complain> {



	public static String TableName="Complain";
	public String UserName=MainActivity.MyInfo.EmployeeCode;
	public String MobileNo;
	public  String Remarks = null;
	public String Name;
	public String Number;
	public String DocIDs;
	public String Date;
	
	public Complain() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Complain GetNewObject() {
		// TODO Auto-generated method stub
		return new Complain();
	}
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		
	}

}
