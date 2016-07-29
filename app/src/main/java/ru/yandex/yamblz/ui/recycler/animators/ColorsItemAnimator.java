package ru.yandex.yamblz.ui.recycler.animators;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.yandex.yamblz.ui.recycler.adapters.ColorsAdapter;

public class ColorsItemAnimator extends DefaultItemAnimator {

    private static final long COLOR_DURATION = 1000;

    Map<RecyclerView.ViewHolder, Animator> animatorsMap = new HashMap<>();

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags, @NonNull List<Object> payloads) {
        ColorInfo colorInfo = new ColorInfo();
        colorInfo.setFrom(viewHolder);
        return colorInfo;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPostLayoutInformation(@NonNull RecyclerView.State state,
                                                      @NonNull RecyclerView.ViewHolder viewHolder) {
        ColorInfo colorInfo = new ColorInfo();
        colorInfo.setFrom(viewHolder);
        return colorInfo;
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder,
                                 @NonNull RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo, @NonNull ItemHolderInfo postInfo) {
        Log.e("TAG", "ANIMATE CHANGE");

        final ColorsAdapter.ContentHolder contentHolder = (ColorsAdapter.ContentHolder)oldHolder;
        final ColorInfo preColorInfo = (ColorInfo)preInfo;
        final ColorInfo postColorInfo = (ColorInfo)postInfo;

        cancelAnimations(contentHolder);

        ObjectAnimator colorAnim = ObjectAnimator.ofInt(contentHolder.itemView,
                "backgroundColor", preColorInfo.mColor, postColorInfo.mColor);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(COLOR_DURATION);

        ObjectAnimator firstRotateAnim = ObjectAnimator.ofFloat(contentHolder.itemView, View.ROTATION_Y,
                0, 90);
        ObjectAnimator secondRotateAnim = ObjectAnimator.ofFloat(contentHolder.itemView, View.ROTATION_Y,
                -90, 0);

        firstRotateAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                contentHolder.setText(preColorInfo.mText);
            }
        });

        firstRotateAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                contentHolder.setText(postColorInfo.mText);
            }
        });

        AnimatorSet rotateSet = new AnimatorSet();
        rotateSet.playSequentially(firstRotateAnim, secondRotateAnim);
        rotateSet.setDuration(COLOR_DURATION >> 1);

        AnimatorSet wholeAnimator = new AnimatorSet();
        wholeAnimator.playTogether(rotateSet, colorAnim);

        wholeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorsMap.remove(contentHolder);
                dispatchAnimationFinished(contentHolder);
            }
        });

        animatorsMap.put(contentHolder, wholeAnimator);

        wholeAnimator.start();

        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Log.e("TAG", "ANIMATE ADD");
        return super.animateAdd(holder);
    }

    @Override
    public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, @Nullable ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        Log.e("TAG", "ANIMATE APPEARANCE");
        return super.animateAppearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        Log.e("TAG", "ANIMATE MOVE");
        return super.animateMove(holder, fromX, fromY, toX, toY);
    }

    @Override
    public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
        Log.e("TAG", "ANIMATE DISAPPEARANCE");
        return super.animateDisappearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    private void cancelAnimations(RecyclerView.ViewHolder holder) {
        Animator animator = animatorsMap.get(holder);
        if(animator != null) {
            animator.cancel();
        }
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        cancelAnimations(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
        for(Animator animator : animatorsMap.values()) {
            animator.cancel();
        }
    }

    private class ColorInfo extends ItemHolderInfo {
        private int mColor;
        private String mText;

        @Override
        public ItemHolderInfo setFrom(RecyclerView.ViewHolder holder) {
            if(holder instanceof ColorsAdapter.ContentHolder) {
                ColorsAdapter.ContentHolder contentHolder = (ColorsAdapter.ContentHolder)holder;
                mColor = contentHolder.getBackgroundColor();
                mText = contentHolder.getText();
            }
            return super.setFrom(holder);
        }
    }
}
