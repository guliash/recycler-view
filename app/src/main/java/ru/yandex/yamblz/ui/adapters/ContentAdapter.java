package ru.yandex.yamblz.ui.adapters;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.yandex.yamblz.R;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder>
        implements ItemTouchHelperAdapter {

    private final Random rnd = new Random();
    private final List<Integer> colors = new ArrayList<>();

    private int mMovedFinalFrom = -1;
    private int mMovedFinalTo = -1;

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ContentHolder holder = new ContentHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.content_item, parent, false));

        holder.itemView.setOnClickListener((View view) -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                holder.changeBG(randomColor());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        Log.e("TAG", "BIND " + position + "");
        if (position == mMovedFinalFrom || position == mMovedFinalTo) {
            holder.highlight(Color.YELLOW);
            holder.bind(createColorForPosition(position));
        } else {
            Integer color = createColorForPosition(position);
            holder.highlight(color);
            holder.bind(color);
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    private Integer randomColor() {
        return Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
    }

    private Integer createColorForPosition(int position) {
        if (position >= colors.size()) {
            colors.add(randomColor());
        }
        return colors.get(position);
    }

    @Override
    public void onItemMove(int from, int to) {
        Collections.swap(colors, from, to);
        notifyItemMoved(from, to);
    }

    @Override
    public void onItemDismiss(int position) {
        colors.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemsFinalMove(int from, int to) {
    }

    @Override
    public void onItemsStartMove() {
    }

    static class ContentHolder extends RecyclerView.ViewHolder {

        private ObjectAnimator mBgAnimator;
        private ArgbEvaluator mArgbEvaluator;

        private static final long ANIM_DURATION = 500;

        ContentHolder(View itemView) {
            super(itemView);

            mBgAnimator = ObjectAnimator.ofInt(this.itemView, "backgroundColor", 0);
            mArgbEvaluator = new ArgbEvaluator();
            mBgAnimator.setEvaluator(mArgbEvaluator);

            mBgAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    bind(getBackgroundColor());
                    itemView.setClickable(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        void bind(Integer color) {
            ((TextView) itemView).setText("#".concat(Integer.toHexString(color).substring(2)));
        }

        void highlight(Integer color) {
            itemView.setBackgroundColor(color);
        }

        int getBackgroundColor() {
            return ((ColorDrawable) itemView.getBackground()).getColor();
        }

        void changeBG(Integer to) {
            itemView.setClickable(false);
            itemView.animate().rotationYBy(360).setDuration(ANIM_DURATION).start();
            mBgAnimator.setIntValues(getBackgroundColor(), to);
            mBgAnimator.setDuration(ANIM_DURATION).start();
        }
    }
}
