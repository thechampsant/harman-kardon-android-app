package mob.field.harmonkardonff.fragments;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.Feedback;
import mob.field.harmonkardonff.entitiymodels.MTDSalesModel;
import mob.field.harmonkardonff.services.WebService;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;

import com.fieldforce.harmonkardonff.R;

public class ViewStock extends IFragment {
	WebService web;
	ArrayList<Feedback> Data = new ArrayList<Feedback>();

	@Override
	public void Activate(View FragmentView) {
		web = new WebService();
		hitapi();

	}

	private void hitapi() {
		if (isNetworkFoundToast())
			loadstockfromServer();
	}

	private void loadstockfromServer() {
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
				return web.TryGetLMTDSale(new Feedback());
			}
		});

	}

	protected void ProcessServerResponse(Response response) {
		if (response.status.equalsIgnoreCase("true")) {
			Data = response.data;
			if (!IsDataFound())
				return;
			setList();
		} else {
			new Dialog(this.context).setTitle("Error").show(response.errormsg);
		}
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

	private void setList() {

		ListView lv = this.getListView(R.id.lv_sale_mtd);
		if (lv == null)
			return;
		lv.setVisibility(View.VISIBLE);
		GenricAdapter adaptor = new GenricAdapter(this.context,
				R.layout.listitem4).setData(Data);
		adaptor.setGenricAdapter(new IAdapter<MTDSalesModel>() {

			@Override
			public void setItemView(MTDSalesModel item, View view, int index) {
//				TextView Category = (TextView) view
//						.findViewById(R.id.myproductname);
//				ImageView image = (ImageView) view.findViewById(R.id.imgl);
//				TextView Qty = (TextView) view.findViewById(R.id.myqty);
//				Category.setText(item.Category);
//				image.setVisibility(View.GONE);
//				Qty.setText(item.Qty);
//				
			}

			
		});
		lv.setAdapter(adaptor);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return this.InflateView(R.layout.activity_stock_display, inflater,
				container);
	}

}
