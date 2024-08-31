package app.core.services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ProductUpdater {

	private String WebUrl;
	private int PazeSize = 100;
	private Context context;

	public ProductUpdater(Context context) {
		this.context = context;
	}

	public ProductUpdater(Context context, String Url) {
		this.context = context;
		this.WebUrl = Url;
	}

	public ProductUpdater setUpdateService(Context context, String Url,
			int PazeSize) {
		this.context = context;
		this.WebUrl = Url;
		this.PazeSize = PazeSize;
		return this;
	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateProducts() {
		try {
			showToast("loading products from server..");
			new AsyncTask() {

				@Override
				protected Object doInBackground(Object... params) {

					return null;
				}
			}.execute("");

		} catch (Exception ex) {
			showToast(ex.getMessage());
		}

	}

}
