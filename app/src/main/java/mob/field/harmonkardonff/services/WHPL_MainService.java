package mob.field.harmonkardonff.services;

import java.util.concurrent.TimeUnit;

import mob.field.harmonkardonff.quiz.QuizDBLayer;
import mob.field.harmonkardonff.survey.SurveyDBLayer;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import app.core.base.BaseService;

@SuppressWarnings("rawtypes")
public class WHPL_MainService extends BaseService {
	
	UpdateService uService = new UpdateService();
	public static WHPL_MainService Current;
	public static QuizDBLayer qdb;
	public static SurveyDBLayer sdb;
	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate() {
		Current = this;
		initializeQuizRelatedData();
		initializeSurveyRelatedData();
		
//		this.Invoke(this);
		this.getScheduler().scheduleAtFixedRate(attendanceWork, 1, 15,
				TimeUnit.MINUTES);
		this.getScheduler().scheduleAtFixedRate(updatePendingWork, 3, 17,
				TimeUnit.MINUTES);		

	}

	private void initializeSurveyRelatedData() {
		sdb = new SurveyDBLayer();
		
	}

	private void initializeQuizRelatedData() {
		qdb = new QuizDBLayer();
		Invoke(this, qdb);
	}

	@Override
	public void onStart(Intent intent, int startId) {

	}

	@Override
	public void onDestroy() {
		Toast.makeText(service, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

	// --------------------Background Runnable task---------------------//

	Runnable attendanceWork = new Runnable() {

		@Override
		public void run() {
			uService.tryShowPendingAttendanceNotification();
		}
	};

	Runnable updatePendingWork = new Runnable() {

		@Override
		public void run() {
			uService.tryUpdateOfflineData();
		}
	};

	@Override
	public int setThreadPoolSize() {
		return 2;
	}

	@Override
	public void RegisterTableInfoForLocalDB() {

	}
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	// TODO Auto-generated method stub
	return Service.START_STICKY;
}
}
