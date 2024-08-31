package com.fieldforce.harmonkardonff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * Bar Code Scanner Activity For Scanning Bar Codes
 * <p>
 * You can find list of Bar Codes supported at {@link com.google.zxing.BarcodeFormat}
 * <example>
 * <code>
 * Intent i = new Intent(MainActivity.this, BarcodeScannerActivity.class);
 * startActivityForResult(i, BarcodeScannerActivity.REQUEST_CODE);
 * </code>
 * <p>
 * How to get Results Back
 * <p>
 * <code>
 * protected void onActivityResult(int requestCode, int resultCode, Intent data)
 * {
 * super.onActivityResult(requestCode, resultCode, data);
 * switch (requestCode)
 * {
 * case BarcodeScannerActivity.REQUEST_CODE:
 * if (resultCode == Activity.RESULT_OK)
 * {
 * switch (data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_FORMAT))
 * {
 * case "CODE_128":
 * String scannedCode = data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_DATA);
 * //Correct Code
 * break;
 * default:
 * new Dialog(MainActivity.this).show("Invalid Bar Code Scanned");
 * }
 * }
 * else if (resultCode == Activity.RESULT_CANCELED)
 * new Dialog(MainActivity.this).show("Bar Code Scanning Cancelled");
 * break;
 * }
 * }
 * </code>
 * </example>
 *
 * @author Himanshu
 */
public class BarcodeScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    /**
     * Request Code for Obtaining Results Back
     */
    public final static int REQUEST_CODE = 89;

    /**
     * Intent Keys For Getting
     */
    public final static String SCAN_RESULT_FORMAT = "SCAN_RESULT_FORMAT";
    public final static String SCAN_RESULT_DATA = "SCAN_RESULT_DATA";
    public final static String DESIRED_SCAN_CODES = "DESIRED_SCAN_CODES";

    private ZXingScannerView mScannerView;

    private boolean scanSuccessful = false;

    private String scanFormat = "";
    private String scanData = "";

    public static boolean isActive(Activity activity) {
        if (activity != null)
            try {
                return activity.getWindow().getDecorView().isShown();
            } catch (Exception e) {
                return false;
            }
        return false;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                       // Set the scanner view as the content view
        setTimerFor15Secs();
    }

    private void setTimerFor15Secs() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    if (isActive(BarcodeScannerActivity.this)) {
                        if (!scanSuccessful)
                            showBarCodeDialog();
                        }
                    }
                catch (Exception e) {}

            }
        }, 10000);
    }

    private void showBarCodeDialog() {

        final EditText barCodeEditText = new EditText(this);
        barCodeEditText.setPadding(15, 50, 15, 30);
        barCodeEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(this)
                .setTitle("Enter the Bar Code You were Scanning")
                .setView(barCodeEditText)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String barCode = barCodeEditText.getText().toString();

                        if (!barCode.equals("")) {
                            scanSuccessful = true;
                            LocalStorage localStorage=new LocalStorage(BarcodeScannerActivity.this);
                            localStorage.setMessage("IsManual","true");
                            scanFormat = "CODE_128";
                            scanData = barCode;

                            onBackPressed();
                        } else {
                            Toast.makeText(BarcodeScannerActivity.this, "Invalid Bar Code Entered", Toast.LENGTH_SHORT).show();
                            showBarCodeDialog();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        scanSuccessful = true;

        scanFormat = rawResult.getBarcodeFormat().toString();
        scanData = rawResult.getText();
        Log.e("scanData",scanData);

        onBackPressed();

    }


    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();

        if (scanSuccessful) {
            backIntent.putExtra(SCAN_RESULT_FORMAT, scanFormat);
            backIntent.putExtra(SCAN_RESULT_DATA, scanData);
            setResult(Activity.RESULT_OK, backIntent);
        } else {
            backIntent.putExtra(SCAN_RESULT_FORMAT, "");
            backIntent.putExtra(SCAN_RESULT_DATA, "");
            setResult(Activity.RESULT_CANCELED, backIntent);
        }
        finish();
        super.onBackPressed();
    }
}
