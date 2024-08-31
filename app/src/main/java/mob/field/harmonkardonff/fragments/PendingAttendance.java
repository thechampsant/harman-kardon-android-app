package mob.field.harmonkardonff.fragments;

import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.services.NotifyService;
import mob.field.harmonkardonff.services.WebService;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
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
import app.core.utils.Message;

import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;

public class PendingAttendance extends IFragment {

	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		return this.InflateView(R.layout.activity_attendance_display, arg0,
				arg1);
	}

	WebService web = new WebService();

	@Override
	public void Activate(View arg0) {
		// TODO Auto-generated method stub
		RefreshDAT();
		SetVisibilityOFUpdateButton();
		AttachUpdateDATButtonHandler();
		SetList();
		setHasOptionsMenu(true);
		this.hideKeyPad();
	}

	public boolean IsAnyPending() {
		return MainActivity.MyAttendances.Any("IsOfflineOnly", "true");
	}

	private void RefreshDAT() {
		MainActivity.Database.LoadAttendancesFromDb();
	}

	private void AttachUpdateDATButtonHandler() {
		// TODO Auto-generated method stub
		Button btn_NewEntry = this.GetButton(R.id.btnupdatedat);
		btn_NewEntry.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateAllPending();

			}
		});
	}

	@SuppressWarnings("rawtypes")
	private void UpdateAllPending() {
		// TODO Auto-generated method stub
		if (!IsAnyPending()) {
			ShowToast("Nothing to update!Already updated");
			return;
		}
		if (!this.isNetworkFoundDialog())
			return;
		BackgroundProcess bp = new BackgroundProcess(this.context)
				.setProgressMessage("sending to server...");
		bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				ProcessSubmitDATResponse((Response) arg0);
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				Response response = null;
				for (MDAT model : MainActivity.MyAttendances.where(
						"IsOfflineOnly", "true")) {
					response = web.TryMarkAttendance(model);
					if (response.status.equalsIgnoreCase("false"))
						break;
					else {
						model.IsOfflineOnly = "false";
						model.InsertOrUpdate();
					}
				}
				return response;
			}
		});
		bp.execute(null, null, null);
	}

	@SuppressWarnings("rawtypes")
	public void ProcessSubmitDATResponse(Response response) {

		if (response.status.equalsIgnoreCase("true")) {
			ShowToast(Message.SUCCESS_SERVER);
			SetVisibilityOFUpdateButton();
			NotifyService.unNotityForPendingAttendance();
			SetList();
		} else {
			SetVisibilityOFUpdateButton();
			new Dialog(this.context).setTitle("Error").show(response.errormsg);
			ShowToast(Message.ERROR_SERVER);
			SetList();
		}
	}

	private void SetVisibilityOFUpdateButton() {
		RefreshDAT();
		UpdateShowUpdateButton(IsAnyPending());
	}

	private void UpdateShowUpdateButton(boolean IsPending) {
		if (MainActivity.MyAttendances.Count() < 1) {
			setVisibility(R.id.btnupdatedat, View.GONE);
			GetTextView(R.id.tbxcat_dat).setVisibility(View.VISIBLE);
			SetTextViewAsString(R.id.tbxcat_dat, "No Pending Attendance!");
		} else {
			if (IsPending) {
				this.setVisibility(R.id.btnupdatedat, View.VISIBLE);
				this.GetTextView(R.id.tbxcat_dat).setVisibility(View.GONE);
			} else {
				this.setVisibility(R.id.btnupdatedat, View.GONE);
				this.GetTextView(R.id.tbxcat_dat).setVisibility(View.VISIBLE);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void SetList() {

		if (MainActivity.MyAttendances == null)
			return;
		ListView lv = this.getListView(R.id.lvmydat);
		if (lv == null)
			return;
		lv.setClickable(true);

		GenricAdapter adaptor = new GenricAdapter(this.context,
				R.layout.listitem3).setData(MainActivity.MyAttendances.where(
				"IsOfflineOnly", "true"));
		adaptor.setGenricAdapter(new IAdapter<MDAT>() {

			@Override
			public void setItemView(MDAT item, View view, int index) {

				TextView ForDate = (TextView) view
						.findViewById(R.id.itemsalefordate); // Date
				TextView Option = (TextView) view
						.findViewById(R.id.itemsaleqty); // Count
				ImageView img = (ImageView) view.findViewById(R.id.imgl);// Image

				if (item.IsOfflineOnly.equalsIgnoreCase("false")) {
					img.setVisibility(View.INVISIBLE);
				}
				ForDate.setText(item.ForDate);
				Option.setText(item.option);
			}

		});
		lv.setAdapter(adaptor);
		lv.setOnCreateContextMenuListener(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		this.context.getMenuInflater().inflate(
				R.menu.attendance_display_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.fdelete:
			Delete(info);
			this.RefreshDAT();
			SetVisibilityOFUpdateButton();
			SetList();
			return true;
		case R.id.fupdate:
			Update(info);
			SetVisibilityOFUpdateButton();
			SetList();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void UpdatePendingDAT(final MDAT model) {
		if (!this.isNetworkFoundDialog())
			return;
		BackgroundProcess bp = new BackgroundProcess(this.context)
				.setProgressMessage("sending to server...");
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings("rawtypes")
			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				ProcessSubmitDATResponse((Response) arg0);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				Response response = null;
				response = web.TryMarkAttendance(model);
				if (response.status.equalsIgnoreCase("true")) {
					model.IsOfflineOnly = "false";
					model.InsertOrUpdate();
				}
				return response;
			}
		});
		bp.execute(null, null, null);
	}

	private void Update(AdapterContextMenuInfo info) {
		// TODO Auto-generated method stub
		MDAT DAT = MainActivity.MyAttendances.get(info.position);
		if (DAT.IsOfflineOnly.equalsIgnoreCase("true"))
			this.UpdatePendingDAT(DAT);
		else
			this.ShowToast("It is already Updated On Server");
		Log.v("Info:", info.toString());
	}

	private void Delete(AdapterContextMenuInfo info) {
		// TODO Auto-generated method stub

		MDAT DAT = MainActivity.MyAttendances.get(info.position);
		DAT.Delete();
		this.ShowToast("Deleted sucessfully!");
		Log.v("Info:", info.toString());
	}

	// ------------------------option menu of setting------------------//
	// -----------------------------------------------------------------//

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.attendance_display, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_update_all:
			this.UpdateAllPending();
			SetVisibilityOFUpdateButton();
			SetList();
			return true;
		case R.id.action_clear_saved:
			MainActivity.Database.CleanAllSavedDAT();
			this.RefreshDAT();
			SetVisibilityOFUpdateButton();
			SetList();
			ShowToast("Opration Ok");
			return true;
		case R.id.action_clear_all_pending:
			MainActivity.Database.CleanAllPendingDAT();
			this.RefreshDAT();
			SetVisibilityOFUpdateButton();
			SetList();
			ShowToast("Opration Ok");
			return true;
		case R.id.action_clear_all:
			MainActivity.Database.CleanAllDAT();
			this.RefreshDAT();
			SetVisibilityOFUpdateButton();
			SetList();
			ShowToast("Opration Ok");
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
