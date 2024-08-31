package mob.field.harmonkardonff.fragments;

import java.util.Date;

import mob.field.harmonkardonff.entitiymodels.OtherInfoModel;
import mob.field.harmonkardonff.services.WebService;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.onDateSetListener;

import com.fieldforce.harmonkardonff.R;

public class FootFallEnter extends IFragment {

	private OtherInfoModel otherinfo = new OtherInfoModel();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle arg2) {
		return this.InflateView(R.layout.activity_footfall_enter, inflater,
				container);
	}

	@Override
	public void Activate(View arg0) {
		try {

			attachDatePicker();
			otherinfo.ForDate = GetCurrentDateInString();
			SetTextViewAsString(R.id.txt_for_date, otherinfo.ForDate);
			SetOnClickListenerOnButton(R.id.btn_send_footfall,
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							trysubmitFootfall();
						}
					});
		} catch (Exception ex) {
			ShowToastLong(ex.getMessage(), 0);
			this.context.finish();
		}

	}

	private void attachDatePicker() {

		this.attachDatePicker(R.id.btn_for_date, new onDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, Date date, String sdate) {
				SetTextViewAsString(R.id.txt_for_date, sdate);
				otherinfo.ForDate = sdate;
			}
		});
	}

	private void trysubmitFootfall() {
		try {
			updateFootfallModel();
			if (validate())
				submitFootfallOnServer();
		} catch (Exception ex) {
			ShowToastLong(ex.getMessage(), 0);
		}
	}

	private void updateFootfallModel() {
		otherinfo.Footfall = GetEditTextAsInt(R.id.edt_footfall);
		otherinfo.NoOfQuery = GetEditTextAsInt(R.id.edt_query);
	}

	private boolean validate() {
		if (validateDate() && validateFootfall())
			return true;
		else
			return false;
	}

	private boolean validateFootfall() {
		if (otherinfo.Footfall < 0) {
			ShowToastLong("Please fill valid Footfall", 0);
			GetEditText(R.id.edt_footfall).setError("Invalid footfall");
			return false;
		} else if (otherinfo.NoOfQuery < 0) {
			ShowToastLong("Please fill valid Footfall", 0);
			GetEditText(R.id.edt_query).setError("Invalid query no.");
			return false;
		} else if (otherinfo.NoOfQuery > otherinfo.Footfall) {
			ShowToastLong("No of query can not exceed from footfall", 0);
			GetEditText(R.id.edt_query).setError("Invalid query no.");
			return false;
		} else
			return true;
	}

	private boolean validateDate() {
		int backday = 7;
		Date ForDate = ConvertStringToDate(otherinfo.ForDate);
		if (ForDate.after(GetCurrentDate())) {
			ShowToastLong("Future Dates are not allowed", 0);
			return false;
		} else if (ForDate.before(getBackDate(backday))) {
			ShowToastLong("Only Past " + backday + " days footfall is allowed",
					0);
			return false;
		} else
			return true;
	}

	private void submitFootfallOnServer() {
		final WebService server = new WebService();
		if (isNetworkAvailable()) {
			BackgroundProcess bp = new BackgroundProcess(this)
					.setProgressMessage("sending to server..");
			bp.setbackgroundProcess(new IProcess() {

				@SuppressWarnings("rawtypes")
				@Override
				public void processResponse(Object arg0) throws Exception {
					ProcessFootfallResponse((Response) arg0);
				}

				@Override
				public Object underProcess() throws Exception {
					return server.TryUpdateOtherInfo(otherinfo);
				}
			});

			bp.execute(null, null, null);
		} else {
			otherinfo.InsertOrUpdate();
			ShowToastLong("No network found!Try later", 0);
			this.setTab(1);

		}
	}

	@SuppressWarnings("rawtypes")
	private void ProcessFootfallResponse(Response response) {
		if (response.isSuccess()) {
			otherinfo.IsUpdated = "true";
			otherinfo.InsertOrUpdate();
			this.ShowToast("footfall updated successfully!");
		} else {
			ShowToastLong(response.errormsg, 0);
			ShowToast("unable to upload footfall!");
			otherinfo.InsertOrUpdate();
		}
		this.setTab(1);
	}

}
