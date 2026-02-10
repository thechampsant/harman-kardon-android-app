package mob.field.harmonkardonff.fragments;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.StocksModel;
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

public class StockDisplay extends IFragment{

	WebService server = new WebService();
	ArrayList<StocksModel> Data = new ArrayList<StocksModel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return this.InflateView(R.layout.activity_stock_display, inflater,
				container);
	}

	@Override
	public void Activate(View arg0) {
		attachUpdateStockButton();
		SetVisibilityOFUpdateButton();
		SetList();
		setHasOptionsMenu(true);
		this.hideKeyPad();
	}

	// Check if any stock is Pending in Local DB
	private boolean IsAnyPending() {
		return MainActivity.MyStocks.Any("IsUpdated", "false");
	}

	private void RefreshMySales() {
		Data = MainActivity.Database.LoadStockFromLocalDb();
	}

	// Fetch stock From LOCAL DB
	private void SetVisibilityOFUpdateButton() {
		RefreshMySales();
		UpdateShowUpdateButton(IsAnyPending());
	}

	@SuppressWarnings("rawtypes")
	private void ProcessSubmitStockResponse(Response response) {
		if (response.isSuccess()) {
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

	private void attachUpdateStockButton() {
		this.SetOnClickListenerOnButton(R.id.btn_update_pending_stock,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						UpdateAllPendingStock();
					}
				});
	}

	@SuppressWarnings("rawtypes")
	public void UpdateAllPendingStock() {
		if (!IsAnyPending()) {
			ShowToast("No stock found to update!");
			return;
		}
		if (!isNetworkFoundDialog())
			return;
		BackgroundProcess bp = new BackgroundProcess(this.context)
				.setProgressMessage("sending to server...");
		bp.setbackgroundProcess(new IProcess() {

			
			@Override
			public void processResponse(Object arg0) throws Exception {
				ProcessSubmitStockResponse((Response) arg0);
			}

			@Override
			public Object underProcess() throws Exception {
				Response response = null;
				for (StocksModel model : MainActivity.MyStocks.where(
						"IsUpdated", "false")) {
					response = server.TryUpdateStock(model);
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

	public void UpdatePendingStock(final StocksModel model) {
		if (!this.isNetworkFoundDialog())
			return;
		BackgroundProcess bp = new BackgroundProcess(this.context)
				.setProgressMessage("sending to server...");
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings("rawtypes")
			@Override
			public void processResponse(Object arg0) throws Exception {
				ProcessSubmitStockResponse((Response) arg0);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object underProcess() throws Exception {
				Response response = null;
				response = server.TryUpdateStock(model);
				if (response.isSuccess()) {
					model.IsUpdated = "true";
					model.InsertOrUpdate();
				}
				return response;
			}
		});
		bp.execute(null, null, null);
	}

	private void UpdateShowUpdateButton(boolean IsPending) {
		if (MainActivity.MyStocks.Count() < 1) {
			setVisibility(R.id.cardUpdateStock, View.GONE);
			GetTextView(R.id.txt_msg).setVisibility(View.VISIBLE);
			SetTextViewAsString(R.id.txt_msg, "No stock record!");
		} else {
			if (IsPending) {
				this.setVisibility(R.id.cardUpdateStock, View.VISIBLE);
				this.GetTextView(R.id.txt_msg).setVisibility(View.GONE);
			} else {
				this.setVisibility(R.id.cardUpdateStock, View.GONE);
				this.GetTextView(R.id.txt_msg).setVisibility(View.GONE);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void SetList() {

		ListView lv = (ListView) this.findViewById(R.id.lv_stocklist);
		lv.setDividerHeight(0);
		ArrayList<StocksModel> orderedList = MainActivity.MyStocks.Reverse();
		GenricAdapter adaptor = new GenricAdapter(this.context,
				R.layout.listitem_stock).setData(orderedList);
		
		
		adaptor.setGenricAdapter(new IAdapter<StocksModel>() {

			@Override
			public void setItemView(StocksModel item, View view, int index) {

				TextView StockFor = (TextView) view
						.findViewById(R.id.myproductname); // Date
				TextView Qty = (TextView) view.findViewById(R.id.myqty); // Count
				TextView mcat = (TextView) view.findViewById(R.id.tbxmcat); // mcat
				TextView scat = (TextView) view.findViewById(R.id.tbxscat); // scat
				TextView date = (TextView) view.findViewById(R.id.tbxdate); // date----tbxdate
				ImageView img = (ImageView) view.findViewById(R.id.myimg);// Pending
																			// Image

				if (item.IsUpdated.equalsIgnoreCase("true")) {
					img.setVisibility(View.INVISIBLE);
				}

				
				StockFor.setText(item.ProductName);
				mcat.setText(item.Cat1);
				//scat.setText(item.Cat2);
				//mcat.setVisibility(View.INVISIBLE);
				scat.setVisibility(View.GONE);
				date.setText(item.ForDate);

				Qty.setText("QTY :"+Integer.toString(item.Qty));
				//Qty.setVisibility(View.GONE);
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
		StocksModel stock = Data.get(info.position);
		if (stock.IsUpdated.equalsIgnoreCase("false"))
			this.UpdatePendingStock(stock);
		else
			this.ShowToast("It is already Updated On Server");
		Log.v("Info:", info.toString());
	}

	private void Delete(AdapterContextMenuInfo info) {
		StocksModel stock = Data.get(info.position);
		stock.Delete();
		this.ShowToast("Deleted sucessfully!");
		Log.v("Info:", info.toString());
	}

	// ------------------------option menu of setting------------------//
	// -----------------------------------------------------------------//

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.stock_display, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_update_all) {
			UpdateAllPendingStock();
			SetVisibilityOFUpdateButton();
			SetList();
			return true;

		} else if (id == R.id.action_clear_saved) {
			MainActivity.Database.CleanAllSavedStock();
			SetVisibilityOFUpdateButton();
			SetList();
			ShowToast("Operation Ok");
			return true;

		} else if (id == R.id.action_clear_all_pending) {
			MainActivity.Database.CleanAllPendingStock();
			SetVisibilityOFUpdateButton();
			SetList();
			ShowToast("Operation Ok");
			return true;

		} else if (id == R.id.action_clear_all) {
			MainActivity.Database.CleanAllStock();
			SetVisibilityOFUpdateButton();
			SetList();
			ShowToast("Operation Ok");
			return true;

		} else {
			return super.onOptionsItemSelected(item);
		}
	}

}
