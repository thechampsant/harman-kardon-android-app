package mob.field.harmonkardonff.fragments;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.base.IFragment;

import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;

public class HeroCTNs  extends IFragment{



	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		return this.InflateView(R.layout.activity_ctn_display, arg0,arg1);
	}
	
	@Override
	public void Activate(View arg0) {
		// TODO Auto-generated method stub
		setList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setList() {
		// TODO Auto-generated method stub
		ListView lv = this.getListView(R.id.lv_ctn);
		ArrayList<ProductModel> data = MainActivity.MyProductList.Skip(15);
		if (lv == null)
			return;
		lv.setVisibility(View.VISIBLE);
		if (data.Count() < 1) {
			this.setVisibility(R.id.txt_msg, View.VISIBLE);
			return;
		} else
			this.setVisibility(R.id.txt_msg, View.GONE);

		GenricAdapter adaptor = new GenricAdapter(this.context,
				R.layout.listitem4).setData(data);
		adaptor.setGenricAdapter(new IAdapter<ProductModel>() {

			@Override
			public void setItemView(ProductModel item, View view, int index) {

				TextView Category = (TextView) view
						.findViewById(R.id.myproductname);
				ImageView image = (ImageView) view.findViewById(R.id.imgl);
				TextView Qty = (TextView) view.findViewById(R.id.myqty);
				Category.setText(item.Name);
				image.setVisibility(View.GONE);
				Qty.setVisibility(View.GONE);
				
			}

			
		});
		lv.setAdapter(adaptor);
	}


}
