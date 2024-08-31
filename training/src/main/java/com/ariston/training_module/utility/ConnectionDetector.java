package com.ariston.training_module.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ariston.training_module.R;



/*
import com.example.himanshudubey.shortflix.ShortFlix.Login_Activity;
import com.example.himanshudubey.shortflix.ShortFlix.MyProfile;

DEVELOPED BY MANISH

*/

public class ConnectionDetector extends Activity
{
    public final static int TYPE_WIFI = 1;
    public final static int TYPE_MOBILE = 2;
    public final static int TYPE_NOT_CONNECTED = 0;
    private Context _context;

    public ConnectionDetector(Context context){
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            Log.e("ConnectionDetector","ERROR");
            return true;
        }
        else {
            Log.e("ConnectionDetector","mkemwk");
            return false;
        }
      /*  ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;*/
    }


    public int getConnectivityStatus() {

        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork && activeNetwork.isConnected()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }



    public void showNoInternetAlret(){

        final AlertDialog internet_dialog = new AlertDialog.Builder(
                _context).create();

        // Setting Dialog Title
        internet_dialog.setTitle(getString(R.string.No_Internet));

        // Setting Dialog Message
        internet_dialog.setMessage(getString(R.string.intenet));

       // Setting OK Button
        internet_dialog.setButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                internet_dialog.dismiss();
                _context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });

        // Showing Alert Message
        internet_dialog.show();
    }

    public void showCustomDialog(String title, String msg) {
        final AlertDialog internet_dialog = new AlertDialog.Builder(
                _context).create();
        // Setting Dialog Title
        internet_dialog.setTitle(title);
        // Setting Dialog Message
        internet_dialog.setMessage(msg);
        internet_dialog.setCancelable(false);
        // Setting OK Button
        internet_dialog.setButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        internet_dialog.show();
    }



}
