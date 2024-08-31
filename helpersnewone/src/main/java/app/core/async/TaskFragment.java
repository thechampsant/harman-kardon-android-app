package app.core.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;


@SuppressLint("NewApi")
public class TaskFragment extends Fragment {

	/**
	 * Callback interface through which the fragment will report the task's
	 * progress and results back to the Activity.
	 */
	public static interface TaskCallbacks {
		public void callThread();

		public void retainThread();
	}

	private TaskCallbacks mCallbacks = null;
	public BackgroundProcess mTask = null;
	private Activity activity;
	public Object backupData = null;

	/**
	 * Hold a reference to the parent Activity so we can report the task's
	 * current progress and results. The Android framework will pass us a
	 * reference to the newly created Activity after each configuration change.
	 */
	@Override
	public void onAttach(Activity _activity) {
		super.onAttach(activity);
		activity = _activity;
		try {
			mCallbacks = (TaskCallbacks) activity;
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		if (mTask == null)
			mTask = new BackgroundProcess(activity);
		else if (!mTask.showProgress()) {
			//mCallbacks.retainThread();
			retainThreadinBackground();
		}

	}

	     private void retainThreadinBackground() {
		BackgroundProcess bp= new BackgroundProcess(activity);
		bp.showMessage(false,false);
		bp.setProgressDialog(false,false);
		bp.setbackgroundProcess(new IProcess(){

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				java.lang.Thread.sleep(1000);
				return null;
			}

			@Override
			public void processResponse(Object response) throws Exception {
				// TODO Auto-generated method stub
				mCallbacks.retainThread();
			}});
		bp.execute(null,null,null);
	}

	/**
	 * This method will only be called once when the retained Fragment is first
	 * created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);

		if (mTask != null && mCallbacks != null) {
			mCallbacks.callThread();
		}

		// Create and execute the background task.
		// mTask = new BackgroundProcess(activity);
		// mTask.execute(null,null,null);
	}

	/**
	 * Set the callback to null so we don't accidentally leak the Activity
	 * instance.
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

}