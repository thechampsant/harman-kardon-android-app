package com.ariston.training_module.utility;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class HorizontalItemDecorator extends RecyclerView.ItemDecoration {

    private int horizontalSpacing;

    public HorizontalItemDecorator(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = horizontalSpacing;
        outRect.right = horizontalSpacing;
        outRect.top = horizontalSpacing;
        outRect.bottom = horizontalSpacing;
    }
}
