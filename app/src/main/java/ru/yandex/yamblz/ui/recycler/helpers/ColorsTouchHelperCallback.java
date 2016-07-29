package ru.yandex.yamblz.ui.recycler.helpers;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import ru.yandex.yamblz.ui.recycler.adapters.ColorsTouchHelperAdapter;

public class ColorsTouchHelperCallback extends ItemTouchHelper.Callback {

    private ColorsTouchHelperAdapter mAdapter;
    private ArgbEvaluator mEvaluator;
    private Paint mPaint;
    private boolean mDragging;
    private int mDraggedFrom, mDraggedTo;

    public ColorsTouchHelperCallback(ColorsTouchHelperAdapter adapter) {
        this.mAdapter = adapter;
        this.mEvaluator = new ArgbEvaluator();
        this.mPaint = new Paint();
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT
                | ItemTouchHelper.RIGHT, ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.e("ON MOVE" , viewHolder.getAdapterPosition() + " " + target.getAdapterPosition());
        mDraggedTo = target.getAdapterPosition();
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG && !mDragging) {
            mDragging = true;
            mAdapter.onItemsStartMove();
            mDraggedFrom = viewHolder.getAdapterPosition();
            mDraggedTo = mDraggedFrom;
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_IDLE && mDragging) {
            mDragging = false;
            notifyFinalMove();
        }
        Log.e("TAG", "ON SELECTED CHANGED " + actionState);
    }

    private void notifyFinalMove() {
        if(mDraggedFrom != mDraggedTo) {
            mAdapter.onItemsFinalMove(mDraggedFrom, mDraggedTo);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if(actionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
            return;
        }
        final View view = viewHolder.itemView;
        final float left = view.getLeft();
        final float top = view.getTop();
        final float height = view.getHeight();
        final float swipeDistance = getSwipeThreshold(viewHolder) * recyclerView.getWidth();
        mPaint.setColor((int)mEvaluator.evaluate(Math.min(dX / swipeDistance, 1f),
                Color.WHITE, Color.RED));
        //change color from white to red on swipe
        c.drawRect(left, top, left + dX, top + height, mPaint);
    }
}
