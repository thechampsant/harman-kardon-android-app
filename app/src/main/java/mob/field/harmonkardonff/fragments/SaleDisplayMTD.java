package mob.field.harmonkardonff.fragments;



import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.Feedback;
import mob.field.harmonkardonff.entitiymodels.MTDSalesModel;
import mob.field.harmonkardonff.services.WebService;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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


public class SaleDisplayMTD extends IFragment {

	ArrayList<MTDSalesModel> Data = new ArrayList<MTDSalesModel>();
	WebService web = new WebService();
	Feedback model = new Feedback();

	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		return this.InflateView(R.layout.activity_sale_display_mtd, arg0, arg1);
	}

	@Override
	public void Activate(View arg0) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		this.hideKeyPad();
		TryRefreshList();
//		this.setOnDragUpdater(new IDragUpdater() {
//
//			@Override
//			public void onDrag() {
//				// TODO Auto-generated method stub
//				TryRefreshList();
//			}
//
//			@Override
//			public long setUpdateFrequency() {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//		});
	}

	private void TryRefreshList() {
		if (this.isNetworkFoundToast())
			loadMTDSaleFromServer();
		else
			loadMTDSaleFromLocal();
	}

	@SuppressWarnings("rawtypes")
	private void loadMTDSaleFromLocal() {
		// TODO Auto-generated method stub
		BackgroundProcess bp = new BackgroundProcess(this)
				.setHorizantalScreenProgress();
		bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				ProcessServerResponse((Response) arg0);
				ShowToast("Loaded from phone!");
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				Response response = new Response();
				response.data = MainActivity.Database.LoadMTDSalesFromDb();
				response.status = "true";
				return response;
			}
		});
		bp.execute(null, null, null);
	}

	@SuppressWarnings("rawtypes")
	public void loadMTDSaleFromServer() {

		// this.getListView(R.id.lv_sale_mtd).setVisibility(View.GONE);
		BackgroundProcess bp = new BackgroundProcess(this)
				.setHorizantalScreenProgress();

		bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				ProcessServerResponse((Response) arg0);
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				return web.TryGetMTDSale(model);
			}
		});
		bp.execute(null, null, null);

	}

	private boolean IsDataFound() {
		if (Data == null) {
			this.ShowToast("data not found!");
			return false;
		} else if (Data.Count() < 1) {
			this.ShowToast("data not found!");
			return false;
		} else
			return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void ProcessServerResponse(Response response) {
		if (response.status.equalsIgnoreCase("true")) {
			Data = response.data;
			if (!IsDataFound())
				return;
			setList();
			MainActivity.Database.SaveMTDSalesInDb(Data);
		} else {
			new Dialog(this.context).setTitle("Error").show(response.errormsg);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setList() {

		ListView lv = this.getListView(R.id.lv_sale_mtd);
		if (lv == null)
			return;
		lv.setVisibility(View.VISIBLE);
		setTgtVsAch();
		GenricAdapter adaptor = new GenricAdapter(this.context,
				R.layout.listitem4).setData(Data);
		adaptor.setGenricAdapter(new IAdapter<MTDSalesModel>() {

			@Override
			public void setItemView(MTDSalesModel item, View view, int index) {
				TextView Category = (TextView) view
						.findViewById(R.id.myproductname);
				ImageView image = (ImageView) view.findViewById(R.id.imgl);
				TextView Qty = (TextView) view.findViewById(R.id.myqty);
				Category.setText(item.Category);
				image.setVisibility(View.GONE);
				Qty.setText(item.Qty);
				
			}

			
		});
		lv.setAdapter(adaptor);
	}

	private void setTgtVsAch() {
		this.setVisibility(R.id.lyt_tgtvsach, View.VISIBLE);
		this.setVisibility(R.id.view_gap, View.VISIBLE);
		this.setVisibility(R.id.txt_msg, View.GONE);

		this.SetTextViewAsString(R.id.txt_mtd_sale, ToString(Data.Sum("Qty")));
		this.SetTextViewAsString(R.id.txt_mtd_tgt, Data.First().Tgt);
		this.SetTextViewAsString(R.id.txt_mtd_ach, Data.First().Ach + " %");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.sale_mtd, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_refresh_mtd:
			TryRefreshList();
			return true;
    
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
