package com.fieldforce.checkversion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.fieldforce.harmonkardonff.R;

import org.jsoup.Jsoup;

/**
 * Created by Himanshu on 7/23/2017.
 */

public class AppVersionController extends AsyncTask<Void, String, String> {

    Context mContext;
    float currentVersion;

    public AppVersionController(Context mContext) {
        this.mContext = mContext;
    }

    public void getCurrenAppVersion() {
        try {
            String temp =
                    mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            currentVersion = Float.valueOf(temp);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AppVersionController", "NameNotFoundException, " + e.getMessage());
        }
    }

    public void checkPlayStoreForUpdate() {
        execute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        String newVersion = null;
        try {
            getCurrenAppVersion();
            newVersion = Jsoup.connect(
                    "https://play.google.com/store/apps/details?id=" + mContext.getPackageName() + "&hl=it")
                    .timeout(30000)
                    .userAgent(
                            "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();
            return newVersion;
        } catch (Exception e) {
            return newVersion;
        }
    }

    @Override
    protected void onPostExecute(String onlineVersion) {
        super.onPostExecute(onlineVersion);
        if (onlineVersion != null && !onlineVersion.isEmpty()) {
            try {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    showUpdateAppDailog();
                }
            } catch (Exception e) {
            }
        }

        Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
    }

    private void showUpdateAppDailog() {
        new AlertDialog.Builder(mContext)
                .setTitle("Update App ?")
                .setMessage("Update Available, Want to update ?")
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        redirectToPlayStore();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      /*  Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(a);*/
                        dialog.dismiss();
                    }
                }).setIcon(R.drawable
                .arrowupwrd)
                .show();
    }

    private void redirectToPlayStore() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(
                Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()));
        mContext.startActivity(i);
    }
}
