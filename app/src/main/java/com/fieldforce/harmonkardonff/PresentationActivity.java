package com.fieldforce.harmonkardonff;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.DocumentModel;
import mob.field.harmonkardonff.services.WebService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.model.Response;

public class PresentationActivity extends InnosolsActivity {

	private ListView listvw;
	WebService webapi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presentation);
		initialise();
		hitapi();
	}

	private void initialise() {
		listvw = (ListView) findViewById(R.id.list_vw);
		webapi = new WebService();

	}

	private void hitapi() {
		if (!isNetworkFoundDialog())
			return;
		BackgroundProcess bp = new BackgroundProcess(this)
				.setbackgroundProcess(new IProcess() {

					@Override
					public Object underProcess() throws Exception {
						// TODO Auto-generated method stub
						return webapi.GetPresentation();
					}

					@Override
					public void processResponse(Object respo) throws Exception {
						Response resp = (Response) respo;
						if (resp != null) {
							{
								if (resp.isSuccess()) {
									setlist(resp.data);

								} else {
									ShowToast(resp.errormsg);
								}
							}
						}

					}
				});
		bp.execute();

	}

	protected void setlist(ArrayList data) {
		final ArrayList<DocumentModel> array_doc = data;
		if (array_doc == null) {
			ShowToast("No data Found");
			return;
		}
		GenricAdapter adaptor = new GenricAdapter(this,
				R.layout.listitem_document).setData(array_doc);
		adaptor.setGenricAdapter(new IAdapter<DocumentModel>() {

			@Override
			public void setItemView(DocumentModel item, View v, int index) {
				TextView docname = (TextView) v.findViewById(R.id.name);
				if (!IsNullOrWhiteSpace(item.DocName)) {
					
					docname.setText(item.DocName);

				}

			}

		});

		listvw.setAdapter(adaptor);
		listvw.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DocumentModel model = array_doc.get(position);
				String docurl=model.DocURL;
				Intent i=new Intent(PresentationActivity.this,PresentationView.class);
				i.putExtra("documenturl",docurl);
				startActivity(i);
			}
		});

	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

}
