package ru.yandex.yamblz.ui.recycler.decorators;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.yandex.yamblz.ui.recycler.adapters.ColorsAdapter;

public class ColorsItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Path mPath;

    private final int mStrokeWidth, mHalfStrokeWidth;
    private final int mStrokeColor;
    private final int mHighlightStrokeColor;

    /**
     * Constructor
     * @param strokeWidth stroke width
     * @param strokeColor stroke color
     * @param highlightStrokeColor highlight stroke color (needed when move is highlighted)
     */
    public ColorsItemDecoration(int strokeWidth, int strokeColor, int highlightStrokeColor) {
        this.mStrokeWidth = strokeWidth;
        this.mHalfStrokeWidth = mStrokeWidth / 2;
        this.mStrokeColor = strokeColor;
        this.mHighlightStrokeColor = highlightStrokeColor;

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mPath = new Path();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int adapterPos = parent.getChildAdapterPosition(view);
        if(isItemDecorated(adapterPos) || isItemHighlighted((ColorsAdapter)parent.getAdapter(), adapterPos)) {
            outRect.set(mStrokeWidth, mStrokeWidth, mStrokeWidth, mStrokeWidth);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        for(int layoutPos = 0; layoutPos < childCount; layoutPos++) {
            final View child = parent.getChildAt(layoutPos);
            final int adapterPos = parent.getChildAdapterPosition(child);
            if(isItemHighlighted((ColorsAdapter)parent.getAdapter(), adapterPos)) {
                drawStrokeForChild(c, child, mHighlightStrokeColor);
            } else if(isItemDecorated(adapterPos)) {
                drawStrokeForChild(c, child, mStrokeColor);
            }
        }
    }

    /**
     * Draws stroke for a child
     * @param canvas canvas to draw at
     * @param child the child
     * @param strokeColor stroke color
     */
    private void drawStrokeForChild(Canvas canvas, View child, int strokeColor) {
        final float top = child.getTop();
        final float left = child.getLeft();
        final float width = child.getWidth();
        final float height = child.getHeight();

        mPaint.setColor(strokeColor);
        mPath.reset();

        mPath.moveTo(left - mStrokeWidth, top - mHalfStrokeWidth);
        mPath.lineTo(left + width, top - mHalfStrokeWidth);
        mPath.moveTo(left + width + mHalfStrokeWidth, top - mStrokeWidth);
        mPath.lineTo(left + width + mHalfStrokeWidth, top + height);
        mPath.moveTo(left + width + mStrokeWidth, top + height + mHalfStrokeWidth);
        mPath.lineTo(left, top + height + mHalfStrokeWidth);
        mPath.moveTo(left - mHalfStrokeWidth, top + height + mStrokeWidth);
        mPath.lineTo(left - mHalfStrokeWidth, top);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

    /**
     * Checks whether item at the position should be decorated
     * @param adapterPos adapter position of the item
     * @return {@code true} if item decorated
     */
    private boolean isItemDecorated(int adapterPos) {
        return adapterPos % 2 == 0;
    }

    /**
     * Should item be highlighted?
     * @param colorsAdapter colors adapter to ask from
     * @param adapterPos adapter position of item
     * @return {@code true} if item is highlighted
     */
    private boolean isItemHighlighted(ColorsAdapter colorsAdapter, int adapterPos) {
        return colorsAdapter.getLastMovedFromPosition() == adapterPos ||
                colorsAdapter.getLastMovedToPosition() == adapterPos;
    }

}
