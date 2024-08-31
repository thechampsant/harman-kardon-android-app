package app.core.utils;



import java.io.File;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.model.Response;
import app.core.server.FileDownloader;

public class VersionHandler {

	AlertDialog.Builder alertDialogBuilder = null;
	protected Activity BaseActivity;
	// private IScreenManager _screen=null;
	private Intent NextIntent = null;
	private String FileUrl;
	private String _ServerVersion = null;

	public VersionHandler(Activity activity) {
		BaseActivity = activity;
	}

	public void TryUpdateApp(String _ServerVersion, String ApkUrl) {
		this._ServerVersion = _ServerVersion;
		this.FileUrl = ApkUrl;
		alertDialogBuilder = new AlertDialog.Builder(this.BaseActivity);
		TryUpdateApplication();
	}

	public void ForceUpdateApp(String _ServerVersion, String ApkUrl) {
		this._ServerVersion = _ServerVersion;
		this.FileUrl = ApkUrl;
		alertDialogBuilder = new AlertDialog.Builder(this.BaseActivity);
		TryForceUpdateApplication();
	}

	private String GetFileName() {
		try {
			String[] t = FileUrl.split("/");
			return t[t.length - 1];
		} catch (Exception ex) {
			Toast.makeText(BaseActivity.getApplicationContext(),
					"unable to find file name!", Toast.LENGTH_LONG).show();
			return null;
		}
	}

	public void SetNavigateTo(Intent I) {
		this.NextIntent = I;
	}

	/*
	 * public void SetNavigateTo(IScreenManager _screen, Object arg0) {
	 * this._screen=_screen; this.arg0=arg0; }
	 */
	private void GoToFromIntent() {
		if (this.NextIntent == null)
			BaseActivity.startActivity(this.NextIntent);
		else
			Toast.makeText(BaseActivity.getApplicationContext(),
					"unable to find next screen!", Toast.LENGTH_LONG).show();
	}

	/*
	 * private void GoToFromScreen() { InnosolsActivity
	 * a=(InnosolsActivity)BaseActivity; if(_screen!=null) a.NavigateTo(_screen,
	 * arg0); else Toast.makeText(BaseActivity.getApplicationContext(),
	 * "unable to find next screen!", Toast.LENGTH_LONG).show(); }
	 */

	private void TryUpdateApplication() {
		if (IsNewVersion()) {
			alertDialogBuilder
					.setTitle("New version is available!Please install!");
			this.showdialog();
		} else {
			Toast.makeText(BaseActivity.getApplicationContext(),
					"Running latest version! " + getCurrentVersion(),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void TryForceUpdateApplication() {
		if (IsNewVersion()) {
			alertDialogBuilder
					.setTitle("New version is available!Please install!");
			this.showdialog();
		} else {
			alertDialogBuilder.setTitle("Running latest version! "
					+ getCurrentVersion() + "\n" + "want to update?");
			this.showdialog();
		}
	}

	private boolean IsNewVersion() {
		if (getserverversion() > getCurrentVersion())
			return true;
		else
			return false;
	}

	private double getserverversion() {
		if (_ServerVersion == null)
			return 0.0;
		else {
			try {
				double version = Double.parseDouble(_ServerVersion);
				return version;
			} catch (Exception ex) {
				Toast.makeText(BaseActivity.getApplicationContext(),
						"Version resolver failed!", Toast.LENGTH_LONG).show();
				return 0.0;
			}
		}
	}

	private double getCurrentVersion() {
		String version = "0.0";
		try {
			version = this.BaseActivity.getPackageManager().getPackageInfo(
					BaseActivity.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Double.parseDouble(version);
	}

	private void showdialog() {
		alertDialogBuilder
				.setMessage("Click yes to install!")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								// download app from server
								updateapp();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
						if (NextIntent != null)
							GoToFromIntent();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	private void showMessage(String msg) {
		Toast.makeText(BaseActivity.getApplicationContext(), msg,
				Toast.LENGTH_LONG).show();
	}

	private FileDownloader downloader = new FileDownloader();

	private String LocalPath = "";

	private void updateapp() {
		if (FileUrl == null) {
			showMessage("Unable to locate file url");
			return;
		}
		// URL url = new URL(this.FileUrl);
		final String PATH = Environment.getExternalStorageDirectory()
				+ "/Download/";
		LocalPath = PATH + GetFileName();
		File file = new File(PATH);
		if (file.exists() == false) {
			file.mkdirs();
		}
		final BackgroundProcess bp = new BackgroundProcess(BaseActivity);
		bp.showMessage(false, false);
		bp.setProgressDialog(true);
		bp.showProgressType(true);

		bp.setbackgroundProcess(new IProcess() {

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				return downloader.downloadFile(FileUrl, LocalPath, bp);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void processResponse(Object response) throws Exception {
				// TODO Auto-generated method stub
				ProcessResponse((Response) response);
			}
		});
		
		bp.execute(null,null,null);
	}

	@SuppressWarnings("rawtypes")
	private void ProcessResponse(Response response) {
		if (response.status.equalsIgnoreCase("true")) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(LocalPath)),
					"application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			BaseActivity.startActivity(intent);
		} else {
			new Dialog(BaseActivity).setTitle("Unable to download file!").show(
					response.errormsg);
		}
	}
}
