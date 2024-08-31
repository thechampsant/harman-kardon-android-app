package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class MNotification extends DataEntity<MNotification> {
	
	
//	DataEntity used for local Storage  
//	public static String _TableName="MNotification";
	public String Message;
	public String Date;
	public String ReadStatus;
	public String isMessage;
	public String isImageUrl;
	public String imageUrl;
	public String isTextTitle;
	public String textTitle;
	public String HeaderID;
	public String isSeen = "false";
	
	@SuppressWarnings("deprecation")
	public MNotification() {
		super("MNotification");
		// TODO Auto-generated constructor stub	
//		date = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm").format(Calendar.getInstance().getTime());
		ReadStatus = "NO";
		
	}
	
	@Override
	public MNotification GetNewObject() {
		// TODO Auto-generated method stub
		return new MNotification();
	}

	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("Message", DataTypes.TEXT);
		this.RegisterMapping("Date", DataTypes.TEXT);
		this.RegisterMapping("ReadStatus", DataTypes.TEXT);
		this.RegisterMapping("isMessage", DataTypes.TEXT);
		this.RegisterMapping("isImageUrl", DataTypes.TEXT);
		this.RegisterMapping("imageUrl", DataTypes.TEXT);
		this.RegisterMapping("isTextTitle", DataTypes.TEXT);
		this.RegisterMapping("textTitle", DataTypes.TEXT);
		this.RegisterMapping("HeaderID", DataTypes.TEXT);
		this.RegisterMapping("isSeen", DataTypes.TEXT);
	}
	
}
