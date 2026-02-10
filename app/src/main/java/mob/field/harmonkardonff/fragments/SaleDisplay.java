package mob.field.harmonkardonff.fragments;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.NewSaleModel;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
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

public class SaleDisplay extends IFragment {

	WebService server = new WebService();
	ArrayList<NewSaleModel> Data = new ArrayList<NewSaleModel>();
	ArrayList<NewSaleModel> orderedList = new ArrayList<NewSaleModel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return this.InflateView(R.layout.activity_sale_display, inflater,
				container);
	}

	@Override
	public void Activate(View arg0) {
		// TODO Auto-generated method stub
		AttachUpdateSaleButton();
		SetVisibilityOFUpdateButton();
		SetList();
		setHasOptionsMenu(true);
		this.hideKeyPad();
		SetTextViewAsString(R.id.pending_sale_current_user_textview, "Current User : "+WebService.UserName);
	}

	// Check if any Sales is Pending in Local DB
	private boolean IsAnyPending() {
		return MainActivity.MySales.Any("IsUpdated", "false");
	}

	private void RefreshMySales() {
		Data = MainActivity.Database.LoadSalesFromLocalDb();
	}

	// Fetch Sales From LOCAL DB
	private void SetVisibilityOFUpdateButton() {
		RefreshMySales();
		UpdateShowUpdateButton(IsAnyPending());
	}

	@SuppressWarnings("rawtypes")
	private void ProcessSubmitSaleResponse(Response response) {
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

	private void AttachUpdateSaleButton() {
		this.SetOnClickListenerOnButton(R.id.btn_update_pending_sale,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						UpdateAllPendingSale();
					}
				});
	}

	public void UpdateAllPendingSale() {
		if (!IsAnyPending()) {
			ShowToast("No Sale found to update!");
			return;
		}
		if (!isNetworkFoundDialog())
			return;
		BackgroundProcess bp = new BackgroundProcess(this.context)
				.setProgressMessage("sending to server...");
		bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				ProcessSubmitSaleResponse((Response) arg0);
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				Response response = null;
				for (NewSaleModel model : MainActivity.MySales.where(
						"IsUpdated", "false")) {
					response = server.TryUpdateSale(model);
					if (response.status.equalsIgnoreCase("false"))
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

	public void UpdatePendingSale(final NewSaleModel model) {
		if (!this.isNetworkFoundDialog())
			return;
		BackgroundProcess bp = new BackgroundProcess(this.context)
				.setProgressMessage("sending to server...");
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings("rawtypes")
			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				ProcessSubmitSaleResponse((Response) arg0);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				Response response = null;
				response = server.TryUpdateSale(model);
				if (response.status.equalsIgnoreCase("true")) {
					model.IsUpdated = "true";
					model.InsertOrUpdate();
				}
				return response;
			}
		});
		bp.execute(null, null, null);
	}

	private void UpdateShowUpdateButton(boolean IsPending) {
		if (MainActivity.MySales.Count() < 1) {
			setVisibility(R.id.btn_update_pending_sale, View.GONE);
			GetTextView(R.id.txt_msg).setVisibility(View.VISIBLE);
			SetTextViewAsString(R.id.txt_msg, "No sale record!");
		} else {
			if (IsPending) {
				this.setVisibility(R.id.btn_update_pending_sale, View.VISIBLE);
				this.GetTextView(R.id.txt_msg).setVisibility(View.GONE);
			} else {
				this.setVisibility(R.id.btn_update_pending_sale, View.GONE);
				this.GetTextView(R.id.txt_msg).setVisibility(View.GONE);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void SetList() {

		ListView lv = (ListView) this.findViewById(R.id.lv_salelist);
	   orderedList = MainActivity.MySales.where(
				"IsUpdated", "false").Reverse();

		GenricAdapter adaptor = new GenricAdapter(this.context,
				R.layout.listitem_sale).setData(orderedList);
		adaptor.setGenricAdapter(new IAdapter<NewSaleModel>() {

			@Override
			public void setItemView(NewSaleModel item, View view, int index) {
				TextView customer = (TextView) view
						.findViewById(R.id.txt_customer_name);
				TextView mobile = (TextView) view
						.findViewById(R.id.txt_customer_mobile);

				TextView Qty = (TextView) view.findViewById(R.id.txt_item);
				/*
				 * TextView amount = (TextView)
				 * view.findViewById(R.id.txt_amount);
				 */
				TextView scat = (TextView) view.findViewById(R.id.tbxscat);
				TextView date = (TextView) view.findViewById(R.id.tbxdate);
				ImageView img = (ImageView) view.findViewById(R.id.myimg);// Pending
																			// Image

				if (item.IsUpdated.equalsIgnoreCase("true")) {
					img.setVisibility(View.INVISIBLE);
				}

				customer.setText(item.CustomerName);
				mobile.setText("Mobile:" + item.MobileNo);
				// scat.setText(item.Cat1);
				scat.setVisibility(View.GONE);
				/* amount.setText(String.valueOf("Rs. "+item.TotalAmount)); */
				Qty.setText(item.ProductName + " : "
						+ String.valueOf(item.TotalQty));
				date.setText(item.ForDate);

			}

		});

		lv.setAdapter(adaptor);
		lv.setOnCreateContextMenuListener(this);
	}

	// --------------------Context menu ---of List item------------------//
	// -------------------------------------------------------------------//

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		this.context.getMenuInflater().inflate(R.menu.floatmenu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int id = item.getItemId();

		if (id == R.id.fdelete) {
			Delete(info);
			SetVisibilityOFUpdateButton();
			SetList();
			return true;

		} else if (id == R.id.fupdate) {
			Update(info);
			SetVisibilityOFUpdateButton();
			SetList();
			return true;

		} else {
			return super.onContextItemSelected(item);
		}
	}


	private void Update(AdapterContextMenuInfo info) {
		// TODO Auto-generated method stub
		NewSaleModel sale = orderedList.get(info.position);
		if (sale.IsUpdated.equalsIgnoreCase("false"))
			this.UpdatePendingSale(sale);
		else
			this.ShowToast("It is already Updated On Server");
		Log.v("Info:", info.toString());
	}

	private void Delete(AdapterContextMenuInfo info) {
		// TODO Auto-generated method stub

		NewSaleModel sale = orderedList.get(info.position);
		sale.Delete();
		this.ShowToast("Deleted sucessfully!");
		Log.v("Info:", info.toString());
	}

	// ------------------------option menu of setting------------------//
	// -----------------------------------------------------------------//

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.sale_display, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_update_all) {
			UpdateAllPendingSale();
			SetVisibilityOFUpdateButton();
			SetList();
			return true;

		} else if (id == R.id.action_clear_saved) {
			MainActivity.Database.CleanAllSavedSale();
			SetVisibilityOFUpdateButton();
			SetList();
			ShowToast("Operation Ok");
			return true;

		} else if (id == R.id.action_clear_all_pending) {
			MainActivity.Database.CleanAllPendingSale();
			SetVisibilityOFUpdateButton();
			SetList();
			ShowToast("Operation Ok");
			return true;

		} else if (id == R.id.action_clear_all) {
			MainActivity.Database.CleanAllSale();
			SetVisibilityOFUpdateButton();
			SetList();
			ShowToast("Operation Ok");
			return true;

		} else {
			return super.onOptionsItemSelected(item);
		}
	}


}
