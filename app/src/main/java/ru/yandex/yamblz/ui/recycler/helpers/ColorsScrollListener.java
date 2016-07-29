package ru.yandex.yamblz.ui.recycler.helpers;

import android.animation.ObjectAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Handles scrolling animation
 */
public class ColorsScrollListener extends RecyclerView.OnScrollListener {

    private static final long ROTATION_DURATION = 1000;
    private static final int ROTATE_VALUE = 360;

    /**
     * First visible item
     */
    private int mFirstVisible;

    /**
     * Last visible item
     */
    private int mLastVisible;

    private final RecyclerView mRecyclerView;
    private final GridLayoutManager mLayoutManager;

    public ColorsScrollListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
        setValues();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        final int firstVisible = mLayoutManager.findFirstVisibleItemPosition();
        final int lastVisible = mLayoutManager.findLastVisibleItemPosition();
        final int spanCount = mLayoutManager.getSpanCount();

        if(dy < 0) {
            //if scrolling up
            if(mFirstVisible != firstVisible) {
                //if new items appeared, then animate them
                for(int adapterPos = firstVisible; adapterPos < firstVisible + spanCount; adapterPos++) {
                    rotate(mRecyclerView.findViewHolderForLayoutPosition(adapterPos));
                }
            }
        } else {
            //if scrolling down
            if(mLastVisible != lastVisible) {
                //if new items appeared then animate them
                for(int adapterPos = lastVisible - spanCount + 1; adapterPos <= lastVisible; adapterPos++) {
                    rotate(mRecyclerView.findViewHolderForLayoutPosition(adapterPos));
                }
            }
        }
        setValues();
    }

    /**
     * Sets the border items' positions
     */
    private void setValues() {
        mFirstVisible = mLayoutManager.findFirstVisibleItemPosition();
        mLastVisible = mLayoutManager.findLastVisibleItemPosition();
    }

    /**
     * Rotates itemView
     * @param viewHolder the holder
     */
    private void rotate(RecyclerView.ViewHolder viewHolder) {
        ObjectAnimator.ofFloat(viewHolder.itemView, View.ROTATION_X,
                0, ROTATE_VALUE).setDuration(ROTATION_DURATION).start();
    }
}
