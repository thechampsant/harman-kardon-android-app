package mob.field.harmonkardonff.entitiymodels;

import com.fieldforce.harmonkardonff.MainActivity;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class OtherInfoModel extends DataEntity<OtherInfoModel> {

	public OtherInfoModel() {
		super("OtherInfoModel");
	}

	public int Footfall;
	public int NoOfQuery;
	public String ForDate;
	public String Remarks;
	public String IsUpdated = "false";
	public String UserName = MainActivity.MyInfo.EmployeeCode;

	

	@Override
	public void RegisterMappings() {

		this.RegisterMapping("Footfall", DataTypes.INT);
		this.RegisterMapping("NoOfQuery", DataTypes.INT);
		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("ForDate", DataTypes.TEXT);
		this.RegisterMapping("IsUpdated", DataTypes.TEXT);

	}

	@Override
	public OtherInfoModel GetNewObject() {

		return new OtherInfoModel();
	}

}
