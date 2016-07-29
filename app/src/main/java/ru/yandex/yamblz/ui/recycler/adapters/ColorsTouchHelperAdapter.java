package ru.yandex.yamblz.ui.recycler.adapters;

public interface ColorsTouchHelperAdapter {

    void onItemMove(int from, int to);

    void onItemDismiss(int position);

    void onItemsFinalMove(int from, int to);

    void onItemsStartMove();
}
