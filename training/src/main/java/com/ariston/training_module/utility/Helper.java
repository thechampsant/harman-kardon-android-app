package com.ariston.training_module.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.content.res.AppCompatResources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Helper {

    public static float getSizeInDp(Context ctx, int dip) {
        Resources r = ctx.getResources();
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
    }

    public static int getViewHeight(View view) {
        WindowManager wm =
                (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int deviceWidth;
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return view.getMeasuredHeight(); //        view.getMeasuredWidth();
    }

    public static int getScreenHeight(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;

    }

    public static int getScreenWidth(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;

    }

    public static void openNextActivity(Activity context, Class clzz, Bundle bundle, boolean forResult, boolean finishCurrent, int reqCode) {
        Intent intent = new Intent(context, clzz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (forResult) {
            context.startActivityForResult(intent, reqCode);
        } else {
            context.startActivity(intent);
        }
        if (finishCurrent) {
            context.finish();
        }


    }

    public static void setDrawableEditText(EditText textView, int vectorId, int gravity) {
        Drawable drawable = AppCompatResources.getDrawable(
                textView.getContext(),
                vectorId);
        switch (gravity) {
            case 0:
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
                break;
            case 1:
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
                break;
        }
    }


    public static void setDrawableButton(Button textView, int vectorId, int gravity) {
        Drawable drawable = AppCompatResources.getDrawable(
                textView.getContext(),
                vectorId);
        switch (gravity) {
            case 0:
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
                break;
            case 1:
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
                break;
        }
    }

    public static void setDrawableTextView(TextView textView, int vectorId, int gravity) {
        Drawable drawable = AppCompatResources.getDrawable(
                textView.getContext(),
                vectorId);
        switch (gravity) {
            case 0:
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
                break;
            case 1:
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
                break;
        }
    }


    public static void print(Object o){

        Log.e("_____Harman____","value = "+o);

    }

    public static void showMsg(Context context,String o){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Message");
        builder1.setMessage(o);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK, Got it",
                (dialog, id) -> dialog.cancel());



        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}