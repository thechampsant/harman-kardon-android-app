package mob.field.harmonkardonff.services;

import java.util.Date;

import com.fieldforce.harmonkardonff.MainActivity;

import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.entitiymodels.NewSaleModel;
import android.os.AsyncTask;
import android.util.Log;
import app.core.model.Response;

public class UpdateService {

	private String TAG = UpdateService.class.getSimpleName();
	public boolean IsSuccess = false;
	public String Message = "";
	WebService web = new WebService();

	// ------------------Verification-----------------------//

	@SuppressWarnings("deprecation")
	public boolean isShowAttendacePending() {
		Date Fdate = new Date();
		Fdate.setHours(12);
		Fdate.setMinutes(30);

		Date Cdate = new Date();
		if (Cdate.after(Fdate))
			return true;
		else
			return false;
	}

	public boolean isMarked() {
		boolean ismarked = MainActivity.MyAttendances.Any("ForDate",
				WHPL_MainService.service.GetCurrentDateInString());
		return !ismarked;
	}

	public boolean isTodayAttendancePending() {
		try {
			if (MainActivity.MyAttendances == null
					|| MainActivity.MyAttendances.Count() == 0) {
				if (MainActivity.Database == null)
					return false;
				else {
					MainActivity.Database.LoadAttendancesFromDb();
					return isMarked();
				}
			} else
				return isMarked();
		} catch (Exception ex) {
			IsSuccess = false;
			Message = ex.getMessage();
			return false;
		}

	}

	public boolean isSalePending() {
		try {
			if (MainActivity.MySales == null
					|| MainActivity.MySales.Count() == 0)
				return false;
			else {
				boolean isPending = MainActivity.MySales.Any("IsUpdated",
						"false");
				return isPending;
			}
		} catch (Exception ex) {
			IsSuccess = false;
			Message = ex.getMessage();
			return false;
		}
	}

	      public boolean isAttendancePending() {
		try {
			if (MainActivity.MyAttendances == null
					|| MainActivity.MyAttendances.Count() == 0)
				return false;
			else {
				boolean isPending = MainActivity.MyAttendances.Any(
						"IsOfflineOnly", "true");
				return isPending;
			}
		} catch (Exception ex) {
			IsSuccess = false;
			Message = ex.getMessage();
			return false;
		}
	}

	// -------------------Update pending data methods----------------//

	public void tryShowPendingAttendanceNotification() {
		try {
			if (isShowAttendacePending() && isTodayAttendancePending()) {
				Log.i(TAG, "pending attednace nofifcation raised !");
				NotifyService.notityForPendingAttendance();
			} else {
				Log.i(TAG,
						"waiting for notify the user for pending attendance!");
			}
		} catch (Exception ex) {
		}
	}

	public void tryUpdateOfflineData() {
		try {
			if (!WHPL_MainService.service.isNetworkAvailable())
				return;
			tryUpdateSale();
			tryUpdateAttendance();
		} catch (Exception ex) {
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void tryUpdateSale() {
		if (!isSalePending()) {
			Log.i(TAG, "No sale found to update!");
			return;
		}
		Log.i(TAG, " Updating sale in asynch!");
		new AsyncTask() {

			@Override
			protected void onPostExecute(Object result) {
				if (IsSuccess) {
					WHPL_MainService.service.ShowToast("Sale Updated Successfully!");
					NotifyService.notityForUpdatedSale();
				} else {
					Log.i(TAG, Message);
				}
				super.onPostExecute(result);
			}

			@Override
			    protected Object doInBackground(Object... params) {
				for (NewSaleModel model : MainActivity.MySales.where(
						"IsUpdated", "false")) {
					try {
						Response response = web.TryUpdateSale(model);
						if (response.isSuccess()) {
							model.IsUpdated = "true";
							model.InsertOrUpdate();
							MainActivity.MySales.remove(model);
							IsSuccess = true;
						}

					} catch (Exception ex) {
						IsSuccess = false;
						Message = ex.getMessage();
						break;
					}
				}
				return null;
			}
		}.execute("");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void tryUpdateAttendance() {

		if (!this.isAttendancePending()) {
			Log.i(TAG, "No attendance found to update!");
			return;
		}
		Log.i(TAG, " Updating attendance in asynch!");
		new AsyncTask() {

			@Override
			protected void onPostExecute(Object result) {
				if (IsSuccess) {
					WHPL_MainService.service
							.ShowToast("Attendance Updated Successfully!");
					NotifyService.notityForUpdatedAttendance();
				} else {
					Log.i(TAG, Message);
				}
				super.onPostExecute(result);
			}

			@Override
			protected Object doInBackground(Object... params) {
				for (MDAT model : MainActivity.MyAttendances.where(
						"IsOfflineOnly", "true")) {
					try {
						Response response = web.TryMarkAttendance(model);
						if (response.isSuccess()) {
							model.IsOfflineOnly = "false";
							model.InsertOrUpdate();
							MainActivity.MyAttendances.remove(model);
						}
						IsSuccess = true;
					} catch (Exception ex) {
						IsSuccess = false;
						Message = ex.getMessage();
						break;
					}
				}
				return null;
			}
		}.execute("");
	}

}
