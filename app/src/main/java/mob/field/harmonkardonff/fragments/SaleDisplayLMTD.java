package mob.field.harmonkardonff.fragments;


import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.Feedback;
import mob.field.harmonkardonff.entitiymodels.LMTDSalesModel;
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


public class SaleDisplayLMTD extends IFragment {

	ArrayList<LMTDSalesModel> Data = new ArrayList<LMTDSalesModel>();
	WebService web = new WebService();
	Feedback model = new Feedback();

	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		return this.InflateView(R.layout.activity_sale_display_lmtd, arg0, arg1);
	}
	
	@Override
	public void Activate(View arg0) {
		setHasOptionsMenu(true);
		this.hideKeyPad();
		TryRefreshList();
	}
	private void TryRefreshList() {
		if (isNetworkFoundToast())
			loadLMTDSaleFromServer();
		else
			loadLMTDSaleFromLocal();
	}

	@SuppressWarnings("rawtypes")
	private void loadLMTDSaleFromLocal() {
		BackgroundProcess bp = new BackgroundProcess(this)
				.setHorizantalScreenProgress();
		bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				ProcessServerResponse((Response) arg0);
				ShowToast("Loaded from phone!");
			}

			@SuppressWarnings("unchecked")
			@Override
			public Object underProcess() throws Exception {
				Response response = new Response();
				response.data = MainActivity.Database.LoadLMTDSalesFromDb();
				response.status = "true";
				return response;
			}
		});
		bp.execute(null, null, null);
	}

	@SuppressWarnings("rawtypes")
	public void loadLMTDSaleFromServer() {

		BackgroundProcess bp = new BackgroundProcess(this)
				.setHorizantalScreenProgress();

		bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				ProcessServerResponse((Response) arg0);
			}

			@Override
			public Object underProcess() throws Exception {
				return web.TryGetLMTDSale(model);
			}
		});
		bp.execute(null, null, null);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void ProcessServerResponse(Response response) {
		if (response.isSuccess() && response.isDataFound()) {
			Data = response.data;
			setList();
			MainActivity.Database.SaveLMTDSalesInDb(Data);
		} else {
			new Dialog(this.context).setTitle("Error").show(response.getMessage());
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
		adaptor.setGenricAdapter(new IAdapter<LMTDSalesModel>() {

			@Override
			public void setItemView(LMTDSalesModel item, View view, int index) {
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
