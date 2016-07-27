package ru.yandex.yamblz.ui.adapters;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {


    private ItemTouchHelperAdapter mAdapter;
    private ArgbEvaluator mEvaluator;
    private Paint mPaint;

    public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
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
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
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
