package com.example.temporaryclass;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import app.core.base.InnosolsActivity;
import app.core.model.PlayDetailsModel;
import app.core.server.Decode;
import app.core.server.Server;

public class VersionHandler {

	String appDetailsUrl = "https://androidquery.appspot.com/api/market?app=";
	private String PackageName = null;
	private InnosolsActivity context;
	private double PlayVersion = 0.0;
	AlertDialog.Builder alertDialogBuilder = null;
	private PlayDetailsModel model = new PlayDetailsModel();
	private boolean NewVersion = false;

	public VersionHandler(InnosolsActivity context) {
		this.context = context;
		this.PackageName = context.getPackageName();
	}

	public void tryUpdateApp(boolean isforceupdate) {
		try {
			alertDialogBuilder = new AlertDialog.Builder(this.context);
			checkAppVersionOnGooglePlay(isforceupdate);
		} catch (Exception ex) {
		}

	}

	private void tryUpdateApplication(boolean isforceupdate) {
		try {
			if (NewVersion) {
				alertDialogBuilder.setTitle("New version is available!");
				showConfirmationDialog();

			} else {
				/*if (context.getCurrentVersion().equalsIgnoreCase("2.3")
						|| context.getCurrentVersion().equalsIgnoreCase("2.4")||context.getCurrentVersion().equalsIgnoreCase("2.5")) {
					Toast.makeText(context, "Running latest version! 2.3",
							Toast.LENGTH_SHORT).show();
				} else*/
					Toast.makeText(
							context,
							"Running latest version! "
									+ context.getCurrentVersion(),
							Toast.LENGTH_SHORT).show();
			}

			if (isforceupdate)
				showConfirmationDialog();
		} catch (Exception ex) {
		}
	}

	private void showConfirmationDialog() {
		alertDialogBuilder
				.setMessage("Click yes to install!")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {

								goToPlayStoreToDownloadApp();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void goToPlayStoreToDownloadApp() {

		try {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("market://details?id=" + PackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("https://play.google.com/store/apps/details?id="
							+ PackageName)));
		}
	}

	private boolean isNewVersion(String PlayVersion) {
		this.PlayVersion = context.ToDouble(PlayVersion);
		if (this.PlayVersion > context.ToDouble(context.getCurrentVersion()))
			return true;
		else
			return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void checkAppVersionOnGooglePlay(final boolean isforceupdate) {
		new AsyncTask() {

			@Override
			protected void onPostExecute(Object result) {
				tryUpdateApplication(isforceupdate);
				super.onPostExecute(result);
			}

			@Override
			protected Object doInBackground(Object... params) {

				try {
					if (!context.isNetworkAvailable())
						return null;
					JSONObject Jobj = (JSONObject) new Server()
							.getServerResponse(appDetailsUrl + PackageName)
							.get(0);
					model = (PlayDetailsModel) new Decode()
							.ToClass(model, Jobj);
					if (model != null && model.version != null)
						NewVersion = isNewVersion(model.version);

				} catch (Exception ex) {
					String newVersion;
					try {
						newVersion="";
					/*newVersion = Jsoup
							.connect(
									"https://play.google.com/store/apps/details?id="
											+ PackageName + "&hl=en")
							.timeout(30000)
							.userAgent(
									"Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
							.referrer("http://www.google.com").get()
							.select("div[itemprop=softwareVersion]").first().ownText();*/
					} catch (Exception e) {

						e.printStackTrace();
						return false;
					}
					NewVersion = isNewVersion(newVersion);
				}
				return null;
			}
		}.execute("");
	}

}
