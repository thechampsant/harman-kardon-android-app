package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class DocumentModel extends DataEntity<DocumentModel> {
	static String TableName = "DocumentModel";
	public String DocName;
	public String DocURL;
	public String Thumbnail;

	public String getThumbnail() {
		return Thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}

	public DocumentModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void RegisterMappings() {
		this.RegisterMapping("DocName", DataTypes.TEXT);
		this.RegisterMapping("DocURL", DataTypes.TEXT);
		this.RegisterMapping("Thumbnail", DataTypes.TEXT);

	}

	@Override
	public DocumentModel GetNewObject() {
		// TODO Auto-generated method stub
		return new DocumentModel();
	}

}
