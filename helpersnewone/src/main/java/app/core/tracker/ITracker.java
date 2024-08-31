package app.core.tracker;
import com.google.android.gms.analytics.Tracker;

public interface ITracker {

	public Tracker getTracker(String trackerId);
	public void enableLogger(int LogLevel);
}
