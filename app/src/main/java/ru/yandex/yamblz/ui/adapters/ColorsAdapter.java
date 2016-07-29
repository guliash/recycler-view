package ru.yandex.yamblz.ui.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.yandex.yamblz.R;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ContentHolder>
        implements ColorsTouchHelperAdapter {

    private final Random rnd = new Random();
    private final List<Integer> colors = new ArrayList<>();

    private int mMovedFrom = -1;
    private int mMovedTo = -1;

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
        holder.bind(createColorForPosition(position));
        //holder.itemView.animate().rotationXBy(360).setDuration(500).start();
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    private Integer randomColor() {
        return Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
    }

    private Integer createColorForPosition(int position) {
        while(position > colors.size()) {
            colors.add(-1);
        }
        if (position == colors.size()) {
            colors.add(randomColor());
        }
        return colors.get(position);
    }

    @Override
    public void onItemMove(int from, int to) {
        mMovedFrom = from;
        mMovedTo = to;
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
            setText("#".concat(Integer.toHexString(color).substring(2)));
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

    public int getLastMovedFromPosition() {
        return mMovedFrom;
    }

    public int getLastMovedToPosition() {
        return mMovedTo;
    }
}
