package com.ariston.training_module.utility;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class VerticalItemDecorator extends RecyclerView.ItemDecoration {

    private final int verticalSpace;

    public VerticalItemDecorator(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = verticalSpace;
        outRect.right = verticalSpace;
        outRect.left = verticalSpace;
    }
}
