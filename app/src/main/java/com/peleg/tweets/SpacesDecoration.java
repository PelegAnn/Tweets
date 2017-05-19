package com.peleg.tweets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hannypeleg on 5/18/17.
 */

public class SpacesDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public SpacesDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = mSpace;
        outRect.bottom = mSpace;
        //outRect.right = mSpace;


        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1)
            outRect.top = mSpace;
    }
}
