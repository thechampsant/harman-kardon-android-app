package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;

public class AllPendingPushNotificationMsg extends DataEntity<AllPendingPushNotificationMsg>{

	public String Message;
	public String HeaderID;
	/**
	 * @param _Name
	 */
	static String TableName="acknowledgeresponse";
	public AllPendingPushNotificationMsg() {
		super(TableName);
	}

	public String getMessage() {
		return Message;
	}



	public void setMessage(String message) {
		Message = message;
	}



	public String getHeaderID() {
		return HeaderID;
	}



	public void setHeaderID(String headerID) {
		HeaderID = headerID;
	}



	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public AllPendingPushNotificationMsg GetNewObject() {
		// TODO Auto-generated method stub
		return new AllPendingPushNotificationMsg();
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		}
}
