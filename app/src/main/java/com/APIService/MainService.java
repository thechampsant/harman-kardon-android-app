package com.APIService;

import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import app.core.base.BaseService;
import mob.field.harmonkardonff.services.UpdateService;

@SuppressWarnings("rawtypes")
public class MainService extends BaseService {

	UpdateService uService = new UpdateService();
	public static MainService Current;
	public static QuizDBLayer qdb;

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate() {
		Current = this;
		this.EnableLocalDatabase(Config.DBNAME, Config.Version);
		qdb = new QuizDBLayer();
		Invoke(this, qdb);
		this.Invoke(this);
		this.getScheduler().scheduleAtFixedRate(attendanceWork, 1, 15,
				TimeUnit.MINUTES);
		this.getScheduler().scheduleAtFixedRate(updatePendingWork, 2, 20,
				TimeUnit.SECONDS);

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

}
