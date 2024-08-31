package com.fieldforce.utility.widgets;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.fieldforce.utility.FontHelper;

public class RobotoTextView extends androidx.appcompat.widget.AppCompatTextView {


    public RobotoTextView(Context context) {
        super(context);
        init();
    }

    public RobotoTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    public void init() {
        FontHelper.setTypeFace(this, getContext().getAssets(), "fonts/rob_med.ttf");
    }


}
