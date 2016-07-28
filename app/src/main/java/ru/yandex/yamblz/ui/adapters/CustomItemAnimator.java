package ru.yandex.yamblz.ui.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItemAnimator extends DefaultItemAnimator {

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
        final ContentAdapter.ContentHolder contentHolder = (ContentAdapter.ContentHolder)oldHolder;
        final ColorInfo preColorInfo = (ColorInfo)preInfo;
        final ColorInfo postColorInfo = (ColorInfo)postInfo;

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

        if(animatorsMap.containsKey(contentHolder)) {
            animatorsMap.get(contentHolder).cancel();
        }

        animatorsMap.put(contentHolder, wholeAnimator);

        wholeAnimator.start();



        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Log.e("TAG", "ANIMATE APPEARANCE " + holder.getAdapterPosition());
        return super.animateAdd(holder);
    }

    private class ColorInfo extends ItemHolderInfo {
        private int mColor;
        private String mText;

        @Override
        public ItemHolderInfo setFrom(RecyclerView.ViewHolder holder) {
            if(holder instanceof ContentAdapter.ContentHolder) {
                ContentAdapter.ContentHolder contentHolder = (ContentAdapter.ContentHolder)holder;
                mColor = contentHolder.getBackgroundColor();
                mText = contentHolder.getText();
            }
            return super.setFrom(holder);
        }
    }
}
