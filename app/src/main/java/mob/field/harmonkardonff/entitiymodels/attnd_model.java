package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class attnd_model extends DataEntity<attnd_model> {

	static String TableName = "ViewAttendance";
	public String Date;
	public String IsMarked;
	public String Attendance;
	public String CheckIn;
	public String CheckOut;
	public String WorkingHours;

	public attnd_model() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("Date", DataTypes.TEXT);
		this.RegisterMapping("IsMarked", DataTypes.TEXT);
		this.RegisterMapping("Attendance", DataTypes.TEXT);
		this.RegisterMapping("CheckIn", DataTypes.TEXT);
		this.RegisterMapping("CheckOut", DataTypes.TEXT);
		this.RegisterMapping("WorkingHours", DataTypes.TEXT);

	}

	@Override
	public attnd_model GetNewObject() {
		// TODO Auto-generated method stub
		return new attnd_model();
	}

}
