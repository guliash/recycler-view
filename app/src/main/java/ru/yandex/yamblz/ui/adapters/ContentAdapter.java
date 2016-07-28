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

import org.w3c.dom.Text;

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
                colors.set(adapterPos, randomColor());
                notifyItemChanged(adapterPos);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        Log.e("TAG", "ON BIND VIEW HOLDER " + position);
        holder.bind(createColorForPosition(position));
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

        ContentHolder(View itemView) {
            super(itemView);
        }

        void bind(Integer color) {
            setBackgroundColor(color);
            ((TextView) itemView).setText("#".concat(Integer.toHexString(color).substring(2)));
        }

        public void setBackgroundColor(int color) {
            itemView.setBackgroundColor(color);
        }

        public int getBackgroundColor() {
            return ((ColorDrawable)itemView.getBackground()).getColor();
        }

        public void setText(String text) {
            ((TextView)itemView).setText(text);
        }

        public String getText() {
            return ((TextView)itemView).getText().toString();
        }
    }
}
