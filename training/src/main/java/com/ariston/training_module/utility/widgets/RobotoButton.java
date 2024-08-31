package com.ariston.training_module.utility.widgets;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;

import com.ariston.training_module.utility.FontHelper;

public class RobotoButton extends Button {

    public RobotoButton(Context context) {
        super(context);
        init();
    }

    public RobotoButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        FontHelper.setTypeFace(this, getContext().getAssets(), "fonts/rob_bold.ttf");
    }
}
