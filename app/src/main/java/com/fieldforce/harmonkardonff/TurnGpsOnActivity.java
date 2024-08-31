package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class TurnGpsOnActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turngps);
		initialise();
	}

	private void initialise() {
		showSettingsAlert();
		// TODO Auto-generated method stub
		
	}
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle("Location Settings");

		alertDialog.setCancelable(false);
		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to location settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					
						TurnGpsOnActivity.this.startActivity(intent);
						finish();
				
						
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				});

		// Showing Alert Message
		alertDialog.show();

	}
	
	}

	
	