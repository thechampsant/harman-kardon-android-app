package mob.field.harmonkardonff.services;

import app.core.model.NotificationModel;
import app.core.utils.NotificationHandler;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SplashScreen;


public class NotifyService {

	
	
	
	private static NotificationHandler nhandler = null;
	private static NotificationHandler getNotifier() {
		if (nhandler == null) {
			nhandler = new NotificationHandler(WHPL_MainService.service);
			return nhandler;
		} else
			return nhandler;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void notityForPendingAttendance() {

		NotificationModel model = new NotificationModel();
		model.PendingIntent = SplashScreen.class;
		model.ImageResourceID = R.drawable.profile_new;
		model.NotifyID = 1111;

		model.MessageHeading = "Attendance is pending";
		model.Message = "Please mark your attendance";
		model.NotificationMessage = "Please mark your attendance!!";

		getNotifier().Notify(model);
	}

	public static void unNotityForPendingAttendance() {
		int NotifyID = 1111;
		getNotifier().UnNotify(NotifyID);
	}
	


	  @SuppressWarnings({ "rawtypes", "unchecked" })
	   public static void notityForUpdatedSale() {
		NotificationModel model = new NotificationModel();
		model.PendingIntent = SplashScreen.class;
		model.ImageResourceID = R.drawable.sales_entry;
		model.NotifyID = 1112;

		model.MessageHeading = "Sales updated successfully !";
		model.Message = "Sales updated successfully ";
		model.NotificationMessage = "Sales updated successfully!!";
		

		getNotifier().Notify(model);
		
	     }
	     @SuppressWarnings({ "rawtypes", "unchecked" })
	     public static void notityForUpdatedAttendance() {
		 NotificationModel model = new NotificationModel();
		 model.PendingIntent = SplashScreen.class;
		 model.ImageResourceID = R.drawable.mark_attendance;
		 model.NotifyID = 1113;

		model.MessageHeading = "Attendance updated successfully !";
		model.Message = "Attendance updated successfully ";
		model.NotificationMessage = "Attendance updated successfully!!";

		getNotifier().Notify(model);		
	}
}
