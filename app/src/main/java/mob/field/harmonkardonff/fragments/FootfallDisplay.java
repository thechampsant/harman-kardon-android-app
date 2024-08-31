package mob.field.harmonkardonff.fragments;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.OtherInfoModel;
import mob.field.harmonkardonff.services.WebService;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;

import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;

public class FootfallDisplay extends IFragment {

	WebService server = new WebService();
	ArrayList<OtherInfoModel> Data = new ArrayList<OtherInfoModel>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle arg2) {
		return this.InflateView(R.layout.activity_footfall_display, inflater,
				container);
	}

	@Override
	public void Activate(View arg0) {
		try {
			AttachUpdateFootfallButton();
			SetVisibilityOFUpdateButton();
			SetList();
			
		} catch (Exception ex) {
			ShowToastLong(ex.getMessage(), 0);
			this.context.finish();
		}

	}
	private void AttachUpdateFootfallButton() {
		this.SetOnClickListenerOnButton(R.id.btn_update_pending_footfall,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						UpdateAllPendingFootfall();
					}
				});
	}
	private boolean IsAnyPending() {
		return MainActivity.footfalls.Any("IsUpdated", "false");
	}
	private void RefreshFootfall() {
		Data = MainActivity.Database.LoadFootfallFromDb();
	}
	private void SetVisibilityOFUpdateButton() {
		RefreshFootfall();
		UpdateShowUpdateButton(IsAnyPending());
	}
	private void UpdateShowUpdateButton(boolean IsPending) {
		if (MainActivity.footfalls.Count() < 1) {
			setVisibility(R.id.btn_update_pending_footfall, View.GONE);
			GetTextView(R.id.txt_msg).setVisibility(View.VISIBLE);
			SetTextViewAsString(R.id.txt_msg, "No footfall record!");
		} else {
			if (IsPending) {
				this.setVisibility(R.id.btn_update_pending_footfall, View.VISIBLE);
				this.GetTextView(R.id.txt_msg).setVisibility(View.GONE);
			} else {
				this.setVisibility(R.id.btn_update_pending_footfall, View.GONE);
				this.GetTextView(R.id.txt_msg).setVisibility(View.GONE);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void SetList() {

		ListView lv = (ListView) this.findViewById(R.id.lv_footfalllist);
		ArrayList<OtherInfoModel> orderedList = MainActivity.footfalls.Reverse();
		GenricAdapter adaptor = new GenricAdapter(this.context,
				R.layout.listitem_footfall).setData(orderedList);
		adaptor.setGenricAdapter(new IAdapter<OtherInfoModel>() {

			@Override
			public void setItemView(OtherInfoModel item, View view, int index) {
				TextView footfall = (TextView) view
						.findViewById(R.id.txt_footfall);
				TextView query = (TextView) view
						.findViewById(R.id.txt_query);
				TextView date = (TextView) view
						.findViewById(R.id.txt_fordate);

				ImageView img = (ImageView) view.findViewById(R.id.myimg);// Pending
																			// Image

				if (item.IsUpdated.equalsIgnoreCase("true")) {
					img.setVisibility(View.INVISIBLE);
				}

				footfall.setText("Footfall: "+String.valueOf(item.Footfall));
				query.setText("No of query: " + String.valueOf(item.NoOfQuery));
				date.setText(item.ForDate);
			}

		});
		lv.setAdapter(adaptor);
	}
	
	
	private void UpdateAllPendingFootfall() {
		if (!IsAnyPending()) {
			ShowToast("No footfall found to update!");
			return;
		}
		if (!isNetworkFoundDialog())
			return;
		BackgroundProcess bp = new BackgroundProcess(this.context)
				.setProgressMessage("sending to server...");
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings("rawtypes")
			@Override
			public void processResponse(Object arg0) throws Exception {
				ProcessSubmitFootfallResponse((Response) arg0);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object underProcess() throws Exception {
				Response response = null;
				for (OtherInfoModel model : MainActivity.footfalls.where(
						"IsUpdated", "false")) {
					response = server.TryUpdateOtherInfo(model);
					if (!response.isSuccess())
						break;
					else {
						model.IsUpdated = "true";
						model.InsertOrUpdate();
					}
				}
				return response;
			}
		});

		bp.execute(null, null, null);
	}
	@SuppressWarnings("rawtypes")
	private void ProcessSubmitFootfallResponse(Response response) {
		if (response.status.equalsIgnoreCase("true")) {
			ShowToast("Updated Sucessfully");
			SetVisibilityOFUpdateButton();
			SetList();
		} else {
			SetVisibilityOFUpdateButton();
			new Dialog(this.context).setTitle("Error").show(response.errormsg);
			ShowToast("Unable to update");
			SetList();
		}
	}

	

}
