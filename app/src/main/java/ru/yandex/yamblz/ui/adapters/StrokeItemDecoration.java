package ru.yandex.yamblz.ui.adapters;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class StrokeItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Path mPath;

    private int mStrokeWidth, mHalfStrokeWidth;
    private int mStrokeColor;
    public StrokeItemDecoration(int strokeWidth, int strokeColor) {
        this.mStrokeWidth = strokeWidth;
        this.mHalfStrokeWidth = mStrokeWidth / 2;
        this.mStrokeColor = strokeColor;

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mStrokeColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mPath = new Path();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(isItemDecorated(parent.getChildAdapterPosition(view))) {
            outRect.set(mStrokeWidth, mStrokeWidth, mStrokeWidth, mStrokeWidth);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if(isItemDecorated(parent.getChildAdapterPosition(child))) {
                drawStrokeForChild(c, child);
            }
        }
    }

    private void drawStrokeForChild(Canvas canvas, View child) {
        final float top = child.getTop();
        final float left = child.getLeft();
        final float width = child.getWidth();
        final float height = child.getHeight();

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

    private boolean isItemDecorated(int adapterPos) {
        return adapterPos % 2 == 0;
    }
}
