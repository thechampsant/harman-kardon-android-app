package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class VersionUpdationModel extends DataEntity<VersionUpdationModel>{

	public String AppVersion;
	public String force_update;
	public String soft_update;
	public String IsActive;

	public VersionUpdationModel() {
		// TODO Auto-generated constructor stub
		super("VersionResponse");
	}

	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("AppVersion", DataTypes.TEXT);
		this.RegisterMapping("force_update", DataTypes.TEXT);
		this.RegisterMapping("soft_update", DataTypes.TEXT);
		this.RegisterMapping("IsActive", DataTypes.TEXT);
	}

	@Override
	public VersionUpdationModel GetNewObject() {
		// TODO Auto-generated method stub
		return new VersionUpdationModel();
	}


}
