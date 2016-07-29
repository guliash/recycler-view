package ru.yandex.yamblz.ui.recycler.helpers;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ColorsScrollListener extends RecyclerView.OnScrollListener {

    private static final long ROTATION_DURATION = 1000;
    private static final int ROTATE_VALUE = 360;

    private int mFirstVisible, mLastVisible;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;

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
            if(mFirstVisible != firstVisible) {
                for(int adapterPos = firstVisible; adapterPos < firstVisible + spanCount; adapterPos++) {
                    rotate(mRecyclerView.findViewHolderForLayoutPosition(adapterPos));
                }
            }
        } else {
            if(mLastVisible != lastVisible) {
                for(int adapterPos = lastVisible - spanCount + 1; adapterPos <= lastVisible; adapterPos++) {
                    rotate(mRecyclerView.findViewHolderForLayoutPosition(adapterPos));
                }
            }
        }
        setValues();
    }

    private void setValues() {
        mFirstVisible = mLayoutManager.findFirstVisibleItemPosition();
        mLastVisible = mLayoutManager.findLastVisibleItemPosition();
    }

    private void rotate(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder != null) {
            viewHolder.itemView.animate().rotationXBy(ROTATE_VALUE).setDuration(ROTATION_DURATION).start();
        }
    }
}
