package app.core.utils;

import android.app.Activity;
import android.app.AlertDialog;

public class Dialog {


	private Activity context = null;
	AlertDialog.Builder dialog = null;



	public Dialog(Activity _Context) {
		context = _Context;
		dialog = new AlertDialog.Builder(this.context);

	}

	public Dialog setMessage(String msg) {
		this.Intit(msg);
		return this;
	}

	public Dialog show(String msg) {
		if (!((Activity) context).isFinishing()) {
			if (msg == null)
				return this;
			if (msg.trim().length() < 1)
				return this;
			this.setMessage(msg);
			this.dialog.show();
		}
		return this;
	}

	public Dialog setTitle(String title) {
		this.dialog.setTitle(title);
		return this;
	}

	public Dialog show() {
		if (!((Activity) context).isFinishing()) {
			this.dialog.show();
		
		}
		return this;
	}

	private Dialog Intit(String message) {
		dialog.setNegativeButton("Ok", null);
		dialog.setMessage(message);
		return this;
	}
}
