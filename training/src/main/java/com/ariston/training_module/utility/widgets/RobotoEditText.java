package com.ariston.training_module.utility.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.ariston.training_module.utility.FontHelper;

public class RobotoEditText extends EditText {
    public RobotoEditText(Context context) {
        super(context);
        init();
    }

    public RobotoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init() {
        FontHelper.setTypeFace(this, getContext().getAssets(), "fonts/rob_med.ttf");
    }

}
