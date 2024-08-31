package com.fieldforce.utility.widgets;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.fieldforce.utility.FontHelper;

public class RobotoBoldTextView extends androidx.appcompat.widget.AppCompatTextView {
    public RobotoBoldTextView(Context context) {
        super(context);
        init();
    }

    public RobotoBoldTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoBoldTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        FontHelper.setTypeFace(this, getContext().getAssets(), "fonts/rob_bold.ttf");
    }

}
