package ru.yandex.yamblz.ui.recycler.manager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * {@link GridLayoutManager} customizable with extra layout space for smooth scrolling
 */
public class PreCachingGridLayoutManager extends GridLayoutManager {

    private static final int DEFAULT_EXTRA_LAYOUT_SPACE = 300;

    private int mExtraLayoutSpace = -1;


    public PreCachingGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreCachingGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public PreCachingGridLayoutManager(Context context, int spanCount, int extraLayoutSpace) {
        super(context, spanCount);
        setExtraLayoutSpace(extraLayoutSpace);
    }

    public PreCachingGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    /**
     * Sets extra layout space in pixels.
     * {@link PreCachingGridLayoutManager#getExtraLayoutSpace(RecyclerView.State)}
     * @param extraLayoutSpace extra layout space to set (in pixels)
     */
    public void setExtraLayoutSpace(int extraLayoutSpace) {
        if(extraLayoutSpace < 0) {
            throw new IllegalArgumentException("extraLayoutSpace should be >= 0");
        }
        mExtraLayoutSpace = extraLayoutSpace;
    }


    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if(mExtraLayoutSpace != -1) {
            return mExtraLayoutSpace;
        }
        return DEFAULT_EXTRA_LAYOUT_SPACE;
    }
}
