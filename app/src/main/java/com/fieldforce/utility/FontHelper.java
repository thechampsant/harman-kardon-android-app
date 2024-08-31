package com.fieldforce.utility;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

public final class FontHelper {
    public static void setTypeFace(TextView textView, AssetManager assetManager, String path) {
        Typeface face = Typeface.createFromAsset(assetManager,
                path);
        textView.setTypeface(face);
    }


}
