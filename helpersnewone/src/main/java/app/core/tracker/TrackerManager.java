package app.core.tracker;

import java.util.HashMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.content.Context;


public class TrackerManager implements ITracker {

	private Context context;
	HashMap<String, Tracker> mTrackers = new HashMap<String, Tracker>();

	public TrackerManager(Context context) {
		this.context = context;
	}

	synchronized Tracker setTracker(String trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
			Tracker t= analytics.newTracker(trackerId);
			mTrackers.put(trackerId, t);

		}
		return mTrackers.get(trackerId);
	}

	     public Tracker getTracker(String trackerId) {
		          return setTracker(trackerId);
	}

	@Override
	public void enableLogger(int LogLevel) {
		GoogleAnalytics.getInstance(context).getLogger()
	    .setLogLevel(LogLevel);
		
	}
	
	

	



}
